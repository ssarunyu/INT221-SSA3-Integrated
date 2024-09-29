<script setup>
import { ref, watch, onMounted, computed } from 'vue';
import { getData, getDataById, deleteData, postData, updateData } from '@/lib/fetchMethod.js';
import router from '@/router';
import { useRoute } from 'vue-router';
import Toast from '@/components/Toast.vue'
import { styleStatus } from '@/lib/styleStatus';
import DeletePopup from '@/components/DeletePopup.vue';


const route = useRoute()

const userAuthItem = JSON.parse(localStorage.getItem('payload'))

const allTasks = ref([])
const allStatuses = ref([])
const fetch = async() => {
    // Fetch task
    const allTask = await getData(`${import.meta.env.VITE_BASE_URL}/v3/boards/${route.params.boardId}/tasks`)
    if(allTask !== undefined) {
        allTasks.value.push(...allTask)
    }

    // Fetch status
    const allStatus = await getData(`${import.meta.env.VITE_BASE_URL}/v3/boards/${route.params.boardId}/statuses`)
    if(allStatus !== undefined) {
        allStatuses.value.push(...allStatus)
    }
}

const items = allTasks.value
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

const tasks = ref(allTasks.value)
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

const allStatusArr = allStatuses.value
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
    const response = await getData(`${import.meta.env.VITE_BASE_URL}/v3/boards/tasks?${userFilter}`)
    tasks.value = response
}

const clearFilter = async () => {
    filterSelect.value = []
    const response = await getData(`${import.meta.env.VITE_BASE_URL}/v3/boards/tasks`)
    tasks.value = response
}

const removeFilter = async (r) => {
    const findExist = filterSelect.value.indexOf(r)
    if (findExist !== -1) {
        filterSelect.value.splice(findExist, 1)
        await updateTasks()
    }
}

// Delete
const deletePopupStatus = ref(false)
const deleteTarget = ref()
const openDeletePopup = async (id) => {
    deletePopupStatus.value = true
    const result = await getData(`${import.meta.env.VITE_BASE_URL}/v3/boards/${route.params.boardId}/tasks/${id}`)
    deleteTarget.value = result
}
const deleteConfirm = async () => {
    // Back-end
    const response = await deleteData(`${import.meta.env.VITE_BASE_URL}/v3/boards/${route.params.boardId}/tasks`, deleteTarget.value.id)
    // Check Status
    if(response.ok) {
        toastHandle.value = {type: 'success', status: true, message: `The task has been deleted`}
        // Front-end
        const indexToDelete = allTasks.value.findIndex(a => a.id === deleteTarget.value.id)
        allTasks.value.splice(indexToDelete, 1)
        deletePopupStatus.value = false
    } else {
        toastHandle.value = {type: 'error', status: true, message: `Task doesn't exist`}
        deletePopupStatus.value = false
    }
}
</script>

<template>
    <router-view></router-view>
    <DeletePopup v-show="deletePopupStatus" v-if="deleteTarget" :deleteItem="deleteTarget" @close="deletePopupStatus = false" @confirm="deleteConfirm()"/>
    <div class="w-full min-h-screen">
        <!-- Nav -->
        <div class="flex justify-between items-center bg-slate-800 p-5">
            <h1 class="font-bold text-2xl text-white">ITBKK SSA3 Taskboard</h1>
            <!-- NOTE: Need fix -->
            <div v-if="userAuthItem" class="text-right text-white">
                <h1 class="font-semibold">{{ userAuthItem.name }}</h1>
                <h1 class="text-xs">{{ userAuthItem.email }}</h1>
            </div>
        </div>
        <div class="flex flex-col">
            <!-- Status Page button -->
            <div class="flex justify-end">
                <div class="itbkk-manage-status cursor-pointer" @click="router.push({name: 'StatusView'})">Status Page</div>
            </div>
            <!-- Head of table -->
            <div class="flex w-full items-center justify-between font-xl font-bold text-white p-3 bg-slate-600">
                <p>Title</p>
                <p>Assignees</p>
                <div class="flex items-center space-x-1">
                    <p>Status</p>
                    <div @click="changeSortStage()" class="p-2 cursor-pointer transition duration-300">
                        <font-awesome-icon class="itbkk-status-sort" v-if="sortIcon" :icon="sortIcon" />
                    </div>
                </div>
            </div>
            <!-- Filter -->
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
            <div class="w-full flex flex-col justify-center items-center space-y-5">
                <div @click="router.push({ name: 'AddTask' })"
                class="itbkk-button-add w-2/3 rounded-md p-5 bg-slate-200 text-slate-500 cursor-pointer duration-300 hover:bg-slate-300 hover:text-slate-700">
                + Add New Task
            </div>
            <Toast :toastObject="toastHandle" @close="toastHandle.status = false"/>
            <div v-for="item in sortTask" :key="item.id" class="itbkk-item relative flex items-center justify-between w-full p-3 rounded border">
                <div class="absolute left-0 w-1 h-10" :class="`bg-[${item.status.statusColor}]`"></div>
                <div class="flex items-center space-x-3">
                    <div>
                        <div class="dropdown itbkk-button-action">
                            <p tabindex="0" role="button" class=" btn m-1 border-none font-bold text-2xl">
                                :
                            </p>
                            <ul tabindex="0" class="dropdown-content z-[1] menu p-2 shadow bg-white rounded-box w-40">
                                <!-- NOTE: Edit must send params -->
                                <li>
                                    <a class="itbkk-button-edit"
                                    @click="router.push({name: 'EditTask', params: { taskId: item.id }})">
                                    Edit
                                </a>
                            </li>
                            <li>
                                        <a class="itbkk-button-delete"
                                        @click="openDeletePopup(item.id)">
                                        Delete
                                        </a>
                                    </li>
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
                        <p class="itbkk-status px-4 py-2 rounded" :style="{ backgroundColor: item.status.statusColor }">{{ item.status.name }}</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>