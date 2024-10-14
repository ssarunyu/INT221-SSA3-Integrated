<script setup>
import { useRouter } from 'vue-router';

const router = useRouter();
const userLogin = JSON.parse(localStorage.getItem("payload"));

const logout = () => {
  localStorage.removeItem("payload");
  localStorage.removeItem("token");
  router.push({ name: 'Login' });
};

</script>

<template>
    <div
      class="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50"
    >
      <div class="w-1/2 p-6 bg-white rounded-lg shadow-lg">
        <!-- User Info Header -->
        <div class="flex items-center mb-5 space-x-4">
          <img
            src="@/assets/profile-user-b.svg"
            alt="User Profile"
            class="object-cover w-12 h-12 rounded-full shadow-lg"
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
            <span class="font-medium text-gray-700">Username :</span>
            <span class="text-gray-600">{{ userLogin.sub }}</span>
          </div>
          <div class="flex items-center justify-between">
            <span class="font-medium text-gray-700">Email :</span>
            <span class="text-gray-600">{{ userLogin.email }}</span>
          </div>
          <div class="flex items-center justify-between">
            <span class="font-medium text-gray-700">ISS :</span>
            <span class="text-gray-600">{{ userLogin.iss }}</span>
          </div>
        </div>
  
        <!-- Buttons -->
        <div class="flex justify-end mt-6 space-x-4">
          <button
            @click="logout"
            class="flex items-center px-4 py-2 text-white transition bg-red-500 rounded-md hover:bg-red-600"
          >
            <img src="@/assets/alert.svg" class="h-5 mr-2 text-white fill-current" alt="Logout Icon" />
            <span class="font-semibold">Logout</span>
          </button>
          <button
            @click="$emit('close')"
            class="px-4 py-2 text-gray-700 transition bg-gray-300 rounded-md hover:bg-gray-400"
          >
            Cancel
          </button>
        </div>
      </div>
    </div>
  </template>
