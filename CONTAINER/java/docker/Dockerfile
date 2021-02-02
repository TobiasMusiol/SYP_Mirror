FROM openjdk:11.0.9.1-jdk-buster

WORKDIR /app

RUN apt update && \
  apt -y dist-upgrade

RUN apt-get -y install maven

RUN apt-get -y install nano vim

RUN apt -y autoremove

COPY ./ca_iot_etage.crt /tmp/ca_iot_etage.crt
RUN keytool -import -alias IoTEtage -file /tmp/ca_iot_etage.crt -keystore /usr/local/openjdk-11/lib/security/cacerts -storepass changeit -noprompt -trustcacerts


COPY ./entrypoint.sh /scripts/entrypoint.sh
ENTRYPOINT [ "/scripts/entrypoint.sh" ]