import { memberService } from '@/services/member.service.js'

const toLower = text => {
    return text.toString().toLowerCase()
}

const searchByName = (items, term) => {
    if (term) {
        return items.filter(item => toLower(item.lastName).includes(toLower(term)))
    }
    return items
}

export const members = {
    namespaced: true,
    state: {
        search: null,
        searched: [],
        members: []
    },
    mutations: {
        fetchAllSuccess(state, members) {
            state.members = members
            state.searched = members
        },
        searchSuccess(state, members) {
            state.searched = members
        },
        updateSearch(state, searchValue) {
            state.search = searchValue
        }
    },
    actions: {
        async fetchAll({ commit }) {
            const members = await memberService.fetchAll()
            commit('fetchAllSuccess', members)
        },
        async createMember({ commit, dispatch }, { lastName, firstName, gender, email, mobile, birthDate }) {
            await memberService.create(lastName, firstName, gender, email, mobile, birthDate)
            await dispatch('fetchAll')
            dispatch('searchOnTable')
        },
        searchOnTable({ commit, state }) {
            commit('searchSuccess', searchByName(state.members, state.search))
        }
    },
    getters: {}
}
