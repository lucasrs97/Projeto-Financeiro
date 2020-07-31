import { http } from './config'

import Store from '../store/index'

export default {
    listar:() => {
        return http.get('releases',
        
        {
            auth: {
                username: Store.state.usuarioLogado.email,
                password: Store.state.usuarioLogado.senha
            },
        }

        )
    }
}