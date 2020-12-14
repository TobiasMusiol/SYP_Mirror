const Pool = require('pg').Pool;
const pool = new Pool({
	user: 'iot_etage_blau_admin',
	host: 'postgres',
	database: 'iot_etage_blau_db',
	password: 'iot_etage_blau-password',
	port: 5432,
});

const getSensordata = (req, res) => {
	pool.query('SELECT * FROM iot_etage_sensordata ORDER BY id ASC', (error, result) => {
		if (error) {
			throw error;
		}
		res.status(200).json(result.rows)
	});
};

const getSensordataById = (req, res) => {
	const id = parseInt(req.params.id)
	pool.query('SELECT * FROM iot_etage_sensordata WHERE id = $1', [id], (error, result) => {
		if (error) {
			throw error;
		}
		res.status(200).json(result.rows)
	});
};

const insertSensordata = (req, res) => {
	const { uid, sensorType, payload } = req.body;

	pool.query('INSERT INTO iot_etage_sensordata (uid, "sensorType", payload) VALUES ($1, $2, $3)', [uid, sensorType, payload], (error, result) => {
		if (error) {
			throw error;
		}
		res.status(201).send(`Sensordata added with ID ${result.insertId}`);
	});
};

module.exports = {
	getSensordata,
	getSensordataById,
	insertSensordata
}