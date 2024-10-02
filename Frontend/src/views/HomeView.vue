<script setup>
  import { ref, watch, onMounted, computed } from "vue"
  import { getData, deleteData, patchVisi } from "@/lib/fetchMethod.js"
  import router from "@/router"
  import { useRoute } from "vue-router"
  import Toast from "@/components/Toast.vue"
  import VisibilityBoardPopup from "@/components/VisibilityBoardPopup.vue"
  import DeletePopup from "@/components/DeletePopup.vue"
  import UserInfoPopup from "@/components/UserInfoPopup.vue"

  const route = useRoute()
  const userAuthItem = JSON.parse(localStorage.getItem("payload"))

  const allTasks = ref([])
  const allStatuses = ref([])
  const boardDetail = ref() // Set initial value to null
  const fetch = async () => {
    // Fetch board details
    const responseBoardDetail = await getData(
      `${import.meta.env.VITE_BASE_URL}/v3/boards/${route.params.boardId}`,
      false
    )

    if (responseBoardDetail) {
      boardDetail.value = responseBoardDetail

      // Check if the board is public
      if (boardDetail.value.visibility === "PUBLIC") {
        // Fetch tasks and statuses for public boards
        const allTask = await getData(
          `${import.meta.env.VITE_BASE_URL}/v3/boards/${
            route.params.boardId
          }/tasks`,
          false,
          true
        )
        if (allTask !== undefined) {
          allTasks.value.push(...allTask)
        }

        const allStatus = await getData(
          `${import.meta.env.VITE_BASE_URL}/v3/boards/${route.params.boardId}/statuses`, false, true)
        if (allStatus !== undefined) {
          allStatuses.value.push(...allStatus)
        }

        return // No need for redirect, simply return
      } else if (boardDetail.value.visibility === "PRIVATE" && !userAuthItem) {
        // Redirect only for private boards when not authenticated
        router.push({ name: "Login" })
        return
      }

      // If the board is private and the user is authenticated, fetch tasks and statuses
      if (userAuthItem) {
        const allTask = await getData(
          `${import.meta.env.VITE_BASE_URL}/v3/boards/${
            route.params.boardId
          }/tasks`
        )
        if (allTask !== undefined) {
          allTasks.value.push(...allTask)
        }

        const allStatus = await getData(
          `${import.meta.env.VITE_BASE_URL}/v3/boards/${
            route.params.boardId
          }/statuses`
        )
        if (allStatus !== undefined) {
          allStatuses.value.push(...allStatus)
        }
      }
    } else {
      // Handle the case where the board detail could not be fetched
      console.error("Failed to fetch board details.")
    }
  }

  const isPrivate = ref(false)
  onMounted(async () => {
    await fetch()
    isPrivate.value =
      boardDetail.value && boardDetail.value.visibility === "PRIVATE"
  })

  // Toast
  const toastHandle = ref()

  // Edit
  const options = {
    timeZoneName: "short",
    hour12: false,
  }

  // const tasks = ref(allTasks.value)
  // const sortStage = ref(0);
  // const changeSortStage = () => {
  //     sortStage.value = (sortStage.value + 1) % 3;
  // };

  // const sortTask = computed(() => {
  //     const copyTask = [...tasks.value];

  //     const sortFunctions = {
  //         0: tasks => tasks,
  //         1: tasks => tasks.sort((a, b) => a.status.name.localeCompare(b.status.name)),
  //         2: tasks => tasks.sort((a, b) => b.status.name.localeCompare(a.status.name))
  //     };

  //     return sortFunctions[sortStage.value](copyTask);
  // });

  // const sortIcon = computed(() => {
  //     const icons = {
  //         0: 'fa-solid fa-sort',
  //         1: 'fa-solid fa-sort-down',
  //         2: 'fa-solid fa-sort-up'
  //     };

  //     return icons[sortStage.value];
  // });

  // const allStatusArr = allStatuses.value
  // const filterSelect = ref([])
  // const submitFilter = async (userClick) => {
  //     const findExist = filterSelect.value.indexOf(userClick)
  //     if (findExist !== -1) {
  //         filterSelect.value.splice(findExist, 1)
  //     } else {
  //         filterSelect.value.push(userClick)
  //     }
  //     await updateTasks()
  // }

  // const updateTasks = async () => {
  //     let userFilter = ''
  //     if (filterSelect.value.length) {
  //         userFilter = filterSelect.value.map(a => `filterStatuses=${a}`).join('&')
  //     }
  //     const response = await getData(`${import.meta.env.VITE_BASE_URL}/v3/boards/tasks?${userFilter}`)
  //     tasks.value = response
  // }

  // const clearFilter = async () => {
  //     filterSelect.value = []
  //     const response = await getData(`${import.meta.env.VITE_BASE_URL}/v3/boards/tasks`)
  //     tasks.value = response
  // }

  // const removeFilter = async (r) => {
  //     const findExist = filterSelect.value.indexOf(r)
  //     if (findExist !== -1) {
  //         filterSelect.value.splice(findExist, 1)
  //         await updateTasks()
  //     }
  // }

  // Delete
  const deletePopupStatus = ref(false)
  const deleteTarget = ref()
  const openDeletePopup = async id => {
    deletePopupStatus.value = true
    const result = await getData(
      `${import.meta.env.VITE_BASE_URL}/v3/boards/${
        route.params.boardId
      }/tasks/${id}`
    )
    deleteTarget.value = result
  }
  const deleteConfirm = async () => {
    // Back-end
    const response = await deleteData(
      `${import.meta.env.VITE_BASE_URL}/v3/boards/${
        route.params.boardId
      }/tasks`,
      deleteTarget.value.id
    )
    // Check Status
    if (response.ok) {
      toastHandle.value = {
        type: "success",
        status: true,
        message: `The task has been deleted`,
      }
      // Front-end
      const indexToDelete = allTasks.value.findIndex(
        a => a.id === deleteTarget.value.id
      )
      allTasks.value.splice(indexToDelete, 1)
      deletePopupStatus.value = false
    } else {
      toastHandle.value = {
        type: "error",
        status: true,
        message: `Task doesn't exist`,
      }
      deletePopupStatus.value = false
    }
  }

  // Visibility and modal
  // NOTE: True = private, False = public
  const showVisi = ref(false)
  // Open popup
  const changeVisi = () => {
    showVisi.value = true
  }
  // Handle confirm
  const confirmVisibilityChange = async confirmation => {
    console.log("confirm", confirmation)
    isPrivate.value = confirmation
    try {
      // Should couter with base because it's change opposite
      const response = await patchVisi(
        import.meta.env.VITE_BASE_URL,
        route.params.boardId,
        confirmation ? "PRIVATE" : "PUBLIC"
      )
      if (response.status === 500) {
        window.alert("There is a problem. Please try again later.")
      }
      if (response.status === 403) {
        window.alert("you do not have permission")
      }
      showVisi.value = false
    } catch (error) {
      console.error(error)
    }
  }
  // Handle cancel
  const cancelVisibilityChange = () => {
    showVisi.value = false
  }

  // User permission
  const isOwner = ref(route.meta.isOwner)
  console.log(isOwner);
  

  //User card
  const showUserInfoPopup = ref(false)

  const openUserInfoPopup = () => {
    showUserInfoPopup.value = true
  }

  const closeUserInfoPopup = () => {
    showUserInfoPopup.value = false
  }
