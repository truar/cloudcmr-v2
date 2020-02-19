<template>
    <v-list-item v-bind:to="to" link>
        <v-list-item-action>
            <v-icon>{{icon}}</v-icon>
        </v-list-item-action>
        <v-list-item-content>
            <v-list-item-title>{{text}}</v-list-item-title>
        </v-list-item-content>
    </v-list-item>
</template>

<script>
export default {
    props: {
        text: String,
        icon: String,
        to: String
    }
}
</script>
