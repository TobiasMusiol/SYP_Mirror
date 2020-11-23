FROM nginx

RUN apt update && \
  apt -y dist-upgrade 

RUN apt install -y vim nano

#COPY entrypoint.sh docker-entrypoint.d/entrypoint.sh