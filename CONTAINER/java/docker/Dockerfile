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


FROM debian as prod

WORKDIR /app

RUN apt update && \
  apt dist-upgrade -y

RUN apt install -y vim nano

RUN apt install -y openjdk-11-jre

RUN apt -y autoremove

COPY ./ca_iot_etage.crt /tmp/ca_iot_etage.crt
RUN keytool -import -alias IoTEtage -file /tmp/ca_iot_etage.crt -keystore /usr/lib/jvm/java-11-openjdk-amd64/lib/security/cacerts -storepass changeit -noprompt -trustcacerts

CMD java -jar -Dspring.profiles.active=prod /app/iot_etage-0.0.1-SNAPSHOT.jar