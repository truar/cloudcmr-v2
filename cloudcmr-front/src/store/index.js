import Vue from 'vue'
import Vuex from 'vuex'
import axios from 'axios'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        principal: null
    },
    mutations: {
        loginSuccess (state, principal) {
            state.principal = principal
        }
    },
    actions: {
        async login ({ commit }, userCredentials) {
            let { username, password } = userCredentials
            let response = await axios.post(`/api/login?username=${username}&password=${password}`, {}, { withCredentials: true })
            axios.defaults.headers.common['Authorization'] = response.headers.authorization
            localStorage.setItem('principal', JSON.stringify(response.data))
            commit('loginSuccess', response.data)
        }
    },
    modules: {
    },
    getters: {
        isLoggedIn: state => !!state.principal
    }
})
