## Docker Befehle

* `docker help` - Alle Docker Optionen anzeigen

* `docker ps` - Alle laufenden Container anzeigen
    * `docker ps -a` - Alle Container laufenen und geexiten

* `docker volume XXXX` - Volumes verwalten, die in die Container gemappt werden
    * `docker volume ls` - Alle Vorhandenen Volumes anzeigen
    * `docker volume prune` - Alle Volumes löschen, die gerande von den Containern nicht benutzt werden
    * `docker volume remove VOLUME-ID/VOLUME-NAME` - bestimmtes Volume löschen
    * `docker volume help/--help` - Alle möglichen Optionen für Volume Verwaltung anzeigen 

* `docker image XXXX` - Dockerimages verwalten
    * `docker image ls` - Images anzeigen
    * `docker image help` - Alle  möglichen Optionen für Image Verwaltung anzeigen
* `docker stats` - Anschauen, wie viel Ressourcen die Container verbrauchen
    * `docker stats -a` - auch anzeigen wieviel geexitet Container Ressourcen verbrauchen

## Docker Compose

* `docker-compose up` - *docker-compose.yml* ausführen
* `docker-compose up --build` - *docker-compose.yml* ausführen, dabei die DockerImages neu bauen, falls sie verändert wurden
* `docker-compose down` - Container, die in *docker-compose.yml* definiert sind stopen/herunterfahren
* `docker-compose -f DATEI.yml up/down` - bestimtme *DATEI.yml* anstelle der Default Datei *docker-compose.yml* ausführen bzw. herunterfahren  
* `docker-compose restart CONTAINER`- besten Container restarten

Wenn man bestimmte Services nicht starten möchte:

1. in Ornder *CONTAINER* Datei `docker-compose.override.yml` erstellen.

2. In der Datei die Services eintragen, die man nicht starten möchte z.B wenn man **vue** und **java** nicht starten möchte

```yml
services:
  vue:
    command: echo "I will be Disabled"
    restart: 'no'
  worker:

  java:
    command: echo "I will be Disabled"
    restart: 'no'
```
