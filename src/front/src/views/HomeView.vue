<script setup>
import axios from "axios";
import {computed, onMounted, ref} from "vue";
import {RouterLink} from "vue-router";
import Cookies from "vue-cookies";
import {showCustomAlert} from "@/main";
import QRCode from 'qrcode';

const posts = ref([]);
const currentPage = ref(1);
const postsTotal = ref(0);

const name = ref();
const QRvalue = ref("");
const qrCanvas = ref(null)
const generateQRCode = function (){
    QRCode.toCanvas(qrCanvas.value, QRvalue.value, { width: 200 }, (error) => {
        if (error) {
            console.error(error)
        }
    })
}


const fetchData = async () => {
    try {
        const response = await axios.get(`/api/posts?page=${currentPage.value}`);
        posts.value = response.data.list;
        postsTotal.value = response.data.count;
        QRvalue.value = response.data.count.toString();
    } catch (error) {
        if (error.response) {
            const errorMessage = error.response.data.message;
            showCustomAlert(`${errorMessage}`);
        }
    }
};

// 초기 데이터 로딩
(async () => {
    await fetchData();
    generateQRCode();
})();

    const totalPages = computed(() => Math.ceil(postsTotal.value / 10));
</script>

<template>
    <main>
        <div>
            <canvas ref="qrCanvas"></canvas>
        </div>
        <ol>
            <li v-for="post in posts" :key="post.id">
                <div class="title">
                    <router-link :to="{ name: 'read', params: { postId: post.id } }">
                        {{ post.id }} : {{ post.title }}
                    </router-link>
                </div>
                <div class="content">
                    내용 : {{ post.content }}
                    작성일 : {{ post.dateTime }}
                    작성자 : {{ post.name }}
                    조회수 : {{ post.hit }}
                </div>
            </li>
        </ol>
    </main>
</template>


<style scoped>
ol {
    list-style: none;
    padding: 0;
}

li {
}

li .title {
    font-size: 1.3rem;
    color: #74FF94;
}

li .content {
    font-size: 0.95rem;
    color: #23494A;
}

li:last-child {
    margin-bottom: 0;
}

.pagination {
    margin-top: 20px;
    display: flex;
    justify-content: center;
    align-items: center;
}

.pagination span {
    font-size: 1rem;
    color: #333;
    cursor: pointer;
    margin: 0 7px;
}

.pagination span.active {
    font-weight: bold;
    color: #379F51;
}

.el-button {
    margin: 0 5px;
}
 /*div {*/
 /*    display: flex;*/
 /*    justify-content: center;*/
 /*    align-items: center;*/
 /*    height: 100vh;*/
 /*}*/
</style>