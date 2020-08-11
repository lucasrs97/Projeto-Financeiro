<template>
    <v-row justify="center">
        <v-dialog v-model="showModal" width="1000px" persistent hide-overlay transition="dialog-bottom-transition">
            <v-card>
                <v-toolbar dark color="indigo darken-2">
                    <v-btn icon dark @click="CLOSE_MODAL_EDIT_USER">
                        <v-icon>mdi-close</v-icon>
                    </v-btn>
                    <v-toolbar-title>Editar perfil</v-toolbar-title>
                    <v-spacer></v-spacer>
                    <v-toolbar-items>
                        <v-btn dark text @click="deletarConta">Apagar minha conta</v-btn>
                    </v-toolbar-items>
                </v-toolbar>
                
                <v-card-text>
                    <div class="user">
                   
                        <v-list color="grey">
                            <v-list-item>
                                <v-list-item-avatar>
                                    <v-avatar color="indigo" size="42" >
                                        <span class="white--text"> L </span>
                                    </v-avatar>
                                </v-list-item-avatar>
                            </v-list-item>

                            <v-list-item link>
                                <v-list-item-content>
                                    <v-list-item-title class="title">{{ nomeUsuario }}</v-list-item-title>
                                    <v-list-item-subtitle>{{ emailUsuario }}</v-list-item-subtitle>
                                </v-list-item-content>
                            </v-list-item>

                            <v-list-item>
                                <v-list-item-action>
                                    <v-btn color="info" dark @click="dialogChangePassword = true">Alterar Senha</v-btn>

                                    <v-dialog
                                        v-model="dialogChangePassword"
                                        max-width="320"
                                    >
                                        <v-card>
                                            <v-row>
                                                <v-col cols="12" >
                                                    <h3>Alterar Senha</h3>

                                                    <v-text-field
                                                        v-model="usuario.senha"
                                                        label="Senha Atual"
                                                        :error-messages="senhaErrors"
                                                        :counter="16"
                                                        :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
                                                        :type="showPassword ? 'text' : 'password'"
                                                        @click:append="showPassword = !showPassword"
                                                        @input="$v.usuario.senha.$touch()"
                                                        @blur="$v.usuario.senha.$touch()"
                                                    ></v-text-field>
                                            
                                                    <v-text-field
                                                        v-model="usuario.nova_senha"
                                                        label="Nova Senha"
                                                        :error-messages="novaSenhaErrors"
                                                        :counter="16"
                                                        :append-icon="showNewPassword ? 'mdi-eye' : 'mdi-eye-off'"
                                                        :type="showNewPassword ? 'text' : 'password'"
                                                        @click:append="showNewPassword = !showNewPassword"
                                                        @input="$v.usuario.nova_senha.$touch()"
                                                        @blur="$v.usuario.nova_senha.$touch()"
                                                    ></v-text-field>

                                                    <v-btn class="botao-alterar-senha" color="info" dark @click="atualizarSenhaUsuario">Atualizar Senha</v-btn>
                                                </v-col>
                                            </v-row>
                                        </v-card>
                                    </v-dialog>
                                </v-list-item-action>
                            </v-list-item>
                        </v-list>
                    
                        <v-row>
                            <v-col cols="12" >
                                <v-text-field
                                    v-model="usuario.nome"
                                    label="Nome"
                                    :error-messages="nomeErrors"
                                    :counter="40"
                                    @input="$v.usuario.nome.$touch()"
                                    @blur="$v.usuario.nome.$touch()"
                                ></v-text-field>
                           
                                <v-text-field
                                    v-model="usuario.email"
                                    label="E-mail"
                                    :error-messages="emailErrors"
                                    :counter="50"
                                    @input="$v.usuario.email.$touch()"
                                    @blur="$v.usuario.email.$touch()"
                                ></v-text-field>
                            
                                <v-btn color="success" dark @click="atualizarUsuario">Atualizar</v-btn>
                            </v-col>
                        </v-row>
                      
                    </div>
                </v-card-text>

                    <v-snackbar 
                        v-model="snackbar" 
                        :timeout="timeout"
                        top
                        color="error" 
                        absolute
                    >
                        <span>
                            <v-icon dark>mdi-alert</v-icon>
                            Erro ao tentar atualizar informações do usuário.
                        </span>
                        
                        <v-btn color="white" text @click="snackbar = false">Fechar</v-btn>
                    </v-snackbar>

            </v-card>
        </v-dialog>
    </v-row>
