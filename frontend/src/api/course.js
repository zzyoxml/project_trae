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

function mapCourse(course) {
  return {
    ...course,
    studentCount: course.totalStudents || 0,
    lessonCount: course.totalLessons || 0
  }
}

export function getCourseList(params) {
  return ruoyiApi.get('/edu/course/list', { params }).then(response => {
    const rows = (response.data.rows || []).map(mapCourse)
    const total = response.data.total || 0
    return {
      data: {
        code: 200,
        rows: rows,
        total: total
      }
    }
  })
}

export function getCourseDetail(courseId) {
  return ruoyiApi.get('/edu/course/' + courseId).then(response => {
    return {
      data: mapCourse(response.data.data || {})
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
  return ruoyiApi.get('/edu/course/list', { params: { pageNum: 1, pageSize: 3 } }).then(response => {
    return {
      data: (response.data.rows || []).map(mapCourse)
    }
  })
}

export function getFeaturedCourses(limit = 10) {
  return ruoyiApi.get('/edu/course/list', { params: { pageNum: 1, pageSize: limit } }).then(response => {
    return {
      data: (response.data.rows || []).map(mapCourse)
    }
  })
}

export function getPopularCourses(limit = 10) {
  return ruoyiApi.get('/edu/course/list', { params: { pageNum: 1, pageSize: limit } }).then(response => {
    return {
      data: (response.data.rows || []).map(mapCourse)
    }
  })
}
