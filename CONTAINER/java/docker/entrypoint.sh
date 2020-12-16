#!/usr/bin/env bash

mvn install

cd /app
exec "${@}"