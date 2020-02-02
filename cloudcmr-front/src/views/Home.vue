<template>
  <div class="home">
    <div v-if="isLoggedIn">{{principal.username}}</div>
    <img alt="Vue logo" src="../assets/logo.png">
    <HelloWorld msg="Welcome to Your Vue.js App"/>
  </div>
</template>

<script>
// @ is an alias to /src
import HelloWorld from '@/components/HelloWorld.vue'

export default {
    name: 'home',
    components: {
        HelloWorld
    },
    computed: {
        isLoggedIn: function () {
            return this.$store.getters.isLoggedIn
        },
        principal: function () {
            return this.$store.state.principal
        }
    }
}
</script>
