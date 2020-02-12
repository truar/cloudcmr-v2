<template>
    <header>
        <v-navigation-drawer v-model="drawer" app clipped>
            <v-list dense>
                <v-list-item to="/" link>
                    <v-list-item-action>
                        <v-icon>mdi-view-dashboard</v-icon>
                    </v-list-item-action>
                    <v-list-item-content>
                        <v-list-item-title>Home</v-list-item-title>
                    </v-list-item-content>
                </v-list-item>
                <v-list-item to="/about" link>
                    <v-list-item-action>
                        <v-icon>mdi-view-dashboard</v-icon>
                    </v-list-item-action>
                    <v-list-item-content>
                        <v-list-item-title>About</v-list-item-title>
                    </v-list-item-content>
                </v-list-item>
            </v-list>
        </v-navigation-drawer>

        <v-app-bar app clipped-left>
            <v-app-bar-nav-icon @click.stop="drawer = !drawer" />
            <v-toolbar-title>Cloud CMR</v-toolbar-title>

            <v-spacer></v-spacer>

            <v-btn class="ma-2" text v-if="isLoggedOut" to="/login">
                <v-icon>mdi-account</v-icon>Se connecter
            </v-btn>

             <v-btn class="ma-2" text v-if="!isLoggedOut">
                <v-icon>mdi-account</v-icon>{{principal.username}}
            </v-btn>
        </v-app-bar>
    </header>
</template>

<script>
export default {
    computed: {
        isLoggedOut: function () {
            return !this.$store.getters.isLoggedIn
        },
        principal: function () {
            return this.$store.state.principal
        }
    },
    data: () => ({
        drawer: true
    })
}
</script>
