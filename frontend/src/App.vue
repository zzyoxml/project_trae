<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { getToken, removeToken } from '@/utils/auth'

const router = useRouter()
const userStore = useUserStore()

onMounted(async () => {
  if (userStore.token) {
    try {
      await userStore.getUserInfo()
    } catch (error) {
      console.warn('Token已失效，已自动清除')
      userStore.resetToken()
      if (router.currentRoute.path !== '/login') {
        router.push('/login')
      }
    }
  }
})
</script>

<style lang="scss">
#app {
  width: 100%;
  height: 100%;
  min-height: 100vh;
  background-color: $bg-color;
  color: $text-color;
  font-family: $font-family;
}
</style>
