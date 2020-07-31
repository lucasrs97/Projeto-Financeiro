<template>
    <div id="app">
        <v-app id="inspire">
            <v-app-bar
            app
            color="indigo darken-2"
            dark
            >
                <v-toolbar-title>Meu Patrimônio</v-toolbar-title>
            </v-app-bar>
        
            <v-content>
                <v-container fluid>
                    
                    
                    <div class="form-cadastro">
                        <v-col cols="3">
                            <v-card>
                                <v-card-title class="headline">Login</v-card-title>
                                <!--<v-card-subtitle>ou entre para controlar seu orçamento.</v-card-subtitle>-->

                                <v-card-text>
                                    <v-form>
                                        <v-text-field
                                            v-model="login.email"
                                            :error-messages="emailErrors"
                                            label="E-mail"
                                            required
                                            @input="$v.login.email.$touch()"
                                            @blur="$v.login.email.$touch()"
                                        ></v-text-field>

                                        <v-text-field
                                            v-model="login.senha"

                                            :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
                                            :type="showPassword ? 'text' : 'password'"
                                            @click:append="showPassword = !showPassword"

                                            :error-messages="senhaErrors"
                                            :counter="10"
                                            label="Senha"
                                            required
                                            @input="$v.login.senha.$touch()"
                                            @blur="$v.login.senha.$touch()"
                                        ></v-text-field>

                                        <v-card-actions>
                                            <div class="my-2">
                                                <v-btn x-large color="success" dark @click="doLogin">Entrar</v-btn>
                                            </div>
                                        </v-card-actions>
                                        <div>
                                            <span class="texto-alteracao-rota" @click="alterarRota('index')">Ainda não possui uma conta? Cadastre-se</span>
                                        </div>
                                    </v-form>
                                </v-card-text>
                            </v-card>
                        </v-col> 
                    </div>

                </v-container>
            </v-content>
        </v-app>
    </div>
</template>

<script>

import { validationMixin } from 'vuelidate'
import { required, maxLength, minLength, email } from 'vuelidate/lib/validators'

import Login from '../services/login'

import { mapMutations } from 'vuex'

export default {
    name: 'Index',

    mixins: [validationMixin],

    validations: {
        login: {
            email: { required, email },
            senha: { required, maxLength: maxLength(16), minLength: minLength(3) },
        }
        
    },

    data: () => ({
        login: {
            email: '',
            senha: ''
        },

        usuarioLogado: {
            id: '',
            nome: '',
            email: '',
            senha: '',
            data: null
        },

        showPassword: false,
        
    }),

    mounted() {
        localStorage.clear()
    },

    methods: {
        alterarRota(where) {
            this.$router.push(
                {name: where}
            )
        },

        ...mapMutations([
            'SET_STATUS_AUTENTICACAO',
            'SET_USUARIO_LOGADO'
        ]),

        doLogin() {
            this.$v.$touch()

            if (!this.$v.$invalid) {
                Login.logar(this.login).then( resp => {

                    if(resp.status == 200) {

                        console.log(resp)

                        this.usuarioLogado.id = resp.data.id;
                        this.usuarioLogado.nome = resp.data.nome;
                        this.usuarioLogado.email = resp.data.email
                        this.usuarioLogado.senha = resp.data.senha
                        this.usuarioLogado.data = resp.data.dataCadastro
                        this.SET_USUARIO_LOGADO(this.usuarioLogado)
                        this.SET_STATUS_AUTENTICACAO(true)

                        this.alterarRota('home')

                        //setTimeout(() => this.alterarRota('home') , 2000)
                        
                    }

                }).catch( () => {
                    alert("Erro ao tentar realizar o login.")
                })
            }
        },
        
    },

    computed: {
        emailErrors () {
            const errors = []
            
            if (!this.$v.login.email.$dirty) 
                return errors
            
            !this.$v.login.email.email && errors.push('Insira um e-mail válido.')
            
            !this.$v.login.email.required && errors.push('E-mail é requerido.')
            
            return errors
        },

        senhaErrors () {
            const errors = []
            
            if (!this.$v.login.senha.$dirty) 
                return errors
            
            !this.$v.login.senha.minLength && errors.push('Sua senha deve ter no mínimo 6 caracteres.')

            !this.$v.login.senha.maxLength && errors.push('Sua senha deve ter no máximo 16 caracteres.')
            
            !this.$v.login.senha.required && errors.push('Senha é requerida.')
            
            return errors
        },
    }
}
</script>

<style scoped>
    .form-cadastro {
        display: flex;
        justify-content: center;
        align-items: center;

        height: 70vh;
    }

    .texto-alteracao-rota:hover {
        cursor: pointer;
        color: black;
    }
</style>
