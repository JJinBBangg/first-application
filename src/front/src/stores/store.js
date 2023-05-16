import { createStore } from 'vuex'
import {ref} from "vue";

export default createStore({
  state: {
    token: ref("")
  },
  mutations: {
    setToken (state, token) {
      state.token = token
    }
  },
  getters: {
    getToken (state) {
      return state.token
    }
  }
})