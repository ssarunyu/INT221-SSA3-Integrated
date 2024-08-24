<script setup>
import { ref } from 'vue'
import router from '@/router';
import { postLogin } from '@/lib/fetchMethod.js'
import { useUserStore } from '@/stores/UserStore.js'

const userStore = useUserStore()

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
    if (res.ok) {
      router.push({name: 'Home'})
    } else {
      userIncorrect.value = "Username or Password is incorrect"
    }
  } catch (error) {
    userIncorrect.value = "Username or Password is incorrect"
  }

}
</script>

<template>
  <div class="grid grid-cols-2 h-screen">
    <div class="flex items-center justify-center bg-white backdrop-blur-md">
      <div class="p-8 max-w-md w-full">
        <div class="mb-6">
          <!-- title -->
          <h1 class="font-bold mb-2 text-lg text-center ">Sign in Taskboard</h1>
          <!-- Username -->
          <div>
            <p>Username</p>
            <input v-model="username" class="itbkk-username rounded-md p-2 border border-gray-400 w-full mb-4"
              placeholder="Username" type="text" required />
          </div>

          <!-- Password -->
          <div>
            <p>Password</p>
            <input v-model="password" class="itbkk-password rounded-md p-2 border border-gray-400 w-full mb-4"
              placeholder="Password" type="password" required />
          </div>
          <!-- Button Login -->
          <div>
            <button @click="userLogin"
              class="bg-green-500 itbkk-button-signin font-bold rounded-md w-full py-2 text-white hover:bg-green-600">
              Login
            </button>
          </div>
          <!-- Message Incorrect -->

            <p v-if="userIncorrect" class=" text-red-500 mt-2">{{ userIncorrect }}</p>

        </div>

      </div>
    </div>
    <div class="hidden md:block">
      <!-- <p class="bg-black h-full w-full object-cover"></p> -->
      <img src="../assets/555.png" alt="sky" class="h-full w-full object-cover">
    </div>
  </div>
</template>

<style scoped>
button {
  font-weight: bold;
}
</style>
