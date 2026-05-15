import request from '@/utils/request'

/**
 * 获取学习统计数据
 */
export function getLearningStats() {
  return request({
    url: '/edu/learning/stats/all',
    method: 'get'
  })
}

/**
 * 获取用户学习列表
 */
export function getUserLearningList(params) {
  return request({
    url: '/edu/learning/users',
    method: 'get',
    params
  })
}

/**
 * 获取用户学习详情
 */
export function getUserLearningDetail(userId) {
  return request({
    url: `/edu/learning/users/${userId}`,
    method: 'get'
  })
}