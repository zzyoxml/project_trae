/**
 * API 统一封装
 * 
 * @description 基于 Axios 的 HTTP 请求封装，包含请求拦截器、响应拦截器等
 * @author LingLearn Team
 */

import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getToken } from '@/utils/auth'
import router from '@/router'

// 创建 Axios 实例
const service = axios.create({
  // API基础路径 - 使用相对路径，通过Vite代理到后端
  baseURL: import.meta.env.VITE_API_BASE_URL || '/ruoyi-api',
  // 请求超时时间
  timeout: 30000,
  // 请求头
  headers: {
    'Content-Type': 'application/json;charset=utf-8'
  }
})

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    // 添加Token到请求头
    const token = getToken()
    if (token) {
      config.headers['Authorization'] = 'Bearer ' + token
    }
    
    // 添加时间戳防止缓存
    if (config.method === 'get') {
      config.params = {
        ...config.params,
        _t: Date.now()
      }
    }
    
    return config
  },
  (error) => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  (response) => {
    const res = response.data

    // 根据业务状态码判断请求是否成功
    if (res.code === 200) {
      // 如果有 data 字段且不为空，返回 data；否则返回整个 res
      if (res.data !== undefined) {
        return res.data
      }
      // 兼容没有 data 字段的响应（如 getInfo 返回 {code: 200, user: {...}}）
      return res
    } else if (res.code === 401) {
      // 业务 401：token 被踢下线 / 过期 / 认证失败
      ElMessage.error('登录状态已过期，请重新登录')
      localStorage.clear()
      // 用 SPA 路由跳转，避免 window.location.href 触发整页请求被 vite proxy 拦到后端
      if (router.currentRoute.value.path !== '/login') {
        router.replace('/login')
      }
      return Promise.reject(new Error(res.msg || '认证失败'))
    } else {
      // 处理业务错误
      ElMessage.error(res.msg || '请求失败')
      return Promise.reject(new Error(res.msg || '请求失败'))
    }
  },
  (error) => {
    console.error('响应错误:', error)
    
    // 处理HTTP错误状态码
    if (error.response) {
      switch (error.response.status) {
        case 401:
          // Token过期或被踢下线：直接清登录态 + 跳登录页（不弹确认框，避免用户没点导致停留在死页面）
          ElMessage.error('登录状态已过期，请重新登录')
          localStorage.clear()
          // 用 SPA 路由跳转，避免 window.location.href 触发整页请求被 vite proxy 拦到后端
          if (router.currentRoute.value.path !== '/login') {
            router.replace('/login')
          }
          break
          
        case 403:
          ElMessage.error('没有权限访问该资源')
          break
          
        case 404:
          ElMessage.error('请求的资源不存在')
          break
          
        case 500:
          ElMessage.error('服务器内部错误')
          break
          
        default:
          ElMessage.error('请求失败: ' + error.response.status)
      }
    } else {
      // 网络错误
      ElMessage.error('网络连接失败，请检查您的网络设置')
    }
    
    return Promise.reject(error)
  }
)

/**
 * GET 请求
 * @param {string} url - 请求地址
 * @param {object} params - 请求参数
 * @param {object} config - 配置项
 */
export function get(url, params = {}, config = {}) {
  return service.get(url, { params, ...config })
}

/**
 * POST 请求
 * @param {string} url - 请求地址
 * @param {object} data - 请求数据
 * @param {object} config - 配置项
 */
export function post(url, data = {}, config = {}) {
  return service.post(url, data, config)
}

/**
 * PUT 请求
 * @param {string} url - 请求地址
 * @param {object} data - 请求数据
 * @param {object} config - 配置项
 */
export function put(url, data = {}, config = {}) {
  return service.put(url, data, config)
}

/**
 * DELETE 请求
 * @param {string} url - 请求地址
 * @param {object} params - 请求参数
 * @param {object} config - 配置项
 */
export function del(url, params = {}, config = {}) {
  return service.delete(url, { params, ...config })
}

/**
 * PATCH 请求
 * @param {string} url - 请求地址
 * @param {object} data - 请求数据
 * @param {object} config - 配置项
 */
export function patch(url, data = {}, config = {}) {
  return service.patch(url, data, config)
}

/**
 * 上传文件
 * @param {string} url - 请求地址
 * @param {FormData} formData - FormData对象
 * @param {function} onProgress - 上传进度回调
 */
export function upload(url, formData, onProgress) {
  return service.post(url, formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    onUploadProgress: (progressEvent) => {
      if (onProgress && progressEvent.total) {
        const percent = Math.round((progressEvent.loaded * 100) / progressEvent.total)
        onProgress(percent)
      }
    }
  })
}

/**
 * 下载文件
 * @param {string} url - 请求地址
 * @param {object} params - 请求参数
 * @param {string} filename - 文件名
 */
export function download(url, params = {}, filename = 'download') {
  return service.get(url, {
    params,
    responseType: 'blob'
  }).then((response) => {
    const blob = new Blob([response.data])
    const link = document.createElement('a')
    link.href = window.URL.createObjectURL(blob)
    link.download = filename
    link.click()
    window.URL.revokeObjectURL(link.href)
  })
}

export default service
