/**
 * 认证工具类
 *
 * @description 管理Token和用户信息的存储与获取
 * @author LingLearn Team
 */

const TOKEN_KEY = 'token'
const USER_KEY = 'user'

/**
 * 获取Token
 */
export function getToken() {
  return localStorage.getItem(TOKEN_KEY)
}

/**
 * 设置Token
 */
export function setToken(token) {
  localStorage.setItem(TOKEN_KEY, token)
}

/**
 * 移除Token
 */
export function removeToken() {
  localStorage.removeItem(TOKEN_KEY)
}

/**
 * 获取用户信息
 */
export function getUserInfo() {
  const userStr = localStorage.getItem(USER_KEY)
  return userStr ? JSON.parse(userStr) : null
}

/**
 * 设置用户信息
 */
export function setUserInfo(userInfo) {
  localStorage.setItem(USER_KEY, JSON.stringify(userInfo))
}

/**
 * 移除用户信息
 */
export function removeUserInfo() {
  localStorage.removeItem(USER_KEY)
}

/**
 * 清除所有认证信息
 */
export function clearAuth() {
  removeToken()
  removeUserInfo()
}

/**
 * 获取学习语言
 */
export function getLanguage() {
  return localStorage.getItem('language') || 'en'
}

/**
 * 设置学习语言
 */
export function setLanguage(language) {
  localStorage.setItem('language', language)
}
