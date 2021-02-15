FROM nginx

RUN apt update && \
  apt -y dist-upgrade

RUN apt -y install npm

RUN apt -y autoremove

COPY ./buildVue.sh /docker-entrypoint.d/buildVue.sh
