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
          <el-option label="英语" value="英语" />
          <el-option label="日语" value="日语" />
          <el-option label="汉语" value="汉语" />
        </el-select>
      </el-form-item>
      <el-form-item label="难度" prop="difficulty">
        <el-select v-model="queryParams.difficulty" placeholder="请选择难度" clearable size="small">
          <el-option label="初级" value="初级" />
          <el-option label="中级" value="中级" />
          <el-option label="高级" value="高级" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable size="small">
          <el-option label="启用" value="0" />
          <el-option label="禁用" value="1" />
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
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >导出</el-button>
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
            {{ scope.row.language }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="难度" align="center" prop="difficulty" width="80">
        <template slot-scope="scope">
          <el-tag :type="getDifficultyType(scope.row.difficulty)" size="small">
            {{ scope.row.difficulty }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="课程描述" align="center" prop="description" :show-overflow-tooltip="true" />
      <el-table-column label="学习人数" align="center" prop="enrollCount" width="100" />
      <el-table-column label="课程时长" align="center" prop="duration" width="100">
        <template slot-scope="scope">
          {{ scope.row.duration }}分钟
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" width="80">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            active-value="0"
            inactive-value="1"
            @change="handleStatusChange(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200">
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
            icon="el-icon-view"
            @click="handleDetail(scope.row)"
          >详情</el-button>
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
        <el-row>
          <el-col :span="12">
            <el-form-item label="课程名称" prop="courseName">
              <el-input v-model="form.courseName" placeholder="请输入课程名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="语言" prop="language">
              <el-select v-model="form.language" placeholder="请选择语言">
                <el-option label="英语" value="英语" />
                <el-option label="日语" value="日语" />
                <el-option label="汉语" value="汉语" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="难度等级" prop="difficulty">
              <el-select v-model="form.difficulty" placeholder="请选择难度">
                <el-option label="初级" value="初级" />
                <el-option label="中级" value="中级" />
                <el-option label="高级" value="高级" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="课程时长" prop="duration">
              <el-input-number v-model="form.duration" :min="1" label="请输入课程时长（分钟）" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="课程描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="4" placeholder="请输入课程描述" />
        </el-form-item>
        <el-form-item label="课程封面" prop="coverImage">
          <image-upload v-model="form.coverImage" />
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="排序" prop="orderNum">
              <el-input-number v-model="form.orderNum" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio label="0">启用</el-radio>
                <el-radio label="1">禁用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="课程详情" :visible.sync="detailOpen" width="900px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="课程ID">{{ detailData.courseId }}</el-descriptions-item>
        <el-descriptions-item label="课程名称">{{ detailData.courseName }}</el-descriptions-item>
        <el-descriptions-item label="语言">
          <el-tag :type="getLanguageType(detailData.language)" size="small">
            {{ detailData.language }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="难度">
          <el-tag :type="getDifficultyType(detailData.difficulty)" size="small">
            {{ detailData.difficulty }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="课程时长">{{ detailData.duration }}分钟</el-descriptions-item>
        <el-descriptions-item label="学习人数">{{ detailData.enrollCount }}</el-descriptions-item>
        <el-descriptions-item label="课程描述" :span="2">{{ detailData.description }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ parseTime(detailData.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ parseTime(detailData.updateTime) }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
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
      detailOpen: false,
      detailData: {},
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        courseName: null,
        language: null,
        difficulty: null,
        status: null
      },
      form: {},
      rules: {
        courseName: [
          { required: true, message: '课程名称不能为空', trigger: 'blur' }
        ],
        language: [
          { required: true, message: '请选择语言', trigger: 'change' }
        ],
        difficulty: [
          { required: true, message: '请选择难度', trigger: 'change' }
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
      setTimeout(() => {
        this.courseList = [
          {
            courseId: 1,
            courseName: '英语口语入门',
            language: '英语',
            difficulty: '初级',
            description: '专为初学者设计的英语口语基础课程',
            enrollCount: 1234,
            duration: 120,
            status: '0',
            createTime: '2024-01-01 10:00:00',
            updateTime: '2024-01-15 14:30:00'
          },
          {
            courseId: 2,
            courseName: '日语五十音图',
            language: '日语',
            difficulty: '初级',
            description: '学习日语假名发音和书写',
            enrollCount: 987,
            duration: 90,
            status: '0',
            createTime: '2024-01-02 10:00:00',
            updateTime: '2024-01-16 14:30:00'
          },
          {
            courseId: 3,
            courseName: '汉语拼音基础',
            language: '汉语',
            difficulty: '初级',
            description: '学习汉语拼音发音规则',
            enrollCount: 876,
            duration: 80,
            status: '0',
            createTime: '2024-01-03 10:00:00',
            updateTime: '2024-01-17 14:30:00'
          },
          {
            courseId: 4,
            courseName: '英语语法精讲',
            language: '英语',
            difficulty: '中级',
            description: '深入讲解英语语法知识',
            enrollCount: 654,
            duration: 150,
            status: '0',
            createTime: '2024-01-04 10:00:00',
            updateTime: '2024-01-18 14:30:00'
          },
          {
            courseId: 5,
            courseName: '日语N3备考',
            language: '日语',
            difficulty: '中级',
            description: '针对JLPT N3级别的备考课程',
            enrollCount: 543,
            duration: 180,
            status: '0',
            createTime: '2024-01-05 10:00:00',
            updateTime: '2024-01-19 14:30:00'
          }
        ]
        this.total = this.courseList.length
        this.loading = false
      }, 500)
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
      const courseId = row.courseId || this.ids
      this.form = { ...row }
      this.open = true
      this.title = '修改课程'
    },
    handleDetail(row) {
      this.detailData = { ...row }
      this.detailOpen = true
    },
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.courseId) {
            this.$message.success('修改成功')
          } else {
            this.$message.success('新增成功')
          }
          this.open = false
          this.getList()
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
        language: null,
        difficulty: null,
        description: null,
        duration: 60,
        coverImage: null,
        orderNum: 0,
        status: '0'
      }
      this.resetForm('form')
    },
    handleStatusChange(row) {
      const text = row.status === '0' ? '启用' : '禁用'
      this.$message.success(`${text}课程成功`)
    },
    handleDelete(row) {
      const courseIds = row.courseId || this.ids
      this.$confirm(`是否确认删除课程ID为"${courseIds}"的数据项?`, '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('删除成功')
        this.getList()
      })
    },
    handleExport() {
      this.$message.info('导出功能开发中')
    },
    getLanguageType(language) {
      const types = {
        '英语': 'primary',
        '日语': 'success',
        '汉语': 'warning'
      }
      return types[language] || 'info'
    },
    getDifficultyType(difficulty) {
      const types = {
        '初级': 'success',
        '中级': 'warning',
        '高级': 'danger'
      }
      return types[difficulty] || 'info'
    },
    parseTime(time) {
      return time || '-'
    }
  }
}
</script>

<style scoped>
.mb8 {
  margin-bottom: 8px;
}
</style>
