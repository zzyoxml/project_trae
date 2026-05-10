/**
 * 课程相关 API 接口
 */

import axios from 'axios'

const mockApi = axios.create({
  baseURL: '/',
  timeout: 10000
})

export function getCourseList(params) {
  return mockApi.get('/courses').then(response => {
    let filteredCourses = [...response.data]
    
    if (params?.language) {
      filteredCourses = filteredCourses.filter(course => course.language === params.language)
    }
    
    if (params?.level) {
      filteredCourses = filteredCourses.filter(course => course.level === params.level)
    }
    
    if (params?.category) {
      const categoryKeywords = {
        'vocabulary': ['词汇', '单词', 'vocabulary'],
        'grammar': ['语法', 'grammar'],
        'listening': ['听力', 'listening'],
        'speaking': ['口语', 'speaking']
      }
      const keywords = categoryKeywords[params.category] || []
      filteredCourses = filteredCourses.filter(course => 
        keywords.some(keyword => course.courseName.includes(keyword))
      )
    }
    
    if (params?.sortBy && params.sortBy !== 'default') {
      switch (params.sortBy) {
        case 'rating':
          filteredCourses.sort((a, b) => b.rating - a.rating)
          break
        case 'students':
          filteredCourses.sort((a, b) => b.studentCount - a.studentCount)
          break
        case 'lessons':
          filteredCourses.sort((a, b) => b.lessonCount - a.lessonCount)
          break
        default:
          break
      }
    }
    
    const pageNum = params?.pageNum || 1
    const pageSize = params?.pageSize || 10
    const start = (pageNum - 1) * pageSize
    const end = start + pageSize
    const rows = filteredCourses.slice(start, end)
    
    return {
      data: {
        code: 200,
        rows: rows,
        total: filteredCourses.length
      }
    }
  })
}

export function getCourseDetail(courseId) {
  return mockApi.get('/courses').then(response => {
    const course = response.data.find(c => c.courseId == courseId)
    return {
      data: course || response.data[0] || {}
    }
  })
}

export function enrollCourse(courseId) {
  return Promise.resolve({
    data: {
      code: 200,
      msg: '报名成功'
    }
  })
}

export function getMyCourses() {
  return mockApi.get('/courses').then(response => ({
    data: response.data.slice(0, 3)
  }))
}

export function getFeaturedCourses(limit = 10) {
  return mockApi.get('/courses').then(response => {
    const featured = response.data.filter(c => c.isFeatured)
    return {
      data: featured.length > 0 ? featured.slice(0, limit) : response.data.slice(0, limit)
    }
  })
}

export function getPopularCourses(limit = 10) {
  return mockApi.get('/courses').then(response => {
    const sorted = [...response.data].sort((a, b) => b.studentCount - a.studentCount)
    return {
      data: sorted.slice(0, limit)
    }
  })
}
