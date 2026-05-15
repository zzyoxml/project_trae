import request from '@/utils/request'

// 查询学员列表
export function listUser(query) {
  return request({
    url: '/edu/user/list',
    method: 'get',
    params: query
  })
}

// 查询学员详细
export function getUser(userId) {
  return request({
    url: '/edu/user/profile/' + userId,
    method: 'get'
  })
}

// 新增学员
export function addUser(data) {
  return request({
    url: '/edu/user/register',
    method: 'post',
    data: data
  })
}

// 修改学员
export function updateUser(data) {
  return request({
    url: '/edu/user/profile',
    method: 'put',
    data: data
  })
}

// 删除学员
export function delUser(userId) {
  return request({
    url: '/edu/user/delete/' + userId,
    method: 'post'
  })
}

// 修改用户状态
export function changeUserStatus(userId, status) {
  return request({
    url: '/edu/user/status',
    method: 'put',
    data: { userId, status }
  })
}

// 获取学习统计
export function getLearningStats(userId) {
  return request({
    url: '/edu/user/stats',
    method: 'get'
  })
}
