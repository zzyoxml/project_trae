<template>
  <div class="app-container">
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-refresh" size="mini" @click="loadData">刷新数据</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="loadData"></right-toolbar>
    </el-row>

    <div v-loading="loading" class="course-chapter-tree">
      <el-collapse v-model="expandedCourses" @change="handleCollapseChange">
        <el-collapse-item v-for="course in courseList" :key="course.courseId" :name="course.courseId">
          <template slot="title">
            <div class="course-title-bar">
              <div class="course-title-left">
                <span class="course-icon">📘</span>
                <span class="course-name">{{ course.courseName }}</span>
                <el-tag size="mini" :type="getLanguageTagType(course.language)">{{ getLanguageText(course.language) }}</el-tag>
                <el-tag size="mini" :type="getLevelTagType(course.level)">{{ getLevelText(course.level) }}</el-tag>
              </div>
              <div class="course-title-right">
                <el-tag size="mini" type="info">{{ getChapterCount(course.courseId) }} 个章节</el-tag>
                <el-button type="primary" size="mini" icon="el-icon-plus" plain @click.stop="handleAdd(course)">添加章节</el-button>
              </div>
            </div>
          </template>

          <div class="chapter-list">
            <el-empty v-if="getChaptersByCourse(course.courseId).length === 0" description="暂无章节，点击上方按钮添加" :image-size="60"></el-empty>
            <div
              v-for="chapter in getChaptersByCourse(course.courseId)"
              :key="chapter.chapterId"
              class="chapter-card"
            >
              <div class="chapter-card-header">
                <div class="chapter-info-main">
                  <span class="chapter-order-badge">第{{ chapter.chapterOrder }}章</span>
                  <span class="chapter-name-text">{{ chapter.chapterName }}</span>
                </div>
                <div class="chapter-stats">
                  <span class="stat-item"><i class="el-icon-document"></i> {{ chapter.totalUnits || 0 }} 单元</span>
                  <span class="stat-item"><i class="el-icon-notebook-2"></i> {{ chapter.totalLessons || 0 }} 课时</span>
                </div>
              </div>
              <div v-if="chapter.description" class="chapter-desc">{{ chapter.description }}</div>
              <div class="chapter-actions">
                <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(chapter, course)">编辑</el-button>
                <el-button size="mini" type="text" icon="el-icon-delete" style="color:#f56c6c" @click="handleDelete(chapter)">删除</el-button>
              </div>
            </div>
          </div>
        </el-collapse-item>
      </el-collapse>

      <el-empty v-if="!loading && courseList.length === 0" description="暂无课程数据"></el-empty>
    </div>

    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="所属课程" prop="courseId">
          <el-select v-model="form.courseId" placeholder="请选择课程" style="width: 100%" disabled>
            <el-option v-for="c in courseList" :key="c.courseId" :label="c.courseName" :value="c.courseId" />
          </el-select>
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="章节名称" prop="chapterName">
              <el-input v-model="form.chapterName" placeholder="请输入章节名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="排序" prop="chapterOrder">
              <el-input-number v-model="form.chapterOrder" :min="1" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入章节描述" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listChapter, getChapter, addChapter, updateChapter, delChapter } from '@/api/edu/chapter'
import { listCourse } from '@/api/edu/course'

