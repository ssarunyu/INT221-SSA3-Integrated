import { defineStore } from "pinia";
import { reactive } from "vue";

// Task Store
export const useTaskStore = defineStore("taskStore", () => {
  const state = reactive({
    allTasks: []
  });

  function addTask(item) {
   state.allTasks.push(item);
  }

  function addAllTask(items) {
    items.forEach((i) => {
      state.allTasks.push(i);
    });
  }

  function updateTask(task, id) {
    const index = state.allTasks.findIndex((item) => item.id === id);
    if (index !== -1) {
      state.allTasks[index] = task;
    }
  }

  function deleteTask(id) {
    const index = state.allTasks.findIndex((item) => item.id === id);
    if (index !== -1) {
      state.allTasks.splice(index, 1);
    }
  }

  function getAllTask() {
    return state.allTasks;
  }

  return { state, addTask, addAllTask, updateTask, deleteTask, getAllTask };
});
