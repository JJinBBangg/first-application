import { createRouter, createWebHistory } from 'vue-router'
import WriteView from "@/views/WriteView.vue";
import ReadView from "@/views/ReadView.vue";
import EditView from "@/views/EditView.vue";
import SignUpView from "@/views/SignUpView.vue";
import PostView from "@/views/PostView.vue";
import LoginView from "@/views/LoginView.vue";
import HomeView from "@/views/HomeView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/write',
      name: 'write',
      component: WriteView
    },
    {
      path : '/read/:postId',
      name : 'read',
      component: ReadView,
      props : true
    },
    {
      path : '/edit/:postId',
      name : 'edit',
      component: EditView,
      props : true
    },
    {
      path : '/login',
      name : 'login',
      component: LoginView

    },
    {
      path : '/signup',
      name : 'signup',
      component: SignUpView

    },
    {
      path: '/post',
      name: 'post',
      component: PostView
    }

  ]
})

export default router
