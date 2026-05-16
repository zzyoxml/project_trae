<template>
  <div class="app-container dashboard">
    <el-row :gutter="20" class="stats-row">
      <el-col :xs="12" :sm="6">
        <el-card class="stat-card stat-users">
          <div class="stat-icon">
            <i class="el-icon-user-solid"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.totalUsers }}</div>
            <div class="stat-label">总用户数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6">
        <el-card class="stat-card stat-courses">
          <div class="stat-icon">
            <i class="el-icon-reading"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.totalCourses }}</div>
            <div class="stat-label">课程总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6">
        <el-card class="stat-card stat-posts">
          <div class="stat-icon">
            <i class="el-icon-s-comment"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.totalPosts }}</div>
            <div class="stat-label">社区帖子</div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6">
        <el-card class="stat-card stat-achievements">
          <div class="stat-icon">
            <i class="el-icon-medal"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.totalAchievements }}</div>
            <div class="stat-label">成就总数</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="chart-row">
      <el-col :xs="24">
        <el-card class="chart-card">
          <div slot="header">
            <span>📊 学习数据趋势</span>
          </div>
          <div ref="trendChart" style="width: 100%; height: 400px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="chart-row">
      <el-col :xs="24">
        <el-card class="chart-card">
          <div slot="header">
            <span>🌐 课程语言分布</span>
          </div>
          <div ref="languageChart" style="width: 100%; height: 400px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="table-row">
      <el-col :xs="24">
        <el-card class="recent-card">
          <div slot="header">
            <span> 热门课程</span>
            <el-button style="float: right; padding: 3px 10px" type="text" @click="$router.push('/edu/course')">查看更多</el-button>
          </div>
          <el-table :data="popularCourses" style="width: 100%" :header-cell-style="{fontSize: '15px', fontWeight: '600', textAlign: 'left'}" :cell-style="{fontSize: '14px', textAlign: 'left'}">
            <el-table-column prop="courseName" label="课程名称" min-width="250">
              <template slot-scope="scope">
                <span style="padding-left: 10px;">{{ scope.row.courseName }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="language" label="语言" width="150">
              <template slot-scope="scope">
                <el-tag :type="getLanguageType(scope.row.language)" size="medium">
                  {{ getLanguageLabel(scope.row.language) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="enrollCount" label="学习人数" width="150">
              <template slot-scope="scope">
                <el-tag type="success" size="medium">
                  <i class="el-icon-user-solid"></i> {{ scope.row.enrollCount }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="table-row">
      <el-col :xs="24">
        <el-card class="recent-card">
          <div slot="header">
            <span>💬 最新社区帖子</span>
            <el-button style="float: right; padding: 3px 10px" type="text" @click="$router.push('/edu/community')">查看更多</el-button>
          </div>
          <el-table :data="recentPosts" style="width: 100%" :header-cell-style="{fontSize: '15px', fontWeight: '600', textAlign: 'left'}" :cell-style="{fontSize: '14px', textAlign: 'left'}" v-loading="postsLoading">
            <el-table-column prop="title" label="标题" min-width="250">
              <template slot-scope="scope">
                <span style="padding-left: 10px;">{{ scope.row.title }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="authorName" label="作者" width="150"></el-table-column>
            <el-table-column prop="likeCount" label="点赞" width="120">
              <template slot-scope="scope">
                <el-tag type="danger" size="medium">
                  <i class="el-icon-thumb"></i> {{ scope.row.likeCount }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="时间" width="180"></el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="info-row">
      <el-col :xs="24" :lg="24">
        <el-card class="info-card">
          <div slot="header">
            <span>📖 平台使用指南</span>
          </div>
          <el-collapse accordion>
            <el-collapse-item title="🎯 快速开始" name="1">
              <ol>
                <li>在「课程管理」中添加您的第一门课程</li>
                <li>在「用户管理」中查看和管理注册用户</li>
                <li>在「社区管理」中审核和管理社区帖子</li>
                <li>在「成就管理」中设置学习成就和徽章</li>
              </ol>
            </el-collapse-item>
            <el-collapse-item title="📚 课程管理" name="2">
              <ol>
                <li>支持创建英语、日语、汉语三种语言课程</li>
                <li>可以为课程添加多个单元和课时</li>
                <li>支持设置课程难度等级</li>
                <li>可以管理课程评论和评分</li>
              </ol>
            </el-collapse-item>
            <el-collapse-item title="👥 用户学习追踪" name="3">
              <ol>
                <li>实时查看用户学习进度</li>
                <li>追踪用户学习时长和完成度</li>
                <li>查看用户成就和积分</li>
                <li>分析用户学习数据趋势</li>
              </ol>
            </el-collapse-item>
          </el-collapse>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getFeaturedCourses } from '@/api/edu/course'
import { listPost } from '@/api/edu/post'

export default {
  name: 'Dashboard',
  data() {
    return {
      stats: {
        totalUsers: 0,
        totalCourses: 0,
        totalPosts: 0,
        totalAchievements: 0
      },
      recentUsers: [],
      popularCourses: [],
      recentPosts: [],
      postsLoading: false
    }
  },
  mounted() {
    this.loadStats()
    this.loadPopularCourses()
    this.loadRecentPosts()
    this.loadCharts()
  },
  methods: {
    async loadStats() {
      this.stats = {
        totalUsers: 156,
        totalCourses: 45,
        totalPosts: 328,
        totalAchievements: 28
      }
    },

    async loadPopularCourses() {
      try {
        const res = await getFeaturedCourses(5)
        this.popularCourses = res.data.map(course => ({
          courseName: course.courseName,
          language: course.language,
          enrollCount: course.totalStudents || 0
        }))
      } catch (e) {
        this.popularCourses = []
      }
    },

    async loadRecentPosts() {
      this.postsLoading = true
      try {
        const res = await listPost({ pageNum: 1, pageSize: 5, orderByColumn: 'createTime', isAsc: 'desc' })
        if (res && res.rows) {
          this.recentPosts = res.rows.map(post => ({
            title: post.title,
            authorName: post.userName || post.username || '未知用户',
            likeCount: post.likeCount || 0,
            createTime: post.createTime || ''
          }))
        }
      } catch (error) {
        console.error('加载社区帖子失败:', error)
        this.recentPosts = []
      } finally {
        this.postsLoading = false
      }
    },

    loadCharts() {
      this.initTrendChart()
      this.initLanguageChart()
    },

    initTrendChart() {
      const chartDom = this.$refs.trendChart
      if (!chartDom) return

      const myChart = echarts.init(chartDom)
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['新增用户', '学习人次', '发帖数']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '新增用户',
            type: 'line',
            stack: 'Total',
            data: [120, 132, 101, 134, 90, 230, 210],
            smooth: true,
            areaStyle: {
              color: {
                type: 'linear',
                x: 0, y: 0, x2: 0, y2: 1,
                colorStops: [
                  { offset: 0, color: 'rgba(64, 158, 255, 0.5)' },
                  { offset: 1, color: 'rgba(64, 158, 255, 0.1)' }
                ]
              }
            }
          },
          {
            name: '学习人次',
            type: 'line',
            stack: 'Total',
            data: [220, 182, 191, 234, 290, 330, 310],
            smooth: true,
            areaStyle: {
              color: {
                type: 'linear',
                x: 0, y: 0, x2: 0, y2: 1,
                colorStops: [
                  { offset: 0, color: 'rgba(103, 194, 58, 0.5)' },
                  { offset: 1, color: 'rgba(103, 194, 58, 0.1)' }
                ]
              }
            }
          },
          {
            name: '发帖数',
            type: 'line',
            stack: 'Total',
            data: [150, 232, 201, 154, 190, 330, 410],
            smooth: true,
            areaStyle: {
              color: {
                type: 'linear',
                x: 0, y: 0, x2: 0, y2: 1,
                colorStops: [
                  { offset: 0, color: 'rgba(230, 162, 60, 0.5)' },
                  { offset: 1, color: 'rgba(230, 162, 60, 0.1)' }
                ]
              }
            }
          }
        ]
      }
      myChart.setOption(option)
      window.addEventListener('resize', () => myChart.resize())
    },

    initLanguageChart() {
      const chartDom = this.$refs.languageChart
      if (!chartDom) return

      const myChart = echarts.init(chartDom)
      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left',
          data: ['英语', '日语', '汉语']
        },
        series: [
          {
            name: '课程语言分布',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 20,
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: [
              { value: 18, name: '英语', itemStyle: { color: '#409EFF' } },
              { value: 15, name: '日语', itemStyle: { color: '#67C23A' } },
              { value: 12, name: '汉语', itemStyle: { color: '#E6A23C' } }
            ]
          }
        ]
      }
      myChart.setOption(option)
      window.addEventListener('resize', () => myChart.resize())
    },

    getLanguageType(language) {
      const types = {
        'en': 'primary',
        'ja': 'success',
        'zh': 'warning',
        '英语': 'primary',
        '日语': 'success',
        '汉语': 'warning'
      }
      return types[language] || 'info'
    },
    getLanguageLabel(language) {
      const labels = {
        'en': '英语',
        'ja': '日语',
        'zh': '汉语'
      }
      return labels[language] || language
    }
  }
}
</script>

