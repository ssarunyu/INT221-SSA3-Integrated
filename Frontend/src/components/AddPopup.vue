<script setup>
import { onMounted, ref, defineEmits, watch } from 'vue';
import { getData, postData } from '@/lib/fetchMethod';
import router from '@/router';
import { useRoute } from 'vue-router';

// For send data back to parent
const emit = defineEmits(['taskAdded'])

const route = useRoute()

const addTitle = ref('');
const addDescription = ref('');
const addAssignees = ref('');
const addStatus = ref('');

const allStatus = ref([]);

const fetchStatus = async () => {
  const response = await getData(`${import.meta.env.VITE_BASE_URL}/v3/boards/${route.params.boardId}/statuses`);
  allStatus.value = await response;
  if(allStatus.value) {
    if(allStatus.value[0].name === 'NO STATUS') {
      allStatus.value[0].name = 'No Status'
    }
    if(allStatus.value[1].name === 'TODO') {
      allStatus.value[1].name = 'To Do'
    }
    if(allStatus.value[2].name === 'DOING') {
      allStatus.value[2].name = 'Doing'
    }
    if(allStatus.value[3].name === 'DONE') {
      allStatus.value[3].name = 'Done'
    }
  }
};

onMounted(async () => {
  await fetchStatus();
});

const confirmHandle = async () => {
  const newTask = {
    title: addTitle.value ? addTitle.value.trim() : null,
    description: addDescription.value ? addDescription.value.trim() : null,
    assignees: addAssignees.value ? addAssignees.value.trim() : null,
    status: addStatus.value,
  };

  try {
    const addNewTask = await postData(`${import.meta.env.VITE_BASE_URL}/v3/boards/${route.params.boardId}/tasks`, newTask)
    // Send task that just add to parent to resolve ref update
    emit('taskAdded', newTask)
    // Redirect when complete
    router.push({ name: 'Home', params: { boardId: route.params.boardId }})
  } catch (error) {
    console.error(error);
  }

  resetForm();
};

const resetForm = () => {
  addTitle.value = '';
  addDescription.value = '';
  addAssignees.value = '';
  addStatus.value = '';
};

const closeHandle = () => {
  resetForm();
  router.push({ name: 'Home', params: { boardId: route.params.boardId }})
};
</script>

<template>
  <div class="fixed inset-0 z-10 overflow-y-auto">
    <div class="flex items-center justify-center h-screen">
      <!-- Overlay -->
      <div class="fixed inset-0 bg-gray-500 bg-opacity-75 backdrop-blur"></div>
      <!-- Popup -->
      <div class="itbkk-modal-task relative bg-white rounded-lg shadow-xl w-[70%]">
        <div class="flex flex-col p-5">
          <form class="space-y-5" novalidate>
            <h2 class="mb-4 text-2xl font-bold">Add Task</h2>
            <hr>
            <div class="flex flex-col">
              <p class="font-semibold">Title</p>
              <input v-model="addTitle" class="p-2 border border-black rounded itbkk-title peer invalid:border-red-500 focus:outline-none" type="text" required>
              <p class="hidden text-sm text-red-600 peer-invalid:block">This field is required</p>
            </div>
            <div class="flex flex-col">
              <p class="font-semibold">Description</p>
              <input v-model="addDescription" class="p-2 border border-black rounded itbkk-description focus:outline-none" type="text">
            </div>
            <div class="flex flex-col">
              <p class="font-semibold">Assignees</p>
              <input v-model="addAssignees" class="p-2 border border-black rounded itbkk-assignees focus:outline-none" type="text">
            </div>
            <div class="flex items-center space-x-3">
              <p class="font-semibold">Status</p>
              <select v-model="addStatus" class="px-3 py-1 border border-gray-300 rounded itbkk-status">
                <option v-for="status in allStatus" :key="status.id" :value="status.id">
                  {{ status.name }}
                </option>
              </select>
            </div>
          </form>
          <div class="mt-5 space-x-5">
            <button @click="confirmHandle" class="px-4 py-2 font-semibold text-white duration-200 bg-green-500 rounded itbkk-button-confirm hover:bg-green-600 disabled:bg-green-500 disabled:cursor-not-allowed disabled:opacity-50">
              Save
            </button>
            <button @click="closeHandle" class="px-4 py-2 font-semibold text-white duration-200 bg-red-500 rounded itbkk-button-cancel hover:bg-red-600">
              Cancel
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
