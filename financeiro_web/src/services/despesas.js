import { http } from './config'

export default {
    salvar: (despesa) => {
        return http.post('/despesa', despesa)
    },

    deletar:(despesa) => {
		return http.delete('delete-despesa', { data: despesa })
	}
}