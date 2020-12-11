#!/usr/bin/env bash

service ssh start

cd /app
echo "--- Migrations: start ---"
python3 manage.py makemigrations
python3 manage.py showmigrations
python manage.py migrate IOT_ETAGE --fake
#python manage.py migrate IOT_ETAGE
python3 manage.py migrate
echo "-- Migrations: end ---"


echo "--- start WYSG-Server ---"
cd /app
exec "${@}"