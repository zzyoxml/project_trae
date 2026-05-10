/**
 * 语音识别 Hook
 * 
 * @description Vue 3 Composition API Hook for Speech Recognition
 * @author LingLearn Team
 */

import { ref, onUnmounted } from 'vue'
import { evaluatePronunciation, synthesizeSpeech, getSupportedLanguages } from '@/api/speech'
import { ElMessage } from 'element-plus'

/**
 * 语音识别 Hook
 * 
 * @param {Object} options - 配置选项
 * @returns {Object} 语音识别相关状态和方法
 */
export function useSpeechRecognition(options = {}) {
  // 状态
  const isRecording = ref(false)
  const isProcessing = ref(false)
  const recognizedText = ref('')
  const audioBlob = ref(null)
  const audioUrl = ref('')
  const error = ref(null)
  
  // 配置
  const language = options.language || 'en'
  const continuous = options.continuous || false
  const interimResults = options.interimResults || true
  
  // 语音识别引擎
  let recognition = null

  /**
   * 初始化语音识别
   */
  const initRecognition = () => {
    // 检查浏览器支持
    const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition
    
    if (!SpeechRecognition) {
      error.value = '您的浏览器不支持语音识别功能'
      ElMessage.error('您的浏览器不支持语音识别功能，请使用Chrome或Edge浏览器')
      return false
    }

    recognition = new SpeechRecognition()
    recognition.lang = getLanguageCode(language)
    recognition.continuous = continuous
    recognition.interimResults = interimResults
    recognition.maxAlternatives = 1

    // 结果处理
    recognition.onresult = (event) => {
      const results = event.results
      const transcript = results[results.length - 1][0].transcript
      recognizedText.value = transcript
      
      if (results[results.length - 1].isFinal) {
        isRecording.value = false
      }
    }

    // 错误处理
    recognition.onerror = (event) => {
      error.value = event.error
      isRecording.value = false
      
      if (event.error === 'not-allowed') {
        ElMessage.error('请允许麦克风权限')
      } else if (event.error === 'no-speech') {
        ElMessage.warning('未检测到语音，请重试')
      }
    }

    // 结束处理
    recognition.onend = () => {
      isRecording.value = false
    }

    return true
  }

  /**
   * 开始录音
   */
  const startRecording = () => {
    if (!recognition) {
      initRecognition()
    }
    
    if (recognition) {
      try {
        recognition.start()
        isRecording.value = true
        error.value = null
      } catch (e) {
        error.value = e.message
        ElMessage.error('启动录音失败：' + e.message)
      }
    }
  }

  /**
   * 停止录音
   */
  const stopRecording = () => {
    if (recognition) {
      recognition.stop()
    }
    isRecording.value = false
  }

  /**
   * 获取语言代码
   */
  const getLanguageCode = (lang) => {
    const codes = {
      en: 'en-US',
      ja: 'ja-JP',
      zh: 'zh-CN'
    }
    return codes[lang] || 'en-US'
  }

  /**
   * 清理资源
   */
  const cleanup = () => {
    if (recognition) {
      recognition.stop()
      recognition = null
    }
    if (audioUrl.value) {
      URL.revokeObjectURL(audioUrl.value)
    }
    isRecording.value = false
  }

  // 组件卸载时清理
  onUnmounted(() => {
    cleanup()
  })

  return {
    // 状态
    isRecording,
    isProcessing,
    recognizedText,
    audioBlob,
    audioUrl,
    error,
    
    // 方法
    initRecognition,
    startRecording,
    stopRecording,
    cleanup
  }
}

/**
 * 发音评分 Hook
 * 
 * @returns {Object} 发音评分相关方法
 */
export function usePronunciationEvaluation() {
  const isLoading = ref(false)
  const evaluationResult = ref(null)
  const error = ref(null)

  /**
   * 评分发音
   * 
   * @param {string} audioData - 音频数据（Base64）
   * @param {string} referenceText - 参考文本
   * @param {string} language - 语言代码
   * @returns {Promise<Object>} 评分结果
   */
  const evaluate = async (audioData, referenceText, language = 'en') => {
    isLoading.value = true
    error.value = null
    
    try {
      const result = await evaluatePronunciation({
        audio: audioData,
        referenceText,
        language
      })
      
      evaluationResult.value = result.data
      return result.data
    } catch (e) {
      error.value = e.message
      ElMessage.error('评分失败：' + e.message)
      throw e
    } finally {
      isLoading.value = false
    }
  }

  /**
   * 生成发音反馈
   */
  const getFeedback = (score) => {
    if (score >= 90) {
      return {
        level: 'excellent',
        message: '太棒了！您的发音非常标准！',
        color: '#67C23A'
      }
    } else if (score >= 80) {
      return {
        level: 'good',
        message: '很好！您的发音很不错',
        color: '#85CE61'
      }
    } else if (score >= 70) {
      return {
        level: 'fair',
        message: '不错！继续加油',
        color: '#E6A23C'
      }
    } else if (score >= 60) {
      return {
        level: 'poor',
        message: '还需努力，多听多练',
        color: '#F56C6C'
      }
    } else {
      return {
        level: 'bad',
        message: '建议从基础开始练习',
        color: '#C0C4CC'
      }
    }
  }

  return {
    isLoading,
    evaluationResult,
    error,
    evaluate,
    getFeedback
  }
}

/**
 * 语音合成 Hook
 * 
 * @returns {Object} 语音合成相关方法
 */
export function useSpeechSynthesis() {
  const isPlaying = ref(false)
  const isLoading = ref(false)
  const error = ref(null)

  // 使用浏览器原生 SpeechSynthesis
  let utterance = null

  /**
   * 文本转语音
   * 
   * @param {string} text - 文本内容
   * @param {string} language - 语言代码
   */
  const speak = (text, language = 'en') => {
    if (!('speechSynthesis' in window)) {
      error.value = '您的浏览器不支持语音合成功能'
      ElMessage.error('您的浏览器不支持语音合成功能')
      return
    }

    // 停止之前的朗读
    window.speechSynthesis.cancel()

    utterance = new SpeechSynthesisUtterance(text)
    utterance.lang = getLanguageCode(language)
    utterance.rate = 0.9  // 稍慢的语速
    utterance.pitch = 1.0

    utterance.onstart = () => {
      isPlaying.value = true
    }

    utterance.onend = () => {
      isPlaying.value = false
    }

    utterance.onerror = (e) => {
      error.value = e.error
      isPlaying.value = false
    }

    window.speechSynthesis.speak(utterance)
  }

  /**
   * 停止朗读
   */
  const stop = () => {
    if ('speechSynthesis' in window) {
      window.speechSynthesis.cancel()
      isPlaying.value = false
    }
  }

  /**
   * 暂停朗读
   */
  const pause = () => {
    if ('speechSynthesis' in window) {
      window.speechSynthesis.pause()
    }
  }

  /**
   * 恢复朗读
   */
  const resume = () => {
    if ('speechSynthesis' in window) {
      window.speechSynthesis.resume()
    }
  }

  /**
   * 获取语言代码
   */
  const getLanguageCode = (lang) => {
    const codes = {
      en: 'en-US',
      ja: 'ja-JP',
      zh: 'zh-CN'
    }
    return codes[lang] || 'en-US'
  }

  return {
    isPlaying,
    isLoading,
    error,
    speak,
    stop,
    pause,
    resume
  }
}
