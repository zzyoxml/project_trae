/**
 * 学习相关 API 接口
 */

import axios from 'axios'

const mockApi = axios.create({
  baseURL: '/',
  timeout: 10000
})

export function startLesson(lessonId, courseId) {
  return Promise.resolve({
    data: {
      code: 200,
      msg: '开始学习',
      data: { recordId: Date.now(), lessonId, courseId }
    }
  })
}

export function submitLearningResult(data) {
  return Promise.resolve({
    data: {
      code: 200,
      msg: '提交成功',
      data: { score: 95, exp: 100 }
    }
  })
}

export function getLearningStats() {
  return Promise.resolve({
    data: {
      code: 200,
      data: {
        totalStudyMinutes: 360,
        streakDays: 5,
        completedLessons: 15,
        totalVocabulary: 156,
        completedCourses: 2,
        achievements: 8
      }
    }
  })
}

export function getTodayLearningData() {
  return Promise.resolve({
    data: {
      code: 200,
      data: {
        todayMinutes: 45,
        todayVocabulary: 10,
        weeklyMinutes: 280,
        dailyGoal: 30,
        progress: 150
      }
    }
  })
}

export function getSkillScores() {
  return Promise.resolve({
    data: {
      code: 200,
      data: [
        { skill: 'vocabulary', name: '词汇', score: 85 },
        { skill: 'grammar', name: '语法', score: 78 },
        { skill: 'listening', name: '听力', score: 72 },
        { skill: 'speaking', name: '口语', score: 65 },
        { skill: 'reading', name: '阅读', score: 80 },
        { skill: 'writing', name: '写作', score: 70 }
      ]
    }
  })
}

export function getCourseProgress(courseId) {
  return mockApi.get('/courses').then(response => {
    const course = response.data.find(c => c.courseId == courseId)
    return {
      data: {
        code: 200,
        data: {
          courseId,
          courseName: course?.courseName || '课程',
          totalLessons: course?.lessonCount || 20,
          completedLessons: Math.floor((course?.lessonCount || 20) * 0.4),
          progress: 40
        }
      }
    }
  })
}

export function getRecentLearningRecords(days = 7) {
  return Promise.resolve({
    data: {
      code: 200,
      data: [
        { date: '2026-05-10', minutes: 45, completedLessons: 2 },
        { date: '2026-05-09', minutes: 60, completedLessons: 3 },
        { date: '2026-05-08', minutes: 30, completedLessons: 1 },
        { date: '2026-05-07', minutes: 55, completedLessons: 2 },
        { date: '2026-05-06', minutes: 40, completedLessons: 2 },
        { date: '2026-05-05', minutes: 35, completedLessons: 1 },
        { date: '2026-05-04', minutes: 50, completedLessons: 2 }
      ]
    }
  })
}

export function getLearningRecordList(params) {
  return Promise.resolve({
    data: {
      code: 200,
      rows: [
        { recordId: 1, courseName: '英语入门', lessonName: 'Lesson 1', duration: 20, score: 90, createTime: '2026-05-10 09:00' },
        { recordId: 2, courseName: '英语入门', lessonName: 'Lesson 2', duration: 25, score: 85, createTime: '2026-05-10 09:30' },
        { recordId: 3, courseName: '日语入门', lessonName: '五十音图', duration: 30, score: 95, createTime: '2026-05-09 14:00' },
        { recordId: 4, courseName: '英语入门', lessonName: 'Lesson 3', duration: 22, score: 88, createTime: '2026-05-09 10:00' },
        { recordId: 5, courseName: '汉语拼音入门', lessonName: '声母', duration: 18, score: 92, createTime: '2026-05-08 15:00' }
      ],
      total: 10
    }
  })
}

