<template>
    <simple-layout>
        <template slot="subtitle"><span class="md-title">Adhérents</span></template>
    </simple-layout>
</template>

<script>
    import SimpleLayout from '@/pages/layouts/SimpleLayout.vue'
    // @ is an alias to /src
    export default {
        components: {
            SimpleLayout
        },
        name: 'Members'
    }
</script>
