FROM postgres

RUN apt update && \
  apt dist-upgrade -y
RUN apt install -y vim nano
RUN apt autoremove -y

# Env Variablen
#ENV POSTGRES_PASSWORD=admin_password

COPY ./initDB.sql /docker-entrypoint-initdb.d/initDB.sql
COPY ./entrypoint.sh /docker-entrypoint-initdb.d/entrypoint.sh
