/**
 * 用户相关 API 接口
 */

import { get, post, put } from '@/utils/request'

export function login(data) {
  return post('/login', data)
}

export function register(data) {
  return post('/register', data)
}

export function getUserInfo() {
  return get('/getInfo')
}

export function logout() {
  return post('/logout')
}

export function updateUserInfo(userInfo) {
  return put('/user/profile', userInfo)
}

export function getLearningStats() {
  return get('/edu/user/stats')
}

export function getAllUsers(params) {
  return get('/edu/user/list', params)
}

export function getUserDetail(userId) {
  return get(`/edu/user/profile/${userId}`)
}

export function addUser(data) {
  return post('/edu/user/register', data)
}

export function editUser(data) {
  return put('/edu/user/profile', data)
}

export function deleteUserById(userId) {
  return post(`/edu/user/delete/${userId}`)
}

export function updateUserStatus(userId, status) {
  return put('/edu/user/status', { userId, status })
}
