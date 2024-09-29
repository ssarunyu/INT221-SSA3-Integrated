<script setup>
import { ref, watch, onMounted, computed } from "vue"
import { getData } from "@/lib/fetchMethod.js"
import { useRoute } from "vue-router"
import Toast from "@/components/Toast.vue"
import { useTaskStore } from "@/stores/TaskStore.js"
import { useStatusStore } from "@/stores/StatusStore.js"

const route = useRoute()
const userAuthItem = JSON.parse(localStorage.getItem("payload"))

// State variables
const allTasks = ref([])
const allStatuses = ref([])
const filterSelect = ref([])
const toastHandle = ref()
const sortStage = ref(0)

const taskStore = useTaskStore();
const tasks = computed(() => taskStore.tasks);

const addTaskToList = (newTask) => {
  taskStore.addTask(newTask) // เพิ่ม task ใหม่ไปยัง taskStore
  allTasks.value.push(newTask)
  console.log('New task added:', newTask)
}

const fetch = async () => {
  try {
    const responseTask = await getData(
      `${import.meta.env.VITE_BASE_URL}/v3/boards/${route.params.boardId}/tasks`
    )
    console.log("Fetched Tasks:", responseTask)
    allTasks.value = responseTask

    const responseStatus = await getData(
      `${import.meta.env.VITE_BASE_URL}/v3/boards/${route.params.boardId}/statuses`
    )
    console.log("Fetched Statuses:", responseStatus)
    allStatuses.value = responseStatus

    console.log("All Tasks:", allTasks.value)
    console.log("All Statuses:", allStatuses.value)
  } catch (error) {
    console.error("Error fetching tasks or statuses:", error)
  }
}

watch(allTasks, newTasks => {
  newTasks.forEach(task => {
    switch (task.status) {
      case "TO_DO":
        task.status = "To Do"
        break
      case "NO_STATUS":
        task.status = "No Status"
        break
      case "DOING":
        task.status = "Doing"
        break
      case "DONE":
        task.status = "Done"
        break
      default:
        break
    }
  })
})

onMounted(async () => {
  await fetch()
})

const sortTask = computed(() => {
  const copyTask = [...allTasks.value]
  const sortFunctions = {
    0: tasks => tasks,
    1: tasks => tasks.sort((a, b) => a.status.name.localeCompare(b.status.name)),
    2: tasks => tasks.sort((a, b) => b.status.name.localeCompare(a.status.name)),
  }
  return sortFunctions[sortStage.value](copyTask)
})

const sortIcon = computed(() => {
  const icons = {
    0: "fa-solid fa-sort",
    1: "fa-solid fa-sort-down",
    2: "fa-solid fa-sort-up",
  }
  return icons[sortStage.value]
})

const submitFilter = async userClick => {
  const findExist = filterSelect.value.indexOf(userClick)
  if (findExist !== -1) {
    filterSelect.value.splice(findExist, 1)
  } else {
    filterSelect.value.push(userClick)
  }
  await updateTasks()
}

const updateTasks = async () => {
  let userFilter = ""
  if (filterSelect.value.length) {
    userFilter = filterSelect.value.map(a => `filterStatuses=${a}`).join("&")
  }
  try {
    const response = await getData(
      `${import.meta.env.VITE_BASE_URL}/v3/boards/${route.params.boardId}/tasks?${userFilter}`
    )
    allTasks.value = response // ใช้ allTasks แทน tasks
  } catch (error) {
    console.error("Error updating tasks:", error)
  }
}

const clearFilter = async () => {
  filterSelect.value = []
  await fetch() // Re-fetch all tasks without filters
}

const removeFilter = async r => {
  const findExist = filterSelect.value.indexOf(r)
  if (findExist !== -1) {
    filterSelect.value.splice(findExist, 1)
    await updateTasks()
  }
}

const changeSortStage = () => {
  sortStage.value = (sortStage.value + 1) % 3 // Cycle through sort stages
}
</script>

