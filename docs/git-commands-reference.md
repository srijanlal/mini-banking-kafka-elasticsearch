# Git Commands Reference

A practical guide to Git commands used day-to-day in professional development.

---

## 1. Setup (one-time per machine)

```bash
git config --global user.name "Your Name"
git config --global user.email "you@example.com"
git config --global core.editor "code --wait"   # use VS Code as editor
```

---

## 2. Starting a Repository

```bash
# Initialize a new local repo
git init

# Clone an existing remote repo
git clone https://github.com/user/repo.git

# Clone into a specific folder name
git clone https://github.com/user/repo.git my-folder
```

---

## 3. Daily Workflow

```bash
# Check what has changed
git status

# See line-by-line changes (unstaged)
git diff

# See line-by-line changes (staged)
git diff --staged

# Stage a specific file
git add src/MyFile.java

# Stage all changes
git add .

# Commit staged changes
git commit -m "feat: add account search endpoint"

# Stage + commit tracked files in one step (skips untracked new files)
git commit -am "fix: handle empty query string"
```

---

## 4. Branching

```bash
# List all local branches
git branch

# List all branches including remote
git branch -a

# Create a new branch
git branch feature/account-click

# Switch to a branch
git checkout feature/account-click

# Create AND switch in one command (most common)
git checkout -b feature/account-click

# Modern equivalent (Git 2.23+)
git switch -c feature/account-click

# Delete a local branch (safe — only if merged)
git branch -d feature/account-click

# Force delete (unmerged branch)
git branch -D feature/account-click
```

---

## 5. Remote Repositories

```bash
# See configured remotes
git remote -v

# Add a remote
git remote add origin https://github.com/user/repo.git

# Rename a remote
git remote rename origin upstream

# Remove a remote
git remote remove origin

# Push branch to remote and track it
git push -u origin feature/account-click

# Push to already-tracked remote
git push

# Pull latest changes (fetch + merge)
git pull

# Fetch without merging (safe look)
git fetch origin
```

---

## 6. Merging & Rebasing

```bash
# Merge another branch into current branch
git merge feature/account-click

# Rebase current branch onto main (cleaner history)
git rebase main

# Interactive rebase — squash/edit last 3 commits
git rebase -i HEAD~3

# Abort an in-progress rebase
git rebase --abort

# Continue after resolving conflicts
git rebase --continue
```

### Merge vs Rebase (quick rule)
| Scenario | Use |
|---|---|
| Merging a feature PR into `main` | `merge` |
| Syncing your feature branch with latest `main` | `rebase` |
| Cleaning up messy commits before PR | `rebase -i` |

---

## 7. Undoing Things

```bash
# Unstage a file (keep changes in working dir)
git restore --staged src/MyFile.java

# Discard changes in working directory (IRREVERSIBLE)
git restore src/MyFile.java

# Amend the last commit message (before push only)
git commit --amend -m "corrected message"

# Undo last commit but keep changes staged
git reset --soft HEAD~1

# Undo last commit and unstage changes (keep files)
git reset HEAD~1

# Undo last commit and DISCARD all changes (DESTRUCTIVE)
git reset --hard HEAD~1

# Revert a pushed commit safely (creates a new commit)
git revert <commit-hash>
```

---

## 8. Stashing

Used when you need to switch branches but aren't ready to commit.

```bash
# Stash current uncommitted changes
git stash

# Stash with a descriptive name
git stash push -m "WIP: kafka producer retry logic"

# List all stashes
git stash list

# Apply most recent stash (keep it in stash list)
git stash apply

# Apply AND remove from stash list
git stash pop

# Apply a specific stash
git stash apply stash@{2}

# Drop a specific stash
git stash drop stash@{0}

# Clear all stashes
git stash clear
```

---

## 9. Viewing History

```bash
# Full log
git log

# Compact one-line log
git log --oneline

# Graph view of branches
git log --oneline --graph --all

# Log for a specific file
git log --oneline src/MyFile.java

# Show what changed in a specific commit
git show <commit-hash>

# Find who changed a specific line (blame)
git blame src/MyFile.java
```

---

## 10. Tags (for releases)

```bash
# Create a lightweight tag
git tag v1.0.0

# Create an annotated tag (recommended for releases)
git tag -a v1.0.0 -m "Initial release"

# List tags
git tag

# Push tags to remote
git push origin --tags

# Delete a tag locally
git tag -d v1.0.0

# Delete a tag remotely
git push origin --delete v1.0.0
```

---

## 11. Useful Shortcuts

```bash
# See a compact status
git status -s

# Show files changed between two branches
git diff main..feature/account-click --name-only

# Cherry-pick a single commit onto current branch
git cherry-pick <commit-hash>

# Find the commit that introduced a bug (binary search)
git bisect start
git bisect bad                  # current commit is broken
git bisect good <commit-hash>   # this commit was fine
# Git checks out middle commits; mark each as good/bad until found
git bisect reset                # done

# Clean untracked files (dry run first)
git clean -n
git clean -fd
```

---

## 12. Typical Feature Branch Workflow (Day-to-Day)

```bash
# 1. Start from latest main
git checkout main
git pull

# 2. Create feature branch
git checkout -b feature/recent-search-consumer

# 3. Write code, then stage and commit
git add .
git commit -m "feat: implement Kafka consumer for recent search"

# 4. Keep branch up to date with main
git fetch origin
git rebase origin/main

# 5. Push branch
git push -u origin feature/recent-search-consumer

# 6. After PR is merged, clean up
git checkout main
git pull
git branch -d feature/recent-search-consumer
```

---

## 13. Conventional Commit Message Format

Most teams follow this standard:

```
<type>(<scope>): <short description>

[optional body]

[optional footer]
```

| Type | When to use |
|---|---|
| `feat` | New feature |
| `fix` | Bug fix |
| `refactor` | Code change with no feature/fix |
| `test` | Adding or updating tests |
| `docs` | Documentation only |
| `chore` | Build process, dependencies |
| `perf` | Performance improvement |
| `ci` | CI/CD config changes |

**Examples:**
```
feat(acctsearch): add DLT fallback to Kafka producer
fix(controller): return 400 for blank search query
test(mapper): add null field coverage to AccountMapperTest
chore: upgrade Spring Boot to 3.3.6
```

---

## 14. .gitignore Tips

```gitignore
# Java / Maven
target/
*.class
*.jar

# IDE
.idea/
.vscode/
*.iml

# Frontend
node_modules/
dist/
.angular/

# Logs & OS
*.log
logs/
.DS_Store
Thumbs.db
```

> Run `git rm -r --cached .` after updating `.gitignore` to un-track already-committed files.
