<template>
    <v-card>
        <v-form @submit.prevent="handleSubmit">
            <v-card-title>
                <span class="text-h6 font-weight-medium">{{ memberFullName }}</span>
            </v-card-title>
            <v-card-text>
                <v-container>
                    <v-row>
                        <v-col cols="12">
                            <v-text-field
                                dense
                                label="Nom *"
                                v-model="member.lastName"
                                type="text"
                                :error-messages="lastNameErrors"
                                @input="$v.member.lastName.$touch()"
                                @blur="$v.member.lastName.$touch()"
                                required
                            ></v-text-field>
                        </v-col>
                        <v-col cols="12">
                            <v-text-field
                                dense
                                label="Prénom *"
                                v-model="member.firstName"
                                type="text"
                                :error-messages="firstNameErrors"
                                @input="$v.member.firstName.$touch()"
                                @blur="$v.member.firstName.$touch()"
                                required
                            ></v-text-field>
                        </v-col>
                        <v-col cols="12">
                            <v-text-field
                                dense
                                label="Date de naissance *"
                                v-model="member.birthDate"
                                type="text"
                                :error-messages="birthDateErrors"
                                @input="$v.member.birthDate.$touch()"
                                @blur="$v.member.birthDate.$touch()"
                                required
                            ></v-text-field>
                        </v-col>
                        <v-col cols="12">
                            <v-select
                                v-model="member.gender"
                                :items="[{value: 'MALE', text: 'Homme'}, {value: 'FEMALE', text: 'Femme'}]"
                                label="Sexe *"
                                required
                                :error-messages="genderErrors"
                                @input="$v.member.gender.$touch()"
                                @blur="$v.member.gender.$touch()"
                            ></v-select>
                        </v-col>
                        <v-col cols="12">
                            <v-text-field
                                dense
                                label="E-mail *"
                                v-model="member.email"
                                type="text"
                                :error-messages="emailErrors"
                                @input="$v.member.email.$touch()"
                                @blur="$v.member.email.$touch()"
                                required
                            ></v-text-field>
                        </v-col>
                        <v-col cols="12">
                            <v-text-field
                                dense
                                label="Mobile *"
                                v-model="member.mobile"
                                type="text"
                                :error-messages="mobileErrors"
                                @input="$v.member.mobile.$touch()"
                                @blur="$v.member.mobile.$touch()"
                                required
                            ></v-text-field>
                        </v-col>
                    </v-row>
                    <small>* champs obligatoires</small>
                </v-container>
            </v-card-text>
            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="blue darken-1" text @click="reinitMember">Réinitialiser</v-btn>
                <v-btn type="submit" dark color="blue darken-1">Enregistrer</v-btn>
            </v-card-actions>
        </v-form>
    </v-card>
</template>

<script>
import { validationMixin } from 'vuelidate'
import { email, required } from 'vuelidate/lib/validators'

export default {
    name: 'MemberEditPageEditionForm',
    mixins: [validationMixin],
    props: {
        initialMember: Object,
        loading: Boolean,
        handleSubmit: Function,
        handleReset: Function
    },
    data: function() {
        return {
            member: JSON.parse(JSON.stringify(this.initialMember))
        }
    },
    validations: {
        member: {
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
        memberFullName() {
            return `${this.member.firstName} ${this.member.lastName}`
        },
        lastNameErrors() {
            const errors = []
            if (!this.$v.member.lastName.$dirty) return errors
            !this.$v.member.lastName.required && errors.push('Le nom est obligatoire')
            return errors
        },
        firstNameErrors() {
            const errors = []
            if (!this.$v.member.firstName.$dirty) return errors
            !this.$v.member.firstName.required && errors.push('Le prénom est obligatoire')
            return errors
        },
        genderErrors() {
            const errors = []
            if (!this.$v.member.gender.$dirty) return errors
            !this.$v.member.gender.required && errors.push('Le sexe est obligatoire')
            return errors
        },
        emailErrors() {
            const errors = []
            if (!this.$v.member.email.$dirty) return errors
            !this.$v.member.email.required && errors.push('L\'email est obligatoire')
            !this.$v.member.email.email && errors.push('Format de l\'email incorrect')
            return errors
        },
        mobileErrors() {
            const errors = []
            if (!this.$v.member.mobile.$dirty) return errors
            !this.$v.member.mobile.required && errors.push('Le mobile est obligatoire')
            return errors
        },
        birthDateErrors() {
            const errors = []
            if (!this.$v.member.birthDate.$dirty) return errors
            !this.$v.member.birthDate.required && errors.push('La date de naissance est obligatoire')
            return errors
        }
    },
    methods: {
        reinitMember() {
            this.member = JSON.parse(JSON.stringify(this.initialMember))
        }
    }
}
</script>
