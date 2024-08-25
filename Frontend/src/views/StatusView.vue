<script setup>
import { onMounted, ref } from "vue";
import router from "@/router";
import { styleStatus } from "@/lib/styleStatus";
import { getData, deleteData } from "@/lib/fetchMethod.js";
import AddStatusPopup from "@/components/AddStatusPopup.vue";
import Toast from "@/components/Toast.vue";
import DeleteStatusPopup from "@/components/DeleteStatusPopup.vue";
import TransferDeleteStatusPopup from "@/components/TransferDeleteStatusPopup.vue";

// Store
import { useStatusStore } from "@/stores/StatusStore.js";

const statusManagement = useStatusStore();

const fetch = async () => {
  // Fetch data
  const response = await getData(import.meta.env.VITE_STATUS_URL);
  // Front-end
  statusManagement.addAllStatus(response);
};

onMounted(async () => {
  await fetch();
});

// Toast
const toastHandle = ref();

const statuses = ref(statusManagement.getAllStatus());
const addStatusShow = ref(false);
const normalDeleteStatusShow = ref(false);
const transferDeleteStatusShow = ref(false);
const deleteTarget = ref();

const controlAddStatus = (newStatus) => {
  statusManagement.addStatus(newStatus);
};
const controlUpdateStatus = (updateNewStatus) => {
  statusManagement.updateStatus(updateNewStatus, updateNewStatus.id);
};
const controlToast = (newToast) => {
  toastHandle.value = newToast;
};
const sendDeleteStatus = async (obj) => {
  // Get all task already use that status
  const getAllTask = await getData(import.meta.env.VITE_TASK_URL);
  const filterRepeatTask = getAllTask.filter((a) => a.status.id === obj.id);
  if (filterRepeatTask.length > 0) {
    transferDeleteStatusShow.value = true;
    // NOTE: Transfer
    deleteTarget.value = obj;
  } else {
    // NOTE: Normal delete
    normalDeleteStatusShow.value = true;
    deleteTarget.value = obj;
  }
};
const controlDelete = async (statusId) => {
  statusManagement.deleteStatus(statusId.id);
};
</script>

<template>
  <AddStatusPopup
    v-if="addStatusShow"
    @confirmAddStatus="controlAddStatus"
    @close="addStatusShow = false"
    @toastItem="controlToast"
  />
  <DeleteStatusPopup
    v-if="normalDeleteStatusShow"
    :deleteItem="deleteTarget"
    @confirmDeleteStatus="controlDelete"
    @close="normalDeleteStatusShow = false"
    @toastItem="controlToast"
  />
  <TransferDeleteStatusPopup
    v-if="transferDeleteStatusShow"
    :deleteItem="deleteTarget"
    @confirmDeleteStatus="controlDelete"
    @close="transferDeleteStatusShow = false"
    @toastItem="controlToast"
  />
  <router-view
    @updateStatus="controlUpdateStatus"
    @toastItem="controlToast"
  ></router-view>
  <div class="w-full min-h-screen p-5">
    <div class="p-5 flex justify-between">
      <div>
        <h1 class="text-2xl font-bold mb-5">ITBKK SSA3 Taskboard</h1>
      </div>
      <div class="flex items-center">
        <h1 class="mr-3">profile_name</h1>
        <img src="../assets/profile-icon.png" alt="profile_pic" width="40" height="40">
      </div>
    </div>
    <div class="flex justify-center items-center">
      <div>
        <h3>Hi, <span class="font-bold">Arin</span> Here are all your statuses.</h3>
      </div>
    </div>
    <div class="flex w-full justify-between">
      <div class="flex p-3">
        <router-link
          :to="{ name: 'Home' }"
          class="itbkk-button-home hover:text-blue-500 hover:scale-105 ml-1"
          >Home</router-link
        >
        <p class="font-bold text-gray-400 ml-1">></p>
        <router-link
          :to="{ name: 'StatusView' }"
          class="font-bold ml-1 hover:scale-105"
          >Task Status</router-link
        >
      </div>
      <div
        @click="addStatusShow = true"
        class="itbkk-button-add p-2 w-35 m-1 rounded cursor-pointer duration-300 bg-gray-300 hover:bg-gray-400 hover:scale-105"
      >
        + Add Status
      </div>
    </div>
    <div
      class="grid grid-cols-4 font-xl font-bold text-white border-b border-gray-300 p-3 bg-blue-400 rounded"
    >
      <p>ID</p>
      <p>Name</p>
      <p>Description</p>
      <p>Action</p>
    </div>
    <Toast :toastObject="toastHandle" @close="toastHandle.status = false" />
    <div class="space-y-5 mt-5">
      <div
        v-for="item in statuses"
        class="itbkk-item relative p-2 border rounded"
      >
        <div class="grid grid-cols-4 items-center">
          <!-- Vertical line -->
          <div
            class="absolute left-0 w-1 h-10"
            :class="styleStatus(item.name)"
          ></div>
          <div class="text-lg font-bold text-center">
            {{ item.id }}
          </div>
          <div
            class="itbkk-status-name font-medium text-lg px-4 mx-4 py-2 rounded break-all"
            :class="styleStatus(item.name)"
          >
            {{ item.name }}
          </div>
          <div
            class="itbkk-status-description text-lg m-2 break-all"
            :class="item.description === null ? 'italic text-gray-500' : ''"
          >
            {{
              item.description === null
                ? "No description is provided"
                : item.description
            }}
          </div>
          <div
            class="itbkk-status-action flex"
            v-if="item.name !== 'No Status' && item.name !== 'Done'"
          >
            <div
              @click="
                router.push({
                  name: 'EditStatusPopup',
                  params: { editStatusId: item.id },
                })
              "
              class="itbkk-button-edit p-2 px-5 w-35 text-center m-1 rounded cursor-pointer duration-300 bg-gray-300 hover:bg-gray-400 hover:scale-105"
            >
              Edit
            </div>
            <div
              @click="sendDeleteStatus(item)"
              class="itbkk-button-delete p-2 px-3 w-35 m-1 rounded cursor-pointer text-center duration-300 bg-gray-300 hover:bg-gray-400 hover:scale-105"
            >
              Delete
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
