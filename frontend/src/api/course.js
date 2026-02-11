import axios from 'axios'

const ruoyiApi = axios.create({
  baseURL: '/ruoyi-api',
  timeout: 10000
})

ruoyiApi.interceptors.request.use(config => {
  const token = localStorage.getItem('ruoyi_token')
  if (token) {
    config.headers['Authorization'] = 'Bearer ' + token
  }
  return config
})

ruoyiApi.interceptors.response.use(
  response => response,
  error => {
    console.error('API请求错误:', error)
    return Promise.reject(error)
  }
)

const mockApi = axios.create({
  baseURL: '/',
  timeout: 10000
})

function mapCourse(course) {
  return {
    ...course,
    studentCount: course.studentCount || course.totalStudents || 0,
    lessonCount: course.lessonCount || course.totalLessons || 0,
    rating: course.rating || 0
  }
}

function sortCourses(courses, sortBy) {
  const sorted = [...courses]
  switch (sortBy) {
    case 'rating':
      return sorted.sort((a, b) => (b.rating || 0) - (a.rating || 0))
    case 'students':
      return sorted.sort((a, b) => (b.studentCount || 0) - (a.studentCount || 0))
    case 'lessons':
      return sorted.sort((a, b) => (b.lessonCount || 0) - (a.lessonCount || 0))
    default:
      return sorted
  }
}

function filterCourses(courses, filters) {
  return courses.filter(course => {
    if (filters.language && course.language !== filters.language) {
      return false
    }
    if (filters.level && course.level !== filters.level) {
      return false
    }
    if (filters.category && course.category !== filters.category) {
      return false
    }
    return true
  })
}

export function getCourseList(params = {}) {
  const { language, level, category, sortBy, pageNum = 1, pageSize = 8 } = params

  return mockApi.get('/courses').then(response => {
    let courses = [...(response.data || [])]

    if (language) {
      courses = courses.filter(course => course.language === language)
    }

    if (level) {
      const levelMap = { beginner: 1, intermediate: 2, advanced: 3 }
      const levelNum = levelMap[level] || level
      courses = courses.filter(course => course.level === levelNum)
    }

    if (category) {
      courses = courses.filter(course => course.category === category || course.courseType === category)
    }

    let sortedCourses = [...courses]
    if (sortBy === 'rating') {
      sortedCourses.sort((a, b) => (b.rating || 0) - (a.rating || 0))
    } else if (sortBy === 'students') {
      sortedCourses.sort((a, b) => (b.studentCount || 0) - (a.studentCount || 0))
    } else if (sortBy === 'lessons') {
      sortedCourses.sort((a, b) => (b.lessonCount || 0) - (a.lessonCount || 0))
    }

    const total = sortedCourses.length
    const start = (pageNum - 1) * pageSize
    const end = start + pageSize
    const paginatedCourses = sortedCourses.slice(start, end).map(course => ({
      ...course,
      studentCount: course.studentCount || course.totalStudents || 0,
      lessonCount: course.lessonCount || course.totalLessons || 0,
      rating: course.rating || 0
    }))

    return {
      data: {
        code: 200,
        rows: paginatedCourses,
        total: total
      }
    }
  }).catch(error => {
    console.error('获取课程列表失败:', error)
    return {
      data: {
        code: 500,
        rows: [],
        total: 0
      }
    }
  })
}

export function getCourseDetail(courseId) {
  return mockApi.get('/courses/' + courseId).then(response => {
    return {
      data: mapCourse(response.data || {})
    }
  }).catch(error => {
    console.error('获取课程详情失败:', error)
    return {
      data: {}
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
  return mockApi.get('/courses').then(response => {
    return {
      data: (response.data || []).slice(0, 3).map(mapCourse)
    }
  })
}

export function getFeaturedCourses(limit = 10) {
  return mockApi.get('/courses').then(response => {
    const courses = (response.data || []).filter(c => c.isFeatured)
    return {
      data: courses.slice(0, limit).map(mapCourse)
    }
  })
}

export function getPopularCourses(limit = 10) {
  return mockApi.get('/courses').then(response => {
    const courses = [...(response.data || [])]
    return {
      data: courses
        .sort((a, b) => (b.studentCount || 0) - (a.studentCount || 0))
        .slice(0, limit)
        .map(mapCourse)
    }
  })
}
