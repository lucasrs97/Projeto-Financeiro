import Vue from 'vue'
import VueRouter from 'vue-router'

import Store from '../store/index'

import Index from '../views/Index'

Vue.use(VueRouter)

  const routes = [
    {
        path: '/',
        name: 'index',
        component: Index
    },

    {
        path: '/login',
        name: 'login',
        component: () => import('../views/Login.vue')
    },

    {
        path: '/home',
        name: 'home',
        component: () => import( /*webpackChunkName: "about" */ '../views/Home.vue')
    },

    {
        path: '/releases',
        name: 'releases',
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import( /*webpackChunkName: "about" */ '../views/Releases.vue')
    },
  
  ]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to, from, next) => {    

    if ( to.name === 'index' ) {
        next()
    } else if ( to.name !== 'login' && Store.state.autenticado == null ) {
        next({ name: 'login' })
    } else {
        next()
    }
})

export default router
