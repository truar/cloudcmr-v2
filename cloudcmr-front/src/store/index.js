import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
    },
    mutations: {
    },
    actions: {
        login ({ commit }, user) {
            return new Promise((resolve, reject) => {
                console.log(user)
                resolve()
            })
        }
    },
    modules: {
    }
})