<template>
  <router-view @task-added="addTaskToList"></router-view>
  <div class="w-full min-h-screen bg-gray-50">
    <!-- Nav -->
    <nav class="flex justify-between items-center bg-slate-800 p-4">
      <h1 class="text-xl font-semibold text-white">ITBKK-SSA3 Board</h1>
      <div class="text-right text-white">
        <p class="font-medium">{{ userAuthItem.name }}</p>
        <p class="text-xs">{{ userAuthItem.email }}</p>
      </div>
    </nav>

    <div class="p-6">
      <!-- Status Page button -->
      <div class="flex justify-end mb-4">
        <button
          @click="$router.push({ name: 'StatusView' })"
          class="text-blue-500 underline"
        >
          Status Page
        </button>
      </div>

      <!-- Table Header -->
      <div
        class="flex items-center justify-between bg-slate-600 text-white px-4 py-2 rounded"
      >
        <p>Title</p>
        <p>Assignees</p>
        <div class="flex items-center">
          <p>Status</p>
          <div @click="changeSortStage()" class="ml-2 cursor-pointer">
            <font-awesome-icon v-if="sortIcon" :icon="sortIcon" />
          </div>
        </div>
      </div>

      <!-- Filter -->
      <div class="flex">
        <div class="itbkk-status-filter dropdown dropdown-bottom">
          <div tabindex="0" role="button" class="btn m-1">Filter Tasks</div>
          <ul
            tabindex="0"
            class="dropdown-content z-[1] menu p-2 shadow bg-white rounded-box w-52"
          >
            <div class="text-blue-500 underline flex justify-end w-full my-2">
              <p class="cursor-pointer" @click="clearFilter">Clear all</p>
            </div>
            <div
              v-for="status in allStatuses"
              :key="status.id"
              class="flex justify-between p-2 rounded duration-300 hover:bg-gray-100"
            >
              <button
                class="itbkk-status-choice"
                @click="submitFilter(status.name)"
              >
                {{ status.name }}
              </button>
            </div>
          </ul>
        </div>
        <!-- Show already filtered -->
        <div class="flex items-center space-x-3 ml-4">
          <div
            v-for="status in filterSelect"
            :key="status"
            class="px-5 py-2 rounded bg-blue-200"
          >
            {{ status }}
            <font-awesome-icon
              class="cursor-pointer"
              @click="removeFilter(status)"
              icon="fa-solid fa-xmark"
            />
          </div>
        </div>
      </div>

      <!-- Add Task Button -->
      <div class="flex justify-center mt-6">
        <button
          @click="$router.push({ name: 'AddTask' })"
          class="w-2/3 py-3 bg-slate-200 text-slate-700 rounded-md hover:bg-slate-300 transition"
        >
          + Add New Task
        </button>
      </div>

      <!-- Task List -->
      <div class="mt-6 space-y-4">
        <div
          v-for="item in sortTask"
          :key="item.id"
          class="flex justify-between items-center p-4 bg-white shadow-sm rounded-md"
        >
          <div class="flex items-center space-x-3">
            <div>
              <div class="dropdown itbkk-button-action">
                <p
                  tabindex="0"
                  role="button"
                  class="btn m-1 border-none font-bold text-2xl"
                >
                  :
                </p>
                <ul
                  tabindex="0"
                  class="dropdown-content z-[1] menu p-2 shadow bg-white rounded-box w-40"
                >
                  <li>
                    <a
                      class="itbkk-button-edit"
                      @click="
                        $router.push({
                          name: 'EditTask',
                          params: { taskId: item.id },
                        })
                      "
                      >Edit</a
                    >
                  </li>
                  <li>
                    <a class="itbkk-button-delete">Delete</a>
                  </li>
                </ul>
              </div>
            </div>
            <div class="text-lg font-semibold">{{ item.id }}</div>
            <div>
              <p
                @click="
                  $router.push({
                    name: 'TaskDetail',
                    params: { detailId: item.id },
                  })
                "
                class="text-slate-700 font-semibold cursor-pointer"
              >
                {{ item.title }}
              </p>
              <p class="text-sm text-gray-500">
                Assignees:
                {{ item.assignees === null ? "Unassigned" : item.assignees }}
              </p>
            </div>
          </div>
          <div>
            <p
              class="px-4 py-1 text-black rounded"
              :class="`bg-[${item.status.statusColor}]`"
            >
              {{ item.status.name }}
            </p>
          </div>
        </div>
      </div>

      <!-- Toast Component -->
      <Toast :toastObject="toastHandle" @close="toastHandle.status = false" />
    </div>
  </div>
</template>
