<template>
    <h1>제목</h1>
    <el-input v-model="title"/>
    <h2>내용</h2>
        <el-tiptap
                :width="700"
                :height="400"
                :extensions="extensions"
                v-model:content="content"
                :lang="ko"
                placeholder="Write something ..."/>
    <el-button @click="write">작성완료</el-button>
</template>

<script setup>
import ko from 'element-tiptap-vue3-fixed/lib/locales/ko'
import {onMounted, ref} from "vue";
import {
    // all extensions
    Doc,
    Text,
    Paragraph,
    Heading,
    Bold,
    Italic,
    Strike,
    Underline,
    Link,
    Image,
    Blockquote,
    BulletList, // use with ListItem
    OrderedList, // use with ListItem
    TextAlign,
    Indent,
    HorizontalRule,
    HardBreak,
    History,
    Fullscreen,
    CodeView
} from "element-tiptap-vue3-fixed";
import codemirror from "codemirror";
import "codemirror/lib/codemirror.css"; // import base style
import "codemirror/mode/xml/xml.js"; // language
import "codemirror/addon/selection/active-line.js"; // require active-line.js
import "codemirror/addon/edit/closetag.js";
import axios from "axios";
import Cookies from "vue-cookies";
import {showCustomAlert} from "@/main"; // autoCloseTags
import {v4} from 'uuid'

const extensions = [
    Doc,
    Text,
    Paragraph,
    Heading.configure({level: 5}),
    Bold.configure({bubble: true}),
    Underline.configure({bubble: true}),
    Italic.configure({bubble: true}),
    Strike.configure({bubble: true}),
    Link.configure({bubble: true}),
    Image,
    Blockquote,
    TextAlign,
    BulletList.configure({bubble: true}),
    OrderedList.configure({bubble: true}),
    Indent,
    HardBreak,
    HorizontalRule.configure({bubble: true}),
    Fullscreen,
    CodeView.configure({
        codemirror,
        codemirrorOptions: {
            styleActiveLine: true,
            autoCloseTags: true
        }
    }),
    History
]
const content = ref([]);
const modifiedHTML = ref("")
const title = ref("")
const write = () => {
    const parser = new DOMParser();
    const doc = parser.parseFromString(content.value, "text/html")
    console.log(doc)
    const i = doc.getElementsByTagName('img')
    for (const img of i) {
        const src = img.getAttribute('src')
        if (!(src.startsWith('http'))) {
            const formData = new FormData();
            const uuid = v4()
            console.log(uuid)
            formData.append('image', src, uuid)

            // 업로드인경우 s3 파일저장
            axios.post("/api/file", {}).then((response) => {

            }).catch((error) => {
                if (error.response) {
                    const errorMessage = error.response.data.message;
                    showCustomAlert(`${errorMessage}`)
                }
            });
            // s3저장된 주소값으로 변경
            img.setAttribute('src', '주소값')
        }
    }
    modifiedHTML.value = doc.body.innerHTML
    console.log(modifiedHTML.value)
}
onMounted(() => {
    const token = Cookies.get('accessToken')
    axios.post("/api/auth/user/login", {}, {
        headers: {
            Authorization: token
        }
    }).then((response) => {
        if (!response.data.authResult) {

        }
    }).catch((error) => {
        if (error.response) {
            const errorMessage = error.response.data.message;
            showCustomAlert(`${errorMessage}`)
        }
    });

})
</script>
<style scoped>
.el-input {
    width: 44rem;
}
.kko{
    width: 45rem;
}
</style>