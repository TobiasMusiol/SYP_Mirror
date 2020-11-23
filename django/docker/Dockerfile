FROM python:slim

WORKDIR /app

RUN apt update && \
  apt -y dist-upgrade

RUN pip install --use-feature=2020-resolver django psycopg2-binary djangorestframework django-filter djongo gunicorn
#RUN pip install ptvsd debugpy
RUN apt -y autoremove


COPY ./entrypoint.sh /scripts/entrypoint.sh
ENTRYPOINT [ "/scripts/entrypoint.sh" ]
CMD 'python3 manage.py runserver 0.0.0.0:8000'