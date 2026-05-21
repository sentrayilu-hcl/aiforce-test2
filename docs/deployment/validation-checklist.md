# SCRUM-179 Validation Checklist

## Pre-deployment
- [ ] Code review approved
- [ ] Unit tests pass
- [ ] Accessibility tests pass
- [ ] Build artifact generated
- [ ] Docker image built successfully

## Staging validation
- [ ] `/health` returns 200
- [ ] Homepage loads
- [ ] Skip link works
- [ ] Keyboard navigation verified
- [ ] Modal focus trap verified
- [ ] Form labels and errors announced
- [ ] Toast announcements verified
- [ ] Playwright smoke tests pass

## Release approval
- [ ] QA signoff obtained
- [ ] Rollback plan confirmed
- [ ] Release notes published
