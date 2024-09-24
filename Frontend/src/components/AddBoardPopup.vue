<script setup>
import { onMounted } from 'vue';
import { getData } from '@/lib/fetchMethod';
import router from '@/router';
import { add } from 'cypress/types/lodash';

const addBoardName = ref('')

const emit = defineEmits(['confirm', 'close'])

onMounted(async () => {
    await fetchStatus()
})

const disabled = ref(false)

const fetchStatus = async () => {
    const response = await getData(import.meta.env.VITE_STATUS_URL)
    allStatus.value = await response
}

const confirmHandle = () => {
    const newBoard = {
        title: addBoardName.value ? addBoardName.trim() : null
    }
    emit('confirm', newBoard) 
    addBoardName.value = ''
}

const closeHandle = () => {
    router.go(-1)
    emit('close')
    // Clear form when open again
    addBoardName.value = ''
    disabled.value = false
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
                        <h2 class="text-2xl font-bold mb-4">Add Task</h2>
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
 
