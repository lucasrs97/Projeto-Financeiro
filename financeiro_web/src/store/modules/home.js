import Home from '../../services/home'

export default {

    namespaced: true,

    state: {
        totalReceitas: '',
        totalDespesas: '',
        caixa: '',
        totalPatrimonio: '',

        mesesGraficos: [],

        valoresEvolucaoPatrimonial: [],
        valoresPercentuaisReceitas: [],
        valoresPercentuaisDespesas: [],
        valoresBalancoReceita: [],
        valoresBalancoDespesa: [],
        valoresBalancoPatrimonio: [],

        loaded: false
    },
    
    mutations: {
        LISTAR_DADOS_HOME(state, payload) {
            //  Cards principais
            state.totalReceitas      = Number(payload[0][0]).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
            state.totalDespesas      = Number(payload[0][1]).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
            state.caixa              = Number(payload[0][2]).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
            state.totalPatrimonio    = Number(payload[0][3]).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });

            //  Meses
            state.mesesGraficos =   payload[1];

            //  Gráfico de evolulção patrimonial
            state.valoresEvolucaoPatrimonial =  payload[2];

            //  Gráfico de percentuais (RECEITAS)
            state.valoresPercentuaisReceitas = payload[3];

            //  Gráfico de percentuais (DESPESAS)
            state.valoresPercentuaisDespesas = payload[4];

            //  Gráfico de Balanço Mensal (RECEITA)
            state.valoresBalancoReceita = payload[5];

            //  Gráfico de Balanço Mensal (DESPESA)
            state.valoresBalancoDespesa = payload[6];

            //  Gráfico de Balanço Mensal (PATRIMÔNIO)
            state.valoresBalancoPatrimonio = payload[7];

            state.loaded = true;
        }
    },

    actions: {
        listar_dados_home(store) {
            Home.listar().then(resposta => {
                console.log(resposta.data)
                store.commit('LISTAR_DADOS_HOME', resposta.data)
            }).catch( () => {
                alert("Houve um problema ao tentar recuperar os registros.")
            })
        },
    },
}