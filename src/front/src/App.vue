<script setup>
import {RouterLink, RouterView} from 'vue-router'
import Header from "@/components/Header.vue";
import Content from "@/components/Content.vue";
import Footer from "@/components/Footer.vue";
</script>
<template>
    <Header class="header"/>
    <Content/>
    <Footer/>
</template>
<style scoped>
.header {
    flex-grow: 1;
}
body{
    margin: 0;
    width: 100%;
    font-family: Pretendard, -apple-system, BlinkMacSystemFont, system-ui, Roboto,
        "Helvetica Neue", "Segoe UI", "Apple SD Gothic Neo", "Noto Sans KR",
        "Malgun Gothic", "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol",
        sans-serif;
}
</style>
