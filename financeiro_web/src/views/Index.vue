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
                        
                            <v-card width="500px">

                                <v-card-title>Criar uma nova conta</v-card-title>
                                <!--<v-card-subtitle>ou entre para controlar seu orçamento.</v-card-subtitle>-->

                                <v-card-text>
                                    <v-form>
                                        <v-text-field
                                            v-model="cadastro.nome"
                                            :error-messages="nomeErrors"
                                            label="Nome"
                                            :counter="40"
                                            required
                                            @input="$v.cadastro.nome.$touch()"
                                            @blur="$v.cadastro.nome.$touch()"
                                        ></v-text-field>

                                        <v-text-field
                                            v-model="cadastro.email"
                                            :error-messages="emailErrors"
                                            label="E-mail"
                                            :counter="50"
                                            required
                                            @input="$v.cadastro.email.$touch()"
                                            @blur="$v.cadastro.email.$touch()"
                                        ></v-text-field>

                                        <v-text-field
                                            v-model="cadastro.senha"
                                            :error-messages="senhaErrors"
                                            label="Senha"
                                            :counter="16"
                                            required
                                            :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
                                            :type="showPassword ? 'text' : 'password'"
                                            @click:append="showPassword = !showPassword"
                                            @input="$v.cadastro.senha.$touch()"
                                            @blur="$v.cadastro.senha.$touch()"
                                        ></v-text-field>

                                        <template v-if="exibirMensagemErro">
                                            <div class="mensagem-erro">
                                                <span>{{ mensagemErro }}</span>
                                            </div>
                                        </template>

                                        <div class="actions-form">
                                            <v-btn color="success" dark @click="cadastrar">Cadastrar</v-btn>
                                            <span class="texto-alteracao-rota" @click="alterarRota('login')">Já possui uma conta? Entre!</span>
                                        </div>
                                    </v-form>
                                </v-card-text>
                            </v-card>

                            
                        
                    </div>

                </v-container>
            </v-content>
        </v-app>
    </div>
</template>

<script>

import { validationMixin } from 'vuelidate'
import { required, maxLength, minLength, email } from 'vuelidate/lib/validators'

import User from '../services/usuarios'

import { mapMutations, mapActions } from 'vuex'

export default {
    name: 'Index',

    mixins: [validationMixin],

    validations: {
        cadastro: {
            nome:   { required, minLength: minLength(3), maxLength: maxLength(40) },
            email:  { required, email, minLength: minLength(8), maxLength: maxLength(50) },
            senha:  { required, minLength: minLength(6), maxLength: maxLength(16) },
        }
    },

    data: () => ({

        cadastro: {
            id: '',
            nome: '',
            email: '',
            senha: '',
            dataCadastro: '',
            mesesCadastro: ''
        },

        showPassword: false,

        exibirMensagemErro: false,
        mensagemErro: ''
        
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

        ...mapActions([
            'SET_MENSAGEM_SNACKBAR'
        ]),

        cadastrar() {
            this.$v.$touch()

            if (!this.$v.$invalid) {
                var vm = this

                User.salvar(this.cadastro).then( response => {
                    console.log(response.data)

                    this.cadastro.id               = response.data.id;
                    this.cadastro.nome             = response.data.nome;
                    this.cadastro.email            = response.data.email
                    this.cadastro.senha            = response.data.senha
                    this.cadastro.dataCadastro     = response.data.dataCadastro
                    this.cadastro.mesesCadastro    = response.data.quantidadeMesesCadastro

                    this.SET_USUARIO_LOGADO(this.cadastro)
                    this.SET_STATUS_AUTENTICACAO(true)

                    this.alterarRota('home')
                    
                    setTimeout(() => {
                        this.SET_MENSAGEM_SNACKBAR('Cadastro realizado com sucesso.')    
                    }, 1000);
                    
                    
                }).catch( function(error) {

                    if(error.response != undefined) {
                        if(error.response.status == 406) {
                            vm.mensagemErro = "Já possui um cadastro para esse e-mail."
                        } else {
                            vm.mensagemErro = "Erro ao tentar realizar o cadastro"
                        }
                    } else {
                        vm.mensagemErro = "Erro ao tentar realizar o cadastro"
                    }
                    vm.exibirMensagemErro = true
                    setTimeout(() => {
                        vm.exibirMensagemErro = false
                    }, 5000);
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
    }

    .texto-alteracao-rota {
        margin-top: 10px;
    }

    .texto-alteracao-rota:hover {
        cursor: pointer;
        color: black;
    }

    .actions-form {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
    }

    .mensagem-erro {
        display: flex;
        justify-content: center;

        margin-bottom: 10px;

        color: red;
        font-weight: bold;
    }
    
</style>
