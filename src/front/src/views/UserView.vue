<script setup>
import axios from "axios";
import {ref} from "vue";
import router from "@/router";

const email =ref("")
const password = ref("")
const userId = ref("")

const login = function (){
    axios.post("/api/auth/login",
        {
            email: email.value,
            password : password.value,
            userId : 233
        }
    ).then(()=>{
        router.replace({name : 'home'})
    }).catch(error =>{
       alert(error)
    })
}
</script>
<template>
    <div class="login-container">
        <h1>Login</h1>
            <el-input type="email" v-model="email" placeholder="E-mail을 입력해주세요" />
            <el-input type="password" v-model="password" placeholder="password를 입력해주세요" />
            <div class="my-2">
                <el-button type="primary" @click="login()"> 로그인</el-button>
            </div>
    </div>
</template>



<style>

</style>