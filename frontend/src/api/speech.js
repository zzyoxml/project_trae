/**
 * 语音识别 API 接口
 * 
 * @description 语音识别、发音评分相关接口
 * @author LingLearn Team
 */

import request from '@/utils/request'

/**
 * 语音转文字
 * @param {FormData} formData - 包含音频和语言参数
 * @returns {Promise}
 */
export function recognizeSpeech(formData) {
  return request({
    url: '/edu/speech/recognize',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 发音评分（前端录音 + 参考文本）
 * @param {FormData} formData - 包含 audio 文件和 target 文本
 * @returns {Promise<{score:number, feedback:string, target:string, audioSize:number}>}
 */
export function evaluatePronunciation(formData) {
  return request({
    url: '/edu/speech/evaluate',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 文字转语音
 * @param {Object} params - 参数 { text, language }
 * @returns {Promise}
 */
export function synthesizeSpeech(params) {
  return request({
    url: '/edu/speech/synthesize',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

/**
 * 获取支持的语言列表
 * @returns {Promise}
 */
export function getSupportedLanguages() {
  return request({
    url: '/edu/speech/languages',
    method: 'get'
  })
}

/**
 * 检查语音服务状态
 * @returns {Promise}
 */
export function checkSpeechServiceStatus() {
  return request({
    url: '/edu/speech/status',
    method: 'get'
  })
}
