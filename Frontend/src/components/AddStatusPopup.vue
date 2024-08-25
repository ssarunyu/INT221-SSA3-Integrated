<script setup>
import { ref, watch } from "vue";
import { postData } from "@/lib/fetchMethod";

const emit = defineEmits(["confirmAddStatus", "close", "toastItem"]);
const closeHandle = () => {
  emit("close");
};

const disabled = ref(false);
const uniqueAlert = ref(false);
const toastHandle = ref();
const addName = ref("");
const addDescription = ref(null);
const newStatus = ref({});
const statusColor = ref("#000000");//ตรงนี้ด้วย

watch(addName, (valName) => {
  if (valName.length > 50 || valName === "") {
    disabled.value = false;
  } else {
    disabled.value = true;
  }
});
watch(addDescription, (valDesc) => {
  if (valDesc.length > 200 || valDesc === "") {
    disabled.value = false;
  } else {
    disabled.value = true;
  }
});

const confirmHandle = async () => {
  const statusToCreate = {
    name: addName.value ? addName.value.trim() : null,
    description: addDescription.value ? addDescription.value.trim() : null,
    color: statusColor.value,//ตรงนี้ด้วย
  };

    //กูเพิ่มสีเอง ถ้าผิดฝากแก้ด้วย
    localStorage.setItem(`statusColor${statusToCreate.name}`, statusToCreate.color);

  const response = await postData(
    import.meta.env.VITE_STATUS_URL,
    statusToCreate
  );
  if (response.ok) {
    const createdStatus = await response.json();
    newStatus.value = {
      // Extract ID from the response
      id: createdStatus.id,
      name: createdStatus.name,
      description: createdStatus.description,
    };
    emit("confirmAddStatus", newStatus.value);
    toastHandle.value = {
      type: "success",
      status: true,
      message: `The status has been added`,
    };
    emit("toastItem", toastHandle.value);
    emit("close"); // Close modal after adding status
  } else if (!response.ok) {
    uniqueAlert.value = true;
  } else {
    toastHandle.value = {
      type: "error",
      status: true,
      message: `Failed to add status`,
    };
    emit("toastItem", toastHandle.value);
    emit("close");
  }

  // Clear form when open again
  addName.value = "";
  addDescription.value = "";

};
</script>

<template>
  <div class="fixed z-10 inset-0 overflow-y-auto">
    <div class="flex items-center justify-center h-screen">
      <div class="fixed inset-0 bg-gray-500 bg-opacity-75 backdrop-blur"></div>
      <div
        class="itbkk-modal-status relative bg-white rounded-lg shadow-xl w-[40%]"
      >
        <div class="flex flex-col p-5">
          <div
            v-if="uniqueAlert"
            class="flex items-center p-4 mb-4 text-sm text-red-700 rounded-lg bg-red-200"
            role="alert"
          >
            <svg
              class="flex-shrink-0 inline w-4 h-4 me-3"
              aria-hidden="true"
              xmlns="http://www.w3.org/2000/svg"
              fill="currentColor"
              viewBox="0 0 20 20"
            >
              <path
                d="M10 .5a9.5 9.5 0 1 0 9.5 9.5A9.51 9.51 0 0 0 10 .5ZM9.5 4a1.5 1.5 0 1 1 0 3 1.5 1.5 0 0 1 0-3ZM12 15H8a1 1 0 0 1 0-2h1v-3H8a1 1 0 0 1 0-2h2a1 1 0 0 1 1 1v4h1a1 1 0 0 1 0 2Z"
              />
            </svg>
            <span class="sr-only">Info</span>
            <div>
              <span class="font-medium"
                >Status name must be uniques, please choose another name</span
              >
            </div>
          </div>
          <form class="space-y-5" novalidate>
            <h2 class="text-2xl font-bold mb-4">Add Status</h2>
            <hr />
            <div class="flex flex-col">
              <p class="font-semibold">Name</p>
              <input
                v-model="addName"
                class="itbkk-status-name border border-black rounded p-2 peer invalid:border-red-500 focus:outline-none"
                type="text"
                required
              />
              <p class="hidden peer-invalid:block text-red-600 text-sm">
                This field required
              </p>
            </div>
            <div class="flex flex-col">
              <p class="font-semibold">Description</p>
              <input
                v-model="addDescription"
                class="itbkk-status-description border border-black rounded p-2 focus:outline-none"
                type="text"
              />
            </div>
            <!-- ตรงนี้ด้วย -->
            <div class="flex">
              <label for="statusColor">Choose Status Color:</label>
              <input
                type="color"
                id="statusColor"
                v-model="statusColor"
                class="ml-2 border rounded"
              />
            </div>
          </form>
          <div class="mt-5 space-x-5">
            <button
              @click="confirmHandle"
              :disabled="!disabled"
              class="itbkk-button-confirm disabled bg-green-500 duration-200 hover:bg-green-600 text-white font-semibold px-4 py-2 rounded disabled:bg-green-500 disabled:cursor-not-allowed disabled:opacity-50"
            >
              Save
            </button>
            <button
              @click="closeHandle"
              class="itbkk-button-cancel disabled bg-red-500 duration-200 hover:bg-red-600 text-white font-semibold px-4 py-2 rounded"
            >
              Cancel
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
