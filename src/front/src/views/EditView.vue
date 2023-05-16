<script setup>
import {onMounted, ref} from "vue";
import store from "@/stores/store";
import axios from "axios";
import {useRouter} from "vue-router";
import Cookies from "vue-cookies";

const router = useRouter()
const post = ref({
        id: 0,
        title: "",
        content: ""
    }
);

const props = defineProps({
    postId: {
        type: [Number, String],
        require: true
    },
});

onMounted(() => {
    axios.get(`/api/posts/${props.postId}`)
        .then((response) => {
            post.value = response.data;
        }).catch(error => {
        if (error.response) {
            const errorCode = error.response.data.code;
            const errorMessage = error.response.data.message;
            alert(`Error ${errorCode}: ${errorMessage}`)
        }
    })
})

const edit = function () {

    const token = Cookies.get('accessToken');
    axios.patch(`/api/posts/${props.postId}`, {
        title: post.value.title,
        content: post.value.content,
    }, {
        headers: {
            Authorization: token,
        },
    }).then(() => {
        router.replace({name: 'post'})
    }).catch(error => {
        if (error.response) {
            const errorCode = error.response.data.code;
            const errorMessage = error.response.data.message;
            alert(`Error ${errorCode}: ${errorMessage}`)
        }
    })
}

</script>

<template>
    <div class="edit-post">
        <div class="section">
            <p class="section-title">제목</p>
            <div class="input-wrapper">
                <el-input v-model="post.title" placeholder="제목을 입력해주세요"/>
            </div>
        </div>
        <div class="section">
            <p class="section-title">내용</p>
            <div class="input-wrapper">
                <el-input
                        v-model="post.content"
                        type="textarea"
                        :rows="textareaRows"
                        :autosize="{ minRows: 8, maxRows: 20 }"
                />
            </div>
        </div>

        <div class="button-wrapper">
            <el-button type="warning" @click="edit()">수정</el-button>
        </div>
    </div>
</template>

<style>
.edit-post {
    max-width: 600px;
    margin: 0 auto;
    padding: 20px;
}

.section {
    margin-bottom: 20px;
}

.section-title {
    font-size: 18px;
    font-weight: bold;
    margin-bottom: 10px;
}

.input-wrapper {
    margin-bottom: 10px;
}

.button-wrapper {
    text-align: left;
}

.el-textarea__inner {
    resize: none;
    overflow-y: auto;
}
</style>