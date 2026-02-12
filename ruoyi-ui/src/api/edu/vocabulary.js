import request from '@/utils/request'

// 查询词汇列表
export function listVocabulary(query) {
  return request({
    url: '/edu/vocabulary/list',
    method: 'get',
    params: query
  })
}

// 查询词汇详细
export function getVocabulary(vocabId) {
  return request({
    url: '/edu/vocabulary/' + vocabId,
    method: 'get'
  })
}

// 新增词汇
export function addVocabulary(data) {
  return request({
    url: '/edu/vocabulary',
    method: 'post',
    data: data
  })
}

// 修改词汇
export function updateVocabulary(data) {
  return request({
    url: '/edu/vocabulary',
    method: 'put',
    data: data
  })
}

// 删除词汇
export function delVocabulary(vocabIds) {
  return request({
    url: '/edu/vocabulary/' + vocabIds,
    method: 'delete'
  })
}