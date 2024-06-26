<script setup>
import { ref, onMounted } from 'vue'
import { getData, getDataById, deleteData, transferData } from '@/lib/fetchMethod'

const toastHandle = ref()
const props = defineProps({
    deleteItem: Object
})

const emit = defineEmits(['confirmDeleteStatus', 'close', 'toastItem'])
const closeHandle = () => {
    emit('close')
}

const repeatItem = ref()
const allStatusExist = ref()
const userTransferSelect = ref()
onMounted(async () => {
    // Get all status that can select
    const getAllStatus = await getData(import.meta.env.VITE_STATUS_URL)
    const filterStatus = getAllStatus.filter((a) => a.id !== props.deleteItem.id)
    allStatusExist.value = filterStatus

    // Get all task already use that status
    // NOTE: should go to transfer
    const getAllTask = await getData(import.meta.env.VITE_TASK_URL)
    const filterRepeatTask = getAllTask.filter((a) => a.status.id === props.deleteItem.id)
    repeatItem.value = filterRepeatTask
})

const confirmHandle = async () => {
    const response = await transferData(import.meta.env.VITE_STATUS_URL, props.deleteItem.id, userTransferSelect.value)
    if(response.ok) {
        emit('confirmDeleteStatus', props.deleteItem)
        toastHandle.value = { type: 'success', status: true, message: `${repeatItem.value.length} task have been transferred and the status has been deleted` }
        emit('toastItem', toastHandle.value)
        emit('close')
    } else {
        toastHandle.value = { type: 'error', status: true, message: `An error has occurred, the status does not exist` }
        emit('toastItem', toastHandle.value)
        emit('close')
    }
}
</script>

<template>
    <!-- NOTE: Transfer delete -->
    <div v-if="deleteItem && repeatItem" class="fixed inset-0 flex items-center justify-center z-50 bg-gray-500 bg-opacity-75 backdrop-blur confirm-dialog ">
    <div class="relative px-4 min-h-screen md:flex md:items-center md:justify-center">
        <div class=" opacity-25 w-full h-full absolute z-10 inset-0"></div>
        <div class="bg-white rounded-lg md:max-w-md md:mx-auto p-4 fixed inset-x-0 bottom-0 z-50 mb-4 mx-4 md:relative shadow-lg">
            <div class="md:flex items-center">
                <div class="rounded-full border border-gray-300 flex items-center justify-center w-16 h-16 flex-shrink-0 mx-auto">
                <i class="bx bx-error text-3xl">
                &#33;
                </i>
                </div>
                <div class="mt-4 md:mt-0 md:ml-6 text-center md:text-left">
                <p class="font-bold">Transfer task status</p>
                <p>The status you delete already use in some tasks please transfer before delete this status</p>
                <div class="my-3 text-sm text-gray-700 break-all">
                    <ul>
                        <p><strong>{{ repeatItem.length }} Task</strong> already use this status</p>
                        <li v-for="r in repeatItem"><strong>ID: {{r.id}} – {{ r.title }}</strong></li>
                    </ul>
                </div>
                <div>
                    <p>Transfer to</p>
                    <select v-model="userTransferSelect" class="rounded px-3 py-1 border border-gray-300">
                        <option v-for="s in allStatusExist" :value="s.id">{{ s.name }}</option>
                    </select>
                </div>
                </div>
            </div>
            <div class="itbkk-button-action text-center md:text-right mt-4 md:flex md:justify-end">
                <button @click="confirmHandle" id="confirm-delete-btn" class="itbkk-button-confirm block w-full md:inline-block md:w-auto px-4 py-3 md:py-2 bg-red-200 text-red-700 rounded-lg font-semibold text-sm md:ml-2 md:order-2">
                    Confirm
                </button>
                <button @click="closeHandle" id="confirm-cancel-btn" class="itbkk-button-cancel block w-full md:inline-block md:w-auto px-4 py-3 md:py-2 bg-gray-200 rounded-lg font-semibold text-sm mt-4 md:mt-0 md:order-1">
                    Cancel
                </button>
            </div>
        </div>
    </div>
</div>
</template>