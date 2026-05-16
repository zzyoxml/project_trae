<template>
  <div class="challenge-page">
    <h1>🏆 闯关天梯</h1>

    <!-- 挑战模式选择 -->
    <div class="mode-tabs">
      <el-radio-group v-model="currentMode" @change="handleModeChange">
        <el-radio-button label="total">总榜</el-radio-button>
        <el-radio-button label="daily">日榜</el-radio-button>
      </el-radio-group>
    </div>

    <div class="challenge-content">
      <!-- 排行榜 -->
      <el-card class="leaderboard-card">
        <template #header>
          <div class="card-header">
            <span>{{ currentModeText }}</span>
          </div>
        </template>

        <div class="leaderboard" v-loading="loading">
          <div class="top-three">
            <div 
              v-for="(user, index) in topThree" 
              :key="index"
              class="top-item"
              :class="'rank-' + (index + 1)"
            >
              <div class="rank-badge">
                <span v-if="index === 0">🥇</span>
                <span v-else-if="index === 1">🥈</span>
                <span v-else>🥉</span>
              </div>
              <el-avatar :size="64" :src="user.avatar">
                {{ user.username?.charAt(0) || 'U' }}
              </el-avatar>
              <div class="user-info">
                <div class="username">{{ user.username }}</div>
                <div class="level-badge">
                  <el-tag size="small" :type="getLevelType(user.level)">
                    {{ user.levelName || 'Lv.' + user.level }}
                  </el-tag>
                </div>
              </div>
              <div class="score">
                <span class="score-value">{{ user.score }}</span>
                <span class="score-label">积分</span>
              </div>
            </div>
          </div>

          <el-divider />

          <div class="rank-list">
            <div 
              v-for="(user, index) in rankList" 
              :key="index"
              class="rank-item"
              :class="{ 'is-me': user.isMe }"
            >
              <div class="rank-number">
                <span class="rank">{{ index + 4 }}</span>
              </div>
              <el-avatar :size="40" :src="user.avatar">
                {{ user.username?.charAt(0) || 'U' }}
              </el-avatar>
              <div class="user-info">
                <div class="username">
                  {{ user.username }}
                  <el-tag v-if="user.isMe" size="small" type="primary">我</el-tag>
                </div>
                <div class="meta">
                  <span class="level">{{ user.levelName || 'Lv.' + user.level }}</span>
                  <span class="achievements">🏆 {{ user.achievements }}</span>
                </div>
              </div>
              <div class="stats">
                <div class="stat">
                  <span class="value">{{ user.score }}</span>
                  <span class="label">积分</span>
                </div>
                <div class="stat">
                  <span class="value">{{ user.totalMinutes }}</span>
                  <span class="label">分钟</span>
                </div>
              </div>
            </div>

            <el-empty v-if="rankList.length === 0 && !loading" description="暂无排名数据" />
          </div>
        </div>
      </el-card>

      <!-- 我的排名 -->
      <el-card class="my-rank-card">
        <template #header>
          <div class="card-header">
            <span>我的排名</span>
          </div>
        </template>

        <div class="my-rank-info" v-if="myRank">
          <div class="rank-display">
            <span class="rank-number">{{ myRank.rank }}</span>
            <span class="rank-suffix">名</span>
          </div>
          <div class="rank-details">
            <div class="detail-item">
              <span class="label">当前积分</span>
              <span class="value">{{ myRank.score }}</span>
            </div>
            <div class="detail-item">
              <span class="label">本周排名</span>
              <span class="value">{{ myRank.weeklyRank || '-' }}</span>
            </div>
            <div class="detail-item">
              <span class="label">连续学习</span>
              <span class="value">{{ myRank.streakDays || 0 }} 天</span>
            </div>
          </div>
        </div>

        <el-empty v-else description="暂无排名信息" />
      </el-card>

    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getLeaderboard, getMyRank } from '@/api/achievement'

const loading = ref(false)
const currentMode = ref('total')
const leaderboard = ref([])
const myRank = ref(null)

const topThree = computed(() => leaderboard.value.slice(0, 3))
const rankList = computed(() => leaderboard.value.slice(3))

const currentModeText = computed(() => {
  const modes = {
    total: '按总积分排名',
    daily: '按今日积分排名'
  }
  return modes[currentMode.value] || ''
})

onMounted(async () => {
  await loadLeaderboard()
  await loadMyRank()
})

