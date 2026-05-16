<template>
  <div class="home-page">
    <!-- 轮播图区域 -->
    <section class="hero-section">
      <div class="hero-content">
        <h1>🍰 开始您的语言学习之旅</h1>
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
      <h2>✨ 为什么选择 LinguaLearn？</h2>
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
        <h2>🌟 热门课程</h2>
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
        <h2>💬 社区动态</h2>
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
  router.push(`/community/post/${postId}`)
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

  // ========================================
  // 轮播图区域 - 马卡龙渐变
  // ========================================
  .hero-section {
    background: linear-gradient(135deg, #FFB6C1 0%, #E6E6FA 50%, #98D8C8 100%);
    color: white;
    padding: 60px 20px;
    text-align: center;
    border-radius: 28px;
    margin-bottom: 40px;
    box-shadow: 0 8px 32px rgba(255, 105, 180, 0.3);
    position: relative;
    overflow: hidden;

    &::before {
      content: '';
      position: absolute;
      top: -50%;
      left: -50%;
      width: 200%;
      height: 200%;
      background: radial-gradient(circle, rgba(255, 255, 255, 0.1) 0%, transparent 70%);
      animation: pulse 3s ease-in-out infinite;
    }

    @keyframes pulse {
      0%, 100% { transform: scale(1); opacity: 0.5; }
      50% { transform: scale(1.1); opacity: 0.3; }
    }

    h1 {
      font-size: 48px;
      margin-bottom: 16px;
      position: relative;
      text-shadow: 0 2px 8px rgba(255, 105, 180, 0.3);
    }

    p {
      font-size: 20px;
      margin-bottom: 32px;
      opacity: 0.95;
      position: relative;
    }

    .hero-buttons {
      display: flex;
      gap: 16px;
      justify-content: center;
      position: relative;

      .el-button {
        padding: 12px 32px;
        font-size: 16px;
        border-radius: 24px;
        font-weight: 600;
        transition: all 0.3s ease;
        
        &:first-child {
          background: white;
          color: #FF69B4;
          border: none;
          box-shadow: 0 4px 16px rgba(255, 255, 255, 0.3);
          
          &:hover {
            transform: translateY(-3px);
            box-shadow: 0 8px 24px rgba(255, 255, 255, 0.4);
          }
        }
        
        &:last-child {
          background: rgba(255, 255, 255, 0.2);
          color: white;
          border: 2px solid rgba(255, 255, 255, 0.5);
          backdrop-filter: blur(10px);
          
          &:hover {
            background: rgba(255, 255, 255, 0.3);
            transform: translateY(-3px);
          }
        }
      }
    }
  }

  // ========================================
  // 章节标题
  // ========================================
  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 24px;

    h2 {
      font-size: 28px;
      margin: 0;
      background: linear-gradient(135deg, #FF69B4 0%, #FFB6C1 100%);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
    }

    .el-button {
      color: #FF69B4;
      font-weight: 500;
      
      &:hover {
        color: #FFB6C1;
      }
    }
  }

  // ========================================
  // 特色功能 - 马卡龙卡片
  // ========================================
  .features-section {
    margin-bottom: 40px;

    h2 {
      font-size: 28px;
      margin-bottom: 24px;
      text-align: center;
      background: linear-gradient(135deg, #FF69B4 0%, #98D8C8 100%);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
    }

    .features-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
      gap: 24px;

      .feature-card {
        background: white;
        padding: 32px;
        border-radius: 24px;
        text-align: center;
        box-shadow: 0 4px 16px rgba(255, 182, 193, 0.2);
        transition: all 0.3s ease;
        position: relative;
        overflow: hidden;

        &::before {
          content: '';
          position: absolute;
          top: 0;
          left: 0;
          right: 0;
          height: 4px;
          background: linear-gradient(90deg, #FFB6C1, #E6E6FA, #98D8C8);
        }

        &:hover {
          transform: translateY(-8px);
          box-shadow: 0 12px 32px rgba(255, 182, 193, 0.3);
        }

        .feature-icon {
          font-size: 56px;
          margin-bottom: 16px;
          animation: bounce 2s ease-in-out infinite;
        }

        @keyframes bounce {
          0%, 100% { transform: translateY(0); }
          50% { transform: translateY(-10px); }
        }

        h3 {
          font-size: 24px;
          margin-bottom: 8px;
          color: #5D5D5D;
        }

        p {
          color: #909090;
          font-size: 14px;
          line-height: 1.6;
        }
      }
    }
  }

  // ========================================
  // 热门课程 - 马卡龙课程卡片
  // ========================================
  .courses-section {
    margin-bottom: 40px;

    .courses-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
      gap: 24px;

      .course-card {
        background: white;
        border-radius: 24px;
        overflow: hidden;
        box-shadow: 0 4px 16px rgba(255, 182, 193, 0.2);
        cursor: pointer;
        transition: all 0.3s ease;

        &:hover {
          transform: translateY(-8px);
          box-shadow: 0 12px 32px rgba(255, 182, 193, 0.3);

          .course-cover img {
            transform: scale(1.1);
          }
        }

        .course-cover {
          position: relative;
          height: 160px;
          overflow: hidden;

          img {
            width: 100%;
            height: 100%;
            object-fit: cover;
            transition: transform 0.3s ease;
          }

          .course-level {
            position: absolute;
            top: 12px;
            right: 12px;
            padding: 6px 16px;
            border-radius: 20px;
            font-size: 12px;
            color: white;
            font-weight: 600;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);

            &.level-1 { 
              background: linear-gradient(135deg, #98D8C8 0%, #7BC5B8 100%);
            }
            &.level-2 { 
              background: linear-gradient(135deg, #FFB347 0%, #FFA500 100%);
            }
            &.level-3 { 
              background: linear-gradient(135deg, #FF69B4 0%, #FF1493 100%);
            }
          }
        }

        .course-info {
          padding: 20px;

          h3 {
            font-size: 18px;
            margin-bottom: 8px;
            color: #5D5D5D;
          }

          .course-desc {
            color: #909090;
            font-size: 14px;
            margin-bottom: 12px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }

          .course-meta {
            display: flex;
            gap: 16px;
            color: #909090;
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

  // ========================================
  // 社区动态 - 马卡龙帖子卡片
  // ========================================
  .community-section {
    margin-bottom: 40px;

    .community-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
      gap: 24px;

      .post-card {
        background: white;
        padding: 24px;
        border-radius: 24px;
        box-shadow: 0 4px 16px rgba(255, 182, 193, 0.2);
        cursor: pointer;
        transition: all 0.3s ease;

        &:hover {
          transform: translateY(-5px);
          box-shadow: 0 12px 32px rgba(255, 182, 193, 0.3);
        }

        .post-header {
          display: flex;
          align-items: center;
          gap: 12px;
          margin-bottom: 16px;

          .user-avatar {
            width: 48px;
            height: 48px;
            border-radius: 50%;
            background: linear-gradient(135deg, #FFB6C1 0%, #E6E6FA 100%);
            color: white;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: bold;
            font-size: 20px;
            box-shadow: 0 4px 12px rgba(255, 182, 193, 0.3);
          }

          .user-info {
            flex: 1;

            .user-name {
              font-weight: 600;
              font-size: 14px;
              color: #5D5D5D;
            }

            .post-time {
              color: #909090;
              font-size: 12px;
            }
          }
        }

        .post-title {
          font-size: 16px;
          margin-bottom: 8px;
          font-weight: 600;
          color: #5D5D5D;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
          overflow: hidden;
          line-height: 1.5;
        }

        .post-content {
          color: #909090;
          font-size: 14px;
          margin-bottom: 16px;
          display: -webkit-box;
          -webkit-line-clamp: 3;
          -webkit-box-orient: vertical;
          overflow: hidden;
          line-height: 1.6;
        }

        .post-footer {
          display: flex;
          justify-content: space-between;
          align-items: center;

          .post-tag {
            font-size: 12px;
            color: #FF69B4;
            background: rgba(255, 105, 180, 0.1);
            padding: 4px 12px;
            border-radius: 16px;
            font-weight: 500;
          }

          .post-stats {
            display: flex;
            gap: 16px;
            color: #909090;
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

// ========================================
// 响应式设计
// ========================================
@media (max-width: 768px) {
  .home-page {
    padding: 16px;

    .hero-section {
      padding: 40px 20px;
      border-radius: 20px;
      margin-bottom: 32px;

      h1 {
        font-size: 32px;
      }

      p {
        font-size: 16px;
      }

      .hero-buttons {
        flex-direction: column;
        align-items: center;

        .el-button {
          width: 100%;
          max-width: 300px;
        }
      }
    }

    .section-header {
      h2 {
        font-size: 24px;
      }
    }

    .features-section {
      h2 {
        font-size: 24px;
      }

      .features-grid {
        gap: 16px;

        .feature-card {
          padding: 24px;

          .feature-icon {
            font-size: 40px;
          }

          h3 {
            font-size: 20px;
          }
        }
      }
    }

    .courses-section, .community-section {
      .courses-grid, .community-grid {
        gap: 16px;

        .course-card, .post-card {
          border-radius: 20px;

          .course-cover {
            height: 140px;
          }

          .course-info, .post-card {
            padding: 16px;
          }
        }
      }
    }
  }
}
</style>
