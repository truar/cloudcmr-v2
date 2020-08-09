import { userService } from '../services/user.service'

export const account = {
    namespaced: true,
    state: {
        principal: userService.getPrincipal(),
        token: userService.getToken()
    },
    mutations: {
        loginSuccess(state, principal, token) {
            state.principal = principal
            state.token = token
        },
        logoutSuccess(state) {
            state.principal = {}
            state.token = ''
        }
    },
    actions: {
        async login({ commit }, { email, password }) {
            let { user, token } = await userService.login(email, password)
            commit('loginSuccess', user, token)
        },
        logout({ commit }) {
            userService.logout()
            commit('logoutSuccess')
        }
    },
    getters: {
        isLoggedIn: state => !!state.principal,
        getPrincipalName: state => state.principal?.email
    }
}
