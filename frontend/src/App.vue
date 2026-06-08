<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script setup>
import { onMounted, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { getToken, removeToken } from '@/utils/auth'
import { getUserInfo } from '@/api/user'

const router = useRouter()
const userStore = useUserStore()

// 单点登录心跳：每隔 3 秒验证一次 token，发现 401 就清登录态+跳登录页
let ssoTimer = null
const SSO_INTERVAL = 3 * 1000

const startSsoHeartbeat = () => {
  stopSsoHeartbeat()
  console.log('[SSO] 心跳启动，每 3 秒验证一次')
  ssoTimer = setInterval(async () => {
    const token = getToken()
    if (!token) {
      console.log('[SSO] 无 token，停止心跳')
      stopSsoHeartbeat()
      return
    }
    console.log('[SSO] 心跳验证 token...')
    try {
      await getUserInfo()
      console.log('[SSO] token 有效')
    } catch (e) {
      console.log('[SSO] token 已失效，停止心跳')
      // 401 已在 request.js 拦截器中处理（清 localStorage + 跳登录页）
      stopSsoHeartbeat()
    }
  }, SSO_INTERVAL)
}

const stopSsoHeartbeat = () => {
  if (ssoTimer) {
    clearInterval(ssoTimer)
    ssoTimer = null
  }
}

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
  // 启动单点登录心跳
  if (getToken()) {
    startSsoHeartbeat()
  }
})

onBeforeUnmount(() => {
  stopSsoHeartbeat()
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

html.dark #app {
  background-color: #1a2a2a;
  color: #d0e0d8;
}
</style>
