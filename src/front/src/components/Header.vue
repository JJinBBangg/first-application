<script setup>
import {RouterLink} from "vue-router";
import {computed, ref, watch} from "vue";
import store from "@/stores/store";
import Cookies from "vue-cookies";
import router from "@/router";
const token = computed(() => store.getters.getToken);
const isLoggedIn = computed(() => !!token.value);

const logout = function () {
    console.log(Cookies.get('accessToken'))
    console.log(Cookies.get('refreshToken'))
    store.commit("setToken", ""); // 로그아웃 시 토큰 초기화
    Cookies.remove('accessToken'); // 쿠키에서 access token 값 삭제
    Cookies.remove('refreshToken'); // 쿠키에서 refresh token 값 삭제
    router.replace({name: "home"})
    console.log(Cookies.get('accessToken'))
    console.log(Cookies.get('refreshToken'))
};

</script>
<template>
    <el-header>
        <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" :ellipsis="false">
            <RouterLink to="/">
                <el-menu-item index="0">
                    Home
                </el-menu-item>
            </RouterLink>
            <RouterLink to="/post">
                <el-menu-item index="1">
                    게시판
                </el-menu-item>
            </RouterLink>
            <div class="flex-grow"/>
            <RouterLink v-if="!isLoggedIn" to="/login">
                <el-menu-item index="2">
                    로그인
                </el-menu-item>
            </RouterLink>

            <el-menu-item v-else @click="logout()" index="3">로그아웃
            </el-menu-item>


            <el-sub-menu index="4">
                <template #title>더보기</template>

                <el-menu-item index="4-1">1:1 문의</el-menu-item>
                <RouterLink to="/user"><el-menu-item index="4-3">회원정보수정</el-menu-item></RouterLink>
<!--                <el-sub-menu index="4-4">-->
<!--                    <template #title>item four</template>-->
<!--                    <el-menu-item index="4-4-1">item one</el-menu-item>-->
<!--                    <el-menu-item index="4-4-2">item two</el-menu-item>-->
<!--                    <el-menu-item index="4-4-3">item three</el-menu-item>-->
<!--                </el-sub-menu>-->
            </el-sub-menu>
        </el-menu>
    </el-header>
</template>
<style>
.flex-grow {
    flex-grow: 1;
}

body {
    margin: 0;
}
</style>
