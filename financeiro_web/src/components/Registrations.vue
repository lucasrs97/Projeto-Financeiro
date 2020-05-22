<template>

    <v-dialog v-model="dialog" persistent max-width="500px">
        <template v-slot:activator="{ on }">
            <v-btn color="primary" dark v-on="on" small>
                <template v-if="isReceita">
                    <v-icon left>mdi-plus</v-icon>
                    NOVA RECEITA
                </template>

                <template v-if="isDespesa">
                    <v-icon left>mdi-minus</v-icon>
                    NOVA DESPESA
                </template>
            </v-btn>
        </template>
        <v-card>
            <template v-if="isReceita">
                <v-card-title class="receita">
                    <span class="headline">Cadastro de Receitas</span>
                </v-card-title>
            </template>

            <template v-if="isDespesa">
                <v-card-title class="despesa">
                    <span class="headline">Cadastro de Despesas</span>
                </v-card-title>
            </template>

            <v-card-text>
                <v-container>
                    <v-row>
                        <v-col cols="12" md="6">
                            <v-text-field
                                v-model="cadastros.valor"
                                :error-messages="valorErrors"
                                label="Valor*"
                                required
                                @input="$v.cadastros.valor.$touch()"
                                @blur="$v.cadastros.valor.$touch()"
                            ></v-text-field>
                        </v-col>

                        <v-col cols="12" lg="6">
                            <v-menu
                                ref="menu1"
                                v-model="menu1"
                                :close-on-content-click="false"
                                transition="scale-transition"
                                offset-y
                                max-width="290px"
                                min-width="290px"
                            >
                                <!-- prepend-icon="event" -->
                                <template v-slot:activator="{ on }">
                                    <v-text-field
                                        v-model="dateFormatted"
                                        label="Data*"
                                        hint="MM/DD/YYYY format"
                                        persistent-hint
                                        
                                        @blur="date = parseDate(dateFormatted)"
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
                                @change="$v.select.$touch()"
                                @blur="$v.select.$touch()"
                            ></v-select>
                        </v-col>
                    </v-row>
                </v-container>
                <small>*Indicação de campo obrigatório</small>
            </v-card-text>
            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="blue darken-1" text @click="dialog = false, resetForm()">Cancelar</v-btn>
                <v-btn color="blue darken-1" text @click="salvarCadstro()">Salvar</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
    
</template>

<script>
import { required } from 'vuelidate/lib/validators'

export default {
    name: 'Registrations',

    props: {
        tipoCadastro: {
            type: Number,
            required: true
        } 
    },

    /**dialog: false, */
    
    data: vm => ({
        dialog: false,

        date: new Date().toISOString().substr(0, 10),
        dateFormatted: vm.formatDate(new Date().toISOString().substr(0, 10)),
        menu1: false,

        isReceita: false,
        isDespesa: false,

        cadastros: {
            valor: '',
            data: '',
            descricao: '',
            categoria: '',
        },

        items: [],
    }),

    created() {
        if(this.tipoCadastro == 1) {
            this.isReceita = true;
            this.items = ['Salário','Rendimentos','Presente','Outros']
        } else if (this.tipoCadastro == 2) {
            this.isDespesa = true;
            this.items = ['Alimentação','Transporte','Roupa','Pagamentos','Investimentos']
        }
    },

    validations: {
        cadastros: {
            valor: { required },
            data: { required },
            descricao: { required },
            categoria: { required },
        }
    },

    computed: {
        valorErrors () {
            const errors = []
            if (!this.$v.cadastros.valor.$dirty) return errors
            !this.$v.cadastros.valor.required && errors.push('Valor é obrigatório.')
            return errors
        },

        descricaoErrors () {
            const errors = []
            if (!this.$v.cadastros.descricao.$dirty) return errors
            !this.$v.cadastros.descricao.required && errors.push('Descrição é obrigatório.')
            return errors
        },

        categoriaErrors () {
            const errors = []
            if (!this.$v.cadastros.categoria.$dirty) return errors
            !this.$v.cadastros.categoria.required && errors.push('Categoria é obrigatório.')
            return errors
        },

        computedDateFormatted () {
            return this.formatDate(this.date)
        },
    },

    watch: {
        date () {
            this.dateFormatted = this.formatDate(this.date)
        },

        tipoCadastro () {
            if(this.tipoCadastro == 1) {
                alert("receita");
                this.isReceita = true;
            } else if(this.tipoCadastro == 2) {
                alert("despesa");
                this.isDespesa = true;
            }
        }
    },

    methods: {
        formatDate (date) {
        if (!date) return null

        const [year, month, day] = date.split('-')
            return `${month}/${day}/${year}`
        },
        parseDate (date) {
        if (!date) return null

        const [month, day, year] = date.split('/')
        return `${year}-${month.padStart(2, '0')}-${day.padStart(2, '0')}`
        },

        salvarCadstro() {

            this.$v.$touch()

            if (this.$v.$error) {
                alert("É error.")
            } else {
                alert("OK!")
            }

            if (this.$v.$invalid) {
                alert("É inválido.")
            } else {
                alert("OK!")
            }

            
            
            

            /*if (!this.$v.$invalid) {
                alert("Campos preenchidos. Salvando!");
            } else {
                this.$v.$touch();
            }*/

            /*if (this.isReceita == true) {
                Receitas.salvar(this.cadastros).then(resposta => {
                console.log(resposta);
                this.$store.commit('closeModal')
            }).catch( () => {
                alert("Erro ao tentar salvar a Receita.")
            })

            } else if (this.isDespesa == true) {
                Despesas.salvar(this.cadastros).then(resposta => {
                console.log(resposta);
                this.$store.commit('closeModal')
            }).catch( () => {
                alert("Erro ao tentar salvar a Despesa.")
            })
            }*/
        },

        resetForm() {
            this.$v.$reset();
            this.cadastros.valor = '',
            this.cadastros.data = '',
            this.cadastros.descricao = '',
            this.cadastros.categoria = ''
        }
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