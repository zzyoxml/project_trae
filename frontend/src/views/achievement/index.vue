<template>
  <div class="achievement-page">
    <h1>🏆 成就中心</h1>

    <!-- 成就概览 -->
    <el-card class="achievement-overview">
      <div class="overview-stats">
        <div class="stat-item">
          <div class="stat-icon">🏅</div>
          <div class="stat-info">
            <span class="stat-value">{{ stats.total }}</span>
            <span class="stat-label">全部成就</span>
          </div>
        </div>
        <div class="stat-item">
          <div class="stat-icon">✅</div>
          <div class="stat-info">
            <span class="stat-value">{{ stats.completed }}</span>
            <span class="stat-label">已完成</span>
          </div>
        </div>
        <div class="stat-item">
          <div class="stat-icon">🔄</div>
          <div class="stat-info">
            <span class="stat-value">{{ stats.inProgress }}</span>
            <span class="stat-label">进行中</span>
          </div>
        </div>
        <div class="stat-item">
          <div class="stat-icon">⭐</div>
          <div class="stat-info">
            <span class="stat-value">{{ stats.points }}</span>
            <span class="stat-label">成就积分</span>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 成就分类 -->
    <div class="achievement-categories">
      <el-tabs v-model="activeCategory" @tab-change="loadAchievements">
        <el-tab-pane label="全部" name="all"></el-tab-pane>
        <el-tab-pane label="学习成就" name="study"></el-tab-pane>
        <el-tab-pane label="连续成就" name="streak"></el-tab-pane>
        <el-tab-pane label="分数成就" name="score"></el-tab-pane>
        <el-tab-pane label="社交成就" name="social"></el-tab-pane>
      </el-tabs>

      <div class="achievements-grid" v-loading="loading">
        <el-card
          v-for="achievement in achievements"
          :key="achievement.achievementId"
          class="achievement-card"
          :class="{ completed: achievement.completed }"
        >
          <div class="achievement-header">
            <div class="achievement-icon">
              {{ achievement.completed ? '🏆' : '🔒' }}
            </div>
            <el-tag v-if="achievement.completed" type="success" size="small">已获得</el-tag>
          </div>

          <div class="achievement-body">
            <h3>{{ achievement.achievementName }}</h3>
            <p class="achievement-desc">{{ achievement.description }}</p>
            <div class="achievement-reward">
              <span>奖励：{{ achievement.reward }}</span>
            </div>
          </div>

          <div class="achievement-progress" v-if="!achievement.completed">
            <el-progress :percentage="achievement.progress" :stroke-width="8" />
            <span class="progress-text">{{ achievement.current }}/{{ achievement.target }}</span>
          </div>
        </el-card>
      </div>
    </div>

    <!-- 徽章展示 -->
    <el-card class="badges-section">
      <template #header>
        <span>我的徽章</span>
      </template>
      <div class="badges-grid">
        <div
          v-for="badge in badges"
          :key="badge.badgeId"
          class="badge-item"
          :class="{ earned: badge.earned }"
        >
          <div class="badge-icon">{{ badge.earned ? '🎖️' : '🔒' }}</div>
          <div class="badge-name">{{ badge.badgeName }}</div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAchievementList, getBadgeList } from '@/api/achievement'

const loading = ref(false)
const activeCategory = ref('all')
const stats = ref({
  total: 20,
  completed: 8,
  inProgress: 5,
  points: 1500
})

const achievements = ref([])
const badges = ref([])

onMounted(async () => {
  await loadAchievements()
  await loadBadges()
})

const loadAchievements = async () => {
  loading.value = true
  try {
    const res = await getAchievementList({ category: activeCategory.value })
    achievements.value = res.data || getMockAchievements()
  } catch (error) {
    achievements.value = getMockAchievements()
  } finally {
    loading.value = false
  }
}

const loadBadges = async () => {
  try {
    const res = await getBadgeList()
    badges.value = res.data || getMockBadges()
  } catch (error) {
    badges.value = getMockBadges()
  }
}

