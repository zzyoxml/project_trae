import request from '@/utils/request'

export function listCourse(query) {
  return request({
    url: '/edu/course/list',
    method: 'get',
    params: query
  })
}

export function getCourse(courseId) {
  return request({
    url: '/edu/course/' + courseId,
    method: 'get'
  })
}

export function addCourse(data) {
  return request({
    url: '/edu/course',
    method: 'post',
    data: data
  })
}

export function updateCourse(data) {
  return request({
    url: '/edu/course',
    method: 'put',
    data: data
  })
}

export function delCourse(courseIds) {
  return request({
    url: '/edu/course/' + courseIds,
    method: 'delete'
  })
}

export function getFeaturedCourses(limit) {
  return request({
    url: '/edu/course/featured',
    method: 'get',
    params: { limit: limit || 10 }
  })
}

export function enrollCourse(courseId) {
  return request({
    url: '/edu/course/enroll/' + courseId,
    method: 'post'
  })
}

export function cancelEnroll(courseId) {
  return request({
    url: '/edu/course/enroll/' + courseId,
    method: 'delete'
  })
}

export function getMyCourses() {
  return request({
    url: '/edu/course/my',
    method: 'get'
  })
}
