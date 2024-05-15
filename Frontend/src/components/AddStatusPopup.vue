<script setup>
import { ref } from 'vue'
import { postData } from '@/lib/fetchMethod'

const emit = defineEmits(['confirmAddStatus', 'close', 'toastItem'])
const closeHandle = () => {
    emit('close')
}

const toastHandle = ref()
const addName = ref('')
const addDescription = ref(null)
const newStatus = ref({})
const confirmHandle = async () => {
    const statusToCreate = {
        name: addName.value ? addName.value.trim() : null,
        description: addDescription.value ? addDescription.value.trim() : null,
    }

    const response = await postData(import.meta.env.VITE_STATUS_URL, statusToCreate)
    if (response.ok) {
    const createdStatus = await response.json() // Assuming the response contains the created status
    newStatus.value = {
        id: createdStatus.id, // Extract ID from the response
        name: createdStatus.name,
        description: createdStatus.description,
    }
    emit('confirmAddStatus', newStatus.value)
    toastHandle.value = { type: 'success', status: true, message: `Success` }
    emit('toastItem', toastHandle.value)
    emit('close') // Close modal after adding status
    } else {
        toastHandle.value = { type: 'error', status: true, message: `Failed to add status` }
        emit('toastItem', toastHandle.value)
        emit('close') // Close modal after adding status
    }

    // Clear form when open again
    addName.value = ''
    addDescription.value = ''
}

</script>

<template>
    <div class="fixed z-10 inset-0 overflow-y-auto">
        <div class="flex items-center justify-center h-screen">
            <div class="fixed inset-0 bg-gray-500 bg-opacity-75 backdrop-blur"></div>
            <div class="itbkk-modal-status relative bg-white rounded-lg shadow-xl w-[70%]">
                <div class="flex flex-col p-5">
                    <form class="space-y-5" novalidate>
                        <h2 class="text-2xl font-bold mb-4">Add Status</h2>
                        <hr>
                            <div class="flex flex-col">
                                <p class="font-semibold">Name</p>
                                <input v-model="addName" class="itbkk-status-name border border-black rounded p-2 peer invalid:border-red-500 focus:outline-none" type="text" required>
                                <p class="hidden peer-invalid:block text-red-600 text-sm">
                                    This field required
                                </p>
                            </div>
                            <div class="flex flex-col">
                                <p class="font-semibold">Description</p>
                                <input v-model="addDescription" class="itbkk-status-description border border-black rounded p-2 focus:outline-none" type="text">
                            </div>
                    </form>
                        <div class="mt-5 space-x-5">
                            <button @click="confirmHandle" :disabled="!addName" class="itbkk-button-confirm disabled bg-green-500 duration-200 hover:bg-green-600 text-white font-semibold px-4 py-2 rounded disabled:bg-green-500 disabled:cursor-not-allowed disabled:opacity-50">
                                Save
                            </button>
                            <button @click="closeHandle" class="itbkk-button-cancel disabled bg-red-500 duration-200 hover:bg-red-600 text-white font-semibold px-4 py-2 rounded">
                                Cancel
                            </button>
                        </div>
                </div>
            </div>
        </div>
    </div>
</template>