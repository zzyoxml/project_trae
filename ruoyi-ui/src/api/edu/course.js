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
