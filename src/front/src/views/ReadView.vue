<script setup>

import axios from "axios";
import {onMounted, ref} from "vue";
import {RouterLink, useRouter} from "vue-router";
import store from "@/stores/store";
import Cookies from "vue-cookies";
import { showCustomAlert } from '@/main.js';

const router = useRouter();
const props = defineProps({
    postId: {
        type: [Number, String],
        require: true
    },
});
const post = ref({
    id: 0,
    title: "",
    content: "",
    userId: ""
});
const moveToEdit = () => {
    const token = Cookies.get('accessToken');
    console.log(post.value.userId);
    axios
        .post('/api/auth/user/post', {
            userId: post.value.userId,
        }, {
            headers: {
                Authorization: token,
            },
        })
        .then(() => {
            router.push({name: "edit", params: {postId: props.postId}})
        })
        .catch((error) => {
            if (error.response) {
                const errorMessage = error.response.data.message;
                showCustomAlert(`${errorMessage}`)
            }
        });
}

const deletePost = () => {
    const confirmDelete = confirm("게시물을 삭제하시겠습니까?");
    if (confirmDelete) {
        const token = Cookies.get('accessToken');
        axios.delete(`/api/posts/${props.postId}`, {
            headers: {
                Authorization: token,
            },
        })
            .then(() => {
                showCustomAlert('삭제가 완료되었습니다.')
                router.replace({ name: "post" });
            })
            .catch((error) => {
                if (error.response) {
                    const errorMessage = error.response.data.message;
                    showCustomAlert(`${errorMessage}`)
                }
            });
    }
};
onMounted(() => {
    axios.get(`/api/posts/${props.postId}`).then((response) => {
        post.value = response.data;
    }).catch(error => {
        if (error.response) {
            const errorMessage = error.response.data.message;
            showCustomAlert(`${errorMessage}`)
        }
    })
});

</script>

<template>
    <div class="container">
    <h2>제목 : {{ post.title }}</h2>
    <el-input readonly v-model="post.content" />
    <p>작성일 : {{ post.dateTime }}</p>
    <p>조회수 : {{ post.hit }}</p>
    <p>작성자 : {{ post.name }}</p>
    </div>
    <el-button type="warning" @click="moveToEdit()">수정</el-button>
    <el-button type="danger" @click="deletePost()">삭제</el-button>
    <el-button type="info" @click="$router.go(-1)" >이전</el-button>

</template>

<style>
.container{

}

</style>








