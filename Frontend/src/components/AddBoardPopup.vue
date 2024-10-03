<script setup>
import router from "@/router";
import { postBoard } from "@/lib/fetchMethod";
import { ref } from "vue";

// Get payload from localstorage
const payload = JSON.parse(localStorage.getItem('payload'))

const emit = defineEmits(["createNewBoard", "close", "toastItem"]);
const closeHandle = () => {
  emit("close");
};

const addName = ref(`${payload.name} personal board`);
const isPrivate = ref(true);

const createNewBoard = async () => {
  const newBoard = {
    name: addName.value.trim(),
    visibility: isPrivate.value ? 'PRIVATE' : 'PUBLIC'
  }

  try {
    const result = await postBoard(`${import.meta.env.VITE_BASE_URL}/v3/boards`, newBoard);
    
    if(result.status === 401) {
      router.push({ name: 'Login' })
    }
    if (result && result.status === 201) {
      closeHandle();
      router.push({ name: "Home", params: { boardId: result.data.id } });
    } else {
      if(result.status === 401) {
        router.push({ name: 'Login' })
      }
      console.error("Error creating board:", result);
    }
  } catch (error) {
    console.error("Error in creating new board:", error);
  }
}
</script>

<template>
    <div class="fixed z-10 inset-0 overflow-y-auto">
      <div class="flex items-center justify-center h-screen">
        <div class="fixed inset-0 bg-gray-500 bg-opacity-75 backdrop-blur"></div>
        <div class="itbkk-modal-new relative bg-white rounded-lg shadow-xl w-[70%]">
          <div class="flex flex-col p-5">
            <form class="space-y-5" novalidate @submit.prevent="createNewBoard">
              <div class="flex justify-start items-center">
                <h2 class="text-2xl font-bold mb-0">Create new board</h2>
              </div>
              <hr>
              <div class="flex flex-col">
                <p class="font-semibold">Name</p>
                <input v-model="addName"
                  class="itbkk-board-name border border-black rounded p-2 peer invalid:border-red-500 focus:outline-none"
                  type="text" required maxlength="120" />
                <p class="hidden peer-invalid:block text-red-600 text-sm">
                  This field is required
                </p>
              </div>
              <div class="flex items-center mt-4">
                <label class="mr-2 font-semibold">Visibility:</label>
                <!-- Toggle visibility button -->
                <div class="relative flex">
                  <input type="checkbox" v-model="isPrivate" class="absolute opacity-0" />
                  <div
                    class="w-12 h-6 bg-gray-300 rounded-full cursor-pointer transition duration-200"
                    :class="{'bg-green-500': !isPrivate}"
                    @click="isPrivate = !isPrivate"
                  >
                    <div class="w-6 h-6 bg-white rounded-full shadow-md transform transition duration-200" :style="isPrivate ? 'transform: translateX(0)' : 'transform: translateX(100%);'"></div>
                  </div>
                  <p class="ml-3">{{ isPrivate ? 'Private' : 'Public' }} Board</p>
                </div>
              </div>
            </form>
            <div class="mt-5 space-x-5">
              <button @click="createNewBoard()"
                class="itbkk-button-ok bg-green-500 duration-200 hover:bg-green-600 text-white font-semibold px-4 py-2 rounded">
                Create
              </button>
              <button @click="closeHandle()"
                class="itbkk-button-cancel bg-red-500 duration-200 hover:bg-red-600 text-white font-semibold px-4 py-2 rounded">
                Cancel
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </template>