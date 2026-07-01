[CmdletBinding()]
param(
	[string]$JavaHome = 'C:\Users\Srijan Lal\.jdks\ms-21.0.7',
	[switch]$WithKibana,
	[switch]$AllowBusyPorts
)

$ErrorActionPreference = 'Stop'

$repoRoot = (Resolve-Path (Join-Path $PSScriptRoot '..')).Path
$runId = Get-Date -Format 'yyyyMMdd-HHmmss'
$logDir = Join-Path $repoRoot 'scripts\logs'
New-Item -ItemType Directory -Path $logDir -Force | Out-Null

$recentLog = Join-Path $logDir "recentsearch-$runId.log"
$acctLog = Join-Path $logDir "acctsearch-$runId.log"
$uiLog = Join-Path $logDir "frontend-$runId.log"
$acctPort = 8100
$recentPort = 8200
$uiPort = 4200
$kafkaPort = 9094

function Get-PortListener {
	param([int]$Port)
	$listener = Get-NetTCPConnection -LocalPort $Port -State Listen -ErrorAction SilentlyContinue | Select-Object -First 1
	if ($null -eq $listener) {
		return $null
	}

	$procName = '<unknown>'
	try {
		$procName = (Get-Process -Id $listener.OwningProcess -ErrorAction Stop).ProcessName
	}
	catch {
		# Ignore process lookup failures and keep fallback name.
	}

	return [pscustomobject]@{
		Port = $Port
		Pid = $listener.OwningProcess
		ProcessName = $procName
	}
}

function Assert-PortFree {
	param(
		[int]$Port,
		[string]$ServiceName,
		[switch]$AllowBusy
	)

	$listener = Get-PortListener -Port $Port
	if ($null -eq $listener) {
		return
	}

	$message = "Port $Port is already in use by $($listener.ProcessName) (PID $($listener.Pid)). $ServiceName may not start, and health checks can point to an old process."
	if ($AllowBusy) {
		Write-Warning $message
		return
	}

	throw "$message Use scripts/stop-local.ps1 or rerun with -AllowBusyPorts if this is intentional."
}

function Wait-HttpHealthy {
	param(
		[string]$Name,
		[string]$Url,
		[int]$TimeoutSeconds = 120,
		[scriptblock]$IsHealthy = { param($response) $response.status -eq 'UP' }
	)

	$deadline = (Get-Date).AddSeconds($TimeoutSeconds)
	while ((Get-Date) -lt $deadline) {
		try {
			$response = Invoke-RestMethod -Uri $Url -TimeoutSec 3 -ErrorAction Stop
			if (& $IsHealthy $response) {
				Write-Host "$Name is healthy at $Url"
				return
			}
		}
		catch {
			# Keep polling until timeout.
		}

		Start-Sleep -Seconds 2
	}

	throw "$Name did not become healthy within $TimeoutSeconds seconds at $Url"
}

function Show-LogTail {
	param(
		[string]$LogPath,
		[string]$Name,
		[int]$LineCount = 80
	)

	if (-not (Test-Path $LogPath)) {
		Write-Warning "$Name log not found at $LogPath"
		return
	}

	Write-Host "----- Last $LineCount lines from $Name log: $LogPath -----"
	Get-Content -Path $LogPath -Tail $LineCount -ErrorAction SilentlyContinue
	Write-Host '----- End of log tail -----'
}

function Wait-PortListening {
	param(
		[string]$Name,
		[int]$Port,
		[int]$TimeoutSeconds = 90
	)

	$deadline = (Get-Date).AddSeconds($TimeoutSeconds)
	while ((Get-Date) -lt $deadline) {
		if ($null -ne (Get-PortListener -Port $Port)) {
			Write-Host "$Name is reachable on port $Port"
			return
		}

		Start-Sleep -Seconds 2
	}

	throw "$Name did not open port $Port within $TimeoutSeconds seconds"
}

if (-not (Test-Path $JavaHome)) {
	throw "JAVA_HOME path not found: $JavaHome"
}

Assert-PortFree -Port $acctPort -ServiceName 'acctsearch-service' -AllowBusy:$AllowBusyPorts
Assert-PortFree -Port $recentPort -ServiceName 'recentsearch-service' -AllowBusy:$AllowBusyPorts
Assert-PortFree -Port $uiPort -ServiceName 'Angular UI' -AllowBusy:$AllowBusyPorts

Set-Location $repoRoot
Write-Host 'Starting infrastructure (Kafka, Elasticsearch)...'
if ($WithKibana) {
	docker compose --profile observability up -d
}
else {
	docker compose up -d
}

Write-Host 'Waiting for infrastructure readiness...'
Wait-PortListening -Name 'Kafka external listener' -Port $kafkaPort
Wait-HttpHealthy -Name 'Elasticsearch' -Url 'http://localhost:9200/_cluster/health' -IsHealthy { param($response) $null -ne $response.status }

$recentCmd = @"
`$env:JAVA_HOME='$JavaHome'
`$env:Path='$JavaHome\bin;' + `$env:Path
Set-Location '$repoRoot\recentsearch-service'
mvn "spring-boot:run" "-Dspring-boot.run.profiles=local" "-Dspring-boot.run.arguments=--server.port=$recentPort" 2>&1 | Tee-Object -FilePath '$recentLog' -Append
"@

$acctCmd = @"
`$env:JAVA_HOME='$JavaHome'
`$env:Path='$JavaHome\bin;' + `$env:Path
Set-Location '$repoRoot\acctsearch-service'
mvn "spring-boot:run" "-Dspring-boot.run.profiles=local" "-Dspring-boot.run.arguments=--server.port=$acctPort" 2>&1 | Tee-Object -FilePath '$acctLog' -Append
"@

$uiCmd = @"
Set-Location '$repoRoot\frontend\bank-ui'
npm start 2>&1 | Tee-Object -FilePath '$uiLog' -Append
"@

Write-Host 'Launching recentsearch-service in a new terminal (port 8200)...'
$recentProc = Start-Process powershell -ArgumentList '-NoExit', '-Command', $recentCmd -PassThru

Write-Host 'Launching acctsearch-service in a new terminal (port 8100)...'
$acctProc = Start-Process powershell -ArgumentList '-NoExit', '-Command', $acctCmd -PassThru

Write-Host 'Launching Angular frontend in a new terminal (port 4200)...'
$uiProc = Start-Process powershell -ArgumentList '-NoExit', '-Command', $uiCmd -PassThru

Write-Host 'Waiting for Spring Boot actuator health endpoints...'
try {
	Wait-HttpHealthy -Name 'recentsearch-service' -Url "http://localhost:$recentPort/actuator/health" -TimeoutSeconds 180
}
catch {
	Show-LogTail -LogPath $recentLog -Name 'recentsearch-service'
	throw
}

try {
	Wait-HttpHealthy -Name 'acctsearch-service' -Url "http://localhost:$acctPort/actuator/health" -TimeoutSeconds 180
}
catch {
	Show-LogTail -LogPath $acctLog -Name 'acctsearch-service'
	throw
}

Write-Host 'All processes have been started in separate terminals.'
Write-Host "Terminal PIDs: recentsearch=$($recentProc.Id), acctsearch=$($acctProc.Id), ui=$($uiProc.Id)"
Write-Host "Logs: recentsearch=$recentLog"
Write-Host "      acctsearch=$acctLog"
Write-Host "      frontend=$uiLog"
Write-Host 'UI:           http://localhost:4200'
Write-Host 'acctsearch:   http://localhost:8100/actuator/health'
Write-Host 'recentsearch: http://localhost:8200/actuator/health'
