<template>
    <div>
        <v-container>
          <!--<v-row>
            <v-col cols="1">
              <Registrations />
            </v-col>
          </v-row>-->

          <v-row dense>
            <v-col cols="12" sm="6" lg="3">
                <v-card
                color="#385F73"
                dark
                class="mycard"
                >
                  <v-card-title class="headline">R$ 10.895,00</v-card-title>
                  <v-card-subtitle>Total de Receitas</v-card-subtitle>
                  <v-card-actions>
                      <v-btn text>VER MAIS</v-btn>
                  </v-card-actions>
                </v-card>
            </v-col>
            <v-col cols="12" sm="6" lg="3">
                <v-card
                color="#385F73"
                dark
                class="mycard"
                >
                  <v-card-title class="headline">R$ 7.500,00</v-card-title>
                  <v-card-subtitle>Total de Despesas</v-card-subtitle>
                  <v-card-actions>
                      <v-btn text>VER MAIS</v-btn>
                  </v-card-actions>
                </v-card>
            </v-col>
            <v-col cols="12" sm="6" lg="3">
                <v-card
                color="#385F73"
                dark
                class="mycard"
                >
                  <v-card-title class="headline">R$ 895,00</v-card-title>
                  <v-card-subtitle>Caixa Mensal</v-card-subtitle>
                  <v-card-actions>
                      <v-btn text>VER MAIS</v-btn>
                  </v-card-actions>
                </v-card>
            </v-col>
            <v-col cols="12" sm="6" lg="3">
                <v-card
                color="#385F73"
                dark
                class="mycard"
                >
                  <v-card-title class="headline">R$ 2.500,00</v-card-title>
                  <v-card-subtitle>Patrimônio Acumulado</v-card-subtitle>
                  <v-card-actions>
                      <v-btn text>VER MAIS</v-btn>
                  </v-card-actions>
                </v-card>
            </v-col>
          </v-row>
        </v-container>

    </div>
</template>

<script>

/*import Registrations from '../components/Registrations'*/

export default {
    name: 'Home',

    /*components: {
        Registrations
    }*/
}
</script>

<style scoped>
  .mycard:hover {
    transform: scale(1.050);
    transition: all 200ms ease-in;
    cursor: pointer;
    opacity: 0.9;
  }
</style>