<template>
    <div>
        <v-dialog v-model="showModalTransfer" persistent width="800px" >
            <div>
                <v-container class="containerModal">
                    <v-row>
                        <v-col>
                            <div class="titulo">
                                <v-spacer></v-spacer>

                                <h1>O que você deseja fazer?</h1>

                                <v-spacer></v-spacer>

                                <v-btn icon @click="CLOSE_MODAL_TRANSFERIR">
                                    <v-icon large>mdi-close</v-icon>
                                </v-btn>
                            </div>
                        </v-col>
                    </v-row>

                    <v-row>
                        <v-col>
                            <div class="cards">
                                <div class="opcaoGuardar" @click="OPEN_MODAL_GUARDAR">
                                    <h3>Guardar dinheiro</h3>
                                </div>
                                <div class="opcaoResgatar" @click="OPEN_MODAL_RESGATAR" >
                                    <h3>Resgatar dinheiro</h3>
                                </div>
                            </div>
                        </v-col>
                    </v-row>
                </v-container>
            </div>
        </v-dialog>

        <v-dialog v-model="showModaGuardar" persistent width="500" >
            <div>
                <v-container class="containerModal">
                    <v-row>
                        <v-col>
                            <div class="titulo">
                                <h1>Quanto você quer guardar?</h1>

                                <v-spacer></v-spacer>

                                <v-btn icon @click="CLOSE_MODAL_GUARDAR">
                                    <v-icon large>mdi-close</v-icon>
                                </v-btn>
                            </div>
                        </v-col>
                    </v-row>

                    <v-row>
                        <v-col>
                            <div class="cards">
                                <div>
                                    <form>
                                        <v-row>
                                            <v-col >
                                                <v-text-field
                                                    v-model.lazy="patrimonio.valor"
                                                    @change="formatarValor"
                                                    prepend-icon="R$"
                                                    reverse 
                                                    rounded
                                                    class="valorTransfere"
                                                ></v-text-field>
                                            </v-col>
                                            <v-col class="botaoTransfere">
                                                <v-btn class="mx-2" dark color="black" @click="guardarDinheiro()">
                                                    Aumentar Patrimônio
                                                </v-btn>
                                            </v-col>
                                        </v-row>                                 
                                    </form>
                                </div>
                            </div>
                        </v-col>
                    </v-row>
                </v-container>
            </div>
        </v-dialog>

        <v-dialog v-model="showModalResgatar" persistent width="550" >
            <div>
                <v-container class="containerModal">
                    <v-row>
                        <v-col>
                            <div class="titulo">
                                <h1>Quanto você quer resgatar?</h1>

                                <v-spacer></v-spacer>

                                <v-btn icon @click="CLOSE_MODAL_RESGATAR">
                                    <v-icon large>mdi-close</v-icon>
                                </v-btn>
                            </div>
                        </v-col>
                    </v-row>

                    <v-row>
                        <v-col>
                            <div class="cards">
                                <div>
                                    <form>
                                        <v-row>
                                            <v-col cols="6">
                                                <v-text-field
                                                    v-model.lazy="patrimonio.valor"
                                                    @change="formatarValor"
                                                    prepend-icon="R$"
                                                    reverse 
                                                    rounded
                                                    class="valorTransfere"

                                                    :error-messages="valorErrors"
                                                    @input="$v.patrimonio.valor.$touch()"
                                                    @blur="$v.patrimonio.valor.$touch()"

                                                    @keypress="apenasNumeros"
                                                ></v-text-field>
                                            </v-col>
                                            <v-col cols="5" >
                                                <v-btn class="mx-2 botaoTransfere" dark color="black" @click="resgatarDinheiro()">
                                                    Resgatar Patrimônio
                                                </v-btn>
                                            </v-col>
                                        </v-row>                               
                                    </form>
                                </div>
                            </div>
                        </v-col>
                    </v-row>
                </v-container>
            </div>
        </v-dialog>

    </div>
</template>

<script>

import Patrimonio from '../services/patrimonio'

import { mapMutations, mapActions } from 'vuex'

import { required, numeric } from 'vuelidate/lib/validators'

