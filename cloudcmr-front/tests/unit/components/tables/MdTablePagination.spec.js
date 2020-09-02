import { createLocalVue, mount, shallowMount } from '@vue/test-utils'
import MdTablePagination from '@/components/tables/MdTablePagination'
import VueMaterial from 'vue-material'

const localVue = createLocalVue()
localVue.use(VueMaterial)

const factory = (values = {}, props = {}) => {
    return mount(MdTablePagination, {
        localVue,
        propsData: {
            ...props
        },
        data() {
            return {
                ...values
            }
        }
    })
}

describe('MdTablePagination', () => {
    it('renders a default label', () => {
        const wrapper = factory()

        expect(wrapper.find('.md-table-pagination-label').text()).toEqual('Rows per page:')
    })
    it('renders a label passed as a property', () => {
        const wrapper = factory({}, { mdLabel: 'Pages' })

        expect(wrapper.find('.md-table-pagination-label').text()).toEqual('Pages')
    })
    it('renders a select with default page size', () => {
        const wrapper = factory()
        expect(wrapper.findAll('md-option-stub').length).toBe(4)
        expect(wrapper.find('md-option-stub:nth-child(1)').text()).toEqual('10')
        expect(wrapper.find('md-option-stub:nth-child(2)').text()).toEqual('25')
        expect(wrapper.find('md-option-stub:nth-child(3)').text()).toEqual('50')
        expect(wrapper.find('md-option-stub:nth-child(4)').text()).toEqual('100')
    })
    it('renders a select with page size as props', () => {
        const wrapper = factory({}, { mdPageOptions: [5, 10] })
        expect(wrapper.findAll('md-option-stub').length).toBe(2)
        expect(wrapper.find('md-option-stub:nth-child(1)').text()).toEqual('5')
        expect(wrapper.find('md-option-stub:nth-child(2)').text()).toEqual('10')
    })
    it('renders page and item counts information', () => {
        const wrapper = factory({}, { mdSeparator: 'de', mdTotal: '100' })
        expect(wrapper.find('.md-table-pagination-information').text()).toEqual('1-10 de 100')
    })
    it('renders page and item count information 2', () => {
        const wrapper = factory({}, { mdSeparator: 'de', mdTotal: '100' })
        console.log(wrapper.html())
        // wrapper.vm.goToNext()
        // wrapper.emitted()
        wrapper.find('.md-table-pagination-next').trigger('click')
        expect(wrapper.emitted('pagination')[0]).toEqual([2])
    })
    it('renders page and item count information 3', () => {
        const wrapper = factory({}, { mdSeparator: 'de', mdTotal: '100' })
        console.log(wrapper.html())
        // wrapper.vm.goToNext()
        // wrapper.emitted()
        wrapper.find('.md-menu-content-container ul .md-list-item:nth-child(2) button.md-list-item-button').trigger('click')
        console.log(wrapper.find('.md-menu-content-container ul .md-list-item:nth-child(2) button.md-list-item-button').text())
        console.table(wrapper.emitted())
    })
})
