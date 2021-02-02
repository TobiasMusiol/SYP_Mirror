<template>
  <v-container fill-height class="text-center">
    <v-sheet
      class="mx-auto"
      :width="$vuetify.breakpoint.smAndUp ? '60%' : '100%'"
    >
      <v-form ref="form" lazy-validation>
        <v-text-field v-model="name" label="Name" required></v-text-field>

        <v-text-field
          v-model="password"
          :type="show ? 'text' : 'password'"
          :append-icon="show ? 'mdi-eye' : 'mdi-eye-off'"
          @click:append="show = !show"
          label="Password"
          required
        ></v-text-field>

        <v-btn color="success" class="mr-4" @click="login()"> Log In </v-btn>

        <v-btn color="warning" @click="reset"> Reset </v-btn>

        <!-- Modal für Registrierung -->
        <modal-register-content />
      </v-form>
    </v-sheet>

    <v-alert></v-alert>
  </v-container>
</template>

<script>
// import ModalBasis from "../Modal/ModalBasis";
import config from "../../config/config";

import ModalRegisterContent from "../Modal/ModalRegisterContent.vue";

export default {
  name: "Login",
  components: {
    ModalRegisterContent,
  },

  data: () => ({
    lala: true,
    name: "",
    nameRules: [(v) => !!v || "Name ist nötig"],
    password: "",
    show: false,
    passwordRules: [(v) => !!v || "Password ist nötig"],
  }),
  computed: {
    user() {
      return this.$store.state.user;
    },
  },

  methods: {
    login() {
      console.log(this.alert);
      if (this.$refs.form.validate()) {
        let loginBody = {};
        loginBody.name = this.name;
        loginBody.password = this.password;

        fetch(`${config.urls.backend.auth}/login`, {
          method: "post",
          headers: { ...config.headers },
          body: JSON.stringify(loginBody),
        })
          .then((response) => {
            if (response.status === 200) {
              response.json().then((data) => {
                //Mit etwas sinvollerem erstetzen
                console.log(data);
                console.log(data.status);
                this.$store.commit("authenticateUser", { ...data });
                this.$router.push("/");
              });
            } else {
              this.$store.commit("toggleAlert", {
                alertType: "info",
                alertMessage: "Falsches Password oder Username",
                showAlert: true,
              });
            }
          })
          .catch((e) => {
            console.log(this.$store);
            this.$store.commit("toggleAlert", {
              alertType: "info",
              alertMessage: "Login fehlgeschlagen",
              showAlert: true,
            });
          });
      }
    },
    /*
    reset() {
      },
    */
    clearInputField() {
      console.log(this.$refs.form);
    },
    reset() {
      this.$refs.form.resetValidation();
      this.$refs.form.reset();
    },
  },
};
</script>

<style scoped>
a {
  cursor: pointer;
}
</style>