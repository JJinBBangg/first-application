<script setup>
import axios from "axios";
import {computed, ref} from "vue";
import {RouterLink} from "vue-router";
import router from "@/router";
import store from "@/stores/store";
import Cookies from "vue-cookies";
import {showCustomAlert} from "@/main";

const currentPage = ref(1);
const postsTotal = ref(0);

const goToPage = (page) => {
    currentPage.value = page;
    fetchData();
};

const getPreviousPage = () => {
    if (currentPage.value > 1) {
        currentPage.value--;
        fetchData();
    }
};

const getNextPage = () => {
    if (currentPage.value < totalPages.value) {
        currentPage.value++;
        fetchData();
    }
};
const search = ref("");
const type = ref('all');
const posts = ref([]);

const fetchData = () => {
    axios
        .get(`/api/posts?page=${currentPage.value}&search=${search.value}&type=${type.value}`)
        .then((response) => {
            posts.value = response.data.list;
            postsTotal.value = response.data.count;
        })
        .catch((error) => {
            if (error.response) {
                const errorMessage = error.response.data.message;
                showCustomAlert(`${errorMessage}`)
            }
        });
};
const write = () => {
    const token = Cookies.get('accessToken');
    axios
        .post(`/api/auth/user/login`, {
            userId: null
        }, {
            headers: {
                Authorization: token,
            }
        }).then((response) => {
        if (true == response.data.authResult)
            router.replace({name: "write"});
        }).catch((error) => {
            if (error.response) {
                const errorMessage = error.response.data.message;
                showCustomAlert(`${errorMessage}`)
            }
        });
};


// 초기 데이터 로딩
fetchData();

const totalPages = computed(() => Math.ceil(postsTotal.value / 10));
</script>

<template>
    <main>
        <el-select v-model="type">
            <el-option value="all" label="전체"/>
            <el-option value="title" label="제목"/>
            <el-option value="content" label="내용"/>
            <el-option value="writer" label="작성자"/>
        </el-select>
        <el-button @click="fetchData">검색</el-button>
        <el-input type="text" v-model="search" placeholder="검색할 키워드를 입력하세요." @keyup.enter="fetchData" />

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

        <div class="pagination">
            <el-button @click="getPreviousPage" :disabled="currentPage === 1">이전</el-button>
            <span
                    v-for="(pageNumber, index) in totalPages"
                    :key="pageNumber"
                    :class="{ active: currentPage === pageNumber }"
                    @click="goToPage(pageNumber)"
            >
        {{ pageNumber }}
        <template v-if="index < totalPages - 1">  </template>
      </span>
            <el-button @click="getNextPage" :disabled="currentPage === totalPages">다음</el-button>
            <el-button @click="write" class="item">글 작성</el-button>
        </div>
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
    color: #74ff94;
}

li .content {
    font-size: 0.95rem;
    color: #23494a;
}

li:last-child {
    margin-bottom: 0;
}

.pagination {
    margin-top: 20px;
    display: flex;
    justify-content: center;
    align-items: center;
    flex-wrap: wrap;
}

.pagination span {
    font-size: 1rem;
    color: #333;
    cursor: pointer;
    margin: 0 7px;
}

.pagination span.active {
    font-weight: bold;
    color: #379f51;
}
</style>