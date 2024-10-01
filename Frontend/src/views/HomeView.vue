<script setup>
import { ref, watch, onMounted, computed } from 'vue';
import { getData, getDataById, deleteData, postData, updateData, patchVisi } from '@/lib/fetchMethod.js';
import router from '@/router';
import { useRoute } from 'vue-router';
import Toast from '@/components/Toast.vue'
import { styleStatus } from '@/lib/styleStatus';

// Components
import DeletePopup from '@/components/DeletePopup.vue';
import VisibilityBoardPopup from '@/components/VisibilityBoardPopup.vue';


const route = useRoute()

const userAuthItem = JSON.parse(localStorage.getItem('payload'))

const allTasks = ref([])
const allStatuses = ref([])
const boardDetail = ref()
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

    // Fetch that board detail
    const responseBoardDetail = await getData(`${import.meta.env.VITE_BASE_URL}/v3/boards/${route.params.boardId}`)
    boardDetail.value = responseBoardDetail

    
}

watch(allTasks.value, (newItems) => {
    newItems.forEach(item => {
        switch(item.status.name) {
            case 'TODO':
                item.status.name = 'To Do';
                break;
            case 'NO STATUS':
                item.status.name = 'No Status';
                break;
            case 'DOING':
                item.status.name = 'Doing';
                break;
            case 'DONE':
                item.status.name = 'Done';
                break;
            default:
                break;
        }
    })
});

// Set the initial value for isPrivate base on boardDetail bisibility after fetch
const isPrivate = ref()
onMounted(async () => {
    await fetch()
    console.log(boardDetail.value)
    isPrivate.value = boardDetail.value.visibility === 'PRIVATE' ? true : false
})

// Toast
const toastHandle = ref()

// Edit
const options = {
    timeZoneName: "short",
    hour12: false,
}

// const tasks = ref(allTasks.value)
// const sortStage = ref(0);
// const changeSortStage = () => {
//     sortStage.value = (sortStage.value + 1) % 3;
// };

// const sortTask = computed(() => {
//     const copyTask = [...tasks.value];

//     const sortFunctions = {
//         0: tasks => tasks,
//         1: tasks => tasks.sort((a, b) => a.status.name.localeCompare(b.status.name)),
//         2: tasks => tasks.sort((a, b) => b.status.name.localeCompare(a.status.name))
//     };

//     return sortFunctions[sortStage.value](copyTask);
// });

// const sortIcon = computed(() => {
//     const icons = {
//         0: 'fa-solid fa-sort',
//         1: 'fa-solid fa-sort-down',
//         2: 'fa-solid fa-sort-up'
//     };

//     return icons[sortStage.value];
// });

// const allStatusArr = allStatuses.value
// const filterSelect = ref([])
// const submitFilter = async (userClick) => {
//     const findExist = filterSelect.value.indexOf(userClick)
//     if (findExist !== -1) {
//         filterSelect.value.splice(findExist, 1)
//     } else {
//         filterSelect.value.push(userClick)
//     }
//     await updateTasks()
// }

// const updateTasks = async () => {
//     let userFilter = ''
//     if (filterSelect.value.length) {
//         userFilter = filterSelect.value.map(a => `filterStatuses=${a}`).join('&')
//     }
//     const response = await getData(`${import.meta.env.VITE_BASE_URL}/v3/boards/tasks?${userFilter}`)
//     tasks.value = response
// }

// const clearFilter = async () => {
//     filterSelect.value = []
//     const response = await getData(`${import.meta.env.VITE_BASE_URL}/v3/boards/tasks`)
//     tasks.value = response
// }

// const removeFilter = async (r) => {
//     const findExist = filterSelect.value.indexOf(r)
//     if (findExist !== -1) {
//         filterSelect.value.splice(findExist, 1)
//         await updateTasks()
//     }
// }

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

