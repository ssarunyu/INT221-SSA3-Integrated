<script setup>
const props = defineProps({
  item: {
    type: Object
  }
})

</script>

<template>
  <div>
    <h1 class="itbkk-title text-2xl font-bold break-all">
      {{ item.title }}
    </h1>
    <hr class="h-px my-4 bg-gray-200 border-0">
    <div class="flex flex-col mt-5 space-y-5">
      <!-- Desc -->
      <div>
        <p class="font-lg font-bold">Description</p>
        <p class="itbkk-description rounded break-all" :class="item.description === null ? 'text-gray-500 italic' : ''">
          {{ item.description === null ? 'No Description Provided' : item.description }}
        </p>
      </div>
      <!-- Ass -->
      <div>
        <p class="text-lg font-bold" for="">Assignees</p>
        <p class="itbkk-assignees" :class="item.assignees === null ? 'text-gray-500 italic' : ''">
          {{ item.assignees === null ? 'Unassigned' : item.assignees }}
        </p>
      </div>
      <!-- Status -->
      <div>
        <p class="text-lg font-bold" for="">Status</p>
        <p>{{ item.status }}</p>
      </div>
    </div>
    <hr class="h-px my-5 bg-gray-200 border-0">
    <div>
        <p class="text-lg font-bold">Time Details</p>
        <p class="itbkk-timezone"><strong>Timezone </strong>{{ Intl.DateTimeFormat().resolvedOptions().timeZone }}</p>
        <p class="itbkk-created-on"><strong>Created on </strong>{{ item.createdOn }}</p>
        <p class="itbkk-updated-on"><strong>Updated on </strong>{{ item.updatedOn }}</p>
    </div>
  </div>
</template>