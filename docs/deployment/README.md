# SCRUM-179 Deployment Guide

## Overview
This deployment package supports the React + TypeScript accessibility improvements for SCRUM-179.

## Runtime
- Docker container runtime
- Nginx serves the production build
- SPA fallback configured for client-side routing

## Local Run
```bash
cp deploy/env/.env.example .env
docker compose up --build
```

## Health Check
- `GET /health` returns `200 OK`

## Smoke Validation
```bash
chmod +x deploy/scripts/smoke-test.sh
BASE_URL=http://localhost:8080 deploy/scripts/smoke-test.sh
```

## Validation Gates
- Unit tests pass
- Accessibility tests pass
- Build succeeds
- Container starts successfully
- Health endpoint returns OK
- Smoke test passes

## Rollback
See `docs/deployment/rollback-notes.md`.
