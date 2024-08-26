<script setup>
import { ref, watch, onMounted, computed } from 'vue';
import { getData, getDataById, deleteData, postData, updateData } from '@/lib/fetchMethod.js';
import router from '@/router';
import Toast from '@/components/Toast.vue'
import AddPopup from '@/components/AddPopup.vue';
import EditPopup from '@/components/EditPopup.vue'
import DeletePopup from '@/components/DeletePopup.vue'
import { styleStatus } from '@/lib/styleStatus';

// Store
import { useTaskStore } from '@/stores/TaskStore.js'
import { useStatusStore } from '@/stores/StatusStore.js'
// NOTE: Will change to use store
const userAuthItem = JSON.parse(localStorage.getItem('payload'))


const taskManagement = useTaskStore()
const statusManagement = useStatusStore()

const fetch = async() => {
    // Fetch task
    const allTask = await getData(import.meta.env.VITE_TASK_URL)
    taskManagement.addAllTask(allTask)

    // Fetch status
    const allStatus = await getData(import.meta.env.VITE_STATUS_URL)
    statusManagement.addAllStatus(allStatus)
}

const items = taskManagement.getAllTask()
watch(items, (newItems) => {
    newItems.forEach(item => {
        switch(item.status) {
            case 'TO_DO':
                item.status = 'To Do'
                break;
            case 'NO_STATUS':
                item.status = 'No Status'
                break;
            case 'DOING':
                item.status = 'Doing'
                break;
            case 'DONE':
                item.status = 'Done'
                break;
            default:
                break;
        }
    })
})

onMounted(async () => {
    await fetch()
})

// Toast
const toastHandle = ref()

// Edit
const options = {
    timeZoneName: "short",
    hour12: false,
}
const editTaskTarget = ref()
const editPopupStatus = ref(false)
const openEditPopup = async (componentId) => {
    const result = await getDataById(import.meta.env.VITE_TASK_URL, componentId)
    if(result) {
        editPopupStatus.value = true
        router.push({ name: 'EditPopup', params: { editId: componentId }})
        result.createdOn = new Date(result.createdOn).toLocaleString('en-AU', options)
        result.updatedOn = new Date(result.updatedOn).toLocaleString('en-AU', options)
        editTaskTarget.value = result
    }
}
const closeEditPopup = () => {
    editTaskTarget.value = ''
    editPopupStatus.value = false
    router.push({ name: 'Home' })
}
const updateEdit = async (newEdit) => {
    const response = await updateData(import.meta.env.VITE_TASK_URL,
    {
        title: newEdit.title,
        description: newEdit.description,
        assignees: newEdit.assignees,
        status: newEdit.status.id
    }, newEdit.id)
    if(response.ok) {
        // Toast
        toastHandle.value = {type: 'success', status: true, message: `Task successfully edited to ${newEdit.title} !`}
        // Front-end
        taskManagement.updateTask(newEdit, newEdit.id)
        editPopupStatus.value = false
        router.push({ name: 'Home' })
    }
}

// Add
const addPopupStatus = ref(false)
const openAddPopup = () => {
    addPopupStatus.value = true
}
const closeAddPopup = () => {
    addPopupStatus.value = false
}
const confirmAdd = async (newTask) => {
    // Back-end
    const response = await postData(import.meta.env.VITE_TASK_URL, newTask)
    if(response.ok) {
        // Add Toast
        toastHandle.value = {type: 'success', status: true, message: `${newTask.title} task added successfully!`}
        // Front-end
        taskManagement.addTask(await response.json())
        addPopupStatus.value = false
    }
}

// Delete
const deletePopupStatus = ref(false)
const deleteTarget = ref()
const openDeletePopup = async (id) => {
    deletePopupStatus.value = true
    const result = await getDataById(import.meta.env.VITE_TASK_URL, id)
    deleteTarget.value = result
}
const deleteConfirm = async () => {
    // Back-end
    const response = await deleteData(import.meta.env.VITE_TASK_URL, deleteTarget.value.id)
    // Check Status
    if(response.ok) {
        toastHandle.value = {type: 'success', status: true, message: `The task has been deleted`}
        // Front-end
        taskManagement.deleteTask(deleteTarget.value.id)
        deletePopupStatus.value = false
    } else {
        toastHandle.value = {type: 'error', status: true, message: `Task doesn't exist`}
        taskManagement.deleteTask(deleteTarget.value.id)
        deletePopupStatus.value = false
    }
}

const tasks = ref(taskManagement.getAllTask())
const sortStage = ref(0);
const changeSortStage = () => {
    sortStage.value = (sortStage.value + 1) % 3;
};

const sortTask = computed(() => {
    const copyTask = [...tasks.value];

    const sortFunctions = {
        0: tasks => tasks,
        1: tasks => tasks.sort((a, b) => a.status.name.localeCompare(b.status.name)),
        2: tasks => tasks.sort((a, b) => b.status.name.localeCompare(a.status.name))
    };

    return sortFunctions[sortStage.value](copyTask);
});

const sortIcon = computed(() => {
    const icons = {
        0: 'fa-solid fa-sort',
        1: 'fa-solid fa-sort-down',
        2: 'fa-solid fa-sort-up'
    };

    return icons[sortStage.value];
});

const allStatusArr = statusManagement.getAllStatus()
const filterSelect = ref([])
const submitFilter = async (userClick) => {
    const findExist = filterSelect.value.indexOf(userClick)
    if (findExist !== -1) {
        filterSelect.value.splice(findExist, 1)
    } else {
        filterSelect.value.push(userClick)
    }
    await updateTasks()
}

