import { memberService } from '@/services/member.service.js'

export const members = {
    namespaced: true,
    state: {
        members: [],
        total: 0,
        loading: true,
        page: 1,
        itemsPerPage: 10,
        sortBy: undefined,
        isDesc: undefined
    },
    mutations: {
        fetchAllSuccess(state, memberList) {
            state.members = memberList.members
            state.total = memberList.total
            state.loading = false
        },
        paginationUpdated(state, { page, itemsPerPage, sortBy, isDesc }) {
            state.page = page
            state.itemsPerPage = itemsPerPage
            state.sortBy = sortBy
            state.isDesc = isDesc
        },
        isLoading(state) {
            state.loading = true
        }
    },
    actions: {
        async fetchAll({ commit, getters }) {
            commit('isLoading')
            const { page, itemsPerPage } = getters.getPagination
            const { sortBy, isDesc } = getters.getSort
            const memberList = await memberService.fetchAll(page, itemsPerPage, sortBy, isDesc)
            commit('fetchAllSuccess', memberList)
        },
        async createMember({ commit, dispatch }, { lastName, firstName, gender, email, mobile, birthDate }) {
            await memberService.create(lastName, firstName, gender, email, mobile, birthDate)
            dispatch('fetchAll')
        },
        updatePagination({ commit, getters, dispatch }, { page, itemsPerPage, sortBy, isDesc }) {
            if (sortBy !== getters.getSort.sortBy || isDesc !== getters.getSort.isDesc) {
                commit('paginationUpdated', { page: 1, itemsPerPage, sortBy, isDesc })
            } else {
                commit('paginationUpdated', { page, itemsPerPage, sortBy, isDesc })
            }
            dispatch('fetchAll')
        }
    },
    getters: {
        getPagination: state => {
            return { page: state.page, itemsPerPage: state.itemsPerPage }
        },
        getSort: state => {
            return { sortBy: state.sortBy, isDesc: state.isDesc }
        }
    }
}
