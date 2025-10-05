# Contributing Guide

Quick reference for team collaboration.

## Workflow

```bash
# 1. Start working
git checkout dev
git pull origin dev
git checkout -b feature/your-feature-name  # or checkout existing branch

# 2. Make changes and commit
git add .
git commit -m "feat: Add student login validation"
git push origin feature/your-feature-name

# 3. Create PR on GitHub
# feature/your-feature-name → dev
# Add description, request review, wait for approval, merge
```

## Commit Message Format

```
<type>: <description>

Types:
- feat:     New feature
- fix:      Bug fix
- refactor: Code restructuring
- docs:     Documentation
- style:    Formatting (no logic change)
```

**Examples:**
```bash
git commit -m "feat: Add course eligibility validation"
git commit -m "fix: Resolve null pointer in enrollment"
git commit -m "refactor: Extract email service into separate class"
```

## Branch Protection

`main` and `dev` are protected - you **cannot** push directly.

```bash
# ❌ This will fail
git push origin main
git push origin dev

# ✅ Do this instead
git push origin feature/your-feature-name  # Then create PR
```

## Code Review Checklist

Before approving a PR, check:

- [ ] Follows naming conventions (camelCase methods, PascalCase classes)
- [ ] No magic numbers (use constants)
- [ ] Null checks on inputs
- [ ] No empty catch blocks
- [ ] Methods are short and focused
- [ ] Meaningful variable names
- [ ] Code tested locally

## Handling Merge Conflicts

```bash
# When conflict occurs
git status  # See which files have conflicts

# Open file, look for:
# <<<<<<< HEAD
# Your changes
# =======
# Their changes
# >>>>>>> branch-name

# Edit to keep correct code, remove markers
git add .
git commit -m "Resolve merge conflict in Student.java"
git push origin feature/your-feature-name
```

## Common Issues

**"Branch protection" error?**
→ You tried pushing to `main`/`dev`. Push to your feature branch instead.

**Merge conflict?**
→ Follow steps above. Ask teammate if unsure which code to keep.

**Forgot to pull before starting?**
→ `git stash` → `git pull` → `git stash pop`

## DON'Ts

- ❌ Push directly to `main` or `dev`
- ❌ Commit without pulling first
- ❌ Use vague messages ("update", "fix")
- ❌ Ignore merge conflicts
- ❌ Commit `.class` files (in `.gitignore`)

## DOs

- ✅ Work on feature branches
- ✅ Pull from `dev` before starting
- ✅ Write clear commit messages
- ✅ Test locally before PR
- ✅ Review teammates' code thoughtfully

---

See [CODING_STANDARDS.md](CODING_STANDARDS.md) for Java conventions
