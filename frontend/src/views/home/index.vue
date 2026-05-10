<template>
  <div class="home-page">
    <!-- 轮播图区域 -->
    <section class="hero-section">
      <div class="hero-content">
        <h1>🎓 开始您的语言学习之旅</h1>
        <p>支持英语、日语、汉语等多语种学习，沉浸式语言学习体验</p>
        <div class="hero-buttons">
          <el-button type="primary" size="large" @click="$router.push('/course')">
            开始学习
          </el-button>
          <el-button size="large" @click="$router.push('/register')" v-if="!userStore.isLoggedIn">
            立即注册
          </el-button>
        </div>
      </div>
    </section>

    <!-- 特色功能 -->
    <section class="features-section">
      <h2>为什么选择 LinguaLearn？</h2>
      <div class="features-grid">
        <div class="feature-card">
          <div class="feature-icon">📚</div>
          <h3>分级课程</h3>
          <p>从零基础到高级，科学的分级体系让学习更高效</p>
        </div>
        <div class="feature-card">
          <div class="feature-icon">🎯</div>
          <h3>闯关天梯</h3>
          <p>游戏化学习体验，挑战自我不断提升</p>
        </div>
        <div class="feature-card">
          <div class="feature-icon">🏆</div>
          <h3>成就系统</h3>
          <p>丰富的成就奖励，激发学习动力</p>
        </div>
        <div class="feature-card">
          <div class="feature-icon">👥</div>
          <h3>社区交流</h3>
          <p>与学习伙伴交流互动，共同进步</p>
        </div>
      </div>
    </section>

    <!-- 热门课程 -->
    <section class="courses-section">
      <h2>热门课程</h2>
      <div class="courses-grid" v-loading="loading">
        <div
          v-for="course in courses"
          :key="course.courseId"
          class="course-card"
          @click="goToCourse(course.courseId)"
        >
          <div class="course-cover">
            <img :src="course.coverImage || '/default-course.jpg'" :alt="course.courseName">
            <span class="course-level" :class="'level-' + course.level">{{ getLevelText(course.level) }}</span>
          </div>
          <div class="course-info">
            <h3>{{ course.courseName }}</h3>
            <p class="course-desc">{{ course.description || '暂无描述' }}</p>
            <div class="course-meta">
              <span><el-icon><User /></el-icon> {{ course.studentCount || 0 }} 学员</span>
              <span><el-icon><Star /></el-icon> {{ course.rating || 0 }}</span>
            </div>
          </div>
        </div>
      </div>
      <div class="more-courses">
        <el-button @click="$router.push('/course')">查看更多课程</el-button>
      </div>
    </section>

    <!-- 学习统计 -->
    <section class="stats-section" v-if="userStore.isLoggedIn">
      <h2>我的学习数据</h2>
      <div class="stats-grid">
        <div class="stat-card">
          <div class="stat-value">{{ stats.totalLearningMinutes || 0 }}</div>
          <div class="stat-label">学习时长(分钟)</div>
        </div>
        <div class="stat-card">
          <div class="stat-value">{{ stats.streakDays || 0 }}</div>
          <div class="stat-label">连续学习天数</div>
        </div>
        <div class="stat-card">
          <div class="stat-value">{{ stats.completedCourses || 0 }}</div>
          <div class="stat-label">已完成课程</div>
        </div>
        <div class="stat-card">
          <div class="stat-value">{{ stats.achievements || 0 }}</div>
          <div class="stat-label">获得成就</div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { getCourseList } from '@/api/course'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const loading = ref(false)
const courses = ref([])
const stats = ref({})

onMounted(async () => {
  await loadCourses()
  if (userStore.isLoggedIn) {
    await loadStats()
  }
})

const loadCourses = async () => {
  loading.value = true
  try {
    const res = await getCourseList({ pageNum: 1, pageSize: 6 })
    courses.value = res.data?.rows || []
  } catch (error) {
    console.error('加载课程失败:', error)
    ElMessage.error('加载课程失败')
  } finally {
    loading.value = false
  }
}

const loadStats = async () => {
  stats.value = {
    totalLearningMinutes: 120,
    streakDays: 7,
    completedCourses: 3,
    achievements: 5
  }
}

const goToCourse = (courseId) => {
  router.push(`/course/${courseId}`)
}

const getLevelText = (level) => {
  const levelMap = { 1: '初级', 2: '中级', 3: '高级' }
  return levelMap[level] || '初级'
}
</script>

<style lang="scss" scoped>
.home-page {
  .hero-section {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    padding: 60px 20px;
    text-align: center;
    border-radius: 12px;
    margin-bottom: 40px;

    h1 {
      font-size: 48px;
      margin-bottom: 16px;
    }

    p {
      font-size: 20px;
      margin-bottom: 32px;
      opacity: 0.9;
    }

    .hero-buttons {
      display: flex;
      gap: 16px;
      justify-content: center;
    }
  }

  .features-section, .courses-section, .stats-section {
    margin-bottom: 40px;

    h2 {
      font-size: 32px;
      margin-bottom: 24px;
      text-align: center;
    }
  }

  .features-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
    gap: 24px;

    .feature-card {
      background: white;
      padding: 32px;
      border-radius: 12px;
      text-align: center;
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
      transition: transform 0.3s;

      &:hover {
        transform: translateY(-5px);
      }

      .feature-icon {
        font-size: 48px;
        margin-bottom: 16px;
      }

      h3 {
        font-size: 24px;
        margin-bottom: 8px;
      }

      p {
        color: #909399;
      }
    }
  }

  .courses-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
    gap: 24px;

    .course-card {
      background: white;
      border-radius: 12px;
      overflow: hidden;
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
      cursor: pointer;
      transition: transform 0.3s;

      &:hover {
        transform: translateY(-5px);
      }

      .course-cover {
        position: relative;
        height: 160px;

        img {
          width: 100%;
          height: 100%;
          object-fit: cover;
        }

        .course-level {
          position: absolute;
          top: 12px;
          right: 12px;
          padding: 4px 12px;
          border-radius: 12px;
          font-size: 12px;
          color: white;

          &.level-1 { background: #67c23a; }
          &.level-2 { background: #e6a23c; }
          &.level-3 { background: #f56c6c; }
        }
      }

      .course-info {
        padding: 16px;

        h3 {
          font-size: 18px;
          margin-bottom: 8px;
        }

        .course-desc {
          color: #909399;
          font-size: 14px;
          margin-bottom: 12px;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }

        .course-meta {
          display: flex;
          gap: 16px;
          color: #909399;
          font-size: 14px;

          span {
            display: flex;
            align-items: center;
            gap: 4px;
          }
        }
      }
    }
  }

  .more-courses {
    text-align: center;
    margin-top: 24px;
  }

  .stats-section {
    .stats-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
      gap: 24px;

      .stat-card {
        background: white;
        padding: 32px;
        border-radius: 12px;
        text-align: center;
        box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

        .stat-value {
          font-size: 48px;
          font-weight: bold;
          color: #409eff;
          margin-bottom: 8px;
        }

        .stat-label {
          color: #909399;
          font-size: 16px;
        }
      }
    }
  }
}
</style>
