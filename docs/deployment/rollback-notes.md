# Rollback Notes — SCRUM-179

## Trigger Conditions
Rollback if:
- App fails health checks
- Accessibility smoke tests fail in staging
- Critical keyboard navigation regression is found
- Screen reader support is broken in a release candidate

## Rollback Approach
1. Revert deployment to the last stable container image.
2. Restore previous frontend build artifact.
3. Confirm `/health` and homepage return OK.
4. Re-run smoke checks and accessibility spot checks.

## Validation After Rollback
- Home page loads
- Skip link visible on focus
- Navigation is usable by keyboard
- Modal focus trap behaves correctly
- No new critical console/build errors
