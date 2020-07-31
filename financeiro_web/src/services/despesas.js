import { http } from './config'

import Store from '../store/index'

export default {
    salvar: (despesa) => {
        return http.post('/despesa', despesa,
        
            {
                auth: {
                    username: Store.state.usuarioLogado.email,
                    password: Store.state.usuarioLogado.senha
                },
            }

        )
    },

    deletar:(despesa) => {
        return http.delete('delete-despesa', 
        
            { 
                data: despesa,
                auth: {
                    username: Store.state.usuarioLogado.email,
                    password: Store.state.usuarioLogado.senha
                },
            }
            
        )
	}
}