<template>
    <v-card flat>
        <v-form id="changeAddressForm" @submit.prevent="handleChangeAddress">
            <v-card-text>
                <v-row>
                    <v-col cols="12" md="6">
                        <v-text-field
                            dense
                            label="Numéro et libellé de la voie*"
                            v-model="address.line1"
                            type="text"
                            :error-messages="line1Errors"
                            @input="$v.address.line1.$touch()"
                            @blur="$v.address.line1.$touch()"
                            required
                        ></v-text-field>
                    </v-col>
                </v-row>
                <v-row>
                    <v-col cols="12" md="6">
                        <v-text-field
                            dense
                            label="Complément d'adresse"
                            v-model="address.line2"
                            type="text"
                        ></v-text-field>
                    </v-col>
                </v-row>
                <v-row>
                    <v-col cols="12" md="6">
                        <v-text-field
                            dense
                            label="Lieu-dit / Boite postal"
                            v-model="address.line3"
                            type="text"
                        ></v-text-field>
                    </v-col>
                </v-row>
                <v-row>
                    <v-col cols="12" md="6">
                        <v-text-field
                            dense
                            label="Ville*"
                            v-model="address.city"
                            type="text"
                            :error-messages="cityErrors"
                            @input="$v.address.city.$touch()"
                            @blur="$v.address.city.$touch()"
                            required
                        ></v-text-field>
                    </v-col>
                </v-row>
                <v-row>
                    <v-col cols="12" md="6">
                        <v-text-field
                            dense
                            label="Code postal*"
                            v-model="address.zipCode"
                            type="text"
                            :error-messages="zipCodeErrors"
                            @input="$v.address.zipCode.$touch()"
                            @blur="$v.address.zipCode.$touch()"
                            required
                        ></v-text-field>
                    </v-col>
                </v-row>
            </v-card-text>
            <v-card-actions>
                <v-btn color="blue darken-1" text @click="handleReinit">Réinitialiser</v-btn>
                <v-btn type="submit" dark color="blue darken-1">Enregistrer l'adresse</v-btn>
            </v-card-actions>
        </v-form>
    </v-card>
</template>

<script>
import { memberService } from '@/services/member.service'
import { validationMixin } from 'vuelidate'
import { required } from 'vuelidate/lib/validators'

export default {
    name: 'ChangeAddressForm',
    mixins: [validationMixin],
    props: {
        memberId: String,
        initialAddress: Object
    },
    validations: {
        address: {
            line1: {
                required
            },
            city: {
                required
            },
            zipCode: {
                required
            }
        }
    },
    data: function() {
        return {
            address: JSON.parse(JSON.stringify(this.initialAddress))
        }
    },
    computed: {
        line1Errors() {
            const errors = []
            if (!this.$v.address.line1.$dirty) return errors
            !this.$v.address.line1.required && errors.push('Le numéro et libellé sont obligatoires')
            return errors
        },
        cityErrors() {
            const errors = []
            if (!this.$v.address.city.$dirty) return errors
            !this.$v.address.city.required && errors.push('La ville est obligatoire')
            return errors
        },
        zipCodeErrors() {
            const errors = []
            if (!this.$v.address.zipCode.$dirty) return errors
            !this.$v.address.zipCode.required && errors.push('Le code postal est obligatoire')
            return errors
        }
    },
    methods: {
        async handleChangeAddress() {
            await memberService.changeAddress(this.memberId, this.address)
            this.$emit('addressChanged', this.address)
        },
        handleReinit() {
            this.address = JSON.parse(JSON.stringify(this.initialAddress))
        }
    }
}
</script>