const updateTasks = async () => {
    let userFilter = ''
    if (filterSelect.value.length) {
        userFilter = filterSelect.value.map(a => `filterStatuses=${a}`).join('&')
    }
    const response = await getData(`${import.meta.env.VITE_TASK_URL}?${userFilter}`)
    tasks.value = response
}

const clearFilter = async () => {
    filterSelect.value = []
    const response = await getData(import.meta.env.VITE_TASK_URL)
    tasks.value = response
}

const removeFilter = async (r) => {
    const findExist = filterSelect.value.indexOf(r)
    if (findExist !== -1) {
        filterSelect.value.splice(findExist, 1)
        await updateTasks()
    }
}
</script>

<template>
    <!-- For details -->
    <router-view></router-view>
    <!-- Components -->
    <DeletePopup v-show="deletePopupStatus" v-if="deleteTarget" :deleteItem="deleteTarget" @close="deletePopupStatus = false" @confirm="deleteConfirm()"/>
    <AddPopup v-show="addPopupStatus" @close="closeAddPopup()" @confirm="confirmAdd"/>
    <EditPopup v-show="editPopupStatus" v-if="editTaskTarget" :itemData="editTaskTarget" :statusData="taskManagement.getAllStatus()" @update="updateEdit" @close="closeEditPopup()"/>
    <div class="w-full min-h-screen p-5">
        <h1 class="flex text-2xl font-bold justify-center mb-5">ITBKK SSA3 Taskboard</h1>
        <div class="flex flex-col space-y-3">
            <Toast :toastObject="toastHandle" @close="toastHandle.status = false"/>
            <div class="flex w-full justify-between font-xl ">
                <div @click="openAddPopup()" class="itbkk-button-add w-40 text-center p-2 bg-green-300 rounded cursor-pointer duration-300 hover:bg-green-400 hover:scale-105">
                    + Add New Task
                </div>
                <router-link class="itbkk-manage-status p-2 bg-gray-300 rounded w-[150px] text-center duration-300 hover:bg-gray-400" :to="{ name: 'StatusView' }">Manage Status</router-link>
            </div>
            <div class="text-2xl font-bold">
                <h1>Hi!, {{ userAuthItem.name }}</h1>
            </div>
            <div class="flex w-full items-center justify-between font-xl font-bold text-white border-b border-gray-300 p-3 bg-blue-400 rounded">
                <p>Title</p>
                <p>Assignees</p>
                <div class="flex items-center space-x-1">
                    <p>Status</p>
                    <div @click="changeSortStage()" class="p-2 cursor-pointer transition duration-300">
                        <font-awesome-icon class="itbkk-status-sort" v-if="sortIcon" :icon="sortIcon" />
                    </div>
                </div>
            </div>
            <div class="flex">
                <div class="itbkk-status-filter dropdown dropdown-bottom">
                    <div tabindex="0" role="button" class="btn m-1">Filter Tasks</div>
                    <ul tabindex="0" class="dropdown-content z-[1] menu p-2 shadow bg-white rounded-box w-52">
                    <div class="text-blue-500 underline flex justify-end w-full my-2">
                        <p class="cursor-pointer" @click="clearFilter">Clear all</p>
                    </div>
                    <div v-for="status in allStatusArr" class="flex justify-between p-2 rounded duration-300 hover:bg-gray-100 ">
                        <button class="itbkk-status-choice" @click="submitFilter(status.name)">{{ status.name }}</button> 
                    </div>
                    </ul>
                </div>
                <!-- Show already filter -->
                <div class="flex items-center space-x-3 ml-4">
                    <div v-for="status in filterSelect" class="px-5 py-2 rounded bg-blue-200">
                    {{ status }}
                    <font-awesome-icon class="cursor-pointer" @click="removeFilter(status)" icon="fa-solid fa-xmark" />
                    </div>
                </div>
            </div>
            <div class="space-y-5">
                <div v-for="item in sortTask" :key="item.id" class="itbkk-item relative flex items-center justify-between w-full p-3 rounded border">
                    <div class="absolute left-0 w-1 h-10" :class="styleStatus(item.status.name)"></div>
                    <div class="flex items-center space-x-3">
                        <div>
                            <div class="dropdown itbkk-button-action">
                                <p tabindex="0" role="button" class=" btn m-1 border-none font-bold text-2xl">
                                    :
                                </p>
                                <ul tabindex="0" class="dropdown-content z-[1] menu p-2 shadow bg-white rounded-box w-40">
                                    <li><a class="itbkk-button-edit" @click="openEditPopup(item.id)">Edit</a></li>
                                    <li><a class="itbkk-button-delete" @click="openDeletePopup(item.id)">Delete</a></li>
                                </ul>
                            </div>
                        </div>
                        <p class="text-xl font-bold">
                            {{ item.id }}
                        </p>
                        <div class="w-full">
                            <p @click="router.push({ name: 'TaskDetail', params: { detailId: item.id}})" class="itbkk-title break-all font-bold text-xl duration-200 cursor-pointer hover:text-gray-700">
                                {{ item.title }}
                            </p>
                            <p class="itbkk-assignees" :class="item.assignees === null ? 'italic text-gray-500' : ''">
                                Assignees : {{ item.assignees === null ? 'Unassigned' : item.assignees }}
                            </p>
                        </div>
                    </div>
                    <div>
                        <p class="itbkk-status px-4 py-2 rounded" :class="styleStatus(item.status.name)">{{ item.status.name }}</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
