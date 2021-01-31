//Beleuchtungssteuerung

let json = {
	'MCUID': 1001,
	'action': 'switchMode',
	'payload': {
		'targetMode': 'auto'
	}
};

let json2 = {
	'MCUID': 1001,
	'action': 'setBrigthness',
	'payload': {
		'brigthness': 60
	}
}

//Markisensteuerung

let json5 = {
	'MCUID': 1002,
	'action': 'switchMode',
	'payload': {
		'targetMode': 'auto',
		'threshold': 60
	}
}

let json3 = {
	'MCUID': 1002,
	'action': 'setThreshold',
	'payload': {
		'threshold': 60
	}
}

let json4 = {
	'MCUID': 1002,
	'action': 'toggle',
	'payload': {
		'direction' : 'up'
	}
};

//Lüftung

let json6 = {
	'MCUID': 1003,
	'action': 'switchMode',
	'payload': {
		'targetMode': 'auto',
		'threshold': 50
	}
}

let json7 = {
	'MCUID': 1003,
	'action': 'setSpeed',
	'payload': {
		'speed': 100
	}
}

let json8 = {
	'MCUID': 1003,
	'action': 'setThreshold',
	'payload': {
		'threshold': 50
	}
}

//Raumstatus

let json9 = {
	'MCUID': 1004,
	'action': 'setState',
	'payload': {
		'state': 'open' //states definieren
	}
}

let json10 = {
	'MCUID': 1004,
	'action': 'setThreshold',
	'payload': {
		'threshold': 70
	}
}