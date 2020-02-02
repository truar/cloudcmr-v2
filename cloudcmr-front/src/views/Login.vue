<template>
    <form @submit.prevent="login" name="login">
        <label for="username">Utilisateur :</label><input v-model="username" name="username" id="username" value="" />
        <label for="password">Mot de passe : </label><input v-model="password" name="password" id="password" value="" />
        <button type="submit" id="login-form">Se connecter</button>
    </form>
</template>

<script>
export default {
    name: 'login',
    data () {
        return {
            username: '',
            password: ''
        }
    },
    methods: {
        login: function () {
            let username = this.username
            let password = this.password
            this.$store.dispatch('login', { username, password })
                .then(() => this.$router.push('/'))
                .catch(err => err)
        }
    }
}
</script>
