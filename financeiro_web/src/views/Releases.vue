<template>
    <v-card>
        <v-card-title>
            Meus Lançamentos
            <v-spacer></v-spacer>
            <v-text-field
                v-model="search"
                append-icon="mdi-magnify"
                label="Procurar"
                single-line
                hide-details
            ></v-text-field>
        </v-card-title>

        <!--TESTE DE APLICAÇÃO DO FILTRO
        <v-card-text>
            <span class="subheading">Filtrar apenas por</span>

            <v-chip-group
                v-model="selection"
                active-class="deep-purple--text text--accent-4"
                mandatory
            >
                <v-chip>Receitas</v-chip>
                <v-chip>Despesas</v-chip>
            </v-chip-group>
        </v-card-text>
        TESTE DE APLICAÇÃO DO FILTRO-->

        <v-data-table
            :headers="headers"
            :items="items"
            item-key="id"
            :search="search"
            @click:row="rowClick"
        >
        
            <template v-slot:item.tipo="{ item }">
                <v-chip :color="getColor(item.tipo)" dark>{{ item.tipo }}</v-chip>
            </template>

            <template v-slot:item.data="{ item }">
            <!--    <v-chip> {{ item.dataFormatada }}   </v-chip>  -->
                {{ item.dataFormatada }}
            </template>
           
            <template v-slot:item.valor="{ item }">
                <v-chip outlined :color="getColor(item.tipo)" dark> {{ item.valorFormatado }}</v-chip>
            </template>

        </v-data-table>
  </v-card>
</template>

<script>
import { mapActions } from 'vuex'

export default {
    data () {
      return {
        search: '',
        headers: [
            { text: 'TIPO DE LANÇAMENTO', align: 'start', value: 'tipo', },
            { text: 'DATA', value: 'data' },
            { text: 'VALOR', value: 'valor' },
            { text: 'CATEGORIA', value: 'categoria' },
            { text: 'DESCRIÇÃO', value: 'descricao' },
          
            // Retirar em produção
            { text: 'ID', value: 'id' },
        ],

        receitas: [],
        despesas: [],

        cadastro: [],
        
      }
    },

    mounted() {
        this.LISTAR_DADOS()
    },

    computed: {
        items() {
            return this.$store.state.items
        }
    },

    methods: {
        ...mapActions([
            'OPEN_MODAL_UPDATE',
            'LISTAR_DADOS',
        ]),

        getColor (tipo) {
            if (tipo == 'Receita') 
                return 'green'
            else if (tipo == 'Despesa') 
                return 'red'
            
            else return 'orange'
        },

        rowClick: function (item) {    
            //row.select(true);
            this.OPEN_MODAL_UPDATE(item)
        }, 
      
    },
}
</script>

<style>
    .v-data-table table {
        cursor: pointer !important;
    }
</style>