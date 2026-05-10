/**
 * 社区相关 API 接口
 */

import axios from 'axios'

const mockApi = axios.create({
  baseURL: '/',
  timeout: 10000
})

export function getPostList(params) {
  return mockApi.get('/posts').then(response => {
    let posts = [...response.data]
    
    if (params?.postType) {
      posts = posts.filter(post => post.postType === params.postType)
    }
    
    if (params?.language) {
      posts = posts.filter(post => post.language === params.language || post.language === 'all')
    }
    
    const pageNum = params?.pageNum || 1
    const pageSize = params?.pageSize || 10
    const start = (pageNum - 1) * pageSize
    const end = start + pageSize
    const rows = posts.slice(start, end)
    
    return {
      data: {
        code: 200,
        rows: rows,
        total: posts.length
      }
    }
  })
}

export function getHotPosts(limit = 10) {
  return mockApi.get('/posts').then(response => ({
    data: response.data.slice(0, limit)
  }))
}

export function getPostsByLanguage(language, limit = 20) {
  return mockApi.get('/posts').then(response => {
    const filtered = response.data.filter(post => 
      post.language === language || post.language === 'all'
    )
    return { data: filtered.slice(0, limit) }
  })
}

export function getPostDetail(postId) {
  return mockApi.get('/posts').then(response => {
    const post = response.data.find(p => p.postId == postId)
    return {
      data: post || response.data[0] || {}
    }
  })
}

export function publishPost(data) {
  return Promise.resolve({
    data: {
      code: 200,
      msg: '发布成功',
      data: { postId: Date.now() }
    }
  })
}

export function updatePost(data) {
  return Promise.resolve({
    data: {
      code: 200,
      msg: '修改成功'
    }
  })
}

export function deletePost(postId) {
  return Promise.resolve({
    data: {
      code: 200,
      msg: '删除成功'
    }
  })
}

export function likePost(postId) {
  return Promise.resolve({
    data: {
      code: 200,
      msg: '点赞成功'
    }
  })
}

export function unlikePost(postId) {
  return Promise.resolve({
    data: {
      code: 200,
      msg: '取消点赞'
    }
  })
}

export function getPostComments(postId) {
  return Promise.resolve({
    data: [
      {
        commentId: 1,
        username: '热心学员',
        content: '写得真好，受益匪浅！',
        createTime: '2026-05-10 09:30',
        likeCount: 5
      },
      {
        commentId: 2,
        username: '学习达人',
        content: '感谢分享，我也用这个方法学习日语',
        createTime: '2026-05-10 08:15',
        likeCount: 3
      }
    ]
  })
}

export function addComment(data) {
  return Promise.resolve({
    data: {
      code: 200,
      msg: '评论成功',
      data: { commentId: Date.now() }
    }
  })
}

export function deleteComment(commentId) {
  return Promise.resolve({
    data: {
      code: 200,
      msg: '删除成功'
    }
  })
}

export function likeComment(commentId) {
  return Promise.resolve({
    data: {
      code: 200,
      msg: '点赞成功'
    }
  })
}

export function getMyPosts() {
  return mockApi.get('/posts').then(response => ({
    data: response.data.slice(0, 5)
  }))
}

export function getPostStats() {
  return Promise.resolve({
    data: {
      totalPosts: 10,
      totalComments: 25,
      totalLikes: 58
    }
  })
}

export function createPost(data) {
  return Promise.resolve({
    data: {
      code: 200,
      msg: '发布成功',
      data: { postId: Date.now() }
    }
  })
}
