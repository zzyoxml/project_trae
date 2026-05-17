<template>
  <div class="login-page" :class="{ 'dark-mode': themeStore.isDark }">
    <div class="login-container">
      <div class="login-decoration">
        <div class="decoration-circle circle-1"></div>
        <div class="decoration-circle circle-2"></div>
        <div class="decoration-circle circle-3"></div>
      </div>

      <div class="login-header">
        <h1>🍵 LinguaLearn</h1>
        <p>✨ 多语种在线教育平台</p>
      </div>

      <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="rules"
        class="login-form"
        @keyup.enter="handleLogin"
      >
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
            size="large"
            prefix-icon="User"
          />
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            size="large"
            prefix-icon="Lock"
            show-password
          />
        </el-form-item>

        <el-form-item>
          <div class="form-options">
            <el-checkbox v-model="loginForm.rememberMe">记住我</el-checkbox>
            <a href="#" @click.prevent="handleForgot">忘记密码？</a>
          </div>
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            size="large"
            :loading="loading"
            @click="handleLogin"
            class="login-button"
          >
            🍵 登 录
          </el-button>
        </el-form-item>
      </el-form>

      <div class="login-footer">
        <span>还没有账号？</span>
        <router-link to="/register">立即注册</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { useThemeStore } from '@/stores/theme'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const themeStore = useThemeStore()

const loginFormRef = ref()
const loading = ref(false)
const loginForm = ref({
  username: '',
  password: '',
  rememberMe: false
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  if (!loginFormRef.value) return

  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await userStore.login(loginForm.value)
        ElMessage.success('🎉 登录成功')
        const redirect = route.query.redirect || '/home'
        router.push(redirect)
      } catch (error) {
        console.error('登录失败:', error)
        ElMessage.error(error.message || '登录失败')
      } finally {
        loading.value = false
      }
    }
  })
}

const handleForgot = () => {
  ElMessage.info('请联系管理员重置密码')
}
</script>

<style lang="scss" scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #E8FFF2 0%, #F0FFF5 50%, #E8F5E8 100%);
  position: relative;
  overflow: hidden;
}

.login-container {
  background: rgba(255, 255, 255, 0.95);
  padding: 48px;
  border-radius: 32px;
  box-shadow: 0 20px 60px rgba(152, 216, 200, 0.3);
  width: 420px;
  max-width: 90%;
  position: relative;
  z-index: 1;
  backdrop-filter: blur(10px);
  border: 2px solid rgba(152, 216, 200, 0.2);
}

.login-decoration {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  overflow: hidden;
  pointer-events: none;

  .decoration-circle {
    position: absolute;
    border-radius: 50%;
    opacity: 0.1;

    &.circle-1 {
      width: 200px;
      height: 200px;
      background: #98D8C8;
      top: -100px;
      right: -100px;
      animation: float 6s ease-in-out infinite;
    }

    &.circle-2 {
      width: 150px;
      height: 150px;
      background: #ADD8E6;
      bottom: -75px;
      left: -75px;
      animation: float 8s ease-in-out infinite reverse;
    }

    &.circle-3 {
      width: 100px;
      height: 100px;
      background: #B5E5D3;
      top: 50%;
      right: -50px;
      animation: float 7s ease-in-out infinite;
    }
  }
}

@keyframes float {
  0%, 100% { transform: translateY(0) rotate(0deg); }
  50% { transform: translateY(-20px) rotate(10deg); }
}

