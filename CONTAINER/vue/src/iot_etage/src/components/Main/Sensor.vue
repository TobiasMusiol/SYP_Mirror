<template>
  <div>
    <v-container>
      <v-row>
        <v-col class="text-center pb-0">Sensoren</v-col>
      </v-row>
    </v-container>
    <v-data-table
      :headers="headers"
      :items="sensors"
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
export default {
  data: function () {
    return {
      appName: "sensors",
      search: "",
      sensors: [],
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
          text: "UID",
          value: "uid",
        },
        {
          text: "Sensor Typ",
          value: "sensorType",
        },
        {
          text: "Payload",
          value: "payload",
        },
        {
          text: "Datum",
          value: "timestamp",
        },
      ];
    },
  },

  mounted() {
    fetch(`${config.urls.backend.base}/${this.appName}`, {
      methode: "get",
      headers: {
        ...config.headers,
        "Authorization": localStorage.getItem("user-token"),
      },
    }).then((response) => {
      if (response.status === 200) {
        response.json().then((data) => {
          this.sensors = data;
        });
      }
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