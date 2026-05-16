<template>
  <div class="learning-page">
    <h1>📖 学习中心</h1>

    <!-- 学习进度概览 -->
    <el-card class="progress-card">
      <template #header>
        <div class="card-header">
          <span>学习概览</span>
          <el-tag type="success">今日已学习 {{ todayMinutes }} 分钟</el-tag>
        </div>
      </template>

      <div class="progress-overview">
        <div class="progress-item">
          <el-progress 
            type="circle" 
            :percentage="dailyProgress" 
            :width="120"
            :stroke-width="10"
          />
          <div class="progress-info">
            <div class="progress-label">今日目标</div>
            <div class="progress-value">30 分钟</div>
          </div>
        </div>

        <div class="stats-list">
          <div class="stat-item">
            <span class="stat-icon">🔥</span>
            <div class="stat-content">
              <div class="stat-value">{{ currentStreak }}</div>
              <div class="stat-label">连续学习天数</div>
            </div>
          </div>

          <div class="stat-item">
            <span class="stat-icon">⏱️</span>
            <div class="stat-content">
              <div class="stat-value">{{ totalStudyTime }}</div>
              <div class="stat-label">总学习时间(分钟)</div>
            </div>
          </div>

          <div class="stat-item">
            <span class="stat-icon">📚</span>
            <div class="stat-content">
              <div class="stat-value">{{ completedCourses }}</div>
              <div class="stat-label">已完成课程</div>
            </div>
          </div>

          <div class="stat-item">
            <span class="stat-icon">⭐</span>
            <div class="stat-content">
              <div class="stat-value">{{ totalPoints }}</div>
              <div class="stat-label">总积分</div>
            </div>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 继续学习 -->
    <el-card class="continue-card">
      <template #header>
        <div class="card-header">
          <span>继续学习</span>
          <el-button link @click="$router.push('/course')">查看全部课程</el-button>
        </div>
      </template>

      <div class="continue-list" v-loading="loading">
        <div 
          v-for="course in inProgressCourses" 
          :key="course.courseId"
          class="continue-item"
        >
          <div class="course-cover">
            <img :src="course.coverImage" :alt="course.courseName" @error="handleImageError">
          </div>
          <div class="course-info">
            <div class="course-name">{{ course.courseName }}</div>
            <div class="course-progress">
              <el-progress 
                :percentage="course.progress || 0" 
                :stroke-width="6"
                :show-text="false"
              />
              <span class="progress-text">{{ course.progress || 0 }}%</span>
            </div>
            <div class="course-meta">
              <span>课时 {{ course.completedLessons || 0 }}/{{ course.totalLessons || course.lessonCount }}</span>
              <span>{{ course.lastLesson || '第1课' }}</span>
            </div>
          </div>
          <el-button type="primary" @click="continueLearning(course)">
            继续学习
          </el-button>
        </div>

        <el-empty v-if="inProgressCourses.length === 0 && !loading" description="暂无正在学习的课程">
          <el-button type="primary" @click="$router.push('/course')">去选课</el-button>
        </el-empty>
      </div>
    </el-card>

    <!-- 推荐学习 -->
    <el-card class="recommend-card">
      <template #header>
        <div class="card-header">
          <span>推荐学习</span>
        </div>
      </template>

      <div class="recommend-list">
        <div 
          v-for="course in recommendedCourses" 
          :key="course.courseId"
          class="recommend-item"
          @click="goToCourse(course.courseId)"
        >
          <div class="course-cover">
            <img :src="course.coverImage" :alt="course.courseName" @error="handleImageError">
            <span class="course-language">{{ getLanguageText(course.language) }}</span>
          </div>
          <div class="course-info">
            <div class="course-name">{{ course.courseName }}</div>
            <div class="course-desc">{{ course.description }}</div>
            <div class="course-meta">
              <span>⭐ {{ course.rating }}</span>
              <span>👥 {{ formatNumber(course.studentCount) }}</span>
              <span>📚 {{ course.lessonCount }} 课时</span>
            </div>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 每日任务 -->
    <el-card class="tasks-card">
      <template #header>
        <div class="card-header">
          <span>每日任务</span>
          <el-tag type="warning">{{ completedTasks }}/{{ totalTasks }} 已完成</el-tag>
        </div>
      </template>

      <div class="task-list">
        <div 
          v-for="task in dailyTasks" 
          :key="task.id"
          class="task-item"
          :class="{ completed: task.completed }"
        >
          <div class="task-icon">
            <span v-if="task.completed">✓</span>
            <span v-else>{{ task.icon }}</span>
          </div>
          <div class="task-info">
            <div class="task-name">{{ task.name }}</div>
            <div class="task-reward">+{{ task.reward }} 积分</div>
          </div>
          <el-tag v-if="task.claimed" type="info" size="small">已领取</el-tag>
          <el-button 
            v-else-if="task.completed" 
            type="success" 
            size="small"
            @click="claimReward(task)"
          >
            领取 +{{ task.reward }}积分
          </el-button>
          <el-button 
            v-else
            type="primary" 
            size="small"
            @click="goToTask(task)"
          >
            去完成
          </el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { getMyCourses, getFeaturedCourses } from '@/api/course'
