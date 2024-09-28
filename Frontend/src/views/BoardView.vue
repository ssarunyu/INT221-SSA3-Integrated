<script setup>
import { ref, onMounted } from 'vue';
import { getData } from '@/lib/fetchMethod';
import AddBoardPopup from '@/components/AddBoardPopup.vue';
import UserInfoPopup from '@/components/UserInfoPopup.vue';

const userAuthItem = JSON.parse(localStorage.getItem('payload'));

const boards = ref([]);
const showAddBoardPopup = ref(false);
const showUserInfoPopup = ref(false);

const fetchBoards = async () => {
  try {
    boards.value = await getData('http://localhost:8080/v3/boards');
  } catch (error) {
    console.error('Error fetching boards:', error);
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

const openUserInfoPopup = () => {
  showUserInfoPopup.value = true;
};


const closeUserInfoPopup = () => {
  showUserInfoPopup.value = false;
};

const handleLogout = () => {
  localStorage.removeItem('payload');
  router.push('/login');
};
</script>

<template>
  <router-view></router-view>
  <div class="w-full h-screen bg-gray-100">
    <!-- TITLE -->
    <div
      class="title bg-slate-800 text-white shadow-md flex justify-between items-center px-5"
    >
      <h1 class="text-2xl font-bold py-5">ITBKK-SSA3 Board</h1>

      <!-- User Profile Section -->
      <div class="flex items-center space-x-4">
        <span class="text-white font-medium">{{ userAuthItem.name }}</span>
        <img
          src="@/assets/user-circle-1.svg"
          alt="User Profile"
          class="w-10 h-10 rounded-full object-cover cursor-pointer"
          @click="openUserInfoPopup">
      </div>
    </div>

    <!-- Main Content -->
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

    <!-- Add Board Popup -->
    <AddBoardPopup
      v-if="showAddBoardPopup"
      @close="closeAddBoardPopup"
      @createNewBoard="refreshBoards"
    />

    <!-- User Info Popup -->
    <UserInfoPopup
      v-if="showUserInfoPopup"
      @close="closeUserInfoPopup"
      @logout="handleLogout"
    />
  </div>
</template>
