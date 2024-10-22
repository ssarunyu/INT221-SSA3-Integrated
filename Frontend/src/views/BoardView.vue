<script setup>
  import { ref, onMounted } from "vue"
  import router from "@/router"
  import { getData } from "@/lib/fetchMethod"
  import AddBoardPopup from "@/components/AddBoardPopup.vue"
  import UserInfoPopup from "@/components/UserInfoPopup.vue"

  const userAuthItem = JSON.parse(localStorage.getItem("payload"))

  const boards = ref([])
  const showAddBoardPopup = ref(false)
  const showUserInfoPopup = ref(false)
  const justLoggedIn = ref(false)
  const isFetchingBoards = ref(false);

  const fetchBoards = async () => {
  if (!userAuthItem) {
    router.push({ name: "Login" });
    return;
  }

  isFetchingBoards.value = true; // Set fetching to true
  try {
    boards.value = await getData(`${import.meta.env.VITE_BASE_URL}/v3/boards`);

    // Debug data type
    console.log('board.value', typeof boards.value, boards.value)

    // Redirect only if just logged in and if we're not coming back from 'Home'
    if (boards.value.length > 0) {
      router.push({ name: 'Home', params: { boardId: boards.value.personalBoards[0] } })
    }
  } catch (error) {
    console.error("Error fetching boards:", error);
  } finally {
    isFetchingBoards.value = false; // Set fetching to false regardless of success or failure
  }
};


onMounted(async () => {
  justLoggedIn.value = true;

  // ตรวจสอบว่า route มาจากหน้าไหน
  if (router.name !== "Home") {
    try {
      await fetchBoards();
    } catch (error) {
      console.error("Error during mounted hook:", error);
    }
  }
});

  const openAddBoardPopup = () => {
    showAddBoardPopup.value = true
  }

  const closeAddBoardPopup = () => {
    showAddBoardPopup.value = false
  }

  const refreshBoards = async () => {
    await fetchBoards()
  }

  const openUserInfoPopup = () => {
    showUserInfoPopup.value = true
  }

  const closeUserInfoPopup = () => {
    showUserInfoPopup.value = false
  }

  const handleLogout = () => {
    localStorage.removeItem("payload")
    router.push("/login")
  }
</script>

<template>
  <div class="w-full h-screen bg-gray-100">
    <!-- TITLE -->
    <div
      class="flex items-center justify-between px-5 text-white shadow-md title bg-slate-800"
    >
      <h1 class="py-5 text-2xl font-bold">ITBKK-SSA3 Board</h1>

      <!-- User Profile Section -->
      <div v-if="userAuthItem" class="flex items-center space-x-4">
        <span class="font-medium text-white">{{ userAuthItem.name }}</span>
        <img
          src="@/assets/profile-user.svg"
          alt="User Profile"
          class="object-cover w-10 h-10 rounded-full cursor-pointer"
          @click="openUserInfoPopup"
        />
      </div>
    </div>

    <!-- Main Content -->
    <div class="flex flex-col items-center justify-between w-full space-y-5">
      <div
        @click="openAddBoardPopup"
        class="p-5 duration-300 rounded-md cursor-pointer itbkk-button-create bg-slate-200 text-slate-500 hover:bg-slate-300 hover:text-slate-700"
      >
        + Add New Board
      </div>

            <!-- Show loading spinner or message -->
            <div v-if="isFetchingBoards" class="text-gray-600">
        Loading boards...
      </div>
    </div>

    <!-- CARDS SECTION -->
    <div class="flex flex-wrap items-center justify-center gap-5 mt-5">
      <template v-for="board in boards.personalBoards" :key="board.id">
        <div
          class="w-1/4 duration-300 bg-white rounded-lg shadow-lg cursor-pointer hover:bg-slate-50"
        >
          <div
            @click="
              router.push({ name: 'Home', params: { boardId: board.id } })
            "
            class="flex items-center justify-between p-6"
          >
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
              <h5 class="my-1 text-xs text-gray-500">Total Tasks</h5>
              <h5 class="text-2xl font-bold text-gray-800">
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
