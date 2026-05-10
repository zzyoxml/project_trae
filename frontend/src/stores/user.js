/**
 * Pinia Store - 用户模块
 * 
 * @description 用户状态管理，包括登录、用户信息、权限等
 * @author LingLearn Team
 */

import { defineStore } from 'pinia'
import { login, register, getUserInfo, logout } from '@/api/user'
import { getToken, setToken, removeToken } from '@/utils/auth'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: getToken(),
    userId: null,
    username: '',
    nickname: '',
    avatar: '',
    email: '',
    phone: '',
    nativeLanguage: 'zh',
    learningLanguages: ['英语'],
    learningGoal: '',
    dailyGoal: 30,
    totalStudyTime: 0,
    totalStudyMinutes: 360,
    currentStreak: 5,
    totalPoints: 2580,
    level: 1,
    experiencePoints: 0,
    completedCourses: 2,
    streakDays: 5,
    achievementsCount: 8,
    vocabularyCount: 156,
    roles: [],
    permissions: []
  }),

  getters: {
    isLoggedIn: (state) => !!state.token,
    
    levelProgress: (state) => {
      const threshold = 1000
      const progress = state.experiencePoints % threshold
      return Math.floor((progress / threshold) * 100)
    },
    
    expToNextLevel: (state) => {
      const threshold = 1000
      const nextLevelExp = Math.floor(state.experiencePoints / threshold + 1) * threshold
      return nextLevelExp - state.experiencePoints
    },
    
    formattedStudyTime: (state) => {
      const hours = Math.floor(state.totalStudyTime / 60)
      const minutes = state.totalStudyTime % 60
      return `${hours}小时${minutes}分钟`
    }
  },

  actions: {
    async login(loginForm) {
      try {
        const response = await login(loginForm)
        const { token, user } = response.data
        
        this.token = token
        setToken(token)
        
        if (user) {
          this.setUserInfo(user)
        }
        
        return response
      } catch (error) {
        console.error('登录失败:', error)
        throw error
      }
    },

    async register(registerForm) {
      try {
        const response = await register(registerForm)
        return response
      } catch (error) {
        console.error('注册失败:', error)
        throw error
      }
    },

    async getUserInfo() {
      try {
        const response = await getUserInfo()
        const user = response.data
        
        this.setUserInfo(user)
        
        return user
      } catch (error) {
        console.error('获取用户信息失败:', error)
        throw error
      }
    },

    setUserInfo(user) {
      if (!user) return
      
      this.userId = user.userId || user.id
      this.username = user.username || user.userName
      this.nickname = user.nickname || user.nickName
      this.avatar = user.avatar || user.avatarUrl
      this.email = user.email || ''
      this.phone = user.phone || ''
      this.nativeLanguage = user.nativeLanguage || 'zh'
      this.learningLanguages = user.learningLanguages || ['英语']
      this.learningGoal = user.learningGoal || ''
      this.dailyGoal = user.dailyGoal || 30
      this.totalStudyTime = user.totalStudyTime || 0
      this.totalStudyMinutes = user.totalStudyMinutes || user.totalMinutes || 360
      this.currentStreak = user.currentStreak || 5
      this.totalPoints = user.totalPoints || 2580
      this.level = user.level || 1
      this.experiencePoints = user.experiencePoints || 0
      this.completedCourses = user.completedCourses || 2
      this.streakDays = user.streakDays || 5
      this.achievementsCount = user.achievementsCount || 8
      this.vocabularyCount = user.vocabularyCount || 156
    },

    async updateUserInfo(userInfo) {
      try {
        Object.assign(this, userInfo)
        return Promise.resolve()
      } catch (error) {
        console.error('更新用户信息失败:', error)
        throw error
      }
    },

    async logout() {
      try {
        await logout()
        
        this.token = null
        removeToken()
        this.$reset()
        
        return Promise.resolve()
      } catch (error) {
        console.error('登出失败:', error)
        this.token = null
        removeToken()
        this.$reset()
        throw error
      }
    },

    resetToken() {
      this.token = null
      removeToken()
    }
  },

  persist: {
    enabled: true,
    strategies: [
      {
        key: 'lingua-user',
        storage: localStorage,
        paths: [
          'token', 
          'userId', 
          'username', 
          'nickname', 
          'avatar', 
          'email', 
          'phone',
          'nativeLanguage',
          'learningLanguages',
          'learningGoal',
          'dailyGoal'
        ]
      }
    ]
  }
})