import { getLearningStats, claimTaskReward } from '@/api/learning'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const loading = ref(false)
const inProgressCourses = ref([])
const recommendedCourses = ref([])
const todayMinutes = ref(0)
const completedCourses = ref(0)
const currentStreak = ref(0)
const totalStudyTime = ref(0)
const totalPoints = ref(0)

const dailyTasks = ref([])
const completedTasks = ref(0)
const totalTasks = ref(0)

const dailyProgress = computed(() => {
  const goal = 30
  return Math.min(100, Math.round((todayMinutes.value / goal) * 100))
})

onMounted(async () => {
  await Promise.all([
    loadLearningStats(),
    loadInProgressCourses(),
    loadRecommendedCourses()
  ])
})

const getTodayStr = () => {
  const d = new Date()
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
}

const isTaskClaimed = (taskId) => {
  return localStorage.getItem(`task_claimed_${getTodayStr()}_${taskId}`) === 'true'
}

const setTaskClaimed = (taskId) => {
  localStorage.setItem(`task_claimed_${getTodayStr()}_${taskId}`, 'true')
}

const loadLearningStats = async () => {
  try {
    const res = await getLearningStats()
    const data = res && res.data ? res.data : res
    if (data) {
      todayMinutes.value = data.todayStudyTime || 0
      currentStreak.value = data.currentStreak || 0
      totalStudyTime.value = data.total_duration || data.totalStudyTime || 0
      totalPoints.value = data.total_xp || data.totalPoints || 0
      completedCourses.value = data.completedCourses || 0

      const loginTime = parseInt(sessionStorage.getItem('loginTime') || '0')
      const onlineMinutes = loginTime > 0 ? Math.floor((Date.now() - loginTime) / 60000) : 0
      const tasks = [
        { id: 1, icon: '📚', name: '完成1节课', reward: 5, completed: (data.today_records || 0) > 0, claimed: isTaskClaimed(1) },
        { id: 2, icon: '⏱️', name: '学习5分钟', reward: 15, completed: onlineMinutes >= 5, claimed: isTaskClaimed(2) }
      ]
      dailyTasks.value = tasks
      completedTasks.value = tasks.filter(t => t.completed).length
      totalTasks.value = tasks.length
    }
  } catch (error) {
    console.error('加载学习统计失败:', error)
    const tasks = [
      { id: 1, icon: '📚', name: '完成1节课', reward: 5, completed: false, claimed: isTaskClaimed(1) },
      { id: 2, icon: '⏱️', name: '学习5分钟', reward: 15, completed: false, claimed: isTaskClaimed(2) }
    ]
    dailyTasks.value = tasks
    completedTasks.value = 0
    totalTasks.value = 2
  }
}

const loadInProgressCourses = async () => {
  loading.value = true
  try {
    const res = await getMyCourses()
    if (res && Array.isArray(res)) {
      inProgressCourses.value = res.map(course => ({
        ...course,
        progress: course.progressPercent || 0,
        completedLessons: Math.floor((course.progressPercent || 0) / 100 * (course.totalLessons || 20)),
        totalLessons: course.totalLessons || 20
      }))
    }
  } catch (error) {
    console.error('加载学习中课程失败:', error)
    inProgressCourses.value = []
  } finally {
    loading.value = false
  }
}

const loadRecommendedCourses = async () => {
  try {
    const res = await getFeaturedCourses(4)
    recommendedCourses.value = res || []
  } catch (error) {
    console.error('加载推荐课程失败:', error)
    recommendedCourses.value = []
  }
}

const continueLearning = (course) => {
  router.push(`/course/${course.courseId}`)
}

const goToCourse = (courseId) => {
  router.push(`/course/${courseId}`)
}

const goToTask = (task) => {
  if (task.id === 1) {
    router.push('/course')
  } else if (task.id === 2) {
    ElMessage.info('保持在线5分钟即可自动完成')
  }
}

