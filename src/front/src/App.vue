<script setup>
import Header from "@/components/Header.vue";
import Content from "@/components/Content.vue";
import SideBar from "@/components/SideBar.vue";
</script>

<template>
    <div class="app-container">
        <Header class="header"/>
        <div class="content-container">
            <div class="content">
                <Content/>
            </div>
            <div class="sideBar">
                <SideBar/>
            </div>
        </div>
    </div>
</template>

<style scoped>
.app-container {
    min-height: 100vh;
    width: 100vw;
}

.header {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    z-index: 9999;
}
.content{
    margin-left: 5vw;
    width: 60vw;
}
.sideBar{
    margin-right: 5vw;
    width: 30vw;
}
.content-container {
    display: inline-flex;
    margin-top: 60px; /* Adjust this value as needed for spacing below the header */
}

body {
    margin: 0;
    width: 100%;
    font-family: Pretendard, -apple-system, BlinkMacSystemFont, system-ui, Roboto,
    "Helvetica Neue", "Segoe UI", "Apple SD Gothic Neo", "Noto Sans KR",
    "Malgun Gothic", "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol",
    sans-serif;
}
</style>