import { defineStore } from "pinia"
import { reactive } from "vue"
import { getData } from '@/lib/fetchMethod'

// Task Store
export const useTaskStore = defineStore("taskStore", () => {
  const state = reactive({
    allTasks: []
  })

  function addTask(item) {
    state.allTasks.push(item)
  }

  function addAllTask(items) {
    items.forEach((i) => {
      state.allTasks.push(i)
    })
  }

  function updateTask(task, id) {
    const index = state.allTasks.findIndex((item) => item.id === id)
    if (index !== -1) {
      state.allTasks[index] = task
    }
  }

  function deleteTask(id) {
    const index = state.allTasks.findIndex((item) => item.id === id)
    if (index !== -1) {
      state.allTasks.splice(index, 1)
    }
  }

  function getAllTask() {
    return state.allTasks
  }

  async function fetchTasks(boardId) {
    try {
      const response = await getData(`http://localhost:8080/v3/boards/${boardId}/tasks`)
      state.allTasks = response
    } catch (error) {
      console.error("Error fetching tasks:", error)
    }
  }

  return { state, addTask, addAllTask, updateTask, deleteTask, getAllTask, fetchTasks}
})
