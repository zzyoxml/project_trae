<template>
  <div class="settings-page" :class="{ 'dark-mode': themeStore.isDark }">
    <h1>⚙️ 设置</h1>

    <el-card class="settings-card">
      <template #header>
        <span>个人信息设置</span>
      </template>

      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="120px"
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" />
        </el-form-item>

        <el-form-item label="昵称" prop="nickName">
          <el-input v-model="form.nickName" />
        </el-form-item>

        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" />
        </el-form-item>

        <el-form-item label="学习语言" prop="learningLanguages">
          <el-checkbox-group v-model="form.learningLanguages">
            <el-checkbox label="en">英语</el-checkbox>
            <el-checkbox label="ja">日语</el-checkbox>
            <el-checkbox label="zh">汉语</el-checkbox>
          </el-checkbox-group>
        </el-form-item>

        <el-form-item label="每日目标" prop="dailyGoal">
          <el-input-number v-model="form.dailyGoal" :min="10" :max="300" :step="10" />
          <span class="goal-unit">分钟</span>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSave">保存设置</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="settings-card">
      <template #header>
        <span>密码修改</span>
      </template>

      <el-form
        ref="passwordFormRef"
        :model="passwordForm"
        :rules="passwordRules"
        label-width="120px"
      >
        <el-form-item label="原密码" prop="oldPassword">
          <el-input v-model="passwordForm.oldPassword" type="password" show-password />
        </el-form-item>

        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="passwordForm.newPassword" type="password" show-password />
        </el-form-item>

        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="passwordForm.confirmPassword" type="password" show-password />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleChangePassword">修改密码</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { useThemeStore } from '@/stores/theme'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const themeStore = useThemeStore()

const formRef = ref()
const passwordFormRef = ref()

const form = reactive({
  username: userStore.user?.username || '',
  nickName: userStore.user?.nickName || '',
  email: userStore.user?.email || '',
  learningLanguages: ['en'],
  dailyGoal: 30
})

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  nickName: [
    { required: true, message: '请输入昵称', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ]
}

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== passwordForm.newPassword) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const passwordRules = {
  oldPassword: [
    { required: true, message: '请输入原密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

const handleSave = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        ElMessage.success('设置保存成功')
      } catch (error) {
        console.error('保存失败:', error)
        ElMessage.error('保存失败')
      }
    }
  })
}

const handleReset = () => {
  formRef.value?.resetFields()
}

const handleChangePassword = async () => {
  if (!passwordFormRef.value) return

  await passwordFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        ElMessage.success('密码修改成功')
        passwordFormRef.value.resetFields()
      } catch (error) {
        console.error('修改失败:', error)
        ElMessage.error('修改失败')
      }
    }
  })
}
</script>

<style lang="scss" scoped>
.settings-page {
  h1 {
    font-size: 32px;
    margin-bottom: 24px;
  }

  .settings-card {
    margin-bottom: 24px;

    .goal-unit {
      margin-left: 8px;
      color: #909399;
    }
  }
}

.settings-page.dark-mode {
  .settings-card {
    .goal-unit {
      color: #6a8a7a;
    }
  }
}
</style>