const loadLeaderboard = async () => {
  loading.value = true
  try {
    const res = await getLeaderboard(currentMode.value)
    if (res?.rows) {
      leaderboard.value = res.rows.map((user, index) => ({
        ...user,
        isMe: index === 5
      }))
    } else if (Array.isArray(res)) {
      leaderboard.value = res.map((user, index) => ({
        ...user,
        isMe: index === 5
      }))
    }
  } catch (error) {
    console.error('加载排行榜失败:', error)
  } finally {
    loading.value = false
  }
}

const loadMyRank = async () => {
  try {
    const res = await getMyRank(currentMode.value)
    if (res && res.rank !== undefined) {
      myRank.value = res
    }
  } catch (error) {
    console.error('加载我的排名失败:', error)
  }
}

const handleModeChange = () => {
  loadLeaderboard()
  loadMyRank()
}

const getLevelType = (level) => {
  if (level >= 10) return 'danger'
  if (level >= 5) return 'warning'
  return 'success'
}
</script>

<style lang="scss" scoped>
.challenge-page {
  h1 {
    font-size: 32px;
    margin-bottom: 24px;
  }

  .mode-tabs {
    margin-bottom: 24px;
  }

  .challenge-content {
    display: grid;
    grid-template-columns: 1fr 300px;
    gap: 20px;

    @media (max-width: 992px) {
      grid-template-columns: 1fr;
    }

    .leaderboard-card {
      .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
      }

      .leaderboard {
        .top-three {
          display: flex;
          justify-content: center;
          gap: 24px;
          padding: 20px 0;

          @media (max-width: 768px) {
            flex-direction: column;
            align-items: center;
          }

          .top-item {
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: 12px;
            padding: 20px;
            border-radius: 12px;
            background: var(--el-bg-color-page, #f5f7fa);
            min-width: 150px;

            &.rank-1 {
              background: linear-gradient(135deg, #fff9c4 0%, #fff176 100%);
              order: 2;
            }

            &.rank-2 {
              background: linear-gradient(135deg, #f5f5f5 0%, #e0e0e0 100%);
              order: 1;
            }

            &.rank-3 {
              background: linear-gradient(135deg, #ffe0b2 0%, #ffcc80 100%);
              order: 3;
            }

            .rank-badge {
              font-size: 32px;
            }

            .user-info {
              text-align: center;

              .username {
                font-size: 16px;
                font-weight: bold;
                margin-bottom: 8px;
              }
            }

            .score {
              text-align: center;

              .score-value {
                display: block;
                font-size: 24px;
                font-weight: bold;
                color: #409eff;
              }

              .score-label {
                font-size: 12px;
                color: #909399;
              }
            }
          }
        }

        .rank-list {
          .rank-item {
            display: flex;
            align-items: center;
            gap: 12px;
            padding: 12px;
            border-radius: 8px;
            margin-bottom: 8px;
            transition: background 0.3s;

            &:last-child {
              margin-bottom: 0;
            }

            &:hover {
              background: var(--el-bg-color-page, #f5f7fa);
            }

            &.is-me {
              background: #ecf5ff;
              border: 1px solid #409eff;
            }

            .rank-number {
              width: 32px;
              text-align: center;

              .rank {
                font-size: 18px;
                font-weight: bold;
                color: #909399;
              }
            }

            .user-info {
              flex: 1;

              .username {
                display: flex;
                align-items: center;
                gap: 8px;
                font-weight: 500;
                margin-bottom: 4px;
              }

              .meta {
                display: flex;
                gap: 12px;
                font-size: 12px;
                color: #909399;
              }
            }

            .stats {
              display: flex;
              gap: 16px;

              .stat {
                text-align: center;
                min-width: 60px;

                .value {
                  display: block;
                  font-size: 16px;
                  font-weight: bold;
                  color: #303133;
                }

                .label {
                  font-size: 12px;
                  color: #909399;
                }
              }
            }
          }
        }
      }
    }

    .my-rank-card {
      .my-rank-info {
        .rank-display {
          text-align: center;
          margin-bottom: 24px;

          .rank-number {
            font-size: 64px;
            font-weight: bold;
            color: #409eff;
          }

          .rank-suffix {
            font-size: 24px;
            color: #909399;
          }
        }

        .rank-details {
          .detail-item {
            display: flex;
            justify-content: space-between;
            padding: 12px 0;
            border-bottom: 1px solid #ebeef5;

            &:last-child {
              border-bottom: none;
            }

            .label {
              color: #909399;
            }

            .value {
              font-weight: bold;
              color: #303133;
            }
          }
        }
      }
    }
  }
}
</style>
