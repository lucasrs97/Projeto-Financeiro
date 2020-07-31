import { http } from './config'

import Store from '../store/index'

export default {
    guardar: (patrimonio) => {
        return http.post('guardar_patrimonio', patrimonio,
        
            {
                auth: {
                    username: Store.state.usuarioLogado.email,
                    password: Store.state.usuarioLogado.senha
                },
            }

        )
    },

    resgatar: (patrimonio) => {
        return http.post('resgatar_patrimonio', patrimonio,
        
            {
                auth: {
                    username: Store.state.usuarioLogado.email,
                    password: Store.state.usuarioLogado.senha
                },
            }

        )
    }
}