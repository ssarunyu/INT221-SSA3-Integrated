<script setup>
import { TaskManagement } from '@/lib/TaskManagement.js'
import { styleStatus } from '@/lib/styleStatus';

const status = [
  { id: 1, name: 'No Status', description: 'Noooooooo' },
  { id: 2, name: 'To Do', description: 'Noooooooo'  },
  { id: 3, name: 'Doing', description: 'Noooooooo'  },
  { id: 4, name: 'Done', description: 'Nooooooooawdadawdwadawdadawdawdawdawdawdawdawdadwadwadawdadawdad' }
];

// const taskManagement = ref(new TaskManagement())

//Add Status

//Edit Status

</script>

<template>
    <div class="w-full min-h-screen p-5">
        <h1 class="flex text-2xl font-bold justify-center mb-5">ITBKK SSA3 Taskboard</h1>
        <div class="flex w-full justify-between">
            <div class="flex p-3">
                <router-link to="/task" class="itbkk-button-home hover:text-blue-500 hover:scale-105 ml-1">Home</router-link>
                <p class="font-bold text-gray-400 ml-1">></p>
                <router-link to="/task/statuses" class="font-bold ml-1 hover:scale-105">Task Status</router-link>
            </div>
            <div @click="addStatus()" class="itbkk-button-add p-2 w-35 m-1 rounded cursor-pointer duration-300 bg-gray-300 hover:bg-gray-400 hover:scale-105">
                + Add Status
            </div>
        </div>
        <div class="flex w-full justify-between font-xl font-bold text-white border-b border-gray-300 p-3 bg-blue-400 rounded">
                <p>ID</p>
                <p>Name</p>
                <p>Description</p>
                <p>Action</p>
        </div>
        <div v-for="item in status" class="itbkk-item relative p-3 rounded">
            <div class="absolute left-0 w-1 h-10" :class="styleStatus(item.name)"></div>
            <div class="grid grid-cols-10 items-center">
                        <div class="text-lg font-bold col-span-1 text-center">{{ item.id }}</div>
                        <div class="itbkk-status-name font-medium text-lg col-span-1 px-4 mx-4 py-2 rounded" :class=styleStatus(item.name)>{{ item.name }}</div>
                        <div class="itbkk-status-description text-lg col-span-7">{{ item.description }}</div>
            <div class="itbkk-status-action flex col-span-1 " v-if="item.name !== 'No Status'">
                <div @click="editStatus()" class="itbkk-button-edit p-2 px-5 w-35 m-1 rounded cursor-pointer duration-300 bg-gray-300 hover:bg-gray-400 hover:scale-105">
                    Edit
                </div>
                <div @click="deleteStatus()" class="itbkk-button-delete p-2 px-3 w-35 m-1 rounded cursor-pointer duration-300 bg-gray-300 hover:bg-gray-400 hover:scale-105">
                    Delete
                </div>
            </div>
        </div>
    </div>
    </div>
</template>