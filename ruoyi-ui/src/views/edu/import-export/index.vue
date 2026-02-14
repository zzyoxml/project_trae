<template>
  <div class="import-export-container">
    <div class="page-header">
      <h2><i class="el-icon-upload2"></i> 导入导出管理</h2>
      <p class="subtitle">智能导入词汇到课程，支持自动创建课程和单元结构</p>
    </div>

    <el-row :gutter="20">
      <el-col :span="16">
        <el-card class="import-card" shadow="hover">
          <div slot="header" class="card-header">
            <span><i class="el-icon-magic-stick"></i> 智能导入词汇</span>
            <el-tag type="success" size="small" effect="dark">自动创建结构</el-tag>
          </div>

          <el-alert
            title="使用说明"
            type="info"
            :closable="false"
            class="tips-alert"
          >
            <div class="tips">
              <p><i class="el-icon-check"></i> <strong>最简单</strong>：直接上传文件，系统自动创建课程和单元</p>
              <p><i class="el-icon-check"></i> <strong>指定名称</strong>：填写课程名和单元名，复用或创建对应结构</p>
              <p><i class="el-icon-check"></i> <strong>导入已有</strong>：选择已有课程和单元，导入到指定位置</p>
              <p><i class="el-icon-check"></i> <strong>支持格式</strong>：TXT（多种分隔符）、JSON</p>
            </div>
          </el-alert>

          <el-form :model="importForm" label-width="90px" class="import-form">
            <el-row :gutter="16">
              <el-col :span="12">
                <el-form-item label="课程名称">
                  <el-input v-model="importForm.courseName" placeholder="为空则从文件名提取" clearable size="medium">
                    <template slot="prepend"><i class="el-icon-reading"></i></template>
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="单元名称">
                  <el-input v-model="importForm.unitName" placeholder="为空则从文件名提取" clearable size="medium">
                    <template slot="prepend"><i class="el-icon-folder-opened"></i></template>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-divider content-position="left"><i class="el-icon-document-copy"></i> 或选择已有</el-divider>

            <el-row :gutter="16">
              <el-col :span="12">
                <el-form-item label="选择课程">
                  <el-select v-model="importForm.courseId" placeholder="选择已有课程" clearable size="medium" style="width: 100%" @change="loadUnitsForImport">
                    <el-option v-for="course in courses" :key="course.courseId" :label="course.courseName + ' (' + course.language + ')'" :value="course.courseId" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="选择单元">
                  <el-select v-model="importForm.unitId" placeholder="选择已有单元" clearable size="medium" style="width: 100%">
                    <el-option v-for="unit in importUnits" :key="unit.unitId" :label="unit.unitName" :value="unit.unitId" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>

          <el-upload
            ref="uploadRef"
            class="upload-demo"
            drag
            :action="smartImportUrl"
            :headers="uploadHeaders"
            :data="importForm"
            :on-success="handleSmartImportSuccess"
            :on-error="handleImportError"
            :before-upload="beforeSmartUpload"
            :auto-upload="false"
            accept=".txt,.json"
          >
            <div class="upload-content">
              <i class="el-icon-upload"></i>
              <div class="el-upload__text">
                拖拽文件到此处，或 <em>点击上传</em>
              </div>
              <div class="el-upload__tip">
                支持 TXT 和 JSON 文件
              </div>
            </div>
          </el-upload>

          <div class="upload-actions">
            <el-button type="primary" size="medium" icon="el-icon-check" @click="submitUpload" class="action-btn">
              开始导入
            </el-button>
            <el-button size="medium" icon="el-icon-refresh" @click="resetForm" class="action-btn">
              重置
            </el-button>
          </div>
        </el-card>

        <el-card class="result-card" v-if="importResult.show" shadow="hover">
          <div slot="header">
            <span><i class="el-icon-document-checked"></i> 导入结果</span>
          </div>
          <div class="result-stats">
            <el-row :gutter="16">
              <el-col :span="8">
                <div class="stat-item success">
                  <div class="stat-value">{{ importResult.successCount }}</div>
                  <div class="stat-label">成功</div>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="stat-item danger">
                  <div class="stat-value">{{ importResult.failCount }}</div>
                  <div class="stat-label">失败</div>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="stat-item info">
                  <div class="stat-value">{{ importResult.successCount + importResult.failCount }}</div>
                  <div class="stat-label">总计</div>
                </div>
              </el-col>
            </el-row>
          </div>

          <div v-if="importResult.courseCreated" class="info-item success">
            <i class="el-icon-circle-check"></i>
            已创建课程：<strong>{{ importResult.courseName }}</strong>
          </div>
          <div v-if="importResult.unitCreated" class="info-item success">
            <i class="el-icon-circle-check"></i>
            已创建单元：<strong>{{ importResult.unitName }}</strong>
          </div>

          <el-table :data="importResult.errors" v-if="importResult.errors && importResult.errors.length > 0" size="small" class="error-table">
            <el-table-column type="index" label="#" width="60" />
            <el-table-column prop="error" label="错误信息" />
          </el-table>

          <div v-else-if="importResult.successCount > 0" class="success-message">
            <i class="el-icon-success"></i>
            <p>导入成功！共导入 <strong>{{ importResult.successCount }}</strong> 个词汇</p>
          </div>
        </el-card>

        <el-card class="format-card" shadow="hover">
          <div slot="header" class="card-header">
            <span><i class="el-icon-info"></i> 支持的文件格式</span>
          </div>

          <el-collapse v-model="activeCollapse" accordion>
            <el-collapse-item title="TXT 格式" name="txt">
              <div class="format-examples">
                <p><strong>格式1：制表符分隔（推荐）</strong></p>
                <pre class="code-block">apple	苹果