const claimReward = async (task) => {
  try {
    await claimTaskReward(task.name, task.reward)
    task.claimed = true
    setTaskClaimed(task.id)
    totalPoints.value += task.reward
    ElMessage.success(`领取成功！+${task.reward} 积分`)
  } catch (error) {
    console.error('领取奖励失败:', error)
    ElMessage.error('领取失败')
  }
}

const getLanguageText = (language) => {
  const langMap = { en: '英语', ja: '日语', zh: '汉语' }
  return langMap[language] || language
}

const formatNumber = (num) => {
  if (num >= 1000) {
    return (num / 1000).toFixed(1) + 'k'
  }
  return num
}

const handleImageError = (e) => {
  e.target.src = ''
}
</script>

<style lang="scss" scoped>
.learning-page {
  h1 {
    font-size: 32px;
    margin-bottom: 24px;
  }

  .progress-card {
    margin-bottom: 20px;

    .progress-overview {
      display: flex;
      gap: 40px;
      align-items: center;

      @media (max-width: 768px) {
        flex-direction: column;
      }

      .progress-item {
        display: flex;
        align-items: center;
        gap: 20px;
      }

      .stats-list {
        flex: 1;
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
        gap: 20px;

        .stat-item {
          display: flex;
          align-items: center;
          gap: 12px;
          padding: 16px;
          background: var(--el-bg-color-page, #f5f7fa);
          border-radius: 8px;

          .stat-icon {
            font-size: 28px;
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
            }
          }
        }
      }
    }
  }

  .continue-card {
    margin-bottom: 20px;

    .continue-list {
      .continue-item {
        display: flex;
        align-items: center;
        gap: 16px;
        padding: 16px;
        border-bottom: 1px solid #ebeef5;

        &:last-child {
          border-bottom: none;
        }

        .course-cover {
          width: 160px;
          height: 90px;
          border-radius: 8px;
          overflow: hidden;
          flex-shrink: 0;

          img {
            width: 100%;
            height: 100%;
            object-fit: cover;
          }
        }

        .course-info {
          flex: 1;

          .course-name {
            font-weight: bold;
            margin-bottom: 8px;
          }

          .course-progress {
            display: flex;
            align-items: center;
            gap: 12px;
            margin-bottom: 8px;

            .el-progress {
              flex: 1;
            }

            .progress-text {
              font-size: 14px;
              color: #409eff;
              font-weight: bold;
              min-width: 40px;
            }
          }

          .course-meta {
            display: flex;
            gap: 16px;
            font-size: 12px;
            color: #909399;
          }
        }
      }
    }
  }

  .recommend-card {
    margin-bottom: 20px;

    .recommend-list {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
      gap: 16px;

      .recommend-item {
        border-radius: 8px;
        overflow: hidden;
        background: var(--el-bg-color-page, #f5f7fa);
        cursor: pointer;
        transition: all 0.3s;

        &:hover {
          transform: translateY(-5px);
          box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        }

        .course-cover {
          position: relative;
          height: 140px;

          img {
            width: 100%;
            height: 100%;
            object-fit: cover;
          }

          .course-language {
            position: absolute;
            top: 8px;
            left: 8px;
            padding: 4px 8px;
            background: rgba(0, 0, 0, 0.6);
            color: white;
            border-radius: 4px;
            font-size: 12px;
          }
        }

        .course-info {
          padding: 12px;

          .course-name {
            font-weight: bold;
            margin-bottom: 8px;
          }

          .course-desc {
            font-size: 12px;
            color: #909399;
            margin-bottom: 8px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }

          .course-meta {
            display: flex;
            gap: 12px;
            font-size: 12px;
            color: #909399;
          }
        }
      }
    }
  }

  .tasks-card {
    margin-bottom: 20px;

    .task-list {
      .task-item {
        display: flex;
        align-items: center;
        gap: 12px;
        padding: 12px;
        border-bottom: 1px solid #ebeef5;

        &:last-child {
          border-bottom: none;
        }

        &.completed {
          .task-icon {
            background: #67c23a;
            color: white;
          }
        }

        .task-icon {
          width: 40px;
          height: 40px;
          border-radius: 50%;
          background: var(--el-bg-color-page, #f5f7fa);
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 20px;
        }

        .task-info {
          flex: 1;

          .task-name {
            font-weight: 500;
            margin-bottom: 4px;
          }

          .task-reward {
            font-size: 12px;
            color: #e6a23c;
          }
        }
      }
    }
  }
}
</style>
