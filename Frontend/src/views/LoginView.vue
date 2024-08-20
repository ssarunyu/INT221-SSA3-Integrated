<script setup>
import { ref } from 'vue'
import Toast from '@/components/Toast.vue'



</script>

<template>
    <div class="flex items-center justify-center h-screen bg-gray-100 overflow-hidden bg-blend-hard-light">
        <div class="fiexd card-content p-4 rounded-box shadow-lg border border-gray-300 bg-white backdrop-blur">
            <lable class="font-bold mb-2 text-center">Username</lable>
            <div>
                <input class="itbkk-username rounded-lg p-2 border border-black w-full mb-4" type="text" required />
            </div>
            <label class="font-bold mb-2 text-center">Password</label>
            <input class="itbkk-password rounded-lg p-2 border border-black w-full mb-4" type="password" required />
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
