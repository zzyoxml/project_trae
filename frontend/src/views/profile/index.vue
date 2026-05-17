<template>
  <div class="profile-page">
    <h1>👤 个人中心</h1>

    <div class="profile-content" v-loading="loading">
      <!-- 用户信息卡片 -->
      <el-card class="profile-card">
        <template #header>
          <div class="card-header">
            <span>个人信息</span>
            <el-button 
              v-if="!isEditing" 
              type="primary" 
              size="small" 
              @click="startEdit"
            >
              编辑资料
            </el-button>
          </div>
        </template>

        <div class="profile-info" v-if="!isEditing">
          <div class="avatar-section">
            <el-avatar :size="100" :src="formData.avatar">
              {{ formData.nickname?.charAt(0) || 'U' }}
            </el-avatar>
            <div class="user-level">
              <el-tag type="success">Lv.{{ userStore.level || 1 }}</el-tag>
              <span class="exp">{{ userStore.experiencePoints || 0 }} 经验值</span>
            </div>
          </div>

          <div class="info-grid">
            <div class="info-item">
              <label>用户名：</label>
              <span>{{ formData.username || '-' }}</span>
            </div>
            <div class="info-item">
              <label>昵称：</label>
              <span>{{ formData.nickname || '-' }}</span>
            </div>
            <div class="info-item">
              <label>邮箱：</label>
              <span>{{ formData.email || '-' }}</span>
            </div>
            <div class="info-item">
              <label>手机号：</label>
              <span>{{ formData.phone || '-' }}</span>
            </div>
            <div class="info-item">
              <label>母语：</label>
              <span>{{ getLanguageName(formData.nativeLanguage) }}</span>
            </div>
            <div class="info-item">
              <label>学习语言：</label>
              <span>{{ formData.learningLanguages?.join('、') || '-' }}</span>
            </div>
            <div class="info-item">
              <label>学习目标：</label>
              <span>{{ formData.learningGoal || '-' }}</span>
            </div>
            <div class="info-item">
              <label>每日学习时间：</label>
              <span>{{ formData.dailyGoal || '-' }} 分钟</span>
            </div>
          </div>
        </div>

        <!-- 编辑模式 -->
        <el-form 
          v-else 
          :model="formData" 
          :rules="rules" 
          ref="formRef" 
          label-width="120px"
          class="profile-form"
        >
          <el-form-item label="头像：">
            <el-upload
              class="avatar-uploader"
              action="#"
              :show-file-list="false"
              :before-upload="beforeAvatarUpload"
            >
              <img v-if="formData.avatar" :src="formData.avatar" class="avatar" />
              <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
            </el-upload>
          </el-form-item>

          <el-form-item label="昵称：" prop="nickname">
            <el-input v-model="formData.nickname" placeholder="请输入昵称" />
          </el-form-item>

          <el-form-item label="邮箱：" prop="email">
            <el-input v-model="formData.email" placeholder="请输入邮箱" />
          </el-form-item>

          <el-form-item label="手机号：" prop="phone">
            <el-input v-model="formData.phone" placeholder="请输入手机号" />
          </el-form-item>

          <el-form-item label="母语：" prop="nativeLanguage">
            <el-select v-model="formData.nativeLanguage" placeholder="请选择母语">
              <el-option label="中文" value="zh" />
              <el-option label="英语" value="en" />
              <el-option label="日语" value="ja" />
            </el-select>
          </el-form-item>

          <el-form-item label="学习语言：">
            <el-checkbox-group v-model="formData.learningLanguages">
              <el-checkbox label="英语">英语</el-checkbox>
              <el-checkbox label="日语">日语</el-checkbox>
              <el-checkbox label="汉语">汉语</el-checkbox>
            </el-checkbox-group>
          </el-form-item>

          <el-form-item label="学习目标：">
            <el-input 
              v-model="formData.learningGoal" 
              type="textarea" 
              :rows="3"
              placeholder="请描述您的学习目标..."
            />
          </el-form-item>

          <el-form-item label="每日目标：">
            <el-input-number 
              v-model="formData.dailyGoal" 
              :min="10" 
              :max="300" 
              :step="10"
            />
            <span class="unit-text">分钟</span>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="saveProfile">保存</el-button>
            <el-button @click="cancelEdit">取消</el-button>
          </el-form-item>
        </el-form>
      </el-card>

      <!-- 学习统计卡片 -->
      <el-card class="stats-card">
        <template #header>
          <div class="card-header">
            <span>学习统计</span>
          </div>
        </template>

        <div class="stats-grid">
          <div class="stat-item">
            <div class="stat-icon">📚</div>
            <div class="stat-content">
              <div class="stat-value">{{ formData.completedCourses || 0 }}</div>
              <div class="stat-label">已完成课程</div>
            </div>
          </div>

          <div class="stat-item">
            <div class="stat-icon">⏱️</div>
            <div class="stat-content">
              <div class="stat-value">{{ formData.totalStudyMinutes || 0 }}</div>
              <div class="stat-label">学习分钟</div>
            </div>
          </div>

          <div class="stat-item">
            <div class="stat-icon">🔥</div>
            <div class="stat-content">
              <div class="stat-value">{{ formData.streakDays || 0 }}</div>
              <div class="stat-label">连续学习天数</div>
            </div>
          </div>

          <div class="stat-item">
            <div class="stat-icon">🏆</div>
            <div class="stat-content">
              <div class="stat-value">{{ formData.achievementsCount || 0 }}</div>
              <div class="stat-label">获得成就</div>
            </div>
          </div>

          <div class="stat-item">
            <div class="stat-icon">📝</div>
            <div class="stat-content">
              <div class="stat-value">{{ formData.vocabularyCount || 0 }}</div>
              <div class="stat-label">掌握词汇</div>
            </div>
          </div>

          <div class="stat-item">
            <div class="stat-icon">⭐</div>
            <div class="stat-content">
              <div class="stat-value">{{ formData.totalPoints || 0 }}</div>
              <div class="stat-label">总积分</div>
            </div>
          </div>
        </div>
      </el-card>

      <!-- 最近学习记录 -->
      <el-card class="recent-card">
        <template #header>
          <div class="card-header">
            <span>最近学习</span>
            <el-button link @click="$router.push('/learning')">查看全部</el-button>
          </div>
        </template>

        <div class="recent-list">
          <div 
            v-for="record in recentRecords" 
            :key="record.id" 
            class="recent-item"
          >
            <div class="recent-icon">{{ record.icon }}</div>
            <div class="recent-content">
              <div class="recent-title">{{ record.title }}</div>
              <div class="recent-time">{{ record.time }}</div>
            </div>
          </div>

          <el-empty v-if="recentRecords.length === 0" description="暂无学习记录" />
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()

