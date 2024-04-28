<script setup>
import { ref, onMounted } from 'vue';
import { getData } from '@/lib/fetchMethod';
import { TaskManagement } from '@/lib/TaskManagement.js'
const taskManagement = ref(new TaskManagement())

const fetch = async() => {
    // Fetch data
    const result = await getData(import.meta.env.VITE_URL)
    // Empty Handle
    // if(result.status !== 404) {}
    // Front-end
    taskManagement.value.addAllTask(result)
    const items = taskManagement.value.getAllTask()
    items.forEach(item => {
        if(item.status === 'TO_DO') {
            item.status = 'To Do'
        }
        if(item.status === 'NO_STATUS') {
            item.status = 'No Status'
        }
        if(item.status === 'DOING') {
            item.status = 'Doing'
        }
        if(item.status === 'DONE') {
            item.status = 'Done'
        }
    })
}

onMounted(async () => {
    await fetch()
})
</script>

<template>
    <h1 class="text-2xl font-bold">ITBKK SSA3 Taskboard</h1>
        <table class="min-w-1/2 divide-y divide-gray-200">
          <tr>
              <!-- Number -->
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                  Title
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                  Assignees
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                  Status
              </th>
          </tr>
          <tr v-show="taskManagement.getAllTask !== null"
          v-for="(item, index) in taskManagement.getAllTask()"
          class="itbkk-item bg-white divide-y divide-gray-200">
              <td class="px-6 py-4 whitespace-nowrap">
                  {{ index + 1 }}
              </td>
              <router-link :to="{ name: 'TaskDetail', params: { taskId: index + 1} }" class="itbkk-title">
                  {{ item.title }}
              </router-link>
              <td class="itbkk-assignees px-6 py-4 whitespace-nowrap" :class="item.assignees === null ? 'italic text-gray-500' : ''">
                  {{ item.assignees === null ? 'Unassigned' : item.assignees }}
              </td>
              <td class="itbkk-status px-6 py-4 whitespace-nowrap">
                  {{ item.status }}
              </td>
            </tr>
        </table>
</template>
