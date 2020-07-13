<template>
    <div>
        <v-container class="myContainer">
            
            <v-row dense> 
                <v-col cols="12" sm="6" lg="3">
                    <v-card color="#385F73" dark class="mycard">
                        <v-card-title class="headline"> {{ totalReceitas }} </v-card-title>
                  
                        <v-card-subtitle>Total de Receitas</v-card-subtitle>
                        <v-card-actions>
                            <v-btn text>VER MAIS</v-btn>
                        </v-card-actions>
                    </v-card>
                </v-col>
                <v-col cols="12" sm="6" lg="3">
                    <v-card color="#385F73" dark class="mycard">
                        <v-card-title class="headline"> {{ totalDespesas }} </v-card-title>

                        <v-card-subtitle>Total de Despesas</v-card-subtitle>
                        <v-card-actions>
                            <v-btn text>VER MAIS</v-btn>
                        </v-card-actions>
                    </v-card>
                </v-col>
                <v-col cols="12" sm="6" lg="3">
                    <v-card color="#385F73" dark class="mycard">
                        <v-card-title class="headline"> {{ caixa }} </v-card-title>

                        <v-card-subtitle>Caixa Mensal</v-card-subtitle>
                        <v-card-actions>
                            <v-btn text>VER MAIS</v-btn>
                        </v-card-actions>
                    </v-card>
                </v-col>
                <v-col cols="12" sm="6" lg="3">
                    <v-card color="#385F73" dark class="mycard">
                        <v-card-title class="headline"> {{ totalPatrimonio }} </v-card-title>

                        <v-card-subtitle>Patrimônio Acumulado</v-card-subtitle>
                        <v-card-actions>
                            <v-btn text>VER MAIS</v-btn>
                        </v-card-actions>
                    </v-card>
                </v-col>
            </v-row>
<!--
            <v-row>
                <v-col>
                    <v-menu
                        ref="menu"
                        v-model="menu"
                        :close-on-content-click="false"
                        :return-value.sync="date"
                        transition="scale-transition"
                        offset-y
                        max-width="290px"
                        min-width="290px"
                    >
                        <template v-slot:activator="{ on, attrs }">
                        <v-text-field
                            v-model="date"
                            
                            prepend-icon="mdi-calendar"
                            readonly rounded
                            v-bind="attrs"
                            v-on="on"
                        ></v-text-field>
                        </template>
                        <v-date-picker
                        v-model="date"
                        type="month"
                        no-title
                        scrollable
                        >
                        <v-spacer></v-spacer>
                        <v-btn text color="primary" @click="menu = false">Cancel</v-btn>
                        <v-btn text color="primary" @click="$refs.menu.save(date)">OK</v-btn>
                        </v-date-picker>
                    </v-menu>
                </v-col>
            </v-row>
-->
            <v-row style="height: 500px;">
                <v-col>
                    <ChartBar v-if="loaded" :chartdata="chartBar" :options="optionsBar" />
                </v-col>
            </v-row>

            <v-row>
                <v-col cols="12" sm="6">
                    <ChartDoughnut v-if="loaded"
                        :chartdata="ChartDoughnut"
                        :options="optionsDoughnut" />
                </v-col>
                <v-col cols="12" sm="6">
                    <ChartDoughnut v-if="loaded"
                        :chartdata="ChartDoughnut"
                        :options="optionsDoughnut" />
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

import Home from '../services/home'

import ChartDoughnut from '../components/Chart-Doughnut'
import ChartBar from '../components/Char-Bar'

export default {
    name: 'Home',

    components: { ChartDoughnut, ChartBar },

    data: () => {
        return {
            totalReceitas: '',
            totalDespesas: '',
            caixa: '',
            totalPatrimonio: '',

            fab: false,

            /* DATA */
            date: new Date().toISOString().substr(0, 7),
            menu: false,
            modal: false,
            /* DATA */

            loaded: false,
            ChartDoughnut: {
                labels: ['Salário','Rendimentos','Presente','Outros'],
                datasets: [
                    {
                        label: '',
                        backgroundColor: ['#F8877A', '#799ABB', 'gray', '#7EC871'],
                        data: [10, 20, 30, 40],
                    }
                ]
            },
            optionsDoughnut: {
                responsive: true,
                maintainAspectRatio: false,
                onHover: (event, chartElement) => {
                    event.target.style.cursor = chartElement[0] ? 'pointer' : 'default';
                }
            },

            chartBar: {
                labels: ['Janeiro','Fevereiro','Março','Abril', 'Maio', 'Junho'],
                datasets: [
                    {
                        label: 'Evolução Patrimonial',
                        backgroundColor: '#799ABB',
                        data: [500, 1000, 2500, 4800, 5200, 6600],
                    }
                ],
            },
            optionsBar: {
                scales: {
                    yAxes: [
                        {
                            ticks: {
                                beginAtZero: true
                            }
                        }
                    ]
                },
                responsive: true,
                maintainAspectRatio: false,
                //events: ['mousemove'], // isso é necessário, caso contrário, o onHover não é acionado. ESTÁ SENDO ACIONADO SIM
                onHover: (event, chartElement) => {
                    event.target.style.cursor = chartElement[0] ? 'pointer' : 'default';
                }
            }

        }
    },

    

    methods: {
        listar() {
            Home.listar().then(resposta => {
                console.log(resposta);

                this.totalReceitas      = resposta.data[0];
                this.totalDespesas      = resposta.data[1];
                this.caixa              = resposta.data[2];
                this.totalPatrimonio    = resposta.data[3];

                this.formatarValor()

                this.loaded = true

            }).catch( () => {
                alert("Houve um problema ao tentar recuperar os registros.")
            })
        },

        formatarValor() {
            this.totalReceitas = Number(this.totalReceitas).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })
            this.totalDespesas = Number(this.totalDespesas).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })
            this.caixa = Number(this.caixa).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })
            this.totalPatrimonio = Number(this.totalPatrimonio).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })
        },
    },

    async mounted() {
        this.listar()
    }

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

</style>