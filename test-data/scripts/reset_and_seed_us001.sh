#!/usr/bin/env bash
set -euo pipefail

DB_URL="${DB_URL:-jdbc:postgresql://localhost:5432/aiforce}"
DB_USER="${DB_USER:-aiforce}"

if [ -z "${DB_PASSWORD:-}" ]; then
  echo "DB_PASSWORD must be provided via environment variable."
  exit 1
fi

export PGPASSWORD="$DB_PASSWORD"
echo "Resetting and seeding synthetic auth test data..."
psql "$DB_URL" -U "$DB_USER" -f test-data/scripts/cleanup_us001_auth_data.sql
psql "$DB_URL" -U "$DB_USER" -f test-data/sql/seed_us001_auth_data.sql
echo "Done."