const loading = ref(false)
const isEditing = ref(false)
const formRef = ref(null)

const formData = reactive({
  username: '',
  nickname: '',
  email: '',
  phone: '',
  avatar: '',
  nativeLanguage: 'zh',
  learningLanguages: [],
  learningGoal: '',
  dailyGoal: 30,
  completedCourses: 0,
  totalStudyMinutes: 0,
  streakDays: 0,
  achievementsCount: 0,
  vocabularyCount: 0,
  totalPoints: 0
})

const recentRecords = ref([
  { id: 1, icon: '📖', title: '学习英语基础词汇第5课', time: '2小时前' },
  { id: 2, icon: '🎧', title: '完成日语听力训练', time: '1天前' },
  { id: 3, icon: '✏️', title: '汉语拼音练习', time: '2天前' }
])

const rules = {
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ]
}

onMounted(() => {
  loadProfile()
})

const loadProfile = () => {
  Object.assign(formData, {
    username: userStore.username || '',
    nickname: userStore.nickname || '学习者',
    email: userStore.email || '',
    phone: userStore.phone || '',
    avatar: userStore.avatar || '',
    nativeLanguage: userStore.nativeLanguage || 'zh',
    learningLanguages: userStore.learningLanguages || ['英语'],
    learningGoal: userStore.learningGoal || '',
    dailyGoal: userStore.dailyGoal || 30,
    completedCourses: userStore.completedCourses || 2,
    totalStudyMinutes: userStore.totalStudyMinutes || 360,
    streakDays: userStore.streakDays || 5,
    achievementsCount: userStore.achievementsCount || 8,
    vocabularyCount: userStore.vocabularyCount || 156,
    totalPoints: userStore.totalPoints || 2580
  })
}

