import {createApp} from 'vue'
import {createPinia} from 'pinia'
import App from './App.vue'
import router from './router'
import axios from 'axios';
import Cookies from "vue-cookies";
import stores from "@/stores/store";
import 'element-plus/dist/index.css'
import ElementPlus from 'element-plus'
import {ElementTiptapPlugin} from "element-tiptap-vue3-fixed";
import 'element-tiptap-vue3-fixed/lib/style.css'
import "bootstrap/dist/css//bootstrap-utilities.css"
import './assets/main.css'
import { PrismEditor } from 'vue-prism-editor';
import 'vue-prism-editor/dist/prismeditor.min.css';
import '@fortawesome/fontawesome-free/css/all.css';

const app = createApp(App)
app.use(createPinia())
app.use(router)
app.use(stores)
app.use(ElementPlus)
app.use(ElementTiptapPlugin)
app.component('PrismEditor', PrismEditor);
app.mount('#app')


export const showCustomAlert = (message) => {
    const customAlert = document.createElement('div');
    customAlert.classList.add('custom-alert');
    customAlert.textContent = message;

    document.body.appendChild(customAlert);

    setTimeout(() => {
        customAlert.remove();
    }, 1500);
}

const token = Cookies.get('accessToken');
if (token) {
    stores.commit('setToken', token);
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
                    .post("api/auth/login/refresh", {}, {
                        headers: {
                            RefreshToken : refreshToken,
                        },
                    })
                    .then((response) => {
                        const accessToken = response.data.accessToken;
                        if (accessToken) {
                            Cookies.set('accessToken', accessToken, 60*60);
                            stores.commit('setToken', accessToken);
                        }
                        const refreshToken = response.data.refreshToken;
                        if(refreshToken){ //
                            Cookies.set('refreshToken', refreshToken, 60*60*24*30);
                        }
                        // router.replace({ name: "/" });
                        // return Promise.resolve(response);
                        window.location.reload()
                    })
            } else {
                stores.commit("setToken", "");
                Cookies.remove('accessToken'); // 쿠키에서 access token 값 삭제
                Cookies.remove('refreshToken'); // 쿠키에서 refresh token 값 삭제
                router.replace("/").then(()=>{window.location.reload();});
            }
        }
        return Promise.reject(error);
    }
)