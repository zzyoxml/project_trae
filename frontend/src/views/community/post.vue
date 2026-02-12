<template>
  <div class="post-detail-page">
    <el-page-header @back="$router.back()" content="帖子详情" />

    <div class="post-content" v-loading="loading">
      <el-card v-if="post">
        <div class="post-header">
          <div class="user-info">
            <el-avatar :size="48">{{ post.username?.charAt(0) }}</el-avatar>
            <div class="user-details">
              <span class="username">{{ post.username }}</span>
              <span class="post-time">{{ post.createTime }}</span>
            </div>
          </div>
          <el-tag :type="getTypeTagType(post.postType)">
            {{ getTypeText(post.postType) }}
          </el-tag>
        </div>

        <div class="post-body">
          <h1>{{ post.title }}</h1>
          <div class="post-text">{{ post.content }}</div>
        </div>

        <div class="post-footer">
          <div class="post-actions">
            <el-button @click="toggleLike" :type="post.liked ? 'primary' : ''">
              <el-icon><Star /></el-icon> {{ post.likeCount }} 点赞
            </el-button>
            <el-button @click="showCommentInput = !showCommentInput">
              <el-icon><ChatDotRound /></el-icon> {{ post.commentCount }} 评论
            </el-button>
          </div>
        </div>
      </el-card>

      <!-- 评论区 -->
      <el-card class="comments-section">
        <template #header>
          <span>评论 ({{ comments.length }})</span>
        </template>

        <!-- 评论输入框 -->
        <div class="comment-input" v-if="showCommentInput">
          <el-input
            v-model="newComment"
            type="textarea"
            placeholder="写下您的评论..."
            :rows="3"
          />
          <el-button type="primary" @click="submitComment" style="margin-top: 12px">
            发表评论
          </el-button>
        </div>

        <!-- 评论列表 -->
        <div class="comments-list">
          <div v-for="comment in comments" :key="comment.commentId" class="comment-item">
            <div class="comment-header">
              <div class="user-info">
                <el-avatar :size="32">{{ comment.username?.charAt(0) }}</el-avatar>
                <div class="user-details">
                  <span class="username">{{ comment.username }}</span>
                  <span class="comment-time">{{ comment.createTime }}</span>
                </div>
              </div>
            </div>
            <div class="comment-body">
              <p>{{ comment.content }}</p>
            </div>
          </div>
        </div>

        <el-empty v-if="comments.length === 0" description="暂无评论" />
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getPostDetail, getPostComments, addComment } from '@/api/community'
import { ElMessage } from 'element-plus'

const route = useRoute()

const loading = ref(false)
const post = ref(null)
const comments = ref([])
const newComment = ref('')
const showCommentInput = ref(false)

onMounted(async () => {
  await loadPost()
  await loadComments()
})

const loadPost = async () => {
  loading.value = true
  try {
    const postId = route.params.id
    console.log('Loading post with id:', postId)
    const res = await getPostDetail(postId)
    console.log('Post data received:', res)
    post.value = res
    console.log('Post after assignment:', post.value)
    console.log('Post postId:', post.value?.postId)
  } catch (error) {
    console.error('加载帖子失败:', error)
    ElMessage.error('加载帖子失败')
  } finally {
    loading.value = false
  }
}

const loadComments = async () => {
  try {
    const postId = route.params.id
    const res = await getPostComments(postId)
    comments.value = res || []
  } catch (error) {
    console.error('加载评论失败:', error)
  }
}

const submitComment = async () => {
  if (!newComment.value.trim()) {
    ElMessage.warning('请输入评论内容')
    return
  }

  if (!post.value || !post.value.postId) {
    console.log('Post object:', post.value)
    console.log('Post postId value:', post.value?.postId)
    ElMessage.error('帖子信息加载失败，请刷新重试')
    return
  }

  try {
    console.log('Submitting comment with postId:', post.value.postId)
    const commentData = {
      postId: post.value.postId,
      content: newComment.value
    }
    console.log('Comment data:', commentData)
    await addComment({
      postId: post.value.postId,
      content: newComment.value
    })
    ElMessage.success('评论成功')
    newComment.value = ''
    await loadComments()
  } catch (error) {
    console.error('评论失败:', error)
    ElMessage.error('评论失败')
  }
}

const toggleLike = () => {
  post.value.liked = !post.value.liked
  post.value.likeCount += post.value.liked ? 1 : -1
  ElMessage.success(post.value.liked ? '点赞成功' : '取消点赞')
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
.post-detail-page {
  .post-content {
    margin-top: 24px;

    .post-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;

      .user-info {
        display: flex;
        gap: 12px;

        .user-details {
          display: flex;
          flex-direction: column;
          justify-content: center;

          .username {
            font-weight: bold;
            font-size: 16px;
          }

          .post-time {
            font-size: 12px;
            color: #909399;
          }
        }
      }
    }

    .post-body {
      margin-bottom: 20px;

      h1 {
        font-size: 28px;
        margin-bottom: 16px;
      }

      .post-text {
        font-size: 16px;
        line-height: 1.8;
        color: #303133;
      }
    }

    .post-footer {
      .post-actions {
        display: flex;
        gap: 16px;
      }
    }

    .comments-section {
      margin-top: 24px;

      .comment-input {
        margin-bottom: 24px;
        padding-bottom: 16px;
        border-bottom: 1px solid #ebeef5;
      }

      .comments-list {
        .comment-item {
          padding: 16px 0;
          border-bottom: 1px solid #ebeef5;

          &:last-child {
            border-bottom: none;
          }

          .comment-header {
            margin-bottom: 8px;

            .user-info {
              display: flex;
              gap: 12px;

              .user-details {
                display: flex;
                flex-direction: column;
                justify-content: center;

                .username {
                  font-weight: bold;
                  font-size: 14px;
                }

                .comment-time {
                  font-size: 12px;
                  color: #909399;
                }
              }
            }
          }

          .comment-body {
            p {
              margin: 0;
              color: #606266;
              line-height: 1.6;
            }
          }
        }
      }
    }
  }
}
</style>
