/**
 * 成就相关 API 接口
 */

import axios from 'axios'

const mockApi = axios.create({
  baseURL: '/',
  timeout: 10000
})

export function getAchievementList(params) {
  return mockApi.get('/achievements').then(response => {
    let achievements = [...response.data]
    
    if (params?.category && params.category !== 'all') {
      achievements = achievements.filter(a => a.category === params.category)
    }
    
    return {
      data: achievements
    }
  })
}

export function getMyAchievements() {
  return mockApi.get('/achievements').then(response => ({
    data: response.data.filter(a => a.completed)
  }))
}

export function getPendingAchievements() {
  return mockApi.get('/achievements').then(response => ({
    data: response.data.filter(a => !a.completed)
  }))
}

export function checkAchievements() {
  return Promise.resolve({
    data: {
      code: 200,
      msg: '成就已检查'
    }
  })
}

export function getLeaderboard(type = 'total', language = 'all', pageNum = 1, pageSize = 20) {
  const mockLeaderboard = [
    { rank: 1, username: 'LanguageMaster', score: 5800, totalMinutes: 2500, completedCourses: 15, achievements: 18, levelName: '王者' },
    { rank: 2, username: 'WordWizard', score: 5200, totalMinutes: 2200, completedCourses: 13, achievements: 15, levelName: '大师' },
    { rank: 3, username: 'GrammarGuru', score: 4800, totalMinutes: 2000, completedCourses: 12, achievements: 14, levelName: '大师' },
    { rank: 4, username: 'StudyChampion', score: 4200, totalMinutes: 1800, completedCourses: 10, achievements: 12, levelName: '钻石' },
    { rank: 5, username: 'LearningPro', score: 3800, totalMinutes: 1600, completedCourses: 9, achievements: 11, levelName: '钻石' },
    { rank: 6, username: 'VocabularyKing', score: 3200, totalMinutes: 1400, completedCourses: 8, achievements: 10, levelName: '铂金' },
    { rank: 7, username: 'Polyglot2024', score: 2900, totalMinutes: 1200, completedCourses: 7, achievements: 9, levelName: '铂金' },
    { rank: 8, username: 'LinguistPro', score: 2500, totalMinutes: 1000, completedCourses: 6, achievements: 8, levelName: '黄金' },
    { rank: 9, username: 'BilingualStar', score: 2200, totalMinutes: 900, completedCourses: 5, achievements: 7, levelName: '黄金' },
    { rank: 10, username: 'LanguageLearner', score: 1800, totalMinutes: 800, completedCourses: 4, achievements: 6, levelName: '白银' }
  ]
  
  const start = (pageNum - 1) * pageSize
  const end = start + pageSize
  
  return Promise.resolve({
    data: {
      code: 200,
      rows: mockLeaderboard.slice(start, end),
      total: mockLeaderboard.length
    }
  })
}

export function getMyRank(type = 'total', language = 'all') {
  return Promise.resolve({
    data: {
      rank: 150,
      score: 2500,
      type
    }
  })
}

export function initializeUserAchievements(userId) {
  return Promise.resolve({
    data: {
      code: 200,
      msg: '成就初始化成功'
    }
  })
}

export function getBadgeList() {
  return mockApi.get('/badges').then(response => ({
    data: response.data
  }))
}

export function getMyBadges() {
  return mockApi.get('/badges').then(response => ({
    data: response.data.filter(b => b.earned)
  }))
}

export function getUserBadgeWall(userId) {
  return mockApi.get('/badges').then(response => ({
    data: response.data
  }))
}
