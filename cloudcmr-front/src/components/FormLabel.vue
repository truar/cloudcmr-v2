<template>
    <label :for="inputName">
        <slot></slot>
    </label>
</template>

<script>
export default {
    name: 'FormLabel',
    props: {
        inputName: String
    }
}
</script>