<style scoped lang="scss">
.dashboard {
  padding: 20px;
  background-color: #f0f2f5;
}

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  display: flex;
  align-items: center;
  padding: 20px;
  transition: all 0.3s;
  cursor: pointer;
  border-radius: 8px;

  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 8px 20px rgba(0, 0, 0, 0.12);
  }

  .stat-icon {
    font-size: 48px;
    margin-right: 20px;
    width: 60px;
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 12px;
    background-color: rgba(64, 158, 255, 0.1);
  }

  .stat-content {
    flex: 1;

    .stat-value {
      font-size: 32px;
      font-weight: bold;
      color: #303133;
      margin-bottom: 5px;
    }

    .stat-label {
      font-size: 14px;
      color: #909399;
    }
  }
}

.stat-users .stat-icon {
  color: #409EFF;
  background-color: rgba(64, 158, 255, 0.1);
}

.stat-courses .stat-icon {
  color: #67C23A;
  background-color: rgba(103, 194, 58, 0.1);
}

.stat-posts .stat-icon {
  color: #E6A23C;
  background-color: rgba(230, 162, 60, 0.1);
}

.stat-achievements .stat-icon {
  color: #F56C6C;
  background-color: rgba(245, 108, 108, 0.1);
}

.chart-row {
  margin-bottom: 20px;
}

