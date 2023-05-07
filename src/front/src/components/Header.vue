<script setup>
import {RouterLink} from "vue-router";
</script>
<template>
    <el-header>
        <el-menu
                :default-active="activeIndex"
                class="el-menu-demo"
                mode="horizontal"
                :ellipsis="false"
        >
            <el-menu-item index="0">
                <RouterLink to="/">Home</RouterLink>
            </el-menu-item>
            <el-menu-item index="0">
                <RouterLink to="/write">글 작성</RouterLink>
            </el-menu-item>
            <div class="flex-grow"/>
            <el-menu-item index="1"><RouterLink to="/write">로그인</RouterLink></el-menu-item>
            <el-menu-item index="2">검색</el-menu-item>
            <el-sub-menu index="3">
                <template #title>더보기</template>
                <el-menu-item index="3-1">item one</el-menu-item>
                <el-menu-item index="3-2">item two</el-menu-item>
                <el-menu-item index="3-3">item three</el-menu-item>
                <el-sub-menu index="3-4">
                    <template #title>item four</template>
                    <el-menu-item index="3-4-1">item one</el-menu-item>
                    <el-menu-item index="3-4-2">item two</el-menu-item>
                    <el-menu-item index="3-4-3">item three</el-menu-item>
                </el-sub-menu>
            </el-sub-menu>
        </el-menu>
    </el-header>
</template>
<style>
.flex-grow {
    flex-grow: 1;
}
body{
    margin: 0;
}
</style>
