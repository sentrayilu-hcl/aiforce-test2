#!/usr/bin/env bash
set -euo pipefail

export BASE_URL="${BASE_URL:-http://localhost:8080}"
export TEST_USERNAME="${TEST_USERNAME:-customer1}"
export TEST_PASSWORD="${TEST_PASSWORD:-ChangeMe123!}"
export TEST_INVALID_PASSWORD="${TEST_INVALID_PASSWORD:-WrongPassword123!}"
export SESSION_WAIT_SECONDS="${SESSION_WAIT_SECONDS:-5}"

echo "Running auth API automation against ${BASE_URL}"
mvn clean test
