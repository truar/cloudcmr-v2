<template>
    <header>
        <sidebar v-bind:drawer="drawer">
            <sidebar-item to="/" icon="mdi-view-dashboard" text="Home"/>
            <sidebar-item to="/about" icon="mdi-view-dashboard" text="About"/>
        </sidebar>

        <navbar @toggle="handleToggle">
            <navbar-item to="/login" icon="mdi-account" v-if="isLoggedOut" text="Se connecter"/>
            <navbar-item to="/login" icon="mdi-account" v-if="!isLoggedOut" v-bind:text="principal"/>
        </navbar>
    </header>
</template>

<script>
import Navbar from './Navbar.vue'
import NavbarItem from './NavbarItem.vue'
import Sidebar from './Sidebar.vue'
import SidebarItem from './SidebarItem.vue'

export default {
    name: 'commonHeader',
    components: {
        Navbar,
        NavbarItem,
        Sidebar,
        SidebarItem
    },
    computed: {
        isLoggedOut: function() {
            return !this.$store.getters.isLoggedIn
        },
        principal: function() {
            return this.$store.state.principal.email
        }
    },
    methods: {
        handleToggle() {
            this.drawer = !this.drawer
        }
    },
    data: () => ({
        drawer: true
    })
}
</script>
