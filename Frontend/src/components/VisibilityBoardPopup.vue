<script setup>
import { ref } from 'vue'


const isPrivate = ref(false)
const showModal = ref(false)

const confirmBoard = () => {
    isPrivate.value = !isPrivate.value
    showModal.value = false
}

const cancelBoard = () => {
    showModal.value = false
}
</script>

<template>
    <div class="container mx-auto p-5">
        <!-- Section Header -->
        <div class="flex items-center justify-between mb-5">
            <h1 class="itbkk-board-name text-3xl font-bold">ITBKK OLAN personal board</h1>

            <!-- Toggle Button -->
            <div class="flex items-center">
                <span class="mr-3 text-gray-600">Private</span>
                <input type="checkbox" id="toggle" class="toggle hidden" />
                <label for="toggle"
                    class="toggle-label bg-gray-400 border-2 border-gray-400 rounded-full w-12 h-6 cursor-pointer relative flex items-center">
                    <div class="toggle-circle bg-white w-4 h-4 rounded-full shadow absolute left-1 transition-transform duration-300 ease-in-out"
                        :class="{ 'transform translate-x-6': isPrivate }"></div>
                </label>
                <span class="ml-3 text-gray-600">Public</span>
            </div>
        </div>

        <!-- Modal -->
        <div v-if="showModal"
            class="fixed inset-0 bg-gray-500 bg-opacity-50 backdrop-blur flex items-center justify-center z-50">
            <div class="bg-white p-5 rounded-lg shadow-lg w-96">
                <h2 class="text-xl font-bold mb-3">Board visibility changed!</h2>
                <p class="itbkk-message mb-5">
                    In public, anyone can view the board, task list and task detail of tasks in the board. Do you want
                    to change the visibility to Public?
                </p>
                <div class="flex justify-end space-x-3">
                    <button @click="confirmBoard()"
                        class="itbkk-button-cancel bg-red-500 hover:bg-red-600 text-white font-bold py-2 px-4 rounded">
                        Cancel
                    </button>
                    <button @click="cancelBoard()"
                        class="itbkk-button-confirm bg-green-500 hover:bg-green-600 text-white font-bold py-2 px-4 rounded">
                        Confirm
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>