banana	香蕉</pre>

                <p><strong>格式2：多空格分隔</strong></p>
                <pre class="code-block">apple  苹果
banana  香蕉</pre>

                <p><strong>格式3：词性标注</strong></p>
                <pre class="code-block">boat n. 小船  v. 划船</pre>
              </div>
            </el-collapse-item>

            <el-collapse-item title="JSON 格式" name="json">
              <div class="format-examples">
                <pre class="code-block">[
  {
    "word": "boat",
    "definitions": ["n. 小船", "v. 划船"]
  }
]</pre>
              </div>
            </el-collapse-item>
          </el-collapse>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card class="export-card" shadow="hover">
          <div slot="header" class="card-header">
            <span><i class="el-icon-download"></i> 导出词汇</span>
          </div>

          <el-form :inline="true" class="search-form">
            <el-form-item label="课程">
              <el-select v-model="selectedCourseId" placeholder="请选择课程" @change="loadUnits" size="medium" style="width: 180px">
                <el-option v-for="course in courses" :key="course.courseId" :label="course.courseName" :value="course.courseId" />
              </el-select>
            </el-form-item>
            <el-form-item label="单元">
              <el-select v-model="selectedUnitId" placeholder="请选择单元" size="medium" style="width: 180px">
                <el-option v-for="unit in units" :key="unit.unitId" :label="unit.unitName" :value="unit.unitId" />
              </el-select>
            </el-form-item>
          </el-form>

          <div class="export-buttons">
            <el-button type="primary" icon="el-icon-document" @click="exportUnitTxt" :disabled="!selectedUnitId" plain>
              导出TXT
            </el-button>
            <el-button type="success" icon="el-icon-document-copy" @click="exportUnitJson" :disabled="!selectedUnitId" plain>
              导出JSON
            </el-button>
            <el-button type="warning" icon="el-icon-folder-opened" @click="exportCourseJson" :disabled="!selectedCourseId" plain>
              导出完整课程
            </el-button>
          </div>
        </el-card>

        <el-card class="import-full-card" shadow="hover">
          <div slot="header" class="card-header">
            <span><i class="el-icon-upload2"></i> 导入完整课程JSON</span>
            <el-tag type="warning" size="small" effect="dark">恢复备份</el-tag>
          </div>

          <el-alert
            title="导入说明"
            type="info"
            :closable="false"
            class="tips-alert"
          >
            <div class="tips">
              <p><i class="el-icon-check"></i> 选择之前导出的<strong>完整课程JSON</strong>文件进行导入</p>
              <p><i class="el-icon-check"></i> 可选择导入到<strong>已有课程</strong>或<strong>创建新课程</strong></p>
              <p><i class="el-icon-warning"></i> 当前仅导入课程和单元结构，课时内容需单独导入</p>
            </div>
          </el-alert>

          <el-form :inline="true" class="import-full-form">
            <el-form-item label="目标课程">
              <el-select v-model="importFullCourseId" placeholder="选择已有课程（可选）" clearable size="medium" style="width: 200px">
                <el-option v-for="course in courses" :key="course.courseId" :label="course.courseName" :value="course.courseId" />
              </el-select>
            </el-form-item>
          </el-form>

          <el-upload
            ref="importFullUploadRef"
            class="upload-demo import-full-upload"
            drag
            :action="importFullCourseUrl"
            :headers="{ Authorization: 'Bearer ' + getToken() }"
            :data="getImportFullData()"
            :on-success="handleImportFullSuccess"
            :on-error="handleImportFullError"
            :auto-upload="true"
            accept=".json"
          >
            <div class="upload-content">
              <i class="el-icon-upload"></i>
              <div class="el-upload__text">
                拖拽JSON文件到此处，或 <em>点击上传</em>
              </div>
              <div class="el-upload__tip">
                导入完整课程JSON备份文件
              </div>
            </div>
          </el-upload>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import axios from 'axios'
