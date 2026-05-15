import request from '@/utils/request'

export function getCourseList(params = {}) {
  return request({
    url: '/edu/course/list',
    method: 'get',
    params
  })
}

export function getCourseDetail(courseId) {
  return request({
    url: `/edu/course/${courseId}`,
    method: 'get'
  })
}

export function getFeaturedCourses(limit = 10) {
  return request({
    url: '/edu/course/featured',
    method: 'get',
    params: { limit }
  })
}

export function getCoursesByLanguage(language) {
  return request({
    url: `/edu/course/language/${language}`,
    method: 'get'
  })
}

export function getCoursesByLevel(level) {
  return request({
    url: `/edu/course/level/${level}`,
    method: 'get'
  })
}

export function enrollCourse(courseId) {
  return request({
    url: `/edu/course/enroll/${courseId}`,
    method: 'post'
  })
}

export function cancelEnroll(courseId) {
  return request({
    url: `/edu/course/enroll/${courseId}`,
    method: 'delete'
  })
}

export function getMyCourses() {
  return request({
    url: '/edu/course/my',
    method: 'get'
  })
}
