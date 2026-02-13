import request from '@/utils/request'

export function getAchievementList(params) {
  return request({
    url: '/edu/achievement/list',
    method: 'get',
    params
  })
}

export function getMyAchievements() {
  return request({
    url: '/edu/achievement/my',
    method: 'get'
  })
}

export function getPendingAchievements() {
  return request({
    url: '/edu/achievement/pending',
    method: 'get'
  })
}

export function checkAchievements() {
  return request({
    url: '/edu/achievement/check',
    method: 'post'
  })
}

export function getLeaderboard(type = 'total', language = 'all', pageNum = 1, pageSize = 20) {
  return request({
    url: '/edu/leaderboard',
    method: 'get',
    params: { type, language, pageNum, pageSize }
  })
}

export function getMyRank(type = 'total', language = 'all') {
  return request({
    url: '/edu/leaderboard/my',
    method: 'get',
    params: { type, language }
  })
}

export function initializeUserAchievements(userId) {
  return request({
    url: `/edu/achievement/init/${userId}`,
    method: 'post'
  })
}

export function getBadgeList() {
  return request({
    url: '/edu/badge/list',
    method: 'get'
  })
}

export function getMyBadges() {
  return request({
    url: '/edu/badge/my',
    method: 'get'
  })
}

export function getUserBadgeWall(userId) {
  return request({
    url: `/edu/badge/wall/${userId}`,
    method: 'get'
  })
}
