import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '@/pages/Home.vue'
import Login from '@/pages/Login.vue'
import { userService } from '@/services/user.service'
import MemberPageList from '@/pages/members/MemberListPage/index'
import MemberEditPage from '@/pages/members/MemberEditPage/index'

Vue.use(VueRouter)

const routes = [
    { path: '/', component: Home },
    { path: '/login', component: Login, name: 'login' },
    { path: '/members', component: MemberPageList },
    { path: '/members/:memberId', component: MemberEditPage }
]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

router.beforeEach((to, from, next) => {
    // redirect to login page if not logged in and trying to access a restricted page
    const publicPages = ['/login']
    const authRequired = !publicPages.includes(to.path)

    if (authRequired && !userService.isAuthenticated()) {
        return next('/login')
    }

    next()
})

export default router
