<template>
  <div class="course-detail" :class="{ 'dark-mode': themeStore.isDark }">
    <el-page-header @back="$router.back()" content="课程详情" />

    <div class="course-content" v-loading="loading">
      <div class="course-header" v-if="course.courseName">
        <div class="course-cover">
          <img :src="course.coverImage || '/default-course.jpg'" :alt="course.courseName" @error="handleImageError">
          <div class="course-badges">
            <el-tag :type="getLevelType(course.level)">{{ getLevelText(course.level) }}</el-tag>
            <el-tag type="info">{{ getLanguageText(course.language) }}</el-tag>
          </div>
        </div>
        <div class="course-info">
          <h1>{{ course.courseName }}</h1>
          <p class="course-desc">{{ course.description }}</p>
          <div class="course-stats">
            <div class="stat-item">
              <el-icon><User /></el-icon>
              <span>{{ course.studentCount }} 学员</span>
            </div>
            <div class="stat-item">
              <el-icon><Reading /></el-icon>
              <span>{{ course.lessonCount }} 课时</span>
            </div>
            <div class="stat-item">
              <el-icon><Star /></el-icon>
              <span>{{ course.rating }} 评分</span>
            </div>
            <div class="stat-item">
              <el-icon><Clock /></el-icon>
              <span>{{ course.totalDuration }} 分钟</span>
            </div>
          </div>
        </div>
      </div>

      <div class="course-body" v-if="course.courseName">
        <div class="course-main">
          <el-card class="intro-card">
            <template #header>
              <div class="card-header">
                <span>课程介绍</span>
              </div>
            </template>
            <p>{{ course.description || '暂无课程介绍' }}</p>
          </el-card>

          <el-card class="units-card" v-if="course.units && course.units.length > 0">
            <template #header>
              <div class="card-header">
                <span>课程章节</span>
                <span class="unit-count">共 {{ getTotalUnits }} 单元，{{ getTotalLessons }} 课时</span>
              </div>
            </template>
            <el-collapse>
              <el-collapse-item
                v-for="(unit, index) in course.units"
                :key="unit.unitId"
                :title="`${index + 1}. ${unit.unitName}`"
              >
                <template #title>
                  <div class="unit-title">
                    <span class="unit-name">{{ unit.unitName }}</span>
                    <el-tag size="small" type="info">{{ unit.lessons?.length || 0 }} 课时</el-tag>
                  </div>
                </template>
                <div class="lessons">
                  <div
                    v-for="(lesson, lIndex) in unit.lessons"
                    :key="lesson.lessonId"
                    class="lesson-item"
                    :class="{ 'lesson-completed': lesson.completed }"
                    @click="startLearning(lesson)"
                  >
                    <div class="lesson-number">{{ lIndex + 1 }}</div>
                    <div class="lesson-info">
                      <span class="lesson-name">{{ lesson.lessonName }}</span>
                      <span class="lesson-duration">{{ lesson.duration }}分钟</span>
                    </div>
                    <el-tag v-if="lesson.completed" type="success" size="small">已完成</el-tag>
                    <el-button v-else type="primary" size="small">开始学习</el-button>
                  </div>
                </div>
              </el-collapse-item>
            </el-collapse>
          </el-card>
          
          <el-empty v-else description="暂无课程章节" />
        </div>

        <div class="course-sidebar">
          <el-card class="enroll-card">
            <template #header>
              <div class="card-header">
                <span>课程信息</span>
              </div>
            </template>
            <div class="info-list">
              <div class="info-item">
                <span class="label">课程等级：</span>
                <span class="value">
                  <el-tag :type="getLevelType(course.level)" size="small">
                    {{ getLevelText(course.level) }}
                  </el-tag>
                </span>
              </div>
              <div class="info-item">
                <span class="label">学习语言：</span>
                <span class="value">{{ getLanguageText(course.language) }}</span>
              </div>
              <div class="info-item">
                <span class="label">课程时长：</span>
                <span class="value">{{ course.totalDuration }}分钟</span>
              </div>
              <div class="info-item">
                <span class="label">学员数量：</span>
                <span class="value">{{ course.studentCount }} 人</span>
              </div>
              <!-- <div class="info-item">
                <span class="label">课程评分：</span>
                <span class="value rating-value">
                  <el-icon><Star /></el-icon> {{ course.rating }}
                </span>
              </div> -->
            </div>

            <el-button
              type="primary"
              size="large"
              class="enroll-btn"
              @click="handleEnroll"
              v-if="!isEnrolled"
            >
              立即报名
            </el-button>
            <el-button
              type="warning"
              size="large"
              class="enroll-btn"
              @click="handleCancelEnroll"
              v-else
            >
              取消报名
            </el-button>
          </el-card>
        </div>
      </div>
      
      <el-empty v-else description="课程不存在或加载失败" />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { useThemeStore } from '@/stores/theme'
import { getCourseDetail, enrollCourse, cancelEnroll } from '@/api/course'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const themeStore = useThemeStore()

const loading = ref(false)
const course = ref({})
const isEnrolled = ref(false)

