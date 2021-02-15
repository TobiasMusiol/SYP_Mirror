<template>
  <div>
    <v-container>
      <v-row>
        <template v-if="this.apps">
          <v-col v-for="app in apps" :key="app.id" md="4">
            <v-card>
              <v-card-title>
                {{ app.name }}
              </v-card-title>
              <v-card-text class="text-center">
                <v-icon>fas {{ app.icon }}</v-icon>
              </v-card-text>
              <v-card-actions>
                <v-btn
                  color="deep-purple lighten-2"
                  text
                  @click="goToApp(app.url)"
                >
                  {{ app.name }}
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-col>
        </template>
        <template v-else>
          <v-col class="mt-10 text-center red--text text--lighten-1">
            Sie haben keine Rechte für den Zugriff auf die
            <span class="font-weight-bold"> IoT Etage Apps </span>. Logen Sie
            sich
          </v-col>
        </template>
      </v-row>
    </v-container>
    <v-container class="margin-top-auto">
      <v-row v-if="usertype === userTypes.ADMIN" class="mx-4">
        <v-switch
          v-model="sendToTB"
          color="orange"
          label="An Thingsboard senden"
          @change="changeSettins()"
        ></v-switch>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import config from "../../config/config";

export default {
  name: "MainView",
  data: function () {
    return {
      config: config,
      sendToTB: false,
      userTypes: config.userTypes,
    };
  },
  mounted() {
    if (this.usertype === this.userTypes.ADMIN) {
      this.getCurrentSettings();
    }
  },
  computed: {
    apps: function () {
      return this.$store.state.user.apps;
    },
    usertype: function () {
      return this.$store.state.user.usertype;
    },
  },
  methods: {
    goToApp(url) {
      this.$router.push(url);
    },

    async getCurrentSettings() {
      let response = null;
      try {
        response = await fetch(`${config.urls.backend.base}/settings/tb`, {
          method: "get",
          headers: {
            ...config.headers,
            "Authorization": localStorage.getItem("user-token"),
          },
        });
      } catch (e) {
        this.$store.commit("toggleAlert", {
          alertType: "info",
          alertMessage: "Fehler beim GET Request",
          showAlert: true,
        });
        return;
      }

      if (response.status === 200) {
        const data = await response.json();
        console.log(data);
        this.sendToTB = data.sendToTb;
      }
    },

    async changeSettins() {
      let body = {};

      body.sendToTb = this.sendToTB;
      let response = null;
      try {
        response = fetch(`${config.urls.backend.base}/settings/tb`, {
          method: "post",
          headers: {
            ...config.headers,
            "Authorization": localStorage.getItem("user-token"),
          },
          body: JSON.stringify(body),
        });
      } catch (e) {
        this.$store.commit("toggleAlert", {
          alertType: "info",
          alertMessage: "Fehler beim Post Request",
          showAlert: true,
        });
        return;
      }

      if (response.status === 200) {
        const data = response.json();
        console.log(data);
      }
    },
  },
};
</script>

<style scoped>
.app-picker {
  border: solid black 1px;
}
</style>