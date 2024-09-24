<script setup>
import router from '@/router';

// const addBoardPopup = ref(false)
const openBoardPopup = () => {
    addBoardPopup.value = true
}
const closeBoardPopup = () => {
    addBoardPopup.value = false
}
</script>

<template>
    <div class="w-full h-screen">
        <!-- TITLE -->
        <div class="title bg-slate-800 text-white">
            <h1 class="text-2xl font-bold p-5 text-center">ITBKK-SSA3 Board</h1>
        </div>
        <div class="w-full flex flex-col justify-between items-center space-y-5">
            <div @click="openBoardPopup"
                class="itbkk-button-add rounded-md p-5 bg-slate-200 text-slate-500 cursor-pointer duration-300 hover:bg-slate-300 hover:text-slate-700 ">
                + Add New Board
            </div>
        </div>
        <!-- CARDS SECTION -->
        <div class="flex flex-wrap justify-center mt-5 items-center gap-5">
            <!-- Each Card -->
            <div class="w-1/4 bg-white shadow-md rounded-lg cursor-pointer duration-300 hover:bg-slate-100">
                <div class="p-5 flex justify-between items-center">
                    <div>
                        <a href="#">
                            <h5 class="text-2xl font-bold tracking-tight text-gray-900">Board 1</h5>
                        </a>
                    </div>
                    <div class="flex flex-col items-center rounded">
                        <h5 class="text-xs my-2">Total Tasks</h5>
                        <h5 class="font-bold text-xl">21</h5>
                    </div>
                </div>
            </div>
            <!-- Each Card -->
            <div class="w-1/4 bg-white shadow-md rounded-lg cursor-pointer duration-300 hover:bg-slate-100">
                <div class="p-5 flex justify-between items-center">
                    <div>
                        <a href="#">
                            <h5 class="text-2xl font-bold tracking-tight text-gray-900">Board 2</h5>
                        </a>
                    </div>
                    <div class="flex flex-col items-center rounded">
                        <h5 class="text-xs my-2">Total Tasks</h5>
                        <h5 class="font-bold text-xl">1</h5>
                    </div>
                </div>
            </div>
            <!-- Each Card -->
            <div class="w-1/4 bg-white shadow-md rounded-lg cursor-pointer duration-300 hover:bg-slate-100">
                <div class="p-5 flex justify-between items-center">
                    <div>
                        <a href="#">
                            <h5 class="text-2xl font-bold tracking-tight text-gray-900">Board 3</h5>
                        </a>
                    </div>
                    <div class="flex flex-col items-center rounded">
                        <h5 class="text-xs my-2">Total Tasks</h5>
                        <h5 class="font-bold text-xl">101</h5>
                    </div>
                </div>
            </div>
            <!-- Each Card -->
            <div class="w-1/4 bg-white shadow-md rounded-lg cursor-pointer duration-300 hover:bg-slate-100">
                <div class="p-5 flex justify-between items-center">
                    <div>
                        <a href="#">
                            <h5 class="text-2xl font-bold tracking-tight text-gray-900">Board 4</h5>
                        </a>
                    </div>
                    <div class="flex flex-col items-center rounded">
                        <h5 class="text-xs my-2">Total Tasks</h5>
                        <h5 class="font-bold text-xl">45</h5>
                    </div>
                </div>
            </div>
            <!-- Each Card -->
            <div class="w-1/4 bg-white shadow-md rounded-lg cursor-pointer duration-300 hover:bg-slate-100">
                <div class="p-5 flex justify-between items-center">
                    <div>
                        <a href="#">
                            <h5 class="text-2xl font-bold tracking-tight text-gray-900">Board 5</h5>
                        </a>
                    </div>
                    <div class="flex flex-col items-center rounded">
                        <h5 class="text-xs my-2">Total Tasks</h5>
                        <h5 class="font-bold text-xl">30</h5>
                    </div>
                </div>
            </div>
            <!-- Each Card -->
            <div class="w-1/4 bg-white shadow-md rounded-lg cursor-pointer duration-300 hover:bg-slate-100">
                <div class="p-5 flex justify-between items-center">
                    <div>
                        <a href="#">
                            <h5 class="text-2xl font-bold tracking-tight text-gray-900">Board 6</h5>
                        </a>
                    </div>
                    <div class="flex flex-col items-center rounded">
                        <h5 class="text-xs my-2">Total Tasks</h5>
                        <h5 class="font-bold text-xl">18</h5>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
