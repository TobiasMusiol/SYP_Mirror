<template>
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
          <span class="font-weight-bold"> IoT Etage Apps </span>. Logen Sie sich
        </v-col>
      </template>
    </v-row>
  </v-container>
</template>

<script>
import config from "../../config/config";
import helper, { isLoggedIn } from "../../config/helper";

export default {
  name: "MainView",
  data: function () {
    return {
      config: config,
    };
  },
  mounted() {},
  computed: {
    apps: function () {
      return this.$store.state.user.apps;
    },
  },
  methods: {
    goToApp(url) {
      this.$router.push(url);
    },
  },
};
</script>

<style scoped>
.app-picker {
  border: solid black 1px;
}
</style>