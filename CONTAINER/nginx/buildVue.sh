#!/bin/sh

if [ ! -d /app/prod/dist ]; then
  echo "--- Build Vue ---"
  cd /app/vue-dev
  npm run build
  cp -rf ./dist /app/prod
  echo "-- Builded Vue ---"
fi