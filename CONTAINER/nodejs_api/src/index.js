const express = require('express');
const bodyParser = require('body-parser');
const app = express();
const port = 3002;
const db = require('./queries')

app.use(bodyParser.json());
app.use(
	bodyParser.urlencoded({
		extended: true,
	})
);

app.get('/', (request, response) => {
	response.json({
		info: 'IoT_Etage REST Api'
	})
});

app.listen(port, () => {
	console.log(`REST Api running on port ${port}.`);
});

app.get('/sensordata', db.getSensordata);
app.get('/sensordata/:id', db.getSensordataById);
app.post('/sensordata', db.insertSensordata);