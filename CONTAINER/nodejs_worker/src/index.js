const mqtt = require('mqtt');
require('dotenv').config();
const axios = require('axios').default;

let options = {
	port: process.env.MQTT_PORT,
	clientId: 'mqttjs_' + Math.random().toString(16).substr(2, 8),
	username: process.env.MQTT_USER,
	password: process.env.MQTT_PASS
};
let client = mqtt.connect(process.env.MQTT_HOST, options);

client.on('connect', () => {
	console.log("Worker connected successfully to MQTT Broker");
	
	client.subscribe(process.env.MQTT_SENSORDATA_TOPIC, (e) => {
		if (e) console.log(`There was an error subscribing to ${process.env.MQTT_SENSORDATA_TOPIC}": ${e.message}`);
	});
});

client.on('message', (topic, payload, packet) => {
	console.log(`Received [${topic}]: ${payload}`);
	let obj;
	switch (topic) {
		case process.env.MQTT_SENSORDATA_TOPIC:
			try {
				obj = JSON.parse(payload);
			} catch (e) {
				console.log(`Given payload is not a valid JSON Object.`);
				console.error(e.message);
				return;
			}
			if (obj.hasOwnProperty('uid') && obj.hasOwnProperty('sensorType') && obj.hasOwnProperty('payload')) {
				try {
					let response = axios.post('http://api:3002/sensordata', obj);
					if (response.status >= 200 && response.status < 400) {
						console.log(`Sensordata has been send successfully.`);
					}
				} catch (e2) {
					console.error(e2);
				}
			} else {
				console.log(`JSON Object is missing Properties.`);
			}
			break;
		default:
			console.log(`No actions for the topic ${topic} configured.`);
			break;
	}
});

client.on('error', (e) => {
	console.log("There was an error connecting to MQTT Broker.");
	console.log(e.message);
});