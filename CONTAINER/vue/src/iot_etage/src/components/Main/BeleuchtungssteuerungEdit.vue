<template>
  <v-container>
    <v-row>
      <v-col class="text-center"> Belechtungssteuerung Ändern</v-col>
    </v-row>
    <v-row>
      <v-col cols="8" class="text-center mx-auto">
        <v-card>
          <v-list-item three-line>
            <v-list-item-content>
              <div class="overline mb-4">Beleuchtungssteuerung</div>
              <v-list-item-title class="headline mb-1">
                Einstellungen wechseln
              </v-list-item-title>
              <v-list-item-subtitle>
                <v-container>
                  <!-- Hier mit template wenn im Modus automatisch oder manuell-->
                  <!--
                  <v-row>
                    <v-col cols="6">Automatisch</v-col>
                    <v-col cols="6"></v-col>
                  </v-row>
                  <v-row>
                    <v-col cols="6">Leuchkraft</v-col>
                    <v-col cols="6"
                      >4000 <span class="font-weight-bold">Lumen</span>
                    </v-col>
                  </v-row>
                  -->
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
  name: "BeleuchtungssteuerungEdit",
};
</script>

<style>
</style>