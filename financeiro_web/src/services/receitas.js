import { http } from './config'

export default {
    salvar: (receita) => {
        return http.post('/receita', receita)
    }
}