import { getToken } from '@/utils/auth'
import { listCourse } from '@/api/edu/course'
import { listUnitByCourse } from '@/api/edu/unit'

export default {
  name: 'ImportExport',
  data() {
    return {
      courses: [],
      units: [],
      importUnits: [],
      selectedCourseId: null,
      selectedUnitId: null,
      importFullCourseId: null,
      importForm: {
        courseId: null,
        unitId: null,
        courseName: '',
        unitName: ''
      },
      importResult: {
        show: false,
        successCount: 0,
        failCount: 0,
        errors: [],
        courseCreated: false,
        unitCreated: false,
        courseName: '',
        unitName: '',
        courseId: null,
        unitId: null
      },
      activeCollapse: ['txt']
    }
  },
  computed: {
    uploadHeaders() {
      return {
        'Authorization': 'Bearer ' + getToken()
      }
    },
    smartImportUrl() {
      return process.env.VUE_APP_BASE_API + '/edu/import-export/vocabulary/smart'
    },
    importFullCourseUrl() {
      return process.env.VUE_APP_BASE_API + '/edu/import-export/course/full'
    }
  },
  mounted() {
    this.loadCourses()
  },
  methods: {
    getToken,
    async loadCourses() {
      try {
        const response = await listCourse({})
        if (response.code === 200) {
          this.courses = response.rows || []
        }
      } catch (e) {
        this.$message.error('加载课程列表失败')
      }
    },
    async loadUnits() {
      if (this.selectedCourseId) {
        try {
          const response = await listUnitByCourse(this.selectedCourseId)
          if (response.code === 200) {
            this.units = response.data || []
            this.selectedUnitId = null
          }
        } catch (e) {
          this.$message.error('加载单元列表失败')
        }
      } else {
        this.units = []
        this.selectedUnitId = null
      }
    },
    async loadUnitsForImport() {
      if (this.importForm.courseId) {
        try {
          const response = await listUnitByCourse(this.importForm.courseId)
          if (response.code === 200) {
            this.importUnits = response.data || []
            this.importForm.unitId = null
          }
        } catch (e) {
          this.$message.error('加载单元列表失败')
        }
      } else {
        this.importUnits = []
        this.importForm.unitId = null
      }
    },
    beforeSmartUpload(file) {
      const isValidType = file.name.endsWith('.txt') || file.name.endsWith('.json')
      if (!isValidType) {
        this.$message.error('只能上传 TXT 或 JSON 文件')
        return false
      }

      if (this.importForm.courseId && !this.importForm.unitId && !this.importForm.unitName) {
        this.$message.warning('已选择课程但未选择单元，将自动创建新单元')
      }

      return true
    },
    submitUpload() {
      const formData = new FormData()
      const uploadComponent = this.$refs.uploadRef

      if (!uploadComponent.uploadFiles || uploadComponent.uploadFiles.length === 0) {
        this.$message.error('请先选择文件')
        return
      }

      const file = uploadComponent.uploadFiles[0].raw
      formData.append('file', file)

      if (this.importForm.courseId) {
        formData.append('courseId', this.importForm.courseId)
      }
      if (this.importForm.unitId) {
        formData.append('unitId', this.importForm.unitId)
      }
      if (this.importForm.courseName) {
        formData.append('courseName', this.importForm.courseName)
      }
      if (this.importForm.unitName) {
        formData.append('unitName', this.importForm.unitName)
      }

      this.$message.info('正在导入，请稍候...')

      axios.post(this.smartImportUrl, formData, {
        headers: {
          'Authorization': 'Bearer ' + getToken(),
          'Content-Type': 'multipart/form-data'
        }
      }).then(res => {
        this.handleSmartImportSuccess(res.data)
      }).catch(err => {
        this.handleImportError(err)
      })
    },
    resetForm() {
      this.importForm = {
        courseId: null,
        unitId: null,
        courseName: '',
        unitName: ''
      }
      this.importUnits = []
      this.$refs.uploadRef.clearFiles()
      this.importResult.show = false
    },
    handleSmartImportSuccess(response) {
      if (response.code === 200) {
        const data = response.data || {}
        this.importResult = {
          show: true,
          successCount: data.successCount || 0,
          failCount: data.failCount || 0,
          errors: (data.errors || []).map((err, idx) => ({ index: idx + 1, error: err })),
          courseCreated: data.courseCreated || false,
          unitCreated: data.unitCreated || false,
          courseName: data.courseName || '',
          unitName: data.unitName || '',
          courseId: data.courseId || null,
          unitId: data.unitId || null
        }

        let msg = '导入成功'
        if (data.courseCreated) {
          msg += '，已创建课程：' + data.courseName
        }
        if (data.unitCreated) {
          msg += '，已创建单元：' + data.unitName
        }
        msg += '，共导入 ' + (data.successCount || 0) + ' 个词汇'

        if ((data.failCount || 0) > 0) {
          this.$message.warning(msg + '，失败 ' + data.failCount + ' 个')
        } else {
          this.$message.success(msg)
        }

        this.loadCourses()
      } else {
        this.$message.error(response.msg || '导入失败')
      }
    },
    handleImportError(error) {
      this.$message.error('导入失败：' + (error.message || '网络错误'))
    },
    handleImportFullSuccess(response) {
      if (response.code === 200 && response.data) {
        this.$message.success('导入成功！')
        if (response.data.courseCreated) {
          this.$message.info('已创建新课程：' + response.data.courseName)
        }
        if (response.data.unitCreated) {
          this.$message.info('已创建单元：' + response.data.unitName)
        }
        this.loadCourses()
      } else {
        this.$message.error(response.msg || '导入失败')
      }
    },
    handleImportFullError(error) {
      this.$message.error('导入失败：' + (error.message || '网络错误'))
    },
    getImportFullData() {
      const data = {}
      if (this.importFullCourseId) {
        data.courseId = this.importFullCourseId
      }
      return data
    },
    async exportUnitTxt() {
      if (!this.selectedUnitId) {
        this.$message.error('请选择单元')
        return
      }
      try {
        const response = await axios({
          url: process.env.VUE_APP_BASE_API + '/edu/import-export/unit/' + this.selectedUnitId + '/vocabulary/txt',
          method: 'get',
          headers: { Authorization: 'Bearer ' + getToken() },
          responseType: 'blob'
        })
        const blob = new Blob([response.data], { type: 'text/plain;charset=utf-8' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = 'vocabulary.txt'
        link.click()
        window.URL.revokeObjectURL(url)
      } catch (e) {
        this.$message.error('导出失败')
      }
    },
    async exportUnitJson() {
      if (!this.selectedUnitId) {
        this.$message.error('请选择单元')
        return
      }
      try {
        const response = await axios({
          url: process.env.VUE_APP_BASE_API + '/edu/import-export/unit/' + this.selectedUnitId + '/vocabulary/json',
          method: 'get',
          headers: { Authorization: 'Bearer ' + getToken() },
          responseType: 'blob'
        })
        const blob = new Blob([response.data], { type: 'application/json;charset=utf-8' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = 'vocabulary.json'
        link.click()
        window.URL.revokeObjectURL(url)
      } catch (e) {
        this.$message.error('导出失败')
      }
    },
    async exportCourseJson() {
      if (!this.selectedCourseId) {
        this.$message.error('请选择课程')
        return
      }
      try {
        const response = await axios({
          url: process.env.VUE_APP_BASE_API + '/edu/import-export/course/' + this.selectedCourseId + '/json',
          method: 'get',
          headers: { Authorization: 'Bearer ' + getToken() },
          responseType: 'blob'
        })
        const blob = new Blob([response.data], { type: 'application/json;charset=utf-8' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = 'course.json'
        link.click()
        window.URL.revokeObjectURL(url)
      } catch (e) {
        this.$message.error('导出失败')
      }
    }
  }
}
</script>

<style scoped>
.import-export-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 140px);
}

.page-header {
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 2px solid #e4e7ed;
}

.page-header h2 {
  margin: 0 0 8px 0;
  color: #303133;
  font-size: 22px;
  font-weight: 600;
}

.page-header h2 i {
  margin-right: 8px;
  color: #409eff;
}

.page-header .subtitle {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.import-card {
  border-radius: 12px;
  overflow: hidden;
}

.import-card :deep(.el-card__header) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 16px 20px;
}

.import-card :deep(.el-card__header) .card-header span {
  color: white;
  font-weight: 600;
  font-size: 16px;
}

.import-card :deep(.el-card__header) .card-header i {
  margin-right: 6px;
}

.tips-alert {
  margin-bottom: 20px;
  border-radius: 8px;
}

.tips {
  font-size: 13px;
  line-height: 2;
}

.tips p {
  margin: 4px 0;
  color: #606266;
}

.tips i {
  color: #67c23a;
  margin-right: 6px;
}

.import-form {
  margin-bottom: 16px;
}

.import-form :deep(.el-divider) {
  margin: 16px 0;
}

.import-form :deep(.el-divider__text) {
  color: #909399;
  font-size: 12px;
}

.import-form :deep(.el-input-group__prepend) {
  background: #f5f7fa;
  border-color: #dcdfe6;
}

.import-form :deep(.el-input-group__prepend i) {
  color: #909399;
}

.upload-demo {
  border: 2px dashed #d9d9d9;
  border-radius: 12px;
  background: #fafafa;
  transition: all 0.3s;
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 120px;
}

.upload-demo:hover {
  border-color: #667eea;
  background: #f0f0ff;
}

.upload-content {
  padding: 30px 20px;
  text-align: center;
}

.upload-content i.el-icon-upload {
  font-size: 48px;
  color: #667eea;
  margin-bottom: 12px;
}

.upload-content .el-upload__text {
  color: #606266;
  font-size: 14px;
}

.upload-content .el-upload__text em {
  color: #667eea;
  font-style: normal;
  font-weight: 600;
}

.upload-content .el-upload__tip {
  color: #909399;
  margin-top: 8px;
}

.upload-actions {
  margin-top: 20px;
  text-align: center;
}

.action-btn {
  min-width: 120px;
  border-radius: 20px;
}

.upload-actions .action-btn:first-child {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
}

.upload-actions .action-btn:first-child:hover {
  background: linear-gradient(135deg, #5a6fd6 0%, #6a4190 100%);
}

.result-card {
  margin-top: 20px;
  border-radius: 12px;
}

.result-card :deep(.el-card__header) {
  background: #f5f7fa;
  padding: 14px 20px;
  border-bottom: 1px solid #ebeef5;
}

.result-card :deep(.el-card__header) span {
  font-weight: 600;
  color: #303133;
}

.result-card :deep(.el-card__header) i {
  margin-right: 6px;
  color: #409eff;
}

.result-stats {
  padding: 16px 0;
  background: #f5f7fa;
  border-radius: 8px;
  margin-bottom: 16px;
}

.stat-item {
  text-align: center;
  padding: 12px 8px;
  border-radius: 8px;
}

.stat-item.success {
  background: linear-gradient(135deg, #67c23a22 0%, #85ce6122 100%);
}

.stat-item.danger {
  background: linear-gradient(135deg, #f56c6c22 0%, #e6424222 100%);
}

.stat-item.info {
  background: linear-gradient(135deg, #409eff22 0%, #66b1ff22 100%);
}

.stat-item .stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #303133;
}

.stat-item .stat-label {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}

.info-item {
  padding: 8px 12px;
  border-radius: 6px;
  margin-bottom: 8px;
  font-size: 13px;
}

.info-item.success {
  background: #f0f9eb;
  color: #67c23a;
}

.info-item i {
  margin-right: 6px;
}

.success-message {
  text-align: center;
  padding: 24px;
  background: #f0f9eb;
  border-radius: 8px;
  margin-top: 12px;
}

.success-message i {
  font-size: 48px;
  color: #67c23a;
  display: block;
  margin-bottom: 12px;
}

.success-message p {
  margin: 0;
  color: #303133;
  font-size: 15px;
}

.error-table {
  margin-top: 12px;
  border-radius: 8px;
  overflow: hidden;
}

.export-card {
  border-radius: 12px;
  margin-bottom: 20px;
}

.export-card :deep(.el-card__header) {
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
  color: white;
  padding: 14px 20px;
}

.export-card :deep(.el-card__header) .card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.export-card :deep(.el-card__header) span {
  color: white;
  font-weight: 600;
}

.export-card :deep(.el-card__header) i {
  margin-right: 6px;
}

.search-form {
  margin-bottom: 20px;
}

.search-form :deep(.el-form-item__label) {
  color: #606266;
  font-weight: 500;
}

.export-buttons {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.export-buttons .el-button {
  flex: 1;
  min-width: 100px;
  border-radius: 20px;
}

.import-full-card {
  border-radius: 12px;
  margin-bottom: 20px;
}

.import-full-card :deep(.el-card__header) {
  background: linear-gradient(135deg, #f56c6c 0%, #e64242 100%);
  padding: 14px 20px;
}

.import-full-card :deep(.el-card__header) .card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.import-full-card :deep(.el-card__header) span {
  color: white;
  font-weight: 600;
}

.import-full-card :deep(.el-card__header) i {
  margin-right: 6px;
}

.import-full-form {
  margin-bottom: 16px;
}

.import-full-upload {
  min-height: 100px;
}

.format-card {
  border-radius: 12px;
}

.format-card :deep(.el-card__header) {
  background: #f5f7fa;
  padding: 14px 20px;
  border-bottom: 1px solid #ebeef5;
}

.format-card :deep(.el-card__header) span {
  color: #303133;
  font-weight: 600;
}

.format-card :deep(.el-card__header) i {
  margin-right: 6px;
  color: #409eff;
}

.format-examples {
  padding: 8px 0;
}

.format-examples p {
  color: #606266;
  font-size: 13px;
  margin: 8px 0 6px 0;
}

.format-examples pre {
  background: #f5f7fa;
  border-radius: 6px;
  padding: 12px;
  font-size: 12px;
  color: #303133;
  margin: 6px 0 12px 0;
  border: 1px solid #ebeef5;
}
</style>
