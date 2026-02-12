<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="所属单元" prop="unitId">
        <el-select v-model="queryParams.unitId" placeholder="请选择单元" clearable size="small" @change="handleQuery">
          <el-option
            v-for="unit in unitOptions"
            :key="unit.unitId"
            :label="unit.unitName"
            :value="unit.unitId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="课时类型" prop="lessonType">
        <el-select v-model="queryParams.lessonType" placeholder="请选择类型" clearable size="small">
          <el-option label="词汇" value="vocabulary" />
          <el-option label="语法" value="grammar" />
          <el-option label="听力" value="listening" />
          <el-option label="口语" value="speaking" />
          <el-option label="测试" value="quiz" />
          <el-option label="视频" value="video" />
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
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleBatchImport"
        >批量导入</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="lessonList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="课时ID" align="center" prop="lessonId" width="80" />
      <el-table-column label="所属单元" align="center" prop="unitId" width="120">
        <template slot-scope="scope">
          {{ getUnitName(scope.row.unitId) }}
        </template>
      </el-table-column>
      <el-table-column label="课时名称" align="center" prop="lessonName" :show-overflow-tooltip="true" width="160" />
      <el-table-column label="类型" align="center" prop="lessonType" width="90">
        <template slot-scope="scope">
          <el-tag :type="getLessonTypeTag(scope.row.lessonType)" size="small">
            {{ getLessonTypeLabel(scope.row.lessonType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="排序" align="center" prop="lessonOrder" width="70" />
      <el-table-column label="时长(分)" align="center" prop="duration" width="80" />
      <el-table-column label="是否免费" align="center" prop="isFree" width="80">
        <template slot-scope="scope">
          <el-tag :type="scope.row.isFree ? 'success' : 'warning'" size="small">
            {{ scope.row.isFree ? '免费' : '付费' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="经验奖励" align="center" prop="experienceReward" width="80" />
      <el-table-column label="金币奖励" align="center" prop="coinReward" width="80" />
      <el-table-column label="及格分数" align="center" prop="passingScore" width="80" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="220">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handlePreview(scope.row)"
          >预览</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
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
        <el-form-item label="所属单元" prop="unitId">
          <el-select v-model="form.unitId" placeholder="请选择单元" style="width: 100%">
            <el-option
              v-for="unit in unitOptions"
              :key="unit.unitId"
              :label="unit.unitName + ' (' + getCourseName(unit.courseId) + ')'"
              :value="unit.unitId"
            />
          </el-select>
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="课时名称" prop="lessonName">
              <el-input v-model="form.lessonName" placeholder="请输入课时名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="课时类型" prop="lessonType">
              <el-select v-model="form.lessonType" placeholder="请选择类型" style="width: 100%">
                <el-option label="词汇" value="vocabulary" />
                <el-option label="语法" value="grammar" />
                <el-option label="听力" value="listening" />
                <el-option label="口语" value="speaking" />
                <el-option label="测试" value="quiz" />
                <el-option label="视频" value="video" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="排序" prop="lessonOrder">
              <el-input-number v-model="form.lessonOrder" :min="1" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="时长(分)" prop="duration">
              <el-input-number v-model="form.duration" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="是否免费" prop="isFree">
              <el-switch v-model="form.isFree" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="经验奖励" prop="experienceReward">
              <el-input-number v-model="form.experienceReward" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="金币奖励" prop="coinReward">
              <el-input-number v-model="form.coinReward" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="及格分数" prop="passingScore">
              <el-input-number v-model="form.passingScore" :min="0" :max="100" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="最大尝试次数" prop="maxAttempts">
          <el-input-number v-model="form.maxAttempts" :min="1" :max="10" />
        </el-form-item>
        <el-form-item label="学习内容" prop="content">
          <el-input
            v-model="form.content"
            type="textarea"
            :rows="10"
            placeholder="请输入JSON格式的学习内容，例如：&#10;词汇类型：{&quot;words&quot;:[{&quot;en&quot;:&quot;hello&quot;,&quot;zh&quot;:&quot;你好&quot;}]}&#10;语法类型：{&quot;rules&quot;:[{&quot;title&quot;:&quot;一般现在时&quot;,&quot;content&quot;:&quot;...&quot;}]}"
          />
        </el-form-item>
        <el-form-item label="资源(JSON)" prop="resources">
          <el-input
            v-model="form.resources"
            type="textarea"
            :rows="3"
            placeholder="请输入JSON格式的资源信息，可选"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="内容预览" :visible.sync="previewOpen" width="700px" append-to-body>
      <div v-if="previewContent" class="content-preview">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="课时名称">{{ previewContent.lessonName }}</el-descriptions-item>
          <el-descriptions-item label="课时类型">
            <el-tag :type="getLessonTypeTag(previewContent.lessonType)" size="small">
              {{ getLessonTypeLabel(previewContent.lessonType) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="时长">{{ previewContent.duration }} 分钟</el-descriptions-item>
          <el-descriptions-item label="是否免费">
            {{ previewContent.isFree ? '免费' : '付费' }}
          </el-descriptions-item>
        </el-descriptions>
        <el-divider content-position="left">学习内容</el-divider>
        <pre class="json-preview">{{ formatJson(previewContent.content) }}</pre>
      </div>
    </el-dialog>

    <el-dialog title="批量导入" :visible.sync="importOpen" width="650px" append-to-body>
      <el-alert
        title="TXT文件格式说明"
        type="info"
        :closable="false"
        style="margin-bottom: 15px;"
      >
        <div style="line-height: 1.8; font-size: 13px;">
          <p><b>[COURSE]</b> 课程名称|语言|等级|课程类型|描述</p>
          <p><b>[UNIT]</b> 单元名称|排序|描述</p>
          <p><b>[LESSON]</b> 课时名称|类型|内容(JSON)|排序|时长|是否免费</p>
          <p style="color: #909399; margin-top: 5px;">示例：</p>
          <pre style="background: #f5f7fa; padding: 8px; border-radius: 4px; font-size: 12px;">[COURSE] 英语入门|en|beginner|general|英语入门课程
[UNIT] 基础词汇|1|学习基础英语词汇
[LESSON] 问候语|vocabulary|{"words":[{"en":"hello","zh":"你好"}]}|1|10|true
[LESSON] 数字|vocabulary|{"words":[{"en":"one","zh":"一"}]}|2|10|true
[UNIT] 基础语法|2|学习基础英语语法
[LESSON] 现在时|grammar|{"rules":[{"title":"一般现在时","content":"..."}]}|1|15|true</pre>
        </div>
      </el-alert>
      <el-upload
        ref="upload"
        :action="uploadUrl"
        :headers="uploadHeaders"
        :file-list="fileList"
        :before-upload="beforeUpload"
        :on-success="handleUploadSuccess"
        :on-error="handleUploadError"
        :auto-upload="false"
        accept=".txt"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将TXT文件拖到此处，或<em>点击上传</em></div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button @click="importOpen = false">取 消</el-button>
        <el-button type="primary" @click="submitUpload">开始导入</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listLesson, getLesson, addLesson, updateLesson, delLesson, batchImport } from '@/api/edu/lesson'
import { listUnit } from '@/api/edu/unit'
import { listCourse } from '@/api/edu/course'
import { getToken } from '@/utils/auth'

export default {
  name: 'EduLesson',
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      lessonList: [],
      unitOptions: [],
      courseOptions: [],
      title: '',
      open: false,
      previewOpen: false,
      previewContent: null,
      importOpen: false,
      fileList: [],
      uploadUrl: process.env.VUE_APP_BASE_API + '/edu/batch/import',
      uploadHeaders: {
        Authorization: 'Bearer ' + getToken()
      },
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        unitId: null,
        lessonType: null
      },
      form: {},
      rules: {
        unitId: [
          { required: true, message: '请选择所属单元', trigger: 'change' }
        ],
        lessonName: [
          { required: true, message: '课时名称不能为空', trigger: 'blur' }
        ],
        lessonType: [
          { required: true, message: '请选择课时类型', trigger: 'change' }
        ],
        lessonOrder: [
          { required: true, message: '排序不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.loadUnits()
    this.loadCourses()
    this.getList()
  },
  methods: {
    loadUnits() {
      listUnit({ pageNum: 1, pageSize: 1000 }).then(response => {
        this.unitOptions = response.rows || []
      })
    },
    loadCourses() {
      listCourse({ pageNum: 1, pageSize: 1000 }).then(response => {
        this.courseOptions = response.rows || []
      })
    },
    getUnitName(unitId) {
      const unit = this.unitOptions.find(u => u.unitId === unitId)
      return unit ? unit.unitName : unitId
    },
    getCourseName(courseId) {
      const course = this.courseOptions.find(c => c.courseId === courseId)
      return course ? course.courseName : courseId
    },
    getLessonTypeTag(type) {
      const tags = { 'vocabulary': '', 'grammar': 'success', 'listening': 'warning', 'speaking': 'danger', 'quiz': 'info', 'video': '' }
      return tags[type] || 'info'
    },
    getLessonTypeLabel(type) {
      const labels = { 'vocabulary': '词汇', 'grammar': '语法', 'listening': '听力', 'speaking': '口语', 'quiz': '测试', 'video': '视频' }
      return labels[type] || type
    },
    formatJson(str) {
      try {
        return JSON.stringify(JSON.parse(str), null, 2)
      } catch (e) {
        return str
      }
    },
    getList() {
      this.loading = true
      listLesson(this.queryParams).then(response => {
        this.lessonList = response.rows
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
      this.ids = selection.map(item => item.lessonId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加课时'
    },
    handleUpdate(row) {
      this.reset()
      const lessonId = row.lessonId || this.ids[0]
      getLesson(lessonId).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改课时'
      })
    },
    handlePreview(row) {
      this.previewContent = row
      this.previewOpen = true
    },
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.lessonId != null) {
            updateLesson(this.form).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addLesson(this.form).then(response => {
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
        lessonId: null,
        unitId: null,
        lessonName: null,
        lessonType: 'vocabulary',
        lessonOrder: 1,
        content: null,
        duration: 10,
        isFree: true,
        isPreview: false,
        experienceReward: 10,
        coinReward: 5,
        passingScore: 60,
        maxAttempts: 3,
        resources: null
      }
      this.resetForm('form')
    },
    handleDelete(row) {
      const lessonIds = row.lessonId || this.ids
      this.$modal.confirm('是否确认删除课时编号为"' + lessonIds + '"的数据项？').then(function() {
        return delLesson(lessonIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    handleBatchImport() {
      this.fileList = []
      this.importOpen = true
    },
    beforeUpload(file) {
      const isTxt = file.name.toLowerCase().endsWith('.txt')
      if (!isTxt) {
        this.$modal.msgError('只能上传TXT文件')
        return false
      }
      return true
    },
    submitUpload() {
      if (this.fileList.length === 0) {
        this.$modal.msgWarning('请先选择要上传的TXT文件')
        return
      }
      const formData = new FormData()
      formData.append('file', this.fileList[0].raw)
      batchImport(formData).then(response => {
        const data = response.data
        let msg = '导入完成！课程：' + data.courseCount + '个，单元：' + data.unitCount + '个，课时：' + data.lessonCount + '个'
        if (data.errors && data.errors.length > 0) {
          msg += '\n警告信息：\n' + data.errors.join('\n')
        }
        this.$modal.alert(msg)
        this.importOpen = false
        this.loadUnits()
        this.getList()
      }).catch(() => {
        this.$modal.msgError('导入失败')
      })
    },
    handleUploadSuccess(response, file, fileList) {
      this.fileList = fileList
    },
    handleUploadError(err) {
      this.$modal.msgError('上传失败')
    }
  }
}
</script>

<style scoped>
.mb8 {
  margin-bottom: 8px;
}
.content-preview {
  max-height: 500px;
  overflow-y: auto;
}
.json-preview {
  background: #f5f7fa;
  padding: 15px;
  border-radius: 4px;
  font-size: 13px;
  line-height: 1.6;
  white-space: pre-wrap;
  word-break: break-all;
  max-height: 300px;
  overflow-y: auto;
}
</style>