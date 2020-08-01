<template>
    <div class="home">
        <header>{{username}}</header>
        Successfully loggedIn
    </div>
</template>

<script>
    // @ is an alias to /src

    export default {
        name: 'home',
        computed: {
            username() {
                return this.$store.getters.getPrincipalName
            }
        }
    }
</script>
