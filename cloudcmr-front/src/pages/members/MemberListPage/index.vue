<template>
    <simple-layout>
        <template slot="subtitle">Adhérents</template>
        <v-row>
            <v-col>
                <v-data-table
                    :headers="headers"
                    :items="members"
                    :server-items-length="total"
                    :options="tableOptions"
                    :footer-props="footer"
                    :loading="loading"
                    @update:options="updateOptions"
                    class="elevation-1"
                >
                    <template v-slot:top>
                        <v-toolbar flat color="white">
                            <creation-modal-form></creation-modal-form>
                        </v-toolbar>
                    </template>
                    <template v-slot:item.actions="{ item }">
                        <v-icon
                            small
                            class="mr-2"
                            @click="editItem(item)"
                        >
                            mdi-pencil
                        </v-icon>
                    </template>
                </v-data-table>
            </v-col>
        </v-row>
    </simple-layout>
</template>

<script>
import SimpleLayout from '@/pages/layouts/SimpleLayout.vue'
import { mapActions, mapState } from 'vuex'
import CreationModalForm from '@/pages/members/MemberListPage/CreationModalForm'

export default {
    name: 'MemberListPage',
    components: {
        SimpleLayout,
        CreationModalForm
    },
    data: () => ({
        headers: [
            { text: 'Nom', value: 'lastName' },
            { text: 'Prénom', value: 'firstName' },
            { text: 'E-mail', value: 'email' },
            { text: 'Actions', value: 'actions', sortable: false }
        ],
        footer: {
            itemsPerPageOptions: [5, 10, 25, 50]
        }
    }),
    computed: {
        ...mapState('members', ['members', 'total', 'loading', 'page', 'itemsPerPage']),
        tableOptions() {
            return {
                page: this.page,
                itemsPerPage: this.itemsPerPage
            }
        }
    },
    methods: {
        ...mapActions('members', ['fetchAll', 'updatePagination']),
        newMember() {
            this.showCreationForm = true
        },
        updateOptions(options) {
            const { page, itemsPerPage } = options
            const sortBy = options.sortBy[0]
            const isDesc = options.sortDesc[0]
            this.updatePagination({ page, itemsPerPage, sortBy, isDesc })
        },
        editItem(item) {
            this.$router.push(`/members/${item.id}`)
        }
    }
}
</script>
