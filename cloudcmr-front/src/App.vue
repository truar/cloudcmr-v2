<template>
  <v-app id="inspire">
    <v-content>
        <common-header />
        <router-view />
    </v-content>
  </v-app>
</template>

<script>
import CommonHeader from '@/components/header/CommonHeader'

export default {
    name: 'App',
    components: {
        CommonHeader
    },
    created () {
        this.$vuetify.theme.dark = true
    }
}
</script>
