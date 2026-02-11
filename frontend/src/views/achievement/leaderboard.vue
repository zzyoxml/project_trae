<template>
  <div class="leaderboard-page">
    <h1>📊 排行榜</h1>

    <!-- 排行榜类型 -->
    <el-tabs v-model="activeType" @tab-change="loadLeaderboard">
      <el-tab-pane label="总排行榜" name="total"></el-tab-pane>
      <el-tab-pane label="周排行榜" name="weekly"></el-tab-pane>
      <el-tab-pane label="日排行榜" name="daily"></el-tab-pane>
    </el-tabs>

    <!-- 前三名展示 -->
    <div class="top-three" v-if="leaderboard.length >= 3">
      <div class="rank-card second">
        <div class="avatar-container">
          <el-avatar :size="80">{{ leaderboard[1]?.username?.charAt(0) }}</el-avatar>
        </div>
        <div class="rank-badge">🥈</div>
        <h3>{{ leaderboard[1]?.username }}</h3>
        <p>{{ leaderboard[1]?.score }} 积分</p>
      </div>

      <div class="rank-card first">
        <div class="avatar-container">
          <el-avatar :size="100">{{ leaderboard[0]?.username?.charAt(0) }}</el-avatar>
        </div>
        <div class="rank-badge">🥇</div>
        <h3>{{ leaderboard[0]?.username }}</h3>
        <p>{{ leaderboard[0]?.score }} 积分</p>
      </div>

      <div class="rank-card third">
        <div class="avatar-container">
          <el-avatar :size="80">{{ leaderboard[2]?.username?.charAt(0) }}</el-avatar>
        </div>
        <div class="rank-badge">🥉</div>
        <h3>{{ leaderboard[2]?.username }}</h3>
        <p>{{ leaderboard[2]?.score }} 积分</p>
      </div>
    </div>

    <!-- 排行榜列表 -->
    <el-card class="leaderboard-list">
      <el-table :data="leaderboard.slice(3)" v-loading="loading" stripe>
        <el-table-column prop="rank" label="排名" width="80">
          <template #default="{ row }">
            <span class="rank-number">{{ row.rank }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="username" label="用户">
          <template #default="{ row }">
            <div class="user-info">
              <el-avatar :size="32">{{ row.username?.charAt(0) }}</el-avatar>
              <span>{{ row.username }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="totalMinutes" label="学习时长" width="120">
          <template #default="{ row }">
            {{ row.totalMinutes || 0 }} 分钟
          </template>
        </el-table-column>
        <el-table-column prop="completedCourses" label="完成课程" width="100" />
        <el-table-column prop="achievements" label="成就数" width="80" />
        <el-table-column prop="score" label="积分" sortable />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getLeaderboard } from '@/api/achievement'

const loading = ref(false)
const activeType = ref('total')
const leaderboard = ref([])

onMounted(() => {
  loadLeaderboard()
})

const loadLeaderboard = async () => {
  loading.value = true
  try {
    const res = await getLeaderboard({ type: activeType.value })
    leaderboard.value = res.data || getMockLeaderboard()
  } catch (error) {
    leaderboard.value = getMockLeaderboard()
  } finally {
    loading.value = false
  }
}

const getMockLeaderboard = () => [
  { rank: 1, username: 'LanguageMaster', score: 5800, totalMinutes: 2500, completedCourses: 15, achievements: 18 },
  { rank: 2, username: 'WordWizard', score: 5200, totalMinutes: 2200, completedCourses: 13, achievements: 15 },
  { rank: 3, username: 'GrammarGuru', score: 4800, totalMinutes: 2000, completedCourses: 12, achievements: 14 },
  { rank: 4, username: 'StudyChampion', score: 4200, totalMinutes: 1800, completedCourses: 10, achievements: 12 },
  { rank: 5, username: 'LearningPro', score: 3800, totalMinutes: 1600, completedCourses: 9, achievements: 11 },
  { rank: 6, username: 'VocabularyKing', score: 3200, totalMinutes: 1400, completedCourses: 8, achievements: 10 },
  { rank: 7, username: 'Polyglot2024', score: 2900, totalMinutes: 1200, completedCourses: 7, achievements: 9 },
  { rank: 8, username: 'LinguistPro', score: 2500, totalMinutes: 1000, completedCourses: 6, achievements: 8 },
  { rank: 9, username: 'BilingualStar', score: 2200, totalMinutes: 900, completedCourses: 5, achievements: 7 },
  { rank: 10, username: 'LanguageLearner', score: 1800, totalMinutes: 800, completedCourses: 4, achievements: 6 }
]
</script>

<style lang="scss" scoped>
.leaderboard-page {
  h1 {
    font-size: 32px;
    margin-bottom: 24px;
  }

  .top-three {
    display: flex;
    justify-content: center;
    align-items: flex-end;
    gap: 24px;
    margin: 32px 0;
    padding: 32px 0;

    .rank-card {
      background: var(--el-bg-color, white);
      padding: 24px;
      border-radius: 12px;
      text-align: center;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
      transition: transform 0.3s;

      &:hover {
        transform: translateY(-10px);
      }

      &.first {
        order: 2;
        transform: scale(1.1);
        border: 3px solid #ffd700;

        &:hover {
          transform: scale(1.15) translateY(-10px);
        }
      }

      &.second {
        order: 1;
        border: 2px solid #c0c0c0;
      }

      &.third {
        order: 3;
        border: 2px solid #cd7f32;
      }

      .avatar-container {
        margin-bottom: 12px;
      }

      .rank-badge {
        font-size: 36px;
        margin-bottom: 8px;
      }

      h3 {
        margin-bottom: 8px;
        font-size: 18px;
      }

      p {
        color: #909399;
        font-size: 14px;
      }
    }
  }

  .leaderboard-list {
    .rank-number {
      font-weight: bold;
      color: #606266;
    }

    .user-info {
      display: flex;
      align-items: center;
      gap: 8px;
    }
  }
}
</style>
