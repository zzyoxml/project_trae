<template>
  <div class="community-page">
    <h1>👥 学习社区</h1>

    <!-- 发布帖子 -->
    <el-card class="create-post" v-if="userStore.isLoggedIn">
      <el-input
        v-model="newPostContent"
        type="textarea"
        placeholder="分享您的学习心得..."
        :rows="3"
      />
      <div class="post-actions">
        <el-select v-model="postType" placeholder="选择类型" size="small">
          <el-option label="学习心得" value="experience" />
          <el-option label="问题求助" value="question" />
          <el-option label="资源分享" value="resource" />
          <el-option label="交流讨论" value="discussion" />
        </el-select>
        <el-button type="primary" @click="createPost">发布</el-button>
      </div>
    </el-card>

    <!-- 帖子列表 -->
    <div class="posts-section">
      <div class="posts-filters">
        <el-radio-group v-model="filterType" @change="loadPosts">
          <el-radio-button label="">全部</el-radio-button>
          <el-radio-button label="experience">学习心得</el-radio-button>
          <el-radio-button label="question">问题求助</el-radio-button>
          <el-radio-button label="resource">资源分享</el-radio-button>
        </el-radio-group>
      </div>

      <div class="posts-list" v-loading="loading">
        <el-card
          v-for="post in posts"
          :key="post.postId"
          class="post-card"
          @click="goToPost(post.postId)"
        >
          <div class="post-header">
            <div class="user-info">
              <el-avatar :size="40">{{ post.username?.charAt(0) }}</el-avatar>
              <div class="user-details">
                <span class="username">{{ post.username }}</span>
                <span class="post-time">{{ post.createTime }}</span>
              </div>
            </div>
            <el-tag size="small" :type="getTypeTagType(post.postType)">
              {{ getTypeText(post.postType) }}
            </el-tag>
          </div>

          <div class="post-content">
            <h3>{{ post.title }}</h3>
            <p>{{ post.content }}</p>
          </div>

          <div class="post-footer">
            <div class="post-stats">
              <span><el-icon><View /></el-icon> {{ post.viewCount }}</span>
              <span><el-icon><ChatDotRound /></el-icon> {{ post.commentCount }}</span>
              <span @click.stop="toggleLike(post)">
                <el-icon><Star /></el-icon> {{ post.likeCount }}
              </span>
            </div>
          </div>
        </el-card>

        <el-empty v-if="!loading && posts.length === 0" description="暂无帖子" />
      </div>

      <!-- 分页 -->
      <div class="pagination" v-if="total > 0">
        <el-pagination
          v-model:current-page="pagination.pageNum"
          v-model:page-size="pagination.pageSize"
          :total="total"
          :page-sizes="[10, 20, 30, 50]"
          layout="total, sizes, prev, pager, next"
          @size-change="loadPosts"
          @current-change="loadPosts"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { getPostList, createPost as createPostApi } from '@/api/community'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const loading = ref(false)
const filterType = ref('')
const newPostContent = ref('')
const postType = ref('experience')
const posts = ref([])
const total = ref(0)

const pagination = reactive({
  pageNum: 1,
  pageSize: 10
})

onMounted(() => {
  loadPosts()
})

const loadPosts = async () => {
  loading.value = true
  try {
    const params = {
      postType: filterType.value,
      ...pagination
    }
    const res = await getPostList(params)
    posts.value = res.data?.rows || []
    total.value = res.data?.total || 0
  } catch (error) {
    console.error('加载帖子失败:', error)
  } finally {
    loading.value = false
  }
}

const createPost = async () => {
  if (!newPostContent.value.trim()) {
    ElMessage.warning('请输入帖子内容')
    return
  }

  try {
    await createPostApi({
      title: newPostContent.value.substring(0, 50),
      content: newPostContent.value,
      postType: postType.value
    })
    ElMessage.success('发布成功')
    newPostContent.value = ''
    await loadPosts()
  } catch (error) {
    console.error('发布失败:', error)
    ElMessage.error('发布失败')
  }
}

const goToPost = (postId) => {
  router.push(`/community/post/${postId}`)
}

const toggleLike = (post) => {
  post.liked = !post.liked
  post.likeCount += post.liked ? 1 : -1
}

const getTypeText = (type) => {
  const types = {
    experience: '学习心得',
    question: '问题求助',
    resource: '资源分享',
    discussion: '交流讨论'
  }
  return types[type] || type
}

const getTypeTagType = (type) => {
  const types = {
    experience: 'success',
    question: 'warning',
    resource: 'primary',
    discussion: 'info'
  }
  return types[type] || 'info'
}
</script>

<style lang="scss" scoped>
.community-page {
  h1 {
    font-size: 32px;
    margin-bottom: 24px;
  }

  .create-post {
    margin-bottom: 24px;

    .post-actions {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-top: 12px;
    }
  }

  .posts-section {
    .posts-filters {
      margin-bottom: 16px;
    }

    .posts-list {
      .post-card {
        margin-bottom: 16px;
        cursor: pointer;
        transition: transform 0.3s;

        &:hover {
          transform: translateX(5px);
        }

        .post-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 12px;

          .user-info {
            display: flex;
            gap: 12px;

            .user-details {
              display: flex;
              flex-direction: column;

              .username {
                font-weight: bold;
              }

              .post-time {
                font-size: 12px;
                color: #909399;
              }
            }
          }
        }

        .post-content {
          margin-bottom: 12px;

          h3 {
            margin-bottom: 8px;
            font-size: 18px;
          }

          p {
            color: #606266;
            line-height: 1.6;
          }
        }

        .post-footer {
          .post-stats {
            display: flex;
            gap: 20px;
            color: #909399;

            span {
              display: flex;
              align-items: center;
              gap: 4px;
              cursor: pointer;

              &:hover {
                color: #409eff;
              }
            }
          }
        }
      }
    }

    .pagination {
      display: flex;
      justify-content: center;
      margin-top: 24px;
    }
  }
}
</style>
