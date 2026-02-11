<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-header">
        <h1>📚 LinguaLearn</h1>
        <p>多语种学习平台 · 管理后台</p>
      </div>
      
      <el-form ref="loginFormRef" :model="loginForm" :rules="rules" class="login-form">
        <el-form-item prop="username">
          <el-input 
            v-model="loginForm.username" 
            placeholder="请输入管理员账号"
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
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        
        <el-form-item prop="captcha" v-if="captchaEnabled">
          <el-input 
            v-model="loginForm.captcha" 
            placeholder="验证码"
            size="large"
            style="width: 60%"
            @keyup.enter="handleLogin"
          />
          <img :src="captchaUrl" @click="refreshCaptcha" class="captcha-img" alt="验证码"/>
        </el-form-item>
        
        <el-form-item>
          <el-button 
            type="primary" 
            size="large" 
            :loading="loading" 
            @click="handleLogin"
            style="width: 100%"
          >
            {{ loading ? '登录中...' : '登 录' }}
          </el-button>
        </el-form-item>
        
        <div class="tips">
          <span>默认账号：admin / admin123</span>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const loading = ref(false)
const captchaEnabled = ref(false)
const loginFormRef = ref()

const loginForm = reactive({
  username: 'admin',
  password: 'admin123',
  captcha: '',
  uuid: ''
})

const rules = {
  username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const captchaUrl = ref('')

const refreshCaptcha = () => {
  if (captchaEnabled.value) {
    loginForm.uuid = 'uuid-' + Date.now()
    captchaUrl.value = `/captcha/captchaImage?uuid=${loginForm.uuid}`
  }
}

const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        // 模拟登录成功
        localStorage.setItem('admin_token', 'mock_admin_token')
        localStorage.setItem('admin_user', JSON.stringify({
          username: loginForm.username,
          nickname: '管理员',
          roles: ['admin']
        }))
        
        ElMessage.success('登录成功')
        router.push('/dashboard')
      } catch (error) {
        ElMessage.error(error.message || '登录失败')
      } finally {
        loading.value = false
      }
    }
  })
}

// 初始化
refreshCaptcha()
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-box {
  width: 400px;
  padding: 40px;
  background: white;
  border-radius: 10px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.login-header h1 {
  font-size: 28px;
  color: #409eff;
  margin-bottom: 10px;
}

.login-header p {
  font-size: 14px;
  color: #666;
}

.login-form {
  margin-top: 20px;
}

.captcha-img {
  width: 35%;
  height: 40px;
  margin-left: 10px;
  cursor: pointer;
  border-radius: 4px;
}

.tips {
  text-align: center;
  margin-top: 20px;
  font-size: 12px;
  color: #999;
}
</style>
