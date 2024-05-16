<script setup>
import { getDataById, updateData } from "@/lib/fetchMethod";
import { onMounted, ref } from "vue"
import router from '@/router';
import { useRoute } from 'vue-router';
const route = useRoute()
const disabled = ref(true)
const itemData = ref()
const toastHandle = ref()
const emit = defineEmits(['updateStatus', 'toastItem'])

onMounted(async () => {
  const response = await getDataById(import.meta.env.VITE_STATUS_URL, route.params.editStatusId)
  if(response.status === 404) {
    router.push({ name: 'StatusView' })
    toastHandle.value = { type: 'error', status: true, message: `An error has occurred, the status does not exist` }
    emit('toastItem', toastHandle.value)
  } else {
    itemData.value = await response
  }
})

const updateHandle = async () => {
  itemData.value.name = itemData.value.name ? itemData.value.name.trim() : itemData.value.name
  itemData.value.description = itemData.value.description ? itemData.value.description.trim() : null
  const response = await updateData(import.meta.env.VITE_STATUS_URL, itemData.value, route.params.editStatusId)
  if(response.ok) {
    emit('updateStatus', itemData.value)
    router.push({ name : 'StatusView' })
  }
}

const closeHandle = () => {
  router.go(-1)
}
</script>

<template>
  <div v-if="itemData" class="fixed z-10 inset-0 overflow-y-auto">
    <div class="flex items-center justify-center h-screen">
      <!-- Overlay -->
      <div class="fixed inset-0 bg-gray-500 bg-opacity-75 backdrop-blur"></div>
      <!-- Popup -->
      <div class="itbkk-modal-status relative bg-white rounded-lg shadow-xl w-[70%]">
        <!-- Popup content -->
        <div class="flex flex-col p-5">
          <form class="space-y-5">
              <h2 class="text-2xl font-bold mb-4">Edit Task</h2>
              <hr>
              <div class="flex flex-col">
                  <p class="font-semibold">Name</p>
                  <input v-model="itemData.name"  @input="disabled = false" class="itbkk-status-name border border-black rounded p-2 peer invalid:border-red-500 focus:outline-none" type="text" required>
                  <p class="hidden peer-invalid:block text-red-600 text-sm">
                    This field required
                  </p>
              </div>
              <div class="flex flex-col">
                  <p class="font-semibold">Description</p>
                  <input v-model="itemData.description" @input="disabled = false" class="itbkk-status-description border border-black rounded p-2 focus:outline-none" type="text" :placeholder="itemData.description === null ? 'No Description Provided' : ''">
              </div>
            </form>
            <div class="itbkk-button-action mt-5 space-x-5">
                <button :disabled="disabled" class="itbkk-button-confirm bg-green-500 duration-200 hover:bg-green-600 text-white font-semibold px-4 py-2 rounded disabled:cursor-not-allowed disabled:opacity-50"
                @click="updateHandle()">
                  Save
                </button>
                <button class="itbkk-button-cancel bg-red-500 duration-200 hover:bg-red-600 text-white font-semibold px-4 py-2 rounded"
                @click="closeHandle()">
                  Cancel
                </button>
            </div>
        </div>
      </div>
    </div>
  </div>
</template>