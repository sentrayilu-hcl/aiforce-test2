#!/bin/sh
set -e

if [ -n "$APP_BASE_URL" ]; then
  echo "APP_BASE_URL=${APP_BASE_URL}"
fi

exec "$@"
