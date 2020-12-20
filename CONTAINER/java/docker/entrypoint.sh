#!/usr/bin/env bash

if [[ ! -d /app/target || "${REBUILD}" == "true" ]]; then
    mvn clean install
fi

cd /app
exec "${@}"