const startEdit = () => {
  isEditing.value = true
}

const cancelEdit = () => {
  isEditing.value = false
  loadProfile()
}

const saveProfile = async () => {
  try {
    await formRef.value.validate()
    
    await userStore.updateUserInfo({
      nickname: formData.nickname,
      email: formData.email,
      phone: formData.phone,
      avatar: formData.avatar,
      nativeLanguage: formData.nativeLanguage,
      learningLanguages: formData.learningLanguages,
      learningGoal: formData.learningGoal,
      dailyGoal: formData.dailyGoal
    })
    
    ElMessage.success('保存成功')
    isEditing.value = false
  } catch (error) {
    console.error('保存失败:', error)
  }
}

const beforeAvatarUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传图片文件')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB')
    return false
  }
  
  const reader = new FileReader()
  reader.readAsDataURL(file)
  reader.onload = (e) => {
    formData.avatar = e.target.result
  }
  
  return false
}

const getLanguageName = (code) => {
  const map = { zh: '中文', en: '英语', ja: '日语' }
  return map[code] || code
}
</script>

<style lang="scss" scoped>
.profile-page {
  h1 {
    font-size: 32px;
    margin-bottom: 24px;
  }

  .profile-content {
    display: grid;
    gap: 20px;

    .profile-card {
      .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
      }

      .profile-info {
        .avatar-section {
          display: flex;
          align-items: center;
          gap: 20px;
          margin-bottom: 24px;
          padding-bottom: 24px;
          border-bottom: 1px solid #ebeef5;

          .user-level {
            display: flex;
            flex-direction: column;
            gap: 8px;

            .exp {
              font-size: 14px;
              color: #909399;
            }
          }
        }

        .info-grid {
          display: grid;
          grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
          gap: 16px;

          .info-item {
            display: flex;
            gap: 8px;

            label {
              color: #909399;
              min-width: 80px;
            }

            span {
              color: #303133;
            }
          }
        }
      }

      .profile-form {
        .avatar-uploader {
          :deep(.el-upload) {
            border: 1px dashed #d9d9d9;
            border-radius: 50%;
            cursor: pointer;
            position: relative;
            overflow: hidden;
            transition: border-color 0.3s;

            &:hover {
              border-color: #409eff;
            }
          }

          .avatar {
            width: 100px;
            height: 100px;
            display: block;
            border-radius: 50%;
          }

          .avatar-uploader-icon {
            font-size: 28px;
            color: #8c939d;
            width: 100px;
            height: 100px;
            text-align: center;
            line-height: 100px;
          }
        }

        .unit-text {
          margin-left: 12px;
          color: #909399;
        }
      }
    }

    .stats-card {
      .stats-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
        gap: 20px;

        .stat-item {
          display: flex;
          align-items: center;
          gap: 12px;
          padding: 16px;
          background: linear-gradient(135deg, #f5f7fa 0%, #e4e8eb 100%);
          border-radius: 12px;

          .stat-icon {
            font-size: 32px;
          }

          .stat-content {
            .stat-value {
              font-size: 24px;
              font-weight: bold;
              color: #303133;
            }

            .stat-label {
              font-size: 12px;
              color: #909399;
              margin-top: 4px;
            }
          }
        }
      }
    }

    .recent-card {
      .recent-list {
        .recent-item {
          display: flex;
          align-items: center;
          gap: 12px;
          padding: 12px;
          border-bottom: 1px solid #ebeef5;
          cursor: pointer;
          transition: background 0.3s;

          &:last-child {
            border-bottom: none;
          }

          &:hover {
            background: var(--el-bg-color-page, #f5f7fa);
          }

          .recent-icon {
            font-size: 24px;
          }

          .recent-content {
            flex: 1;

            .recent-title {
              font-size: 14px;
              color: #303133;
              margin-bottom: 4px;
            }

            .recent-time {
              font-size: 12px;
              color: #909399;
            }
          }
        }
      }
    }
  }
}
</style>
