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

#### Wichtige Befehle

* `docker-compose restart CONTAINERNAME` - bestimmten Container restarten, ohne die weiteren anzufassen
* `docker-compose stop CONTAINERNAME`
* `docker-compose up -d` mit `-d` kann man den Output/Logging ausmachen
  * wenn man dann doch die Docker Logs braucht `docker-compose log -f` ausführen

#### `command`-Befehle, um Container in Debug-Modus starten

##### Java

für *docker-compose-override.yml*
```yaml
command: 'java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:8091 -jar target/iot_etage-0.0.1-SNAPSHOT.jar'
```

für *.vscode/launch.json*
```json
{
  "type": "java",
  "name": "Debug (Attach) - Remote Java",
  "request": "attach",
  "hostName": "localhost",
  "port": "8091"
},
```

#####  Worker

```yml
command: 'node --inspect=0.0.0.0:3001 index.js
```

für *.vscode/launch.json*
```json
{
  "address": "localhost",
  "localRoot": "${workspaceFolder}/CONTAINER/nodejs_worker/src",
  "name": "Attach to Remote Worker-Node.js",
  "port": 3001,
  "remoteRoot": "/worker",
  "request": "attach",
  "skipFiles": [
    "<node_internals>/**"
  ],
  "type": "pwa-node"
},
```

##### Vue

* Chrome Extension *Vue DevTools* Version 4.X.X herunter laden. (Es gibt zwei Version, Version 4.X.X ist für Vue 2, Version 6.X.X ist für Vue 3)

### Frontend

#### Frameworks

1. Vue 2 <https://vuejs.org/>
2. Vuetify 2 <https://vuetifyjs.com/en/> um nicht selbst die Standard-Componenten neu zuschreiben
3. Vue Router <https://router.vuejs.org/> Routing für Vue
4. Vuex - <https://vuex.vuejs.org/>, um States, die zwischen den ganzen App festzuhalten. Momentan wird Vuex für
   1. User */src/store/user.module.js*
   2. Alert */src/store/alert.module.js* - um Fehler und andere Benachrichtigungen, die für User interessant sind auszugeben
  
#### Ordnerstruktur

* */Components* - hier liegen die eigentlichen Vue-Datei, wobei hier ist es wieder in drei Unterurdner unterteilt
  * */Base* - Componenten, die fast bei bei jeder Seite verwendet werden
  * */Main* - Componenten der einzelnen Seiten (Seite = Componente)
  * */Modal* - gadacht, dass man hier die Modal-Componenten ablegt. Bis jetzt gibt es aber nur eine Modal-Componente
  * */ARCHIVE* - eigentlich nicht benötigt. Die von Vue erstellten Componten
  * */SmallComponents* - eigentlich nicht benötigt (da Vuetify verwendet)
  
Falls man Bilder usw. verwenden möchte, kann man diese in */assets* ablegen

in */config* 
  1. *router.js*, wo Vue-Routing konfiguriert ist. 
  2. *config.js* benutzen, falls man irgendwelche Konstanten für Vue festlegen möchte
  3. *helper.js* wird nicht verwendet, da Vuex verwendet
*/services* wird nicht benötigt. Für guten Still, sollte man aber hier die Logic für Komponenten haben, damit die Komponten, nicht zu unübersichtich werden
  
### Backend

1. Spring-Boot - Kern Framework
2. Spring-Boot-Security - für User-Authentifizierung 
3. JWT - auch für Authentifizierung - <https://jwt.io/>, <https://github.com/jwtk/jjwt>

Hilfreiche Seiten für Authentifizierung:
1. <https://www.toptal.com/spring/spring-security-tutorial>
2. <https://www.marcobehler.com/guides/spring-security#:~:text=The%20short%20answer%3A,standards%20like%20OAuth2%20or%20SAML.>
3. <https://www.youtube.com/watch?v=her_7pa0vrg&t=16212s>

#### Ordnerstruktur

* */auth* - Klassen, für die Implementierung der Authentifizierung, Gruppen und Rechte benötigt werden
* */controller* - HTTP-Methoden zu Java-Methoden mappen. 
  * */dto* - Klassen (etwas die Grenzklassen aus SYP), wobei in denen wird schon überprüft, ob die übergebenen Daten valide sind
* */domain* - Klassen, um die Daten festzuhalten, wobei die */entity* in DB geschrieben werden und */model*-Klassen während des Laufen der Anwendung "In-Memory* festgehalten werden
* */exceptions* - Klassen für eigene Exceptions und Excepion-Handling (bis jetzt aber nicht wirklich was hier gemacht)
* */services* eigentlichen Klassen, die etwas machen/rechnen sollen, wenn Backend Nachrichten bekommt. Wandelt die *dto* Klassen zu den richtigen Klassen um, auf denen dann die Aktionen ausgeführt werden sollen.
* */utils* - Mapper-Klassen, um *dto*-Klassen zu den normal Klassen umzuwandeln und zurück, werden benutzt, um nicht jedes Mal *getXX*, *setXX* zu benutzen
* *StartRunner.java* wird beim Start der App ausgeführt. Momentan werden die User **Admin** und **MCU** erstellt, da in der UI man nur Std-User, Facility Manager und Büromitarbeiter erstellen kann

Man kann die Java-App im Container oder local ausführen.