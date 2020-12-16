SELECT 'CREATE DATABASE iot_etage_blau_db' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'iot_etage_blau_db')\gexec

--CREATE DATABASE iot_etage_blau_db;
CREATE USER iot_etage_blau_admin WITH PASSWORD 'iot_etage_blau-password';

ALTER ROLE iot_etage_blau_admin SET default_transaction_isolation TO 'read committed';
ALTER ROLE iot_etage_blau_admin SET client_encoding TO 'utf8';
ALTER ROLE iot_etage_blau_admin SET timezone TO 'UTC';

\c iot_etage_blau_db

create table IF NOT EXISTS "iot_etage_sensordata" (id serial PRIMARY KEY, "uid" varchar(30) NOT NULL, "sensortype" varchar(30) NOT NULL, "payload" varchar(30) NULL, "timestamp" timestamp NOT NULL DEFAULT NOW());
create table IF NOT EXISTS "iot_etage_eventdata" (id serial PRIMARY KEY, "action" varchar(30) NOT NULL, "oldstate" varchar(30) NOT NULL, "newstate" varchar(30) NOT NULL, "trigger" varchar(30), "timestamp" timestamp NOT NULL DEFAULT NOW());

ALTER DATABASE iot_etage_blau_db SET TIMEZONE TO 'Europe/Berlin';

GRANT ALL PRIVILEGES ON DATABASE iot_etage_blau_db TO postgres;
GRANT ALL PRIVILEGES ON DATABASE iot_etage_blau_db TO iot_etage_blau_admin;

GRANT ALL ON ALL TABLES IN SCHEMA public to iot_etage_blau_admin;
GRANT ALL ON ALL SEQUENCES IN SCHEMA public to iot_etage_blau_admin;
GRANT ALL ON ALL FUNCTIONS IN SCHEMA public to iot_etage_blau_admin;