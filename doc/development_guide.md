# 多语种在线教育平台开发文档

## 项目概述

### 项目名称
LinguaLearn - 多语种在线教育平台

### 项目目标
基于RuoYi框架开发一款支持多语种学习的在线教育平台PC+手机端，涵盖英语、日语、汉语等主流语言，打造沉浸式语言学习体验。

### 技术栈
- **后端**：Spring Boot 2.5.15 + MyBatis + Redis + JWT
- **前端**：Vue 3 + Element Plus + Vite
- **数据库**：MySQL 8.0
- **缓存**：Redis
- **版本控制**：Git

### 核心功能模块
1. 用户认证与多语言支持
2. 分级课程体系管理
3. 互动式学习模块
4. 学习进度追踪
5. 社区交流与成就激励
6. 个性化学习路径推荐

---

## 项目架构设计

### 整体架构
```
┌─────────────────────────────────────────────────────────┐
│                    客户端层 (Web + Mobile)               │
├─────────────────────────────────────────────────────────┤
│                    前端表现层 (Vue 3)                    │
│    ├── 用户界面模块                                       │
│    ├── 课程学习模块                                       │
│    ├── 互动练习模块                                       │
│    ├── 社区交流模块                                       │
│    └── 个人中心模块                                       │
├─────────────────────────────────────────────────────────┤
│                    API网关层 (Spring Security + JWT)      │
├─────────────────────────────────────────────────────────┤
│                    业务逻辑层 (Spring Boot)               │
│    ├── 用户服务 (User Service)                           │
│    ├── 课程服务 (Course Service)                        │
│    ├── 学习服务 (Learning Service)                       │
│    ├── 闯关服务 (Challenge Service)                     │
│    ├── 社区服务 (Community Service)                      │
│    ├── 成就服务 (Achievement Service)                    │
│    └── 推荐服务 (Recommendation Service)                 │
├─────────────────────────────────────────────────────────┤
│                    数据访问层 (MyBatis + Redis)           │
│    ├── MySQL 数据库                                      │
│    └── Redis 缓存                                       │
└─────────────────────────────────────────────────────────┘
```

### 模块划分
```
com.ruoyi
├── edu                      # 教育平台模块
│   ├── controller          # 控制器层
│   │   ├── EduUserController.java
│   │   ├── EduCourseController.java
│   │   ├── EduLearningController.java
│   │   ├── EduChallengeController.java
│   │   ├── EduCommunityController.java
│   │   ├── EduAchievementController.java
│   │   └── EduRecommendationController.java
│   ├── service            # 服务层
│   │   ├── impl
│   │   ├── IEduUserService.java
│   │   ├── IEduCourseService.java
│   │   ├── IEduLearningService.java
│   │   ├── IEduChallengeService.java
│   │   ├── IEduCommunityService.java
│   │   ├── IEduAchievementService.java
│   │   └── IEduRecommendationService.java
│   ├── mapper            # 数据访问层
│   │   ├── EduUserMapper.java
│   │   ├── EduCourseMapper.java
│   │   ├── EduLearningMapper.java
│   │   ├── EduChallengeMapper.java
│   │   ├── EduCommunityMapper.java
│   │   ├── EduAchievementMapper.java
│   │   └── EduVocabularyMapper.java
│   ├── domain           # 实体层
│   │   ├── EduUser.java
│   │   ├── EduCourse.java
│   │   ├── EduCourseUnit.java
│   │   ├── EduCourseLesson.java
│   │   ├── EduVocabulary.java
│   │   ├── EduGrammar.java
│   │   ├── EduListeningResource.java
│   │   ├── EduSpeakingPractice.java
│   │   ├── EduLearningRecord.java
│   │   ├── EduLearningProgress.java
│   │   ├── EduUserCourse.java
│   │   ├── EduLadderChallenge.java
│   │   ├── EduUserChallenge.java
│   │   ├── EduAchievement.java
│   │   ├── EduUserAchievement.java
│   │   ├── EduBadge.java
│   │   ├── EduUserBadge.java
│   │   ├── EduPost.java
│   │   ├── EduPostComment.java
│   │   ├── EduLearningPath.java
│   │   └── EduUserLearningPath.java
│   └── config           # 配置类
│       ├── SecurityConfig.java
│       └── CorsConfig.java
```

