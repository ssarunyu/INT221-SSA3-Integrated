<script setup>
import { ref } from 'vue'
import Toast from '@/components/Toast.vue'
import router from '@/router';



</script>

<template>
    <div
        class="flex items-center justify-center h-screen bg-blue-200 bg-opacity-75 backdrop-blur-md overflow-hidden bg-blend-hard-light">

        <div class="fiexd card-content p-4 rounded-box shadow-lg border border-gray-300 bg-white backdrop-blur">
            <!-- title -->
            <p class="font-bold mb-2 text-lg text-center ">Sign in Taskboard</p>
            <!-- Username -->
            <div>
            <input class="itbkk-username rounded-lg p-2 border border-black w-full mb-4" placeholder="Username"
                type="text" required />
            </div>
            
            <!-- Password -->
            <input class="itbkk-password rounded-lg p-2 border border-black w-full mb-4" placeholder="Password"
                type="password" required />
            <!-- Button Login -->
            <button v-on:click="loginBtn"
                class="bg-green-500 itbkk-button-signin border rounded-3xl w-full py-2 text-white hover:bg-green-600">Login
            </button>
        </div>
    </div>
</template>

<style scoped>
button {
    font-weight: bold;
}
</style>
