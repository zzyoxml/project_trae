<template>
  <div class="app-container">
    <el-card shadow="never" class="filter-card">
      <el-row :gutter="16" align="middle">
        <el-col :span="6">
          <span class="filter-label">选择课程：</span>
          <el-select
            v-model="selectedCourseId"
            placeholder="请先选择课程"
            clearable
            size="medium"
            style="width: 220px"
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
        <el-col :span="4">
          <el-input
            v-model="queryParams.unitName"
            placeholder="搜索单元名称"
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

    <el-table v-loading="loading" :data="unitList" @selection-change="handleSelectionChange" border stripe>
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column label="ID" align="center" prop="unitId" width="70" />
      <el-table-column label="所属课程" align="center" width="160">
        <template slot-scope="scope">
          <el-tag size="small" :type="getCourseTagType(scope.row.courseId)">
            {{ getCourseName(scope.row.courseId) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="单元名称" align="center" prop="unitName" :show-overflow-tooltip="true" min-width="160" />
      <el-table-column label="排序" align="center" prop="unitOrder" width="70" />
      <el-table-column label="描述" align="center" prop="description" :show-overflow-tooltip="true" min-width="200" />
      <el-table-column label="课时数" align="center" prop="totalLessons" width="80" />
      <el-table-column label="总时长(分)" align="center" prop="totalDuration" width="90" />
      <el-table-column label="状态" align="center" width="80">
        <template slot-scope="scope">
          <el-tag :type="scope.row.isLocked === '1' ? 'danger' : 'success'" size="small">
            {{ scope.row.isLocked === '1' ? '锁定' : '开放' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope"><span>{{ parseTime(scope.row.createTime) }}</span></template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" style="color:#f56c6c" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="650px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="所属课程" prop="courseId">
          <el-select v-model="form.courseId" placeholder="请选择课程" style="width: 100%" :disabled="!!form.unitId">
            <el-option v-for="c in courseOptions" :key="c.courseId" :label="c.courseName + ' (' + getLanguageText(c.language) + ')'" :value="c.courseId" />
          </el-select>
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="单元名称" prop="unitName">
              <el-input v-model="form.unitName" placeholder="请输入单元名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="排序" prop="unitOrder">
              <el-input-number v-model="form.unitOrder" :min="1" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="课时数" prop="totalLessons">
              <el-input-number v-model="form.totalLessons" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="总时长(分)" prop="totalDuration">
              <el-input-number v-model="form.totalDuration" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="是否锁定" prop="isLocked">
              <el-switch v-model="form.isLocked" active-value="1" inactive-value="0" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入单元描述" />
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
import { listUnit, getUnit, addUnit, updateUnit, delUnit } from '@/api/edu/unit'
import { listCourse } from '@/api/edu/course'

export default {
  name: 'EduUnit',
  data() {
    return {
      loading: false,
      ids: [],
      single: true,
      multiple: true,
      total: 0,
      unitList: [],
      courseOptions: [],
      selectedCourseId: null,
      title: '',
      open: false,
      queryParams: { pageNum: 1, pageSize: 10, unitName: null },
      form: {},
      rules: {
        courseId: [{ required: true, message: '请选择所属课程', trigger: 'change' }],
        unitName: [{ required: true, message: '单元名称不能为空', trigger: 'blur' }],
        unitOrder: [{ required: true, message: '排序不能为空', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.loadCourses()
  },
  methods: {
    loadCourses() {
      listCourse({ pageNum: 1, pageSize: 1000 }).then(res => {
        console.log('课程列表数据:', res)
        this.courseOptions = res.rows || []
      }).catch(err => {
        console.error('加载课程列表失败:', err)
        this.$message.error('加载课程列表失败')
      })
    },
    getLanguageText(lang) {
      const map = { en: '英语', ja: '日语', zh: '汉语' }
      return map[lang] || lang
    },
    getCourseName(courseId) {
      const c = this.courseOptions.find(item => item.courseId === courseId)
      return c ? c.courseName : (courseId || '-')
    },
    getCourseTagType(courseId) {
      const c = this.courseOptions.find(item => item.courseId === courseId)
      if (!c) return 'info'
      const map = { en: '', ja: 'success', zh: 'warning' }
      return map[c.language] || 'info'
    },
    onCourseChange(courseId) {
      this.queryParams.pageNum = 1
      if (courseId) {
        this.getList()
      } else {
        this.unitList = []
        this.total = 0
      }
    },
    getList() {
      if (!this.selectedCourseId) {
        this.unitList = []
        this.total = 0
        return
      }
      this.loading = true
      const params = {
        ...this.queryParams,
        courseId: this.selectedCourseId
      }
      listUnit(params).then(res => {
        this.unitList = res.rows || []
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
      this.queryParams.unitName = null
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.unitId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      if (!this.selectedCourseId) {
        this.$modal.msgWarning('请先选择课程')
        return
      }
      this.reset()
      this.form.courseId = this.selectedCourseId
      this.open = true
      this.title = '添加单元 - ' + this.getCourseName(this.selectedCourseId)
    },
    handleUpdate(row) {
      this.reset()
      const unitId = row.unitId || this.ids[0]
      getUnit(unitId).then(res => {
        this.form = res.data
        this.open = true
        this.title = '修改单元'
      })
    },
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.unitId != null) {
            updateUnit(this.form).then(() => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addUnit(this.form).then(() => {
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
        unitId: null,
        courseId: this.selectedCourseId,
        unitName: null,
        unitOrder: 1,
        description: null,
        totalLessons: 0,
        totalDuration: 0,
        isLocked: '0',
        passingScore: 60,
        experienceReward: 100
      }
      this.resetForm('form')
    },
    handleDelete(row) {
      const ids = row.unitId || this.ids
      const name = row.unitName || ids
      this.$modal.confirm('确认删除单元"' + name + '"？删除后该单元下的课时和词汇也将受影响。').then(() => {
        return delUnit(ids)
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
  margin-right: 8px;
}
.mt8 {
  margin-top: 8px;
}
.mb8 {
  margin-bottom: 8px;
}
</style>