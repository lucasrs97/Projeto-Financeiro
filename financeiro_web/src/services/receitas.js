import { http } from './config'

import Store from '../store/index'

export default {
    salvar: (receita) => {
        return http.post('receita', receita, 
            {
                auth: {
                    username: Store.state.usuarioLogado.email,
                    password: Store.state.usuarioLogado.senha
                },
            }

        )
    },

    deletar:(receita) => {
        return http.delete('delete-receita', 
            { 
                data: receita,
                auth: {
                    username: Store.state.usuarioLogado.email,
                    password: Store.state.usuarioLogado.senha
                },
            
            },
        
        )
	}
}