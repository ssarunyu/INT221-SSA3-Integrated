<script setup>
import { useRouter } from 'vue-router';

const router = useRouter();
const userLogin = JSON.parse(localStorage.getItem("payload"));

const logout = () => {
  localStorage.removeItem("payload");
  localStorage.removeItem("token");
  router.push('/login');
};

</script>

<template>
    <div
      class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
    >
      <div class="bg-white rounded-lg shadow-lg p-6 w-1/2">
        <!-- User Info Header -->
        <div class="flex items-center space-x-4 mb-5">
          <img
            src="@/assets/user-circle-1.svg"
            alt="User Profile"
            class="w-12 h-12 rounded-full object-cover"
          />
          <div>
            <h3 class="text-xl font-semibold text-gray-800">
              {{ userLogin.name }}
            </h3>
            <p class="text-sm text-gray-500">Role: {{ userLogin.role }}</p>
          </div>
        </div>
  
        <!-- User Details -->
        <div class="space-y-3">
          <div class="flex items-center justify-between">
            <span class="text-gray-700 font-medium">Username :</span>
            <span class="text-gray-600">{{ userLogin.sub }}</span>
          </div>
          <div class="flex items-center justify-between">
            <span class="text-gray-700 font-medium">Email :</span>
            <span class="text-gray-600">{{ userLogin.email }}</span>
          </div>
          <div class="flex items-center justify-between">
            <span class="text-gray-700 font-medium">ISS :</span>
            <span class="text-gray-600">{{ userLogin.iss }}</span>
          </div>
        </div>
  
        <!-- Buttons -->
        <div class="flex justify-end mt-6 space-x-4">
          <button
            @click="logout"
            class="flex items-center bg-red-500 text-white py-2 px-4 rounded-md hover:bg-red-600 transition"
          >
            <img src="@/assets/alert.svg" class="h-5 mr-2 fill-current text-white" alt="Logout Icon" />
            <span class="font-semibold">Logout</span>
          </button>
          <button
            @click="$emit('close')"
            class="bg-gray-300 text-gray-700 py-2 px-4 rounded-md hover:bg-gray-400 transition"
          >
            Cancel
          </button>
        </div>
      </div>
    </div>
  </template>