---

## 数据库设计

详见 [数据库设计文档](./database_design.md)

---

## 功能模块详细设计

### 1. 用户认证与多语言支持

#### 1.1 用户注册
- **功能描述**：支持邮箱、手机号注册
- **验证规则**：
  - 用户名：4-20位字母数字组合
  - 密码：8-20位，必须包含字母和数字
  - 邮箱：标准邮箱格式验证
- **扩展字段**：
  - 母语选择
  - 学习语言选择（可多选）
  - 初始等级测试

#### 1.2 用户登录
- **登录方式**：
  - 用户名/邮箱 + 密码
  - 验证码登录
- **安全机制**：
  - 登录失败锁定（5次失败锁定30分钟）
  - JWT Token认证
  - Token刷新机制
  - 登录日志记录

#### 1.3 多语言支持
- **支持语言**：英语(en)、日语(ja)、汉语(zh)
- **实现方式**：
  - 前端国际化(i18n)
  - 后端资源文件
  - 数据库语言字段

### 2. 分级课程体系

#### 2.1 课程结构
```
课程 (Course)
├── 单元 (Unit)
│   ├── 课时 (Lesson)
│   │   ├── 单词学习 (Vocabulary)
│   │   ├── 语法学习 (Grammar)
│   │   ├── 听力训练 (Listening)
│   │   ├── 口语练习 (Speaking)
│   │   └── 综合测试 (Quiz)
```

#### 2.2 等级体系
- **Beginner（初学者）**：A1级别，适合零基础
- **Elementary（初级）**：A2级别，掌握基础
- **Intermediate（中级）**：B1-B2级别，能够日常交流
- **Advanced（高级）**：C1-C2级别，接近母语水平

#### 2.3 课程类型
- **通用课程**：综合语言能力提升
- **专项课程**：
  - 词汇专项
  - 语法专项
  - 听力专项
  - 口语专项
  - 会话专项

### 3. 互动式学习模块

#### 3.1 单词记忆
- **学习模式**：
  - 闪卡记忆法
  - 间隔重复算法(SRS)
  - 联想记忆
  - 词根词缀法
- **练习模式**：
  - 选词填空
  - 看词选义
  - 听音辨词
  - 拼写测试
- **智能功能**：
  - 艾宾浩斯遗忘曲线
  - 自适应难度调整
  - 生词本管理

#### 3.2 语法练习
- **练习类型**：
  - 选择题（单选/多选）
  - 填空题
  - 改错题
  - 句子翻译
  - 完形填空
- **即时反馈**：
  - 正确答案显示
  - 详细解释
  - 相关知识点链接
  - 类似题目推荐

#### 3.3 口语跟读
- **功能特性**：
  - AI语音识别
  - 发音评分系统
  - 语调纠正
  - 语速控制
  - 录音回放
- **练习形式**：
  - 跟读模仿
  - 对话练习
  - 角色扮演
  - 情景模拟

#### 3.4 听力训练
- **资源类型**：
  - 日常对话
  - 短篇故事
  - 新闻资讯
  - 播客节目
- **练习模式**：
  - 听力理解测试
  - 听写练习
  - 选择题测试
  - 填空练习
- **辅助功能**：
  - 语速调节(0.5x-2.0x)
  - 原文显示/隐藏
  - 单词点读

#### 3.5 闯关天梯
- **系统设计**：
  - 100个关卡等级
  - 3星评价体系
  - 时间限制挑战
  - 技能综合考核
