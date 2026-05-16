/**
 * 路由配置
 * 
 * @description 应用的路由配置，定义了所有页面路由
 * @author LingLearn Team
 */

import { createRouter, createWebHistory } from 'vue-router'
import { getToken } from '@/utils/auth'

// 路由懒加载
const routes = [
  {
    path: '/',
    component: () => import('@/layouts/Layout.vue'),
    redirect: '/home',
    children: [
      {
        path: 'home',
        name: 'Home',
        component: () => import('@/views/home/index.vue'),
        meta: { title: '首页', icon: 'HomeFilled', requiresAuth: false }
      },
      {
        path: 'course',
        name: 'Course',
        component: () => import('@/views/course/index.vue'),
        meta: { title: '课程中心', icon: 'Reading', requiresAuth: false }
      },
      {
        path: 'course/:id',
        name: 'CourseDetail',
        component: () => import('@/views/course/detail.vue'),
        meta: { title: '课程详情', requiresAuth: true }
      },
      {
        path: 'learning',
        name: 'Learning',
        component: () => import('@/views/learning/index.vue'),
        meta: { title: '学习中心', icon: 'Monitor', requiresAuth: true }
      },
      {
        path: 'learning/lesson/:id',
        name: 'LessonDetail',
        component: () => import('@/views/learning/lesson.vue'),
        meta: { title: '课时学习', requiresAuth: true }
      },
      {
        path: 'challenge',
        name: 'Challenge',
        component: () => import('@/views/challenge/index.vue'),
        meta: { title: '闯关天梯', icon: 'Aim', requiresAuth: false }
      },
      {
        path: 'community',
        name: 'Community',
        component: () => import('@/views/community/index.vue'),
        meta: { title: '社区', icon: 'ChatDotRound', requiresAuth: false }
      },
      {
        path: 'community/post/:id',
        name: 'PostDetail',
        component: () => import('@/views/community/post.vue'),
        meta: { title: '帖子详情', requiresAuth: false }
      },
      {
        path: 'achievement',
        name: 'Achievement',
        component: () => import('@/views/achievement/index.vue'),
        meta: { title: '成就中心', icon: 'Trophy', requiresAuth: false }
      },
      {
        path: 'leaderboard',
        name: 'Leaderboard',
        component: () => import('@/views/achievement/leaderboard.vue'),
        meta: { title: '排行榜', icon: 'Medal', requiresAuth: false }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/profile/index.vue'),
        meta: { title: '个人中心', icon: 'User', requiresAuth: false }
      },
      {
        path: 'settings',
        name: 'Settings',
        component: () => import('@/views/profile/settings.vue'),
        meta: { title: '设置', requiresAuth: false }
      },
      {
        path: 'admin/users',
        name: 'AdminUsers',
        component: () => import('@/views/admin/users.vue'),
        meta: { title: '用户管理', requiresAuth: true }
      }
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录', requiresAuth: false }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/register/index.vue'),
    meta: { title: '注册', requiresAuth: false }
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/error/404.vue'),
    meta: { title: '页面不存在' }
  }
]

// 创建路由实例
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    } else {
      return { top: 0 }
    }
  }
})

// 路由前置守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  document.title = to.meta.title ? `${to.meta.title} - LinguaLearn` : 'LinguaLearn'
  
  // 检查是否需要登录
  const token = getToken()
  
  if (to.meta.requiresAuth && !token) {
    // 需要登录但未登录，跳转到登录页
    next({
      path: '/login',
      query: { redirect: to.fullPath }
    })
  } else {
    next()
  }
})

// 路由后置守卫
router.afterEach((to, from) => {
  // 路由切换完成
})

export default router
