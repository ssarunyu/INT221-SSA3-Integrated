<script setup>
import { getData, getDataById } from '@/lib/fetchMethod.js';
import { TaskManagement } from '@/lib/TaskManagement.js'
import router from '@/router';
import { ref, onMounted } from 'vue';

import { useRoute } from 'vue-router';
const { params } = useRoute()

const taskManagement = ref(new TaskManagement())
const options = {
  timeZoneName: "short",
  hour12: false,
};

onMounted(async () => {
    const result = await getDataById(import.meta.env.VITE_URL, params.taskId)
    // Not found page handle
    if(result.status === 404) {
      alert('The requested task does not exist')
      router.push('/error')
      setTimeout(() => {
        router.push('/')
      }, 2000);
    } else {
      const formatCreatedOn = new Date(result.createdOn).toLocaleString('en-AU', options)
      const formatUpdatedOn = new Date(result.updatedOn).toLocaleString('en-AU', options)
      result.createdOn = formatCreatedOn
      result.updatedOn = formatUpdatedOn
      // Front-end
      taskManagement.value.addTask(result)
    }
})
</script>

<template>
  <div class="bg-white shadow-xl p-10 m-10 flex flex-col rounded-lg" v-for="item in taskManagement.getAllTask()">
    <h1 class="itbkk-title text-2xl font-bold break-all">
      {{ item.title }}
    </h1>
    <hr class="h-px my-4 bg-gray-200 border-0">
    <div class="flex flex-col mt-5 space-y-5">
      <!-- Desc -->
      <div>
        <p class="font-lg font-bold">Description</p>
        <p class="itbkk-description rounded break-all" :class="item.description === null ? 'text-gray-500 italic' : ''">
          {{ item.description === null ? 'No Description Provided' : item.description }}
        </p>
      </div>
      <!-- Ass -->
      <div>
        <p class="text-lg font-bold" for="">Assignees</p>
        <p class="itbkk-assignees" :class="item.assignees === null ? 'text-gray-500 italic' : ''">
          {{ item.assignees === null ? 'Unassigned' : item.assignees }}
        </p>
      </div>
      <!-- Status -->
      <div>
        <p class="text-lg font-bold" for="">Status</p>
        <select class="itbkk-status border border-black px-3 py-1 rounded" v-model="item.status" name="" id="">
          <option value="NO_STATUS">No Status</option>
          <option value="TO_DO">To Do</option>
          <option value="DOING">Doing</option>
          <option value="DONE">Done</option>
        </select>
      </div>
    </div>
    <hr class="h-px my-5 bg-gray-200 border-0">
    <div>
        <p class="text-lg font-bold">Time Details</p>
        <p class="itbkk-timezone"><strong>Timezone </strong>{{ Intl.DateTimeFormat().resolvedOptions().timeZone }}</p>
        <p class="itbkk-created-on"><strong>Created on </strong>{{ item.createdOn }}</p>
        <p class="itbkk-updated-on"><strong>Updated on </strong>{{ item.updatedOn }}</p>
    </div>
  </div>
</template>