const getTotalUnits = computed(() => course.value.units?.length || 0)
const getTotalLessons = computed(() => {
  if (!course.value.units) return 0
  return course.value.units.reduce((sum, unit) => sum + (unit.lessons?.length || 0), 0)
})

onMounted(async () => {
  // 检查登录状态
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录后再查看课程详情')
    router.push({ path: '/login', query: { redirect: route.fullPath } })
    return
  }
  
  await loadCourseDetail()
})

const loadCourseDetail = async () => {
  loading.value = true
  try {
    const courseId = route.params.id
    const res = await getCourseDetail(courseId)
    if (res) {
      course.value = res
      isEnrolled.value = res.isEnrolled === true
    } else {
      ElMessage.warning('课程不存在')
    }
  } catch (error) {
    console.error('加载课程详情失败:', error)
    ElMessage.error('加载课程详情失败')
  } finally {
    loading.value = false
  }
}

const handleEnroll = async () => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录后再报名')
    router.push('/login')
    return
  }
  try {
    await enrollCourse(course.value.courseId)
    ElMessage.success('报名成功！')
    isEnrolled.value = true
  } catch (error) {
    console.error('报名失败:', error)
    if (error.message && error.message.includes('已报名')) {
      isEnrolled.value = true
      ElMessage.warning('您已报名该课程')
    } else {
      ElMessage.error('报名失败')
    }
  }
}

const handleCancelEnroll = async () => {
  try {
    await cancelEnroll(course.value.courseId)
    ElMessage.success('取消报名成功！')
    isEnrolled.value = false
  } catch (error) {
    console.error('取消报名失败:', error)
    if (error.message && error.message.includes('尚未报名')) {
      isEnrolled.value = false
      ElMessage.warning('您尚未报名该课程')
    } else {
      ElMessage.error('取消报名失败')
    }
  }
}

const startLearning = (lesson) => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录后再学习')
    router.push('/login')
    return
  }
  router.push(`/learning/lesson/${lesson.lessonId}`)
}

const getLevelText = (level) => {
  const levelMap = { 1: '初级', 2: '中级', 3: '高级' }
  return levelMap[level] || '初级'
}

const getLevelType = (level) => {
  const typeMap = { 1: 'success', 2: 'warning', 3: 'danger' }
  return typeMap[level] || 'info'
}

const getLanguageText = (language) => {
  const langMap = { en: '英语', ja: '日语', zh: '汉语' }
  return langMap[language] || language
}

const handleImageError = (e) => {
  e.target.style.display = 'none'
}
</script>

