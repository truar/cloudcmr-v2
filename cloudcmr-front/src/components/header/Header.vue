<template>
    <header>
        <v-app-bar app color="primary" dark>
            <div class="d-flex align-center">
                <v-img
                    alt="Vuetify Logo"
                    class="shrink mr-2"
                    contain
                    src="https://cdn.vuetifyjs.com/images/logos/vuetify-logo-dark.png"
                    transition="scale-transition"
                    width="40"
                />

                <v-img
                    alt="Vuetify Name"
                    class="shrink mt-1 hidden-sm-and-down"
                    contain
                    min-width="100"
                    src="https://cdn.vuetifyjs.com/images/logos/vuetify-name-dark.png"
                    width="100"
                />
            </div>

            <v-spacer></v-spacer>
            <router-link v-if="isLoggedOut" to="/login">
                <v-btn
                    href="https://github.com/vuetifyjs/vuetify/releases/latest"
                    target="_blank"
                    text
                >
                    <span class="mr-2">Login</span>
                    <v-icon>mdi-open-in-new</v-icon>
                </v-btn>
            </router-link>
            <v-btn
                href="https://github.com/vuetifyjs/vuetify/releases/latest"
                target="_blank"
                text
            >
                <span class="mr-2">Latest Release</span>
                <v-icon>mdi-open-in-new</v-icon>
            </v-btn>
        </v-app-bar>

        <div id="nav">
            <router-link to="/">Home</router-link> |
            <router-link to="/about">About</router-link> |
            <div v-if="isLoggedIn">{{ principal.username }}</div>
        </div>
        <hr />
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
