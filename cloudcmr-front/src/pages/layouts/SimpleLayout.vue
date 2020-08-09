<template>
    <div class="page-container">
        <md-app>
            <md-app-toolbar class="md-primary" md-elevation="0">
                <md-button class="md-icon-button" @click="showMenu">
                    <md-icon>menu</md-icon>
                </md-button>
                <div class="md-toolbar-section-start">
                    <router-link to='/'
                                 class="md-list-item-router md-button-clean">
                        <span class="md-title">Cloud CMR</span>
                    </router-link>
                    <span class="md-title">/</span>
                    <slot name="subtitle"><span class="md-title">Accueil</span></slot>
                </div>
                <div class="md-toolbar-section-end">
                    <span>{{username}}</span>

                    <md-menu md-size="medium" md-align-trigger>
                        <md-button md-menu-trigger class="md-icon-button">
                            <md-icon>more_vert</md-icon>
                        </md-button>
                        <md-menu-content>
                            <router-link to='/login'
                                         class="md-list-item-router md-list-item-container md-button-clean">
                                <md-menu-item>Se déconnecter</md-menu-item>
                            </router-link>
                        </md-menu-content>
                    </md-menu>
                </div>
            </md-app-toolbar>
            <sidebar slot="md-app-toolbar" :menuVisible.sync="menuVisible" />
            <md-app-content>
                <slot></slot>
            </md-app-content>
        </md-app>
    </div>
</template>

<script>
    import { mapGetters } from 'vuex'
    import Sidebar from '../../components/header/Sidebar'

    export default {
        name: 'SimpleLayout',
        components: { Sidebar },
        computed: {
            ...mapGetters('account', ['getPrincipalName']),
            username() {
                return this.getPrincipalName
            }
        },
        data: () => ({
            menuVisible: false
        }),
        methods: {
            showMenu() {
                this.menuVisible = true
            }
        }
    }
</script>

<style lang="scss">
    .md-content {
        height: 93vh
    }
</style>
