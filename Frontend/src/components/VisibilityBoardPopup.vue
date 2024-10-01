<script setup>
import { ref } from 'vue'
import { useRoute } from 'vue-router';

import { defineEmits, defineProps } from 'vue'

const props = defineProps({
    isPrivate: Boolean // fix type
})
// Emit confirm and cancel events
const emit = defineEmits(['confirm', 'cancel'])

const confirmBoard = async () => {
    emit('confirm', !props.isPrivate)
}

const cancelBoard = () => {
    emit('cancel')
}

</script>

<template>
    <div class="itbkk-modal-alert fixed inset-0 bg-gray-500 bg-opacity-50 backdrop-blur-sm flex items-center justify-center z-50">
        <div class="bg-white p-6 rounded-lg shadow-lg w-96">
            <h2 class="text-xl font-bold mb-4">Confirm Visibility Change</h2>
            <p class="mb-5 text-gray-600">
                <p class="itbkk-message">Do you want to change board visibility to <span class="font-bold">{{ isPrivate ? 'public' : 'private' }}</span>?</p>
                <!-- Should couter from home page because it's change opposite -->
                
            </p>
            <div class="flex justify-end space-x-3">
                <button @click="cancelBoard"
                    class="itbkk-button-cancel bg-red-500 hover:bg-red-600 text-white font-bold py-2 px-4 rounded">
                    Cancel
                </button>
                <button @click="confirmBoard"
                    class="itbkk-button-confirm bg-green-500 hover:bg-green-600 text-white font-bold py-2 px-4 rounded">
                    Confirm
                </button>
            </div>
        </div>
    </div>
</template>
