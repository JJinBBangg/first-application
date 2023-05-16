<script setup>

import axios from "axios";
import {onMounted, ref} from "vue";
import {useRouter} from "vue-router";
import store from "@/stores/store";
import Cookies from "vue-cookies";

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
                const errorCode = error.response.data.code;
                const errorMessage = error.response.data.message;
                alert(`Error ${errorCode}: ${errorMessage}`);
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
                router.replace({ name: "post" });
            })
            .catch((error) => {
                if (error.response) {
                    const errorCode = error.response.data.code;
                    const errorMessage = error.response.data.message;
                    alert(`Error ${errorCode}: ${errorMessage}`);
                }
            });
    }
};
onMounted(() => {
    axios.get(`/api/posts/${props.postId}`).then((response) => {
        post.value = response.data;
    }).catch(error => {
        if (error.response) {
            const errorCode = error.response.data.code;
            const errorMessage = error.response.data.message;
            alert(`에러코드(${errorCode}) : ${errorMessage}`)
        }
    })
});

</script>

<template>
    <h2>제목 : {{ post.title }}</h2>
    <p>내용 : {{ post.content }}</p>
    <p>작성일 : {{ post.dateTime }}</p>
    <p>조회수 : {{ post.hit }}</p>
    <p>작성자 : {{ post.name }}</p>
    <el-button type="warning" @click="moveToEdit()">수정</el-button>
    <el-button type="danger" @click="deletePost()">삭제</el-button>
</template>
