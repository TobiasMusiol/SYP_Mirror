#!/usr/bin/env bash

if [[ ! -f /app/target/iot_etage-0.0.1-SNAPSHOT.jar ]]; then
    mvn clean package
fi

cd /app
exec "${@}"