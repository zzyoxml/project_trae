<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="所属课程" prop="courseId">
        <el-select v-model="queryParams.courseId" placeholder="请选择课程" clearable size="small" @change="handleQuery">
          <el-option
            v-for="course in courseOptions"
            :key="course.courseId"
            :label="course.courseName"
            :value="course.courseId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="单元名称" prop="unitName">
        <el-input
          v-model="queryParams.unitName"
          placeholder="请输入单元名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
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

    <el-table v-loading="loading" :data="unitList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="单元ID" align="center" prop="unitId" width="80" />
      <el-table-column label="所属课程" align="center" prop="courseId" width="120">
        <template slot-scope="scope">
          {{ getCourseName(scope.row.courseId) }}
        </template>
      </el-table-column>
      <el-table-column label="单元名称" align="center" prop="unitName" :show-overflow-tooltip="true" width="180" />
      <el-table-column label="排序" align="center" prop="unitOrder" width="80" />
      <el-table-column label="描述" align="center" prop="description" :show-overflow-tooltip="true" width="200" />
      <el-table-column label="课时数" align="center" prop="totalLessons" width="80" />
      <el-table-column label="总时长(分)" align="center" prop="totalDuration" width="100" />
      <el-table-column label="是否锁定" align="center" prop="isLocked" width="80">
        <template slot-scope="scope">
          <el-tag :type="scope.row.isLocked ? 'warning' : 'success'" size="small">
            {{ scope.row.isLocked ? '是' : '否' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="及格分数" align="center" prop="passingScore" width="80" />
      <el-table-column label="经验奖励" align="center" prop="experienceReward" width="80" />
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

    <el-dialog :title="title" :visible.sync="open" width="650px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="所属课程" prop="courseId">
          <el-select v-model="form.courseId" placeholder="请选择课程" style="width: 100%">
            <el-option
              v-for="course in courseOptions"
              :key="course.courseId"
              :label="course.courseName"
              :value="course.courseId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="单元名称" prop="unitName">
          <el-input v-model="form.unitName" placeholder="请输入单元名称" />
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="排序" prop="unitOrder">
              <el-input-number v-model="form.unitOrder" :min="1" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否锁定" prop="isLocked">
              <el-switch v-model="form.isLocked" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="课时数" prop="totalLessons">
              <el-input-number v-model="form.totalLessons" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="总时长(分)" prop="totalDuration">
              <el-input-number v-model="form.totalDuration" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="及格分数" prop="passingScore">
              <el-input-number v-model="form.passingScore" :min="0" :max="100" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="经验奖励" prop="experienceReward">
          <el-input-number v-model="form.experienceReward" :min="0" />
        </el-form-item>
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
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      unitList: [],
      courseOptions: [],
      title: '',
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        courseId: null,
        unitName: null
      },
      form: {},
      rules: {
        courseId: [
          { required: true, message: '请选择所属课程', trigger: 'change' }
        ],
        unitName: [
          { required: true, message: '单元名称不能为空', trigger: 'blur' }
        ],
        unitOrder: [
          { required: true, message: '排序不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.loadCourses()
    this.getList()
  },
  methods: {
    loadCourses() {
      listCourse({ pageNum: 1, pageSize: 1000 }).then(response => {
        this.courseOptions = response.rows || []
      })
    },
    getCourseName(courseId) {
      const course = this.courseOptions.find(c => c.courseId === courseId)
      return course ? course.courseName : courseId
    },
    getList() {
      this.loading = true
      listUnit(this.queryParams).then(response => {
        this.unitList = response.rows
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
      this.ids = selection.map(item => item.unitId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加单元'
    },
    handleUpdate(row) {
      this.reset()
      const unitId = row.unitId || this.ids[0]
      getUnit(unitId).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改单元'
      })
    },
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.unitId != null) {
            updateUnit(this.form).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addUnit(this.form).then(response => {
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
        courseId: null,
        unitName: null,
        unitOrder: 1,
        description: null,
        totalLessons: 0,
        totalDuration: 0,
        isLocked: false,
        passingScore: 60,
        experienceReward: 100
      }
      this.resetForm('form')
    },
    handleDelete(row) {
      const unitIds = row.unitId || this.ids
      this.$modal.confirm('是否确认删除单元编号为"' + unitIds + '"的数据项？').then(function() {
        return delUnit(unitIds)
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