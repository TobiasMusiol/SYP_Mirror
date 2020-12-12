FROM node:slim

WORKDIR /app

RUN apt update && \
    apt -y upgrade

RUN apt install -y nano vim

ENV PATH "/app/node_modules/.bin:$PATH"

COPY entrypoint.sh /scripts/entrypoint.sh

ENTRYPOINT [ "/scripts/entrypoint.sh" ]

CMD "/app/node_modules/.bin/vue-cli-service serve --host 0.0.0.0 --port 8080"