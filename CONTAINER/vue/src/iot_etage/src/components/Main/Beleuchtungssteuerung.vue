<template>
  <v-container>
    <v-row class="my-5">
      <v-col class="text-center"> Beleuchtungssteuerung </v-col>
    </v-row>
    <v-row>
      <v-col cols="8" class="text-center mx-auto">
        <v-card>
          <v-list-item three-line>
            <v-list-item-content>
              <div class="overline mb-4">Beleuchtungssteuerung</div>
              <v-list-item-title class="headline mb-1">
                Live Daten
              </v-list-item-title>
              <v-list-item-subtitle>
                <v-container class="text-left">
                  <v-row>
                    <v-col cols="6">Modus</v-col>
                    <v-col cols="6">Automatisch</v-col>
                  </v-row>
                  <v-row>
                    <v-col cols="6">Leuchkraft</v-col>
                    <v-col cols="6"
                      >4000 <span class="font-weight-bold">Lumen</span>
                    </v-col>
                  </v-row>
                </v-container>
              </v-list-item-subtitle>
            </v-list-item-content>

            <v-list-item-avatar tile size="80" color="grey">
              <v-icon color="blue" x-large> fas fa-sun </v-icon>
            </v-list-item-avatar>
          </v-list-item>
          <v-card-actions class="justify-center">
            <v-btn outlined rounded text> Button </v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  name: "Beleuchtungssteuerung",
};
</script>

<style>
</style>