- **奖励机制**：
  - 经验值奖励
  - 金币奖励
  - 成就解锁
  - 排行榜积分
- **社交功能**：
  - 好友排行榜
  - 公会系统
  - 挑战邀请

### 4. 学习进度追踪

#### 4.1 数据采集
- **学习行为数据**：
  - 学习时长
  - 完成课程数
  - 练习正确率
  - 闯关成绩
  - 学习频率
- **采集频率**：实时采集，按天/周/月统计

#### 4.2 进度展示
- **学习看板**：
  - 今日学习时长
  - 连续学习天数
  - 当前等级进度
  - 技能雷达图
  - 学习数据统计
- **详细报告**：
  - 周/月学习报告
  - 薄弱点分析
  - 学习效率评估
  - 成长曲线图

#### 4.3 数据分析
- **学习模式分析**：
  - 最佳学习时段
  - 薄弱知识点
  - 学习偏好
- **智能建议**：
  - 个性化学习计划
  - 复习提醒
  - 薄弱点强化

### 5. 社区交流与成就激励

#### 5.1 社区功能
- **帖子类型**：
  - 学习心得分享
  - 问题求助
  - 经验技巧
  - 成就展示
- **互动功能**：
  - 评论回复
  - 点赞收藏
  - @提及
  - 话题标签
- **内容管理**：
  - 内容审核
  - 举报处理
  - 精华帖推荐
  - 用户等级权限

#### 5.2 成就系统
- **成就类型**：
  - 学习成就（完成课程、达成目标）
  - 连续成就（坚持学习天数）
  - 分数成就（考试成绩、正确率）
  - 社交成就（互动、分享）
  - 收集成就（收集徽章、词汇）
- **奖励机制**：
  - 经验值奖励
  - 金币奖励
  - 虚拟徽章
  - 排行榜积分

#### 5.3 徽章系统
- **徽章等级**：
  - 青铜（常见）
  - 白银（稀有）
  - 黄金（珍稀）
  - 钻石（传说）
  - 传奇（绝世）
- **展示功能**：
  - 个人主页展示
  - 排行榜标识
  - 动态徽章墙

#### 5.4 排行榜系统
- **排行类型**：
  - 总积分榜
  - 今日学习榜
  - 闯关排行榜
  - 连续学习榜
  - 社区活跃榜
- **周期设置**：日榜、周榜、月榜、总榜

### 6. 个性化学习路径

#### 6.1 智能推荐
- **推荐算法**：
  - 水平测试 + 学习目标
  - 学习历史分析
  - 薄弱点识别
  - 学习偏好匹配
- **路径生成**：
  - 短期目标（每日任务）
  - 中期目标（周/月计划）
  - 长期目标（等级提升）

#### 6.2 自适应学习
- **难度调整**：
  - 根据正确率自动调整
  - 学习曲线分析
  - 遗忘曲线复习
- **个性化内容**：
  - 兴趣话题推荐
  - 学习风格适配
  - 时间安排优化

---

## API接口设计

### 1. 用户相关接口

#### 1.1 用户注册
```
POST /api/edu/user/register
Request:
{
  "username": "string",
  "password": "string",
  "email": "string",
  "nativeLanguage": "zh",
  "learningLanguages": ["en", "ja"]
}

Response:
{
  "code": 200,
  "msg": "注册成功",
  "data": {
    "userId": 1,
    "token": "jwt_token"
  }
}
```

#### 1.2 用户登录
```
POST /api/edu/user/login
Request:
{
  "username": "string",
  "password": "string"
}

Response:
{
  "code": 200,
  "msg": "登录成功",
  "data": {
    "userId": 1,
    "username": "string",
    "token": "jwt_token",
    "profile": {...}
  }
}
```

