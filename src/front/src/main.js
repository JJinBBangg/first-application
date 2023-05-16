import {createApp} from 'vue'
import {createPinia} from 'pinia'
import App from './App.vue'
import router from './router'

import 'element-plus/dist/index.css'
import ElementPlus from 'element-plus'

import "bootstrap/dist/css//bootstrap-utilities.css"

import './assets/main.css'

const app = createApp(App)
app.use(createPinia())
app.use(router)
app.use(ElementPlus)
app.mount('#app')


import axios from 'axios';
import Cookies from "vue-cookies";
import store from "@/stores/store";

const token = Cookies.get('accessToken');
if (token) {
    store.commit('setToken', token);
}
axios.interceptors.response.use(
    response => response,
    error => {
        if (error.response && error.response.data.message == "로그인이 필요합니다.") {
            // Unauthorized 오류 발생 시 로그인 페이지로 이동
            router.push({name: "login"})
        }

        if (error.response && error.response.data.message == "로그인 인증이 만료되었습니다.") {
            // 쿠키에서 refresh 토큰이 존재하면 서버로 토큰 전송
            const refreshToken = Cookies.get('refreshToken');
            if (refreshToken) {
                axios
                    .post("/auth/login/refresh", {}, {
                        headers: {
                            RefreshToken: refreshToken,
                        },
                    })
                    .then((response) => {
                        const accessToken = response.data.accessToken;
                        if (accessToken) {
                            Cookies.set('accessToken', accessToken, { expires : new Date().getTime() + (40 * 60 * 1000) });
                            store.commit('setToken', accessToken);
                            // 1시간 동안 유효한 쿠키 설정 // 서버의 token 유효기간보다 30분 길게 설정
                        }
                        const refreshToken = response.data.refreshToken;
                        if(refreshToken){ //
                            Cookies.set('refreshToken', refreshToken, { expires: 31 });
                            // 31일 동안 유효한 쿠키 설정 // 서버의 token 유효기간보다 1일 길게 설정
                        }
                    });
            }
        }
            return Promise.reject(error);
    }
)