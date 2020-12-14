let mqtt = require('mqtt');
require('dotenv').config();

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
});

client.on('error', (e) => {
	console.log("There was an error connecting to MQTT Broker.");
	console.log(e.message);
});