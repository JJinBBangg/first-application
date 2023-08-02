<script setup>
import {RouterLink} from "vue-router";
import {computed, ref, watch} from "vue";
import {useStore} from "vuex";
import stores from "@/stores/store";
import Cookies from "vue-cookies";
import router from "@/router";

const token = computed(() => stores.getters.getToken);
const isLoggedIn = computed(() => !!token.value);

const logout = function () {
    console.log(Cookies.get('accessToken'))
    console.log(Cookies.get('refreshToken'))
    stores.commit("setToken", ""); // 로그아웃 시 토큰 초기화
    Cookies.remove('accessToken'); // 쿠키에서 access token 값 삭제
    Cookies.remove('refreshToken'); // 쿠키에서 refresh token 값 삭제
    router.replace({name: "home"})
    console.log(Cookies.get('accessToken'))
    console.log(Cookies.get('refreshToken'))
};

const type = ref("all")
let search = ""
const store = useStore();
const submit = () => {
    store.dispatch('updateType', type)
    store.dispatch('updateSearch', search)
}
</script>
<template>
    <el-header>
        <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" :ellipsis="false">
            <RouterLink to="/post">
                <el-menu-item index="1">
                    Home
                </el-menu-item>
            </RouterLink>
<!--            <div class="search">-->
<!--                <select v-model="type">-->
<!--                    <option value="all" label="전체"/>-->
<!--                    <option value="title" label="제목"/>-->
<!--                    <option value="content" label="내용"/>-->
<!--                    <option value="writer" label="작성자"/>-->
<!--                </select>-->
<!--                <input type="text" v-model="search" placeholder="검색할 내용을 입력하세요." @keyup.enter="submit"/>-->
<!--                <button @click="submit">검색</button>-->
<!--            </div>-->
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
                <RouterLink to="/user">
                    <el-menu-item index="4-3">회원정보수정</el-menu-item>
                </RouterLink>
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
