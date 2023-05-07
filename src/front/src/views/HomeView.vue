<script setup>
import axios from "axios";
import {ref} from "vue";

const posts = ref([]);
axios.get("/api/posts").then((response) => {
    response.data.forEach((r) => {
        posts.value.push(r)
    });
});
</script>
<template>
    <main>
        <ol>
            <li v-for="post in posts" :key="post.id" >
                <div class="title">
                <router-link :to="{name: 'read', params :{postId : post.id}}">
                    {{ post.title }}
                </router-link>
                </div>
                <div class="content">
                    {{ post.content }}
                </div>
            </li>
        </ol>
    </main>
</template>

<style scoped>
ol{
    list-style: none;
    padding: 0;
}
li {
}
li .title {
    font-size : 1.3rem;
    color : #74FF94;
}
li .content {
    font-size: 0.95rem;
    color : #23494A;
}
li:last-child {
    margin-bottom: 0;
}
</style>

