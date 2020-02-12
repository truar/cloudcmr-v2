<template>
  <v-app id="inspire">
    <v-content>
        <Header />
        <SideMenu />
        <router-view />
    </v-content>
  </v-app>
</template>

<script>
import Header from '@/components/header/Header'

export default {
    name: 'App',
    components: {
        Header
    },
    created () {
        this.$vuetify.theme.dark = true
    }
}
</script>
