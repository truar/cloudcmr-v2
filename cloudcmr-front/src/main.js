import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import VueMaterial from 'vue-material'
import 'vue-material/dist/vue-material.min.css'
import 'vue-material/dist/theme/default.css'
import * as firebase from 'firebase'

Vue.config.productionTip = false

firebase.initializeApp({
    apiKey: process.env.firebase?.apiKey || 'AIzaSyBiyGFuZ-6kCmv2Ea9oW3bFKYLpm80iZZg',
    projectId: process.env.firbase?.projectId || 'truaro-test-gcp',
    authDomain: process.env.firebase?.authDomain || 'truaro-test-gcp.web.app'
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
    render: h => h(App)
}).$mount('#app')
