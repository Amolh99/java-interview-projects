# Module 23 - Build Tools, CI/CD & Git

A Maven project with profiles, a CI pipeline in GitHub Actions, and the Git workflow used to ship it.

## Files
- `pom.xml`: `dev` and `ci` profiles. Compiler warnings are errors in CI.
- `.github/workflows/ci.yml`: copy this to the repo root `.github/workflows/` to activate it. It runs build, tests, and packaging on every pull request.
- `src/`: a small `OrderTotals` utility with tests.

## Git workflow
```bash
git switch -c feature/add-tax          # branch from main
git commit -m "Add tax rounding"       # small, focused commits
git fetch origin && git rebase origin/main   # keep history linear
git push -u origin feature/add-tax     # open a PR
git revert <sha>                       # undo a merged commit safely
```

## Interview questions
1. **Merge vs rebase?** Merge keeps history as it happened. Rebase rewrites your branch so it sits on top of the target. Never rebase shared branches.
2. **`git reset` vs `git revert`?** Reset moves the branch pointer and can discard history. Revert adds a new commit that undoes a change.
3. **Maven lifecycle?** `validate`, `compile`, `test`, `package`, `verify`, `install`, `deploy`.
4. **What is a Maven profile?** A set of build settings activated by environment or flag.
5. **Why cache dependencies in CI?** It avoids re-downloading the same jars on every run.

## Run
```bash
mvn -Pci verify
```
