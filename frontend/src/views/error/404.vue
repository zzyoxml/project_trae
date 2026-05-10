<template>
  <div class="error-page">
    <div class="error-content">
      <h1>404</h1>
      <h2>页面不存在</h2>
      <p>抱歉，您访问的页面不存在或已被删除</p>
      <el-button type="primary" @click="goHome">返回首页</el-button>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const goHome = () => {
  router.push('/')
}
</script>

<style lang="scss" scoped>
.error-page {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);

  .error-content {
    text-align: center;
    color: white;

    h1 {
      font-size: 150px;
      margin-bottom: 16px;
      text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.2);
    }

    h2 {
      font-size: 32px;
      margin-bottom: 16px;
    }

    p {
      font-size: 18px;
      margin-bottom: 32px;
      opacity: 0.9;
    }
  }
}
</style>
