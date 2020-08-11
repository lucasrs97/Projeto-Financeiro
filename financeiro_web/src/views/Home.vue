<template>
    <v-container>
        
        <v-row>
            <v-tooltip bottom>
                <template v-slot:activator="{ on, attrs }">
                    <v-col cols="12" sm="6" lg="3" v-bind="attrs" v-on="on">
                        <v-card color="#385F73" dark class="mycard">
                            <v-card-title class="headline"> {{ totalReceitasStore }} </v-card-title>
                            <v-card-subtitle>Total de Receitas</v-card-subtitle>

                            <v-card-text class="icon-card">
                                <v-icon large>mdi-arrow-up-bold-box-outline</v-icon>
                            </v-card-text>
                        </v-card>
                    </v-col>
                </template>
                <span>Todas as receitas inseridas.</span>
            </v-tooltip>

            <v-tooltip bottom>
                <template v-slot:activator="{ on, attrs }">
                    <v-col cols="12" sm="6" lg="3" v-bind="attrs" v-on="on">
                        <v-card color="#385F73" dark class="mycard">
                            <v-card-title class="headline"> {{ totalDespesasStore }} </v-card-title>
                            <v-card-subtitle>Total de Despesas</v-card-subtitle>
                            
                            <v-card-text class="icon-card">
                                <v-icon large>mdi-arrow-down-bold-box-outline</v-icon>
                            </v-card-text>
                        </v-card>
                    </v-col>
                </template>
                <span>Todas as despesas inseridas.</span>
            </v-tooltip>

            <v-tooltip bottom>
                <template v-slot:activator="{ on, attrs }">
                    <v-col cols="12" sm="6" lg="3" v-bind="attrs" v-on="on">
                        <v-card color="#385F73" dark class="mycard">
                            <v-card-title class="headline"> {{ caixaMensalStore }} </v-card-title>
                            <v-card-subtitle>Caixa Mensal</v-card-subtitle>
                            
                            <v-card-text class="icon-card">
                                <v-icon large>mdi-currency-usd</v-icon>
                            </v-card-text>
                        </v-card>
                    </v-col>
                </template>
                <span>É o seu dinheiro recorrente no mês atual.</span>
            </v-tooltip>

            <v-tooltip bottom>
                <template v-slot:activator="{ on, attrs }">
                    <v-col cols="12" sm="6" lg="3" v-bind="attrs" v-on="on">
                        <v-card color="#385F73" dark class="mycard">
                            <v-card-title class="headline"> {{ totalPatrimonioStore }} </v-card-title>
                            <v-card-subtitle>Patrimônio Acumulado</v-card-subtitle>
                            
                            <v-card-text class="icon-card">
                                <v-icon large>mdi-finance</v-icon>
                            </v-card-text>
                        </v-card>
                    </v-col>
                </template>
                <span>É o total acumulado para o seu patrimônio.</span>
            </v-tooltip>
        </v-row>

        <v-row >
            <v-col>
                <div class="grafico">
                    <ChartLine v-if="loaded" :chartData="fillDataPatrimonio" :options="optionsLine" />
                </div>
            </v-col>
        </v-row>

        <v-row>
            <v-col cols="12" md="6">
                <div class="grafico grafico-rosquinha">
                    <ChartDoughnut v-if="loaded"
                        :chartData="fillDataDespesas"
                        :options="optionsDoughnutDespesas" />
                </div>
            </v-col>
            <v-col cols="12" md="6">
                <div class="grafico grafico-rosquinha">
                    <ChartDoughnut v-if="loaded"
                        :chartData="fillDataReceitas"
                        :options="optionsDoughnutReceitas" />
                </div>
            </v-col>
        </v-row>

        <v-row >
            <v-col cols="12" md="6">
                <div class="grafico">
                    <ChartBar v-if="loaded" :chartData="fillDataReceitaDespesa" :options="optionsBarReceitaDespesa" />
                </div>
            </v-col>
            <v-col cols="12" md="6">
                <div class="grafico">
                    <ChartBar v-if="loaded" :chartData="fillDataReceitaPatrimonio" :options="optionsBarReceitaPatrimonio" />
                </div>
            </v-col>
        </v-row>

        <v-row>
            <v-col>
                <div class="botao-scroll">
                    <v-btn
                        color="blue darken-2"
                        dark
                        fab
                        @click="scrollToTop"
                    >
                        <v-icon>mdi-chevron-up</v-icon>
                    </v-btn>
                </div>
            </v-col>
        </v-row>

    </v-container>
