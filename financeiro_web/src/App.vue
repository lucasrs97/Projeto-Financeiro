<template>
    <div id="app">

        <Spinner v-if="$store.state.spinner" />

        <template v-if="mostrarMenusAplicacao">
            <Layout />
        </template>

        <template v-else >
            <router-view />
        </template>

        
    </div>
</template>

<script>

import Layout from '../src/components/Layout'

import Spinner from '../src/components/Spinner'

export default {
    name: 'App',

    components: { Layout, Spinner },

    computed: {
        mostrarMenusAplicacao() {
            var rota = this.$route.name

            if(rota === 'index' || rota === 'login') {
                return false
            } else {
                return true
            }
            
        }
    }
}
</script>

<style>
    .v-dialog {
        display: flex;
        align-items: center;
        justify-content: center;
    }

    .v-content__wrap {
        background-color: whitesmoke;
    }
</style>
