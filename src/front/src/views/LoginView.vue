<template>
    <div class="login-container">
        <el-form class="login-form">
            <div class="form-group">
                <label for="email">이메일</label>
                <el-input id="email" type="email" v-model="email"/>
            </div>
            <div class="form-group">
                <label for="password">비밀번호</label>
                <el-input id="password" type="password" v-model="password"/>
            </div>

            <span class="el-button mb-2" style="color: #1a1e21">
            <labe for="keep">로그인정보저장 &nbsp;</labe>
            <input type="checkbox" id="keep" v-model="keepLogin" model-value=true/>
            </span>
            <button class="btn" @click.prevent="login()">로그인</button>
            <div>
                <RouterLink style="color:white" to="/signup">
                    <el-button class="btn mt-2">
                        회원가입
                    </el-button>
                </RouterLink>
            </div>
            <div class="wrap mt-2">
                <div class="title" style="color:black">소셜로그인</div>
                <a class="kakao" :href="URI">
                    <!-- REST_API키 및 REDIRECT_URI는 본인걸로 수정하세요 -->

                    <div class="kakao_i"></div>
                    <div class="kakao_txt">카카오톡로그인 </div>
                </a>
            </div>


        </el-form>

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

const URI = ref(`https://kauth.kakao.com/oauth/authorize?client_id=${process.env.VUE_APP_REST_API_KEY}&redirect_uri=${process.env.VUE_APP_REDIRECT_URI}&response_type=code`)
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

.title {
    margin: 0 auto;
    width: 200px;
    text-align: center;
    font-size: 20px;
    background-repeat: no-repeat;
    background-position: 0 0;
    background-size: 240px auto;
}

.kakao {
    margin-top: 15px;
    height: 50px;
    border: solid 1px #FEE500;
    background: #FEE500;
    color: #3c1d1e;
    font-size: 18px;
    box-sizing: border-box;
    border-radius: 5px;
    cursor: pointer;
    width: 100%; /* Updated width to match the container width */
    max-width: 450px; /* Added max-width to limit the button size */
    display: flex;
}

.kakao_i {
    width: 40px;
    height: 100%;
    background-size: 90%;
    background-position: 50%;
    margin: 0 20px;
}

.kakao_txt {
    width: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 13px;
    padding-right: 60px;
}
</style>