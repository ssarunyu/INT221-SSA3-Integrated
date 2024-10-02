<script setup>
import { getData } from '@/lib/fetchMethod';
import { useRoute } from 'vue-router';
import router from '@/router';
import { ref, onMounted } from 'vue'

// Components
import DeleteStatusPopup from '@/components/DeleteStatusPopup.vue';
import TransferDeleteStatusPopup from '@/components/TransferDeleteStatusPopup.vue';

// Payload
const userAuthItem = JSON.parse(localStorage.getItem('payload'))

const route = useRoute()

const statusInBoard = ref({})
const fetch = async () => {

    const response = await getData(`${import.meta.env.VITE_BASE_URL}/v3/boards/${route.params.boardId}/statuses`, false);

    if(response != null) {
        statusInBoard.value = response
        if(statusInBoard.value) {
            if(statusInBoard.value[0].name === 'NO STATUS') {
                statusInBoard.value[0].name = 'No Status'
            }
            if(statusInBoard.value[1].name === 'TODO') {
                statusInBoard.value[1].name = 'To Do'
            }
            if(statusInBoard.value[2].name === 'DOING') {
                statusInBoard.value[2].name = 'Doing'
            }
            if(statusInBoard.value[3].name === 'DONE') {
                statusInBoard.value[3].name = 'Done'
            }
        }
    } else {
        console.log('error')
        router.push({ name: 'Login' });
        return;
    }
};

// Delete
const statuses = ref(statusInBoard.value);
const normalDeleteStatusShow = ref(false);
const transferDeleteStatusShow = ref(false);
const deleteTarget = ref();

const controlToast = (newToast) => {
  toastHandle.value = newToast;
};
const sendDeleteStatus = async (obj) => {
  // Get all task already use that status
  const getAllTask = await getData(`${import.meta.env.VITE_BASE_URL}/v3/boards/${route.params.boardId}/tasks`);
  const filterRepeatTask = getAllTask.filter((a) => a.status.id === obj.id);
  if (filterRepeatTask.length > 0) {
    transferDeleteStatusShow.value = true;
    // NOTE: Transfer
    deleteTarget.value = obj;
  } else {
    // NOTE: Normal delete
    normalDeleteStatusShow.value = true;
    deleteTarget.value = obj;
  }
};

// Update FE when status deleted
const controlDelete = async (statusId) => {
    const findStatusIndex = statusInBoard.value.findIndex(a => a.id === statusId.id)
    statusInBoard.value.splice(findStatusIndex, 1)
};

onMounted(() => {
    fetch()
})

// User permission
const isOwner = ref(route.meta.isOwner)
</script>
<template>
    <router-view></router-view>
    <DeleteStatusPopup
        v-if="normalDeleteStatusShow"
        :deleteItem="deleteTarget"
        @confirmDeleteStatus="controlDelete"
        @close="normalDeleteStatusShow = false"
        @toastItem="controlToast"
    />
    <TransferDeleteStatusPopup
        v-if="transferDeleteStatusShow"
        :deleteItem="deleteTarget"
        @confirmDeleteStatus="controlDelete"
        @close="transferDeleteStatusShow = false"
        @toastItem="controlToast"
    />
    <div class="w-full min-h-screen">
        <!-- Nav -->
        <div class="flex items-center justify-between p-5 bg-slate-800">
            <h1 class="text-2xl font-bold text-white">ITBKK SSA3 Taskboard</h1>
            <!-- User Info -->
            <div v-if="userAuthItem" class="text-right text-white">
                <h1 class="font-semibold">{{ userAuthItem.name }}</h1>
                <h1 class="text-xs">{{ userAuthItem.email }}</h1>
            </div>
        </div>

        <div class="flex flex-col p-5 space-y-4">
            <div class="flex justify-end">
                <div class="px-5 py-3 rounded cursor-pointer bg-slate-300" @click="router.push({name: 'Home'})">Main Page</div>
            </div>
            <!-- Head of table -->
            <div class="flex items-center justify-between w-full p-3 font-bold text-white bg-slate-600">
                <p>Title</p>
                <div class="flex items-center space-x-1">
                    <p>Status</p>
                </div>
            </div>
            <!-- Add Status -->
            <button @click="router.push({ name: 'AddStatus' })"
                    class="p-5 duration-300 rounded-md cursor-pointer itbkk-button-add disabled:cursor-not-allowed bg-slate-200 text-slate-500 hover:bg-slate-300 hover:text-slate-700"
                    :disabled="!isOwner">
                    + Add New Status
            </button>
            <!-- Card below head of table -->
            <div v-for="status in statusInBoard" class="flex items-center justify-between w-full p-3 bg-white border rounded-lg shadow-md itbkk-item">
                <div class="flex flex-col">
                    <button :disabled="!isOwner" class="itbkk-button-edit disabled:cursor-not-allowed" @click="router.push({ name: 'EditStatusPopup', params: { editStatusId: status.id }})">Edit</button>
                    <button :disabled="!isOwner" class="itbkk-button-delete disabled:cursor-not-allowed" @click="sendDeleteStatus(status)">Delete</button>
                </div>
                <p class="px-5 text-lg font-bold rounded itbkk-status-name" :style="{ backgroundColor: status.color }">{{ status.name }}</p>
                <p>{{ status.description }}</p>
            </div>
        </div>
    </div>

</template>
