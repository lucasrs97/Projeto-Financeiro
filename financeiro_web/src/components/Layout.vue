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
                            <v-avatar color="indigo" size="42" >
                                <span class="white--text"> {{ letraUsuario }} </span>
                            </v-avatar>
                        </v-list-item-avatar>

                        <v-list-item-content>
                            <v-list-item-title>{{ nomeUsuario }}</v-list-item-title>
                            <v-list-item-subtitle>Início em {{ dataCadastro }} </v-list-item-subtitle>
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
                    
                    <!-- MODAL -->
                    <Registrations />

                    <Transfer />

                    <EditUser />
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
                        <span v-bind="attrs" v-on="on">
                            <v-icon>mdi-account</v-icon>
                            Olá, {{ nomeUsuario }}
                            <v-icon>mdi-chevron-down</v-icon>
                        </span>
                        
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
import Registrations from '../components/Registrations'
import Transfer from '../components/Transfer'
import EditUser from '../components/EditUser'

import { mapActions, mapMutations } from 'vuex'

export default {
    name: 'App',

    components: {
        Registrations, Transfer, EditUser
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
        },

        nomeUsuario() {
            return this.$store.state.usuarioLogado.nome
        },

        letraUsuario() {
            if(this.nomeUsuario == null) {
                return null
            } else {
                return this.nomeUsuario.substring(0, 1);
            }
        },

        dataCadastro() {
            let data = this.$store.state.usuarioLogado.dataCadastro

            if(data != null) {
                let ano = data.substring(0, 4)
                let mes = data.substring(5, 7)
            
                return mes + "/" + ano
            } else {
                return data
            }

            
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
            this.OPEN_MODAL_EDIT_USER()
        } else if(index == 1) {
            alert('Configurações')
        } else if(index == 2) {

            this.addSpinner(true)

            setTimeout(() => {
                this.addSpinner(false)
                this.SET_STATUS_AUTENTICACAO(false)
                this.$router.go() // Recarrega a página, redefinindo o estado do Vuex
                this.navigateTo('login')
            }, 1000);

        }
    },

    ...mapActions([
        'OPEN_MODAL',
        'addSpinner'
    ]),

    ...mapMutations([
        'SET_STATUS_AUTENTICACAO',
        'OPEN_MODAL_EDIT_USER'
    ]),

  }
};
</script>
