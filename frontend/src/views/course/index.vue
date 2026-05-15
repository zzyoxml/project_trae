<template>
  <div class="course-page">
    <h1>📚 课程中心</h1>

    <!-- 筛选器 -->
    <div class="filters">
      <el-select v-model="filters.language" placeholder="选择语言" clearable @change="handleFilterChange">
        <el-option label="全部语言" value="" />
        <el-option label="英语" value="en" />
        <el-option label="日语" value="ja" />
        <el-option label="汉语" value="zh" />
      </el-select>

      <el-select v-model="filters.level" placeholder="选择等级" clearable @change="handleFilterChange">
        <el-option label="全部等级" value="" />
        <el-option label="初级" value="beginner" />
        <el-option label="中级" value="intermediate" />
        <el-option label="高级" value="advanced" />
      </el-select>

      <!-- <el-select v-model="filters.category" placeholder="选择分类" clearable @change="handleFilterChange">
        <el-option label="全部分类" value="" />
        <el-option label="单词记忆" value="vocabulary" />
        <el-option label="语法练习" value="grammar" />
        <el-option label="听力训练" value="listening" />
        <el-option label="口语练习" value="speaking" />
      </el-select> -->

      <el-select v-model="filters.sortBy" placeholder="排序方式" @change="handleSortChange">
        <el-option label="默认排序" value="default" />
        <!-- <el-option label="按评分排序" value="rating" /> -->
        <el-option label="按学习人数排序" value="students" />
        <el-option label="按课时数排序" value="lessons" />
      </el-select>

      <el-button @click="resetFilters">重置筛选</el-button>
    </div>

    <!-- 课程列表 -->
    <div class="course-list" v-loading="loading">
      <div
        v-for="course in courses"
        :key="course.courseId"
        class="course-card"
        @click="goToDetail(course.courseId)"
      >
        <div class="course-cover">
          <img :src="course.coverImage || '/default-course.jpg'" :alt="course.courseName" @error="handleImageError">
          <span class="course-level" :class="'level-' + course.level">{{ getLevelText(course.level) }}</span>
          <span class="course-language">{{ getLanguageText(course.language) }}</span>
        </div>
        <div class="course-info">
          <h3>{{ course.courseName }}</h3>
          <p class="course-desc">{{ course.description }}</p>
          <div class="course-meta">
            <span><el-icon><User /></el-icon> {{ formatNumber(course.studentCount) }} 学员</span>
            <span><el-icon><Reading /></el-icon> {{ course.lessonCount }} 课时</span>
            <span class="rating"><el-icon><Star /></el-icon> {{ course.rating }}</span>
          </div>
        </div>
      </div>

      <el-empty v-if="!loading && courses.length === 0" description="暂无符合条件的课程" />
    </div>

    <!-- 分页 -->
    <div class="pagination" v-if="total > 0">
      <el-pagination
        v-model:current-page="pagination.pageNum"
        v-model:page-size="pagination.pageSize"
        :total="total"
        :page-sizes="[8, 16, 24, 32]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handlePageChange"
        @current-change="handlePageChange"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getCourseList } from '@/api/course'
import { ElMessage } from 'element-plus'

const router = useRouter()

const loading = ref(false)
const courses = ref([])
const total = ref(0)

const filters = reactive({
  language: '',
  level: '',
  category: '',
  sortBy: 'default'
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 8
})

onMounted(() => {
  loadCourses()
})

const handleFilterChange = () => {
  pagination.pageNum = 1
  loadCourses()
}

const handleSortChange = () => {
  pagination.pageNum = 1
  loadCourses()
}

const handlePageChange = () => {
  loadCourses()
}

const resetFilters = () => {
  filters.language = ''
  filters.level = ''
  filters.category = ''
  filters.sortBy = 'default'
  pagination.pageNum = 1
  loadCourses()
}

const loadCourses = async () => {
  loading.value = true
  try {
    const params = {
      language: filters.language || undefined,
      level: filters.level || undefined,
      category: filters.category || undefined,
      sortBy: filters.sortBy || undefined,
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    }
    const res = await getCourseList(params)
    courses.value = res.rows || []
    total.value = res.total || 0
  } catch (error) {
    console.error('加载课程失败:', error)
    ElMessage.error('加载课程失败')
  } finally {
    loading.value = false
  }
}

const goToDetail = (courseId) => {
  router.push(`/course/${courseId}`)
}

const getLevelText = (level) => {
  const levelMap = { beginner: '初级', intermediate: '中级', advanced: '高级' }
  return levelMap[level] || '初级'
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
  e.target.style.display = 'none'
}
</script>

