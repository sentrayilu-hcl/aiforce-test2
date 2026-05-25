#!/usr/bin/env bash
set -euo pipefail

BASE_URL="${BASE_URL:-http://localhost:8080}"
TEST_USERNAME="${TEST_USERNAME:-customer1}"
TEST_PASSWORD="${TEST_PASSWORD:-ChangeMe123!}"

echo "Running smoke test against ${BASE_URL}"

LOGIN_RESPONSE=$(curl -s -w "\n%{http_code}" -X POST "${BASE_URL}/api/auth/login" \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"${TEST_USERNAME}\",\"password\":\"${TEST_PASSWORD}\"}")

LOGIN_BODY=$(echo "$LOGIN_RESPONSE" | head -n 1)
LOGIN_STATUS=$(echo "$LOGIN_RESPONSE" | tail -n 1)

if [ "$LOGIN_STATUS" != "200" ]; then
  echo "Login failed with status $LOGIN_STATUS"
  exit 1
fi

ACCESS_TOKEN=$(echo "$LOGIN_BODY" | jq -r '.accessToken')
SESSION_ID=$(echo "$LOGIN_BODY" | jq -r '.sessionId')

if [ -z "$ACCESS_TOKEN" ] || [ "$ACCESS_TOKEN" = "null" ]; then
  echo "Missing access token"
  exit 1
fi

VALIDATE_STATUS=$(curl -s -o /dev/null -w "%{http_code}" \
  -H "X-Session-Id: ${SESSION_ID}" \
  "${BASE_URL}/api/auth/session/validate")

if [ "$VALIDATE_STATUS" != "200" ]; then
  echo "Session validation failed with status $VALIDATE_STATUS"
  exit 1
fi

PROTECTED_STATUS=$(curl -s -o /dev/null -w "%{http_code}" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}" \
  "${BASE_URL}/api/protected/account")

if [ "$PROTECTED_STATUS" != "200" ]; then
  echo "Protected endpoint access failed with status $PROTECTED_STATUS"
  exit 1
fi

LOGOUT_STATUS=$(curl -s -o /dev/null -w "%{http_code}" \
  -X POST -H "X-Session-Id: ${SESSION_ID}" \
  "${BASE_URL}/api/auth/logout")

if [ "$LOGOUT_STATUS" != "200" ]; then
  echo "Logout failed with status $LOGOUT_STATUS"
  exit 1
fi

echo "Smoke test passed"
