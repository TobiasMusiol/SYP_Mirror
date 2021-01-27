<template>
  <v-container>
    <v-row class="my-5">
      <v-col class="text-center"> Beleuchtungssteuerung </v-col>
    </v-row>
    <v-row>
      <v-col cols="8" class="text-center mx-auto">
        <v-card>
          <v-list-item three-line>
            <v-list-item-content>
              <div class="overline mb-4">Beleuchtungssteuerung</div>
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
                      <v-col cols="6">Helligkeit</v-col>
                      <v-col cols="6">
                        <v-slider v-model="brightness" step="1" thumb-label ticks append-icon="mdi-lightbulb" @mouseup="setBrightness"></v-slider>
                      </v-col>
                    </v-row>
                  </template>
                </v-container>
              </v-list-item-subtitle>
            </v-list-item-content>

            <v-list-item-avatar tile size="80" color="grey">
              <v-icon color="blue" x-large> fas fa-sun </v-icon>
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
  name: "Beleuchtungssteuerung",
  data () {
    return {
      appName: 'light',
      switch1: true,
      brightness: 0,
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
    setBrightness() {
      console.log(`Setting Brightness to ${this.brightness}`);
      let json = {
        'MCUID': 1001,
        'action': 'setBrigthness',
        'payload': {
          'brigthness': this.brigthness
        }
      };
      this.sendPostRequest(json);
    },
    changeMode() {
      console.log(`Changing mode to ${this.switch1 ? 'automatisch' : 'manuell'}`);
      let targetMode = this.switch1 ? 'auto' : 'man';
      let json = {
        'MCUID': 1001,
        'action': 'switchMode',
        'payload': {
          'targetMode': targetMode
        }
      };
      this.sendPostRequest(json);
    }
  },
};
</script>

<style>
</style>