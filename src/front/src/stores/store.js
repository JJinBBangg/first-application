import { createStore } from 'vuex'
import {ref} from "vue";

export default createStore({
  state: {
    token: ref(""),
    search: ref(""),
    type: ref("")
  },
  mutations: {
    setToken (state, token) {
      state.token = token
    },
    updateSearch(state, search){
      state.search = search;
    },
    updateType(state, type) {
      state.type = type
    },
  },
  actions : {
    setToken({commit}, token){
      commit('setToken', token)
    },
    updateSearch({commit}, search){
      commit('updateSearch', search)
    },
    updateType({commit}, type) {
      commit('updateType', type)
    },

  },
  getters: {
    getToken (state) {
      return state.token
    },
    getSearch(state){
      return state.search;
    },
    getType(state) {
      return state.type
    },
  }
})
