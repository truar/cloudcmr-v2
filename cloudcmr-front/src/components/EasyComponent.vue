<template>
    <div class="easy-component">
        <span>{{ increment }}</span>
        <input type="submit" class="input-submit" @click="handleClick"/>
        <input type="submit" class="input-submit" @click="handleClick"/>
    </div>
</template>

<script>
export default {
    name: 'EasyComponent',
    data: () => ({
       increment: 0
    }),
    methods: {
        handleClick() {
            const increment = this.increment
            this.increment = increment + 1
            this.$emit('inputClick', this.increment)
        }
    }
}
</script>