#### 1.3 获取用户信息
```
GET /api/edu/user/info
Headers: Authorization: Bearer {token}

Response:
{
  "code": 200,
  "data": {
    "userId": 1,
    "username": "string",
    "email": "string",
    "nativeLanguage": "zh",
    "learningLanguages": ["en", "ja"],
    "totalStudyTime": 1200,
    "currentStreak": 7,
    "totalPoints": 5000,
    "level": 5,
    "achievements": [...],
    "badges": [...]
  }
}
```

### 2. 课程相关接口

#### 2.1 获取课程列表
```
GET /api/edu/course/list
Parameters:
  - language: en|ja|zh
  - level: beginner|elementary|intermediate|advanced
  - pageNum: 1
  - pageSize: 10

Response:
{
  "code": 200,
  "data": {
    "total": 100,
    "list": [
      {
        "courseId": 1,
        "courseName": "英语入门",
        "language": "en",
        "level": "beginner",
        "coverImage": "url",
        "totalLessons": 50,
        "totalStudents": 1000,
        "rating": 4.8,
        "isFree": true
      }
    ]
  }
}
```

#### 2.2 获取课程详情
```
GET /api/edu/course/{courseId}

Response:
{
  "code": 200,
  "data": {
    "courseId": 1,
    "courseName": "英语入门",
    "description": "...",
    "units": [
      {
        "unitId": 1,
        "unitName": "第一单元",
        "lessons": [
          {
            "lessonId": 1,
            "lessonName": "音标学习",
            "lessonType": "vocabulary",
            "duration": 15,
            "isLocked": false
          }
        ]
      }
    ],
    "progress": {
      "completedLessons": 10,
      "totalProgress": 20
    }
  }
}
```

### 3. 学习相关接口

#### 3.1 开始学习
```
POST /api/edu/learning/start
Request:
{
  "lessonId": 1,
  "courseId": 1
}

Response:
{
  "code": 200,
  "data": {
    "recordId": 1,
    "content": {...}
  }
}
```

#### 3.2 提交学习结果
```
POST /api/edu/learning/submit
Request:
{
  "recordId": 1,
  "lessonId": 1,
  "score": 85,
  "duration": 10,
  "answers": [...]
}

Response:
{
  "code": 200,
  "data": {
    "xpEarned": 50,
    "coinsEarned": 20,
    "newAchievements": [...],
    "nextLessonId": 2
  }
}
```

#### 3.3 获取学习进度
```
GET /api/edu/learning/progress

Response:
{
  "code": 200,
  "data": {
    "todayStudyTime": 30,
    "weeklyStudyTime": 180,
    "currentStreak": 7,
    "totalCourses": 5,
    "completedCourses": 2,
    "skillRadar": {
      "vocabulary": 75,
      "grammar": 60,
      "listening": 80,
      "speaking": 55
    }
  }
}
```

### 4. 闯关相关接口

#### 4.1 获取关卡信息
```
GET /api/edu/challenge/{challengeId}

Response:
{
  "code": 200,
  "data": {
    "challengeId": 1,
    "stageName": "第一关",
    "stageLevel": 1,
    "language": "en",
    "difficulty": 1,
    "timeLimit": 300,
    "questions": [...],
    "rewards": {
      "xp": 100,
      "coins": 50
    }
  }
}
```

#### 4.2 提交闯关结果
```
POST /api/edu/challenge/submit
Request:
{
  "challengeId": 1,
  "score": 90,
  "timeSpent": 180,
  "answers": [...]
}

Response:
{
  "code": 200,
  "data": {
    "passed": true,
    "stars": 3,
    "xpEarned": 100,
    "coinsEarned": 50,
    "newAchievement": {...},
    "nextChallenge": {...}
  }
}
```

### 5. 社区相关接口

#### 5.1 发布帖子
```
POST /api/edu/community/post
Request:
{
  "postType": "discussion",
  "language": "en",
  "title": "学习心得分享",
  "content": "...",
  "tags": ["英语", "学习技巧"]
}

Response:
{
  "code": 200,
  "msg": "发布成功",
  "data": {
    "postId": 1
  }
}
```

