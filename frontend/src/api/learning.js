import request from '@/utils/request'

export function startLesson(lessonId, courseId) {
  return request({
    url: '/edu/learning/start',
    method: 'post',
    data: { lessonId, courseId }
  })
}

export function submitLearningResult(data) {
  return request({
    url: '/edu/learning/submit',
    method: 'post',
    data
  })
}

export function getLearningStats() {
  return request({
    url: '/edu/learning/stats',
    method: 'get'
  })
}

export function getTodayLearningData() {
  return request({
    url: '/edu/learning/today',
    method: 'get'
  })
}

export function getSkillScores() {
  return request({
    url: '/edu/learning/skills',
    method: 'get'
  })
}

export function getCourseProgress(courseId) {
  return request({
    url: `/edu/learning/progress/${courseId}`,
    method: 'get'
  })
}

export function getRecentRecords(days = 7) {
  return request({
    url: '/edu/learning/recent',
    method: 'get',
    params: { days }
  })
}

export function completeLesson(lessonId, courseId, data) {
  return request({
    url: '/edu/learning/complete',
    method: 'post',
    data: { lessonId, courseId, ...data }
  })
}

export function getLessonDetail(lessonId) {
  return request({
    url: `/edu/learning/lesson/${lessonId}`,
    method: 'get'
  })
}
