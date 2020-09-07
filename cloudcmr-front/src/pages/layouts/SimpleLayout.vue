<template>
    <v-app>
        <v-navigation-drawer
            v-model="drawer"
            absolute
            temporary>
            <v-list>
                <v-list-item link to="/">
                    <v-list-item-action>
                        <v-icon>mdi-home</v-icon>
                    </v-list-item-action>
                    <v-list-item-content>
                        <v-list-item-title>Page d'accueil</v-list-item-title>
                    </v-list-item-content>
                </v-list-item>
                <v-list-item link to="/members">
                    <v-list-item-action>
                        <v-icon>mdi-account-arrow-right</v-icon>
                    </v-list-item-action>
                    <v-list-item-content>
                        <v-list-item-title>Gestion des adhérents</v-list-item-title>
                    </v-list-item-content>
                </v-list-item>
            </v-list>
        </v-navigation-drawer>

        <v-app-bar app color="primary" dark>
            <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>
            <v-toolbar-title>Cloud CMR /
                <slot name="subtitle">Accueil</slot>
            </v-toolbar-title>
            <v-spacer></v-spacer>
            <span>{{ username }}</span>
            <v-menu bottom left>
                <template v-slot:activator="{ on, attrs }">
                    <v-btn
                        dark
                        icon
                        v-bind="attrs"
                        v-on="on"
                    >
                        <v-icon>mdi-dots-vertical</v-icon>
                    </v-btn>
                </template>

                <v-list>
                    <v-list-item link to="/login">
                        <v-list-item-title>Se déconnecter</v-list-item-title>
                    </v-list-item>
                </v-list>
            </v-menu>
        </v-app-bar>

        <v-main>
            <v-container fluid>
                <slot>
                    <v-row align="center" justify="center">
                        <v-col class="text-center">
                            <v-tooltip left>
                                <template v-slot:activator="{ on }">
                                    <v-btn
                                        icon
                                        large
                                        target="_blank"
                                        v-on="on"
                                    >
                                        <v-icon large>mdi-code-tags</v-icon>
                                    </v-btn>
                                </template>
                                <span>Source</span>
                            </v-tooltip>
                        </v-col>
                    </v-row>
                </slot>
            </v-container>
        </v-main>
        <v-footer app>
            <span class="grey--text">&copy; {{ new Date().getFullYear() }}</span>
        </v-footer>
    </v-app>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
    name: 'SimpleLayout',
    computed: {
        ...mapGetters('account', ['getPrincipalName']),
        username() {
            return this.getPrincipalName
        }
    },
    data: () => ({
        drawer: false
    })
}
</script>
