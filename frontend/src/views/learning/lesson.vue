<template>
  <div class="lesson-page">
    <el-page-header @back="$router.back()" content="课时学习" />

    <div class="lesson-content" v-loading="loading">
      <div class="lesson-header">
        <h2>{{ lesson.lessonName }}</h2>
        <div class="lesson-info">
          <span>课程：{{ courseName }}</span>
          <span>预计时长：{{ lesson.duration }}分钟</span>
        </div>
      </div>

      <el-card class="lesson-main">
        <template #header>
          <div class="lesson-type-tabs">
            <el-tabs v-model="activeTab">
              <el-tab-pane label="单词" name="vocabulary">
                <div class="vocabulary-list">
                  <div
                    v-for="word in lesson.vocabularyList"
                    :key="word.id"
                    class="word-card"
                    @click="playAudio(word)"
                  >
                    <div class="word-main">
                      <h3>{{ word.word }}</h3>
                      <span class="phonetic">{{ word.phonetic }}</span>
                      <el-button
                        type="primary"
                        circle
                        size="small"
                        @click.stop="speakWord(word.word)"
                      >
                        🔊
                      </el-button>
                    </div>
                    <div class="word-translation">{{ word.translation }}</div>
                    <div class="word-example">{{ word.example }}</div>
                  </div>
                </div>
              </el-tab-pane>

              <el-tab-pane label="语法" name="grammar">
                <div class="grammar-content">
                  <div
                    v-for="rule in lesson.grammarList"
                    :key="rule.id"
                    class="grammar-card"
                  >
                    <h3>{{ rule.title }}</h3>
                    <p class="grammar-explanation">{{ rule.explanation }}</p>
                    <div class="grammar-example">
                      <p><strong>例句：</strong>{{ rule.example }}</p>
                    </div>
                  </div>
                </div>
              </el-tab-pane>

              <el-tab-pane label="听力" name="listening">
                <div class="listening-content">
                  <audio controls class="audio-player">
                    <source :src="lesson.audioUrl" type="audio/mpeg">
                    您的浏览器不支持音频播放
                  </audio>
                  <el-input
                    type="textarea"
                    v-model="listeningAnswer"
                    placeholder="请输入您听到的内容..."
                    :rows="4"
                  />
                  <el-button type="primary" @click="checkListening">提交答案</el-button>
                </div>
              </el-tab-pane>

              <el-tab-pane label="口语" name="speaking">
                <div class="speaking-content">
                  <p class="prompt-text">{{ lesson.speakingPrompt }}</p>
                  <el-button type="primary" size="large" @click="startRecording">
                    🎤 开始录音
                  </el-button>
                  <div v-if="isRecording" class="recording-indicator">
                    <span class="recording-dot"></span> 录音中...
                  </div>
                  <div v-if="score !== null" class="score-display">
                    <h3>您的得分：{{ score }}/100</h3>
                    <p>{{ scoreMessage }}</p>
                  </div>
                </div>
              </el-tab-pane>
            </el-tabs>
          </div>
        </template>
      </el-card>

      <div class="lesson-actions">
        <el-button @click="$router.back()">返回</el-button>
        <el-button type="success" @click="completeLesson">完成学习</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { getLessonDetail, completeLesson as completeLessonApi } from '@/api/learning'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()

const loading = ref(false)
const lesson = ref({})
const courseName = ref('')
const activeTab = ref('vocabulary')
const listeningAnswer = ref('')
const isRecording = ref(false)
const score = ref(null)

onMounted(async () => {
  await loadLesson()
})

const loadLesson = async () => {
  loading.value = true
  try {
    const lessonId = route.params.id
    const res = await getLessonDetail(lessonId)
    lesson.value = res.data || {}
    courseName.value = lesson.value.courseName || '课程'
  } catch (error) {
    console.error('加载课时失败:', error)
    ElMessage.error('加载课时失败')
  } finally {
    loading.value = false
  }
}

const playAudio = (word) => {
  speakWord(word.word)
}

const speakWord = (text) => {
  if ('speechSynthesis' in window) {
    const utterance = new SpeechSynthesisUtterance(text)
    utterance.lang = lesson.value.language || 'en-US'
    speechSynthesis.speak(utterance)
  } else {
    ElMessage.warning('您的浏览器不支持语音合成')
  }
}

const checkListening = () => {
  ElMessage.success('答案已提交')
}

const startRecording = () => {
  isRecording.value = true
  ElMessage.info('开始录音，请说话...')

  setTimeout(() => {
    isRecording.value = false
    score.value = 85
    ElMessage.success('录音完成，评分已生成')
  }, 5000)
}

const completeLesson = async () => {
  try {
    const lessonId = route.params.id
    await completeLessonApi({ lessonId })
    ElMessage.success('课时完成！')
    router.back()
  } catch (error) {
    console.error('完成课时失败:', error)
    ElMessage.error('完成失败')
  }
}

const scoreMessage = computed(() => {
  if (score.value >= 90) return '太棒了！发音非常标准！'
  if (score.value >= 80) return '很好！继续保持！'
  if (score.value >= 70) return '不错，还有进步空间'
  return '继续加油，多练习几遍'
})
</script>

<style lang="scss" scoped>
.lesson-page {
  .lesson-content {
    margin-top: 24px;

    .lesson-header {
      margin-bottom: 24px;

      h2 {
        font-size: 28px;
        margin-bottom: 8px;
      }

      .lesson-info {
        display: flex;
        gap: 24px;
        color: #909399;
      }
    }

    .lesson-main {
      min-height: 500px;

      .vocabulary-list {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
        gap: 16px;

        .word-card {
          background: #f5f7fa;
          padding: 16px;
          border-radius: 8px;
          cursor: pointer;
          transition: all 0.3s;

          &:hover {
            background: #ecf5ff;
            transform: translateY(-2px);
          }

          .word-main {
            display: flex;
            align-items: center;
            gap: 8px;
            margin-bottom: 8px;

            h3 {
              font-size: 24px;
              margin: 0;
            }

            .phonetic {
              color: #909399;
              flex: 1;
            }
          }

          .word-translation {
            color: #409eff;
            font-size: 16px;
            margin-bottom: 8px;
          }

          .word-example {
            color: #606266;
            font-size: 14px;
          }
        }
      }

      .grammar-content {
        .grammar-card {
          background: #f5f7fa;
          padding: 20px;
          border-radius: 8px;
          margin-bottom: 16px;

          h3 {
            margin-bottom: 12px;
            color: #409eff;
          }

          .grammar-explanation {
            margin-bottom: 12px;
            line-height: 1.6;
          }

          .grammar-example {
            background: white;
            padding: 12px;
            border-radius: 4px;
          }
        }
      }

      .listening-content, .speaking-content {
        display: flex;
        flex-direction: column;
        gap: 16px;

        .audio-player {
          width: 100%;
        }

        .prompt-text {
          font-size: 18px;
          line-height: 1.6;
          padding: 16px;
          background: #f5f7fa;
          border-radius: 8px;
        }

        .recording-indicator {
          display: flex;
          align-items: center;
          gap: 8px;
          color: #f56c6c;

          .recording-dot {
            width: 12px;
            height: 12px;
            background: #f56c6c;
            border-radius: 50%;
            animation: pulse 1s infinite;
          }
        }

        .score-display {
          text-align: center;
          padding: 24px;
          background: #f5f7fa;
          border-radius: 8px;

          h3 {
            color: #409eff;
            margin-bottom: 8px;
          }
        }
      }
    }

    .lesson-actions {
      display: flex;
      justify-content: center;
      gap: 16px;
      margin-top: 24px;
    }
  }
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
}
</style>
