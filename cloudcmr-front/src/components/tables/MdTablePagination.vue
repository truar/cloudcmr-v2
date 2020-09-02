<template>
    <div class="md-table-pagination">
        <span class="md-table-pagination-label">{{ mdLabel }}</span>

        <md-field>
            <md-select :value="mdPageSize" md-dense md-class="md-pagination-select" @md-selected="setPageSize">
                <md-option v-for="amount in mdPageOptions" :key="amount" :value="amount">{{ amount }}</md-option>
            </md-select>
        </md-field>

        <span class="md-table-pagination-information">{{ currentItemCount }}-{{ currentPageCount }} {{ mdSeparator }} {{ mdTotal }}</span>

        <md-button class="md-icon-button md-table-pagination-previous" @click="goToPrevious()" :disabled="mdPage === 1">
            <md-icon>keyboard_arrow_left</md-icon>
        </md-button>

        <md-button class="md-icon-button md-table-pagination-next" @click="goToNext()"
                   :disabled="currentPageCount >= mdTotal">
            <md-icon>keyboard_arrow_right</md-icon>
        </md-button>
    </div>
</template>

<script>
export default {
    name: 'MdTablePagination',
    props: {
        mdPageSize: {
            type: [String, Number],
            default: 10
        },
        mdPageOptions: {
            type: Array,
            default: () => [10, 25, 50, 100]
        },
        mdPage: {
            type: Number,
            default: 1
        },
        mdTotal: {
            type: [String, Number],
            default: 'Many'
        },
        mdLabel: {
            type: String,
            default: 'Rows per page:'
        },
        mdSeparator: {
            type: String,
            default: 'of'
        }
    },
    computed: {
        currentItemCount() {
            return ((this.mdPage - 1) * this.mdPageSize) + 1
        },
        currentPageCount() {
            return Math.min(this.mdPage * this.mdPageSize, this.mdTotal)
        }
    },
    methods: {
        setPageSize(pageSize) {
            const previousPageSize = this.mdPageSize
            const newCurrentPage = Math.floor((this.mdPage - 1) * previousPageSize / pageSize) + 1
            this.$emit('update:mdPageSize', pageSize)
            this.$emit('pagination', newCurrentPage)
        },
        goToPrevious() {
            this.$emit('pagination', this.mdPage - 1)
        },
        goToNext() {
            this.$emit('pagination', this.mdPage + 1)
        }
    },
    created() {
        this.currentPageSize = this.mdPageSize
    }
}
</script>

<style lang="scss">

.md-table-pagination {
    height: 56px;
    display: flex;
    flex: 1;
    align-items: center;
    justify-content: flex-end;
    border-top: 1px solid;
    font-size: 12px;

    .md-table-pagination-previous {
        margin-right: 2px;
        margin-left: 18px;
    }

    .md-field {
        width: 48px;
        min-width: 36px;
        margin: -16px 24px 0 32px;

        &:after,
        &:before {
            display: none;
        }

        .md-select-value {
            font-size: 13px;
        }
    }
}

.md-menu-content.md-pagination-select {
    max-width: 82px;
    min-width: 56px;
    margin-top: 5px;
}
</style>