.login-header {
  text-align: center;
  margin-bottom: 36px;
  position: relative;

  h1 {
    font-size: 36px;
    background: linear-gradient(135deg, #98D8C8 0%, #6BA899 100%);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
    margin-bottom: 12px;
    font-weight: 700;
  }

  p {
    color: #4A7A6A;
    font-size: 14px;
    margin: 0;
  }
}

.login-form {
  position: relative;

  :deep(.el-form-item) {
    margin-bottom: 24px;
  }

  :deep(.el-input) {
    .el-input__wrapper {
      padding: 12px 16px;
      border-radius: 16px;
      box-shadow: 0 0 0 2px rgba(152, 216, 200, 0.2);
      transition: all 0.3s ease;
      background: white;

      &:hover {
        box-shadow: 0 0 0 2px rgba(152, 216, 200, 0.4);
      }

      &.is-focus {
        box-shadow: 0 0 0 3px rgba(152, 216, 200, 0.5);
      }
    }

    .el-input__inner {
      font-size: 15px;
      color: #2D5A4A;

      &::placeholder {
        color: #6A9A8A;
      }
    }

    .el-input__prefix {
      color: #98D8C8;
      font-size: 18px;
    }
  }

  .form-options {
    width: 100%;
    display: flex;
    justify-content: space-between;
    align-items: center;

    :deep(.el-checkbox) {
      .el-checkbox__input {
        .el-checkbox__inner {
          border-radius: 6px;
          border-color: #98D8C8;

          &::after {
            border-color: #6BA899;
          }
        }

        &.is-checked {
          .el-checkbox__inner {
            background-color: #98D8C8;
            border-color: #98D8C8;
          }
        }
      }

      .el-checkbox__label {
        color: #4A7A6A;
        font-size: 14px;
      }
    }

    a {
      color: #6BA899;
      text-decoration: none;
      font-size: 14px;
      font-weight: 500;
      transition: all 0.3s ease;

      &:hover {
        color: #98D8C8;
        text-decoration: underline;
      }
    }
  }

  .login-button {
    width: 100%;
    height: 48px;
    font-size: 16px;
    font-weight: 600;
    border-radius: 16px;
    background: linear-gradient(135deg, #98D8C8 0%, #6BA899 100%);
    border: none;
    box-shadow: 0 6px 20px rgba(152, 216, 200, 0.4);
    transition: all 0.3s ease;
    color: white;

    &:hover {
      transform: translateY(-3px);
      box-shadow: 0 10px 30px rgba(152, 216, 200, 0.5);
    }

    &:active {
      transform: translateY(-1px);
    }

    &.is-loading {
      background: linear-gradient(135deg, #98D8C8 0%, #6BA899 100%);
    }
  }
}

.login-footer {
  text-align: center;
  margin-top: 32px;
  color: #4A7A6A;
  font-size: 14px;
  position: relative;

  a {
    color: #6BA899;
    text-decoration: none;
    margin-left: 8px;
    font-weight: 600;
    transition: all 0.3s ease;

    &:hover {
      color: #98D8C8;
      text-decoration: underline;
    }
  }
}

// 响应式设计
@media (max-width: 480px) {
  .login-container {
    padding: 32px 24px;
    border-radius: 24px;
    width: 100%;
    max-width: 100%;
    margin: 16px;
  }

  .login-header {
    margin-bottom: 28px;

    h1 {
      font-size: 28px;
    }

    p {
      font-size: 13px;
    }
  }

  .login-form {
    :deep(.el-form-item) {
      margin-bottom: 20px;
    }

    :deep(.el-input) {
      .el-input__wrapper {
        padding: 10px 14px;
        border-radius: 14px;
      }

      .el-input__inner {
        font-size: 14px;
      }
    }

    .login-button {
      height: 44px;
      font-size: 15px;
      border-radius: 14px;
    }
  }

  .login-footer {
    margin-top: 24px;
    font-size: 13px;
  }
}

// 暗黑模式
.login-page.dark-mode {
  background: linear-gradient(135deg, #0f1a1a 0%, #1a2a2a 50%, #0f1a1a 100%);

  .login-container {
    background: rgba(30, 46, 46, 0.95);
    border: 2px solid rgba(152, 216, 200, 0.15);
    box-shadow: 0 20px 60px rgba(0, 0, 0, 0.5);
  }

  .login-header {
    h1 {
      background: linear-gradient(135deg, #98D8C8 0%, #B5E5D3 100%);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
    }

    p {
      color: #8aa89a;
    }
  }

  .login-form {
    :deep(.el-input) {
      .el-input__wrapper {
        background: #2a3a3a;
        box-shadow: 0 0 0 2px rgba(152, 216, 200, 0.15);

        &:hover {
          box-shadow: 0 0 0 2px rgba(152, 216, 200, 0.3);
        }

        &.is-focus {
          box-shadow: 0 0 0 3px rgba(152, 216, 200, 0.4);
        }
      }

      .el-input__inner {
        color: #d0e0d8;

        &::placeholder {
          color: #5a7a6a;
        }
      }

      .el-input__prefix {
        color: #98D8C8;
      }
    }

    .form-options {
      :deep(.el-checkbox) {
        .el-checkbox__label {
          color: #8aa89a;
        }
      }

      a {
        color: #98D8C8;

        &:hover {
          color: #B5E5D3;
        }
      }
    }
  }

  .login-footer {
    color: #8aa89a;

    a {
      color: #98D8C8;

      &:hover {
        color: #B5E5D3;
      }
    }
  }
}
</style>
