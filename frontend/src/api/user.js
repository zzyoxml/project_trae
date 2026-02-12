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