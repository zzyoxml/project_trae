<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="单词" prop="word">
        <el-input
          v-model="queryParams.word"
          placeholder="请输入单词"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="语言" prop="language">
        <el-select v-model="queryParams.language" placeholder="请选择语言" clearable size="small" @change="handleQuery">
          <el-option label="英语" value="en" />
          <el-option label="日语" value="ja" />
          <el-option label="中文" value="zh" />
        </el-select>
      </el-form-item>
      <el-form-item label="等级" prop="level">
        <el-select v-model="queryParams.level" placeholder="请选择等级" clearable size="small" @change="handleQuery">
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
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="vocabularyList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="单词ID" align="center" prop="vocabId" width="80" />
      <el-table-column label="单词" align="center" prop="word" width="120" />
      <el-table-column label="语言" align="center" prop="language" width="80">
        <template slot-scope="scope">
          <el-tag :type="getLanguageTagType(scope.row.language)" size="small">
            {{ getLanguageName(scope.row.language) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="音标" align="center" prop="pronunciation" width="150" />
      <el-table-column label="词性" align="center" prop="partOfSpeech" width="80" />
      <el-table-column label="释义" align="center" prop="definition" :show-overflow-tooltip="true" width="200" />
      <el-table-column label="等级" align="center" prop="level" width="80">
        <template slot-scope="scope">
          <el-tag :type="getLevelTagType(scope.row.level)" size="small">
            {{ getLevelName(scope.row.level) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="难度" align="center" prop="difficulty" width="80" />
      <el-table-column label="频率" align="center" prop="frequency" width="80" />
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

    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="单词" prop="word">
              <el-input v-model="form.word" placeholder="请输入单词" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="语言" prop="language">
              <el-select v-model="form.language" placeholder="请选择语言" style="width: 100%">
                <el-option label="英语" value="en" />
                <el-option label="日语" value="ja" />
                <el-option label="中文" value="zh" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="音标" prop="pronunciation">
              <el-input v-model="form.pronunciation" placeholder="请输入音标" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="词性" prop="partOfSpeech">
              <el-select v-model="form.partOfSpeech" placeholder="请选择词性" style="width: 100%">
                <el-option label="名词" value="noun" />
                <el-option label="动词" value="verb" />
                <el-option label="形容词" value="adj" />
                <el-option label="副词" value="adv" />
                <el-option label="其他" value="other" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="等级" prop="level">
              <el-select v-model="form.level" placeholder="请选择等级" style="width: 100%">
                <el-option label="初级" value="beginner" />
                <el-option label="中级" value="intermediate" />
                <el-option label="高级" value="advanced" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="难度" prop="difficulty">
              <el-input-number v-model="form.difficulty" :min="1" :max="5" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="频率" prop="frequency">
              <el-input-number v-model="form.frequency" :min="1" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="释义" prop="definition">
          <el-input v-model="form.definition" type="textarea" :rows="3" placeholder="请输入单词释义" />
        </el-form-item>
        <el-form-item label="例句" prop="exampleSentences">
          <el-input v-model="form.exampleSentences" type="textarea" :rows="3" placeholder="请输入例句（JSON数组格式，如：[&quot;句子1&quot;,&quot;句子2&quot;]）" />
        </el-form-item>
        <el-form-item label="同义词" prop="synonyms">
          <el-input v-model="form.synonyms" placeholder="请输入同义词（JSON数组格式）" />
        </el-form-item>
        <el-form-item label="反义词" prop="antonyms">
          <el-input v-model="form.antonyms" placeholder="请输入反义词（JSON数组格式）" />
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="音频URL" prop="audioUrl">
              <el-input v-model="form.audioUrl" placeholder="请输入音频URL" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="配图URL" prop="imageUrl">
              <el-input v-model="form.imageUrl" placeholder="请输入配图URL" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="标签" prop="tags">
          <el-input v-model="form.tags" placeholder="请输入标签（多个用逗号分隔）" />
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

export default {
  name: 'EduVocabulary',
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      vocabularyList: [],
      title: '',
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        word: null,
        language: null,
        level: null
      },
      form: {},
      rules: {
        word: [
          { required: true, message: '单词不能为空', trigger: 'blur' }
        ],
        language: [
          { required: true, message: '请选择语言', trigger: 'change' }
        ],
        definition: [
          { required: true, message: '释义不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getLanguageName(language) {
      const map = { 'en': '英语', 'ja': '日语', 'zh': '中文' }
      return map[language] || language
    },
    getLanguageTagType(language) {
      const map = { 'en': 'info', 'ja': 'success', 'zh': 'primary' }
      return map[language] || 'default'
    },
    getLevelName(level) {
      const map = { 'beginner': '初级', 'intermediate': '中级', 'advanced': '高级' }
      return map[level] || level
    },
    getLevelTagType(level) {
      const map = { 'beginner': 'success', 'intermediate': 'warning', 'advanced': 'danger' }
      return map[level] || 'default'
    },
    getList() {
      this.loading = true
      listVocabulary(this.queryParams).then(response => {
        this.vocabularyList = response.rows
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
      this.ids = selection.map(item => item.vocabId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加词汇'
    },
    handleUpdate(row) {
      this.reset()
      const vocabId = row.vocabId || this.ids[0]
      getVocabulary(vocabId).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改词汇'
      })
    },
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.vocabId != null) {
            updateVocabulary(this.form).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addVocabulary(this.form).then(response => {
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
        word: null,
        language: 'en',
        pronunciation: null,
        audioUrl: null,
        partOfSpeech: null,
        level: 'beginner',
        definition: null,
        exampleSentences: null,
        synonyms: null,
        antonyms: null,
        wordFamily: null,
        difficulty: 1,
        frequency: 1,
        tags: null,
        imageUrl: null
      }
      this.resetForm('form')
    },
    handleDelete(row) {
      const vocabIds = row.vocabId || this.ids
      this.$modal.confirm('是否确认删除词汇编号为"' + vocabIds + '"的数据项？').then(function() {
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
.mb8 {
  margin-bottom: 8px;
}
</style>