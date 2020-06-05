<template>

    <v-dialog v-model="showModal" persistent max-width="500px">
        <v-card>
            <template v-if="isReceita">
                <v-card-title class="receita">
                    <span class="headline">Cadastro de Receitas</span>

                    <template v-if="isUpdate">
                        <v-spacer></v-spacer>
                        
                        <v-btn class="mx-2" fab dark small color="white" @click="deletarRegistro = true">
                            <v-icon dark color="green">mdi-delete</v-icon>
                        </v-btn>
                    </template>
                </v-card-title>
            </template>

            <template v-if="isDespesa">
                <v-card-title class="despesa">
                    <span class="headline">Cadastro de Despesas</span>

                    <template v-if="isUpdate">
                        <v-spacer></v-spacer>
                        
                        <v-btn class="mx-2" fab dark small color="white" @click="deletarRegistro = true">
                            <v-icon dark color="red">mdi-delete</v-icon>
                        </v-btn>
                    </template>
                </v-card-title>
            </template>

            <v-row justify="center">
                <v-dialog v-model="deletarRegistro" persistent max-width="290">
                    <v-card dark>
                        <v-card-title class="headline">Deseja deletar?</v-card-title>
                        <v-card-text>Ao deletar esse lançamento, você não poderá recuperar esse registro.</v-card-text>
                        <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn color="blue darken-1" text @click="deletarRegistro = false">Cancelar</v-btn>
                            <v-btn color="blue darken-1" text @click="deletar(cadastros)">Deletar</v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            </v-row>

            <v-card-text>
                <v-container>
                   <!-- <v-row>
                        <v-col>
                            {{ formatPrice(cadastros.valor) }}
                        </v-col>
                    </v-row>

                    <v-row>
                        <v-col>
                            {{ this.cadastros }}
                        </v-col>
                    </v-row>-->

                    <v-row>
                        <v-col cols="12" md="6">
                            <v-text-field
                                v-model="cadastros.valor"
                                :error-messages="valorErrors"
                                label="Valor*"
                                prefix="R$"
                                required
                                @input="$v.cadastros.valor.$touch()"
                                @blur="$v.cadastros.valor.$touch()"
                            ></v-text-field>
                        </v-col>

                        <v-col cols="12" lg="6">
                           <v-menu
                                v-model="menu1"
                                :close-on-content-click="false"
                                transition="scale-transition"
                                offset-y
                                max-width="290px"
                                min-width="290px"
                            >
                                <template v-slot:activator="{ on }">
                                    <v-text-field
                                    v-model="computedDateFormatted"
                                    label="Data"
                                    hint="dd/mm/yyyy"
                                    persistent-hint
                                    prepend-icon="mdi-calendar"
                                    readonly
                                    v-on="on"
                                    ></v-text-field>
                                </template>
                                <v-date-picker v-model="date" no-title @input="menu1 = false"></v-date-picker>
                            </v-menu>
                        </v-col>

                        <v-col cols="12">
                            <v-text-field
                            v-model="cadastros.descricao"
                                :error-messages="descricaoErrors"
                                label="Descrição*"
                                required
                                @input="$v.cadastros.descricao.$touch()"
                                @blur="$v.cadastros.descricao.$touch()">
                            </v-text-field>
                        </v-col>

                        <v-col cols="12">
                            <v-select
                                v-model="cadastros.categoria"
                                :items="items"
                                label="Categoria*"
                                required
                                :error-messages="categoriaErrors"
                                @change="$v.cadastros.categoria.$touch()"
                                @blur="$v.cadastros.categoria.$touch()"
                            ></v-select>
                        </v-col>
                    </v-row>
                </v-container>
                <small>*Indicação de campo obrigatório</small>
            </v-card-text>
            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="blue darken-1" text @click="CLOSE_MODAL"> Cancelar</v-btn>
                <v-btn color="blue darken-1" text @click="salvar()"> Salvar</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
    
</template>

<script>
import { required } from 'vuelidate/lib/validators'

import { mapMutations } from 'vuex'
import { mapActions } from 'vuex'

import Receitas from '../services/receitas'
import Despesas from '../services/despesas'

