import { http } from './config'

export default {
    guardar: (patrimonio) => {
        return http.post('guardar_patrimonio', patrimonio)
    },

    resgatar: (patrimonio) => {
        return http.post('resgatar_patrimonio', patrimonio)
    }
}