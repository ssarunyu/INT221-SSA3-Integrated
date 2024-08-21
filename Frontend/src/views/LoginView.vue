<script setup>
import { ref } from 'vue'
import router from '@/router';
import { postLogin } from '@/lib/fetchMethod.js'

const user = ref({})
const username = ref('')
const password = ref('')
const userIncorrect = ref('')
const userLogin = async () => {
  user.value = {
    username: username.value,
    password: password.value
  }
  try {
    const res = await postLogin('http://localhost:8080/login', user.value)

    console.log(res)
    if (res.ok) {
      router.push('/task')
    } else {
      userIncorrect.value = "Username or Password is incorrect"
    }
  } catch (error) {
    userIncorrect.value = "Username or Password is incorrect"
  }

}
</script>

<template>
  <div
    class="flex items-center justify-center h-screen bg-blue-200 bg-opacity-75 backdrop-blur-md overflow-hidden bg-blend-hard-light">

    <div class="fiexd card-content p-4 rounded-box shadow-lg border border-gray-300 bg-white backdrop-blur">
      <!-- title -->
      <p class="font-bold mb-2 text-lg text-center ">Sign in Taskboard</p>
      <!-- Username -->
      <div>
        <input v-model="username" class="itbkk-username rounded-lg p-2 border border-black w-full mb-4"
          placeholder="Username" type="text" required />
      </div>

      <!-- Password -->
      <input v-model="password" class="itbkk-password rounded-lg p-2 border border-black w-full mb-4"
        placeholder="Password" type="password" required />
      <!-- Button Login -->
      <button @click="userLogin"
        class="bg-green-600 itbkk-button-signin border rounded-3xl w-full py-2 text-white hover:bg-green-500">
        Login
      </button>
      <p v-if="userIncorrect" class="text-red-500 text-center mt-2">{{ userIncorrect }}</p>
    </div>
  </div>
</template>

<style scoped>
button {
  font-weight: bold;
}
</style>