#### 5.2 获取帖子列表
```
GET /api/edu/community/posts
Parameters:
  - language: en|ja|zh
  - postType: discussion|question|tip|share
  - pageNum: 1
  - pageSize: 10

Response:
{
  "code": 200,
  "data": {
    "total": 100,
    "list": [
      {
        "postId": 1,
        "userId": 1,
        "username": "学习达人",
        "title": "...",
        "content": "...",
        "viewCount": 100,
        "likeCount": 20,
        "commentCount": 5,
        "createTime": "2024-01-01 10:00:00"
      }
    ]
  }
}
```

### 6. 成就相关接口

#### 6.1 获取用户成就
```
GET /api/edu/achievement/list

Response:
{
  "code": 200,
  "data": {
    "achievements": [
      {
        "achievementId": 1,
        "achievementName": "学习达人",
        "description": "累计学习100小时",
        "iconUrl": "url",
        "tier": "gold",
        "progress": 80,
        "isCompleted": false
      }
    ],
    "totalAchievements": 20,
    "completedAchievements": 5
  }
}
```

#### 6.2 获取排行榜
```
GET /api/edu/achievement/leaderboard
Parameters:
  - type: total|weekly|daily
  - language: en|ja|zh|all
  - pageNum: 1
  - pageSize: 20

Response:
{
  "code": 200,
  "data": {
    "rankings": [
      {
        "rank": 1,
        "userId": 1,
        "username": "学习王者",
        "avatar": "url",
        "totalPoints": 10000,
        "level": 15,
        "badge": "..."
      }
    ],
    "myRank": 50
  }
}
```

---

## 前端页面设计

### 1. 页面结构

#### 1.1 PC端页面
```
├── 首页
│   ├── Banner轮播
│   ├── 推荐课程
│   ├── 学习统计
│   └── 排行榜入口
├── 课程中心
│   ├── 课程分类筛选
│   ├── 课程列表
│   └── 课程详情
├── 学习中心
│   ├── 今日任务
│   ├── 学习进度
│   └── 闯关挑战
├── 社区
│   ├── 帖子列表
│   ├── 帖子详情
│   ├── 发布帖子
│   └── 我的帖子
├── 个人中心
│   ├── 学习报告
│   ├── 成就展示
│   ├── 学习设置
│   └── 账号设置
└── 管理后台
    ├── 课程管理
    ├── 用户管理
    ├── 数据统计
    └── 系统设置
```

#### 1.2 移动端页面
```
├── 首页（底部导航）
│   ├── 学习
│   ├── 课程
│   ├── 社区
│   ├── 我的
│   └── 管理（管理员）
├── 学习页面
│   ├── 单词学习
│   ├── 语法练习
│   ├── 听力训练
│   └── 口语跟读
├── 闯关页面
│   ├── 关卡列表
│   ├── 排行榜
│   └── 挑战历史
└── 个人中心
    ├── 学习数据
    ├── 成就徽章
    └── 设置
```

### 2. 响应式设计
- **桌面端**：1920px以上，全功能展示
- **平板端**：768px-1919px，简化布局
- **手机端**：320px-767px，移动优先设计

### 3. 核心组件设计
- 学习卡片组件
- 课程卡片组件
- 闯关挑战组件
- 成就展示组件
- 社区帖子组件
- 学习进度环形图
- 技能雷达图
- 排行榜列表组件

---

## Git版本控制策略

### 1. 分支管理
```
main (主分支 - 生产环境)
├── develop (开发分支)
│   ├── feature/用户认证模块
│   ├── feature/课程管理模块
│   ├── feature/学习模块
│   ├── feature/闯关模块
│   ├── feature/社区模块
│   └── feature/成就系统
├── release (发布分支)
└── hotfix (热修复分支)
```

