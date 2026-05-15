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
      <div class="section-header">
        <h2>热门课程</h2>
        <el-button text @click="$router.push('/course')">查看全部 →</el-button>
      </div>
      <div class="courses-grid" v-loading="coursesLoading">
        <div
          v-for="course in courses"
          :key="course.courseId"
          class="course-card"
          @click="goToCourse(course.courseId)"
        >
          <div class="course-cover">
            <img :src="course.coverImage || '/default-course.jpg'" :alt="course.courseName" />
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
        <div v-if="courses.length === 0 && !coursesLoading" class="empty-state">
          <el-empty description="暂无课程数据" />
        </div>
      </div>
    </section>

    <!-- 社区动态 -->
    <section class="community-section">
      <div class="section-header">
        <h2>社区动态</h2>
        <el-button text @click="$router.push('/community')">查看全部 →</el-button>
      </div>
      <div class="community-grid" v-loading="postsLoading">
        <div
          v-for="post in hotPosts"
          :key="post.postId"
          class="post-card"
          @click="goToPost(post.postId)"
        >
          <div class="post-header">
            <div class="user-avatar">{{ post.userName ? post.userName.charAt(0).toUpperCase() : 'U' }}</div>
            <div class="user-info">
              <div class="user-name">{{ post.userName || '匿名用户' }}</div>
              <div class="post-time">{{ formatTime(post.createTime) }}</div>
            </div>
          </div>
          <h3 class="post-title">{{ post.title }}</h3>
          <p class="post-content">{{ truncateText(post.content, 100) }}</p>
          <div class="post-footer">
            <span class="post-tag" v-if="post.language">{{ getLanguageName(post.language) }}</span>
            <div class="post-stats">
              <span><el-icon><View /></el-icon> {{ post.viewCount || 0 }}</span>
              <span><el-icon><ChatDotRound /></el-icon> {{ post.commentCount || 0 }}</span>
              <span><el-icon><Star /></el-icon> {{ post.likeCount || 0 }}</span>
            </div>
          </div>
        </div>
        <div v-if="hotPosts.length === 0 && !postsLoading" class="empty-state">
          <el-empty description="暂无社区动态" />
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { getFeaturedCourses } from '@/api/course'
import { getHotPosts } from '@/api/community'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const coursesLoading = ref(false)
const postsLoading = ref(false)
const courses = ref([])
const hotPosts = ref([])

onMounted(async () => {
  await Promise.all([
    loadCourses(),
    loadHotPosts()
  ])
})

const loadCourses = async () => {
  coursesLoading.value = true
  try {
    const res = await getFeaturedCourses(6)
    courses.value = res || []
  } catch (error) {
    console.error('加载热门课程失败:', error)
  } finally {
    coursesLoading.value = false
  }
}

const loadHotPosts = async () => {
  postsLoading.value = true
  try {
    const res = await getHotPosts(3)
    hotPosts.value = res || []
  } catch (error) {
    console.error('加载社区动态失败:', error)
  } finally {
    postsLoading.value = false
  }
}

const goToCourse = (courseId) => {
  router.push(`/course/${courseId}`)
}

const goToPost = (postId) => {
  router.push(`/community/${postId}`)
}

const getLevelText = (level) => {
  const levelMap = { 1: '初级', 2: '中级', 3: '高级' }
  return levelMap[level] || '初级'
}

const getLanguageName = (lang) => {
  const langMap = { 'en': '英语', 'ja': '日语', 'zh': '汉语' }
  return langMap[lang] || lang
}

const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  const now = new Date()
  const diff = now - date
  const minute = 60 * 1000
  const hour = 60 * minute
  const day = 24 * hour
  
  if (diff < minute) return '刚刚'
  if (diff < hour) return `${Math.floor(diff / minute)}分钟前`
  if (diff < day) return `${Math.floor(diff / hour)}小时前`
  return `${Math.floor(diff / day)}天前`
}

const truncateText = (text, max) => {
  if (!text) return ''
  if (text.length <= max) return text
  return text.substring(0, max) + '...'
}
</script>

<style lang="scss" scoped>
.home-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;

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

  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 24px;

    h2 {
      font-size: 28px;
      margin: 0;
    }
  }

  .features-section {
    margin-bottom: 40px;

    h2 {
      font-size: 28px;
      margin-bottom: 24px;
      text-align: center;
    }

    .features-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
      gap: 24px;

      .feature-card {
        background: var(--el-bg-color, white);
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
  }

  .courses-section {
    margin-bottom: 40px;

    .courses-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
      gap: 24px;

      .course-card {
        background: var(--el-bg-color, white);
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

    .empty-state {
      grid-column: 1 / -1;
      padding: 40px;
    }
  }

  .community-section {
    margin-bottom: 40px;

    .community-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
      gap: 24px;

      .post-card {
        background: var(--el-bg-color, white);
        padding: 20px;
        border-radius: 12px;
        box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
        cursor: pointer;
        transition: transform 0.3s;

        &:hover {
          transform: translateY(-3px);
        }

        .post-header {
          display: flex;
          align-items: center;
          gap: 12px;
          margin-bottom: 16px;

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
            font-size: 18px;
          }

          .user-info {
            flex: 1;

            .user-name {
              font-weight: 600;
              font-size: 14px;
            }

            .post-time {
              color: #909399;
              font-size: 12px;
            }
          }
        }

        .post-title {
          font-size: 16px;
          margin-bottom: 8px;
          font-weight: 600;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
          overflow: hidden;
        }

        .post-content {
          color: #606266;
          font-size: 14px;
          margin-bottom: 16px;
          display: -webkit-box;
          -webkit-line-clamp: 3;
          -webkit-box-orient: vertical;
          overflow: hidden;
        }

        .post-footer {
          display: flex;
          justify-content: space-between;
          align-items: center;

          .post-tag {
            font-size: 12px;
            color: #409eff;
            background: rgba(64, 158, 255, 0.1);
            padding: 2px 8px;
            border-radius: 4px;
          }

          .post-stats {
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

      .empty-state {
        grid-column: 1 / -1;
        padding: 40px;
      }
    }
  }
}
</style>
