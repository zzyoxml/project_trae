import request from '@/utils/request'

export function listChapter(query) {
  return request({
    url: '/edu/chapter/list',
    method: 'get',
    params: query
  })
}

export function listChapterByCourse(courseId) {
  return request({
    url: '/edu/chapter/byCourse/' + courseId,
    method: 'get'
  })
}

export function getChapter(chapterId) {
  return request({
    url: '/edu/chapter/' + chapterId,
    method: 'get'
  })
}

export function addChapter(data) {
  return request({
    url: '/edu/chapter',
    method: 'post',
    data: data
  })
}

export function updateChapter(data) {
  return request({
    url: '/edu/chapter',
    method: 'put',
    data: data
  })
}

export function delChapter(chapterIds) {
  return request({
    url: '/edu/chapter/' + chapterIds,
    method: 'delete'
  })
}