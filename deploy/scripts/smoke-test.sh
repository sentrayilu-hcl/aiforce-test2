#!/bin/sh
set -e

BASE_URL="${BASE_URL:-http://localhost:8080}"

echo "Running smoke checks against ${BASE_URL}"

curl -fsS "${BASE_URL}/health" >/dev/null
curl -fsS "${BASE_URL}/" >/dev/null

echo "Smoke checks passed"
