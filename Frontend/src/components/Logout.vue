<script setup>
import router from '@/router';

const logout => () {
    localStorage.clear()

    router.push('/login')}
</script>
 
<template>
    <div>
        <button @click="logout" class="itbkk-sign-out">Logout</button>
    </div>
</template>
 
