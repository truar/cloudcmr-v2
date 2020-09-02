import { shallowMount } from '@vue/test-utils'
import EasyComponent from '@/components/EasyComponent'

const factory = () => {
    return shallowMount(EasyComponent)
}

describe('EasyComponents', () => {
    it('display with 0 as default', () => {
        const wrapper = factory()
        expect(wrapper.find('span').text()).toEqual('0')
    })
    it('handle a click trigger', () => {
        const wrapper = factory()
        wrapper.find('.input-submit').trigger('click')
        expect(wrapper.find('span').text()).toEqual('1')
    })
    it('listen for emits event', () => {
        const wrapper = factory()
        wrapper.find('.input-submit').trigger('click')
        wrapper.emitted()
    })
})
