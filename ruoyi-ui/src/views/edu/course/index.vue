<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="课程名称" prop="courseName">
        <el-input
          v-model="queryParams.courseName"
          placeholder="请输入课程名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="语言" prop="language">
        <el-select v-model="queryParams.language" placeholder="请选择语言" clearable size="small">
          <el-option label="英语" value="en" />
          <el-option label="日语" value="ja" />
          <el-option label="汉语" value="zh" />
        </el-select>
      </el-form-item>
      <el-form-item label="等级" prop="level">
        <el-select v-model="queryParams.level" placeholder="请选择等级" clearable size="small">
          <el-option label="初级" value="beginner" />
          <el-option label="中级" value="intermediate" />
          <el-option label="高级" value="advanced" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['edu:course:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['edu:course:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['edu:course:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="courseList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="课程ID" align="center" prop="courseId" width="80" />
      <el-table-column label="课程名称" align="center" prop="courseName" :show-overflow-tooltip="true" width="180" />
      <el-table-column label="语言" align="center" prop="language" width="80">
        <template slot-scope="scope">
          <el-tag :type="getLanguageType(scope.row.language)" size="small">
            {{ getLanguageLabel(scope.row.language) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="等级" align="center" prop="level" width="80">
        <template slot-scope="scope">
          <el-tag :type="getLevelType(scope.row.level)" size="small">
            {{ getLevelLabel(scope.row.level) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="课程类型" align="center" prop="courseType" width="100">
        <template slot-scope="scope">
          {{ getCourseTypeLabel(scope.row.courseType) }}
        </template>
      </el-table-column>
      <el-table-column label="封面" align="center" prop="coverImage" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.coverImage" v-if="scope.row.coverImage" />
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="学习人数" align="center" prop="totalStudents" width="80" />
      <el-table-column label="总课时" align="center" prop="totalLessons" width="80" />
      <el-table-column label="是否免费" align="center" prop="isFree" width="80">
        <template slot-scope="scope">
          <el-tag :type="scope.row.isFree ? 'success' : 'warning'" size="small">
            {{ scope.row.isFree ? '免费' : '付费' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="是否发布" align="center" prop="isPublished" width="80">
        <template slot-scope="scope">
          <el-tag :type="scope.row.isPublished ? 'success' : 'info'" size="small">
            {{ scope.row.isPublished ? '已发布' : '未发布' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['edu:course:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['edu:course:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="课程名称" prop="courseName">
              <el-input v-model="form.courseName" placeholder="请输入课程名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="课程编码" prop="courseCode">
              <el-input v-model="form.courseCode" placeholder="请输入课程编码" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="语言" prop="language">
              <el-select v-model="form.language" placeholder="请选择语言">
                <el-option label="英语" value="en" />
                <el-option label="日语" value="ja" />
                <el-option label="汉语" value="zh" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="等级" prop="level">
              <el-select v-model="form.level" placeholder="请选择等级">
                <el-option label="初级" value="beginner" />
                <el-option label="基础" value="elementary" />
                <el-option label="中级" value="intermediate" />
                <el-option label="高级" value="advanced" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="课程类型" prop="courseType">
              <el-select v-model="form.courseType" placeholder="请选择课程类型">
                <el-option label="综合" value="general" />
                <el-option label="会话" value="conversation" />
                <el-option label="语法" value="grammar" />
                <el-option label="词汇" value="vocabulary" />
                <el-option label="听力" value="listening" />
                <el-option label="口语" value="speaking" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="总时长(分)" prop="totalDuration">
              <el-input-number v-model="form.totalDuration" :min="0" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="总课时数" prop="totalLessons">
              <el-input-number v-model="form.totalLessons" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="难度系数" prop="difficultyScore">
              <el-input-number v-model="form.difficultyScore" :min="1" :max="5" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="课程描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入课程描述" />
        </el-form-item>
        <el-form-item label="学习目标" prop="learningObjectives">
          <el-input v-model="form.learningObjectives" type="textarea" :rows="3" placeholder="请输入学习目标" />
        </el-form-item>
        <el-form-item label="课程封面" prop="coverImage">
          <image-upload v-model="form.coverImage" :limit="1" />
        </el-form-item>
        <el-row>
          <el-col :span="8">
            <el-form-item label="是否免费" prop="isFree">
              <el-switch v-model="form.isFree" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="价格" prop="price" v-if="!form.isFree">
              <el-input-number v-model="form.price" :min="0" :precision="2" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="是否发布" prop="isPublished">
              <el-switch v-model="form.isPublished" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="是否精选" prop="isFeatured">
              <el-switch v-model="form.isFeatured" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="标签" prop="tags">
              <el-input v-model="form.tags" placeholder="多个标签用逗号分隔" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listCourse, getCourse, addCourse, updateCourse, delCourse } from '@/api/edu/course'

export default {
  name: 'EduCourse',
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      courseList: [],
      title: '',
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        courseName: null,
        language: null,
        level: null
      },
      form: {},
      rules: {
        courseName: [
          { required: true, message: '课程名称不能为空', trigger: 'blur' }
        ],
        language: [
          { required: true, message: '请选择语言', trigger: 'change' }
        ],
        level: [
          { required: true, message: '请选择等级', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listCourse(this.queryParams).then(response => {
        this.courseList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.courseId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加课程'
    },
    handleUpdate(row) {
      this.reset()
      const courseId = row.courseId || this.ids[0]
      getCourse(courseId).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改课程'
      })
    },
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.courseId != null) {
            updateCourse(this.form).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addCourse(this.form).then(response => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
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
      this.form = {
        courseId: null,
        courseName: null,
        courseCode: null,
        language: null,
        level: null,
        courseType: 'general',
        description: null,
        coverImage: null,
        totalDuration: 0,
        totalLessons: 0,
        isFree: true,
        price: 0,
        isPublished: false,
        isFeatured: false,
        learningObjectives: null,
        tags: null,
        difficultyScore: 1
      }
      this.resetForm('form')
    },
    handleDelete(row) {
      const courseIds = row.courseId || this.ids
      this.$modal.confirm('是否确认删除课程编号为"' + courseIds + '"的数据项？').then(function() {
        return delCourse(courseIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    getLanguageType(language) {
      const types = { 'en': 'primary', 'ja': 'success', 'zh': 'warning' }
      return types[language] || 'info'
    },
    getLanguageLabel(language) {
      const labels = { 'en': '英语', 'ja': '日语', 'zh': '汉语' }
      return labels[language] || language
    },
    getLevelType(level) {
      const types = { 'beginner': 'success', 'elementary': '', 'intermediate': 'warning', 'advanced': 'danger' }
      return types[level] || 'info'
    },
    getLevelLabel(level) {
      const labels = { 'beginner': '初级', 'elementary': '基础', 'intermediate': '中级', 'advanced': '高级' }
      return labels[level] || level
    },
    getCourseTypeLabel(type) {
      const labels = { 'general': '综合', 'conversation': '会话', 'grammar': '语法', 'vocabulary': '词汇', 'listening': '听力', 'speaking': '口语' }
      return labels[type] || type
    }
  }
}
</script>

<style scoped>
.mb8 {
  margin-bottom: 8px;
}
</style>
