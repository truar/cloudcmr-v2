<template>
    <simple-layout>
        <template slot="subtitle">Adhérents</template>
        <v-row>
            <v-col md="4">
                <member-edit-page-edition-form v-if="isMemberLoaded"
                                               :initial-member="member"
                ></member-edit-page-edition-form>
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
import { memberService } from '@/services/member.service'
import MemberEditPageEditionForm from '@/pages/members/MemberEditPageEditionForm'

export default {
    name: 'MemberEdit',
    components: {
        SimpleLayout,
        MemberEditPageEditionForm
    },
    data: function() {
        return {
            tab: null,
            memberId: this.$route.params['memberId'],
            isMemberLoaded: false,
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
            }
        }
    },
    methods: {
        async handleChangeAddress() {
            await memberService.changeAddress(this.memberId, this.member.address)
        }
    },
    async created() {
        this.member = await memberService.fetchMember(this.memberId)
        this.isMemberLoaded = true
        const emptyAddress = {
            line1: '',
            line2: '',
            line3: '',
            city: '',
            zipCode: ''
        }
        this.member.address = this.member.address || emptyAddress
    }
}
</script>
