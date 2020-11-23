#!/usr/bin/env bash

if [ ! -d /app ]; then
    cd /app
    npm install
fi

cd /app
exec "${@}"