// Visibility and modal
// NOTE: True = private, False = public
const showVisi = ref(false)
// Open popup
const changeVisi = () => {
    showVisi.value = true
}
// Handle confirm
const confirmVisibilityChange = async (confirmation) => {
    console.log('confirm', confirmation)
    isPrivate.value = confirmation
    try {
        // Should couter with base because it's change opposite
        const response = await patchVisi(import.meta.env.VITE_BASE_URL, route.params.boardId, confirmation ? 'PRIVATE' : 'PUBLIC')
        if(response.status === 500) {
            window.alert('There is a problem. Please try again later.')
        }
        if(response.status === 403) {
            window.alert('you do not have permission')
        }
        showVisi.value = false
    } catch (error) {
        console.error(error)
    }
}
// Handle cancel
const cancelVisibilityChange = () => {
    showVisi.value = false
}
</script>

<template>
    <VisibilityBoardPopup 
        v-if="showVisi" 
        :isPrivate="isPrivate" 
        @confirm="confirmVisibilityChange" 
        @cancel="cancelVisibilityChange" 
    />
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
        <div class="flex flex-col space-y-4 p-5">
            <!-- Filter -->
            <!-- <div class="flex">
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
                <div class="flex items-center space-x-3 ml-4">
                    <div v-for="status in filterSelect" class="px-5 py-2 rounded bg-blue-200">
                        {{ status }}
                        <font-awesome-icon class="cursor-pointer" @click="removeFilter(status)" icon="fa-solid fa-xmark" />
                    </div>
                </div>
            </div> -->
            <!-- Status Page button -->
            <div class="flex justify-end">
                <div class="itbkk-manage-status cursor-pointer px-5 py-3 bg-slate-300 rounded" @click="router.push({name: 'StatusView'})">Status Page</div>
            </div>
            <div v-if="boardDetail">
                <!-- Toggle visibility button -->
                <div class="relative flex">
                    <input type="checkbox" class="absolute opacity-0" />
                    <div
                        class="itbkk-board-visibility w-12 h-6 bg-gray-300 rounded-full cursor-pointer transition duration-200"
                        :class="{'bg-green-500': !isPrivate}"
                        @click="changeVisi"
                    >
                        <div class="w-6 h-6 bg-white rounded-full shadow-md transform transition duration-200" :style="isPrivate ? 'transform: translateX(0)' : 'transform: translateX(100%);'"></div>
                    </div>
                    <p class="ml-3">{{ isPrivate ? 'Private' : 'Public' }} Board</p>
                </div>
                <p class="text-center text-2xl font-bold">{{ boardDetail.boardName }}</p>
            </div>
            <!-- Head of table -->
            <div class="flex w-full items-center justify-between font-xl font-bold text-white p-3 bg-slate-600">
                <p>Title</p>
                <p>Assignees</p>
                <div class="flex items-center space-x-1">
                    <p>Status</p>
                    <!-- <div @click="changeSortStage()" class="p-2 cursor-pointer transition duration-300"> -->
                        <!-- <font-awesome-icon class="itbkk-status-sort" v-if="sortIcon" :icon="sortIcon" /> -->
                    <!-- </div> -->
                </div>
            </div>
            <!-- Add task button -->
            <div class="w-full flex flex-col justify-center items-center space-y-5">
                <div @click="router.push({ name: 'AddTask' })"
                class="itbkk-button-add w-full rounded-md p-5 bg-slate-200 text-slate-500 cursor-pointer duration-300 hover:bg-slate-300 hover:text-slate-700">
                    + Add New Task
                </div>
            <Toast :toastObject="toastHandle" @close="toastHandle.status = false"/>
            <!-- Each element -->
            <div v-for="item in allTasks" :key="item.id" class="itbkk-item relative flex items-center justify-between w-full p-3 rounded border">
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
                            <p @click="router.push({ name: 'TaskDetail', params: { detailId: item.id }})" class="itbkk-title break-all font-bold text-xl duration-200 cursor-pointer hover:text-gray-700">
                                {{ item.title }}
                            </p>
                            <p class="itbkk-assignees" :class="item.assignees === null ? 'italic text-gray-500' : ''">
                                Assignees : {{ item.assignees === null ? 'Unassigned' : item.assignees }}
                            </p>
                        </div>
                    </div>
                    <div>
                        <p class="itbkk-status px-4 py-2 rounded shadow-md" :style="{ backgroundColor: item.status.statusColor }">
                            {{ item.status.name }}
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>