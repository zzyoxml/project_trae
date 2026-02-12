import request from '@/utils/request'

// 查询帖子列表
export function listPost(query) {
  return request({
    url: '/edu/post/list',
    method: 'get',
    params: query
  })
}

// 查询帖子详细
export function getPost(postId) {
  return request({
    url: '/edu/post/' + postId,
    method: 'get'
  })
}

// 新增帖子
export function addPost(data) {
  return request({
    url: '/edu/post',
    method: 'post',
    data: data
  })
}

// 修改帖子
export function updatePost(data) {
  return request({
    url: '/edu/post',
    method: 'put',
    data: data
  })
}

// 删除帖子
export function delPost(postIds) {
  return request({
    url: '/edu/post/' + postIds,
    method: 'delete'
  })
}

// 审核帖子
export function auditPost(postId, status) {
  return request({
    url: '/edu/post/audit/' + postId,
    method: 'put',
    data: { status: status }
  })
}

// 批量审核帖子
export function batchAuditPost(postIds) {
  return request({
    url: '/edu/post/batchAudit',
    method: 'put',
    data: postIds
  })
}