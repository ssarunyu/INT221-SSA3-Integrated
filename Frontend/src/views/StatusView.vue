<script setup>
import { styleStatus } from '@/lib/styleStatus';
import AddStatusPopup from '@/components/AddStatusPopup.vue';
import { ref } from 'vue'
import Toast from '@/components/Toast.vue'

const status = [
  { id: 1, name: 'No Status', description: `Task not yet assigned any status. Typically in initial stages or pending initiation. Tracking or assigning a status in the future might help progress this task.`},
  { id: 2, name: 'To Do', description: `Task assigned to be done but not yet started. Often used to indicate tasks to be done in the next steps of the process.`},
  { id: 3, name: 'Doing', description: `Task currently in progress. May require adjustments or checks to ensure it's progressing in the right direction.`  },
  { id: 4, name: 'Done', description: `Task completed with no errors or incompleteness. Tasks in this status often have usable or deployable outcomes/products. Changing the status to Done verifies that the task has been carried out and completed as desired.`}
];

// Toast
const toastHandle = ref()

//Add Status
const addStatusPopup = ref(false)
const openAddStatusPopup = () => {
    addStatusPopup.value = true
}
const closeAddStatusPopup = () => {
    addStatusPopup.value = false
    router.push('/task/statuses')
}

const confirmAddStatus = async (newStatus) => {
    // Back-end
    const response = await postData(import.meta.env.VITE_URL, newStatus)
    if(response.ok) {
        // Add Toast
        toastHandle.value = {type: 'success', status: true, message: `${newStatus.name} Status added successfully!`}
        // Front-end
        taskManagement.value.addStatus(await response.json())
        addPopupStatus.value = false
    }
}

//Edit Status

</script>

<template>
    <AddStatusPopup v-show="addStatusPopup" @close="closeAddStatusPopup()" @confirm="deleteConfirm()"/>
    <div class="w-full min-h-screen p-5">
        <h1 class="flex text-2xl font-bold justify-center mb-5">ITBKK SSA3 Taskboard</h1>
        <div class="flex w-full justify-between">
            <div class="flex p-3">
                <router-link :to="{ name: 'Home' }" class="itbkk-button-home hover:text-blue-500 hover:scale-105 ml-1">Home</router-link>
                <p class="font-bold text-gray-400 ml-1">></p>
                <router-link :to="{ name: 'StatusView' }" class="font-bold ml-1 hover:scale-105">Task Status</router-link>
            </div>
            <div @click="openAddStatusPopup()" class="itbkk-button-add p-2 w-35 m-1 rounded cursor-pointer duration-300 bg-gray-300 hover:bg-gray-400 hover:scale-105">
                + Add Status
            </div>
        </div>
        <div class="flex w-full justify-between font-xl font-bold text-white border-b border-gray-300 p-3 bg-blue-400 rounded">
                <p>ID</p>
                <p>Name</p>
                <p>Description</p>
                <p>Action</p>
        </div>
        <Toast :toastObject="toastHandle" @close="toastHandle.status = false"/>
        <div v-for="item in status" class="itbkk-item relative p-3 rounded">
            <div class="grid grid-cols-10 items-center">
                <div class="absolute left-0 w-1 h-10 col-span-1" :class="styleStatus(item.name)"></div>
                        <div class="text-lg font-bold col-span-1 text-center">{{ item.id }}</div>
                        <div class="itbkk-status-name font-medium text-lg col-span-1 px-4 mx-4 py-2 rounded" :class=styleStatus(item.name)>{{ item.name }}</div>
                        <div class="itbkk-status-description text-lg col-span-7 m-2">{{ item.description }}</div>
            <div class="itbkk-status-action flex col-span-1 " v-if="item.name !== 'No Status'">
                <div @click="editStatus()" class="itbkk-button-edit p-2 px-5 w-35 m-1 rounded cursor-pointer duration-300 bg-gray-300 hover:bg-gray-400 hover:scale-105">
                    Edit
                </div>
                <div @click="deleteStatus()" class="itbkk-button-delete p-2 px-3 w-35 m-1 rounded cursor-pointer duration-300 bg-gray-300 hover:bg-gray-400 hover:scale-105">
                    Delete
                </div>
            </div>
        </div>
    </div>
    </div>
</template>