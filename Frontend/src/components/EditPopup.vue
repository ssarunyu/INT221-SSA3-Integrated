<script setup>
import { getData, getDataById, updateData } from "@/lib/fetchMethod";
import { onMounted, ref } from "vue"
import { useRoute } from 'vue-router'
import router from '@/router';
const route = useRoute()
const disabled = ref(true)
const itemData = ref()
const toastHandle = ref()
const emit = defineEmits(['updateTask', 'toastItem'])

const statusData = ref([])
onMounted(async () => {
  // Get task data first
  const response = await getData(`${import.meta.env.VITE_BASE_URL}/v3/boards/${route.params.boardId}/tasks/${route.params.taskId}`)
  const statusResponse = await getData(`${import.meta.env.VITE_BASE_URL}/v3/boards/${route.params.boardId}/statuses`)
  statusData.value = statusResponse

  if(response.status === 404) {
    // NOTE: Give data to variables cause need to show the popup
    itemData.value = await response
    toastHandle.value = { type: 'error', status: true, message: `An error has occurred, the status does not exist` }
    emit('toastItem', toastHandle.value)
  } else {
    itemData.value = await response
  }
})

const updateHandle = async () => {
  itemData.value.title = itemData.value.title ? itemData.value.title.trim() : itemData.value.title
  itemData.value.description = itemData.value.description ? itemData.value.description.trim() : null
  console.log('task', itemData.value)
  // Fetch to Backend
  const response = await updateData(
    `${import.meta.env.VITE_BASE_URL}/v3/boards/${route.params.boardId}/tasks`,
    itemData.value,
    route.params.taskId
  )
  if(response.ok) {
    router.go(-1)
  }
}

const closeHandle = () => {
  router.go(-1)
}
</script>

<template>
  <div v-if="itemData" class="fixed z-10 inset-0 overflow-y-auto">
    <div class="flex items-center justify-center h-screen">
      <!-- Overlay -->
      <div class="fixed inset-0 bg-gray-500 bg-opacity-75 backdrop-blur"></div>
      <!-- Popup -->
      <div class="relative bg-white rounded-lg shadow-xl w-[70%]">
        <!-- Popup content -->
        <div class="flex flex-col p-5">
          <form class="space-y-5">
              <h2 class="text-2xl font-bold mb-4">Edit Task</h2>
              <hr>
              <div class="flex flex-col">
                  <p class="font-semibold">Title</p>
                  <input @input="disabled = false" class="itbkk-title border border-black rounded p-2 peer invalid:border-red-500 focus:outline-none" type="text" v-model="itemData.title" required>
                  <p class="hidden peer-invalid:block text-red-600 text-sm">
                    This field required
                  </p>
              </div>
              <div class="flex flex-col">
                  <p class="font-semibold">Description</p>
                  <input @input="disabled = false" class="itbkk-description border border-black rounded p-2 focus:outline-none" type="text" v-model="itemData.description" :placeholder="itemData.description === null ? 'No Description Provided' : ''">
              </div>
              <div class="flex flex-col">
                  <p class="font-semibold">Assignees</p>
                  <input @input="disabled = false" class="itbkk-assignees border border-black rounded p-2 focus:outline-none" type="text" v-model="itemData.assignees" :placeholder="itemData.assignees === null ? 'Unassigned' : ''">
              </div>
              <div class="flex items-center space-x-3 ">
                  <p>Status</p>
                  <select @change="disabled = false" class="itbkk-status rounded px-3 py-1 border border-gray-300" v-model="itemData.status" name="" id="">
                    <!-- TODO: Fetch all status that can change to -->
                    <option v-for="status in statusData" :value="status.id">{{ status.name }}</option>
                  </select>
              </div>
              <div class="flex flex-col">
                <p><strong>Timezone</strong> {{ Intl.DateTimeFormat().resolvedOptions().timeZone }}</p>
                <p><strong>Created On</strong> {{ itemData.createdOn }}</p>
                <p><strong>Updated On</strong> {{ itemData.updatedOn }}</p>
              </div>
            </form>
            <div class="itbkk-button-action mt-5 space-x-5">
                <button :disabled="disabled" class="itbkk-button-edit bg-green-500 duration-200 hover:bg-green-600 text-white font-semibold px-4 py-2 rounded disabled:cursor-not-allowed disabled:opacity-50"
                @click="updateHandle()">
                  Save
                </button>
                <button class="itbkk-button-cancel bg-red-500 duration-200 hover:bg-red-600 text-white font-semibold px-4 py-2 rounded"
                @click="closeHandle()">
                  Cancel
                </button>
            </div>
        </div>
      </div>
    </div>
  </div>
</template>