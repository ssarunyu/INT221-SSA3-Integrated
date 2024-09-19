<script setup>
import { styleStatus } from '@/lib/styleStatus';
import { getDataById } from '@/lib/fetchMethod';
import { ref, onMounted } from 'vue';
import router from '@/router';
import { useRoute } from 'vue-router';

const route = useRoute()

const options = {
    timeZoneName: "short",
    hour12: false,
}
const item = ref()
onMounted(async () => {
  const fetch = await getDataById(import.meta.env.VITE_TASK_URL, route.params.detailId)
  fetch.createdOn = new Date(fetch.createdOn).toLocaleString('en-AU', options)
  fetch.updatedOn = new Date(fetch.updatedOn).toLocaleString('en-AU', options)
  item.value = await fetch
})
</script>

<template>
  <div v-if="item" class="fixed z-10 inset-0 overflow-y-auto">
    <div class="flex items-center justify-center h-screen">
      <div class="fixed inset-0 bg-gray-500 bg-opacity-75 backdrop-blur"></div>
      <div class="relative bg-white p-10 rounded-lg shadow-xl w-[70%]">
        <h1 class="itbkk-title text-2xl font-bold break-all">
          {{ item.title }}
        </h1>
        <hr class="h-px my-4 bg-gray-200 border-0">
        <div class="flex flex-col mt-5 space-y-5">
          <div>
            <p class="font-lg font-bold">Description</p>
            <p class="itbkk-description rounded break-all" :class="item.description === null ? 'text-gray-500 italic' : ''">
              {{ item.description === null ? 'No Description Provided' : item.description }}
            </p>
          </div>
          <div>
            <p class="text-lg font-bold">Assignees</p>
            <p class="itbkk-assignees" :class="item.assignees === null ? 'text-gray-500 italic' : ''">
              {{ item.assignees === null ? 'Unassigned' : item.assignees }}
            </p>
          </div>
          <div>
            <p class="text-lg font-bold">Status</p>
            <p :class="styleStatus(item.status.name)" class="px-5 py-1 w-[7rem] rounded text-center">{{ item.status.name }}</p>
          </div>
        </div>
        <hr class="h-px my-5 bg-gray-200 border-0">
        <div>
            <p class="text-lg font-bold">Time Details</p>
            <p class="itbkk-timezone"><strong>Timezone </strong>{{ Intl.DateTimeFormat().resolvedOptions().timeZone }}</p>
            <p class="itbkk-created-on"><strong>Created on </strong>{{ item.createdOn }}</p>
            <p class="itbkk-updated-on"><strong>Updated on </strong>{{ item.updatedOn }}</p>
        </div>
        <div class="mt-5">
          <button class="bg-red-500 duration-200 hover:bg-red-600 text-white font-semibold px-4 py-2 rounded"
          @click="router.push({ name : 'Home' })">
            Close
          </button>
        </div>
      </div>
    </div>
  </div>
</template>