<template>
  <v-container>
    <v-row class="my-5">
      <v-col class="text-center">Belüftungssteuerung</v-col>
    </v-row>
    <v-row>
      <v-col cols="8" class="text-center mx-auto">
        <v-card>
          <v-list-item three-line>
            <v-list-item-content>
              <div class="overline mb-4">Belüftungssteuerung</div>
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
                        :label="`${
                          switch1.toString() == 'true'
                            ? 'Automatisch'
                            : 'Manuell'
                        }`"
                        @change="changeMode"
                      ></v-switch>
                    </v-col>
                  </v-row>
                  <v-row align="center">
                    <v-col cols="6">Aktuelle Temparatur</v-col>
                    <v-col cols="6">{{ sensorValue }} °C</v-col>
                  </v-row>
                  <template v-if="switch1.toString() == 'false'">
                    <v-row align="center">
                      <v-col cols="6">Lüftergeschwindigkeit</v-col>
                      <v-col cols="6">
                        <v-slider
                          v-model="speed"
                          step="1"
                          thumb-label
                          ticks
                          append-icon="mdi-fan"
                          @mouseup="setSpeed"
                        ></v-slider>
                      </v-col>
                    </v-row>
                  </template>
                  <template v-if="switch1.toString() == 'true'">
                    <v-row align="center">
                      <v-col cols="6"
                        >Aktivierungsschwelle in Grad Celsius</v-col
                      >
                      <v-col cols="6">
                        <v-slider
                          v-model="threshold"
                          step="0.1"
                          min="15"
                          max="35"
                          thumb-label
                          ticks
                          append-icon="mdi-thermometer"
                          @mouseup="setThreshold"
                        ></v-slider>
                      </v-col>
                    </v-row>
                  </template>
                </v-container>
              </v-list-item-subtitle>
            </v-list-item-content>

            <v-list-item-avatar tile size="80" color="grey">
              <v-icon color="blue" x-large> fas fa-fan </v-icon>
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
  name: "Belueftungssteuerung",
  data() {
    return {
      appName: "air",
      switch1: true,
      speed: 0,
      threshold: 20,
      sensorValue: 0,
    };
  },
  mounted: async function () {
    try {
      const response = await fetch(
        `${config.urls.backend.base}/${this.appName}`,
        {
          method: "get",
          headers: {
            ...config.headers,
            "Authorization": localStorage.getItem("user-token"),
          },
        }
      );

      if (response.status === 200) {
        const data = await response.json();
        console.log(data);

        if (data.state === "AUTO") {
          //true = Auto, false = Man
          this.switch1 = true;
        } else if (data.state === "MAN") {
          this.switch1 = false;
        } else {
          this.switch1 = true;
        }

        this.sensorValue = data.sensorValue;
      } else {
        this.$store.commit("toggleAlert", {
          alertType: "info",
          alertMessage: "Fehler beim GET Request",
          showAlert: true,
        });
      }
    } catch (e) {
      this.$store.commit("toggleAlert", {
        alertType: "error",
        alertMessage: "Server nicht erreicht",
        showAlert: true,
      });
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
        headers: {
          ...config.headers,
          "Authorization": localStorage.getItem("user-token"),
        },
        body: JSON.stringify(jsonObj),
      }).then(async (response) => {
        if (response.status === 200) {
          //TODO Richtigen Response auswerten
          console.log("success");
          const data = await response.json();

          this.$store.commit("toggleAlert", {
            alertType: "info",
            alertMessage: data.message,
            showAlert: true,
          });
        } else {
          this.$store.commit("toggleAlert", {
            alertType: "info",
            alertMessage: "Fehler beim Post Request",
            showAlert: true,
          });
        }
      });
    },
    setSpeed() {
      console.log(`Setting speed to ${this.speed}`);
      let json = {
        "MCUID": 1003,
        "action": "setSpeed",
        "payload": {
          "speed": this.speed,
        },
      };
      this.sendPostRequest(json);
    },
    changeMode() {
      console.log(`Changing mode to ${this.switch1 ? "auto" : "man"}`);
      let targetMode = this.switch1 ? "auto" : "man";
      let json = {
        "MCUID": 1003,
        "action": "switchMode",
        "payload": {
          "targetMode": targetMode,
          "threshold": this.threshold,
        },
      };
      this.sendPostRequest(json);
    },
    setThreshold() {
      console.log(`Setting threshold to ${this.threshold}`);
      let json = {
        "MCUID": 1003,
        "action": "setThreshold",
        "payload": {
          "threshold": this.threshold,
        },
      };
      this.sendPostRequest(json);
    },
  },
};
</script>

<style>
</style>