<style lang="scss" scoped>
.course-detail {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  overflow-x: hidden;

  h1 {
    font-size: 28px;
    margin-bottom: 16px;
  }

  .course-content {
    margin-top: 20px;

    .course-header {
      display: flex;
      gap: 20px;
      margin-bottom: 20px;
      background: var(--el-bg-color, white);
      padding: 20px;
      border-radius: 12px;
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

      .course-cover {
        width: 360px;
        height: 220px;
        border-radius: 12px;
        overflow: hidden;
        position: relative;
        flex-shrink: 0;

        img {
          width: 100%;
          height: 100%;
          object-fit: cover;
        }

        .course-badges {
          position: absolute;
          top: 12px;
          left: 12px;
          display: flex;
          gap: 8px;
        }
      }

      .course-info {
        flex: 1;
        min-width: 0;
        display: flex;
        flex-direction: column;

        h1 {
          font-size: 24px;
          margin-bottom: 10px;
        }

        .course-desc {
          color: #606266;
          line-height: 1.6;
          margin-bottom: 16px;
          flex: 1;
        }

        .course-stats {
          display: flex;
          gap: 20px;
          flex-wrap: wrap;

          .stat-item {
            display: flex;
            align-items: center;
            gap: 6px;
            color: #909399;
            font-size: 14px;

            .el-icon {
              color: #409eff;
            }
          }
        }
      }
    }

    .course-body {
      display: grid;
      grid-template-columns: 1fr 320px;
      gap: 20px;

      .course-main {
        min-width: 0;

        .intro-card {
          margin-bottom: 16px;
          
          p {
            line-height: 1.8;
            color: #606266;
          }
        }

        .units-card {
          .card-header {
            display: flex;
            justify-content: space-between;
            align-items: center;

            .unit-count {
              font-size: 12px;
              color: #909399;
            }
          }

          .unit-title {
            display: flex;
            justify-content: space-between;
            align-items: center;
            width: 100%;
            padding-right: 20px;

            .unit-name {
              font-weight: 500;
            }
          }

          .lessons {
            .lesson-item {
              display: flex;
              align-items: center;
              gap: 12px;
              padding: 12px;
              border-radius: 8px;
              margin-bottom: 8px;
              background: var(--el-bg-color-page, #f5f7fa);
              cursor: pointer;
              transition: all 0.3s;

              &:last-child {
                margin-bottom: 0;
              }

              &:hover {
                background: #ecf5ff;
                transform: translateX(5px);
              }

              .lesson-number {
                width: 28px;
                height: 28px;
                border-radius: 50%;
                background: #409eff;
                color: white;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 14px;
                font-weight: bold;
                flex-shrink: 0;
              }

              &.lesson-completed {
                background: rgba(103, 194, 58, 0.12);
                border-left: 4px solid #67c23a;
                
                .lesson-number {
                  background: #67c23a;
                }
                
                .lesson-name {
                  color: #67c23a;
                  font-weight: 500;
                }
                
                &:hover {
                  background: rgba(103, 194, 58, 0.2);
                }
              }

              .lesson-info {
                flex: 1;
                min-width: 0;
                display: flex;
                flex-direction: column;
                gap: 4px;

                .lesson-name {
                  font-size: 14px;
                  color: #303133;
                }

                .lesson-duration {
                  font-size: 12px;
                  color: #909399;
                }
              }
            }
          }
        }
      }

      .course-sidebar {
        .enroll-card {
          position: sticky;
          top: 20px;

          .info-list {
            .info-item {
              display: flex;
              justify-content: space-between;
              align-items: center;
              padding: 12px 0;
              border-bottom: 1px solid #ebeef5;

              &:last-child {
                border-bottom: none;
              }

              .label {
                color: #909399;
                flex-shrink: 0;
              }

              .value {
                color: #303133;
                font-weight: 500;
                text-align: right;

                &.rating-value {
                  color: #e6a23c;
                  display: flex;
                  align-items: center;
                  gap: 4px;
                }
              }
            }
          }

          .enroll-btn {
            width: 100%;
            margin-top: 16px;
            height: 48px;
            font-size: 16px;
          }
        }
      }
    }
  }
}

@media (max-width: 992px) {
  .course-detail {
    .course-content {
      .course-header {
        flex-direction: column;

        .course-cover {
          width: 100%;
          height: 200px;
        }

        .course-info {
          h1 {
            font-size: 22px;
          }

          .course-stats {
            gap: 12px;
          }
        }
      }

      .course-body {
        grid-template-columns: 1fr;

        .course-sidebar {
          .enroll-card {
            position: static;
          }
        }
      }
    }
  }
}

@media (max-width: 768px) {
  .course-detail {
    padding: 12px;

    h1 {
      font-size: 22px;
    }

    .course-content {
      margin-top: 12px;

      .course-header {
        padding: 12px;
        gap: 12px;

        .course-cover {
          height: 180px;
        }

        .course-info {
          h1 {
            font-size: 18px;
            margin-bottom: 8px;
          }

          .course-desc {
            font-size: 13px;
            margin-bottom: 12px;
          }

          .course-stats {
            gap: 8px;

            .stat-item {
              font-size: 12px;
            }
          }
        }
      }

      .course-body {
        gap: 12px;

        .course-main {
          .intro-card {
            p {
              font-size: 13px;
            }
          }

          .units-card {
            .lessons {
              .lesson-item {
                padding: 10px;
                gap: 8px;

                .lesson-number {
                  width: 24px;
                  height: 24px;
                  font-size: 12px;
                }

                .lesson-info {
                  .lesson-name {
                    font-size: 13px;
                  }
                }
              }
            }
          }
        }

        .course-sidebar {
          .enroll-card {
            .info-list {
              .info-item {
                padding: 8px 0;
                font-size: 13px;
              }
            }

            .enroll-btn {
              height: 42px;
              font-size: 14px;
            }
          }
        }
      }
    }
  }
}

@media (max-width: 480px) {
  .course-detail {
    padding: 8px;

    .course-content {
      .course-header {
        padding: 8px;
        border-radius: 8px;

        .course-cover {
          height: 150px;
          border-radius: 8px;
        }

        .course-info {
          h1 {
            font-size: 16px;
          }

          .course-desc {
            font-size: 12px;
            -webkit-line-clamp: 3;
            display: -webkit-box;
            -webkit-box-orient: vertical;
            overflow: hidden;
          }

          .course-stats {
            gap: 6px;

            .stat-item {
              font-size: 11px;
            }
          }
        }
      }
    }
  }
}

.course-detail.dark-mode {
  .course-content {
    .course-header {
      background: #1e2e2e;
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.3);

      .course-info {
        .course-desc {
          color: #8aa89a;
        }

        .course-stats {
          .stat-item {
            color: #6a8a7a;
          }
        }
      }
    }

    .course-body {
      .course-main {
        .intro-card {
          p {
            color: #8aa89a;
          }
        }

        .units-card {
          .lessons {
            .lesson-item {
              background: #1a2a2a;

              &:hover {
                background: #253535;
              }

              .lesson-info {
                .lesson-name {
                  color: #d0e0d8;
                }

                .lesson-duration {
                  color: #6a8a7a;
                }
              }

              &.lesson-completed {
                background: rgba(152, 216, 200, 0.08);
              }
            }
          }
        }
      }

      .course-sidebar {
        .enroll-card {
          .info-list {
            .info-item {
              border-bottom-color: #2a3a3a;

              .label {
                color: #6a8a7a;
              }

              .value {
                color: #d0e0d8;
              }
            }
          }
        }
      }
    }
  }
}
</style>
