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
      <el-col :xs="24" :lg="16">
        <el-card class="chart-card">
          <div slot="header">
            <span>📊 学习数据趋势</span>
          </div>
          <div ref="trendChart" style="width: 100%; height: 350px;"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="8">
        <el-card class="chart-card">
          <div slot="header">
            <span>🌐 课程语言分布</span>
          </div>
          <div ref="languageChart" style="width: 100%; height: 350px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="table-row">
      <el-col :xs="24" :lg="12">
        <el-card class="recent-card">
          <div slot="header">
            <span>👥 最新注册用户</span>
            <el-button style="float: right; padding: 3px 10px" type="text" @click="$router.push('/system/user')">查看更多</el-button>
          </div>
          <el-table :data="recentUsers" style="width: 100%">
            <el-table-column prop="nickName" label="用户名" width="120"></el-table-column>
            <el-table-column prop="language" label="学习语言" width="100">
              <template slot-scope="scope">
                <el-tag :type="getLanguageType(scope.row.language)" size="small">
                  {{ scope.row.language }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="joinTime" label="注册时间"></el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="12">
        <el-card class="recent-card">
          <div slot="header">
            <span>📚 热门课程</span>
            <el-button style="float: right; padding: 3px 10px" type="text" @click="$router.push('/edu/course')">查看更多</el-button>
          </div>
          <el-table :data="popularCourses" style="width: 100%">
            <el-table-column prop="courseName" label="课程名称" width="150"></el-table-column>
            <el-table-column prop="language" label="语言" width="80">
              <template slot-scope="scope">
                <el-tag :type="getLanguageType(scope.row.language)" size="small">
                  {{ getLanguageLabel(scope.row.language) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="enrollCount" label="学习人数" width="100"></el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="table-row">
      <el-col :xs="24" :lg="12">
        <el-card class="recent-card">
          <div slot="header">
            <span>🏆 学习排行榜 TOP 10</span>
            <el-button style="float: right; padding: 3px 10px" type="text" @click="$router.push('/edu/achievement')">查看更多</el-button>
          </div>
          <el-table :data="topLearners" style="width: 100%">
            <el-table-column type="index" label="排名" width="60"></el-table-column>
            <el-table-column prop="nickName" label="用户名" width="120"></el-table-column>
            <el-table-column prop="totalPoints" label="积分" width="80"></el-table-column>
            <el-table-column prop="achievementCount" label="成就数" width="80"></el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="12">
        <el-card class="recent-card">
          <div slot="header">
            <span>💬 最新社区帖子</span>
            <el-button style="float: right; padding: 3px 10px" type="text" @click="$router.push('/edu/community')">查看更多</el-button>
          </div>
          <el-table :data="recentPosts" style="width: 100%">
            <el-table-column prop="title" label="标题"></el-table-column>
            <el-table-column prop="authorName" label="作者" width="100"></el-table-column>
            <el-table-column prop="likeCount" label="点赞" width="60"></el-table-column>
            <el-table-column prop="createTime" label="时间" width="120"></el-table-column>
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
      topLearners: [],
      recentPosts: []
    }
  },
  mounted() {
    this.loadStats()
    this.loadPopularCourses()
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

      this.recentUsers = [
        { nickName: '张小明', language: '英语', joinTime: '2024-01-15' },
        { nickName: '李华', language: '日语', joinTime: '2024-01-14' },
        { nickName: '小明', language: '汉语', joinTime: '2024-01-13' },
        { nickName: 'John', language: '英语', joinTime: '2024-01-12' },
        { nickName: '田中', language: '日语', joinTime: '2024-01-11' }
      ]

      this.topLearners = [
        { nickName: '学习达人', totalPoints: 8500, achievementCount: 25 },
        { nickName: '进步之星', totalPoints: 7200, achievementCount: 22 },
        { nickName: '坚持不懈', totalPoints: 6800, achievementCount: 20 },
        { nickName: '语言爱好者', totalPoints: 6500, achievementCount: 19 },
        { nickName: '每日学习', totalPoints: 6200, achievementCount: 18 },
        { nickName: '单词王', totalPoints: 5900, achievementCount: 17 },
        { nickName: '语法专家', totalPoints: 5600, achievementCount: 16 },
        { nickName: '口语达人', totalPoints: 5300, achievementCount: 15 },
        { nickName: '听力高手', totalPoints: 5000, achievementCount: 14 },
        { nickName: '阅读专家', totalPoints: 4700, achievementCount: 13 }
      ]

      this.recentPosts = [
        { title: '英语学习方法分享', authorName: '小明', likeCount: 45, createTime: '2024-01-15' },
        { title: '日语学习资源推荐', authorName: '太郎', likeCount: 38, createTime: '2024-01-14' },
        { title: '汉语拼音技巧', authorName: '小红', likeCount: 32, createTime: '2024-01-13' },
        { title: '学习打卡第二天', authorName: 'John', likeCount: 28, createTime: '2024-01-12' },
        { title: '每日一句日语', authorName: '田中', likeCount: 25, createTime: '2024-01-11' }
      ]
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

  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  }

  .stat-icon {
    font-size: 48px;
    margin-right: 20px;
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
}

.stat-courses .stat-icon {
  color: #67C23A;
}

.stat-posts .stat-icon {
  color: #E6A23C;
}

.stat-achievements .stat-icon {
  color: #F56C6C;
}

.chart-row {
  margin-bottom: 20px;
}

.chart-card {
  .el-card__header {
    font-size: 16px;
    font-weight: bold;
  }
}

.table-row {
  margin-bottom: 20px;
}

.recent-card {
  .el-card__header {
    font-size: 16px;
    font-weight: bold;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}

.info-row {
  margin-bottom: 20px;
}

.info-card {
  .el-card__header {
    font-size: 16px;
    font-weight: bold;
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
