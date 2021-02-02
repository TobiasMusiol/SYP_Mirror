<template>
  <div>
    <v-app-bar>
      <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>

      <v-toolbar-title
        ><a href="/" class="text-decoration-none"
          >IoT Etage Blau</a
        ></v-toolbar-title
      >

      <v-spacer></v-spacer>

      <template v-if="isAuthenticated">
        <v-btn @click="logout()"> Log Out </v-btn>
      </template>
      <template v-else>
        <v-btn :to="'/login'"> Log In </v-btn>
      </template>
    </v-app-bar>

    <v-navigation-drawer v-model="drawer" fixed temporary>
      <v-list-item>
        <v-list-item-content>
          <v-list-item-title class="title"> IoT Etage Blau </v-list-item-title>
          <v-list-item-subtitle>
            Navigation für IoT Etage App
          </v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
      <v-divider></v-divider>
      <template v-if="isAuthenticated">
        <v-list nav>
          <v-list-item v-for="app in apps" :key="app.id" :to="app.url">
            <v-list-item-icon>
              <v-icon>fas {{ app.icon }}</v-icon>
            </v-list-item-icon>
            <v-list-item-content>{{ app.name }}</v-list-item-content>
          </v-list-item>
        </v-list>
      </template>
      <template v-else>
        <div class="pl-2 mt-10 red--text text--lighten-1">
          Sie haben keine Rechte für den Zugriff auf die
          <span class="font-weight-bold"> IoT Etage Apps </span>. Logen Sie sich
          ein
        </div>
      </template>
    </v-navigation-drawer>

    <router-view name="register-button" />
  </div>
</template>

<script>
import { isLoggedIn } from "../../config/helper";
export default {
  name: "NavigationBar",
  data: () => {
    return {
      drawer: false,
    };
  },
  computed: {
    apps: function () {
      return this.$store.state.user.apps;
    },
    isAuthenticated: function () {
      return this.$store.state.user.isAuthenticated;
    },
  },
  mounted: async function () {},
  methods: {
    goToLoginPage() {
      this.$router.push("/login");
    },
    logout() {
      this.$store.commit("logoutUser");
      this.$router.push("/login");
    },
  },
};
</script>

<style lang="css" scoped></style>