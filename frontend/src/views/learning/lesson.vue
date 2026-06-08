<template>
  <div class="lesson-page" :class="{ 'dark-mode': themeStore.isDark }">
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
                <el-empty v-else description="暂无单词数据，请先在后台添加该单元的词汇" :image-size="100" />
              </el-tab-pane>

              <!-- <el-tab-pane label="例句" name="examples">
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
              </el-tab-pane> -->

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
                  <!-- 当前练习的单词（可刷新随机抽一个） -->
                  <div class="speaking-target">
                    <div class="target-word-row">
                      <span class="target-label">请朗读：</span>
                      <span v-if="speakingWord" class="target-word">{{ speakingWord.word }}</span>
                      <el-button
                        v-if="speakingWord"
                        type="success"
                        size="default"
                        circle
                        @click="speakWord(speakingWord.word)"
                        title="朗读这个单词"
                      >
                        🔊
                      </el-button>
                      <el-button
                        type="success"
                        size="default"
                        :disabled="isRecording || !hasVocabulary"
                        @click="refreshSpeakingWord"
                        title="换一个单词"
                      >
                        <el-icon style="margin-right:6px"><Refresh /></el-icon>刷新
                      </el-button>
                    </div>
                    <div v-if="speakingWord" class="target-translation">{{ speakingWord.translation }}</div>
                    <el-empty
                      v-else
                      description="该课时暂无词汇，无法练习口语"
                      :image-size="80"
                    />
                  </div>

                  <el-button
                    type="primary"
                    size="large"
                    :disabled="!speakingWord || evaluating"
                    :loading="evaluating"
                    @click="toggleRecording"
                  >
                    <span v-if="!isRecording && !evaluating">🎤 开始录音</span>
                    <span v-else-if="isRecording">⏹ 停止录音</span>
                    <span v-else>评分中…</span>
                  </el-button>

                  <div v-if="isRecording" class="recording-indicator">
                    <span class="recording-dot"></span> 录音中… {{ recordSeconds }}s
                  </div>

                  <div v-if="score !== null" class="score-display">
                    <h3>本次得分：<span :class="scoreClass">{{ score }}</span> / 100</h3>
                    <p>{{ scoreMessage }}</p>
                    <el-button type="success" size="default" @click="refreshSpeakingWord">
                      <el-icon style="margin-right:6px"><Refresh /></el-icon>换一个再来一次
                    </el-button>
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
import { ref, onMounted, computed, onBeforeUnmount } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { useThemeStore } from '@/stores/theme'
import { getLessonDetail, completeLesson as completeLessonApi } from '@/api/learning'
import { evaluatePronunciation } from '@/api/speech'
import { Refresh } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const themeStore = useThemeStore()

const loading = ref(false)
const lesson = ref({})
const courseName = ref('')
const activeTab = ref('vocabulary')
// const listeningAnswer = ref('')  // 听力相关，暂时注释
const isRecording = ref(false)
const score = ref(null)

