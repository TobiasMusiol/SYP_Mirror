FROM eclipse-mosquitto

WORKDIR /mosquitto

RUN apk -U upgrade

COPY entrypoint.sh /scripts/entrypoint.sh

ENTRYPOINT [ "/scripts/entrypoint.sh" ]

CMD ["/usr/sbin/mosquitto","-c","/mosquitto/config/mosquitto.conf"]
