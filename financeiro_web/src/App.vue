<template>
<div id="app">
  <v-app id="inspire">
    <v-navigation-drawer
      v-model="drawer"
      app
      color="cyan lighten-3"
    >
      <v-list dense>

        <v-list-item two-line>
          <v-list-item-avatar>
            <img src="https://randomuser.me/api/portraits/women/54.jpg">
          </v-list-item-avatar>

          <v-list-item-content>
            <v-list-item-title>Lucas</v-list-item-title>
            <v-list-item-subtitle>Acumulação de Patrimônio</v-list-item-subtitle>
          </v-list-item-content>
        </v-list-item>

        

        <v-divider></v-divider>
        <v-list-item link>
          <v-subheader>LANÇAMENTOS</v-subheader>
        </v-list-item>
        
        <!--
        <v-list-item link>
          <v-list-item-action>
            <v-icon>mdi-plus</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title>Receitas</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
        <v-list-item link>
          <v-list-item-action>
            <v-icon>mdi-minus</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title>Despesas</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
        -->

        <v-list-item link>
          <v-list-item-content>
            <Registrations :tipoCadastro="1"/>
          </v-list-item-content>
        </v-list-item>
        <v-list-item link>
          <v-list-item-content>
            <Registrations :tipoCadastro="2"/>
          </v-list-item-content>
        </v-list-item>

        <v-divider></v-divider>
        <v-list-item link>
          <v-subheader>DASHBOARD</v-subheader>
        </v-list-item>
        <v-list-item link @click="navigateTo('home')">
          <v-list-item-action>
            <v-icon>mdi-home</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title>Home</v-list-item-title>
          </v-list-item-content>
        </v-list-item>

        <v-divider></v-divider>
        <v-list-item link>
          <v-subheader>RELATÓRIOS</v-subheader>
        </v-list-item>
        <v-list-item link @click="navigateTo('releases')">
          <v-list-item-action>
            <v-icon>mdi-chart-bar</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title>Lançamentos</v-list-item-title>
          </v-list-item-content>
        </v-list-item>

      </v-list>
    </v-navigation-drawer>
  
    <v-app-bar
      app
      color="indigo darken-2"
      dark
    >
      <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>

      <v-toolbar-title>Meu Patrimônio</v-toolbar-title>

      <v-spacer></v-spacer>
  
      <v-avatar color="cyan lighten-3" size="36">
        <span class="white--text">L</span>
      </v-avatar>
        
    </v-app-bar>
  
      <v-content>
        <v-container
          fluid
        >
          <router-view />
          
        </v-container>
      </v-content>
      
      <v-footer color="indigo darken-2" app>
        <span class="white--text">&copy; 2020</span>
        <v-spacer></v-spacer>
        <span class="white--text">v1.0</span>
      </v-footer>
  </v-app>
</div>
</template>

<script>
import Registrations from '../src/components/Registrations'

export default {
  name: 'App',

  components: {
    Registrations
  },

  data: () => ({
    drawer: null,
  }),

  methods: {
    navigateTo(where) {
      this.$router.push( {name: where} )
    },
    
    mapMutations() {
      alert("Chamou o evento!");
    }
  }
};
</script>
