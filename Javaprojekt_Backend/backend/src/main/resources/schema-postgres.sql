DROP TABLE IF EXISTS Sersordatas;
CREATE TABLE Sensordatas(uid serial PRIMARY KEY, sensorType VARCHAR, payload VARCHAR, date DATE);