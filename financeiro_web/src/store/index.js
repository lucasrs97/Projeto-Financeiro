import Vue from 'vue'
import Vuex from 'vuex'

import store_home from './modules/home'

import Lancamentos from '../services/lancamentos'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    showModalReceita: false,
    showModalDespesa: false,

    showModalTransferir: false,
    showModalGuardar: false,
    showModalResgatar: false,

    cadastro: [],
    isUpdate: false,

    items: [],

    snackbar: false,
    textSnackbar: '',

    autenticado: localStorage.getItem('autenticado'),
    usuarioLogado: {
        id:     localStorage.getItem('id'),
        nome:   localStorage.getItem('nome'),
        email:  localStorage.getItem('email'),
        senha:  localStorage.getItem('senha'),
        data:   localStorage.getItem('data')
    },

    spinner: false

  },

  mutations: {

    SET_SPINNER(state, payload) {
        state.spinner = payload
    },

    SET_USUARIO_LOGADO(state, payload) {
        /**
         * Preciso setar a informação do usuário no LocalStorage,
         * porque essa informação era perdida toda vez que o usuário
         * atualizava a página ou tentava navegar via url, uma vez que
         * os dados da store são limpados em qualquer refresh manual.
         * Agora, é possível fazer a navegação manual sem que o usuário
         * perca a sua autenticação.
         */
        localStorage.setItem('id', payload.id);
        localStorage.setItem('nome', payload.nome);
        localStorage.setItem('email', payload.email);
        localStorage.setItem('senha', payload.senha);
        localStorage.setItem('data', payload.data)

        /**
         * Por outro lado, preciso também setar a informação diretamente no state,
         * porque o primeiro carregamento da home é feito antes da store retornar os
         * dados do LocalStorage, ou seja, a Home era carregada e os dados da API não
         * eram trazidos, pois não havia sido enviado nenhum usuário para a requisição.
         * Dessa forma, o state recebe rapidamente os dados e já é possível fazer uma requisição
         * para a página home. 
         */
        state.usuarioLogado.id = payload.id
        state.usuarioLogado.nome = payload.nome
        state.usuarioLogado.email = payload.email
        state.usuarioLogado.senha = payload.senha
        state.usuarioLogado.data = payload.data
    },

    SET_STATUS_AUTENTICACAO(state, payload) {
        localStorage.setItem('autenticado', payload);
        state.autenticado = payload
    },

    SET_TEXT_SNACKBAR(state, payload) {
        state.textSnackbar = payload 
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

    OPEN_MODAL_TRANSFERIR (state) {
        state.showModalTransferir = true
    },

    CLOSE_MODAL_TRANSFERIR (state) {
        state.showModalTransferir = false
    },

    OPEN_MODAL_GUARDAR (state) {
        state.showModalGuardar = true
        state.showModalTransferir = false
    },
    CLOSE_MODAL_GUARDAR(state) {
        state.showModalGuardar = false
    },

    OPEN_MODAL_RESGATAR (state) {
        state.showModalResgatar = true
        state.showModalTransferir = false
    },
    CLOSE_MODAL_RESGATAR(state) {
        state.showModalResgatar = false
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

    addSpinner(store, payload) {
        store.commit('SET_SPINNER', payload)
    },

    SET_MENSAGEM_SNACKBAR(store, payload) {
        store.commit('SET_SNACKBAR', true)
        store.commit('SET_TEXT_SNACKBAR', payload)
    },
    RESET_SNACKBAR(store) {
        store.commit('SET_SNACKBAR', false)
        store.commit('SET_TEXT_SNACKBAR', '')
    },

    LISTAR_DADOS(store) {
        Lancamentos.listar().then(resposta => {
            store.commit('ATUALIZA_TABELA', resposta)
        }).catch( () => {
            alert("Não foi possível listar os dados.");
        });

    },

    OPEN_MODAL(store, payload) {
        
        store.commit('SET_SNACKBAR', false)
        store.commit('SET_TEXT_SNACKBAR', '')

        if(payload == 'Receita') {
            store.commit('OPEN_MODAL_RECEITA')
        } else if (payload == 'Despesa') {
            store.commit('OPEN_MODAL_DESPESA')
        } else if(payload == 'Transferir') {
            store.commit('OPEN_MODAL_TRANSFERIR')
        }
    },

    OPEN_MODAL_UPDATE(store, payload) {

        store.commit('SET_SNACKBAR', false)
        store.commit('SET_TEXT_SNACKBAR', '')

        store.commit('PREPARA_EDICAO', payload)
        
        if(payload.tipo == 'Receita') {
            store.commit('OPEN_MODAL_RECEITA')
        } else if (payload.tipo == 'Despesa') {
            store.commit('OPEN_MODAL_DESPESA')
        }

    },

  },

  modules: {
      store_home
  },
})
