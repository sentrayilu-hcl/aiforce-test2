# Deployment Guide - US-001

Traceability:
BN-01 -> BR-001 -> EPIC-001 -> US-001

## Prerequisites
- Kubernetes cluster
- kubectl configured
- PostgreSQL available
- Container image published
- Spring Boot app built with actuator health endpoint enabled

## Deploy
```bash
kubectl apply -f deploy/k8s/namespace.yaml
kubectl apply -f deploy/k8s/configmap.yaml
kubectl apply -f deploy/k8s/secret-template.yaml
kubectl apply -f deploy/k8s/service.yaml
kubectl apply -f deploy/k8s/pdb.yaml
kubectl apply -f deploy/k8s/hpa.yaml
kubectl apply -f deploy/k8s/deployment.yaml
kubectl apply -f deploy/k8s/ingress.yaml
```

## Validate
```bash
kubectl get pods -n aiforce-test2
kubectl rollout status deployment/aiforce-test2 -n aiforce-test2
bash scripts/smoke-test/auth-session-smoke.sh
```

## Rollback
```bash
kubectl rollout undo deployment/aiforce-test2 -n aiforce-test2
kubectl rollout status deployment/aiforce-test2 -n aiforce-test2
```
