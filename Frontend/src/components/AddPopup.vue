<script setup>
import { onMounted, computed, watch } from 'vue';
import { getData } from '@/lib/fetchMethod';
import { ref } from 'vue'
import router from '@/router';

const addTitle = ref('')
const addDescription = ref(null)
const addAssignees = ref(null)
const addStatus = ref(null)

const emit = defineEmits(['confirm', 'close'])

const allStatus = ref()
const fetchStatus = async () => {
  const response = await getData(import.meta.env.VITE_STATUS_URL)
  allStatus.value = await response
}

onMounted(async() => {
  await fetchStatus()
})

const disabled = ref(false)
watch(addTitle, (valTitle) => {
  if(valTitle.length > 100 || valTitle === '') {
    disabled.value = false
  } else {
    disabled.value = true
  }
})
watch(addDescription, (valDesc) => {
  console.log(valDesc.length)
  if(valDesc.length > 500 || valDesc === '') {
    disabled.value = false
  } else {
    disabled.value = true
  }
})
watch(addAssignees, (valAssign) => {
  console.log(valAssign.length)
  if(valAssign.length > 30 || valAssign === '') {
    disabled.value = false
  } else {
    disabled.value = true
  }
})

const confirmHandle = () => {
  const newTask = 
  {
    title: addTitle.value ? addTitle.value.trim() : null,
    description: addDescription.value ? addDescription.value.trim() : null,
    assignees: addAssignees.value ? addAssignees.value.trim() : null,
    status: addStatus.value
  }
  emit('confirm', newTask)
  // Clear form when open again
  addTitle.value = ''
  addDescription.value = ''
  addAssignees.value = ''
  addStatus.value = ''
}

const closeHandle = () => {
  router.go(-1)
  emit('close')
  // Clear form when open again
  addTitle.value = ''
  addDescription.value = ''
  addAssignees.value = ''
  addStatus.value = ''
  disabled.value = false
}
</script>

<template>
<div class="fixed z-10 inset-0 overflow-y-auto">
  <div class="flex items-center justify-center h-screen">
    <!-- Overlay -->
    <div class="fixed inset-0 bg-gray-500 bg-opacity-75 backdrop-blur"></div>
    <!-- Popup -->
    <div class="itbkk-modal-task relative bg-white rounded-lg shadow-xl w-[70%]">
      <!-- Popup content -->
      <div class="flex flex-col p-5">
        <form class="space-y-5" novalidate>
            <h2 class="text-2xl font-bold mb-4">Add Task</h2>
            <hr>
            <div class="flex flex-col">
                <p class="font-semibold">Title</p>
                <input v-model="addTitle" class="itbkk-title border border-black rounded p-2 peer invalid:border-red-500 focus:outline-none" type="text" required>
                <p class="hidden peer-invalid:block text-red-600 text-sm">
                    This field required
                </p>
            </div>
            <div class="flex flex-col">
                <p class="font-semibold">Description</p>
                <input v-model="addDescription" class="itbkk-description border border-black rounded p-2 focus:outline-none" type="text">
            </div>
            <div class="flex flex-col">
                <p class="font-semibold">Assignees</p>
                <input v-model="addAssignees" class="itbkk-assignees border border-black rounded p-2 focus:outline-none" type="text">
            </div>
            <div class="flex items-center space-x-3 ">
                <p class="font-semibold">Status</p>
                <select v-model="addStatus" class="itbkk-status rounded px-3 py-1 border border-gray-300">
                  <option v-for="status in allStatus" :value="status.id">{{ status.name }}</option>
                </select>
              </div>
            </form>
            <div class="mt-5 space-x-5">
              <button @click="confirmHandle()" :disabled="!disabled" class="itbkk-button-confirm disabled bg-green-500 duration-200 hover:bg-green-600 text-white font-semibold px-4 py-2 rounded disabled:bg-green-500 disabled:cursor-not-allowed disabled:opacity-50">
                Save
              </button>
              <button @click="closeHandle()" class="itbkk-button-cancel disabled bg-red-500 duration-200 hover:bg-red-600 text-white font-semibold px-4 py-2 rounded">
                Cancel
              </button>
            </div>
        </div>
    </div>
  </div>
</div>
</template>