### 2. 提交规范
```
feat: 新功能
fix: 修复bug
docs: 文档更新
style: 代码格式调整
refactor: 重构
perf: 性能优化
test: 测试相关
chore: 构建/工具相关
```

### 3. 版本号规范
- 主版本号.次版本号.修订号
- 例如：1.0.0, 1.1.0, 1.0.1

---

## 开发计划

### 第一阶段：基础架构搭建（1-2周）
1. 项目初始化和配置
2. 数据库设计和创建
3. 基础模块开发（用户、认证）
4. Git版本控制初始化

### 第二阶段：核心功能开发（3-6周）
1. 课程管理模块
2. 学习模块开发
3. 闯关天梯系统
4. 前后端接口对接

### 第三阶段：社交和激励系统（7-8周）
1. 社区交流功能
2. 成就徽章系统
3. 排行榜系统
4. 消息通知系统

### 第四阶段：个性化推荐（9-10周）
1. 学习路径推荐
2. 自适应学习算法
3. 数据分析和报表
4. 个性化设置

### 第五阶段：优化和测试（11-12周）
1. 性能优化
2. 移动端适配
3. 全系统测试
4. 文档完善
5. 部署上线

---

## 测试策略

### 1. 单元测试
- Service层测试覆盖率 > 80%
- 核心业务逻辑测试
- 边界条件测试

### 2. 集成测试
- API接口测试
- 数据库操作测试
- 缓存功能测试

### 3. 系统测试
- 功能完整性测试
- 用户场景测试
- 性能压力测试
- 安全测试

### 4. 兼容性测试
- 浏览器兼容性（Chrome, Firefox, Safari, Edge）
- 操作系统兼容性（Windows, macOS, Linux, iOS, Android）
- 响应式布局测试

---

## 性能优化

### 1. 后端优化
- Redis缓存热点数据
- 数据库索引优化
- 异步任务处理
- 接口响应时间 < 200ms

### 2. 前端优化
- 路由懒加载
- 图片懒加载
- 组件缓存
- CDN加速
- Gzip压缩

### 3. 数据库优化
- 读写分离
- 分表策略
- SQL优化
- 索引优化

---

## 安全措施

### 1. 认证授权
- JWT Token认证
- 角色权限控制
- 接口访问限制
- CSRF防护

### 2. 数据安全
- 密码加密存储
- SQL注入防护
- XSS攻击防护
- 敏感数据加密

### 3. 基础设施
- HTTPS加密传输
- 防火墙配置
- DDoS防护
- 日志审计

---

## 部署架构

### 1. 开发环境
- 开发服务器：本地开发
- 数据库：MySQL本地
- 缓存：Redis本地

### 2. 测试环境
- 应用服务器：测试服务器
- 数据库：测试数据库
- 缓存：测试Redis

### 3. 生产环境
- 应用服务器：Nginx + 多实例部署
- 数据库：MySQL主从复制
- 缓存：Redis集群
- CDN：静态资源加速
- OSS：文件存储服务

---

## 监控和运维

### 1. 日志管理
- 应用日志：ELK日志收集
- 访问日志：Nginx日志
- 错误日志：实时告警

### 2. 性能监控
- APM工具：SkyWalking
- 监控系统：Prometheus + Grafana
- 业务监控：自定义埋点

### 3. 告警机制
- 邮件告警
- 短信告警
- 即时通讯告警

---

## 文档清单

1. [x] 数据库设计文档
2. [x] 开发文档（本文档）
3. [ ] API接口文档
4. [ ] 前端组件文档
5. [ ] 部署文档
6. [ ] 用户手册
7. [ ] 管理员手册
8. [ ] 测试报告

---

## 更新日志

### Version 1.0.0 (2024-01-15)
- 完成数据库设计
- 完成开发文档编写
- 开始项目初始化

---

## 联系方式

- 项目负责人：[待定]
- 技术支持：[待定]
- 用户反馈：[待定]

---

*本文档最后更新于：2024年1月15日*
