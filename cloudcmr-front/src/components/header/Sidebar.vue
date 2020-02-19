<template>
    <v-navigation-drawer v-model="drawer" app clipped>
        <v-list dense>
            <slot></slot>
        </v-list>
    </v-navigation-drawer>
</template>

<script>
export default {
    props: {
        drawer: Boolean
    }
}
</script>
