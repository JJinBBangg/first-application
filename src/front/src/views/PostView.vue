<script setup>
import axios from "axios";
import {computed, ref, watch} from "vue";
import { useStore} from "vuex";
import {RouterLink} from "vue-router";
import router from "@/router";
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
const store = useStore();
const storeType = computed(()=>{
    return store.getters.getType
})
const storeSearch = computed(()=>{
    return store.getters.getSearch
})
const check = watch(storeSearch, (newValue, oldValue)=>{
  if(newValue !== oldValue){
      console.log(storeSearch.value)
      search.value = storeSearch.value
      console.log(storeType.value)
      type.value = storeType.value
      console.log(currentPage.value)
      fetchData()
  }
})



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

const totalPages = computed(() => Math.ceil(postsTotal.value / 10)>10? 10:Math.ceil(postsTotal.value / 10));
</script>

<template>
    <div class="posts">
        <ol>
            <li v-for="post in posts" :key="post.id">
                <div class="title">
                    <router-link :to="{ name: 'read', params: { postId: post.id } }">
                        제목 : {{ post.title }}
                    </router-link>
                </div>
                <div class="content">
                    <p class="content__detail">내용 : {{ post.content }}</p>
                    <p>
                        작성일 : {{ post.dateTime }}
                        작성자 : {{ post.name }}
                        조회수 : {{ post.hit }}
                    </p>
                </div>
            </li>
        </ol>
    </div>
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
</template>

<style scoped>
/*.search {*/
/*    position: fixed;*/
/*    width: 80%;*/
/*    border-radius: 5px;*/
/*    z-index: 9999;*/
/*}*/

.posts {
    width: 80%;
    padding-top: 15px;
}
.content__detail{
    white-space: pre-line;
    overflow: hidden;
    line-height: 1.2rem;
    max-height: 6rem;
}

ol {
    list-style: none;
    padding: 0;
}

.pagination {
    margin-top: 20px;
    width : 80%;
    /*justify-content: center;*/
    /*align-items: center;*/
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

/*li {*/
/*}*/

/*li .title {*/
/*    font-size: 1.3rem;*/
/*    color: #74ff94;*/
/*}*/

/*li .content {*/
/*    font-size: 0.95rem;*/
/*    color: #23494a;*/
/*}*/

/*li:last-child {*/
/*    margin-bottom: 0;*/
/*}*/

</style>