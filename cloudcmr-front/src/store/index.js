import Vue from 'vue'
import Vuex from 'vuex'
import { userService } from '../services/user.service'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        principal: userService.getPrincipal(),
        token: userService.getToken()
    },
    mutations: {
        loginSuccess(state, principal, token) {
            state.principal = principal
            state.token = token
        }
    },
    actions: {
        async login({ commit }, userCredentials) {
            let { email, password } = userCredentials
            let { user, token } = await userService.login(email, password)
            commit('loginSuccess', user, token)
        }
    },
    modules: {},
    getters: {
        isLoggedIn: state => !!state.principal,
        getPrincipalName: state => state.principal?.email
    }
})
