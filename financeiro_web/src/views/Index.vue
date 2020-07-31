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
                                <v-card-title class="headline">Criar uma nova conta</v-card-title>
                                <!--<v-card-subtitle>ou entre para controlar seu orçamento.</v-card-subtitle>-->

                                <v-card-text>
                                    <v-form>
                                        <v-text-field
                                            v-model="cadastro.nome"
                                            :error-messages="nomeErrors"
                                            :counter="40"
                                            label="Nome"
                                            required
                                            @input="$v.cadastro.nome.$touch()"
                                            @blur="$v.cadastro.nome.$touch()"
                                        ></v-text-field>

                                        <v-text-field
                                            v-model="cadastro.email"
                                            :error-messages="emailErrors"
                                            label="E-mail"
                                            required
                                            @input="$v.cadastro.email.$touch()"
                                            @blur="$v.cadastro.email.$touch()"
                                        ></v-text-field>

                                        <v-text-field
                                            v-model="cadastro.senha"

                                            :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
                                            :type="showPassword ? 'text' : 'password'"
                                            @click:append="showPassword = !showPassword"

                                            :error-messages="senhaErrors"
                                            :counter="10"
                                            label="Senha"
                                            required
                                            @input="$v.cadastro.senha.$touch()"
                                            @blur="$v.cadastro.senha.$touch()"
                                        ></v-text-field>

                                        <v-card-actions>
                                            <div class="my-2">
                                                <v-btn x-large color="success" dark @click="cadastrar">Cadastrar</v-btn>
                                            </div>
                                        </v-card-actions>
                                        <div>
                                            <span class="texto-alteracao-rota" @click="alterarRota">Já possui uma conta? Entre!</span>
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

import Index from '../services/index'

export default {
    name: 'Index',

    mixins: [validationMixin],

    validations: {
        cadastro: {
            nome:   { required, maxLength: maxLength(40), minLength: minLength(3) },
            email:  { required, email },
            senha:  { required, maxLength: maxLength(16), minLength: minLength(6) },
        }
    },

    data: () => ({

        cadastro: {
            nome: '',
            email: '',
            senha: ''
        },

        showPassword: false,
        
    }),

    mounted() {
        localStorage.clear()
    },

    methods: {
        alterarRota() {
            this.$router.push(
                {name: 'login'}
            )
        },

        cadastrar() {
            this.$v.$touch()

            if (!this.$v.$invalid) {
                Index.salvar(this.cadastro).then( () => {
                    alert('salvou')
                }).catch( () => {
                    alert("Erro ao tentar realizar o cadastro.")
                })
            }
        }
    },

    computed: {
        nomeErrors () {
            const errors = []
            
            if (!this.$v.cadastro.nome.$dirty) 
                return errors

            !this.$v.cadastro.nome.minLength && errors.push('Seu nome deve ter no mínimo 3 caracteres.')
            
            !this.$v.cadastro.nome.maxLength && errors.push('Seu nome deve ter no máximo 40 caracteres.')
            
            !this.$v.cadastro.nome.required && errors.push('Nome é requerido.')
            
            return errors
        },

        emailErrors () {
            const errors = []
            
            if (!this.$v.cadastro.email.$dirty) 
                return errors
            
            !this.$v.cadastro.email.email && errors.push('Insira um e-mail válido.')
            
            !this.$v.cadastro.email.required && errors.push('E-mail é requerido.')
            
            return errors
        },

        senhaErrors () {
            const errors = []
            
            if (!this.$v.cadastro.senha.$dirty) 
                return errors
            
            !this.$v.cadastro.senha.minLength && errors.push('Sua senha deve ter no mínimo 6 caracteres.')

            !this.$v.cadastro.senha.maxLength && errors.push('Sua senha deve ter no máximo 16 caracteres.')
            
            !this.$v.cadastro.senha.required && errors.push('Senha é requerida.')
            
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