</script>

<template>
  <!-- User Info Popup -->
  <UserInfoPopup v-if="showUserInfoPopup" @close="closeUserInfoPopup" />
  <VisibilityBoardPopup
    v-if="showVisi"
    :isPrivate="isPrivate"
    @confirm="confirmVisibilityChange"
    @cancel="cancelVisibilityChange"
  />
  <router-view></router-view>
  <DeletePopup
    v-show="deletePopupStatus"
    v-if="deleteTarget"
    :deleteItem="deleteTarget"
    @close="deletePopupStatus = false"
    @confirm="deleteConfirm()"
  />
  <div class="w-full min-h-screen">
    <!-- Nav -->
    <div class="flex items-center justify-between p-5 bg-slate-800">
      <h1 class="text-2xl font-bold text-white">ITBKK SSA3 Taskboard</h1>
      <!-- User Profile Section -->
      <div
        v-if="userAuthItem"
        class="flex items-center justify-between space-x-4"
      >
        <div class="flex flex-col items-end">
          <span class="font-medium text-white">{{ userAuthItem.name }}</span>
          <span class="font-normal text-white">{{ userAuthItem.email }}</span>
        </div>
        <img
          src="@/assets/profile-user.svg"
          alt="User Profile"
          class="object-cover w-10 h-10 rounded-full cursor-pointer"
          @click="openUserInfoPopup"
        />
      </div>
    </div>
    <div class="flex flex-col p-5 space-y-4">
      <!-- Filter -->
      <!-- <div class="flex">
                <div class="itbkk-status-filter dropdown dropdown-bottom">
                    <div tabindex="0" role="button" class="m-1 btn">Filter Tasks</div>
                    <ul tabindex="0" class="dropdown-content z-[1] menu p-2 shadow bg-white rounded-box w-52">
                        <div class="flex justify-end w-full my-2 text-blue-500 underline">
                            <p class="cursor-pointer" @click="clearFilter">Clear all</p>
                        </div>
                        <div v-for="status in allStatusArr" class="flex justify-between p-2 duration-300 rounded hover:bg-gray-100 ">
                            <button class="itbkk-status-choice" @click="submitFilter(status.name)">{{ status.name }}</button> 
                        </div>
                    </ul>
                </div>
                <div class="flex items-center ml-4 space-x-3">
                    <div v-for="status in filterSelect" class="px-5 py-2 bg-blue-200 rounded">
                        {{ status }}
                        <font-awesome-icon class="cursor-pointer" @click="removeFilter(status)" icon="fa-solid fa-xmark" />
                    </div>
                </div>
            </div> -->
      <!-- Menu -->
      <span class="flex justify-between">
        <!-- Go Back to All Boards Button -->
        <button
          class="px-4 py-2 mb-4 text-white bg-blue-500 rounded hover:bg-blue-600"
          @click="router.push({ name: 'Board' })"
        >
          ← Go Back to All Boards
        </button>
        <!-- Status Page button -->
        <button
          class="px-4 py-2 mb-4 rounded cursor-pointer itbkk-manage-status bg-slate-300 hover:bg-slate-400"
          @click="router.push({ name: 'StatusView' })"
        >
          Status Page
        </button>
      </span>

      <div v-if="boardDetail">
        <!-- Toggle visibility button -->
        <div class="relative flex">
          <input type="checkbox" class="absolute opacity-0" />
          <div
            class="w-12 h-6 transition duration-200 bg-gray-300 rounded-full cursor-pointer itbkk-board-visibility"
            :class="{ 'bg-green-500': !isPrivate }"
            @click="changeVisi"
          >
            <div
              class="w-6 h-6 transition duration-200 transform bg-white rounded-full shadow-md"
              :style="
                isPrivate
                  ? 'transform: translateX(0)'
                  : 'transform: translateX(100%);'
              "
            ></div>
          </div>
          <p class="ml-3">{{ isPrivate ? "Private" : "Public" }} Board</p>
        </div>
        <p class="text-2xl font-bold text-center">
          {{ boardDetail.boardName }}
        </p>
      </div>
      <!-- Head of table -->
      <div
        class="flex items-center justify-between w-full p-3 font-bold text-white font-xl bg-slate-600"
      >
        <p>Title</p>
        <p>Assignees</p>
        <div class="flex items-center space-x-1">
          <p>Status</p>
          <!-- <div @click="changeSortStage()" class="p-2 transition duration-300 cursor-pointer"> -->
          <!-- <font-awesome-icon class="itbkk-status-sort" v-if="sortIcon" :icon="sortIcon" /> -->
          <!-- </div> -->
        </div>
      </div>
      <!-- Add task button -->
      <div class="flex flex-col items-center justify-center w-full space-y-5">
        <button
          @click="router.push({ name: 'AddTask' })"
          class="w-full p-5 duration-300 rounded-md cursor-pointer itbkk-button-add disabled:cursor-not-allowed bg-slate-200 text-slate-500 hover:bg-slate-300 hover:text-slate-700"
          :disabled="!isOwner"
        >
          + Add New Task
        </button>
        <Toast :toastObject="toastHandle" @close="toastHandle.status = false" />
        <!-- Each element -->
        <div
          v-for="item in allTasks"
          :key="item.id"
          class="relative flex items-center justify-between w-full p-3 border rounded itbkk-item"
        >
          <div
            class="absolute left-0 w-1 h-10"
            :class="`bg-[${item.status.statusColor}]`"
          ></div>
          <div class="flex items-center space-x-3">
            <div>
              <div class="dropdown itbkk-button-action">
                <p
                  tabindex="0"
                  role="button"
                  class="m-1 text-2xl font-bold border-none btn"
                >
                  :
                </p>
                <ul
                  tabindex="0"
                  class="dropdown-content z-[1] menu p-2 shadow bg-white rounded-box w-40"
                >
                  <!-- NOTE: Edit must send params -->
                  <li>
                    <button
                      class="itbkk-button-edit disabled:cursor-not-allowed"
                      @click="
                        router.push({
                          name: 'EditTask',
                          params: { taskId: item.id },
                        })
                      "
                      :disabled="!isOwner"
                    >
                      Edit
                    </button>
                  </li>
                  <li>
                    <button
                      class="itbkk-button-delete disabled:cursor-not-allowed"
                      @click="openDeletePopup(item.id)"
                      :disabled="!isOwner"
                    >
                      Delete
                    </button>
                  </li>
                </ul>
              </div>
            </div>
            <p class="text-xl font-bold">
              {{ item.id }}
            </p>
            <div class="w-full">
              <p
                @click="
                  router.push({
                    name: 'TaskDetail',
                    params: { detailId: item.id },
                  })
                "
                class="text-xl font-bold break-all duration-200 cursor-pointer itbkk-title hover:text-gray-700"
              >
                {{ item.title }}
              </p>
              <p
                class="itbkk-assignees"
                :class="item.assignees === null ? 'italic text-gray-500' : ''"
              >
                Assignees :
                {{ item.assignees === null ? "Unassigned" : item.assignees }}
              </p>
            </div>
          </div>
          <div>
            <p
              class="px-4 py-2 rounded shadow-md itbkk-status"
              :style="{ backgroundColor: item.status.statusColor }"
            >
              {{ item.status.name }}
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
