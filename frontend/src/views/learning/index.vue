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
            <div class="progress-value">{{ userStore.dailyGoal || 30 }} 分钟</div>
          </div>
        </div>

        <div class="stats-list">
          <div class="stat-item">
            <span class="stat-icon">🔥</span>
            <div class="stat-content">
              <div class="stat-value">{{ userStore.currentStreak || 0 }}</div>
              <div class="stat-label">连续学习天数</div>
            </div>
          </div>

          <div class="stat-item">
            <span class="stat-icon">⏱️</span>
            <div class="stat-content">
              <div class="stat-value">{{ userStore.totalStudyTime || 0 }}</div>
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
              <div class="stat-value">{{ userStore.totalPoints || 0 }}</div>
              <div class="stat-label">总积分</div>
            </div>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 学习模式选择 -->
    <el-card class="modes-card">
      <template #header>
        <div class="card-header">
          <span>学习模式</span>
        </div>
      </template>

      <div class="learning-modes">
        <div 
          v-for="mode in learningModes" 
          :key="mode.id"
          class="mode-item"
          @click="selectMode(mode)"
        >
          <div class="mode-icon">{{ mode.icon }}</div>
          <div class="mode-info">
            <div class="mode-name">{{ mode.name }}</div>
            <div class="mode-desc">{{ mode.description }}</div>
          </div>
          <el-tag size="small" type="info">{{ mode.count }} 个内容</el-tag>
        </div>
      </div>
    </el-card>

    <!-- 继续学习 -->
    <el-card class="continue-card">
      <template #header>
        <div class="card-header">
          <span>继续学习</span>
          <el-button type="text" @click="$router.push('/course')">查看全部课程</el-button>
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
          <el-button 
            v-if="!task.completed" 
            type="primary" 
            size="small"
            @click="completeTask(task)"
          >
            去完成
          </el-button>
          <el-tag v-else type="success" size="small">已完成</el-tag>
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
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const loading = ref(false)
const inProgressCourses = ref([])
const recommendedCourses = ref([])
const todayMinutes = ref(45)

const completedCourses = ref(2)

const learningModes = ref([
  { 
    id: 1, 
    icon: '📝', 
    name: '单词记忆', 
    description: '使用联想记忆法高效背单词',
    count: 156
  },
  { 
    id: 2, 
    icon: '📖', 
    name: '语法学习', 
    description: '系统学习语言语法规则',
    count: 45
  },
  { 
    id: 3, 
    icon: '🎧', 
    name: '听力训练', 
    description: '提升听力理解能力',
    count: 30
  },
  { 
    id: 4, 
    icon: '🗣️', 
    name: '口语练习', 
    description: '跟读练习提升口语',
    count: 25
  }
])

const dailyTasks = ref([
  { id: 1, icon: '📚', name: '完成1节课', reward: 10, completed: true },
  { id: 2, icon: '⏱️', name: '学习30分钟', reward: 15, completed: true },
  { id: 3, icon: '🔥', name: '连续签到', reward: 20, completed: false },
  { id: 4, icon: '🏆', name: '完成每日挑战', reward: 25, completed: false }
])

const completedTasks = computed(() => dailyTasks.value.filter(t => t.completed).length)
const totalTasks = computed(() => dailyTasks.value.length)

const dailyProgress = computed(() => {
  const goal = userStore.dailyGoal || 30
  return Math.min(100, Math.round((todayMinutes.value / goal) * 100))
})

onMounted(async () => {
  await loadInProgressCourses()
  await loadRecommendedCourses()
})

const loadInProgressCourses = async () => {
  loading.value = true
  try {
    const res = await getMyCourses()
    inProgressCourses.value = (res.data || []).map(course => ({
      ...course,
      progress: Math.floor(Math.random() * 80) + 10,
      completedLessons: Math.floor(Math.random() * course.lessonCount),
      lastLesson: `第${Math.floor(Math.random() * course.lessonCount) + 1}课`
    }))
  } catch (error) {
    console.error('加载学习中课程失败:', error)
    inProgressCourses.value = [
      {
        courseId: 1,
        courseName: '英语入门 - 基础词汇',
        coverImage: 'https://via.placeholder.com/200x120/409eff/ffffff?text=English',
        progress: 65,
        completedLessons: 13,
        totalLessons: 20,
        lastLesson: '第13课'
      },
      {
        courseId: 2,
        courseName: '日语入门 - 五十音图',
        coverImage: 'https://via.placeholder.com/200x120/f56c6c/ffffff?text=Japanese',
        progress: 40,
        completedLessons: 10,
        totalLessons: 25,
        lastLesson: '第10课'
      }
    ]
  } finally {
    loading.value = false
  }
}

const loadRecommendedCourses = async () => {
  try {
    const res = await getFeaturedCourses(4)
    recommendedCourses.value = res.data || []
  } catch (error) {
    console.error('加载推荐课程失败:', error)
    recommendedCourses.value = []
  }
}

const selectMode = (mode) => {
  ElMessage.info(`即将进入${mode.name}模式`)
}

const continueLearning = (course) => {
  router.push(`/course/${course.courseId}`)
}

const goToCourse = (courseId) => {
  router.push(`/course/${courseId}`)
}

const completeTask = (task) => {
  ElMessage.info(`即将前往完成：${task.name}`)
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
  e.target.src = 'https://via.placeholder.com/200x120/409eff/ffffff?text=Course'
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

  .modes-card {
    margin-bottom: 20px;

    .learning-modes {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
      gap: 16px;

      .mode-item {
        display: flex;
        align-items: center;
        gap: 12px;
        padding: 16px;
        background: var(--el-bg-color-page, #f5f7fa);
        border-radius: 8px;
        cursor: pointer;
        transition: all 0.3s;

        &:hover {
          background: #ecf5ff;
          transform: translateX(5px);
        }

        .mode-icon {
          font-size: 32px;
        }

        .mode-info {
          flex: 1;

          .mode-name {
            font-weight: bold;
            margin-bottom: 4px;
          }

          .mode-desc {
            font-size: 12px;
            color: #909399;
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
