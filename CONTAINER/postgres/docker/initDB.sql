SELECT 'CREATE DATABASE iot_etage_blau_db' WHERE NOT EXISTS (
    SELECT
    FROM pg_database
    WHERE datname = 'iot_etage_blau_db'
)\gexec 
--CREATE DATABASE iot_etage_blau_db;

CREATE USER iot_etage_blau_admin WITH PASSWORD 'iot_etage_blau-password';
ALTER ROLE iot_etage_blau_admin SET default_transaction_isolation TO 'read committed';
ALTER ROLE iot_etage_blau_admin SET client_encoding TO 'utf8';
ALTER ROLE iot_etage_blau_admin SET timezone TO 'UTC';

\c iot_etage_blau_db 

create table IF NOT EXISTS "sensor" (
    id serial PRIMARY KEY,
    "UID" varchar(30) NOT NULL,
    "SensorType" varchar(30) NOT NULL,
    "payload" varchar(30) NULL,
    "timestamp" DATE NOT NULL
);

create table IF NOT EXISTS "event" (
    id serial PRIMARY KEY,
    "action" varchar(30) NOT NULL,
    "old_state" varchar(30) NOT NULL,
    "new_state" varchar(30) NOT NULL,
    "trigger" varchar(30),
    "timestamp" DATE NOT NULL
);

GRANT ALL PRIVILEGES ON DATABASE iot_etage_blau_db TO postgres;
GRANT ALL PRIVILEGES ON DATABASE iot_etage_blau_db TO iot_etage_blau_admin;
GRANT ALL ON ALL TABLES IN SCHEMA public to iot_etage_blau_admin;
GRANT ALL ON ALL SEQUENCES IN SCHEMA public to iot_etage_blau_admin;
GRANT ALL ON ALL FUNCTIONS IN SCHEMA public to iot_etage_blau_admin;

insert into "event" (
    "action",
    "old_state",
    "new_state",
    "trigger",
    "timestamp"
)
values(
    'action1',
    'oldState1',
    'newState1',
    'trigger1',
    '12-12-2020'
);
insert into "event" (
    "action",
    "old_state",
    "new_state",
    "trigger",
    "timestamp"
)
values(
    'action2',
    'oldState2',
    'newState2',
    'trigger2',
    '12-12-2020'
);