<template>
  <v-container>
    <v-row class="my-5">
      <v-col class="text-center">RaumStatus</v-col>
    </v-row>
    <v-row>
      <v-col cols="8" class="text-center mx-auto">
        <v-card>
          <v-list-item three-line>
            <v-list-item-content>
              <div class="overline mb-4">Raumstatus</div>
              <v-list-item-title class="headline mb-1">
                Live Daten
              </v-list-item-title>
              <v-list-item-subtitle>
                <v-container class="text-left">
                  <v-row align="center">
                    <v-col cols="6">Status</v-col>
                    <template v-if="this.$store.state.usertype === 'FM' || 'ADMIN'">
                    <v-col cols="6">
                      <v-select
                        :items="roomstateAdmin"
                          label="Status Auswahl"
                            solo
                             @click="setStateAdmin"
                      ></v-select>
                    </v-col>
                    </template>
                    <template v-if="this.$store.state.usertype === 'OW' ">
                    <v-col cols="6">
                      <v-select
                        :items="roomstateUser"
                          label="Status Auswahl"
                            solo
                             @click="setStateUser"
                      ></v-select>
                    </v-col>
                    </template>
                  </v-row>
                    <v-row align="center">
                      <v-col cols="6">Luftqualitäts Grenze</v-col>
                      <v-col cols="6">
                        <v-slider v-model="threshold" step="1" max="100" thumb-label ticks append-icon="mdi-fan" @mouseup="setThreshold"></v-slider>
                      </v-col>
                    </v-row>
                </v-container>
              </v-list-item-subtitle>
            </v-list-item-content>

            <v-list-item-avatar tile size="80" color="grey">
              <v-icon color="blue" x-large> fas fa-house-user </v-icon>
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
  name: "RaumStatus",

  data(){
    return{
    appName: "roomstate",
    roomstateAdmin: ["frei", "besetzt", "reinigen"],
    roomstateUser: ["frei","besetzt"],
    threshold: 20,
    }
  },
    computed: {
    usertype: function () {
      return this.$store.state.usertype;
    },
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
    setStateAdmin(){
      console.log(`Setting Roomstate to ${this.roomstateAdmin}`);
      let targetState = this.roomstateAdmin;

      let json = {
        'MCUID': 1004,
        'action': 'setState',
        'payload': {
           'state': targetState
        }

      };
      this.sendPostRequest(json);
    },
    setStateUser(){
      console.log(`Setting Roomstate to ${this.roomstateUser}`);
      let targetState = this.roomstateUser;

      let json = {
        'MCUID': 1004,
        'action': 'setState',
        'payload': {
           'state': targetState
        }

      };
      this.sendPostRequest(json);
    },
    setThreshold() {
      console.log(`Setting threshold to ${this.threshold}`);
      let json = {
        'MCUID': 1004,
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