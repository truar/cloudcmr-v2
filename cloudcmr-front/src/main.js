import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import VueMaterial from 'vue-material'
import 'vue-material/dist/vue-material.min.css'
import 'vue-material/dist/theme/default.css'
import * as firebase from 'firebase'
import axios from 'axios'
import { userService } from '@/services/user.service'
import vuetify from './plugins/vuetify'

Vue.config.productionTip = false

firebase.initializeApp({
    apiKey: process.env.firebase?.apiKey || 'AIzaSyBiyGFuZ-6kCmv2Ea9oW3bFKYLpm80iZZg',
    projectId: process.env.firbase?.projectId || 'truaro-test-gcp',
    authDomain: process.env.firebase?.authDomain || 'truaro-test-gcp.web.app'
})

axios.defaults.baseURL = process.env.VUE_APP_SERVER_URL || 'http://localhost:8080'
axios.defaults.headers.common['Authorization'] = userService.getToken()
axios.interceptors.response.use(function(response) {
    return response
}, function(error) {
    if (error.response.status === 401) {
        const currentLocation = window.location.pathname
        router.push({ name: 'login', query: { from: currentLocation } })
    }
})

Vue.use(VueMaterial)

// change multiple options
Vue.material = {
    ...Vue.material,
    locale: {
        ...Vue.material.locale,
        dateFormat: 'dd/MM/yyyy',
        firstDayOfAWeek: 1
    }
}

new Vue({
    router,
    store,
    vuetify,
    render: h => h(App)
}).$mount('#app')
