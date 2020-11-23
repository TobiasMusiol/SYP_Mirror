FROM mongo

RUN apt update && \
  apt -y dist-upgrade 

RUN apt -y install vim nano

# ENV Variables
ENV MONGO_INITDB_ROOT_USERNAME admin
ENV MONGO_INITDB_ROOT_PASSWORD=password
ENV MONGO_INITDB_DATABASE iot_etage_db
ENV MONGODB_USERNAME iot_etage-admin
ENV MONGODB_PASSWORD iot_etage-password

ADD ./initDB.js /docker-entrypoint-initdb.d/initDB.js

# Rewrite default start set up
#COPY ./entrypoint.sh /scripts/entrypoint.sh
#ENTRYPOINT [ "/scripts/entrypoint.sh" ]
#CMD /usr/local/bin/docker-entrypoint.sh mongod