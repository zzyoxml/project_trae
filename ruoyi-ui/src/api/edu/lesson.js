import request from '@/utils/request'

export function listLesson(query) {
  return request({
    url: '/edu/lesson/list',
    method: 'get',
    params: query
  })
}

export function listLessonByUnit(unitId) {
  return request({
    url: '/edu/lesson/byUnit/' + unitId,
    method: 'get'
  })
}

export function getLesson(lessonId) {
  return request({
    url: '/edu/lesson/' + lessonId,
    method: 'get'
  })
}

export function addLesson(data) {
  return request({
    url: '/edu/lesson',
    method: 'post',
    data: data
  })
}

export function updateLesson(data) {
  return request({
    url: '/edu/lesson',
    method: 'put',
    data: data
  })
}

export function delLesson(lessonIds) {
  return request({
    url: '/edu/lesson/' + lessonIds,
    method: 'delete'
  })
}

export function batchImport(formData) {
  return request({
    url: '/edu/batch/import',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}