onMounted(async () => {
  // 检查登录状态
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录后再学习')
    router.push({ path: '/login', query: { redirect: route.fullPath } })
    return
  }
  
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
    // 加载完课时后，自动随机抽一个单词用于口语练习
    if (data.vocabularyList && data.vocabularyList.length > 0 && !speakingWord.value) {
      refreshSpeakingWord()
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
  // 优先使用浏览器自带的 SpeechSynthesis（Web Speech API），无需后端/外部 TTS 服务
  if ('speechSynthesis' in window && window.SpeechSynthesisUtterance) {
    try {
      window.speechSynthesis.cancel()  // 打断上一次
      const utter = new SpeechSynthesisUtterance(text)
      const langMap = { en: 'en-US', ja: 'ja-JP', zh: 'zh-CN' }
      utter.lang = langMap[language] || 'en-US'
      utter.rate = 0.9
      utter.pitch = 1
      utter.volume = 1
      utter.onerror = (e) => {
        console.warn('SpeechSynthesis 朗读失败:', e)
        // 兜底：尝试 /tts
        speakWithBackendTTS(text, language)
      }
      window.speechSynthesis.speak(utter)
      return
    } catch (e) {
      console.warn('SpeechSynthesis 异常:', e)
    }
  }
  speakWithBackendTTS(text, language)
}

// 兜底：调用外部 /tts 服务（需 Nginx 代理或 vite 代理配置）
const speakWithBackendTTS = (text, language) => {
  const voiceMap = {
    'en': 'en-US-JennyNeural',
    'ja': 'ja-JP-NanamiNeural',
    'zh': 'zh-CN-XiaoxiaoNeural'
  }
  const voice = voiceMap[language] || voiceMap['en']
  // 通过同域Nginx代理访问TTS服务，解决HTTPS页面Mixed Content问题
  const url = `/tts?t=${encodeURIComponent(text)}&v=${voice}&r=0&p=0&s=&api_key=`
  const audio = new Audio(url)
  audio.addEventListener('canplay', () => {
    audio.play().catch(err => {
      console.error('播放失败:', err)
      ElMessage.warning('语音播放失败')
    })
  })
  audio.addEventListener('error', () => {
    console.warn('TTS 加载失败:', url)
    ElMessage.warning('语音加载失败')
  })
}

// const checkListening = () => {
//   ElMessage.success('答案已提交')
// }  // 听力相关，暂时注释

// 口语练习相关状态
const speakingWord = ref(null)        // 当前随机选中的单词
const evaluating = ref(false)         // 评分中
const recordSeconds = ref(0)          // 录音计时（秒）
let mediaRecorder = null              // MediaRecorder 实例
let audioChunks = []                  // 录音数据片段
let recordTimer = null                // 录音计时器
let recordStream = null               // 麦克风媒体流

// 是否有可用词汇
const hasVocabulary = computed(() => {
  return lesson.value.vocabularyList && lesson.value.vocabularyList.length > 0
})

// 评分对应的 CSS class（高/中/低）
const scoreClass = computed(() => {
  if (score.value === null) return ''
  if (score.value >= 85) return 'score-high'
  if (score.value >= 60) return 'score-mid'
  return 'score-low'
})

// 从 vocabularyList 随机抽一个单词
const refreshSpeakingWord = () => {
  if (!hasVocabulary.value) {
    speakingWord.value = null
    return
  }
  const list = lesson.value.vocabularyList
  // 如果和当前单词不同则随机；保证每次都换
  let next = list[Math.floor(Math.random() * list.length)]
  if (speakingWord.value && list.length > 1) {
    while (next.word === speakingWord.value.word) {
      next = list[Math.floor(Math.random() * list.length)]
    }
  }
  speakingWord.value = next
  score.value = null  // 换词时清掉旧分
  // 顺带用 TTS 念一下，方便用户跟着读
  speakWord(next.word)
}

// 切换录音状态：开始 / 停止
const toggleRecording = async () => {
  if (isRecording.value) {
    stopRecording()
  } else {
    await startRecording()
  }
}

// 开始录音
const startRecording = async () => {
  if (!navigator.mediaDevices || !navigator.mediaDevices.getUserMedia) {
    ElMessage.error('当前浏览器不支持录音，请使用 Chrome / Edge 等现代浏览器')
    return
  }
  if (!speakingWord.value) {
    ElMessage.warning('请先选择要练习的单词')
    return
  }
  try {
    recordStream = await navigator.mediaDevices.getUserMedia({ audio: true })
    audioChunks = []
    // 优先用 webm，Chrome/Edge/Firefox 都支持；Safari 较新版本也支持
    const mimeType = MediaRecorder.isTypeSupported('audio/webm;codecs=opus')
      ? 'audio/webm;codecs=opus'
      : (MediaRecorder.isTypeSupported('audio/webm') ? 'audio/webm' : '')
    mediaRecorder = mimeType
      ? new MediaRecorder(recordStream, { mimeType })
      : new MediaRecorder(recordStream)
    mediaRecorder.ondataavailable = (e) => {
      if (e.data && e.data.size > 0) audioChunks.push(e.data)
    }
    mediaRecorder.onstop = handleRecordingStop
    mediaRecorder.start()
    isRecording.value = true
    recordSeconds.value = 0
    recordTimer = setInterval(() => { recordSeconds.value++ }, 1000)
    ElMessage.info('开始录音，请大声朗读')
  } catch (e) {
    console.error('录音启动失败:', e)
    ElMessage.error('无法访问麦克风：' + (e.message || e.name))
    cleanupRecording()
  }
}

// 停止录音
const stopRecording = () => {
  if (mediaRecorder && mediaRecorder.state !== 'inactive') {
    mediaRecorder.stop()
  }
  isRecording.value = false
  if (recordTimer) {
    clearInterval(recordTimer)
    recordTimer = null
  }
}

// 录音结束 -> 构造 Blob -> 调后端打分
const handleRecordingStop = async () => {
  try {
    if (audioChunks.length === 0) {
      ElMessage.warning('没有录到声音，请重试')
      cleanupRecording()
      return
    }
    const mimeType = mediaRecorder?.mimeType || 'audio/webm'
    const blob = new Blob(audioChunks, { type: mimeType })
    if (blob.size < 1024) {
      ElMessage.warning('录音太短（< 1KB），请多说一会儿')
      cleanupRecording()
      return
    }
    const ext = mimeType.includes('mp4') ? 'mp4' : 'webm'
    const file = new File([blob], `speaking.${ext}`, { type: mimeType })
    const formData = new FormData()
    formData.append('audio', file)
    formData.append('target', speakingWord.value.word)
    evaluating.value = true
    const res = await evaluatePronunciation(formData)
    // 后端返回 { code: 200, msg, data: { score, feedback, ... } }，request.js 已解出 data
    if (res && typeof res.score === 'number') {
      score.value = res.score
      ElMessage.success(`打分完成：${res.score} 分`)
    } else {
      ElMessage.error('评分失败，返回数据异常')
    }
  } catch (e) {
    console.error('评分失败:', e)
    ElMessage.error('评分失败：' + (e.message || '未知错误'))
  } finally {
    evaluating.value = false
    cleanupRecording()
  }
}

// 释放麦克风资源
const cleanupRecording = () => {
  if (recordStream) {
    recordStream.getTracks().forEach(t => t.stop())
    recordStream = null
  }
  mediaRecorder = null
  audioChunks = []
}

onBeforeUnmount(() => {
  if (recordTimer) clearInterval(recordTimer)
  cleanupRecording()
})

const completeLesson = async () => {
  try {
    const lessonId = route.params.id
    const res = await completeLessonApi({ lessonId })
    if (res && res.pointsAdded !== undefined) {
      ElMessage.success(`课时完成！获得 ${res.pointsAdded} 积分`)
    } else {
      ElMessage.success('课时完成！')
    }
    router.push('/learning')
  } catch (error) {
    console.error('完成课时失败:', error)
    if (error.message && error.message.includes('已完成')) {
      ElMessage.warning('该课时已完成，不可重复获取积分')
    } else {
      ElMessage.error('完成失败')
    }
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

        .speaking-target {
          background: var(--el-bg-color-page, #f5f7fa);
          padding: 20px 24px;
          border-radius: 12px;
          border-left: 4px solid #67c23a;

          .target-word-row {
            display: flex;
            align-items: center;
            gap: 12px;
            flex-wrap: wrap;
            margin-bottom: 8px;

            .target-label {
              color: #909399;
              font-size: 14px;
            }

            .target-word {
              font-size: 32px;
              font-weight: 700;
              color: #303133;
              letter-spacing: 1px;
            }

            .target-phonetic {
              color: #909399;
              font-size: 16px;
              font-style: italic;
            }
          }

          .target-translation {
            color: #409eff;
            font-size: 16px;
            padding-left: 4px;
          }
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

          .score-high { color: #67c23a; font-weight: 700; }
          .score-mid  { color: #e6a23c; font-weight: 700; }
          .score-low  { color: #f56c6c; font-weight: 700; }
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

.lesson-page.dark-mode {
  .lesson-content {
    .lesson-header {
      .lesson-info {
        color: #6a8a7a;
      }
    }

    .lesson-main {
      .vocabulary-list {
        .word-card {
          background: #1a2a2a;

          &:hover {
            background: #253535;
          }

          .word-translation {
            color: #98D8C8;
          }

          .word-example {
            color: #8aa89a;
          }
        }
      }

      .example-list {
        .example-card {
          background: #1a2a2a;
          border-left-color: #98D8C8;

          .example-sentence {
            color: #d0e0d8;
          }

          .example-translation {
            color: #6a8a7a;
            border-left-color: #2a3a3a;
          }
        }
      }

      .speaking-content {
        .prompt-text {
          background: #1a2a2a;
          color: #d0e0d8;
        }

        .speaking-target {
          background: #1a2a2a;
          border-left-color: #67c23a;

          .target-word {
            color: #e0e8e4;
          }
        }

        .score-display {
          background: #1a2a2a;

          h3 {
            color: #98D8C8;
          }

          .score-high { color: #67c23a; font-weight: 700; }
          .score-mid  { color: #e6a23c; font-weight: 700; }
          .score-low  { color: #f56c6c; font-weight: 700; }
        }
      }
    }
  }
}
</style>
