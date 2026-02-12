/**
 * 社区相关 API 接口
 */

import { get, post, put, del } from '@/utils/request'

export function getPostList(params) {
  return get('/edu/community/post/list', params)
}

export function getHotPosts(limit = 10) {
  return get('/edu/community/post/hot', { limit })
}

export function getPostsByLanguage(language, limit = 20) {
  return get(`/edu/community/post/language/${language}`, { limit })
}

export function getPostDetail(postId) {
  return get(`/edu/community/post/${postId}`)
}

export function publishPost(data) {
  return post('/edu/community/post', data)
}

export function updatePost(data) {
  return put('/edu/community/post', data)
}

export function deletePost(postId) {
  return del(`/edu/community/post/${postId}`)
}

export function likePost(postId) {
  return post(`/edu/community/post/${postId}/like`)
}

export function unlikePost(postId) {
  return del(`/edu/community/post/${postId}/like`)
}

export function getPostComments(postId) {
  return get(`/edu/community/post/${postId}/comments`)
}

export function addComment(data) {
  return post('/edu/community/comment', data)
}

export function deleteComment(commentId) {
  return del(`/edu/community/comment/${commentId}`)
}

export function likeComment(commentId) {
  return post(`/edu/community/comment/${commentId}/like`)
}

export function getMyPosts() {
  return get('/edu/community/post/my')
}

export function getPostStats() {
  return get('/edu/community/post/stats')
}

export function createPost(data) {
  return post('/edu/community/post', data)
}