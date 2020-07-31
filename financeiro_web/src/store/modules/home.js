import Home from '../../services/home'

export default {

    namespaced: true,

    state: {
        totalReceitas: '',
        totalDespesas: '',
        caixa: '',
        totalPatrimonio: '',

        mesesEvolucaoPatrimonial: [],
        valoresEvolucaoPatrimonial: [],

        valoresPercentuaisReceitas: [],
        valoresPercentuaisDespesas: [],

        loaded: false
    },
    
    mutations: {
        LISTAR_DADOS_HOME(state, payload) {
            //  Cards principais
            state.totalReceitas      = Number(payload[0][0]).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
            state.totalDespesas      = Number(payload[0][1]).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
            state.caixa              = Number(payload[0][2]).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
            state.totalPatrimonio    = Number(payload[0][3]).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
            //  Cards principais

            //  Gráfico de evolulção patrimonial
            state.mesesEvolucaoPatrimonial =   payload[1];
            state.valoresEvolucaoPatrimonial =  payload[2];
            //  Gráfico de evolulção patrimonial

            //  Gráfico de rosquinha (RECEITAS X DESPESAS)
            state.valoresPercentuaisReceitas = payload[3];
            state.valoresPercentuaisDespesas = payload[4];
            //  Gráfico de rosquinha (RECEITAS X DESPESAS)

            state.loaded = true;
        }
    },

    actions: {
        listar_dados_home(store) {
            Home.listar().then(resposta => {
                store.commit('LISTAR_DADOS_HOME', resposta.data)
            }).catch( () => {
                alert("Houve um problema ao tentar recuperar os registros.")
            })
        },
    },
}