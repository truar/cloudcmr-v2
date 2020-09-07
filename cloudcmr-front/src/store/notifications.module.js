export const notifications = {
    namespaced: true,
    state: {
        index: 0,
        notifications: []
    },
    mutations: {
        addNotification(state, { message, notificationStatus }) {
            state.index = state.index + 1
            const index = state.index
            state.notifications.unshift({
                index, message, notificationStatus
            })
        },
        deleteNotification(state, { notificationId }) {
            const newNotifications = state.notifications.filter((value) => value.index !== notificationId)
            state.notifications = newNotifications
        }
    },
    actions: {
        addSuccessNotification({ commit }, { message }) {
            commit('addNotification', { message, notificationStatus: 'success' })
        },
        deleteNotification({ commit }, { notificationId }) {
            commit('deleteNotification', { notificationId })
        }
    },
    getters: {}
}