</template>

<script>
import User from '../services/usuarios'

import { mapMutations, mapActions } from 'vuex'

import { required, maxLength, minLength, email, sameAs } from 'vuelidate/lib/validators'

export default {
    data: () => ({
        usuario: {
            id: null,
            dataCadastro: null,
            nome: '',
            email: '',
            senha: '',
            nova_senha: '',
        },

        showPassword: false,
        showNewPassword: false,

        snackbar: false,
        timeout: 5000,

        dialogChangePassword: false,
    }),

    validations: {
        usuario: {
            nome: { required, minLength: minLength(3), maxLength: maxLength(40) },
            
            email: { required, email, minLength: minLength(8), maxLength: maxLength(50) },

            senha:  { 
                required,
                minLength: minLength(3),
                maxLength: maxLength(16), 
                sameAs: sameAs( function () {
                    return this.$store.state.usuarioLogado.senha
                })
            },

            nova_senha: { 
                required,
                minLength: minLength(3), 
                maxLength: maxLength(16) 
            },
        },
        
    },

    methods: {
        atualizarUsuario() {

            this.$v.usuario.nome.$touch()
            this.$v.usuario.email.$touch()

            if (!this.$v.usuario.nome.$invalid && !this.$v.usuario.email.$invalid) {

                this.usuario.id = this.$store.state.usuarioLogado.id
                this.usuario.dataCadastro = this.$store.state.usuarioLogado.dataCadastro
                this.usuario.senha = this.$store.state.usuarioLogado.senha

                User.salvar(this.usuario).then( response => {
                    this.SET_USUARIO_LOGADO(response.data)
                    this.limparFormulario()
                    this.CLOSE_MODAL_EDIT_USER()
                    this.SET_MENSAGEM_SNACKBAR('Seu cadastro foi alterado com sucesso.')
                }).catch( () => {
                    this.snackbar = true
                })
            }
        },

        atualizarSenhaUsuario() {

            this.$v.usuario.senha.$touch()
            this.$v.usuario.nova_senha.$touch()

            if (!this.$v.usuario.senha.$invalid && !this.$v.usuario.nova_senha.$invalid) {

                this.usuario.id = this.$store.state.usuarioLogado.id
                this.usuario.dataCadastro = this.$store.state.usuarioLogado.dataCadastro
                this.usuario.nome = this.$store.state.usuarioLogado.nome
                this.usuario.email = this.$store.state.usuarioLogado.email

                this.usuario.senha = this.usuario.nova_senha

                User.salvar(this.usuario).then( response => {
                    this.SET_USUARIO_LOGADO(response.data)
                    this.limparFormulario()
                    this.CLOSE_MODAL_EDIT_USER()
                    this.dialogChangePassword = false
                    this.SET_MENSAGEM_SNACKBAR('Sua senha foi alterada com sucesso.')
                }).catch( () => {
                    this.snackbar = true
                })
            }
        },

        deletarConta() {
            this.usuario.id               = this.$store.state.usuarioLogado.id
            this.usuario.nome             = this.$store.state.usuarioLogado.nome
            this.usuario.email            = this.$store.state.usuarioLogado.email
            this.usuario.senha            = this.$store.state.usuarioLogado.senha
            this.usuario.dataCadastro     = this.$store.state.usuarioLogado.dataCadastro

            User.deletar(this.usuario).then( () => {
                this.addSpinner(true)
                this.CLOSE_MODAL_EDIT_USER()
                this.$router.go() // Recarrega a página, redefinindo o estado do Vuex
                this.navigateTo('login')

                setTimeout(() => {    
                    this.addSpinner(false)
                }, 2000);
                
            }).catch( () => {
                alert("Erro!")
            })
        },

        navigateTo(where) {
            this.$router.push(
                {name: where} 
        ).catch(erro => {
                if(erro.name != "NavigationDuplicated") {
                    throw erro;
                }
            })
        },

        ...mapMutations([
            'CLOSE_MODAL_EDIT_USER',
            'SET_USUARIO_LOGADO',
        ]),

        ...mapActions([
            'SET_MENSAGEM_SNACKBAR',
            'addSpinner'
        ]),

        limparFormulario() {
            this.usuario.nome = '',
            this.usuario.email = '',
            this.usuario.senha = '',
            this.usuario.nova_senha = ''
        },
    },

    computed: {

        showModal() {
            var vm = this

            //  RESETA VALIDAÇÕES
            this.$v.$reset();

            //  LIMPA FORMULÁRIO
            this.limparFormulario()

            if(this.$store.state.showModalEditUser == true) {
                vm.usuario.nome = this.$store.state.usuarioLogado.nome
                vm.usuario.email = this.$store.state.usuarioLogado.email
                
                return true
            } else {
                return false
            }
  
        },

        nomeUsuario() {
            return this.$store.state.usuarioLogado.nome
        },

        emailUsuario() {
            return this.$store.state.usuarioLogado.email
        },

        nomeErrors () {
            const errors = []
            
            if (!this.$v.usuario.nome.$dirty) 
                return errors

            !this.$v.usuario.nome.required && errors.push('Nome é requerido.')

            !this.$v.usuario.nome.minLength && errors.push('Seu nome deve ter no mínimo 3 caracteres.')
            
            !this.$v.usuario.nome.maxLength && errors.push('Seu nome deve ter no máximo 40 caracteres.')
            
            return errors
        },

        emailErrors () {
            const errors = []
            
            if (!this.$v.usuario.email.$dirty) 
                return errors
            
            !this.$v.usuario.email.required && errors.push('E-mail é requerido.')

            !this.$v.usuario.email.email && errors.push('Insira um e-mail válido.')

            !this.$v.usuario.email.minLength && errors.push('Seu e-mail deve ter no mínimo 8 caracteres.')
            
            !this.$v.usuario.email.maxLength && errors.push('Seu e-mail deve ter no máximo 50 caracteres.')
            
            return errors
        },

        senhaErrors () {
            const errors = []
            
            if (!this.$v.usuario.senha.$dirty) 
                return errors

            !this.$v.usuario.senha.required && errors.push('Senha é requerida.')
            
            !this.$v.usuario.senha.minLength && errors.push('Sua senha deve ter no mínimo 6 caracteres.')

            !this.$v.usuario.senha.maxLength && errors.push('Sua senha deve ter no máximo 16 caracteres.')

            !this.$v.usuario.senha.sameAs && errors.push('Senha incorreta.')
            
            return errors
        },

        novaSenhaErrors () {
            const errors = []
            
            if (!this.$v.usuario.nova_senha.$dirty) 
                return errors

            !this.$v.usuario.nova_senha.required && errors.push('Confirme sua senha.')
            
            !this.$v.usuario.nova_senha.minLength && errors.push('Sua senha deve ter no mínimo 6 caracteres.')

            !this.$v.usuario.nova_senha.maxLength && errors.push('Sua senha deve ter no máximo 16 caracteres.')
            
            return errors
        }
    }
}
</script>

<style scoped>
    .user {
        display: flex;
        flex-direction: row;
        margin-top: 10px;
    }

    .row {
        margin: 0px;
    }

    .botao-alterar-senha {
        margin-top: 10px;
    }
</style>