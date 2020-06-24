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

                    <v-row>
                        <v-col cols="12" md="6">
                            <v-text-field
                                v-model.lazy="cadastros.valor"
                                @change="formatarValor"

                                :error-messages="valorErrors"
                                label="Valor*"

                                prepend-icon="mdi-cash-multiple"
                                
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
                                prepend-icon="mdi-card-text-outline"
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
                                prepend-icon="mdi-map-marker-check"
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
        date: '',

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

        deletarRegistro: false,

        formatPrice: '',

    }),

    created() {},

    validations: {
        cadastros: {
            valor: { required },
            descricao: { required },
            categoria: { required },
        }
    },

    watch: {
      formatPrice: function () {
            this.formatPrice = this.formataValor(this.formatPrice)
            console.log(this.formatPrice)
        },

        date () {
            this.dateFormatted = this.formatDate(this.date)
        },
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

        isUpdate() {
            return this.$store.state.isUpdate
        },

        showModal() {
            var vm = this;

            this.$v.$reset();

            vm.date = new Date().toISOString().substr(0, 10)

            if(vm.isUpdate == true) {
                vm.cadastros.id = this.$store.state.cadastro.id;
                vm.cadastros.usuario = this.$store.state.cadastro.usuario;
                vm.cadastros.valor = this.$store.state.cadastro.valorFormatado;
                
                //  SE FOR UMA ALTERAÇÃO, PEGA A DATA DO REGISTRO NA STORE
                vm.date = this.$store.state.cadastro.data;

                vm.cadastros.descricao = this.$store.state.cadastro.descricao;
                vm.cadastros.categoria = this.$store.state.cadastro.categoria;

            } else if (vm.isUpdate == false) {
                vm.cadastros.id = null
                vm.cadastros.usuario = null
                vm.cadastros.valor = ''

                //  SE FOR UMA INCLUSÃO, PEGA A DATA ATUAL DO SISTEMA
                vm.date = new Date().toISOString().substr(0, 10)

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

        retiraFormatacao(valor) {
            let valorSemFormatacao = ''

            // Retira o "R" o "$" e o " " da String formatada.
            valorSemFormatacao = valor.replace(/R\$\s/,'') /*.replace(/\./g,'').replace(',','.')*/
            
            // Retira todos os pontos da String formatada.
            /*
            Expressão Regular Javascript que procura todas as ocorrências de um ponto decimal. 
            Obs.: Foi preciso colocar uma barra invertida antes do ponto, pois o mesmo sozinho substituía todas as letras.
            */
            valorSemFormatacao = valorSemFormatacao.replace(/\./g,'')
            
            // Troca a vírgula (usada para exibição da casa decimal) por um ponto decimal (como é esperado no BigDecimal da API).
            valorSemFormatacao = valorSemFormatacao.replace(',','.')

            return valorSemFormatacao
        },

        formatarValor() {
            //console.log("valor(formatado): ")
            //console.log(valor)

            let valorSemFormatacao = this.retiraFormatacao(this.cadastros.valor)
            //console.log("valorSemFormatacao: ")
            //console.log(valorSemFormatacao)

            this.cadastros.valor = Number(valorSemFormatacao).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })

            //console.log("this.cadastros.valor: ")
            //console.log(this.cadastros.valor)


            /*console.log('ANTES de retirar o R$')
            console.log(valor)
            console.log()

            valor = valor.replace('R$ ','')
            console.log('DEPOIS de retirar o R$')
            console.log(valor)
            console.log()

            valor = valor.replace(/\./g,'')
            valor = valor.replace(',','.')*/

            //valor = Number(valor).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })

            /*console.log()
            console.log()

            console.log("Depois de retirar o R$, os pontos, e trocar a vírgula por ponto.")
            this.cadastros.valor = valor
            console.log(this.cadastros.valor)
            console.log()

            /*try {
                console.log('entrou no try')

                valor = Number(valor).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })
            } catch (error) {
                console.log('entrou no catch')

                console.log(error)
                valor = this.cadastros.valor
            }

            console.log('depois')
            console.log(valor)*/

        },

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

                // Retira o "R$ " da String formatada.
                // Retira todos os pontos da String formatada.
                // Troca a vírgula (usada para exibição da casa decimal) por um ponto decimal (como é esperado no BigDecimal da API).
                let valorSemFormatacao = this.retiraFormatacao(this.cadastros.valor)
                this.cadastros.valor = valorSemFormatacao

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

            let valorSemFormatacao = this.retiraFormatacao(this.cadastros.valor)
            this.cadastros.valor = valorSemFormatacao

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

            
        },
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