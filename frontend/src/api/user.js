/**
 * 用户相关 API 接口
 */

import axios from 'axios'

const mockApi = axios.create({
  baseURL: '/',
  timeout: 10000
})

export function login(data) {
  return mockApi.get('/users').then(response => {
    const users = response.data
    const user = users.find(u => u.username === data.username && u.password === data.password)
    
    if (user) {
      return {
        data: {
          code: 200,
          msg: '登录成功',
          token: 'mock_token_' + user.id,
          user: {
            userId: user.id,
            username: user.username,
            nickname: user.nickname,
            email: user.email,
            avatar: user.avatar,
            level: user.level,
            totalMinutes: user.totalMinutes,
            streakDays: user.streakDays,
            completedCourses: user.completedCourses,
            achievements: user.achievements,
            learningLanguages: user.learningLanguages || ['英语'],
            learningGoal: user.learningGoal || '',
            dailyGoal: user.dailyGoal || 30
          }
        }
      }
    } else {
      return {
        data: {
          code: 500,
          msg: '用户名或密码错误'
        }
      }
    }
  }).catch(error => {
    console.error('登录请求失败:', error)
    return {
      data: {
        code: 500,
        msg: '网络错误，请稍后重试'
      }
    }
  })
}

export function register(data) {
  const newUser = {
    id: Date.now(),
    username: data.username,
    password: data.password,
    nickname: data.nickname || data.username,
    email: data.email,
    phone: data.phone,
    level: 1,
    totalMinutes: 0,
    streakDays: 0,
    completedCourses: 0,
    achievements: 0,
    createdAt: new Date().toISOString()
  }
  
  return mockApi.post('/users', newUser).then(response => ({
    data: {
      code: 200,
      msg: '注册成功',
      user: response.data
    }
  }))
}

export function getUserInfo() {
  return mockApi.get('/users/3').then(response => ({
    data: response.data
  }))
}

export function logout() {
  return Promise.resolve({
    data: {
      code: 200,
      msg: '退出成功'
    }
  })
}

export function updateUserInfo(userInfo) {
  return mockApi.put('/users/3', userInfo).then(response => ({
    data: response.data
  }))
}
