<template>
  <div class="admin-users-page">
    <div class="page-header">
      <h1>👥 用户管理</h1>
      <div class="header-actions">
        <el-button type="primary" @click="openAddDialog">
          <el-icon><Plus /></el-icon>
          新增用户
        </el-button>
        <el-button type="success" @click="loadUsers" :loading="loading">
          <el-icon><Refresh /></el-icon>
          刷新
        </el-button>
      </div>
    </div>

    <div class="filter-section">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="用户名">
          <el-input
            v-model="searchForm.username"
            placeholder="请输入用户名"
            clearable
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input
            v-model="searchForm.email"
            placeholder="请输入邮箱"
            clearable
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item label="学习语言">
          <el-select v-model="searchForm.learningLanguage" placeholder="请选择" clearable>
            <el-option label="全部" value="" />
            <el-option label="英语" value="en" />
            <el-option label="日语" value="ja" />
            <el-option label="汉语" value="zh" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon><RefreshRight /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="stats-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon">👥</div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.total }}</div>
              <div class="stat-label">总用户数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon">✅</div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.active }}</div>
              <div class="stat-label">活跃用户</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon">🎓</div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.totalStudyHours }}</div>
              <div class="stat-label">总学习时长(小时)</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon">🏆</div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.totalAchievements }}</div>
              <div class="stat-label">获得成就数</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <div class="users-table" v-loading="loading">
      <el-table :data="users" stripe border>
        <el-table-column prop="userId" label="ID" width="80" />
        <el-table-column label="用户信息" min-width="220">
          <template #default="{ row }">
            <div class="user-info-cell">
              <div class="user-avatar">
                {{ row.nickName ? row.nickName.charAt(0).toUpperCase() : 'U' }}
              </div>
              <div class="user-details">
                <div class="user-name">{{ row.nickName || row.username }}</div>
                <div class="user-email">{{ row.email }}</div>
                <div class="user-phone">{{ row.phonenumber }}</div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="password" label="密码" min-width="180">
          <template #default="{ row }">
            <div class="password-cell">
              <span class="password-text">{{ row.showPassword ? row.password : '••••••••' }}</span>
              <el-button link size="small" @click="togglePasswordVisibility(row)">
                {{ row.showPassword ? '隐藏' : '显示' }}
              </el-button>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="学习语言" width="150">
          <template #default="{ row }">
            <div class="language-tags">
              <el-tag
                v-for="lang in formatLanguages(row.learningLanguages)"
                :key="lang"
                size="small"
                :type="getLanguageType(lang)"
              >
                {{ lang }}
              </el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="totalStudyTime" label="学习时长" width="100">
          <template #default="{ row }">
            <span>{{ row.totalStudyTime || 0 }}分钟</span>
          </template>
        </el-table-column>
        <el-table-column prop="currentStreak" label="连续学习" width="100">
          <template #default="{ row }">
            <span class="streak-badge">🔥 {{ row.currentStreak || 0 }}天</span>
          </template>
        </el-table-column>
        <el-table-column prop="totalPoints" label="积分" width="80" />
        <el-table-column prop="level" label="等级" width="80">
          <template #default="{ row }">
            <el-tag type="warning">Lv.{{ row.level || 1 }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="注册时间" width="180">
          <template #default="{ row }">
            {{ formatTime(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === '0' ? 'success' : 'danger'">
              {{ row.status === '0' ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="viewUserDetail(row)">
              详情
            </el-button>
            <el-button link type="primary" @click="openEditDialog(row)">
              编辑
            </el-button>
            <el-button
              link
              :type="row.status === '0' ? 'warning' : 'success'"
              @click="toggleUserStatus(row)"
            >
              {{ row.status === '0' ? '禁用' : '启用' }}
            </el-button>
            <el-button link type="danger" @click="deleteUser(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>

    <el-dialog v-model="detailDialogVisible" title="用户详情" width="600px">
      <div v-if="currentUser" class="user-detail-content">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="用户ID">{{ currentUser.userId }}</el-descriptions-item>
          <el-descriptions-item label="用户名">{{ currentUser.username }}</el-descriptions-item>
          <el-descriptions-item label="密码" :span="2">
            <span>{{ currentUser.showPassword ? (currentUser.password || '-') : '••••••••' }}</span>
            <el-button link size="small" @click="toggleDetailPasswordVisibility">
              {{ currentUser.showPassword ? '隐藏' : '显示' }}
            </el-button>
          </el-descriptions-item>
          <el-descriptions-item label="昵称">{{ currentUser.nickName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="邮箱">{{ currentUser.email || '-' }}</el-descriptions-item>
          <el-descriptions-item label="手机号">{{ currentUser.phonenumber || '-' }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="currentUser.status === '0' ? 'success' : 'danger'">
              {{ currentUser.status === '0' ? '正常' : '禁用' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="母语">{{ formatLanguage(currentUser.nativeLanguage) }}</el-descriptions-item>
          <el-descriptions-item label="学习语言">{{ formatLanguagesText(currentUser.learningLanguages) }}</el-descriptions-item>
          <el-descriptions-item label="当前等级">Lv.{{ currentUser.level || 1 }}</el-descriptions-item>
          <el-descriptions-item label="总积分">{{ currentUser.totalPoints || 0 }}</el-descriptions-item>
          <el-descriptions-item label="学习时长">{{ currentUser.totalStudyTime || 0 }} 分钟</el-descriptions-item>
          <el-descriptions-item label="连续学习">{{ currentUser.currentStreak || 0 }} 天</el-descriptions-item>
          <el-descriptions-item label="注册时间" :span="2">{{ formatTime(currentUser.createTime) }}</el-descriptions-item>
          <el-descriptions-item label="个人简介" :span="2">{{ currentUser.bio || '暂无简介' }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <template #footer>
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="userFormDialogVisible" :title="isEditMode ? '编辑用户' : '新增用户'" width="700px">
      <el-form ref="userFormRef" :model="userForm" :rules="userFormRules" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="userForm.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item :label="isEditMode ? '新密码' : '密码'" :prop="isEditMode ? '' : 'password'">
          <el-input
            v-model="userForm.password"
            :type="showPasswordInput ? 'text' : 'password'"
            :placeholder="isEditMode ? '留空则不修改密码' : '请输入密码'"
          >
            <template #suffix>
              <el-button link @click="showPasswordInput = !showPasswordInput">
                {{ showPasswordInput ? '隐藏' : '显示' }}
              </el-button>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="昵称" prop="nickName">
          <el-input v-model="userForm.nickName" placeholder="请输入昵称" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userForm.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="手机号" prop="phonenumber">
          <el-input v-model="userForm.phonenumber" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="母语" prop="nativeLanguage">
          <el-select v-model="userForm.nativeLanguage" placeholder="请选择母语">
            <el-option label="中文" value="zh" />
            <el-option label="英语" value="en" />
            <el-option label="日语" value="ja" />
          </el-select>
        </el-form-item>
        <el-form-item label="学习语言" prop="learningLanguages">
          <el-select v-model="userForm.learningLanguages" multiple placeholder="请选择学习语言">
            <el-option label="英语" value="en" />
            <el-option label="日语" value="ja" />
            <el-option label="汉语" value="zh" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="userForm.status">
            <el-radio label="0">正常</el-radio>
            <el-radio label="1">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="userFormDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitUserForm" :loading="formSubmitting">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getAllUsers, deleteUserById, updateUserStatus, addUser, editUser, getUserDetail } from '@/api/user'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const users = ref([])
const detailDialogVisible = ref(false)
const currentUser = ref(null)
const userFormDialogVisible = ref(false)
const isEditMode = ref(false)
const formSubmitting = ref(false)
const showPasswordInput = ref(false)
const userFormRef = ref(null)

const searchForm = reactive({
  username: '',
  email: '',
  learningLanguage: ''
})

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const stats = reactive({
  total: 0,
  active: 0,
  totalStudyHours: 0,
  totalAchievements: 0
})

const userForm = reactive({
  userId: null,
  username: '',
  password: '',
  nickName: '',
  email: '',
  phonenumber: '',
  nativeLanguage: 'zh',
  learningLanguages: [],
  status: '0'
})

const userFormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度为3-20个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度为6-20个字符', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  nickName: [
    { max: 20, message: '昵称不能超过20个字符', trigger: 'blur' }
  ]
}

onMounted(() => {
  loadUsers()
})

const loadUsers = async () => {
  loading.value = true
  try {
    const res = await getAllUsers({
      page: pagination.page,
      size: pagination.size,
      username: searchForm.username,
      email: searchForm.email,
      learningLanguage: searchForm.learningLanguage
    })
    
    if (res && res.rows) {
      users.value = res.rows.map(user => ({ ...user, showPassword: false }))
      pagination.total = res.total || 0
    } else if (Array.isArray(res)) {
      users.value = res.map(user => ({ ...user, showPassword: false }))
      pagination.total = res.length || 0
    } else {
      users.value = []
      pagination.total = 0
    }
    
    calculateStats()
  } catch (error) {
    console.error('加载用户列表失败:', error)
    ElMessage.error('加载用户列表失败')
  } finally {
    loading.value = false
  }
}

const calculateStats = () => {
  stats.total = pagination.total
  const activeUsers = users.value.filter(user => {
    const lastStudy = new Date(user.lastStudyDate)
    const weekAgo = new Date(Date.now() - 7 * 24 * 60 * 60 * 1000)
    return lastStudy > weekAgo
  })
  stats.active = activeUsers.length
  const totalMinutes = users.value.reduce((sum, user) => sum + (user.totalStudyTime || 0), 0)
  stats.totalStudyHours = Math.floor(totalMinutes / 60)
  stats.totalAchievements = users.value.reduce((sum, user) => sum + (user.achievementCount || 0), 0)
}

const handleSearch = () => {
  pagination.page = 1
  loadUsers()
}

const handleReset = () => {
  searchForm.username = ''
  searchForm.email = ''
  searchForm.learningLanguage = ''
  pagination.page = 1
  loadUsers()
}

const handleSizeChange = (size) => {
  pagination.size = size
  pagination.page = 1
  loadUsers()
}

const handleCurrentChange = (page) => {
  pagination.page = page
  loadUsers()
}

const viewUserDetail = async (user) => {
  try {
    const res = await getUserDetail(user.userId)
    currentUser.value = { ...res, showPassword: false }
    detailDialogVisible.value = true
  } catch (error) {
    currentUser.value = { ...user, showPassword: false }
    detailDialogVisible.value = true
  }
}

const toggleDetailPasswordVisibility = () => {
  if (currentUser.value) {
    currentUser.value.showPassword = !currentUser.value.showPassword
  }
}

const togglePasswordVisibility = (user) => {
  user.showPassword = !user.showPassword
}

const openAddDialog = () => {
  isEditMode.value = false
  resetForm()
  userFormDialogVisible.value = true
}

const openEditDialog = async (user) => {
  isEditMode.value = true
  try {
    const res = await getUserDetail(user.userId)
    Object.assign(userForm, {
      userId: res.userId,
      username: res.username,
      password: '',
      nickName: res.nickName || '',
      email: res.email || '',
      phonenumber: res.phonenumber || '',
      nativeLanguage: res.nativeLanguage || 'zh',
      learningLanguages: res.learningLanguages ? res.learningLanguages.split(',') : [],
      status: res.status || '0'
    })
  } catch (error) {
    Object.assign(userForm, {
      userId: user.userId,
      username: user.username,
      password: '',
      nickName: user.nickName || '',
      email: user.email || '',
      phonenumber: user.phonenumber || '',
      nativeLanguage: user.nativeLanguage || 'zh',
      learningLanguages: user.learningLanguages ? user.learningLanguages.split(',') : [],
      status: user.status || '0'
    })
  }
  userFormDialogVisible.value = true
}

const resetForm = () => {
  userForm.userId = null
  userForm.username = ''
  userForm.password = ''
  userForm.nickName = ''
  userForm.email = ''
  userForm.phonenumber = ''
  userForm.nativeLanguage = 'zh'
  userForm.learningLanguages = []
  userForm.status = '0'
  showPasswordInput.value = false
}

const submitUserForm = async () => {
  if (!userFormRef.value) return
  
  await userFormRef.value.validate(async (valid) => {
    if (valid) {
      formSubmitting.value = true
      try {
        const submitData = {
          ...userForm,
          learningLanguages: userForm.learningLanguages.join(',')
        }
        
        if (isEditMode.value) {
          if (!submitData.password) {
            delete submitData.password
          }
          await editUser(submitData)
          ElMessage.success('编辑成功')
        } else {
          await addUser(submitData)
          ElMessage.success('新增成功')
        }
        
        userFormDialogVisible.value = false
        loadUsers()
      } catch (error) {
        console.error('提交失败:', error)
        ElMessage.error(error.message || '操作失败')
      } finally {
        formSubmitting.value = false
      }
    }
  })
}

const toggleUserStatus = async (user) => {
  try {
    await ElMessageBox.confirm(
      `确定要${user.status === '0' ? '禁用' : '启用'}用户 "${user.nickName || user.username}" 吗？`,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await updateUserStatus(user.userId, user.status === '0' ? '1' : '0')
    ElMessage.success('操作成功')
    loadUsers()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('更新用户状态失败:', error)
      ElMessage.error('操作失败')
    }
  }
}

const deleteUser = async (user) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除用户 "${user.nickName || user.username}" 吗？此操作不可恢复！`,
      '危险操作',
      {
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        type: 'error'
      }
    )
    
    await deleteUserById(user.userId)
    ElMessage.success('删除成功')
    loadUsers()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除用户失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

const formatLanguages = (languages) => {
  if (!languages) return []
  const langMap = { 'en': '英语', 'ja': '日语', 'zh': '汉语' }
  const langs = languages.split(',')
  return langs.map(lang => langMap[lang] || lang).filter(lang => lang)
}

const formatLanguagesText = (languages) => {
  const langs = formatLanguages(languages)
  return langs.join('、') || '-'
}

const formatLanguage = (lang) => {
  const langMap = { 'en': '英语', 'ja': '日语', 'zh': '汉语' }
  return langMap[lang] || lang || '-'
}

const getLanguageType = (lang) => {
  const typeMap = { '英语': '', '日语': 'warning', '汉语': 'success' }
  return typeMap[lang] || 'info'
}

const formatTime = (time) => {
  if (!time) return '-'
  const date = new Date(time)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}
</script>

<style lang="scss" scoped>
.admin-users-page {
  max-width: 1600px;
  margin: 0 auto;
  padding: 20px;

  .page-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 24px;

    h1 {
      font-size: 28px;
      font-weight: 600;
      margin: 0;
    }

    .header-actions {
      display: flex;
      gap: 12px;
    }
  }

  .filter-section {
    background: white;
    padding: 20px;
    border-radius: 8px;
    margin-bottom: 20px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);

    .search-form {
      margin: 0;
    }
  }

  .stats-cards {
    margin-bottom: 20px;

    .el-col {
      margin-bottom: 16px;
    }

    .stat-card {
      background: white;
      padding: 20px;
      border-radius: 8px;
      display: flex;
      align-items: center;
      gap: 16px;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);

      .stat-icon {
        font-size: 40px;
      }

      .stat-content {
        flex: 1;

        .stat-value {
          font-size: 28px;
          font-weight: bold;
          color: #409eff;
          margin-bottom: 4px;
        }

        .stat-label {
          color: #909399;
          font-size: 14px;
        }
      }
    }
  }

  .users-table {
    background: white;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);

    .user-info-cell {
      display: flex;
      align-items: center;
      gap: 12px;

      .user-avatar {
        width: 40px;
        height: 40px;
        border-radius: 50%;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        color: white;
        display: flex;
        align-items: center;
        justify-content: center;
        font-weight: bold;
        font-size: 16px;
      }

      .user-details {
        flex: 1;

        .user-name {
          font-weight: 600;
          font-size: 14px;
          margin-bottom: 4px;
        }

        .user-email {
          color: #909399;
          font-size: 12px;
          margin-bottom: 2px;
        }

        .user-phone {
          color: #909399;
          font-size: 12px;
        }
      }
    }

    .password-cell {
      display: flex;
      align-items: center;
      gap: 8px;

      .password-text {
        font-family: monospace;
        color: #606266;
      }
    }

    .language-tags {
      display: flex;
      gap: 4px;
      flex-wrap: wrap;
    }

    .streak-badge {
      color: #f56c6c;
      font-weight: 600;
    }

    .pagination-container {
      margin-top: 20px;
      display: flex;
      justify-content: flex-end;
    }
  }

  .user-detail-content {
    padding: 10px 0;
  }
}
</style>