export function getLearningRecordDetail(recordId) {
  return Promise.resolve({
    data: {
      code: 200,
      data: {
        recordId,
        courseName: '英语入门',
        lessonName: 'Lesson 1',
        duration: 20,
        score: 90,
        answers: [
          { question: '单词 "apple" 的意思是？', userAnswer: '苹果', correctAnswer: '苹果', isCorrect: true },
          { question: '单词 "book" 的意思是？', userAnswer: '书', correctAnswer: '书', isCorrect: true },
          { question: '单词 "cat" 的意思是？', userAnswer: '猫', correctAnswer: '猫', isCorrect: true }
        ],
        createTime: '2026-05-10 09:00'
      }
    }
  })
}

export function getVocabularyList(params) {
  return mockApi.get('/vocabulary').then(response => {
    let words = [...response.data]
    if (params?.language) {
      words = words.filter(w => w.language === params.language)
    }
    if (params?.limit) {
      words = words.slice(0, params.limit)
    }
    return {
      data: {
        code: 200,
        data: words
      }
    }
  })
}

export function getTodayVocabulary(limit = 10) {
  return mockApi.get('/vocabulary').then(response => ({
    data: {
      code: 200,
      data: response.data.slice(0, limit)
    }
  }))
}

export function recordVocabulary(vocabId, masteryLevel) {
  return Promise.resolve({
    data: {
      code: 200,
      msg: '记录成功'
    }
  })
}

export function getGrammarList(params) {
  return mockApi.get('/grammar').then(response => ({
    data: {
      code: 200,
      data: response.data
    }
  }))
}

export function getListeningResources(params) {
  return Promise.resolve({
    data: {
      code: 200,
      data: [
        { id: 1, title: '日常对话听力', duration: 5, level: 1, language: 'en' },
        { id: 2, title: '新闻听力', duration: 8, level: 2, language: 'en' },
        { id: 3, title: '日语场景对话', duration: 6, level: 1, language: 'ja' },
        { id: 4, title: '中文故事听力', duration: 10, level: 1, language: 'zh' }
      ]
    }
  })
}

export function submitListeningTest(data) {
  return Promise.resolve({
    data: {
      code: 200,
      msg: '提交成功',
      data: { score: 85, correctCount: 8, totalCount: 10 }
    }
  })
}

export function getLearningProgress() {
  return Promise.resolve({
    data: {
      code: 200,
      data: {
        totalMinutes: 360,
        streakDays: 5,
        vocabularyCount: 156,
        grammarCount: 45,
        completedLessons: 15,
        completedCourses: 2
      }
    }
  })
}

export function getLearningRecords(params) {
  return Promise.resolve({
    data: {
      code: 200,
      rows: [
        { recordId: 1, courseId: 1, courseName: '英语入门', lessonId: 1, lessonName: 'Lesson 1', duration: 20, score: 90, createTime: '2026-05-10 09:00' },
        { recordId: 2, courseId: 1, courseName: '英语入门', lessonId: 2, lessonName: 'Lesson 2', duration: 25, score: 85, createTime: '2026-05-10 09:30' },
        { recordId: 3, courseId: 2, courseName: '日语入门', lessonId: 1, lessonName: '五十音图', duration: 30, score: 95, createTime: '2026-05-09 14:00' }
      ],
      total: 10
    }
  })
}

export function getLessonDetail(lessonId) {
  return Promise.resolve({
    data: {
      code: 200,
      data: {
        lessonId,
        title: 'Lesson ' + lessonId,
        content: '这是第 ' + lessonId + ' 课的内容，包含词汇学习和练习。',
        duration: 20,
        courseId: 1,
        courseName: '英语入门',
        materials: [
          { type: 'video', title: '教学视频', url: '#' },
          { type: 'text', title: '课文内容', url: '#' },
          { type: 'exercise', title: '练习题', url: '#' }
        ]
      }
    }
  })
}

export function completeLesson(data) {
  return Promise.resolve({
    data: {
      code: 200,
      msg: '学习完成',
      data: { exp: 50, points: 20 }
    }
  })
}
