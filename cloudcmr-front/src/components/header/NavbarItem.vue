<template>
    <v-btn class="ma-2" text v-bind:to="to">
        <v-icon>{{icon}}</v-icon>{{text}}
    </v-btn>
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
