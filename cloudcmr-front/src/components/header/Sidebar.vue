<template>
    <!-- Disable the .sync modifier that allows data to be updated, and use the update:mdActive to trigger my custom event. Avoid mutating props -->
    <md-app-drawer :md-active="menuVisible" @update:mdActive="hideMenu">
        <md-toolbar class="md-transparent" md-elevation="0">
            <span>Menu</span>

            <div class="md-toolbar-section-end">
                <md-button class="md-icon-button md-dense" @click="hideMenu">
                    <md-icon>keyboard_arrow_left</md-icon>
                </md-button>
            </div>
        </md-toolbar>

        <md-list>
            <slot name="links">
                <sidebar-item to="/" icon="people" text="Page d'accueil"/>
                <sidebar-item to="/members" icon="people" text="Gestion des adhérents"/>
            </slot>
        </md-list>
    </md-app-drawer>
</template>

<script>
    import SidebarItem from './SidebarItem'

    export default {
        name: 'sidebar',
        components: { SidebarItem },
        props: {
            menuVisible: { type: Boolean, default: false }
        },
        methods: {
            hideMenu() {
                this.$emit('update:menuVisible', false)
            }
        }
    }
</script>
