<template>
    <simple-layout>
        <template slot="subtitle"><span class="md-title">Adhérents</span></template>
        <div>
            <md-table :value="searched" md-sort="name" md-sort-order="asc" md-card>
                <md-table-toolbar>
                    <div class="md-toolbar-section-start">
                        <h1 class="md-title">Liste des adhérents</h1>
                    </div>

                    <md-field md-clearable class="md-toolbar-section-end">
                        <md-input placeholder="Recherche rapide par nom/prénom..." v-model="searchLocal"
                                  @input="searchOnTable"/>
                    </md-field>
                </md-table-toolbar>

                <md-table-empty-state
                    md-label="Aucun adhérent trouvé"
                    :md-description="`Aucun adhérent trouvé pour la recherche ${search}.
                    Essayez une autre recherche ou bien ajoutez un nouvel adhérent`">
                    <md-button class="md-primary md-raised" @click="newMember">Créer un nouvel adhérent</md-button>
                </md-table-empty-state>

                <md-table-row slot="md-table-row" slot-scope="{ item }">
                    <md-table-cell md-label="Nom" md-sort-by="lastName">{{ item.lastName }}</md-table-cell>
                    <md-table-cell md-label="Prenom" md-sort-by="firstName">{{ item.firstName }}</md-table-cell>
                    <md-table-cell md-label="E-mail" md-sort-by="email">{{ item.email }}</md-table-cell>
                </md-table-row>
            </md-table>
        </div>
    </simple-layout>
</template>

<script>
    import SimpleLayout from '@/pages/layouts/SimpleLayout.vue'
    import { mapActions, mapState } from 'vuex'

    export default {
        name: 'Members',
        components: {
            SimpleLayout
        },
        computed: {
            ...mapState('members', ['search', 'searched', 'members']),
            searchLocal: {
                get() {
                    return this.search
                },
                set(value) {
                    this.$store.commit('members/updateSearch', value)
                }
            }
        },
        methods: {
            ...mapActions('members', ['fetchAll', 'searchOnTable']),
            newMember() {
                window.alert('Noop')
            }
        },
        async created() {
            await this.fetchAll()
        }
    }
</script>

<style lang='scss' scoped>
.md-field {
    max-width: 300px;
}
</style>
