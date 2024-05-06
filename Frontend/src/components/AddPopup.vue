<script setup>

const emit = defineEmits(['confirm', 'close'])
const closeHandle = () => {
  emit('close')
}

const addTitle = ref('')
const addDescription = ref(null)
const addAssignees = ref(null)
const addStatus = ref(null)
const confirmHandle = () => {
  const newTask = 
  {
    title: addTitle.value,
    description: addDescription.value,
    assignees: addAssignees.value,
    status: addStatus.value
  }
  emit('confirm', newTask)
}
</script>

<template>
<div class="fixed z-10 inset-0 overflow-y-auto">
  <div class="flex items-center justify-center h-screen">
    <!-- Overlay -->
    <div class="fixed inset-0 bg-gray-500 bg-opacity-75 backdrop-blur"></div>
    <!-- Popup -->
    <div class="relative bg-white rounded-lg shadow-xl w-[70%]">
      <!-- Popup content -->
      <div class="flex flex-col p-5">
        <form class="space-y-5" novalidate>
            <h2 class="text-2xl font-bold mb-4">Add Task</h2>
            <hr>
            <div class="flex flex-col">
                <label class="font-semibold" for="title">Title</label>
                <input v-model="addTitle" class="itbkk-title border border-black rounded p-2 peer invalid:border-red-500 focus:outline-none" type="text" required>
                <p class="hidden peer-invalid:block text-red-600 text-sm">
                    This field required
                </p>
            </div>
            <div class="flex flex-col">
                <label class="font-semibold" for="assignees">Description</label>
                <input v-model="addDescription" class="itbkk-description border border-black rounded p-2 focus:outline-none" type="text">
            </div>
            <div class="flex flex-col">
                <label class="font-semibold" for="assignees">Assignees</label>
                <input v-model="addAssignees" class="itbkk-assignees border border-black rounded p-2 focus:outline-none" type="text">
            </div>
            <div class="flex items-center space-x-3 ">
                <label for="">Status</label>
                <select v-model="addStatus" class="itbkk-status rounded px-3 py-1 border border-gray-300">
                  <option value="NO_STATUS">No Status</option>
                  <option value="TO_DO">To Do</option>
                  <option value="DOING">Doing</option>
                  <option value="DONE">Done</option>
                </select>
              </div>
            </form>
            <div class="mt-5 space-x-5">
              <button @click="confirmHandle()" :disabled="!addTitle" class="itbkk-button-confirm bg-green-500 duration-200 hover:bg-green-600 text-white font-semibold px-4 py-2 rounded disabled:bg-green-500 disabled:cursor-not-allowed disabled:opacity-50">
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