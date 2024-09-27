<script setup>
import { ref, onMounted } from "vue";
import { getData } from "@/lib/fetchMethod";
import router from "@/router";
import AddBoardPopup from "@/components/AddBoardPopup.vue"; // Import the popup

const boards = ref([]);
const showAddBoardPopup = ref(false);

const fetchBoards = async () => {
  try {
    boards.value = await getData("http://localhost:8080/v3/boards");
  } catch (error) {
    console.error("Error fetching boards:", error);
  }
};

onMounted(async () => {
  await fetchBoards();
});

const openAddBoardPopup = () => {
  showAddBoardPopup.value = true;
};

const closeAddBoardPopup = () => {
  showAddBoardPopup.value = false;
};

const refreshBoards = async () => {
  await fetchBoards();
};
</script>

<template>
  <router-view></router-view>
  <div class="w-full h-screen bg-gray-100">
    <!-- TITLE -->
    <div class="title bg-slate-800 text-white shadow-md">
      <h1 class="text-2xl font-bold p-5 text-center">ITBKK-SSA3 Board</h1>
    </div>
    <div class="w-full flex flex-col justify-between items-center space-y-5">
      <div
        @click="openAddBoardPopup"
        class="itbkk-button-add rounded-md p-5 bg-slate-200 text-slate-500 cursor-pointer duration-300 hover:bg-slate-300 hover:text-slate-700"
      >
        + Add New Board
      </div>
    </div>
    <!-- CARDS SECTION -->
    <div class="flex flex-wrap justify-center mt-5 items-center gap-5">
      <template v-for="board in boards" :key="board.id">
        <div
          class="w-1/4 bg-white shadow-lg rounded-lg cursor-pointer duration-300 hover:bg-slate-50"
        >
          <div class="p-6 flex justify-between items-center">
            <div>
              <a href="#">
                <h5 class="text-xl font-semibold tracking-tight text-gray-800">
                  {{ board.boardName }}
                </h5>
                <p class="text-sm font-medium text-gray-600">
                  Status: {{ board.visibility }}
                </p>
              </a>
            </div>
            <div class="flex flex-col items-center rounded">
              <h5 class="text-xs my-1 text-gray-500">Total Tasks</h5>
              <h5 class="font-bold text-2xl text-gray-800">
                {{ board.totalTasks }}
              </h5>
            </div>
          </div>
        </div>
      </template>
    </div>
    <AddBoardPopup
      v-if="showAddBoardPopup"
      @close="closeAddBoardPopup"
      @createNewBoard="refreshBoards"
    />
  </div>
</template>
