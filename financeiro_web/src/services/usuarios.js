import { http } from './config'

import Store from '../store/index'

export default {
    salvar: (usuario) => {
        return http.post('usuario', usuario)
    },

    deletar:(id) => {
        return http.delete('delete-usuario', 
            { 
                data: id,
                auth: {
                    username: Store.state.usuarioLogado.email,
                    password: Store.state.usuarioLogado.senha
                },
            
            },
        )
	}
}