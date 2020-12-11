SELECT 'CREATE DATABASE iot_etage_blau_db' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'iot_etage_blau_db')\gexec

--CREATE DATABASE iot_etage_blau_db;
CREATE USER iot_etage_blau_admin WITH PASSWORD 'iot_etage_blau-password';

ALTER ROLE iot_etage_blau_admin SET default_transaction_isolation TO 'read committed';
ALTER ROLE iot_etage_blau_admin SET client_encoding TO 'utf8';
ALTER ROLE iot_etage_blau_admin SET timezone TO 'UTC';

\c iot_etage_blau_db


create table IF NOT EXISTS "IOT_ETAGE_sensordata" (id serial PRIMARY KEY, "UID" varchar(30) NOT NULL, "SensorType" varchar(30) NOT NULL, "payload" varchar(30) NULL, "timestamp" DATE NOT NULL);

/*
insert into "GAMEDAY_user" ("userName", "userPassword")  values('Admin', 'i82B[;\DRp;z,\g)');
insert into "GAMEDAY_user" ("userName", "userPassword")  values('Jackson', 'iI-gOd]1gbfXRTKW');
insert into "GAMEDAY_user" ("userName", "userPassword")  values('Rebecca', '*ss;2Wh.8]vhk"1W');
*/

create table IF NOT EXISTS "IOT_ETAGE_Eventdata" (id serial PRIMARY KEY, "action" varchar(30) NOT NULL, "oldState" varchar(30) NOT NULL, "newState" varchar(30) NOT NULL, "trigger" varchar(30), "timestamp" DATE NOT NULL);


/*
insert into "GAMEDAY_node" ("nodeName", "nodeUser", "nodeSSHPort")  values('nginx', 'jackson', '2222');
insert into "GAMEDAY_node" ("nodeName", "nodeUser", "nodeSSHPort")  values('django', 'guiseppe', '22');
insert into "GAMEDAY_node" ("nodeName", "nodeUser", "nodeSSHPort")  values('mongo', 'lorenzo', '22');
insert into "GAMEDAY_node" ("nodeName", "nodeUser", "nodeSSHPort")  values('postgres', 'rebecca', '22');
insert into "GAMEDAY_node" ("nodeName", "nodeUser", "nodeSSHPort")  values('flag', '####', '');
*/


GRANT ALL PRIVILEGES ON DATABASE iot_etage_blau_db TO postgres;
GRANT ALL PRIVILEGES ON DATABASE iot_etage_blau_db TO iot_etage_blau_admin;

GRANT ALL ON ALL TABLES IN SCHEMA public to iot_etage_blau_admin;
GRANT ALL ON ALL SEQUENCES IN SCHEMA public to iot_etage_blau_admin;
GRANT ALL ON ALL FUNCTIONS IN SCHEMA public to iot_etage_blau_admin;