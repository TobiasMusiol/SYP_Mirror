FROM openjdk:11.0.9.1-jdk-buster

WORKDIR /app

RUN apt update && \
    apt -y dist-upgrade

RUN apt-get -y install maven

RUN apt-get -y install nano vim

RUN apt -y autoremove


COPY ./entrypoint.sh /scripts/entrypoint.sh
ENTRYPOINT [ "/scripts/entrypoint.sh" ]