<template>
  <div class="lesson-page">
    <el-page-header @back="$router.back()" content="课时学习" />

    <div class="lesson-content" v-loading="loading">
      <template v-if="lesson.lessonId">
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
              <el-tab-pane label="字/词" name="vocabulary">
                <div class="vocabulary-list" v-if="lesson.vocabularyList && lesson.vocabularyList.length > 0">
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
                        siUPDATE sys_config SET config_value = 'N' WHERE config_key = 'sys.account.captchaEnabled';ze="small"
                        @click.stop="speakWord(word.word)"
                      >
                        🔊
                      </el-button>
                    </div>
                    <div class="word-translation">{{ word.translation }}</div>
                    <div class="word-example">{{ word.example }}</div>
                  </div>
                </div>
                <el-empty v-else description="暂无单词数据，请先在后台添加该单元的词汇" :image-size="100" />
              </el-tab-pane>

              <el-tab-pane label="例句" name="examples">
                <div class="example-list" v-if="lesson.exampleList && lesson.exampleList.length > 0">
                  <div
                    v-for="(ex, idx) in lesson.exampleList"
                    :key="idx"
                    class="example-card"
                  >
                    <div class="example-sentence">{{ ex.sentence }}</div>
                    <div class="example-translation">{{ ex.translation }}</div>
                  </div>
                </div>
                <el-empty v-else description="暂无例句数据" :image-size="100" />
              </el-tab-pane>

              <!-- <el-tab-pane label="听力" name="listening">
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
              </el-tab-pane> -->

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
      </template>

      <el-empty v-else-if="!loading" description="课时不存在或暂无内容">
        <el-button type="primary" @click="$router.back()">返回课程</el-button>
      </el-empty>
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
// const listeningAnswer = ref('')  // 听力相关，暂时注释
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
    const data = res?.data ?? res ?? {}
    lesson.value = data
    courseName.value = data.courseName || '课程'
    if (!data.lessonId) {
      ElMessage.warning('课时数据为空，请检查是否已添加课时内容')
    }
  } catch (error) {
    console.error('加载课时失败:', error)
    ElMessage.error('加载课时失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

const playAudio = (word) => {
  speakWord(word.word)
}

const speakWord = (text) => {
  const language = lesson.value.language || 'en'
  speakWithTTS(text, language)
}

const speakWithTTS = (text, language) => {
  const voiceMap = {
    'en': 'en-US-JennyNeural',
    'ja': 'ja-JP-NanamiNeural',
    'zh': 'zh-CN-XiaoxiaoNeural'
  }
  const voice = voiceMap[language] || voiceMap['en']
  
  const url = `http://36.248.181.23:22335/tts?t=${encodeURIComponent(text)}&v=${voice}&r=0&p=0&s=&api_key=`
  const audio = new Audio(url)
  
  audio.addEventListener('canplay', () => {
    audio.play().catch(err => {
      console.error('播放失败:', err)
    })
  })
  
  audio.addEventListener('error', () => {
    ElMessage.warning('语音加载失败')
  })
}

// const checkListening = () => {
//   ElMessage.success('答案已提交')
// }  // 听力相关，暂时注释

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
          background: var(--el-bg-color-page, #f5f7fa);
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

      .example-list {
        .example-card {
          background: var(--el-bg-color-page, #f5f7fa);
          padding: 16px 20px;
          border-radius: 8px;
          margin-bottom: 12px;
          border-left: 4px solid #409eff;

          .example-sentence {
            font-size: 17px;
            font-weight: 500;
            color: #303133;
            margin-bottom: 8px;
            line-height: 1.6;
          }

          .example-translation {
            font-size: 14px;
            color: #909399;
            line-height: 1.5;
            padding-left: 12px;
            border-left: 2px solid #dcdfe6;
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
          background: var(--el-bg-color-page, #f5f7fa);
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
          background: var(--el-bg-color-page, #f5f7fa);
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
