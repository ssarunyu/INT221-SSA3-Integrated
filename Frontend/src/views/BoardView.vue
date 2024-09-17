<script setup>
</script>

<template>
    <div class="w-full h-screen">
        <!-- TITLE -->
        <div class="title">
            <h1 class="text-2xl font-bold p-5">ITBKK-SSA3 Board</h1>
        </div>
        <!-- CARDS SECTION -->
        <div class="flex flex-col justify-center mt-5 items-center space-y-5">
            <!-- Each Card -->
            <div class="w-1/2 bg-white shadow-md rounded-lg cursor-pointer duration-300 hover:bg-slate-100">
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
            <div class="w-1/2 bg-white shadow-md rounded-lg cursor-pointer duration-300 hover:bg-slate-100">
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
            <div class="w-1/2 bg-white shadow-md rounded-lg cursor-pointer duration-300 hover:bg-slate-100">
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
        </div>
    </div>
</template>