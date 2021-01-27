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
                  <v-row align="center">
                    <v-col cols="6">Modus</v-col>
                    <v-col cols="6">
                      <v-switch v-model="switch1" :label="`${switch1.toString() == 'true' ? 'Automatisch' : 'Manuell'}`"></v-switch>
                    </v-col>
                  </v-row>
                  <template v-if="switch1.toString() == 'false'">
                    <v-row align="center">
                      <v-col cols="6">Helligkeit</v-col>
                      <v-col cols="6">
                        <v-slider v-model="brightness" step="1" thumb-label ticks append-icon="mdi-lightbulb"></v-slider>
                      </v-col>
                    </v-row>
                  </template>
                </v-container>
              </v-list-item-subtitle>
            </v-list-item-content>

            <v-list-item-avatar tile size="80" color="grey">
              <v-icon color="blue" x-large> fas fa-sun </v-icon>
            </v-list-item-avatar>
          </v-list-item>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  name: "Beleuchtungssteuerung",
  data () {
    return {
      switch1: true,
      brightness: 0,
    }
  },
};
</script>

<style>
</style>