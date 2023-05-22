<template>
    <div class="login-container">
        <form class="login-form">
            <div class="form-group">
                <label for="email">이메일</label>
                <input id="email" type="email" v-model="email"/>
            </div>
            <div class="form-group">
                <label for="password">비밀번호</label>
                <input id="password" type="password" v-model="password"/>
            </div>

            <span class="el-button" style="color: #1a1e21">
            <label for="keep">로그인정보저장 &nbsp;</label>
            <input type="checkbox" id="keep" v-model="keepLogin" model-value=true/>
            </span>
            <button class="btn" @click.prevent="login()">로그인</button>
            <div>
                <RouterLink style="color:white" to="/signup">
                    <button class="btn mt-2">
                        회원가입
                    </button>
                </RouterLink>
            </div>


        </form>

    </div>
</template>

<script setup>
import {mapMutations} from "vuex";
import axios from "axios";
import {onMounted, ref} from "vue";
import {RouterLink, useRouter} from "vue-router";
import store from "@/stores/store";

const email = ref("");
const password = ref("");
const {setToken} = mapMutations(["setToken"]);
import Cookies from 'vue-cookies';
import {showCustomAlert} from "@/main";

const router = useRouter();

const keepLogin = ref(false);

const login = () => {
    const expirationTime = new Date(); // 현재 시간을 기준으로 설정
    expirationTime.setTime(expirationTime.getTime() + (1 * 60 * 60 * 1000))
    axios.post("/api/auth/login", {
        email: email.value,
        password: password.value,
    }).then((response) => {
        const accessToken = response.data.accessToken;
        if (accessToken) {
            Cookies.set('accessToken', accessToken, 60 * 60);
            store.commit('setToken', accessToken);
            // 1시간 동안 유효한 쿠키 설정 // 서버의 token 유효기간보다 30분 길게 설정
        }
        const refreshToken = response.data.refreshToken;
        if (keepLogin.value == true) { //
            Cookies.set('refreshToken', refreshToken, 60 * 60 * 24 * 31);
            // 31일 동안 유효한 쿠키 설정 // 서버의 token 유효기간보다 1일 길게 설정
        }
        router.replace({name: "home"});
    }).catch((error) => {
        if (error.response) {
            const errorMessage = error.response.data.message;
            showCustomAlert(`${errorMessage}`)
        }
    });
}


</script>

<style scoped>
.login-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
}

.login-form {
    width: 300px;
    padding: 20px;
    border: 1px solid #ccc;
    border-radius: 5px;
}

.form-group {
    margin-bottom: 10px;
}

label {
    display: block;
    margin-bottom: 5px;
}

input {
    width: 100%;
    padding: 8px;
    border: 1px solid #ccc;
    border-radius: 3px;
    font-size: 1rem;
}

.btn {
    width: 100%;
    padding: 8px;
    border: none;
    border-radius: 3px;
    font-size: 1rem;
    background-color: #1a73e8;
    color: #fff;
    cursor: pointer;
}

.btn:hover {
    background-color: #0d47a1;
}
</style>