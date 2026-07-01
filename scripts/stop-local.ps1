$ErrorActionPreference = 'Continue'

Write-Host 'Stopping Docker infrastructure...'
docker compose down

Write-Host 'Stopping Java and Node processes started for local run...'
Get-Process java,node -ErrorAction SilentlyContinue | Stop-Process -Force -ErrorAction SilentlyContinue

Write-Host 'Local stack stop command completed.'
