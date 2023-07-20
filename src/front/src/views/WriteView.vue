<template>
    <div>
        <el-form :model="form" :rules="rules">
            <el-form-item label="제목" required>
                <el-input v-model="form.title" placeholder="제목을 입력해주세요" />
            </el-form-item>
            <el-form-item label="내용" required>
                <el-input v-model="form.content" type="textarea" rows="15" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="write()">글 작성 완료</el-button>
            </el-form-item>
            <div>
                <div class="editor-container">
                    <div v-for="(block, index) in contentBlocks" :key="index">
                        <div v-if="block.type === 'text'">{{ block.content }}</div>
                        <prism-editor v-else v-model="block.code" :language="block.language"></prism-editor>
                    </div>
                </div>
                <button @click="addCodeBlock">Add Code Block</button>
                <button @click="addTextBlock">Add Text Block</button>
            </div>
        </el-form>
    </div>
</template>

<script setup>
import { mapGetters } from 'vuex';
import axios from 'axios';
import store from "@/stores/store";
import { ref } from 'vue';
import router from "@/router";
import Cookies from "vue-cookies";
import {showCustomAlert} from "@/main";
// import Prism Editor
import { PrismEditor } from 'vue-prism-editor';
import 'vue-prism-editor/dist/prismeditor.min.css'; // import the styles somewhere


const form = ref({
    title: '',
    content: '',
});
const rules = {
    title: [{ required: true, message: '제목을 입력해주세요', trigger: 'blur' }],
    content: [{ required: true, message: '내용을 입력해주세요', trigger: 'blur' }],
};

const write = () => {
    const token = Cookies.get('accessToken');
    axios
        .post('/api/posts', {
            title: form.value.title,
            content: form.value.content,
        }, {
            headers: {
                Authorization: token,
            },
        })
        .then(() => {
            router.replace({ name: "post" });
        })
        .catch((error) => {
            if (error.response) {
                const errorMessage = error.response.data.message;
                showCustomAlert(`${errorMessage}`)
            }
        });
};

const contentBlocks = ref([]);

const addCodeBlock = () => {
    contentBlocks.value.push({
        type: "code",
        code: "",
        language: "java"
    });
};

const addTextBlock = () => {
    contentBlocks.value.push({
        type: "text",
        content: ""
    });
};

</script>






<style>

</style>
