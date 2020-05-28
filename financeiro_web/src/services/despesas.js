import { http } from './config'

export default {
    salvar: (despesa) => {
        return http.post('/despesa', despesa)
    }
}