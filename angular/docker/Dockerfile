FROM node:slim

WORKDIR /app

RUN apt update && \
  apt -y upgrade
RUN apt install -y nano vim
#RUN npm install -g @angular/cli

RUN apt -y autoremove

ENV PATH="/app/node_modules/.bin:${PATH}"

COPY entrypoint.sh /scripts/entrypoint.sh
ENTRYPOINT [ "/scripts/entrypoint.sh" ]
CMD "ng serve --host 0.0.0.0"