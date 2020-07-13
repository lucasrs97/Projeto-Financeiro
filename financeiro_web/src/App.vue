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
                    <v-list-item>
                        <v-subheader>LANÇAMENTOS</v-subheader>
                    </v-list-item>

                    <v-list-item>
                        <v-list-item-content>
                            <v-menu
                                :close-on-click="true"
                                :close-on-content-click="true"
                                :offset-x="true"
                                :offset-y="false"
                            >
                                <template v-slot:activator="{ on, attrs }">
                                    <v-btn color="primary" dark v-bind="attrs" v-on="on">
                                        <v-icon left>mdi-plus</v-icon>
                                        NOVO
                                    </v-btn>
                                </template>

                                <v-list>
                                    <v-list-item
                                        v-for="(item, index) in items"
                                        :key="index"
                                        @click="abrirModal(index)"
                                    >
                                        <v-icon left> {{ item.icon }} </v-icon>
                                        <v-list-item-title>{{ item.title }}</v-list-item-title>
                                    </v-list-item>
                                </v-list>
                        </v-menu>
                    </v-list-item-content>
                </v-list-item>
                    <!--
                    <v-list-item link @click="OPEN_MODAL('Receita')">
                        <v-list-item-content>
                            <v-btn color="primary" dark small>
                                <v-icon left>mdi-plus-circle</v-icon>
                                NOVA RECEITA
                            </v-btn>
                        </v-list-item-content>
                    </v-list-item>

                    <v-list-item link @click="OPEN_MODAL('Despesa')">
                        <v-list-item-content>
                            <v-btn color="primary" dark small>
                                <v-icon left>mdi-minus-circle</v-icon>
                                NOVA DESPESA
                            </v-btn>
                        </v-list-item-content>
                    </v-list-item>

                    <v-list-item link @click="OPEN_MODAL('Transferir')">
                        <v-list-item-content>
                            <v-btn color="primary" dark small>
                                <v-icon left>mdi-minus-circle</v-icon>
                                GERENCIAR PATRIMÔNIO
                            </v-btn>
                        </v-list-item-content>
                    </v-list-item>
                    -->
                    
                    <!-- MODAL -->
                    <Registrations />
                    <!-- MODAL -->

                    <!-- MODAL -->
                    <Transfer />
                    <!-- MODAL -->

                    <v-divider></v-divider>
                    <v-list-item>
                        <v-subheader>DASHBOARD</v-subheader>
                    </v-list-item>
                    <v-list-item link @click="navigateTo('home')">
                        <v-list-item-icon>
                            <v-icon>mdi-home</v-icon>
                        </v-list-item-icon>
                        <v-list-item-content>
                            <v-list-item-title>Home</v-list-item-title>
                        </v-list-item-content>
                    </v-list-item>

                    <v-divider></v-divider>
                    <v-list-item>
                        <v-subheader>RELATÓRIOS</v-subheader>
                    </v-list-item>
                    <v-list-item link @click="navigateTo('releases')">
                        <v-list-item-icon>
                            <v-icon>mdi-chart-bar</v-icon>
                        </v-list-item-icon>
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

                <v-menu
                    :close-on-click="true"
                    :close-on-content-click="true"
                    :offset-y="true"
                >
                    <template v-slot:activator="{ on, attrs }">
                         <v-avatar color="cyan lighten-3" size="42" v-bind="attrs" v-on="on">
                            <span class="white--text">L</span>
                        </v-avatar>
                    </template>

                    <v-list>
                        <v-list-item
                            v-for="(item, index) in itemsAvatar"
                            :key="index"
                            @click="abrirOpcoes(index)"
                        >
                            <v-icon left> {{ item.icon }} </v-icon>
                            <v-list-item-title>{{ item.title }}</v-list-item-title>
                        </v-list-item>
                    </v-list>
                </v-menu>
                
            </v-app-bar>
        
            <v-content>
                <v-container fluid>
                    <v-snackbar v-model="snackbar" :timeout="timeout" multi-line top
                        color="info" >
                        
                        <span>
                            <v-icon dark>mdi-check-circle</v-icon>
                            {{ textSnackbar }} 
                        </span>
                        
                        <v-btn color="white" text @click="snackbar = false">Fechar</v-btn>
                    </v-snackbar>

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
import Transfer from '../src/components/Transfer'

import { mapActions } from 'vuex'

export default {
    name: 'App',

    components: {
        Registrations, Transfer
    },

    data: () => ({
        drawer: null,
        timeout: 5000,


        items: [
            { title: 'Receitas', icon: 'mdi-plus' },
            { title: 'Despesas', icon: 'mdi-minus' },
            { title: 'Gerenciar Patrimônio', icon: 'mdi-chart-line-variant' },
        ],

        itemsAvatar: [
            { title: 'Editar Meu Perfil', icon: 'mdi-account' },
            { title: 'Configurações', icon: 'mdi-settings' },
            { title: 'Sair', icon: 'mdi-exit-to-app' }
        ],

  }),

    computed: {
        snackbar: {
            get: function() {
                return this.$store.state.snackbar
            },

            set: function() {
                this.$store.dispatch('RESET_SNACKBAR', false)
            }
        },

        textSnackbar() {
            return this.$store.state.textSnackbar
        }
    },

    methods: {
        navigateTo(where) {
            this.$router.push(
                {name: where} 
        ).catch(erro => {
            if(erro.name != "NavigationDuplicated") {
                throw erro;
            }
        })
    },

    abrirModal(index) {
        if(index == 0) {
            this.OPEN_MODAL('Receita')
        } else if(index == 1) {
            this.OPEN_MODAL('Despesa')
        } else if(index == 2) {
            this.OPEN_MODAL('Transferir')
        }
    },

    abrirOpcoes(index) {
        if(index == 0) {
            alert('Editar Perfil')
        } else if(index == 1) {
            alert('Configurações')
        } else if(index == 2) {
            alert('Sair')
        }
    },

    ...mapActions([
        'OPEN_MODAL'
    ]),

  }
};
</script>
