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
                                <v-card-title class="headline">Login</v-card-title>
                                <!--<v-card-subtitle>ou entre para controlar seu orçamento.</v-card-subtitle>-->

                                <v-card-text>
                                    <v-form>
                                        <v-text-field
                                            v-model="login.email"
                                            :error-messages="emailErrors"
                                            label="E-mail"
                                            :counter="50"
                                            required
                                            @input="$v.login.email.$touch()"
                                            @blur="$v.login.email.$touch()"
                                        ></v-text-field>

                                        <v-text-field
                                            v-model="login.senha"
                                            :error-messages="senhaErrors"
                                            label="Senha"
                                            :counter="16"
                                            required
                                            :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
                                            :type="showPassword ? 'text' : 'password'"
                                            @click:append="showPassword = !showPassword"
                                            @input="$v.login.senha.$touch()"
                                            @blur="$v.login.senha.$touch()"
                                        ></v-text-field>

                                        <template v-if="exibirMensagemErro">
                                            <div class="mensagem-erro">
                                                <span>{{ mensagemErro }}</span>
                                            </div>
                                        </template>

                                        <div class="actions-form">
                                            <v-btn color="success" dark @click="doLogin">Entrar</v-btn>
                                            <span class="texto-alteracao-rota" @click="alterarRota('index')">Ainda não possui uma conta? Cadastre-se</span>
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

import Login from '../services/login'

import { mapMutations } from 'vuex'

export default {
    name: 'Index',

    mixins: [validationMixin],

    validations: {
        login: {
            email: { required, email, minLength: minLength(8), maxLength: maxLength(50), },
            senha: { required, minLength: minLength(6), maxLength: maxLength(16) },
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
            dataCadastro: null
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

        doLogin() {
            this.$v.$touch()

            if (!this.$v.$invalid) {
                /**
                 * Foi preciso guardar a instância do data, pois o erro agora não é mais uma String
                 * e sim uma função, na qual é possível acessar o status code da requisição.
                 * O status code será necessário para exibir a mensagem de erro "genérico" ou erro de "usuário/senha".
                 * A função de error dentro do catch não reconhece a instância this do data, por isso ela é associada à variável vm.
                 */
                var vm = this
                

                Login.logar(this.login).then( resp => {

                    if(resp.status == 200) {
                        this.usuarioLogado.id               = resp.data.id;
                        this.usuarioLogado.nome             = resp.data.nome;
                        this.usuarioLogado.email            = resp.data.email
                        this.usuarioLogado.senha            = resp.data.senha
                        this.usuarioLogado.dataCadastro     = resp.data.dataCadastro
                        this.usuarioLogado.mesesCadastro    = resp.data.quantidadeMesesCadastro

                        this.SET_USUARIO_LOGADO(vm.usuarioLogado)
                        this.SET_STATUS_AUTENTICACAO(true)

                        this.alterarRota('home')
                    }

                }).catch( function(error) {
                    /**
                     * A exibição da mensagem de erro é tratada dentro da função error, onde não é possível acessar
                     * o data através da instância "this", por isso é utilizada a variável vm.
                     */
                    if(error.response != undefined) {
                        if(error.response.status == 401) {
                            vm.mensagemErro = "Usuário e Senha não conferem."
                        } else {
                            vm.mensagemErro = "Erro ao tentar realizar o login."
                        }
                    } else {
                        vm.mensagemErro = "Erro ao tentar realizar o login."
                    }
                    vm.exibirMensagemErro = true
                    setTimeout(() => {
                        vm.exibirMensagemErro = false
                    }, 5000);

                    
                })
            }
        },
        
    },

    computed: {
        emailErrors () {
            const errors = []
            
            if (!this.$v.login.email.$dirty) 
                return errors
            
            !this.$v.login.email.required && errors.push('E-mail é requerido.')

            !this.$v.login.email.minLength && errors.push('Seu e-mail deve ter no mínimo 8 caracteres.')

            !this.$v.login.email.maxLength && errors.push('Seu e-mail deve ter no máximo 50 caracteres.')

            !this.$v.login.email.email && errors.push('Insira um e-mail válido.')
            
            return errors
        },

        senhaErrors () {
            const errors = []
            
            if (!this.$v.login.senha.$dirty) 
                return errors

            !this.$v.login.senha.required && errors.push('Senha é requerida.')
            
            !this.$v.login.senha.minLength && errors.push('Sua senha deve ter no mínimo 6 caracteres.')

            !this.$v.login.senha.maxLength && errors.push('Sua senha deve ter no máximo 16 caracteres.')
            
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
