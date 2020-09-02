<template>
    <!--    <div class="page-container">-->
    <!--        <md-app>-->
    <!--            <md-app-toolbar class="md-primary" md-elevation="0">-->
    <!--                <md-button class="md-icon-button" @click="showMenu">-->
    <!--                    <md-icon>menu</md-icon>-->
    <!--                </md-button>-->
    <!--                <div class="md-toolbar-section-start">-->
    <!--                    <router-link to='/'-->
    <!--                                 class="md-list-item-router md-button-clean">-->
    <!--                        <span class="md-title">Cloud CMR</span>-->
    <!--                    </router-link>-->
    <!--                    <span class="md-title">/</span>-->
    <!--                    <slot name="subtitle"><span class="md-title">Accueil</span></slot>-->
    <!--                </div>-->
    <!--                <div class="md-toolbar-section-end">-->
    <!--                    <span>{{username}}</span>-->

    <!--                    <md-menu md-size="medium" md-align-trigger>-->
    <!--                        <md-button md-menu-trigger class="md-icon-button">-->
    <!--                            <md-icon>more_vert</md-icon>-->
    <!--                        </md-button>-->
    <!--                        <md-menu-content>-->
    <!--                            <router-link to='/login'-->
    <!--                                         class="md-list-item-router md-list-item-container md-button-clean">-->
    <!--                                <md-menu-item>Se déconnecter</md-menu-item>-->
    <!--                            </router-link>-->
    <!--                        </md-menu-content>-->
    <!--                    </md-menu>-->
    <!--                </div>-->
    <!--            </md-app-toolbar>-->
    <!--            <sidebar slot="md-app-toolbar" :menuVisible.sync="menuVisible"/>-->
    <!--            <md-app-content>-->
    <!--                <slot></slot>-->
    <!--            </md-app-content>-->
    <!--        </md-app>-->
    <!--    </div>-->

    <v-app id="inspire">
        <v-navigation-drawer
            v-model="drawer"
            app
        >
            <v-list>
                <v-list-item link to="/">
                    <v-list-item-action>
                        <v-icon>mdi-home</v-icon>
                    </v-list-item-action>
                    <v-list-item-content>
                        <v-list-item-title>Home</v-list-item-title>
                    </v-list-item-content>
                </v-list-item>
                <v-list-item link to="/members">
                    <v-list-item-action>
                        <v-icon>mdi-email</v-icon>
                    </v-list-item-action>
                    <v-list-item-content>
                        <v-list-item-title>Contact</v-list-item-title>
                    </v-list-item-content>
                </v-list-item>
            </v-list>
        </v-navigation-drawer>

        <v-app-bar
            app
            color="blue"
            dark
        >
            <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>
            <v-toolbar-title>Application</v-toolbar-title>
        </v-app-bar>

        <v-main>
            <v-container
                class="fill-height"
                fluid
            >
                <v-row
                    align="center"
                    justify="center"
                >
                    <v-col class="text-center">
                        <v-tooltip left>
                            <template v-slot:activator="{ on }">
                                <v-btn
                                    :href="source"
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
            </v-container>
        </v-main>
        <v-footer app>
            <span class="white--text">&copy; {{ new Date().getFullYear() }}</span>
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

<style lang="scss">
.md-app {
    min-height: 100vh;
}
</style>
