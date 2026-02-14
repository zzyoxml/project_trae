<template>
  <div class="app-container">
    <el-card shadow="never" class="filter-card">
      <el-row :gutter="16" align="middle">
        <el-col :span="5">
          <span class="filter-label">选择课程：</span>
          <el-select
            v-model="selectedCourseId"
            placeholder="请先选择课程"
            clearable
            size="medium"
            style="width: 200px"
            @change="onCourseChange"
          >
            <el-option
              v-for="c in courseOptions"
              :key="c.courseId"
              :label="c.courseName + ' (' + getLanguageText(c.language) + ')'"
              :value="c.courseId"
            />
          </el-select>
        </el-col>
        <el-col :span="5">
          <span class="filter-label">所属单元：</span>
          <el-select
            v-model="queryParams.unitId"
            placeholder="请选择单元"
            clearable
            size="medium"
            style="width: 200px"
            :disabled="!selectedCourseId"
            @change="handleQuery"
          >
            <el-option
              v-for="u in filteredUnitOptions"
              :key="u.unitId"
              :label="u.unitName"
              :value="u.unitId"
            />
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" icon="el-icon-search" size="medium" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="medium" @click="resetQuery">重置</el-button>
        </el-col>
      </el-row>
    </el-card>

    <el-row :gutter="10" class="mb8 mt8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" :disabled="!selectedCourseId">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-upload2" size="mini" @click="handleBatchImport">批量导入</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="lessonList" @selection-change="handleSelectionChange" border stripe>
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column label="ID" align="center" prop="lessonId" width="70" />
      <el-table-column label="所属课程" align="center" width="140">
        <template slot-scope="scope">
          <el-tag size="small" :type="getCourseTagType(scope.row.unitId)">
            {{ getCourseNameByUnit(scope.row.unitId) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="所属单元" align="center" width="140">
        <template slot-scope="scope">
          {{ getUnitName(scope.row.unitId) }}
        </template>
      </el-table-column>
      <el-table-column label="课时名称" align="center" prop="lessonName" :show-overflow-tooltip="true" min-width="160" />
      <el-table-column label="排序" align="center" prop="lessonOrder" width="70" />
      <el-table-column label="时长(分)" align="center" prop="duration" width="80" />
      <el-table-column label="免费" align="center" prop="isFree" width="70">
        <template slot-scope="scope">
          <el-tag :type="scope.row.isFree === '1' ? 'success' : 'warning'" size="small">
            {{ scope.row.isFree === '1' ? '免费' : '付费' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="经验" align="center" prop="experienceReward" width="70" />
      <el-table-column label="金币" align="center" prop="coinReward" width="70" />
      <el-table-column label="及格分" align="center" prop="passingScore" width="70" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="150">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="220">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handlePreview(scope.row)">预览</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" style="color:#f56c6c" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="所属课程" prop="courseId">
          <el-select v-model="formCourseId" placeholder="请选择课程" style="width: 100%" @change="onFormCourseChange">
            <el-option v-for="c in courseOptions" :key="c.courseId" :label="c.courseName + ' (' + getLanguageText(c.language) + ')'" :value="c.courseId" />
          </el-select>
        </el-form-item>
        <el-form-item label="所属单元" prop="unitId">
          <el-select v-model="form.unitId" placeholder="请选择单元" style="width: 100%" :disabled="!formCourseId">
            <el-option v-for="u in formUnitOptions" :key="u.unitId" :label="u.unitName" :value="u.unitId" />
          </el-select>
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="课时名称" prop="lessonName">
              <el-input v-model="form.lessonName" placeholder="请输入课时名称" />
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
              <el-switch v-model="form.isFree" active-value="1" inactive-value="0" />
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
            placeholder="请输入JSON格式的学习内容"
          />
        </el-form-item>
        <el-form-item label="资源(JSON)" prop="resources">
          <el-input v-model="form.resources" type="textarea" :rows="3" placeholder="请输入JSON格式的资源信息，可选" />
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
          <el-descriptions-item label="时长">{{ previewContent.duration }} 分钟</el-descriptions-item>
          <el-descriptions-item label="是否免费">
            {{ previewContent.isFree === '1' ? '免费' : '付费' }}
          </el-descriptions-item>
        </el-descriptions>
        <el-divider content-position="left">学习内容</el-divider>
        <pre class="json-preview">{{ formatJson(previewContent.content) }}</pre>
      </div>
    </el-dialog>

    <el-dialog title="批量导入" :visible.sync="importOpen" width="650px" append-to-body>
      <el-alert title="TXT文件格式说明" type="info" :closable="false" style="margin-bottom: 15px;">
        <div style="line-height: 1.8; font-size: 13px;">
          <p><b>[COURSE]</b> 课程名称|语言|等级|课程类型|描述</p>
          <p><b>[UNIT]</b> 单元名称|排序|描述</p>
          <p><b>[LESSON]</b> 课时名称|类型|内容(JSON)|排序|时长|是否免费</p>
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
      loading: false,
      ids: [],
      single: true,
      multiple: true,
      total: 0,
      lessonList: [],
      unitOptions: [],
      courseOptions: [],
      selectedCourseId: null,
      formCourseId: null,
      title: '',
      open: false,
      previewOpen: false,
      previewContent: null,
      importOpen: false,
      fileList: [],
      uploadUrl: process.env.VUE_APP_BASE_API + '/edu/batch/import',
      uploadHeaders: { Authorization: 'Bearer ' + getToken() },
      queryParams: { pageNum: 1, pageSize: 10, unitId: null },
      form: {},
      rules: {
        unitId: [{ required: true, message: '请选择所属单元', trigger: 'change' }],
        lessonName: [{ required: true, message: '课时名称不能为空', trigger: 'blur' }],
        lessonOrder: [{ required: true, message: '排序不能为空', trigger: 'blur' }]
      }
    }
  },
  computed: {
    filteredUnitOptions() {
      if (!this.selectedCourseId) return []
      return this.unitOptions.filter(u => u.courseId === this.selectedCourseId)
    },
    formUnitOptions() {
      if (!this.formCourseId) return []
      return this.unitOptions.filter(u => u.courseId === this.formCourseId)
    }
  },
  created() {
    this.loadCourses()
    this.loadAllUnits()
  },
  methods: {
    loadCourses() {
      listCourse({ pageNum: 1, pageSize: 1000 }).then(res => {
        this.courseOptions = res.rows || []
      })
    },
    loadAllUnits() {
      listUnit({ pageNum: 1, pageSize: 1000 }).then(res => {
        this.unitOptions = res.rows || []
      })
    },
    getLanguageText(lang) {
      const map = { en: '英语', ja: '日语', zh: '汉语' }
      return map[lang] || lang
    },
    getUnitName(unitId) {
      const u = this.unitOptions.find(item => item.unitId === unitId)
      return u ? u.unitName : (unitId || '-')
    },
    getCourseNameByUnit(unitId) {
      const u = this.unitOptions.find(item => item.unitId === unitId)
      if (!u) return '-'
      const c = this.courseOptions.find(item => item.courseId === u.courseId)
      return c ? c.courseName : '-'
    },
    getCourseTagType(unitId) {
      const u = this.unitOptions.find(item => item.unitId === unitId)
      if (!u) return 'info'
      const c = this.courseOptions.find(item => item.courseId === u.courseId)
      if (!c) return 'info'
      const map = { en: '', ja: 'success', zh: 'warning' }
      return map[c.language] || 'info'
    },
    formatJson(str) {
      try { return JSON.stringify(JSON.parse(str), null, 2) } catch (e) { return str }
    },
    onCourseChange(courseId) {
      this.queryParams.pageNum = 1
      this.queryParams.unitId = null
      if (courseId) {
        this.getList()
      } else {
        this.lessonList = []
        this.total = 0
      }
    },
    onFormCourseChange(courseId) {
      this.form.unitId = null
    },
    getList() {
      if (!this.selectedCourseId) {
        this.lessonList = []
        this.total = 0
        return
      }
      this.loading = true
      const params = { ...this.queryParams }
      if (!params.unitId) {
        const courseUnitIds = this.unitOptions
          .filter(u => u.courseId === this.selectedCourseId)
          .map(u => u.unitId)
        if (courseUnitIds.length === 0) {
          this.lessonList = []
          this.total = 0
          this.loading = false
          return
        }
        params.unitIds = courseUnitIds.join(',')
      }
      listLesson(params).then(res => {
        this.lessonList = res.rows || []
        this.total = res.total || 0
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.queryParams.unitId = null
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.lessonId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      if (!this.selectedCourseId) {
        this.$modal.msgWarning('请先选择课程')
        return
      }
      this.reset()
      this.formCourseId = this.selectedCourseId
      this.open = true
      this.title = '添加课时'
    },
    handleUpdate(row) {
      this.reset()
      const lessonId = row.lessonId || this.ids[0]
      getLesson(lessonId).then(res => {
        this.form = res.data
        const u = this.unitOptions.find(item => item.unitId === this.form.unitId)
        if (u) {
          this.formCourseId = u.courseId
        }
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
            updateLesson(this.form).then(() => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addLesson(this.form).then(() => {
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
        lessonOrder: 1,
        content: null,
        duration: 10,
        isFree: '1',
        isPreview: '0',
        experienceReward: 10,
        coinReward: 5,
        passingScore: 60,
        maxAttempts: 3,
        resources: null
      }
      this.formCourseId = null
      this.resetForm('form')
    },
    handleDelete(row) {
      const lessonIds = row.lessonId || this.ids
      const name = row.lessonName || lessonIds
      this.$modal.confirm('确认删除课时"' + name + '"？').then(() => {
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
      batchImport(formData).then(res => {
        const data = res.data
        let msg = '导入完成！课程：' + data.courseCount + '个，单元：' + data.unitCount + '个，课时：' + data.lessonCount + '个'
        if (data.errors && data.errors.length > 0) {
          msg += '\n警告信息：\n' + data.errors.join('\n')
        }
        this.$modal.alert(msg)
        this.importOpen = false
        this.loadAllUnits()
        this.getList()
      }).catch(() => {
        this.$modal.msgError('导入失败')
      })
    },
    handleUploadSuccess(response, file, fileList) {
      this.fileList = fileList
    },
    handleUploadError() {
      this.$modal.msgError('上传失败')
    }
  }
}
</script>

<style scoped>
.filter-card {
  margin-bottom: 12px;
}
.filter-label {
  font-size: 14px;
  font-weight: 600;
  color: #303133;
  margin-right: 4px;
}
.mt8 {
  margin-top: 8px;
}
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