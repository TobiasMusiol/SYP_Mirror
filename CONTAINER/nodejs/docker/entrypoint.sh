#!/usr/bin/env bash
if [ ! -d /worker/node_modules ]; then
    cd /worker
    npm install
fi

cd /worker
exec "${@}"