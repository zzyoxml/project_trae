<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="用户名" prop="userName">
        <el-input
          v-model="queryParams.userName"
          placeholder="请输入用户名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="学习语言" prop="language">
        <el-select v-model="queryParams.language" placeholder="请选择语言" clearable size="small">
          <el-option label="英语" value="英语" />
          <el-option label="日语" value="日语" />
          <el-option label="汉语" value="汉语" />
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
          <div class="stat-value">{{ stats.totalLearningTime }}</div>
          <div class="stat-label">总学习时长（小时）</div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6">
        <el-card class="stat-card">
          <div class="stat-value">{{ stats.activeUsers }}</div>
          <div class="stat-label">活跃用户数</div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6">
        <el-card class="stat-card">
          <div class="stat-value">{{ stats.totalEnrollments }}</div>
          <div class="stat-label">总报名人次</div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6">
        <el-card class="stat-card">
          <div class="stat-value">{{ stats.avgProgress }}%</div>
          <div class="stat-label">平均完成进度</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="chart-row">
      <el-col :xs="24" :lg="12">
        <el-card>
          <div slot="header">
            <span>📊 用户学习时长分布</span>
          </div>
          <div ref="learningTimeChart" style="width: 100%; height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="12">
        <el-card>
          <div slot="header">
            <span>📈 课程完成率</span>
          </div>
          <div ref="completionRateChart" style="width: 100%; height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >导出数据</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="userList">
      <el-table-column label="用户ID" align="center" prop="userId" width="80" />
      <el-table-column label="用户名" align="center" prop="nickName" width="120" />
      <el-table-column label="学习语言" align="center" prop="language" width="100">
        <template slot-scope="scope">
          <el-tag :type="getLanguageType(scope.row.language)" size="small">
            {{ scope.row.language }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="学习时长" align="center" prop="learningTime" width="100">
        <template slot-scope="scope">
          {{ scope.row.learningTime }}小时
        </template>
      </el-table-column>
      <el-table-column label="学习进度" align="center" prop="progress" width="200">
        <template slot-scope="scope">
          <el-progress :percentage="scope.row.progress" :color="getProgressColor(scope.row.progress)"></el-progress>
        </template>
      </el-table-column>
      <el-table-column label="完成课程" align="center" prop="completedCourses" width="100" />
      <el-table-column label="获得积分" align="center" prop="points" width="100" />
      <el-table-column label="获得成就" align="center" prop="achievementCount" width="100" />
      <el-table-column label="最近学习" align="center" prop="lastLearningTime" width="180">
        <template slot-scope="scope">
          <span>{{ formatDate(scope.row.lastLearningTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150">
        <template slot-scope="scope">
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
            @click="handleResetProgress(scope.row)"
          >重置</el-button>
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

    <el-dialog title="用户学习详情" :visible.sync="detailOpen" width="900px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="用户名">{{ detailData.nickName }}</el-descriptions-item>
        <el-descriptions-item label="学习语言">
          <el-tag :type="getLanguageType(detailData.language)" size="small">
            {{ detailData.language }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="总学习时长">{{ detailData.learningTime }}小时</el-descriptions-item>
        <el-descriptions-item label="完成课程数">{{ detailData.completedCourses }}</el-descriptions-item>
        <el-descriptions-item label="获得积分">{{ detailData.points }}</el-descriptions-item>
        <el-descriptions-item label="获得成就">{{ detailData.achievementCount }}</el-descriptions-item>
        <el-descriptions-item label="最近学习时间" :span="2">{{ formatDate(detailData.lastLearningTime) }}</el-descriptions-item>
      </el-descriptions>

      <el-divider>学习历史</el-divider>
      <el-table :data="detailData.learningHistory || []">
        <el-table-column label="课程名称" align="center" prop="courseName" />
        <el-table-column label="进度" align="center" prop="progress" width="150">
          <template slot-scope="scope">
            <el-progress :percentage="scope.row.progress" :color="getProgressColor(scope.row.progress)"></el-progress>
          </template>
        </el-table-column>
        <el-table-column label="学习时长" align="center" prop="duration" width="100">
          <template slot-scope="scope">
            {{ scope.row.duration }}小时
          </template>
        </el-table-column>
        <el-table-column label="开始时间" align="center" prop="startTime" width="180" />
      </el-table>

      <div slot="footer" class="dialog-footer">
        <el-button @click="detailOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'EduUserLearning',
  data() {
    return {
      loading: true,
      showSearch: true,
      total: 0,
      userList: [],
      stats: {
        totalLearningTime: 0,
        activeUsers: 0,
        totalEnrollments: 0,
        avgProgress: 0
      },
      detailOpen: false,
      detailData: {},
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: null,
        language: null
      }
    }
  },
  created() {
    this.getList()
  },
  mounted() {
    this.loadCharts()
  },
  methods: {
    getList() {
      this.loading = true
      setTimeout(() => {
        this.userList = [
          {
            userId: 1,
            nickName: '张小明',
            language: '英语',
            learningTime: 45,
            progress: 75,
            completedCourses: 3,
            points: 850,
            achievementCount: 12,
            lastLearningTime: '2024-01-15 10:30:00'
          },
          {
            userId: 2,
            nickName: '李华',
            language: '日语',
            learningTime: 38,
            progress: 60,
            completedCourses: 2,
            points: 680,
            achievementCount: 8,
            lastLearningTime: '2024-01-14 15:20:00'
          },
          {
            userId: 3,
            nickName: '王芳',
            language: '汉语',
            learningTime: 52,
            progress: 85,
            completedCourses: 4,
            points: 920,
            achievementCount: 15,
            lastLearningTime: '2024-01-15 09:15:00'
          },
          {
            userId: 4,
            nickName: 'John',
            language: '英语',
            learningTime: 30,
            progress: 50,
            completedCourses: 2,
            points: 520,
            achievementCount: 6,
            lastLearningTime: '2024-01-13 14:00:00'
          },
          {
            userId: 5,
            nickName: '田中',
            language: '日语',
            learningTime: 28,
            progress: 45,
            completedCourses: 1,
            points: 450,
            achievementCount: 5,
            lastLearningTime: '2024-01-12 11:30:00'
          }
        ]
        this.total = this.userList.length
        this.stats = {
          totalLearningTime: 193,
          activeUsers: 156,
          totalEnrollments: 423,
          avgProgress: 63
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
    handleDetail(row) {
      this.detailData = {
        ...row,
        learningHistory: [
          { courseName: '英语口语入门', progress: 100, duration: 12, startTime: '2024-01-01' },
          { courseName: '英语语法精讲', progress: 80, duration: 20, startTime: '2024-01-10' },
          { courseName: '英语听力训练', progress: 45, duration: 13, startTime: '2024-01-14' }
        ]
      }
      this.detailOpen = true
    },
    handleResetProgress(row) {
      this.$confirm(`是否重置用户"${row.nickName}"的学习进度?`, '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('重置成功')
        this.getList()
      })
    },
    handleExport() {
      this.$message.info('导出功能开发中')
    },
    loadCharts() {
      this.initLearningTimeChart()
      this.initCompletionRateChart()
    },
    initLearningTimeChart() {
      const chartDom = this.$refs.learningTimeChart
      if (!chartDom) return

      const myChart = echarts.init(chartDom)
      const option = {
        tooltip: {
          trigger: 'item'
        },
        series: [
          {
            name: '学习时长',
            type: 'pie',
            radius: ['40%', '70%'],
            data: [
              { value: 45, name: '英语' },
              { value: 30, name: '日语' },
              { value: 25, name: '汉语' }
            ],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      }
      myChart.setOption(option)
      window.addEventListener('resize', () => myChart.resize())
    },
    initCompletionRateChart() {
      const chartDom = this.$refs.completionRateChart
      if (!chartDom) return

      const myChart = echarts.init(chartDom)
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'value',
          max: 100,
          axisLabel: {
            formatter: '{value}%'
          }
        },
        yAxis: {
          type: 'category',
          data: ['0-20%', '20-40%', '40-60%', '60-80%', '80-100%']
        },
        series: [
          {
            name: '用户数',
            type: 'bar',
            data: [15, 25, 35, 50, 31],
            itemStyle: {
              color: '#409EFF'
            }
          }
        ]
      }
      myChart.setOption(option)
      window.addEventListener('resize', () => myChart.resize())
    },
    getLanguageType(language) {
      const types = {
        '英语': 'primary',
        '日语': 'success',
        '汉语': 'warning'
      }
      return types[language] || 'info'
    },
    getProgressColor(progress) {
      if (progress < 30) return '#F56C6C'
      if (progress < 60) return '#E6A23C'
      if (progress < 80) return '#409EFF'
      return '#67C23A'
    },
    formatDate(date) {
      return date || '-'
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

  .stat-value {
    font-size: 32px;
    font-weight: bold;
    color: #303133;
    margin-bottom: 10px;
  }

  .stat-label {
    font-size: 14px;
    color: #909399;
  }
}

.chart-row {
  margin-bottom: 20px;
}
</style>
