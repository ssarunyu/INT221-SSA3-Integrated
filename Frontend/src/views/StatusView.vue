<script setup>
import { getData } from '@/lib/fetchMethod';
import { useRoute } from 'vue-router';
import router from '@/router';
import { ref, onMounted } from 'vue'

const route = useRoute()

const statusInBoard = ref({})
const fetch = async () => {
    const response = await getData(`${import.meta.env.VITE_BASE_URL}/v3/boards/${route.params.boardId}/statuses`)
    if(response) {
        statusInBoard.value = response
    } else {
        console.log('error')
    }
}

onMounted(() => fetch())
</script>

<template>
    <router-view></router-view>
    <div class="w-full min-h-screen">
        <!-- Nav -->
        <div class="flex justify-between items-center bg-slate-800 p-5">
            <h1 class="font-bold text-2xl text-white">ITBKK SSA3 Taskboard</h1>
            <!-- User Info -->
            <div class="text-right text-white">
                <h1 class="font-semibold">John Doe</h1>
                <h1 class="text-xs">john.doe@example.com</h1>
            </div>
        </div>

        <div class="flex flex-col space-y-4 p-5">

            <!-- Head of table -->
            <div class="flex w-full items-center justify-between font-bold text-white p-3 bg-slate-600">
                <p>Title</p>
                <div class="flex items-center space-x-1">
                    <p>Status</p>
                </div>
            </div>
            <!-- Add Status -->
            <div @click="router.push({ name: 'AddStatus' })"
                    class="itbkk-button-add w-2/3 rounded-md p-5 bg-slate-200 text-slate-500 cursor-pointer duration-300 hover:bg-slate-300 hover:text-slate-700">
                    + Add New Status
            </div>
            <!-- Card below head of table -->
            <div v-for="status in statusInBoard" class="w-full flex items-center justify-between p-3 bg-white rounded-lg shadow-md border">
                <p @click="router.push({ name: 'EditStatusPopup', params: { editStatusId: status.id }})">Edit</p>
                <p class="font-bold text-lg">{{ status.name }}</p>
                <p>{{ status.description }}</p>
                <p class="px-3 py-1 text-black rounded-lg" :style="{ backgroundColor: status.color }">{{ status.name }}</p>
            </div>
        </div>
    </div>

</template>
