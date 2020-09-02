<template>
    <simple-layout>
        <template slot="subtitle"><span class="md-title">Adhérents</span></template>
        <div>
            <md-table :value="searched" md-sort="name" md-sort-order="asc" md-card>
                <md-table-toolbar>
                    <div class="md-toolbar-section-start">
                        <md-button class="md-primary md-raised" @click="newMember">Créer un nouvel adhérent</md-button>
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
                <md-table-pagination
                    md-label="Pages"
                    :md-page-size="page.pageSize"
                    :md-page="page.currentPage"
                    :md-total="page.total"
                    md-separator="de"
                    :md-page-options="page.pageOptions"
                    @update:mdPageSize="onMdPageSize"
                    @pagination="onPagination">
                </md-table-pagination>
            </md-table>
            <md-dialog :md-active.sync="showCreationForm">
                <md-dialog-title>Création d'un nouvel adhérent</md-dialog-title>
                <form novalidate id="new-member-form" class="md-layout" @submit.prevent="handleCreateMember">
                    <md-dialog-content>
                        <md-field :class="getValidationClass('lastName')">
                            <label for='lastName'>Nom</label>
                            <md-input id='lastName' v-model="form.lastName" autofocus></md-input>
                            <span class="md-error"
                                  v-if="!$v.form.lastName.required">Le nom est obligatoire</span>
                        </md-field>
                        <md-field :class="getValidationClass('firstName')">
                            <label for='firstName'>Prénom</label>
                            <md-input id='firstName' v-model="form.firstName"></md-input>
                            <span class="md-error"
                                  v-if="!$v.form.firstName.required">Le prénom est obligatoire</span>
                        </md-field>
                        <div>
                            <md-radio v-model="form.gender" value="MALE">Homme</md-radio>
                            <md-radio v-model="form.gender" value="FEMALE">Femme</md-radio>
                        </div>
                        <md-field :class="getValidationClass('email')">
                            <label for='email'>E-mail</label>
                            <md-input id='email' v-model="form.email"></md-input>
                            <span class="md-error"
                                  v-if="!$v.form.email.required">L'e-mail est obligatoire</span>
                            <span class="md-error"
                                  v-if="!$v.form.email.email">Format de l'email incorrect</span>
                        </md-field>
                        <md-field :class="getValidationClass('mobile')">
                            <label for='mobile'>Mobile</label>
                            <md-input id='mobile' v-model="form.mobile"></md-input>
                            <span class="md-error"
                                  v-if="!$v.form.mobile.required">Le mobile est obligatoire</span>
                        </md-field>
                        <md-field :class="getValidationClass('birthDate')">
                            <label for='birthDate'>Date de naissance</label>
                            <md-input id='birthDate' v-model="form.birthDate"></md-input>
                        </md-field>
                        <md-dialog-actions>
                            <md-button class="md-primary" @click="showCreationForm = false">Annuler</md-button>
                            <md-button type="submit" class="md-raised md-primary">Créer</md-button>
                        </md-dialog-actions>
                    </md-dialog-content>
                </form>
            </md-dialog>
        </div>
    </simple-layout>
</template>

<script>
import SimpleLayout from '@/pages/layouts/SimpleLayout.vue'
import MdTablePagination from '@/components/tables/MdTablePagination.vue'
import { validationMixin } from 'vuelidate'
import { email, required } from 'vuelidate/lib/validators'
import { mapActions, mapState } from 'vuex'

export default {
    name: 'Members',
    mixins: [validationMixin],
    components: {
        SimpleLayout,
        MdTablePagination
    },
    data: () => ({
        showCreationForm: false,
        page: {
            pageSize: 5,
            currentPage: 1,
            pageOptions: [5, 10, 25, 50],
            total: 35
        },
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
            }
        }
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
        ...mapActions('members', ['fetchAll', 'searchOnTable', 'createMember']),
        newMember() {
            this.showCreationForm = true
        },
        getValidationClass(fieldName) {
            const field = this.$v.form[fieldName]
            if (field) {
                return {
                    'md-invalid': field.$invalid && field.$dirty
                }
            }
        },
        async handleCreateMember() {
            this.$v.$touch()

            if (!this.$v.$invalid) {
                const { lastName, firstName, email, gender, mobile, birthDate } = this.form
                await this.createMember({ lastName, firstName, gender, email, mobile, birthDate })
                this.showCreationForm = false
            }
        },
        onPagination(page) {
            this.page.currentPage = page
        },
        onMdPageSize(pageSize) {
            this.page.pageSize = pageSize
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
