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
                      <v-switch
                        v-model="switch1"
                        :label="`${switch1 ? 'Automatisch' : 'Manuell'}`"
                      ></v-switch>
                    </v-col>
                  </v-row>
                  <template v-if="!switch1">
                    <v-row align="center">
                      <v-col cols="6">Helligkeit</v-col>
                      <v-col cols="6">
                        <v-slider
                          v-model="brightness"
                          step="1"
                          thumb-label
                          ticks
                          append-icon="mdi-lightbulb"
                        ></v-slider>
                      </v-col>
                    </v-row>
                  </template>
                </v-container>
                <v-card-actions v-if="checkPermissions()" class="text-center">
                  <v-btn outlined rounded text @click="changeState()">
                    Ändern
                  </v-btn>
                </v-card-actions>
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
  data() {
    return {
      switch1: true, // true = Automatisch, false = Manuell
      brightness: 0,
      sensorValue: 0,
    };
  },
  computed: {
    usertype: function () {
      return this.$store.state.user.usertype;
    },
  },
  methods: {
    checkPermissions() {
      return this.usertype === config.userTypes.ADMIN ||
        this.usertype === config.userTypes.FM
        ? true
        : false;
    },
    async changeState() {
      let payloadBody = {};
      //Automatisch
      if (this.switch1) {
        payloadBody.action = "switchMode";
        payloadBody.payload = {};
        payloadBody.payload.targetMode = "auto";
      } else {
        payloadBody.aciton = "setBrightness";
        payloadBody.payload = {};
        payloadBody.payload.brightness = this.brightness;
      }
      const respone = await fetch(`${config.urls.backend.base}/light`, {
        method: "post",
        headers: {
          ...config.headers,
          "Authorization": localStorage.getItem("user-token"),
        },
        body: JSON.stringify(payloadBody),
      });
      if (respone.status === 201 || respone.status === 200) {
        console.log("alles gut");
      } else {
        this.$store.commit("toggleAlert", {
          alertType: "error",
          alertMessage: "Änderung fehlgeschalgen",
          showAlert: true,
        });
      }
    },
  },
  async checkSensorSettings() {},
};
</script>

<style>
</style>