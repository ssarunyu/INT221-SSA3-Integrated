<script setup>
import { getData } from '@/lib/fetchMethod';
import { useRoute } from 'vue-router';
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
        <!-- Card below head of table -->
        <div v-for="status in statusInBoard" class="w-full flex items-center justify-between p-3 bg-white rounded-lg shadow-md border">
            <p class="font-bold text-lg">{{ status.name }}</p>
            <p>{{ status }}</p>
            <p class="px-3 py-1 bg-blue-500 text-white rounded-lg">{{ status.name }}</p>
        </div>
    </div>
</div>

</template>