<style lang="scss" scoped>
.course-page {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  overflow-x: hidden;

  h1 {
    font-size: 28px;
    margin-bottom: 20px;
  }

  .filters {
    display: flex;
    gap: 12px;
    margin-bottom: 20px;
    flex-wrap: wrap;
    align-items: center;

    .el-select {
      min-width: 120px;
      max-width: 100%;
      flex: 0 0 auto;
    }

    .el-button {
      flex-shrink: 0;
    }
  }

  .course-list {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
    gap: 20px;

    .course-card {
      background: var(--el-bg-color, white);
      border-radius: 12px;
      overflow: hidden;
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
      cursor: pointer;
      transition: all 0.3s;

      &:hover {
        transform: translateY(-4px);
        box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
      }

      .course-cover {
        position: relative;
        height: 160px;
        overflow: hidden;

        img {
          width: 100%;
          height: 100%;
          object-fit: cover;
          transition: transform 0.3s;
        }

        &:hover img {
          transform: scale(1.05);
        }

        .course-level {
          position: absolute;
          top: 8px;
          right: 8px;
          padding: 2px 10px;
          border-radius: 12px;
          font-size: 12px;
          color: white;

          &.level-beginner { background: #67c23a; }
          &.level-intermediate { background: #e6a23c; }
          &.level-advanced { background: #f56c6c; }
        }

        .course-language {
          position: absolute;
          top: 8px;
          left: 8px;
          padding: 2px 8px;
          border-radius: 4px;
          font-size: 12px;
          color: white;
          background: rgba(0, 0, 0, 0.6);
        }
      }

      .course-info {
        padding: 14px;

        h3 {
          font-size: 16px;
          margin-bottom: 6px;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }

        .course-desc {
          color: #909399;
          font-size: 13px;
          margin-bottom: 10px;
          height: 36px;
          overflow: hidden;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
        }

        .course-meta {
          display: flex;
          gap: 12px;
          color: #909399;
          font-size: 13px;
          flex-wrap: wrap;

          span {
            display: flex;
            align-items: center;
            gap: 4px;
          }

          .rating {
            color: #e6a23c;
            font-weight: bold;
          }
        }
      }
    }
  }

  .pagination {
    display: flex;
    justify-content: center;
    margin-top: 24px;
    overflow-x: auto;
    -webkit-overflow-scrolling: touch;
    padding-bottom: 8px;

    :deep(.el-pagination) {
      flex-wrap: wrap;
      justify-content: center;
      gap: 8px;
    }
  }
}

@media (max-width: 768px) {
  .course-page {
    padding: 12px;

    h1 {
      font-size: 22px;
      margin-bottom: 16px;
    }

    .filters {
      gap: 8px;

      .el-select {
        min-width: 0;
        width: calc(50% - 4px);
        flex: 0 0 calc(50% - 4px);
      }

      .el-button {
        width: 100%;
        flex: 0 0 100%;
      }
    }

    .course-list {
      grid-template-columns: 1fr;
      gap: 12px;

      .course-card {
        display: flex;
        flex-direction: row;

        .course-cover {
          width: 120px;
          height: auto;
          min-height: 100px;
          flex-shrink: 0;

          .course-level {
            padding: 1px 6px;
            font-size: 10px;
          }

          .course-language {
            padding: 1px 5px;
            font-size: 10px;
          }
        }

        .course-info {
          flex: 1;
          min-width: 0;
          padding: 10px;

          h3 {
            font-size: 14px;
          }

          .course-desc {
            font-size: 12px;
            height: 30px;
            -webkit-line-clamp: 2;
          }

          .course-meta {
            gap: 8px;
            font-size: 11px;
          }
        }
      }
    }

    .pagination {
      :deep(.el-pagination) {
        .el-pagination__sizes,
        .el-pagination__jump {
          display: none;
        }
      }
    }
  }
}

@media (max-width: 480px) {
  .course-page {
    padding: 8px;

    h1 {
      font-size: 20px;
    }

    .filters {
      .el-select {
        width: 100%;
        flex: 0 0 100%;
      }
    }

    .course-list {
      .course-card {
        .course-cover {
          width: 100px;
          min-height: 80px;
        }

        .course-info {
          padding: 8px;

          h3 {
            font-size: 13px;
          }

          .course-desc {
            display: none;
          }

          .course-meta {
            gap: 6px;
            font-size: 10px;
          }
        }
      }
    }
  }
}
</style>
