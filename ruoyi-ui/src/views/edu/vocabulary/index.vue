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
          <el-input
            v-model="queryParams.word"
            placeholder="搜索单词"
            clearable
            size="medium"
            @keyup.enter.native="handleQuery"
          />
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
    </el-row>

    <el-table v-loading="loading" :data="vocabularyList" @selection-change="handleSelectionChange" border stripe>
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column label="ID" align="center" prop="vocabId" width="60" />
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
      <el-table-column label="语言" align="center" width="70">
        <template slot-scope="scope">
          <el-tag size="small" :type="getLangTagType(scope.row.language)">
            {{ getLanguageText(scope.row.language) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="单词" align="center" prop="word" width="120" />
      <el-table-column label="音标" align="center" prop="pronunciation" width="130" />
      <el-table-column label="词性" align="center" prop="partOfSpeech" width="80" />
      <el-table-column label="释义" align="center" prop="definition" :show-overflow-tooltip="true" min-width="180" />
      <el-table-column label="例句" align="center" prop="exampleSentences" :show-overflow-tooltip="true" min-width="200" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" style="color:#f56c6c" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="650px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="所属课程" prop="courseId">
          <el-select v-model="formCourseId" placeholder="请选择课程" style="width: 100%" @change="onFormCourseChange">
            <el-option v-for="c in courseOptions" :key="c.courseId" :label="c.courseName + ' (' + getLanguageText(c.language) + ')'" :value="c.courseId" />
          </el-select>
        </el-form-item>
        <el-form-item label="所属单元" prop="unitId">
          <el-select v-model="form.unitId" placeholder="请选择所属单元" style="width: 100%" :disabled="!formCourseId">
            <el-option v-for="u in formUnitOptions" :key="u.unitId" :label="u.unitName" :value="u.unitId" />
          </el-select>
        </el-form-item>
        <el-form-item label="语言" prop="language">
          <el-select v-model="form.language" placeholder="请选择语言" style="width: 100%" :disabled="!!formCourseId">
            <el-option label="英语" value="en" />
            <el-option label="日语" value="ja" />
            <el-option label="汉语" value="zh" />
          </el-select>
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="单词" prop="word">
              <el-input v-model="form.word" placeholder="请输入单词" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="音标" prop="pronunciation">
              <el-input v-model="form.pronunciation" placeholder="请输入音标" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="词性" prop="partOfSpeech">
          <el-select v-model="form.partOfSpeech" placeholder="请选择词性" style="width: 100%">
            <el-option label="名词 noun" value="noun" />
            <el-option label="动词 verb" value="verb" />
            <el-option label="形容词 adj" value="adj" />
            <el-option label="副词 adv" value="adv" />
            <el-option label="介词 prep" value="prep" />
            <el-option label="连词 conj" value="conj" />
          </el-select>
        </el-form-item>
        <el-form-item label="释义" prop="definition">
          <el-input v-model="form.definition" type="textarea" :rows="2" placeholder="请输入中文释义" />
        </el-form-item>
        <el-form-item label="例句" prop="exampleSentences">
          <el-input v-model="form.exampleSentences" type="textarea" :rows="3" placeholder="请输入例句（多条用换行分隔）" />
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
import { listVocabulary, getVocabulary, addVocabulary, updateVocabulary, delVocabulary } from '@/api/edu/vocabulary'
import { listUnit } from '@/api/edu/unit'
import { listCourse } from '@/api/edu/course'

export default {
  name: 'EduVocabulary',
  data() {
    return {
      loading: false,
      ids: [],
      single: true,
      multiple: true,
      total: 0,
      vocabularyList: [],
      unitOptions: [],
      courseOptions: [],
      selectedCourseId: null,
      formCourseId: null,
      title: '',
      open: false,
      queryParams: { pageNum: 1, pageSize: 10, word: null, unitId: null },
      form: {},
      rules: {
        word: [{ required: true, message: '单词不能为空', trigger: 'blur' }],
        definition: [{ required: true, message: '释义不能为空', trigger: 'blur' }],
        unitId: [{ required: true, message: '请选择所属单元', trigger: 'change' }]
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
    getLangTagType(lang) {
      const map = { en: '', ja: 'success', zh: 'warning' }
      return map[lang] || 'info'
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
    onCourseChange(courseId) {
      this.queryParams.pageNum = 1
      this.queryParams.unitId = null
      if (courseId) {
        this.getList()
      } else {
        this.vocabularyList = []
        this.total = 0
      }
    },
    onFormCourseChange() {
      this.form.unitId = null
      if (this.formCourseId) {
        const course = this.courseOptions.find(c => c.courseId === this.formCourseId)
        if (course) {
          this.form.language = course.language
        }
      }
    },
    getList() {
      if (!this.selectedCourseId) {
        this.vocabularyList = []
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
          this.vocabularyList = []
          this.total = 0
          this.loading = false
          return
        }
        params.unitIds = courseUnitIds.join(',')
      }
      listVocabulary(params).then(res => {
        this.vocabularyList = res.rows || []
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
      this.queryParams.word = null
      this.queryParams.unitId = null
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.vocabId)
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
      this.title = '添加词汇'
    },
    handleUpdate(row) {
      this.reset()
      const vocabId = row.vocabId || this.ids[0]
      getVocabulary(vocabId).then(res => {
        this.form = res.data
        const u = this.unitOptions.find(item => item.unitId === this.form.unitId)
        if (u) {
          this.formCourseId = u.courseId
        }
        this.open = true
        this.title = '修改词汇'
      })
    },
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.vocabId != null) {
            updateVocabulary(this.form).then(() => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addVocabulary(this.form).then(() => {
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
        vocabId: null,
        unitId: null,
        word: null,
        language: 'en',
        pronunciation: null,
        partOfSpeech: null,
        definition: null,
        exampleSentences: null
      }
      this.formCourseId = null
      this.resetForm('form')
    },
    handleDelete(row) {
      const vocabIds = row.vocabId || this.ids
      const name = row.word || vocabIds
      this.$modal.confirm('确认删除词汇"' + name + '"？').then(() => {
        return delVocabulary(vocabIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
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
</style>