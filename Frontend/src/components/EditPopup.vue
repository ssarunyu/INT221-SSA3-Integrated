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
  props.itemData.title = props.itemData.title ? props.itemData.title.trim() : null
  props.itemData.description = props.itemData.description ? props.itemData.description.trim() : null
  props.itemData.assignees = props.itemData.assignees ? props.itemData.assignees.trim() : null
  emit('update', props.itemData)
  disabled.value = true
}
</script>

<template>
  <div class="fixed z-10 inset-0 overflow-y-auto">
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
                  <p class="font-semibold">Title</p>
                  <input @input="disabled = false" class="itbkk-title border border-black rounded p-2 peer invalid:border-red-500 focus:outline-none" type="text" v-model="itemData.title" required>
                  <p class="hidden peer-invalid:block text-red-600 text-sm">
                    This field required
                  </p>
              </div>
              <div class="flex flex-col">
                  <p class="font-semibold">Description</p>
                  <input @input="disabled = false" class="itbkk-description border border-black rounded p-2 focus:outline-none" type="text" v-model="itemData.description" :placeholder="itemData.description === null ? 'No Description Provided' : ''">
              </div>
              <div class="flex flex-col">
                  <p class="font-semibold">Assignees</p>
                  <input @input="disabled = false" class="itbkk-assignees border border-black rounded p-2 focus:outline-none" type="text" v-model="itemData.assignees" :placeholder="itemData.assignees === null ? 'Unassigned' : ''">
              </div>
              <div class="flex items-center space-x-3 ">
                  <p for="">Status</p>
                  <select @input="disabled = false" class="itbkk-status rounded px-3 py-1 border border-gray-300" v-model="itemData.status" name="" id="">
                    <option value="NO_STATUS">No Status</option>
                    <option value="TO_DO">To Do</option>
                    <option value="DOING">Doing</option>
                    <option value="DONE">Done</option>
                  </select>
              </div>
              <!-- FIXED: Date -->
              <div class="flex flex-col">
                <p><strong>Timezone</strong> {{ Intl.DateTimeFormat().resolvedOptions().timeZone }}</p>
                <p><strong>Created On</strong> {{ itemData.createdOn }}</p>
                <p><strong>Updated On</strong> {{ itemData.updatedOn }}</p>
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