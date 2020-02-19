<template>
    <v-app-bar app clipped-left>
        <v-app-bar-nav-icon @click.stop="$emit('toggle')" />
        <v-toolbar-title>Cloud CMR</v-toolbar-title>

        <v-spacer></v-spacer>

        <slot></slot>
    </v-app-bar>
</template>
