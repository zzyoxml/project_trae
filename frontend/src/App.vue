<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { getLanguage } from '@/utils/auth'

/**
 * 应用根组件
 * 
 * @description 应用的顶层组件，负责初始化用户状态
 */
const userStore = useUserStore()

// 初始化应用
onMounted(async () => {
  // 获取用户信息
  if (userStore.token) {
    try {
      await userStore.getUserInfo()
    } catch (error) {
      console.error('获取用户信息失败:', error)
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
