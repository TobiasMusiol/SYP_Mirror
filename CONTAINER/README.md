Willkommen in der Repository von TEAM19(aka Blau) SYP Projekt

## Aufsetzen der Entwicklungsumgebung

1. `docker-compose up` ausführen
2. Falls man bestimtme Service disabeln möchte, noch folgede Schritte ausführen
    1. Datei `docker-comopse.override.up` erstellen, die man in dem Verzeichnis *CONTAINER* ablegt.
    2. Die Datei z.B folgendermaßen aussehen, sodass **java** und **vue** Container nicht gestartet bzw. nach dem Start direkt heruntergefahren werden

    ```yml
    services:
        vue:
            command: echo "I will be Disabled"
            restart: 'no'

        java:
            command: echo "I will be Disabled"
            restart: 'no'
    ```

    * Diese Datei wird nicht versioniert, da jeder an verschiedenen Teilen arbeitet und dementsprechen, wird jeder andere services ausmachen wollen, was dazuführen wird, dass die diese Datei Merge-Konflikte verursachen wird.
    * Ebenfalls `docker-compose.override.yml` nutzen, wenn man etwas in Debug Modus starten möchte. Man muss das `command` umschreiben
        * Node.JS: `node --inspect=0.0.0.0:PORT index.js`, wobei `PORT` ist der zweite geöffnete Port des Services in `docker-compose.yml`
        * Java: `java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:8091 -jar target/iot_etage-0.0.1-SNAPSHOT.jar`. Bei Java ist der zweite freigeben Port **8091**
