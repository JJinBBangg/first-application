<template>
    <TiptapWrite :options="options"/>
</template>

<script setup>
import TiptapWrite from "@/components/TiptapWrite.vue";
const options = {
    content: ' ',
    editable: true,
    supportImage: true,
    supportVideo: true
}

</script>
<style >
</style>
