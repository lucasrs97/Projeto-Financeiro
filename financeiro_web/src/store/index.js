import Vue from 'vue'
import Vuex from 'vuex'

import Lancamentos from '../services/lancamentos'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    showModalReceita: false,
    showModalDespesa: false,

    cadastro: [],
    isUpdate: false,

    items: [],

    snackbar: false,
    textSnackbar: '',
  },

  mutations: {

    SET_TEXT_SNACKBAR(state, payload) {
        state.textSnackbar = payload 
    },

    RESET_TEXT_SNACKBAR(state) {
        state.textSnackbar = ''
    },

    SET_SNACKBAR(state, payload) {
        state.snackbar = payload
    },

    OPEN_MODAL_RECEITA (state) {
        state.showModalReceita = true
    },
    
    OPEN_MODAL_DESPESA (state) {
        state.showModalDespesa = true
    },
    
    CLOSE_MODAL (state) {
        if (state.showModalReceita == true) {
            state.showModalReceita = false
        } else if(state.showModalDespesa == true) {
            state.showModalDespesa = false
        }

        state.cadastro = []
        state.isUpdate = false
    },

    PREPARA_EDICAO(state, payload) {
        state.cadastro = payload
        state.isUpdate = true
    },

    ATUALIZA_TABELA(state, payload) {
        state.items = payload.data[1].concat(payload.data[2])
    }

  },

  actions: {

    SET_MENSAGEM_SNACKBAR(store, payload) {
        store.commit('SET_SNACKBAR', true)
        store.commit('SET_TEXT_SNACKBAR', payload)
    },

    RESET_SNACKBAR(store, payload) {
        store.commit('SET_SNACKBAR', payload)
        store.commit('RESET_TEXT_SNACKBAR')
    },

    LISTAR_DADOS(store) {
        Lancamentos.listar().then(resposta => {
            console.log(resposta)
            store.commit('ATUALIZA_TABELA', resposta)
        }).catch( () => {
            alert("Não foi possível listar os dados.");
        }).finally( () => {
            //  Implementar aqui o spinner
        })

    },

    OPEN_MODAL(store, payload) {
        
        store.commit('SET_SNACKBAR', false)
        store.commit('RESET_TEXT_SNACKBAR')

        if(payload == 'Receita') {
            store.commit('OPEN_MODAL_RECEITA')
        } else if (payload == 'Despesa') {
            store.commit('OPEN_MODAL_DESPESA')
        }
    },

    OPEN_MODAL_UPDATE(store, payload) {

        store.commit('SET_SNACKBAR', false)
        store.commit('RESET_TEXT_SNACKBAR')

        store.commit('PREPARA_EDICAO', payload)
        
        if(payload.tipo == 'Receita') {
            store.commit('OPEN_MODAL_RECEITA')
        } else if (payload.tipo == 'Despesa') {
            store.commit('OPEN_MODAL_DESPESA')
        }

    },

  },

  modules: {
  },
})
