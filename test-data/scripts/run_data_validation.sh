#!/usr/bin/env bash
set -euo pipefail

echo "Validating test data fixtures for US-001"
for f in test-data/json/*.json; do
  echo "Checking $f"
  jq empty "$f"
done
echo "Validation complete."