export default {
  name: 'EduChapter',
  data() {
    return {
      loading: true,
      showSearch: true,
      courseList: [],
      allChapters: [],
      expandedCourses: [],
      title: '',
      open: false,
      currentCourse: null,
      form: {},
      rules: {
        courseId: [{ required: true, message: '请选择所属课程', trigger: 'change' }],
        chapterName: [{ required: true, message: '章节名称不能为空', trigger: 'blur' }],
        chapterOrder: [{ required: true, message: '排序不能为空', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    loadData() {
      this.loading = true
      Promise.all([
        listCourse({ pageNum: 1, pageSize: 1000 }),
        listChapter({ pageNum: 1, pageSize: 1000 })
      ]).then(([courseRes, chapterRes]) => {
        this.courseList = courseRes.rows || []
        this.allChapters = chapterRes.rows || []
        if (this.courseList.length > 0 && this.expandedCourses.length === 0) {
          this.expandedCourses = [this.courseList[0].courseId]
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    handleCollapseChange(activeKeys) {
      this.expandedCourses = activeKeys
    },
    getChaptersByCourse(courseId) {
      return this.allChapters.filter(ch => ch.courseId === courseId).sort((a, b) => a.chapterOrder - b.chapterOrder)
    },
    getChapterCount(courseId) {
      return this.getChaptersByCourse(courseId).length
    },
    getLanguageTagType(lang) {
      const map = { en: '', ja: 'success', zh: 'warning' }
      return map[lang] || 'info'
    },
    getLanguageText(lang) {
      const map = { en: '英语', ja: '日语', zh: '汉语' }
      return map[lang] || lang
    },
    getLevelTagType(level) {
      const map = { beginner: '', elementary: 'success', intermediate: 'warning', advanced: 'danger' }
      return map[level] || 'info'
    },
    getLevelText(level) {
      const map = { beginner: '入门', elementary: '初级', intermediate: '中级', advanced: '高级' }
      return map[level] || level
    },
    handleAdd(course) {
      this.reset()
      this.currentCourse = course
      this.form.courseId = course.courseId
      this.form.chapterOrder = this.getChaptersByCourse(course.courseId).length + 1
      this.open = true
      this.title = '添加章节 - ' + course.courseName
    },
    handleUpdate(chapter, course) {
      this.reset()
      this.currentCourse = course
      getChapter(chapter.chapterId).then(res => {
        this.form = res.data
        this.open = true
        this.title = '修改章节 - ' + (course ? course.courseName : '')
      })
    },
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.chapterId != null) {
            updateChapter(this.form).then(() => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.loadData()
            })
          } else {
            addChapter(this.form).then(() => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.loadData()
            })
          }
        }
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = { chapterId: null, courseId: null, chapterName: null, chapterOrder: 1, description: null }
      this.currentCourse = null
      this.resetForm('form')
    },
    handleDelete(row) {
      this.$modal.confirm('确认删除章节"' + row.chapterName + '"？删除后该章节下的单元和课时也将受影响。').then(() => {
        return delChapter(row.chapterId)
      }).then(() => {
        this.loadData()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    }
  }
}
</script>

<style lang="scss" scoped>
.course-chapter-tree {
  padding: 0 4px;
}

.course-title-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  padding-right: 20px;
}

.course-title-left {
  display: flex;
  align-items: center;
  gap: 8px;

  .course-icon {
    font-size: 20px;
  }

  .course-name {
    font-size: 15px;
    font-weight: 600;
    color: #303133;
  }
}

.course-title-right {
  display: flex;
  align-items: center;
  gap: 8px;
}

.chapter-list {
  padding: 8px 16px 16px 24px;
}

.chapter-card {
  background: #fafbfc;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  padding: 14px 18px;
  margin-bottom: 10px;
  transition: all 0.25s;

  &:hover {
    border-color: #c0c4cc;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  }

  .chapter-card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 6px;
  }

  .chapter-info-main {
    display: flex;
    align-items: center;
    gap: 10px;

    .chapter-order-badge {
      display: inline-block;
      padding: 2px 10px;
      font-size: 12px;
      color: #409eff;
      background: #ecf5ff;
      border-radius: 10px;
      font-weight: 500;
    }

    .chapter-name-text {
      font-size: 14px;
      font-weight: 600;
      color: #303133;
    }
  }

  .chapter-stats {
    display: flex;
    gap: 16px;

    .stat-item {
      font-size: 12px;
      color: #909399;

      i {
        margin-right: 3px;
      }
    }
  }

  .chapter-desc {
    font-size: 13px;
    color: #909399;
    line-height: 1.5;
    margin-bottom: 8px;
    padding-left: 4px;
  }

  .chapter-actions {
    text-align: right;
    padding-top: 4px;
    border-top: 1px solid #f0f2f5;
  }
}

::v-deep .el-collapse-item__header {
  height: auto !important;
  min-height: 50px;
  padding: 10px 0;
  line-height: inherit !important;
  background: #fff;
  border-radius: 8px;
  margin-bottom: 4px;

  &:hover {
    background-color: #f5f7fa;
  }
}

::v-deep .el-collapse-item__wrap {
  border-bottom: none;
  border-radius: 0 0 8px 8px;
  background: #fdfdfd;
}

::v-deep .el-collapse-item__content {
  padding-bottom: 8px;
}
</style>