const getMockAchievements = () => [
  { achievementId: 1, achievementName: '初学者', description: '完成第一个课程', reward: 100, completed: true, current: 1, target: 1, progress: 100 },
  { achievementId: 2, achievementName: '单词达人', description: '学习100个单词', reward: 200, completed: true, current: 100, target: 100, progress: 100 },
  { achievementId: 3, achievementName: '连续学习7天', description: '连续学习7天不间断', reward: 300, completed: true, current: 7, target: 7, progress: 100 },
  { achievementId: 4, achievementName: '语法专家', description: '完成所有语法课程', reward: 400, completed: false, current: 8, target: 10, progress: 80 },
  { achievementId: 5, achievementName: '听力大师', description: '听力评分达到90分', reward: 500, completed: false, current: 85, target: 90, progress: 94 },
  { achievementId: 6, achievementName: '口语达人', description: '完成30次口语练习', reward: 350, completed: false, current: 20, target: 30, progress: 67 }
]

const getMockBadges = () => [
  { badgeId: 1, badgeName: '初学者', earned: true },
  { badgeId: 2, badgeName: '单词达人', earned: true },
  { badgeId: 3, badgeName: '连续达人', earned: true },
  { badgeId: 4, badgeName: '社区活跃', earned: true },
  { badgeId: 5, badgeName: '挑战王者', earned: false },
  { badgeId: 6, badgeName: '全能学习者', earned: false }
]
</script>

<style lang="scss" scoped>
.achievement-page {
  h1 {
    font-size: 32px;
    margin-bottom: 24px;
  }

  .achievement-overview {
    margin-bottom: 32px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;

    .overview-stats {
      display: grid;
      grid-template-columns: repeat(4, 1fr);
      gap: 24px;

      @media (max-width: 768px) {
        grid-template-columns: repeat(2, 1fr);
      }

      .stat-item {
        display: flex;
        align-items: center;
        gap: 12px;
        padding: 16px;
        background: rgba(255, 255, 255, 0.2);
        border-radius: 8px;

        .stat-icon {
          font-size: 36px;
        }

        .stat-info {
          display: flex;
          flex-direction: column;

          .stat-value {
            font-size: 32px;
            font-weight: bold;
          }

          .stat-label {
            font-size: 14px;
            opacity: 0.9;
          }
        }
      }
    }
  }

  .achievement-categories {
    margin-bottom: 32px;

    .achievements-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
      gap: 16px;
      margin-top: 16px;

      .achievement-card {
        position: relative;
        transition: transform 0.3s;

        &:hover {
          transform: translateY(-5px);
        }

        &.completed {
          border: 2px solid #67c23a;
        }

        .achievement-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 12px;

          .achievement-icon {
            font-size: 48px;
          }
        }

        .achievement-body {
          margin-bottom: 12px;

          h3 {
            margin-bottom: 8px;
            font-size: 18px;
          }

          .achievement-desc {
            color: #909399;
            font-size: 14px;
            margin-bottom: 8px;
          }

          .achievement-reward {
            color: #e6a23c;
            font-size: 14px;
          }
        }

        .achievement-progress {
          .progress-text {
            display: block;
            text-align: center;
            margin-top: 8px;
            font-size: 12px;
            color: #909399;
          }
        }
      }
    }
  }

  .badges-section {
    .badges-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
      gap: 16px;

      .badge-item {
        text-align: center;
        padding: 16px;
        border-radius: 8px;
        background: var(--el-bg-color-page, #f5f7fa);
        transition: all 0.3s;

        &:hover {
          background: #ecf5ff;
        }

        &.earned {
          background: linear-gradient(135deg, #fdfbfb 0%, #ebedee 100%);
        }

        .badge-icon {
          font-size: 36px;
          margin-bottom: 8px;
        }

        .badge-name {
          font-size: 14px;
          color: #606266;
        }
      }
    }
  }
}
</style>
