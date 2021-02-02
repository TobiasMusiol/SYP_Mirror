<template>
  <v-container>
    <v-row class="my-5">
      <v-col class="text-center">Markisolettensteuerung</v-col>
    </v-row>
    <v-row>
      <v-col cols="8" class="text-center mx-auto">
        <v-card>
          <v-list-item three-line>
            <v-list-item-content>
              <div class="overline mb-4">Markisolettensteuerung</div>
              <v-list-item-title class="headline mb-1">
                Live Daten
              </v-list-item-title>
              <v-list-item-subtitle>
                <v-container class="text-left">
                  <v-row align="center">
                    <v-col cols="6">Modus</v-col>
                    <v-col cols="6">
                      <v-switch v-model="switch1" :label="`${switch1.toString() == 'true' ? 'Automatisch' : 'Manuell'}`" @change="changeMode"></v-switch>
                    </v-col>
                  </v-row>
                  <template v-if="switch1.toString() == 'false'">
                    <v-row align="center">
                      <v-col cols="6">Position</v-col>
                      <v-col cols="6">
                        <v-switch v-model="aufzu" :label="`${aufzu.toString() == 'true' ? 'Auf' : 'Zu' }`" thumb-label ticks append-icon="mdi-format-align-justify" @change="setAufZu"></v-switch>
                      </v-col>
                    </v-row>
                  </template>
                  <template v-if="switch1.toString() == 'true'">
                    <v-row align="center">
                      <v-col cols="6">Aktivierungsschwelle</v-col>
                      <v-col cols="6">
                        <v-slider v-model="threshold" step="1" max="100" thumb-label ticks append-icon="mdi-weather-sunny" @mouseup="setThreshold"></v-slider>
                      </v-col>
                    </v-row>
                  </template>
                </v-container>
              </v-list-item-subtitle>
            </v-list-item-content>

            <v-list-item-avatar tile size="80" color="grey">
              <v-icon color="blue" x-large> fas fa-align-justify </v-icon>
            </v-list-item-avatar>
          </v-list-item>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>

import config from "../../config/config";

export default {
  name: "Markisoletensteuerung",

data () {
    return {
      appName: 'markisolette',
      switch1: true,
      aufzu: true,
      threshold: 20,
    }
  },
  methods: {
    sendPostRequest(jsonObj) {
      fetch(`${config.urls.backend.base}/${this.appName}`, {
          method: "post",
          headers: { ...config.headers },
          body: JSON.stringify(jsonObj),
        }).then((response) => {
          if (response.status === 200) {
            //TODO Richtigen Response auswerten
            console.log('success');
          } else {
            this.$store.commit("toggleAlert", {
                alertType: "info",
                alertMessage: "Fehler beim Post Request",
                showAlert: true,
              });
          }
        })
    },
    setAufZu() {
      console.log(`Setting Position to ${this.aufzu ? 'up' : 'down'}`);
      let targetdirection = this.aufzu ? 'up' : 'down';
      let json = {
        'MCUID': 1002,
        'action': 'toggle',
        'payload': {
          'direction': targetdirection 
        }
      };
      this.sendPostRequest(json);
    },
    changeMode() {
      console.log(`Changing mode to ${this.switch1 ? 'automatisch' : 'manuell'}`);
      let targetMode = this.switch1 ? 'auto' : 'man';
      let json = {
        'MCUID': 1002,
        'action': 'switchMode',
        'payload': {
          'targetMode': targetMode,
          'threshold': this.threshold
        }
      };
      this.sendPostRequest(json);
    },
    setThreshold() {
      console.log(`Setting threshold to ${this.threshold}`);
      let json = {
        'MCUID': 1002,
        'action': 'setThreshold',
        'payload': {
          'threshold': this.threshold
        }
      };
      this.sendPostRequest(json);
    }
  }
};
</script>

<style>
</style>