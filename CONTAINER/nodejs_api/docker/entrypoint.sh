#!/usr/bin/env bash
if [ ! -d /api/node_modules ]; then
    cd /api
    npm install
fi

cd /api
exec "${@}"