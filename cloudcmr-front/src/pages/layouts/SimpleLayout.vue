<template>
    <div class="page-container">
        <md-app>
            <md-app-toolbar class="md-primary" md-elevation="0">
                <md-button class="md-icon-button" @click="toggleMenu" v-if="!menuVisible">
                    <md-icon>menu</md-icon>
                </md-button>
                <div class="md-toolbar-section-start">
                    <span class="md-title">Cloud CMR</span>
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
            <md-app-drawer :md-active.sync="menuVisible">
                <md-toolbar class="md-transparent" md-elevation="0">
                    <span>Menu</span>

                    <div class="md-toolbar-section-end">
                        <md-button class="md-icon-button md-dense" @click="toggleMenu">
                            <md-icon>keyboard_arrow_left</md-icon>
                        </md-button>
                    </div>
                </md-toolbar>

                <md-list>
                    <md-list-item>
                        <router-link to='/members'
                                     class="md-list-item-router md-list-item-container md-button-clean">
                            <div class="md-list-item-content md-ripple">
                                <md-icon>people</md-icon>
                                <span class="md-list-item-text">Gestion des adhérents</span>
                            </div>
                        </router-link>
                    </md-list-item>
                </md-list>
            </md-app-drawer>
            <md-app-content>
                <slot></slot>
            </md-app-content>
        </md-app>
    </div>
</template>

<script>
    import { mapGetters } from 'vuex'

    export default {
        name: 'SimpleLayout',
        data: () => ({
            menuVisible: false
        }),
        computed: {
            ...mapGetters('account', ['getPrincipalName']),
            username() {
                return this.getPrincipalName
            }
        },
        methods: {
            toggleMenu() {
                this.menuVisible = !this.menuVisible
            }
        }
    }
</script>

<style lang="scss">
    .md-content {
        height: 93vh
    }
</style>
