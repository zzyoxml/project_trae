/**
 * 文件相关工具函数
 */

/**
 * 处理图片URL，确保添加 /profile 前缀
 * @param {string} url 图片URL
 * @returns {string} 处理后的URL
 */
export function getImageUrl(url) {
  if (!url) return ''
  
  // 如果已经是完整URL（包含 http:// 或 https://），直接返回
  if (url.startsWith('http://') || url.startsWith('https://')) {
    return url
  }
  
  // 如果已经是 /profile 开头的，直接返回
  if (url.startsWith('/profile')) {
    return url
  }
  
  // 其他情况，添加 /profile 前缀
  return '/profile' + url
}

/**
 * 处理用户头像URL
 * @param {string} avatar 头像URL
 * @returns {string} 处理后的URL
 */
export function getAvatarUrl(avatar) {
  return getImageUrl(avatar)
}

/**
 * 处理课程封面URL
 * @param {string} coverImage 封面URL
 * @returns {string} 处理后的URL
 */
export function getCoverImageUrl(coverImage) {
  return getImageUrl(coverImage)
}
