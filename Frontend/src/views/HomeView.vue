<script setup>
import { ref, onMounted } from 'vue';
import { getData, getDataById } from '@/lib/fetchMethod';
import { TaskManagement } from '@/lib/TaskManagement.js'
import Toast from '@/components/Toast.vue'
import EditPopup from '@/components/EditPopup.vue'
import DeletePopup from '@/components/DeletePopup.vue'
import router from '@/router';

const taskManagement = ref(new TaskManagement())

const fetch = async() => {
    // Fetch data
    const result = await getData(import.meta.env.VITE_URL)
    // Front-end
    taskManagement.value.addAllTask(result)
    const items = taskManagement.value.getAllTask()
    items.forEach(item => {
        if(item.status === 'TO_DO') {
            item.status = 'To Do'
        }
        if(item.status === 'NO_STATUS') {
            item.status = 'No Status'
        }
        if(item.status === 'DOING') {
            item.status = 'Doing'
        }
        if(item.status === 'DONE') {
            item.status = 'Done'
        }
    })
}

onMounted(async () => {
    await fetch()
})

const styleStatus = (name) => {
    if(name === 'No Status') {
        return 'bg-gray-300'
    }
    if(name === 'To Do') {
        return 'bg-amber-300'
    }
    if(name === 'Doing') {
        return 'bg-blue-300'
    }
    if(name === 'Done') {
        return 'bg-green-300'
    }
}

// Edit
const options = {
  timeZoneName: "short",
  hour12: false,
};
const targetItem = ref()
const editPopupStatus = ref(false)
const openEditPopup = async (id) => {
    editPopupStatus.value = true
    const result = await getDataById(import.meta.env.VITE_URL, id)
    result.createdOn = new Date(result.createdOn).toLocaleString('en-AU', options)
    result.updatedOn = new Date(result.updatedOn).toLocaleString('en-AU', options)
    targetItem.value = result
    // Change path
    router.push({ name: 'EditPopup', params: { id: id }})
}
const closeEditPopup = () => {
    editPopupStatus.value = false
    router.push('/task')
}
// const updateEdit = async () => {
//     // const result = await postData(import.meta.env.VITE_URL)
//     // return result.status
// }

// Add


// Delete
const deletePopupStatus = ref(false)
const deleteTarget = ref()
const openDeletePopup = async (id) => {
    deletePopupStatus.value = true
    const result = await getDataById(import.meta.env.VITE_URL, id)
    result.createdOn = new Date(result.createdOn).toLocaleString('en-AU', options)
    result.updatedOn = new Date(result.updatedOn).toLocaleString('en-AU', options)
    deleteTarget.value = result
}
const deleteConfirm = async () => {
    // Back-end
    const deleteBack = await deleteData(import.meta.env.VITE_URL, deleteTarget.id)
    // Front-end
    taskManagement.deleteTask(delId)
}
</script>

<template>
    <DeletePopup v-show="deletePopupStatus" v-if="deleteTarget":deleteItem="deleteTarget" @close="deletePopupStatus = false" @confirm="deleteConfirm()"/>
    <EditPopup v-show="editPopupStatus" v-if="targetItem" :itemData="targetItem" @close="closeEditPopup()"/>
    <div class="w-full min-h-screen p-5">
        <h1 class="flex text-2xl font-bold justify-center mb-5">ITBKK SSA3 Taskboard</h1>
        <div class="flex flex-col space-y-3">
            <div class="flex w-full justify-between font-xl font-bold text-white border-b border-gray-300 p-3 bg-blue-400 rounded">
                <p>Title</p>
                <p>Assignees</p>
                <p>Status</p>
            </div>
            <div class="itbkk-button-add text-center w-20 p-2 bg-green-300 rounded cursor-pointer duration-300 hover:bg-green-400 hover:scale-105">
                + Add
            </div>
            <div v-for="item in taskManagement.getAllTask()" class="itbkk-item relative flex items-center justify-between w-full p-3 rounded">
                <div class="absolute left-0 w-1 h-10" :class="styleStatus(item.status)"></div>
                <div class="flex items-center space-x-3">
                    <div>
                        <p class="cursor-pointer" @click="openEditPopup(item.id)">Edit</p>
                        <hr>
                        <p class="cursor-pointer" @click="openDeletePopup(item.id)">Delete</p>
                    </div>
                    <p class="text-xl font-bold">
                        {{ item.id }}
                    </p>
                    <div class="w-full">
                        <router-link class="itbkk-title break-all font-bold text-xl duration-200 hover:text-blue-500" :to="{ name: 'TaskDetail', params: { taskId: item.id } }">
                            {{ item.title }}
                        </router-link>
                        <p class="itbkk-assignees" :class="item.assignees === null ? 'italic text-gray-500' : ''">
                            Assignees : {{ item.assignees === null ? 'Unassigned' : item.assignees }}
                        </p>
                    </div>
                </div>
                <div>
                    <p class="itbkk-status px-4 py-2 rounded" :class="styleStatus(item.status)">{{ item.status }}</p>
                </div>
            </div>
        </div>
    </div>
</template>
