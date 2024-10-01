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
    const response = await getData(`${import.meta.env.VITE_BASE_URL}/v3/boards/${route.params.boardId}/statuses`)
    if(response) {
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
    }
}

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
        <div class="flex justify-between items-center bg-slate-800 p-5">
            <h1 class="font-bold text-2xl text-white">ITBKK SSA3 Taskboard</h1>
            <!-- User Info -->
            <div class="text-right text-white">
                <h1 class="font-semibold">{{ userAuthItem.name }}</h1>
                <h1 class="text-xs">{{ userAuthItem.email }}</h1>
            </div>
        </div>

        <div class="flex flex-col space-y-4 p-5">
            <div class="flex justify-end">
                <div class="cursor-pointer px-5 py-3 bg-slate-300 rounded" @click="router.push({name: 'Home'})">Main Page</div>
            </div>
            <!-- Head of table -->
            <div class="flex w-full items-center justify-between font-bold text-white p-3 bg-slate-600">
                <p>Title</p>
                <div class="flex items-center space-x-1">
                    <p>Status</p>
                </div>
            </div>
            <!-- Add Status -->
            <div @click="router.push({ name: 'AddStatus' })"
                    class="itbkk-button-add rounded-md p-5 bg-slate-200 text-slate-500 cursor-pointer duration-300 hover:bg-slate-300 hover:text-slate-700">
                    + Add New Status
            </div>
            <!-- Card below head of table -->
            <div v-for="status in statusInBoard" class="itbkk-item w-full flex items-center justify-between p-3 bg-white rounded-lg shadow-md border">
                <div>
                    <p class="itbkk-button-edit" @click="router.push({ name: 'EditStatusPopup', params: { editStatusId: status.id }})">Edit</p>
                    <p class="itbkk-button-delete" @click="sendDeleteStatus(status)">Delete</p>
                </div>
                <p class="itbkk-status-name font-bold text-lg px-5 rounded" :style="{ backgroundColor: status.color }">{{ status.name }}</p>
                <p>{{ status.description }}</p>
            </div>
        </div>
    </div>

</template>
