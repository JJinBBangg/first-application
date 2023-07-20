<template>
    <hr>
  지역
    <el-input type="text" v-model="region"/>
    <el-button @click="searchWeather">조회</el-button>
    <ol>
        <li v-for="(weather, index) in weatherList" :key="index">
        {{weather.category}}  ::  {{weather.fcstValue}}
        </li>
    </ol>

</template>

<script setup>
import {ref} from "vue";
import axios from "axios";
import {showCustomAlert} from "@/main";

const region = ref("");
const weatherList = ref([]);

const searchWeather = () => {
    console.log(region.value)
    axios.get(`/api/weather?region=${region.value}`)
        .then((response) => {
            weatherList.value = response.data.response.body.items.item
            console.log(response.data.response.body.items.item)
        }).catch((error) => {
        if (error.response) {
            const errorMessage = error.response.data.message;
            showCustomAlert(`${errorMessage}`)
        }
    });
}


</script>

<style scoped>

</style>