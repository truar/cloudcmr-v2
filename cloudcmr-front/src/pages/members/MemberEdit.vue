<template>
    <simple-layout>
        <template slot="subtitle">Adhérents</template>
        <v-row>
            <v-col md="4">
                <v-card :loading="loading">
                    <v-form>
                        <v-card-title>
                            <span class="text-h6"><span class="font-weight-medium">{{
                                    memberFullName
                                }}</span></span>
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
            </v-col>
            <v-col md="8">
                <v-tabs
                    fixed-tabs
                    v-model="tab"
                >
                    <v-tab>
                        Coordonnées
                    </v-tab>
                    <v-tab>
                        Cours de ski
                    </v-tab>
                    <v-tab>
                        Récapitulatif d'achat
                    </v-tab>
                    <v-tab>
                        Divers
                    </v-tab>
                </v-tabs>
                <v-tabs-items v-model="tab">
                    <v-tab-item>
                        <v-card flat>
                            <v-form id="changeAddressForm" @submit.prevent="handleChangeAddress">
                                <v-card-text>
                                    <v-row>
                                        <v-col cols="12" md="6">
                                            <v-text-field
                                                dense
                                                label="Numéro et libellé de la voie*"
                                                v-model="member.address.line1"
                                                type="text"
                                                required
                                            ></v-text-field>
                                        </v-col>
                                    </v-row>
                                    <v-row>
                                        <v-col cols="12" md="6">
                                            <v-text-field
                                                dense
                                                label="Complément d'adresse"
                                                v-model="member.address.line2"
                                                type="text"
                                            ></v-text-field>
                                        </v-col>
                                    </v-row>
                                    <v-row>
                                        <v-col cols="12" md="6">
                                            <v-text-field
                                                dense
                                                label="Lieu-dit / Boite postal"
                                                v-model="member.address.line3"
                                                type="text"
                                            ></v-text-field>
                                        </v-col>
                                    </v-row>
                                    <v-row>
                                        <v-col cols="12" md="6">
                                            <v-text-field
                                                dense
                                                label="Ville"
                                                v-model="member.address.city"
                                                type="text"
                                            ></v-text-field>
                                        </v-col>
                                    </v-row>
                                    <v-row>
                                        <v-col cols="12" md="6">
                                            <v-text-field
                                                dense
                                                label="Code postal"
                                                v-model="member.address.zipCode"
                                                type="text"
                                            ></v-text-field>
                                        </v-col>
                                    </v-row>
                                </v-card-text>
                                <v-card-actions>
                                    <v-btn type="submit" dark color="blue darken-1">Enregistrer l'adresse</v-btn>
                                </v-card-actions>
                            </v-form>
                        </v-card>
                    </v-tab-item>
                </v-tabs-items>
            </v-col>
        </v-row>
    </simple-layout>
</template>

<script>
import SimpleLayout from '@/pages/layouts/SimpleLayout'
import { validationMixin } from 'vuelidate'
import { memberService } from '@/services/member.service'
import { email, required } from 'vuelidate/lib/validators'

export default {
    name: 'MemberEdit',
    components: { SimpleLayout },
    mixins: [validationMixin],
    data: function() {
        return {
            tab: null,
            memberId: this.$route.params['memberId'],
            memberBackup: {},
            member: {
                lastName: '',
                firstName: '',
                gender: '',
                birthDate: '',
                email: '',
                mobile: '',
                address: {
                    line1: '',
                    line2: '',
                    line3: '',
                    city: '',
                    zipCode: ''
                }
            },
            loading: true
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
        emailErrors() {
            const errors = []
            if (!this.$v.member.email.$dirty) return errors
            !this.$v.member.email.required && errors.push('L\'email est obligatoire')
            !this.$v.member.email.email && errors.push('Format de l\'email incorrect')
            return errors
        },
        firstNameErrors() {
            const errors = []
            if (!this.$v.member.firstName.$dirty) return errors
            !this.$v.member.firstName.required && errors.push('Le prénom est obligatoire')
            return errors
        },
        lastNameErrors() {
            const errors = []
            if (!this.$v.member.lastName.$dirty) return errors
            !this.$v.member.lastName.required && errors.push('Le nom est obligatoire')
            return errors
        },
        genderErrors() {
            const errors = []
            if (!this.$v.member.gender.$dirty) return errors
            !this.$v.member.gender.required && errors.push('Le sexe est obligatoire')
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
            this.member = JSON.parse(JSON.stringify(this.memberBackup))
        },
        async handleChangeAddress() {
            await memberService.changeAddress(this.memberId, this.member.address)
        }
    },
    async created() {
        this.member = await memberService.fetchMember(this.memberId)
        let emptyAddress = {
            line1: '',
            line2: '',
            line3: '',
            city: '',
            zipCode: ''
        }
        this.member.address = this.member.address || emptyAddress
        this.memberBackup = JSON.parse(JSON.stringify(this.member))
        this.loading = false
    }
}
</script>
