<template>
  <v-app>
    <navigation-bar></navigation-bar>
    <router-view name="mainview" />
    <router-view name="footerview" />
    <div class="alert-position">
      <v-alert
        :value="alert.displayAlert"
        @input="toggleAlert()"
        color="blue-grey darken-2"
        dark
        dense
        dismissible
        close-text="Scließen"
        >{{ alert.alertMessage }}
      </v-alert>
    </div>
    <!-- Alert Testen
    <v-btn color="primary" @click="toggleAlert('warning', 'The TEST')"
      >Toggle Alert</v-btn
    >
    -->
  </v-app>
</template>

<script>
import NavigationBar from "./components/Base/NavigationBar.vue";
export default {
  name: "IoT_EtageApp",

  components: {
    NavigationBar,
  },

  data: () => ({}),

  computed: {
    alert() {
      return this.$store.state.alert;
    },
  },
  methods: {
    isLoggedIn() {
      return this.test;
    },
    toggleAlert() {
      this.$store.commit("toggleAlert", {});
    },
  },
};
</script>

<style lang="css">
.height-100 {
  height: 100%;
}

.alert-position {
  position: absolute;
  bottom: 0;
  right: 15px;
  z-index: 1000;
  max-width: 350px;
}
</style>
