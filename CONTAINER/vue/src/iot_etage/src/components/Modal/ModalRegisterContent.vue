<template>
  <v-dialog
    v-model="show"
    transition="dialog-bottom-transition"
    max-width="600"
  >
    <template v-slot:activator="{ on, attrs }">
      <div class="mt-5">
        <a class="text-decoration-none" color="primary" v-bind="attrs" v-on="on"
          >Registrieren</a
        >
      </div>
    </template>

    <template v-slot:default="dialog">
      <v-card>
        <v-toolbar color="primary" dark>Registrieren</v-toolbar>
        <v-card-text>
          <!--REGISTER FORM -->
          <v-form ref="form" v-model="valid" lazy-validation>
            <v-text-field
              v-model="name"
              :counter="20"
              :rules="nameRules"
              label="Name"
              required
            ></v-text-field>

            <v-text-field
              v-model="password"
              :type="showPassword ? 'text' : 'password'"
              :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
              @click:append="showPassword = !showPassword"
              :rules="passwordRules"
              label="Password"
              required
            ></v-text-field>

            <v-text-field
              v-model="passwordRepeat"
              :type="showPasswordRepeat ? 'text' : 'password'"
              :append-icon="showPasswordRepeat ? 'mdi-eye' : 'mdi-eye-off'"
              @click:append="showPasswordRepeat = !showPasswordRepeat"
              :rules="passwordRepeatRules"
              label="Password Wiederholen"
              required
            ></v-text-field>

            <v-select
              v-model="select"
              :items="usertype"
              :rules="[(v) => !!v || 'Usertyp muss ausgewählt werden']"
              label="Item"
              required
            ></v-select>

            <!--
    <v-checkbox
      v-model="checkbox"
      :rules="[(v) => !!v || 'You must agree to continue!']"
      label="Do you agree?"
      required
    ></v-checkbox>
    -->

            <v-btn
              :disabled="!valid"
              color="success"
              class="mr-4"
              @click="register()"
            >
              Registrieren
            </v-btn>

            <v-btn color="error" class="mr-4" @click="reset"> Reset</v-btn>
            <!--
    <v-btn color="warning" @click="resetValidation"> Reset Validation </v-btn>
    -->
          </v-form>
          <!-- REGISTER FROM END -->
        </v-card-text>
        <v-card-actions class="justify-end">
          <v-btn text @click="dialog.value = false">Close</v-btn>
        </v-card-actions>
      </v-card>
    </template>
  </v-dialog>
</template>

<script>
import config from "../../config/config";

export default {
  name: "ModalRegisterContent",
  data: function () {
    return {
      valid: true,
      name: "",
      nameRules: [
        (v) => !!v || "Name ist nötig",
        (v) => (v && v.length <= 20) || "Name must be less than 10 characters",
      ],
      password: "",
      showPassword: false,
      passwordRules: [
        (v) => !!v || "Password ist nötig",
        (v) =>
          (v && v.length > 8) || "Password muss mindestens 8 Zeichen lang sein",
      ],
      passwordRepeat: "",
      showPasswordRepeat: false,
      passwordRepeatRules: [
        (v) => !!v || "Password Wiederholung is nötig",
        (v) => v == this.password || "Passwords müssen übereinstimmen",
      ],

      select: null,
      usertype: [],

      //checkbox: false,

      show: false,
    };
  },
  mounted() {
    fetch(`${config.urls.backend.auth}/useritems`, {
      method: "get",
      headers: { ...config.headers },
    })
      .then((response) => {
        if (response.status === 200) {
          response.json().then((data) => {
            console.log(data);
            this.usertype = data;
            console.log(data);
          });
        }
      })
      .catch((e) => {
        console.log("error");
      });
  },
  methods: {
    register() {
      if (this.$refs.form.validate()) {
        let registerBody = {};
        registerBody.name = this.name;
        registerBody.password = this.password;
        registerBody.role = this.select;

        fetch(`${config.urls.backend.auth}/register`, {
          method: "post",
          headers: { ...config.headers },
          body: JSON.stringify(registerBody),
        })
          .then((response) => {
            if (response.status === 201) {
              response.json().then((data) => {
                this.show = false;
                console.log(data);
                this.$store.commit("toggleAlert", {
                  alertType: "success",
                  alertMessage: data.message,
                  showAlert: true,
                });
              });
            } else {
              this.$store.commit("toggleAlert", {
                alertType: "info",
                alertMessage: "Rgistrierung is fehlgeschlagen",
                showAlert: true,
              });
            }
          })
          .catch((e) => {
            this.$store.commit("toggleAlert", {
              alertType: "info",
              alertMessage: "Registrierung is fehlgeschlagen",
              showAlert: true,
            });
          });
      }
    },
    reset() {
      this.$refs.form.resetValidation();
      this.$refs.form.reset();
    },
  },
};
</script>

<style>
</style>