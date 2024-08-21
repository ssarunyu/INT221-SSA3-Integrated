import { defineStore } from "pinia";
import { ref } from 'vue';

// User Store
export const useUserStore = defineStore("userStore", () => {
  const user = ref({}) 

  function setToken(token) {
    user.value = {
      token: token
    } 
  }

  function setPayload(payload) {
    user.value = {
      payload: payload
    }
  }

  function getUser() {
    return user.value
  }

  return {setToken, setPayload, getUser};
})