export default {
    name: 'Registrations',
    
    data: () => ({
        date: new Date().toISOString().substr(0, 10),
        
        menu1: false,

        isReceita: false,
        isDespesa: false,

        cadastros: {
            valor: '',
            data: '',
            descricao: '',
            categoria: '',

            usuario: null,
            id: null
        },

        items: [],

        deletarRegistro: false
    }),

    created() {

        /*if(this.tipoCadastro == 1) {
            this.isReceita = true;
            this.items = ['Salário','Rendimentos','Presente','Outros']
        } else if (this.tipoCadastro == 2) {
            this.isDespesa = true;
            this.items = ['Alimentação','Transporte','Roupa','Pagamentos','Investimentos']
        }*/
    },

    validations: {
        cadastros: {
            valor: { required },
            descricao: { required },
            categoria: { required },
        }
    },

    computed: {
        valorErrors () {
            const errors = []
            if (!this.$v.cadastros.valor.$dirty) 
                return errors
            
            !this.$v.cadastros.valor.required && errors.push('Valor é obrigatório.')
                return errors
        },

        descricaoErrors () {
            const errors = []
            if (!this.$v.cadastros.descricao.$dirty) 
                return errors
            
            !this.$v.cadastros.descricao.required && errors.push('Descrição é obrigatório.')
                return errors
        },

        categoriaErrors () {
            const errors = []
            if (!this.$v.cadastros.categoria.$dirty) 
                return errors
            
            !this.$v.cadastros.categoria.required && errors.push('Categoria é obrigatório.')
                return errors
        },

        computedDateFormatted () {
            return this.formatDate(this.date)
        },

        /*valorFormatado(valor) {
            if(typeof valor != Number) {
                return valor
            } else {
                return valor.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })
            }
            
        },*/

        isUpdate() {
            return this.$store.state.isUpdate
        },

        showModal() {
            var vm = this;

            this.$v.$reset();

            if(vm.isUpdate == true) {
                vm.cadastros.id = this.$store.state.cadastro.id;
                vm.cadastros.usuario = this.$store.state.cadastro.usuario;
                vm.cadastros.valor = this.$store.state.cadastro.valor;
                vm.cadastros.data = this.$store.state.cadastro.data;
                vm.cadastros.descricao = this.$store.state.cadastro.descricao;
                vm.cadastros.categoria = this.$store.state.cadastro.categoria;

            } else if (vm.isUpdate == false) {
                vm.cadastros.id = null
                vm.cadastros.usuario = null
                vm.cadastros.valor = ''
                vm.cadastros.data = ''
                vm.cadastros.descricao = ''
                vm.cadastros.categoria = '' 

            }

            // Verifica se Modal de Receita está ativo
            if(this.$store.state.showModalReceita == true) {

                //  É UMA RECEITA
                vm.isReceita = true;    vm.isDespesa = false;
                vm.items = ['Salário','Rendimentos','Presente','Outros']
                return true

            } else if(this.$store.state.showModalDespesa == true) {

                //  É UMA DESPESA
                vm.isDespesa = true;    vm.isReceita = false;
                vm.items = ['Alimentação','Transporte','Roupa','Pagamentos','Investimentos']
                return true
            
            } else {
                return false
            }
        },

    },

    methods: {
        ...mapMutations([
            'CLOSE_MODAL',
        ]),

        ...mapActions([
            'LISTAR_DADOS',
            'SET_MENSAGEM_SNACKBAR'
        ]),

        formatDate (date) {
        if (!date) return null

        const [year, month, day] = date.split('-')
            return `${day}/${month}/${year}`
        },
        parseDate (date) {
        if (!date) return null

        const [day, month, year] = date.split('/')
        return `${year}-${month.padStart(2, '0')}-${day.padStart(2, '0')}`
        },

        salvar() {

            this.$v.$touch()

            if (!this.$v.$invalid) {
                
                this.cadastros.data = this.parseDate(this.computedDateFormatted);
            
                if (this.isReceita == true) {
                    Receitas.salvar(this.cadastros).then( () => {

                    if(this.isUpdate) {
                        this.SET_MENSAGEM_SNACKBAR('Receita atualizada com sucesso.')
                    } else {
                        this.SET_MENSAGEM_SNACKBAR('Receita incluída com sucesso.')
                    }
                    
                    }).catch( () => {
                        alert("Erro ao tentar salvar a Receita.")

                    }).finally( () => {
                        this.CLOSE_MODAL()
                        this.LISTAR_DADOS()
                    })

                } 
                
                else if (this.isDespesa == true) {
                    Despesas.salvar(this.cadastros).then( () => {

                    if(this.isUpdate) {
                        this.SET_MENSAGEM_SNACKBAR('Despesa atualizada com sucesso.')
                    } else {
                        this.SET_MENSAGEM_SNACKBAR('Despesa incluída com sucesso.')
                    }
                    
                    }).catch( () => {

                        alert("Erro ao tentar salvar a Despesa.");

                    }).finally( () => {
                        this.CLOSE_MODAL()
                        this.LISTAR_DADOS()
                    })
                }

            }
        },

        deletar(cadastro){

            if (this.isReceita == true) {
                Receitas.deletar(cadastro).then( () => {

                    this.SET_MENSAGEM_SNACKBAR('Receita deletada com sucesso.')

                }).catch( () => {

                    alert("Erro ao tentar deletar a Receita.");

                }).finally( () => {

                    this.deletarRegistro = false
                    this.CLOSE_MODAL()
                    this.LISTAR_DADOS()

                })

            } else if (this.isDespesa == true) {
                Despesas.deletar(cadastro).then( () => {

                    this.SET_MENSAGEM_SNACKBAR('Despesa deletada com sucesso.')

                }).catch( () => {

                    alert("Erro ao tentar deletar a Despesa.");

                }).finally( () => {

                    this.deletarRegistro = false
                    this.CLOSE_MODAL()
                    this.LISTAR_DADOS()

                })
            }

            
        }

        /*formatPrice(value) {
            return value.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })
        }*/
    },
    

}
</script>

<style scoped>
    .v-card__title {
        color: white;
    }

    .receita {
        background-color: #00cc96;
    }

    .despesa {
        background-color: lightcoral;
    }
</style>