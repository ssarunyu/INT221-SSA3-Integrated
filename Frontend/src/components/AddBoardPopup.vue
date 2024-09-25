<script setup>
import router from '@/router';
const payload = JSON.parse(localStorage.getItem('payload'))
import { ref } from 'vue'
const addTitle = ref(`${payload.name} personal board`)
const closeHandle = () => {
    router.go(-1)
}
</script>

<template>
    <div class="fixed z-10 inset-0 overflow-y-auto">
        <div class="flex items-center justify-center h-screen">
            <!-- Overlay -->
            <div class="fixed inset-0 bg-gray-500 bg-opacity-75 backdrop-blur"></div>
            <!-- Popup -->
            <div class="itbkk-modal-task relative bg-white rounded-lg shadow-xl w-[70%]">
                <!-- Popup content -->
                <div class="flex flex-col p-5">
                    <form class="space-y-5" novalidate>
                        <h2 class="text-2xl font-bold mb-4">Create new board</h2>
                        <hr>
                        <div class="flex flex-col">
                            <p class="font-semibold">Name</p>
                            <input v-model="addTitle"
                                class="itbkk-board-name border border-black rounded p-2 peer invalid:border-red-500 focus:outline-none"
                                type="text" required>
                            <p class="hidden peer-invalid:block text-red-600 text-sm">
                                This field required
                            </p>
                        </div>
                    </form>
                    <div class="mt-5 space-x-5">
                        <div class="mt-5 space-x-5">
                            <button @click="confirmHandle()" :disabled="!disabled"
                                class="itbkk-button-ok disabled bg-green-500 duration-200 hover:bg-green-600 text-white font-semibold px-4 py-2 rounded disabled:bg-green-500 disabled:cursor-not-allowed disabled:opacity-50">
                                Save
                            </button>
                            <button @click="closeHandle()"
                                class="itbkk-button-cancel disabled bg-red-500 duration-200 hover:bg-red-600 text-white font-semibold px-4 py-2 rounded">
                                Cancel
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
 
