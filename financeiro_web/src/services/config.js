import axios from 'axios'
import store from '../store/index'

export const http = axios.create({
    baseURL: 'http://localhost:8080/',
});

    /**
     * INTERCEPTANDO TODAS AS REQUESTS PARA ACRESCENTAR O LOADING COMO TRUE
     */
    http.interceptors.request.use(function (config) {
        store.dispatch('addSpinner', true)
        return config;

    }, function (error) {
        store.dispatch('addSpinner', false)
        return Promise.reject(error);

    });

    /**
     *  INTERCEPTANDO TODAS AS RESPONSES PARA ACRESCENTAR O LOADING COMO FALSE
     */
    http.interceptors.response.use(function (response) {
        
        setTimeout(() => {
            store.dispatch('addSpinner', false)
        }, 300);
        return response;

    }, function (error) {
        store.dispatch('addSpinner', false)
        return Promise.reject(error);

    });