</template>

<script>

import ChartDoughnut from '../components/ChartDoughnut'
import ChartLine from '../components/ChartLine'
import ChartBar from '../components/ChartBar'

import { mapActions } from 'vuex'

export default {
    name: 'Home',

    components: { ChartDoughnut, ChartBar, ChartLine },

    data: () => {
        return {

            /* DATA */
            date: new Date().toISOString().substr(0, 7),
            menu: false,
            modal: false,
            /* DATA */

            loaded: false,

            //  OPÇÕES DO GRÁFICO DE EVOLUÇÃO PATRIMONIAL
            optionsLine: {
                scales: {
                    yAxes: [
                        {
                            ticks: {
                                beginAtZero: true,
                                fontSize: 12,
                                callback: function(value) {
                                    // Acrescenta "R$" na label do eixo Y
                                    return 'R$ ' + value;
                                }
                            },
                        }
                    ]
                },
                responsive: true,
                maintainAspectRatio: false,
                onHover: (event, chartElement) => {
                    event.target.style.cursor = chartElement[0] ? 'pointer' : 'default';
                },

                // ALTERA A VISUALIZAÇÃO DA INFORMAÇÃO NO GRÁFICO
                tooltips: {
                    callbacks: {
                        label: function(tooltipItem, data) {
                            //  Formata o valor patrimonial no gráfico
                            return Number( data['datasets'][0]['data'][tooltipItem['index']] ).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
                        }
                    }
                }
            },
            //  OPÇÕES DO GRÁFICO DE EVOLUÇÃO PATRIMONIAL

            //  OPÇÕES DO GRÁFICO DE PERCENTUAL DE RECEITAS
            optionsDoughnutReceitas: {
                title: {
                    display: true,
                    fontSize: 22,
                    fontColor: 'black',
                    text: 'Total de Receitas',
                },

                animation: {
                    animateScale: false
                },
                
                cutoutPercentage: 0,
                responsive: true,
                maintainAspectRatio: false,

                onHover: (event, chartElement) => {
                    event.target.style.cursor = chartElement[0] ? 'pointer' : 'default';
                },

                // ALTERA A VISUALIZAÇÃO DA INFORMAÇÃO NO GRÁFICO
                tooltips: {
                    callbacks: {
                        title: function() {
                            return "Percentual recebido na categoria"
                        },
                        
                        label: function(tooltipItem, data) {
                            //  Acrescenta o símbolo "%" no valor final
                            return data['labels'][tooltipItem['index']] + ': ' + data['datasets'][0]['data'][tooltipItem['index']] + '%';
                        },
                    }
                }
            },
            //  OPÇÕES DO GRÁFICO DE PERCENTUAL DE RECEITAS

            //  OPÇÕES DO GRÁFICO DE PERCENTUAL DE DESPESAS
            optionsDoughnutDespesas: {
                title: {
                    display: true,
                    fontSize: 22,
                    fontColor: 'black',
                    text: 'Total de Despesas',
                },

                animation: {
                    animateScale: false
                },

                cutoutPercentage: 0,
                responsive: true,
                maintainAspectRatio: false,

                onHover: (event, chartElement) => {
                    event.target.style.cursor = chartElement[0] ? 'pointer' : 'default';
                },

                // ALTERA A VISUALIZAÇÃO DA INFORMAÇÃO NO GRÁFICO
                tooltips: {
                    callbacks: {

                        title: function() {
                            return "Percentual gasto na categoria"
                        },

                        label: function(tooltipItem, data) {
                            //  Acrescenta o símbolo "%" no valor final
                            return data['labels'][tooltipItem['index']] + ': ' + data['datasets'][0]['data'][tooltipItem['index']] + '%';
                        },

                        /*footer: function() {
                            return 'Valor total gasto na categoria: R$ 2.500,00';
                        }*/
                    }
                }
            },
            //  OPÇÕES DO GRÁFICO DE PERCENTUAL DE DESPESAS

            //  OPÇÕES DO GRÁFICO DE RECEITA X DESPESA
            optionsBarReceitaDespesa: {
                title: {
                    display: true,
                    fontSize: 22,
                    fontColor: 'black',
                    text: 'Balanço Receitas x Despesas',
                },

                scales: {
                    yAxes: [
                        {
                            ticks: {
                                beginAtZero: true,
                                fontSize: 12,
                                callback: function(value) {
                                    // Acrescenta "R$" na label do eixo Y
                                    return 'R$ ' + value;
                                }
                            },
                        }
                    ]
                },
                responsive: true,
                maintainAspectRatio: false,
                onHover: (event, chartElement) => {
                    event.target.style.cursor = chartElement[0] ? 'pointer' : 'default';
                },

                // ALTERA A VISUALIZAÇÃO DA INFORMAÇÃO NO GRÁFICO
                tooltips: {
                    callbacks: {
                        //  Formata o valor do Balanço Receita x Despesa
                        label: function(tooltipItem, data) {
                            /*console.log('tooltipItem')
                            console.log(tooltipItem)
                            console.log('data')
                            console.log(data)

                            console.log('teste')
                            console.log(tooltipItem['datasetIndex'])*/
                            
                            //  Verifica em qual dataset o mouse está posicionado para formatá-lo (essa eu consegui sem procurar no stackoverflow).
                            if(tooltipItem['datasetIndex'] == 0) {
                                return Number( data['datasets'][0]['data'][tooltipItem['index']] ).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
                            } else {
                                return Number( data['datasets'][1]['data'][tooltipItem['index']] ).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
                            }

                            
                        }
                    }
                }
            },
            //  OPÇÕES DO GRÁFICO DE RECEITA X DESPESA

            //  OPÇÕES DO GRÁFICO DE RECEITA X PATRIMÔNIO
            optionsBarReceitaPatrimonio: {
                title: {
                    display: true,
                    fontSize: 22,
                    fontColor: 'black',
                    text: 'Balanço Receitas x Patrimônio',
                },

                scales: {
                    yAxes: [
                        {
                            ticks: {
                                beginAtZero: true,
                                fontSize: 12,
                                callback: function(value) {
                                    // Acrescenta "R$" na label do eixo Y
                                    return 'R$ ' + value;
                                }
                            },
                        }
                    ]
                },
                responsive: true,
                maintainAspectRatio: false,
                onHover: (event, chartElement) => {
                    event.target.style.cursor = chartElement[0] ? 'pointer' : 'default';
                },

                // ALTERA A VISUALIZAÇÃO DA INFORMAÇÃO NO GRÁFICO
                tooltips: {
                    callbacks: {
                        //  Formata o valor do Balanço Receita x Despesa
                        label: function(tooltipItem, data) {
                            //  Verifica em qual dataset o mouse está posicionado para formatá-lo.
                            if(tooltipItem['datasetIndex'] == 0) {
                                return Number( data['datasets'][0]['data'][tooltipItem['index']] ).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
                            } else {
                                return Number( data['datasets'][1]['data'][tooltipItem['index']] ).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
                            }

                            
                        }
                    }
                }
            },
            //  OPÇÕES DO GRÁFICO DE RECEITA X PATRIMÔNIO
        }
    },

    computed: {

        totalReceitasStore() {
            return this.$store.state.store_home.totalReceitas
        },

        totalDespesasStore() {
            return this.$store.state.store_home.totalDespesas
        },

        caixaMensalStore() {
            return this.$store.state.store_home.caixa
        },

        totalPatrimonioStore() {
            return this.$store.state.store_home.totalPatrimonio
        },

        mesesGraficos() {
            return this.$store.state.store_home.mesesGraficos;
        },

        patrimonioDatasets() {
            return this.$store.state.store_home.valoresEvolucaoPatrimonial;
        },

        //  RETORNA OS DADOS NECESSÁRIOS PARA PASSAGEM COMO PROPS AO COMPONENTE DO GRÁFICO DE EVOLUÇÃO PATRIMONIAL
        fillDataPatrimonio() {
            return {
                labels: this.mesesGraficos,
                datasets: [
                    {
                        label: 'Evolução Patrimonial',
                        backgroundColor:      "rgba(46,95,255,0.1)",
                        borderColor:          "rgba(46,95,255,0.7)",
                        pointBackgroundColor: "rgba(46,95,255,1)",
                        data: this.patrimonioDatasets,
                    }
                ]
            }
        },

        //  RETORNA A PORCENTAGEM DE RECEITAS DA STORE
        porcentagemReceitas() {
            return this.$store.state.store_home.valoresPercentuaisReceitas;
        },

        //  RETORNA OS DADOS NECESSÁRIOS PARA PASSAGEM COMO PROPS AO COMPONENTE DO GRÁFICO DE ROSQUINHA
        fillDataReceitas() {
            return {
                labels: ['Salário','Rendimentos','Presente','Outros'],
                datasets: [
                    {
                        label: '',
                        backgroundColor: ['#F8877A', '#799ABB', 'gray', '#7EC871'],
                        data: this.porcentagemReceitas,
                    }
                ]
            }
        },

        //  RETORNA A PORCENTAGEM DE DESPESAS DA STORE
        porcentagemDespesas() {
            return this.$store.state.store_home.valoresPercentuaisDespesas;
        },

        //  RETORNA OS DADOS NECESSÁRIOS PARA PASSAGEM COMO PROPS AO COMPONENTE DO GRÁFICO DE ROSQUINHA
        fillDataDespesas() {
            return {
                labels: ['Alimentação','Transporte','Roupa','Pagamentos', 'Investimentos'],
                datasets: [
                    {
                        label: '',
                        backgroundColor: ['#F8877A', '#799ABB', 'gray', '#7EC871', 'orange'],
                        data: this.porcentagemDespesas,
                    }
                ],
            }
        },

        //  RETORNA OS DADOS NECESSÁRIOS PARA PASSAGEM COMO PROPS AO COMPONENTE DO GRÁFICO DE RECEITA X DESPESA
        fillDataReceitaDespesa() {
            return {
                labels: this.mesesGraficos,
                datasets: [
                    {
                        label: 'Receitas',
                        backgroundColor: ['#7EC871', '#7EC871', '#7EC871', '#7EC871'],
                        data: this.valoresBalancoReceitas,
                    },
                    {
                        label: 'Despesas',
                        backgroundColor: ['#F8877A', '#F8877A', '#F8877A', '#F8877A'],
                        data: this.valoresBalancoDespesas,
                    },
                ]
            }
        },

        valoresBalancoReceitas() {
            return this.$store.state.store_home.valoresBalancoReceita
        },

        valoresBalancoDespesas() {
            return this.$store.state.store_home.valoresBalancoDespesa
        },

        valoresBalancoPatrimonio() {
            return this.$store.state.store_home.valoresBalancoPatrimonio
        },

        //  RETORNA OS DADOS NECESSÁRIOS PARA PASSAGEM COMO PROPS AO COMPONENTE DO GRÁFICO DE RECEITA X PATRIMÔNIO
        fillDataReceitaPatrimonio() {
            return {
                labels: this.mesesGraficos,
                datasets: [
                    {
                        label: 'Receitas',
                        backgroundColor: ['#7EC871', '#7EC871', '#7EC871', '#7EC871'],
                        data: this.valoresBalancoReceitas,
                    },
                    {
                        label: 'Patrimônio',
                        backgroundColor: ['#6E95CF', '#6E95CF', '#6E95CF', '#6E95CF'],
                        data: this.valoresBalancoPatrimonio
                    },
                ]
            }
        },
    },

    methods: {

        //  MAPEAMENTO DA CHAMADA AO MÉTODO LISTAR 
        ...mapActions('store_home', ['listar_dados_home']),

        scrollToTop () {
            window.scrollTo({
            top: 0,
            left: 0,
            behavior: 'smooth'
            });
        }

    },

    async mounted() {

        //  CHAMADA DO MÉTODO DE LISTAGEM DOS DADOS DA HOME AO MONTAR O COMPONENTE
        this.listar_dados_home()

        /**
         * AO TENTAR ATUALIZAR A PÁGINA COM F5, OS VALORES NÃO ESTAVAM SENDO RENDERIZADOS NO GRÁFICO EM TEMPO REAL.
         * A ALTERNATIVA ENCONTRADA FOI ATRASAR UM POUCO A CHAMADA DO MÉTODO.
         */
        setInterval(() => {
            this.loaded = true
        }, 200);
    },

}
</script>

<style scoped>
    .mycard:hover {
        transform: scale(1.030);
        transition: all 200ms ease-in;
        cursor: pointer;
        opacity: 0.9;
    }

    .grafico {
        background-color: white;
        
        padding: 50px;
    }

    .grafico-rosquinha  {
        display: flex;
        align-items: center;
        justify-content: center;
    }

    .botao-scroll {
        display: flex;
        flex-direction: row-reverse;
    }

    .icon-card {
        display: flex;
        flex-direction: row-reverse;
    }

</style>