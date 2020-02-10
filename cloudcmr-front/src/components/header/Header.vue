<template>
    <header>
        <div id="nav">
            <router-link to="/">Home</router-link> |
            <router-link to="/about">About</router-link> |
            <router-link v-if="isLoggedOut" to="/login">Login</router-link>
            <div v-if="isLoggedIn">{{principal.username}}</div>
        </div>
        <hr/>
    </header>
</template>

<script>

export default {
    computed: {
        isLoggedOut: function () {
            return !this.$store.getters.isLoggedIn
        },
        isLoggedIn: function () {
            return this.$store.getters.isLoggedIn
        },
        principal: function () {
            return this.$store.state.principal
        }
    }
}
</script>

<style>
#nav {
  padding: 30px;
}

#nav a {
  font-weight: bold;
  color: #2c3e50;
}

#nav a.router-link-exact-active {
  color: #42b983;
}
</style>
