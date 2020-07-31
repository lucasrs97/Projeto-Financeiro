import { http } from './config'

export default {
    salvar: (usuario) => {
        return http.post('usuario', usuario)
    },

    deletar:(usuario) => {
		return http.delete('delete-usuario', { data: usuario })
	}
}