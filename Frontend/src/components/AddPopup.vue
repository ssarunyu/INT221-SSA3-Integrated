<script setup>
import { onMounted, ref, defineEmits, watch } from 'vue';
import { getData, postData } from '@/lib/fetchMethod';
import { useRoute, useRouter } from 'vue-router';
import { useTaskStore } from '@/stores/TaskStore';

const route = useRoute();
const router = useRouter();
const taskStore = useTaskStore();

const addTitle = ref('');
const addDescription = ref('');
const addAssignees = ref('');
const addStatus = ref('');

const allStatus = ref([]);

const emit = defineEmits(['task-added']);

const fetchStatus = async () => {
  const response = await getData(`${import.meta.env.VITE_BASE_URL}/v3/boards/${route.params.boardId}/statuses`);
  allStatus.value = await response;
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
    const result = await postData(`http://localhost:8080/v3/boards/${route.params.boardId}/tasks`, newTask);
    console.log("API Response:", result);

    if (result && result.status === 201) {
      // Check if result.data has the id property
      const taskId = result.data?.id; // Use optional chaining
      if (taskId) {
        taskStore.addTask({ ...newTask, id: taskId }); // Add task with ID
      } else {
        console.error("No ID found in the response:", result.data);
        console.log("Response body:", result);
      }
      closeHandle();
    } else {
      console.error("Error creating task:", result);
    }
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
  taskStore.fetchTasks(route.params.boardId); // Fetch tasks to ensure the latest data
  router.push({ name: 'Home', params: { boardId: route.params.boardId } });
};

// Optional: watch for changes to tasks in the store
watch(() => taskStore.state.allTasks, (newTasks) => {
  console.log("Updated Tasks:", newTasks);
});
</script>

<template>
  <div class="fixed z-10 inset-0 overflow-y-auto">
    <div class="flex items-center justify-center h-screen">
      <!-- Overlay -->
      <div class="fixed inset-0 bg-gray-500 bg-opacity-75 backdrop-blur"></div>
      <!-- Popup -->
      <div class="itbkk-modal-task relative bg-white rounded-lg shadow-xl w-[70%]">
        <div class="flex flex-col p-5">
          <form class="space-y-5" novalidate>
            <h2 class="text-2xl font-bold mb-4">Add Task</h2>
            <hr>
            <div class="flex flex-col">
              <p class="font-semibold">Title</p>
              <input v-model="addTitle" class="itbkk-title border border-black rounded p-2 peer invalid:border-red-500 focus:outline-none" type="text" required>
              <p class="hidden peer-invalid:block text-red-600 text-sm">This field is required</p>
            </div>
            <div class="flex flex-col">
              <p class="font-semibold">Description</p>
              <input v-model="addDescription" class="itbkk-description border border-black rounded p-2 focus:outline-none" type="text">
            </div>
            <div class="flex flex-col">
              <p class="font-semibold">Assignees</p>
              <input v-model="addAssignees" class="itbkk-assignees border border-black rounded p-2 focus:outline-none" type="text">
            </div>
            <div class="flex items-center space-x-3">
              <p class="font-semibold">Status</p>
              <select v-model="addStatus" class="itbkk-status rounded px-3 py-1 border border-gray-300">
                <option v-for="status in allStatus" :key="status.id" :value="status.id">{{ status.name }}</option>
              </select>
            </div>
          </form>
          <div class="mt-5 space-x-5">
            <button @click="confirmHandle" class="itbkk-button-confirm bg-green-500 duration-200 hover:bg-green-600 text-white font-semibold px-4 py-2 rounded disabled:bg-green-500 disabled:cursor-not-allowed disabled:opacity-50">
              Save
            </button>
            <button @click="closeHandle" class="itbkk-button-cancel bg-red-500 duration-200 hover:bg-red-600 text-white font-semibold px-4 py-2 rounded">
              Cancel
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
