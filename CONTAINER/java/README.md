### MQTT Certificat

* Folgedes Befehl ausführen `sudo keytool -import -alias IoTEtage -file PATH_ZU_CA.crt -keystore cacerts` 
  * Password: `changeit`
* Mit `keytool -list -keystore cacerts | grep iotetage` kann man nachschauen, ob der Certificat 
* Java Cert Ordner: */usr/lib/jvm/java-11-openjdk-amd64/lib/security* 