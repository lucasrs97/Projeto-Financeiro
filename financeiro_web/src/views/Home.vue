<template>
    <div>
        <v-container class="myContainer">
            
            <v-row dense>
                <v-tooltip bottom>
                    <template v-slot:activator="{ on, attrs }">
                        <v-col cols="12" sm="6" lg="3" v-bind="attrs" v-on="on">
                            <v-card color="#385F73" dark class="mycard">
                                <v-card-title class="headline"> {{ totalReceitasStore }} </v-card-title>
                                <v-card-subtitle>Total de Receitas</v-card-subtitle>
                                <v-card-actions>
                                    <v-btn text>VER MAIS</v-btn>
                                </v-card-actions>
                            </v-card>
                        </v-col>
                    </template>
                    <span>Todas as receitas inseridas desde seu cadastro no sistema.</span>
                </v-tooltip>

                <v-tooltip bottom>
                    <template v-slot:activator="{ on, attrs }">
                        <v-col cols="12" sm="6" lg="3" v-bind="attrs" v-on="on">
                            <v-card color="#385F73" dark class="mycard">
                                <v-card-title class="headline"> {{ totalDespesasStore }} </v-card-title>
                                <v-card-subtitle>Total de Despesas</v-card-subtitle>
                                <v-card-actions>
                                    <v-btn text>VER MAIS</v-btn>
                                </v-card-actions>
                            </v-card>
                        </v-col>
                    </template>
                    <span>Todas as despesas inseridas desde seu cadastro no sistema.</span>
                </v-tooltip>

                <v-tooltip bottom>
                    <template v-slot:activator="{ on, attrs }">
                        <v-col cols="12" sm="6" lg="3" v-bind="attrs" v-on="on">
                            <v-card color="#385F73" dark class="mycard">
                                <v-card-title class="headline"> {{ caixaMensalStore }} </v-card-title>
                                <v-card-subtitle>Caixa Mensal</v-card-subtitle>
                                <v-card-actions>
                                    <v-btn text>VER MAIS</v-btn>
                                </v-card-actions>
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
                                <v-card-actions>
                                    <v-btn text>VER MAIS</v-btn>
                                </v-card-actions>
                            </v-card>
                        </v-col>
                    </template>
                    <span>É o total acumulado para o seu patrimônio em 8 meses de cadastro no sistema.</span>
                </v-tooltip>
            </v-row>

            <v-row style="height: 500px;">
                <v-col>
                    <ChartLine v-if="loaded" :chartData="fillDataPatrimonio" :options="optionsLine" />
                </v-col>
            </v-row>

            <v-row>
                <v-col cols="12" sm="6" class="teste-despesa">
                    <div class="grafico-rosquinha">
                        <ChartDoughnut v-if="loaded"
                            :chartData="fillDataDespesas"
                            :options="optionsDoughnutDespesas" />

                        <!--<div class="texto-grafico-rosquinha">
                            <span>
                                {{ despesasTeste }}
                            </span>
                            <h5>Total</h5>
                        </div>-->
                    </div>
                </v-col>
                <v-col cols="12" sm="6">
                    <div class="grafico-rosquinha">
                        <ChartDoughnut v-if="loaded"
                            :chartData="fillDataReceitas"
                            :options="optionsDoughnutReceitas" />

                        <!--<div class="texto-grafico-rosquinha">
                            <span>
                                {{ totalReceitasStore }}
                            </span>
                            <h5>Total</h5>
                        </div>-->
                    </div>
                </v-col>
            </v-row>

            <v-row>
                <v-col offset-md="11">
                    <v-speed-dial
                        v-model="fab"
                        direction="top"
                        transition="slide-y-reverse-transition"
                    >
                        <template v-slot:activator>
                            <v-btn
                            v-model="fab"
                            color="blue darken-2"
                            dark
                            fab
                            >
                            <v-icon v-if="fab">mdi-close</v-icon>
                            <v-icon v-else>mdi-plus</v-icon>
                            </v-btn>
                        </template>

                        <v-btn
                            fab
                            dark
                            small
                            color="indigo"
                        >
                            <v-icon>mdi-chart-line-variant</v-icon>
                        </v-btn>
                        
                        <v-btn
                            fab
                            dark
                            small
                            color="red"
                        >
                            <v-icon>mdi-minus</v-icon>
                        </v-btn>
                        
                        <v-btn
                            fab
                            dark
                            small
                            color="green"
                        >
                            <v-icon>mdi-plus</v-icon>
                        </v-btn>
                    </v-speed-dial>
                </v-col>
            </v-row>

        </v-container>
    </div>
</template>

<script>

import ChartDoughnut from '../components/ChartDoughnut'
import ChartLine from '../components/ChartLine'

import { mapActions } from 'vuex'

export default {
    name: 'Home',

    components: { ChartDoughnut, /*ChartBar,*/ ChartLine },

    data: () => {
        return {
            fab: false,

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
                
                cutoutPercentage: 60,
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

                cutoutPercentage: 60,
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
            //  OPÇÕES DO GRÁFICO DE PERCENTUAL DE RECEITAS
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

        patrimonioLabels() {
            return this.$store.state.store_home.mesesEvolucaoPatrimonial;
        },

        patrimonioDatasets() {
            return this.$store.state.store_home.valoresEvolucaoPatrimonial;
        },

        //  RETORNA OS DADOS NECESSÁRIOS PARA PASSAGEM COMO PROPS AO COMPONENTE DO GRÁFICO DE EVOLUÇÃO PATRIMONIAL
        fillDataPatrimonio() {
            return {
                labels: this.patrimonioLabels,
                datasets: [
                    {
                        label: 'Evolução Patrimonial em R$',
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
        }
    },

    methods: {

        //  MAPEAMENTO DA CHAMADA AO MÉTODO LISTAR 
        ...mapActions('store_home', ['listar_dados_home']),

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
        transform: scale(1.025);
        transition: all 400ms ease-in;
        cursor: pointer;
        opacity: 0.9;
    }

    .myContainer {
        background-color: oldlace;
    }

    .grafico-rosquinha  {
        display: flex;
        align-items: center;
        justify-content: center;
    }

    .texto-grafico-rosquinha {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        position: absolute;
        margin-top: 100px
    }

    .texto-grafico-rosquinha span, h5 {
        font-size: 22px;
    }

</style>