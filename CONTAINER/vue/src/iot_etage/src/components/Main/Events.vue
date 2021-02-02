<template>
  <div>
    <v-container>
      <v-row>
        <v-col class="text-center pb-0">Events</v-col>
      </v-row>
    </v-container>
    <v-data-table
      :headers="headers"
      :items="events"
      item-key="name"
      class="elevation-1"
      :search="search"
      :custom-filter="filterOnlyCapsText"
    >
      <template v-slot:top>
        <v-text-field
          v-model="search"
          label="Suchen (IN GROßBUCHSTABEN EINGEBEN)"
          class="mx-4"
        ></v-text-field>
      </template>
      <!--
      <template v-slot:body.append>
        <tr>
          <td></td>
          <td>
            <v-text-field
              v-model="calories"
              type="number"
              label="Less than"
            ></v-text-field>
          </td>
          <td colspan="4"></td>
        </tr>
      </template>
      -->
    </v-data-table>
  </div>
</template>

<script>
import config from "../../config/config";
// import axios from "axios";

export default {
  name: "Event",
  data() {
    return {
      appName: "events",
      search: "",
      // calories: '',
      events: [],
    };
  },
  computed: {
    headers() {
      return [
        {
          text: "ID",
          align: "start",
          sortable: false,
          value: "id",
        },
        {
          text: "Aktion",
          value: "action",
          /*
        filter: (value) => {
          if (!this.calories) {
            return true;
          }
          return value < parseInt(this.calories);
        },
        */
        },
        { text: "Alter Zustand", value: "oldState" },
        { text: "Neuer Zustand", value: "newState" },
        { text: "Trigger", value: "trigger" },
        { text: "Datum", value: "timestamp" },
      ];
    },
  },

  mounted() {
    /*
    // axios.defaults.baseURL = config.urls.backend;
    // axios.get(`/${this.appName}`).then((response) => {
    //   this.events = response.data;
    // });
    */

    console.log();
    let my_header = new Headers({
      // ...config.headers,
      "Authorization": localStorage.getItem("user-token"),
    });
    console.log(my_header.get("Authorizatin"));

    fetch(`${config.urls.backend.base}/${this.appName}`, {
      method: "get",
      headers: {
        ...config.headers,
        "Authorization": localStorage.getItem("user-token"),
      },
    })
      .then((response) => {
        if (response.status === 200) {
          console.log(response);
          response.json().then((data) => {
            console.log(data);
            this.events = data;
          });
        }
      })
      .catch((e) => {
        console.log(e);
      });
  },
  methods: {
    filterOnlyCapsText(value, search, item) {
      return (
        value != null &&
        search != null &&
        typeof value === "string" &&
        value.toString().toLocaleUpperCase().indexOf(search) !== -1
      );
    },
  },
};
</script>

<style>
</style>