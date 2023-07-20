<template>
    <el-input type="text" v-model="messageSend"/>
    <el-button @click="sendMessage" >전송</el-button>
   <ol>
       <li v-for="msg in messages" :key="msg.id">{{ msg }}</li>
   </ol>
</template>

<!--<script setup>-->
<!--// WebSocket 연결-->
<!--import {onBeforeMount, onMounted, onUnmounted, ref} from "vue";-->
<!--import axios from "axios";-->
<!--import Cookies from "vue-cookies";-->
<!--import SockJS from 'sockjs-client';-->


<!--const messageSend = ref("");-->
<!--const socket = new WebSocket("ws://localhost:8085/chat");-->
<!--// let socket = new SockJS("http://localhost:8085");-->
<!--// 대화방 식별자 전송-->
<!--const identifier = "대화방 식별자"; // 대화방 식별자 값 설정-->
<!--socket.onopen = () => {-->
<!--    // socket.send(JSON.stringify({identifier}));-->
<!--    const roomName = "대화방 이름"; // 대화방 이름 설정-->
<!--    console.log(roomName)-->
<!--    socket.send(JSON.stringify({ destination: "/app/createRoom", body: roomName }));-->
<!--};-->
<!--const messages = ref([]);-->


<!--// 서버로부터 메시지 수신-->
<!--socket.onmessage = (event) => {-->
<!--    const receivedMessage = JSON.parse(event.data);-->
<!--    console.log(receivedMessage)-->
<!--    // 메시지 배열에 추가-->
<!--    messages.value.push(receivedMessage);-->

<!--};-->

<!--// 서버로 메시지 전송-->
<!--const sendMessage = () => {-->

<!--    console.log(messageSend.value)-->
<!--    socket.send(JSON.stringify(messageSend.value));-->
<!--};-->

<!--onUnmounted(()=>{-->
<!--    socket.close()-->
<!--})-->

<!--// onMounted(() => {-->
<!--//     const token = Cookies.get('accessToken');-->
<!--//     axios.post("",{-->
<!--//-->
<!--//     }, {-->
<!--//         headers:{-->
<!--//             Authorization : token-->
<!--//         }-->
<!--//     })-->
<!--//         .then((response) => {-->
<!--//-->
<!--//         }).catch((error) => {-->
<!--//-->
<!--//     })-->
<!--// })-->
<!--</script>-->

<!--<style scoped>-->

<!--</style>-->