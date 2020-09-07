import Vue from 'vue'
import Vuex from 'vuex'
import { account } from './account.module'
import { members } from './members.module'

Vue.use(Vuex)

export default new Vuex.Store({
    modules: {
        account,
        members
    }
})
