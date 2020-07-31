import { http } from './config'

export default {
    logar: (login) => {
        return http.post('login', login)
    },
}