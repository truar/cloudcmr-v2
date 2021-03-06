<template>
    <v-dialog v-model="showCreationForm" persistent max-width="600px">
        <template v-slot:activator="{ on, attrs }">
            <v-btn
                color="primary"
                dark
                v-bind="attrs"
                v-on="on"
            >
                Creer un nouvel adhérent
            </v-btn>
        </template>
        <v-card>
            <v-form @submit.prevent="handleCreateMember">
                <v-card-title>
                    <span class="headline">Création d'un nouvel adhérent</span>
                </v-card-title>
                <v-card-text>
                    <v-container>
                        <v-row>
                            <v-col cols="12" sm="6" md="6">
                                <v-text-field
                                    label="Nom *"
                                    v-model="form.lastName"
                                    type="text"
                                    :error-messages="lastNameErrors"
                                    @input="$v.form.lastName.$touch()"
                                    @blur="$v.form.lastName.$touch()"
                                    required
                                ></v-text-field>
                            </v-col>
                            <v-col cols="12" sm="6" md="6">
                                <v-text-field
                                    label="Prénom *"
                                    v-model="form.firstName"
                                    type="text"
                                    :error-messages="firstNameErrors"
                                    @input="$v.form.firstName.$touch()"
                                    @blur="$v.form.firstName.$touch()"
                                    required
                                ></v-text-field>
                            </v-col>
                            <v-col cols="12" sm="6" md="6">
                                <v-text-field
                                    label="Date de naissance *"
                                    v-model="form.birthDate"
                                    type="text"
                                    :error-messages="birthDateErrors"
                                    @input="$v.form.birthDate.$touch()"
                                    @blur="$v.form.birthDate.$touch()"
                                    required
                                ></v-text-field>
                            </v-col>
                            <v-col cols="12" sm="6" md="6">
                                <v-select
                                    v-model="form.gender"
                                    :items="[{value: 'MALE', text: 'Homme'}, {value: 'FEMALE', text: 'Femme'}]"
                                    label="Sexe *"
                                    required
                                    :error-messages="genderErrors"
                                    @input="$v.form.gender.$touch()"
                                    @blur="$v.form.gender.$touch()"
                                ></v-select>
                            </v-col>
                            <v-col cols="12" md="8">
                                <v-text-field
                                    label="E-mail *"
                                    v-model="form.email"
                                    type="text"
                                    :error-messages="emailErrors"
                                    @input="$v.form.email.$touch()"
                                    @blur="$v.form.email.$touch()"
                                    required
                                ></v-text-field>
                            </v-col>
                            <v-col cols="12" md="4">
                                <v-text-field
                                    label="Mobile *"
                                    v-model="form.mobile"
                                    type="text"
                                    :error-messages="mobileErrors"
                                    @input="$v.form.mobile.$touch()"
                                    @blur="$v.form.mobile.$touch()"
                                    required
                                ></v-text-field>
                            </v-col>
                        </v-row>
                    </v-container>
                    <small>* champs obligatoires</small>
                </v-card-text>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="blue darken-1" text @click="showCreationForm = false">Fermer
                    </v-btn>
                    <v-btn type="submit" dark color="blue darken-1">Créer</v-btn>
                </v-card-actions>
            </v-form>
        </v-card>
    </v-dialog>
</template>

<script>
import { validationMixin } from 'vuelidate'
import { email, required } from 'vuelidate/lib/validators'
import { mapActions } from 'vuex'

export default {
    name: 'CreationModalForm',
    mixins: [validationMixin],
    data: () => ({
        showCreationForm: false,
        form: {
            lastName: '',
            firstName: '',
            gender: '',
            birthDate: '',
            email: '',
            mobile: ''
        }
    }),
    validations: {
        form: {
            lastName: {
                required
            },
            firstName: {
                required
            },
            gender: {
                required
            },
            email: {
                required,
                email
            },
            mobile: {
                required
            },
            birthDate: {
                required
            }
        }
    },
    computed: {
        emailErrors() {
            const errors = []
            if (!this.$v.form.email.$dirty) return errors
            !this.$v.form.email.required && errors.push('L\'email est obligatoire')
            !this.$v.form.email.email && errors.push('Format de l\'email incorrect')
            return errors
        },
        firstNameErrors() {
            const errors = []
            if (!this.$v.form.firstName.$dirty) return errors
            !this.$v.form.firstName.required && errors.push('Le prénom est obligatoire')
            return errors
        },
        lastNameErrors() {
            const errors = []
            if (!this.$v.form.lastName.$dirty) return errors
            !this.$v.form.lastName.required && errors.push('Le nom est obligatoire')
            return errors
        },
        genderErrors() {
            const errors = []
            if (!this.$v.form.gender.$dirty) return errors
            !this.$v.form.gender.required && errors.push('Le sexe est obligatoire')
            return errors
        },
        mobileErrors() {
            const errors = []
            if (!this.$v.form.mobile.$dirty) return errors
            !this.$v.form.mobile.required && errors.push('Le mobile est obligatoire')
            return errors
        },
        birthDateErrors() {
            const errors = []
            if (!this.$v.form.birthDate.$dirty) return errors
            !this.$v.form.birthDate.required && errors.push('La date de naissance est obligatoire')
            return errors
        }
    },
    methods: {
        ...mapActions('members', ['createMember']),
        ...mapActions('notifications', ['addSuccessNotification']),
        async handleCreateMember() {
            this.$v.$touch()

            if (!this.$v.$invalid) {
                const { lastName, firstName, email, gender, mobile, birthDate } = this.form
                await this.createMember({ lastName, firstName, gender, email, mobile, birthDate })
                this.addSuccessNotification({ message: 'Adhérent ajouté' })
                this.resetCreationForm()
                this.showCreationForm = false
            }
        },
        resetCreationForm() {
            this.$v.$reset()
            this.form.lastName = ''
            this.form.firstName = ''
            this.form.gender = ''
            this.form.birthDate = ''
            this.form.mobile = ''
            this.form.email = ''
        }
    }
}
</script>