export default {
    name: 'Transferência',

    data:() => {
        return {
            patrimonio: {
                identificadorUsuario: '',
                valor: '',
                data: new Date,
            }
            
        }
        
    },

    validations: {
        patrimonio: {
            valor: { required, numeric }
        }
    },

    computed: {
        showModalTransfer() {
            if(this.$store.state.showModalTransferir == true) {
                return true
            } else {
                return false
            }
        },

        showModaGuardar() {
            var vm = this

            if(this.$store.state.showModalGuardar == true) {
                vm.valor = ''
                return true
            } else {
                return false
            }
        },

        showModalResgatar() {
            var vm = this

            if(this.$store.state.showModalResgatar == true) {
                vm.patrimonio.valor = ''
                return true
            } else {
                return false
            }
        },

        valorErrors () {
            const errors = []
            if (!this.$v.patrimonio.valor.$dirty) 
                return errors
            
            !this.$v.patrimonio.valor.required && errors.push('Preencha um valor para resgatar')
                return errors
        },
    },

    methods: {

        apenasNumeros: function (event) {
            if (! (event.charCode >= 48 && event.charCode <= 57) || (event.keyCode == 44)) {
                console.log('INválido')
            } 
            
        },

        guardarDinheiro() {

            this.patrimonio.identificadorUsuario = this.$store.state.usuarioLogado.id;

            let valorSemFormatacao = this.retiraFormatacao(this.patrimonio.valor)
            this.patrimonio.valor = valorSemFormatacao

            Patrimonio.guardar(this.patrimonio).then( () => {

                this.SET_MENSAGEM_SNACKBAR('Patrimônio guardado! Seu patrimônio acumulado agora está maior :)')
                
            }).catch( () => {
                alert("Erro ao tentar guardar o Patrimônio.")
            }).finally( () => {
                this.CLOSE_MODAL_GUARDAR()
                this.listar_dados_home()
            })
        },

        resgatarDinheiro() {

            this.$v.$touch()

            this.patrimonio.identificadorUsuario = this.$store.state.usuarioLogado.id;

            let valorSemFormatacao = this.retiraFormatacao(this.patrimonio.valor)
            this.patrimonio.valor = valorSemFormatacao

            Patrimonio.resgatar(this.patrimonio).then( () => {

                this.SET_MENSAGEM_SNACKBAR('Patrimônio resgatado! O valor agora está disponível no Caixa Mensal :)')
                
            }).catch( () => {
                alert("Erro ao tentar resgatar o Patrimônio.")
            }).finally( () => {
                this.CLOSE_MODAL_RESGATAR()
                this.listar_dados_home()
            })
        },

        formatarValor() {
            let valorSemFormatacao = this.retiraFormatacao(this.patrimonio.valor)
            this.patrimonio.valor = Number(valorSemFormatacao).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })
            this.patrimonio.valor = this.patrimonio.valor.replace(/R\$\s/,'')
        },

        retiraFormatacao(valor) {
            let valorSemFormatacao = ''

            valorSemFormatacao = valor.replace(/R\$\s/,'')
            valorSemFormatacao = valorSemFormatacao.replace(/\./g,'')
            valorSemFormatacao = valorSemFormatacao.replace(',','.')

            return valorSemFormatacao
        },

        ...mapMutations([
            'OPEN_MODAL_GUARDAR',
            'CLOSE_MODAL_GUARDAR',

            'CLOSE_MODAL_TRANSFERIR',
            'OPEN_MODAL_RESGATAR',
            'CLOSE_MODAL_RESGATAR'
        ]),

        ...mapActions([
            'SET_MENSAGEM_SNACKBAR',
            'RESET_SNACKBAR'
        ]),
        ...mapActions('store_home', ['listar_dados_home']),

    }
}
</script>

<style scoped>

    .containerModal{
        background-color: snow;
    }

    .cards {
        display: flex;
        flex-direction: row;

        justify-content: center;

        margin-left: 5px;
    }

    .titulo {
        display: flex;
        justify-content: center;
    }

    .opcaoGuardar {
        width: 400px;
        height: 300px;
        border-radius: 10px;

        display: flex;
        align-items: center;
        justify-content: center;

        font-size: 28px;

        cursor: pointer; 
    }
    .opcaoGuardar:hover {
        background-color: #97CC7A;
        color: white;

        transition: all 400ms ease-in;
    }

    .opcaoResgatar {
        width: 400px;
        height: 300px;
        border-radius: 10px;

        display: flex;
        align-items: center;
        justify-content: center;

        font-size: 28px;

        cursor: pointer; 
    }
    .opcaoResgatar:hover {
        background-color: #FA9D91;
        color: white;

        transition: all 400ms ease-in;
    }

    .valorTransfere {
        font-size: 32px;
    }

    .botaoTransfere {
        display: flex;
        align-items: center;
    }
    
</style>