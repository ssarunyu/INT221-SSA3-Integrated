import { defineStore } from "pinia";
import { reactive } from "vue";

// Status Store
export const useStatusStore = defineStore("statusStore", () => {
  const state = reactive({
    allStatus: []
  });

  function addStatus(item) {
    state.allStatus.push(item);
  }

  function addAllStatus(items) {
    items.forEach((i) => {
      state.allStatus.push(i);
    });
  }

  function updateStatus(status, id) {
    const index = state.allStatus.findIndex((item) => item.id === id);
    if (index !== -1) {
      state.allStatus[index] = status;
    }
  }

  function deleteStatus(id) {
    const index = state.allStatus.findIndex((item) => item.id === id);
    if (index !== -1) {
      state.allStatus.splice(index, 1);
    }
  }

  function getAllStatus() {
    return state.allStatus;
  }

  return { state, addStatus, addAllStatus, updateStatus, deleteStatus, getAllStatus };
});
