<script setup>
import { ref } from "vue"

const props = defineProps({
    itemData: {
        type: Object
    }
})

const disabled = ref(true)
const emit = defineEmits(['close', 'update'])
const closeHandle = () => {
    disabled.value = true
    emit('close')
}

const updateHandle = () => {
  props.itemData.name = props.itemData.name ? props.itemData.name.trim() : null
  props.itemData.description = props.itemData.description ? props.itemData.description.trim() : null
  emit('update', props.itemData)
  disabled.value = true
}
</script>

<template>
  <div v-if="props.itemData" class="fixed z-10 inset-0 overflow-y-auto">
    <div class="flex items-center justify-center h-screen">
      <!-- Overlay -->
      <div class="fixed inset-0 bg-gray-500 bg-opacity-75 backdrop-blur"></div>
      <!-- Popup -->
      <div class="relative bg-white rounded-lg shadow-xl w-[70%]">
        <!-- Popup content -->
        <div class="flex flex-col p-5">
          <form class="space-y-5">
              <h2 class="text-2xl font-bold mb-4">Edit Task</h2>
              <hr>
              <div class="flex flex-col">
                  <p class="font-semibold">name</p>
                  <input @input="disabled = false" class="itbkk-status-name border border-black rounded p-2 peer invalid:border-red-500 focus:outline-none" type="text" v-model="itemData.name" required>
                  <p class="hidden peer-invalid:block text-red-600 text-sm">
                    This field required
                  </p>
              </div>
              <div class="flex flex-col">
                  <p class="font-semibold">Description</p>
                  <input @input="disabled = false" class="itbkk-description border border-black rounded p-2 focus:outline-none" type="text" v-model="itemData.description" :placeholder="itemData.description === null ? 'No Description Provided' : ''">
              </div>
            </form>
            <div class="itbkk-button-action mt-5 space-x-5">
                <button :disabled="disabled" class="itbkk-button-edit bg-green-500 duration-200 hover:bg-green-600 text-white font-semibold px-4 py-2 rounded disabled:cursor-not-allowed disabled:opacity-50"
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