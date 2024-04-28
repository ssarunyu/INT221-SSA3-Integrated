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
    // const result = await getData(`${import.meta.env.VITE_URL}/${params.taskId}`)
    const result = await getDataById(import.meta.env.VITE_URL, params.taskId)
    // Not found page handle
    if(result.status === 404) {
      alert('The requested task does not exist')
      router.go(-1)
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
  <div class="bg-blue-200 p-10 m-10 flex flex-col rounded-lg" v-for="item in taskManagement.getAllTask()">
    <h1 class="itbkk-title text-2xl font-bold">
      {{ item.title }}
    </h1>
    <div class="flex flex-col mt-5">
      <p class="font-lg font-bold">Description</p>
      <textarea class="itbkk-description rounded" :class="item.description === null ? 'text-gray-500 italic' : ''" cols="30" rows="5">
        {{ item.description === null ? 'No Description Provided' : item.description }}
      </textarea>
    </div>
    <div class="flex flex-col mt-5">
      <label class="text-lg font-bold" for="">Assignees</label>
      <textarea class="itbkk-assignees" :class="item.assignees === null ? 'text-gray-500 italic' : ''" cols="30" rows="10">
        {{ item.assignees === null ? 'Unassigned' : item.assignees }}
      </textarea>
    </div>
    <div class="flex justify-between mt-5">
      <div class="space-x-2">
        <label class="text-lg font-bold" for="">Status</label>
        <select class="itbkk-status" name="" id="">
          <option value="">No Status</option>
          <option value="">To Do</option>
          <option value="">Doing</option>
          <option value="">Done</option>
        </select>
      </div>
      <div>
        <p class="text-lg font-bold">Time Details</p>
        <p class="itbkk-timezone">{{ Intl.DateTimeFormat().resolvedOptions().timeZone }}</p>
        <p class="itbkk-created-on">{{ item.createdOn }}</p>
        <p class="itbkk-updated-on"> {{ item.updatedOn }}</p>
      </div>
    </div>
  </div>
</template>