import { http } from './config'

export default {
    salvar: (receita) => {
        return http.post('receita', receita)
    },

    deletar:(receita) => {
		return http.delete('delete-receita', { data: receita })
	}
}