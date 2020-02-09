<template>
    <div id="login">
        <form @submit.prevent="login" name="form-login">
            <FormInput v-model="username" name="username" id="username" placeholder="Nom d'utilisateur" />
            <FormInput v-model="password" name="password"  id="password" placeholder="Mot de passe" />
            <FormButton id="login-form">Se connecter</FormButton>
        </form>
    </div>
</template>

<script>
import FormInput from '@/components/FormInput.vue'
import FormButton from '@/components/FormButton.vue'

export default {
    name: 'login',
    components: {
        FormInput,
        FormButton
    },
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
                .catch(err => alert(err))
        }
    }
}
</script>
