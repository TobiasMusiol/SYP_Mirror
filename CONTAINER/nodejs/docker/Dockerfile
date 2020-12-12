FROM node:slim

WORKDIR /worker

RUN apt update && \
	apt -y upgrade

RUN apt install -y nano vim

ENV PATH "/worker/node_modules/.bin:$PATH"

COPY entrypoint.sh /scripts/entrypoint.sh

ENTRYPOINT [ "/scripts/entrypoint.sh" ]

CMD "node index.js"