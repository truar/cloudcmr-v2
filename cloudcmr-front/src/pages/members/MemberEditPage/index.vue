<template>
    <simple-layout>
        <template slot="subtitle">Adhérents</template>
        <v-row>
            <v-col md="4">
                <contact-information-form
                    v-if="isMemberLoaded"
                    :initial-member="member"
                    :member-id="memberId"
                    @memberChanged="onChangeMember"
                ></contact-information-form>
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
                        <change-address-form
                            v-if="isMemberLoaded"
                            :member-id="memberId"
                            :initial-address="member.address"
                            @addressChanged="onChangeAddress"
                        ></change-address-form>
                    </v-tab-item>
                </v-tabs-items>
            </v-col>
        </v-row>
    </simple-layout>
</template>

<script>
import SimpleLayout from '@/pages/layouts/SimpleLayout'
import { memberService } from '@/services/member.service'
import ContactInformationForm from '@/pages/members/MemberEditPage/ContactInformationForm'
import ChangeAddressForm from '@/pages/members/MemberEditPage/ChangeAddressForm'
import { mapActions } from 'vuex'

export default {
    name: 'MemberEditPage',
    components: {
        ChangeAddressForm,
        SimpleLayout,
        ContactInformationForm
    },
    data: function() {
        return {
            tab: null,
            memberId: this.$route.params['memberId'],
            isMemberLoaded: false,
            member: {}
        }
    },
    methods: {
        ...mapActions('notifications', ['addErrorNotification']),
        onChangeAddress(address) {
            this.member.address = JSON.parse(JSON.stringify(address))
        },
        async onChangeMember(member) {
            this.member = await memberService.fetchMember(this.memberId)
        }
    },
    async created() {
        try {
            this.member = await memberService.fetchMember(this.memberId)
            const emptyAddress = {
                line1: '',
                line2: '',
                line3: '',
                city: '',
                zipCode: ''
            }
            this.member.address = this.member.address || emptyAddress
            this.isMemberLoaded = true
        } catch (e) {
            this.addErrorNotification({ message: 'Adhérent inexistant' })
            this.$router.push('/members')
        }
    }
}
</script>
