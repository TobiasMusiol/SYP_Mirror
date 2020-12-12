#!/bin/sh

PASSWDFILE=/mosquitto/data/passwd
LIMIT=80

LENGTH=$(awk '{print length}' $PASSWDFILE | sort -n | head -n1)

if [ $LENGTH -gt $LIMIT ]; then
	echo "password file already converted, skipping step"
else
	if [ -f $PASSWDFILE ]; then
    	echo "converting password file"
    	mosquitto_passwd -U $PASSWDFILE
	fi
fi

exec "$@"