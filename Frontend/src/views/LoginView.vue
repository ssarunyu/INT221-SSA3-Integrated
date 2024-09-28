<script setup>
import { ref } from "vue";
import router from "@/router";
import { postLogin } from "@/lib/fetchMethod.js";
import { useUserStore } from "@/stores/UserStore.js";

const userStore = useUserStore();

const user = ref({});
const username = ref("");
const password = ref("");
const userIncorrect = ref("");
const userLogin = async () => {
  user.value = {
    username: username.value,
    password: password.value,
  };
  try {
    const res = await postLogin(import.meta.env.VITE_AUTH_URL, user.value);
    console.log(res);
    if (res.ok) {
      router.push({ name: "Board" });
    } else {
      userIncorrect.value = "Username or Password is incorrect";
    }
  } catch (error) {
    console.log(error);
  }
};

const showPassword = ref(false);

function togglePasswordVisibility() {
  showPassword.value = !showPassword.value;
}

</script>

<template>
  <div
    class="flex h-screen items-center justify-center bg-gradient-to-br from-blue-200 to-teal-100"
  >
    <!-- Main Container -->
    <div
      class="flex max-w-5xl w-full bg-white rounded-3xl shadow-lg overflow-hidden"
    >
      <!-- Left Section: Login Form -->
      <div
        class="flex flex-1 flex-col justify-center items-center p-10 bg-white"
      >
        <h2 class="text-3xl font-bold text-gray-800 mb-2">LOGIN</h2>
        <p class="text-gray-500 mb-8">
          Welcome to Kradan Kanban of SSA3 group !
        </p>

        <!-- Username Input -->
        <div class="w-full mb-4">
          <label
            for="username"
            class="flex items-center bg-gray-100 rounded-md p-2 mb-2"
          >
            <img
              src="@/assets/user.svg"
              alt="padlock"
              class="h-5 w-5 text-gray-400 p-0.5 opacity-70"
            />
            <input
              v-model="username"
              id="username"
              type="text"
              class="flex-1 bg-transparent outline-none"
              placeholder="Username"
              required
              maxlength="50"
            />
          </label>
        </div>

        <!-- Password Input -->
        <div class="w-full mb-4 relative">
          <label
            for="password"
            class="flex items-center bg-gray-100 rounded-md p-2 mb-2"
          >
            <!-- Key Icon -->
            <img
              src="@/assets/padlock.svg"
              alt="padlock"
              class="h-5 w-5 text-gray-400 p-0.5 opacity-70"
            />
            <!-- Password Input -->
            <input
              :type="showPassword ? 'text' : 'password'"
              v-model="password"
              id="password"
              class="flex-1 bg-transparent outline-none"
              placeholder="Password"
              required
              maxlength="14"
            />
            <!-- Eye Icon for Toggling Visibility -->
            <button
              type="button"
              @click="togglePasswordVisibility"
              class="absolute right-3"
            >
              <!-- show password -->
              <img
                v-if="showPassword"
                src="@/assets/eye-alt.svg"
                alt="padlock"
                class="h-5 w-5 text-gray-400 p-0.5 opacity-70"
              />
              <!-- blind password -->
              <img
                v-else
                src="@/assets/eye-slash-alt.svg"
                alt="padlock"
                class="h-5 w-5 text-gray-400 p-0.5 opacity-70"
              />
            </button>
          </label>
        </div>

        <!-- Login Button -->
        <button
          @click="userLogin"
          :disabled="!username || !password"
          class="w-full py-2 bg-sky-600 text-white font-bold rounded-md hover:bg-sky-700 disabled:bg-sky-200"
        >
          Login Now
        </button>
      </div>

      <!-- Right Section: Image -->
      <div class="flex flex-1 items-center justify-center">
        <div class="bg-white rounded-2xl p-2">
          <img
            src="../assets/abyan-athif-K0U0eSAjFGU-unsplash.jpg"
            alt="login image"
            class="rounded-r-xl object-cover h-full"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
button {
  font-weight: bold;
}
</style>
