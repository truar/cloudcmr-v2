<template>
    <md-list-item>
        <router-link :to="to"
                     class="md-list-item-router md-list-item-container md-button-clean"
                     exact>
            <div class="md-list-item-content md-ripple">
                <md-icon>{{icon}}</md-icon>
                <span class="md-list-item-text">{{text}}</span>
            </div>
        </router-link>
    </md-list-item>
</template>

<script>
    export default {
        name: 'SidebarItem',
        props: {
            text: String,
            icon: String,
            to: String
        }
    }
</script>
