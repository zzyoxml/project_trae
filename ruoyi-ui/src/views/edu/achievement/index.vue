<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="成就名称" prop="achievementName">
        <el-input
          v-model="queryParams.achievementName"
          placeholder="请输入成就名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择类型" clearable size="small">
          <el-option label="学习成就" value="学习" />
          <el-option label="社区成就" value="社区" />
          <el-option label="系统成就" value="系统" />
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

    <el-row :gutter="20" class="stats-row">
      <el-col :xs="12" :sm="6">
        <el-card class="stat-card">
          <div class="stat-icon" style="color: #409EFF">
            <i class="el-icon-medal"></i>
          </div>
          <div class="stat-value">{{ stats.totalAchievements }}</div>
          <div class="stat-label">成就总数</div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6">
        <el-card class="stat-card">
          <div class="stat-icon" style="color: #67C23A">
            <i class="el-icon-user-solid"></i>
          </div>
          <div class="stat-value">{{ stats.totalEarned }}</div>
          <div class="stat-label">已获得次数</div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6">
        <el-card class="stat-card">
          <div class="stat-icon" style="color: #E6A23C">
            <i class="el-icon-star-on"></i>
          </div>
          <div class="stat-value">{{ stats.popularAchievement }}</div>
          <div class="stat-label">最热门成就</div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6">
        <el-card class="stat-card">
          <div class="stat-icon" style="color: #F56C6C">
            <i class="el-icon-trophy"></i>
          </div>
          <div class="stat-value">{{ stats.topLearner }}</div>
          <div class="stat-label">榜首用户</div>
        </el-card>
      </el-col>
    </el-row>

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

    <el-table v-loading="loading" :data="achievementList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="成就ID" align="center" prop="achievementId" width="80" />
      <el-table-column label="成就名称" align="center" prop="achievementName" width="150" />
      <el-table-column label="图标" align="center" prop="icon" width="80">
        <template slot-scope="scope">
          <span style="font-size: 24px">{{ scope.row.icon }}</span>
        </template>
      </el-table-column>
      <el-table-column label="类型" align="center" prop="type" width="100">
        <template slot-scope="scope">
          <el-tag :type="getTypeType(scope.row.type)" size="small">
            {{ scope.row.type }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="描述" align="center" prop="description" :show-overflow-tooltip="true" />
      <el-table-column label="所需积分" align="center" prop="points" width="100" />
      <el-table-column label="获得人数" align="center" prop="earnedCount" width="100" />
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

    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="成就名称" prop="achievementName">
          <el-input v-model="form.achievementName" placeholder="请输入成就名称" />
        </el-form-item>
        <el-form-item label="成就图标" prop="icon">
          <el-input v-model="form.icon" placeholder="请输入图标emoji或图标代码">
            <template slot="append">
              <span style="font-size: 20px">{{ form.icon }}</span>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择类型">
            <el-option label="学习成就" value="学习" />
            <el-option label="社区成就" value="社区" />
            <el-option label="系统成就" value="系统" />
          </el-select>
        </el-form-item>
        <el-form-item label="所需积分" prop="points">
          <el-input-number v-model="form.points" :min="0" />
        </el-form-item>
        <el-form-item label="成就描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入成就描述" />
        </el-form-item>
        <el-form-item label="条件说明" prop="condition">
          <el-input v-model="form.condition" placeholder="请输入获得条件说明" />
        </el-form-item>
        <el-form-item label="排序" prop="orderNum">
          <el-input-number v-model="form.orderNum" :min="0" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="0">启用</el-radio>
            <el-radio label="1">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="成就详情" :visible.sync="detailOpen" width="700px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="成就ID">{{ detailData.achievementId }}</el-descriptions-item>
        <el-descriptions-item label="成就名称">{{ detailData.achievementName }}</el-descriptions-item>
        <el-descriptions-item label="图标" :span="2">
          <span style="font-size: 32px">{{ detailData.icon }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="类型">
          <el-tag :type="getTypeType(detailData.type)" size="small">
            {{ detailData.type }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="所需积分">{{ detailData.points }}</el-descriptions-item>
        <el-descriptions-item label="获得人数">{{ detailData.earnedCount }}</el-descriptions-item>
        <el-descriptions-item label="条件说明" :span="2">{{ detailData.condition }}</el-descriptions-item>
        <el-descriptions-item label="成就描述" :span="2">{{ detailData.description }}</el-descriptions-item>
      </el-descriptions>

      <el-divider>最近获得用户</el-divider>
      <el-table :data="recentEarners">
        <el-table-column label="用户" align="center" prop="userName" width="120" />
        <el-table-column label="获得时间" align="center" prop="earnedTime" />
      </el-table>

      <div slot="footer" class="dialog-footer">
        <el-button @click="detailOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'EduAchievement',
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      achievementList: [],
      title: '',
      open: false,
      detailOpen: false,
      detailData: {},
      recentEarners: [],
      stats: {
        totalAchievements: 0,
        totalEarned: 0,
        popularAchievement: '-',
        topLearner: '-'
      },
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        achievementName: null,
        type: null,
        status: null
      },
      form: {},
      rules: {
        achievementName: [
          { required: true, message: '成就名称不能为空', trigger: 'blur' }
        ],
        type: [
          { required: true, message: '请选择类型', trigger: 'change' }
        ],
        points: [
          { required: true, message: '请输入所需积分', trigger: 'blur' }
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
        this.achievementList = [
          {
            achievementId: 1,
            achievementName: '学习达人',
            icon: '🎓',
            type: '学习',
            description: '完成10门课程',
            condition: '完成10门课程学习',
            points: 500,
            earnedCount: 45,
            status: '0'
          },
          {
            achievementId: 2,
            achievementName: '单词王',
            icon: '📚',
            type: '学习',
            description: '背诵100个单词',
            condition: '累计背诵100个单词',
            points: 200,
            earnedCount: 123,
            status: '0'
          },
          {
            achievementId: 3,
            achievementName: '社交达人',
            icon: '💬',
            type: '社区',
            description: '发表10篇帖子',
            condition: '累计发表10篇帖子',
            points: 300,
            earnedCount: 67,
            status: '0'
          },
          {
            achievementId: 4,
            achievementName: '坚持不懈',
            icon: '🔥',
            type: '系统',
            description: '连续学习30天',
            condition: '连续学习30天不中断',
            points: 800,
            earnedCount: 23,
            status: '0'
          },
          {
            achievementId: 5,
            achievementName: '口语达人',
            icon: '🗣️',
            type: '学习',
            description: '完成口语练习100次',
            condition: '累计完成100次口语练习',
            points: 400,
            earnedCount: 89,
            status: '0'
          },
          {
            achievementId: 6,
            achievementName: '活跃用户',
            icon: '⭐',
            type: '系统',
            description: '登录100天',
            condition: '累计登录100天',
            points: 600,
            earnedCount: 156,
            status: '0'
          }
        ]
        this.total = this.achievementList.length
        this.stats = {
          totalAchievements: this.achievementList.length,
          totalEarned: this.achievementList.reduce((sum, a) => sum + a.earnedCount, 0),
          popularAchievement: '活跃用户',
          topLearner: '学习达人'
        }
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
      this.ids = selection.map(item => item.achievementId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加成就'
    },
    handleUpdate(row) {
      this.reset()
      const achievementId = row.achievementId || this.ids
      this.form = { ...row }
      this.open = true
      this.title = '修改成就'
    },
    handleDetail(row) {
      this.detailData = { ...row }
      this.recentEarners = [
        { userName: '张小明', earnedTime: '2024-01-15 10:30:00' },
        { userName: '李华', earnedTime: '2024-01-14 15:20:00' },
        { userName: '王芳', earnedTime: '2024-01-13 09:15:00' }
      ]
      this.detailOpen = true
    },
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.achievementId) {
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
        achievementId: null,
        achievementName: null,
        icon: '🏆',
        type: null,
        description: null,
        condition: null,
        points: 0,
        orderNum: 0,
        status: '0'
      }
      this.resetForm('form')
    },
    handleStatusChange(row) {
      const text = row.status === '0' ? '启用' : '禁用'
      this.$message.success(`${text}成就成功`)
    },
    handleDelete(row) {
      const achievementIds = row.achievementId || this.ids
      this.$confirm(`是否确认删除成就ID为"${achievementIds}"的数据项?`, '警告', {
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
    getTypeType(type) {
      const types = {
        '学习': 'primary',
        '社区': 'success',
        '系统': 'warning'
      }
      return types[type] || 'info'
    }
  }
}
</script>

<style scoped lang="scss">
.mb8 {
  margin-bottom: 8px;
}

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  text-align: center;
  padding: 20px;

  .stat-icon {
    font-size: 32px;
    margin-bottom: 10px;
  }

  .stat-value {
    font-size: 28px;
    font-weight: bold;
    color: #303133;
    margin-bottom: 5px;
  }

  .stat-label {
    font-size: 14px;
    color: #909399;
  }
}
</style>
