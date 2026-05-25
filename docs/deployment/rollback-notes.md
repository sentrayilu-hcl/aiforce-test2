# Rollback Notes

If deployment health checks or smoke tests fail:

1. Inspect rollout:
```bash
kubectl describe deployment aiforce-test2 -n aiforce-test2
kubectl get events -n aiforce-test2 --sort-by=.metadata.creationTimestamp
```

2. Roll back:
```bash
kubectl rollout undo deployment/aiforce-test2 -n aiforce-test2
```

3. Re-run smoke tests after rollback.

## Common rollback triggers
- Unhealthy readiness/liveness probe
- Database connection failure
- Misconfigured secret or JWT key
- Regression in login or session validation