.chart-card {
  border-radius: 8px;
  
  .el-card__header {
    font-size: 16px;
    font-weight: bold;
    background-color: #fafafa;
    border-bottom: 1px solid #ebeef5;
  }
}

.table-row {
  margin-bottom: 20px;
}

.recent-card {
  border-radius: 8px;
  
  .el-card__header {
    font-size: 16px;
    font-weight: bold;
    display: flex;
    justify-content: space-between;
    align-items: center;
    background-color: #fafafa;
    border-bottom: 1px solid #ebeef5;
  }
  
  .el-table {
    border-radius: 4px;
    
    ::v-deep .el-table__header-wrapper th {
      background-color: #f5f7fa;
      font-weight: 600;
    }
    
    ::v-deep .el-table__row:hover {
      background-color: #f5f7fa;
    }
  }
}

.info-row {
  margin-bottom: 20px;
}

.info-card {
  border-radius: 8px;
  
  .el-card__header {
    font-size: 16px;
    font-weight: bold;
    background-color: #fafafa;
    border-bottom: 1px solid #ebeef5;
  }

  ol {
    margin: 0;
    padding-left: 20px;

    li {
      margin-bottom: 8px;
      line-height: 1.6;
    }
  }
}

.el-tag {
  margin: 0;
}
</style>
