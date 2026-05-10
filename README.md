# 多语种在线教育平台 - LinguaLearn

<p align="center">
  <img src="https://img.shields.io/badge/Version-1.0.0-brightgreen" alt="Version">
  <img src="https://img.shields.io/badge/Java-1.8-blue" alt="Java">
  <img src="https://img.shields.io/badge/Spring%20Boot-2.5.15-blue" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Vue-3.x-green" alt="Vue">
  <img src="https://img.shields.io/badge/License-MIT-orange" alt="License">
</p>

## 📚 项目简介

**LinguaLearn** 是一款基于 **RuoYi** 框架二次开发的多语种在线教育平台，支持 **英语、日语、汉语** 等主流语言的学习。平台致力于为用户提供沉浸式的语言学习体验，涵盖从单词记忆、语法练习到口语跟读、听力训练的全方位学习功能。

### 🌟 核心特性

- 🎯 **分级课程体系** - 从初学者到高级，系统化学习路径
- 📝 **互动式学习模块** - 单词记忆、语法练习、口语跟读、听力训练、闯关天梯
- 📊 **学习进度追踪** - 实时掌握学习数据，智能分析薄弱环节
- 🔐 **用户注册登录** - 安全可靠的用户认证系统
- 🎁 **个性化推荐** - AI驱动的学习路径智能推荐
- 👥 **社区交流** - 学习心得分享，问题互助
- 🏆 **成就激励** - 徽章、排行榜、连续学习奖励

## 🏗️ 技术架构

### 后端技术栈
- **核心框架**: Spring Boot 2.5.15
- **安全框架**: Spring Security + JWT
- **数据库**: MySQL 8.0
- **ORM框架**: MyBatis
- **缓存**: Redis
- **构建工具**: Maven

### 前端技术栈
- **核心框架**: Vue 3
- **UI组件**: Element Plus
- **状态管理**: Pinia
- **路由**: Vue Router
- **构建工具**: Vite
- **HTTP客户端**: Axios

### 系统架构图
```
┌─────────────────────────────────────────────────────────┐
│                    客户端层 (Web + Mobile)               │
├─────────────────────────────────────────────────────────┤
│                    前端表现层 (Vue 3)                    │
├─────────────────────────────────────────────────────────┤
│                    API网关层 (Spring Security + JWT)      │
├─────────────────────────────────────────────────────────┤
│                    业务逻辑层 (Spring Boot)               │
│    ├── 用户服务 (User Service)                           │
│    ├── 课程服务 (Course Service)                        │
│    ├── 学习服务 (Learning Service)                       │
│    ├── 闯关服务 (Challenge Service)                     │
│    ├── 社区服务 (Community Service)                      │
│    └── 成就服务 (Achievement Service)                    │
├─────────────────────────────────────────────────────────┤
│                    数据访问层 (MyBatis + Redis)           │
│    ├── MySQL 数据库                                      │
│    └── Redis 缓存                                       │
└─────────────────────────────────────────────────────────┘
```

## 📦 项目结构

```
RuoYi-Vue-master/
├── ruoyi-admin/          # 主应用模块
├── ruoyi-framework/      # 框架核心模块
├── ruoyi-system/         # 系统业务模块
│   └── src/main/java/com/ruoyi/
│       ├── edu/          # 教育平台核心模块
│       │   ├── controller/  # 控制器层
│       │   ├── service/     # 服务层
│       │   ├── mapper/      # 数据访问层
│       │   └── domain/     # 实体层
│       └── system/       # 系统模块
├── ruoyi-common/        # 通用工具模块
├── ruoyi-quartz/        # 定时任务模块
├── ruoyi-generator/     # 代码生成器模块
└── doc/                 # 项目文档
    ├── database_design.md      # 数据库设计文档
    ├── development_guide.md     # 开发指南
    └── database_init.sql        # 数据库初始化脚本
```

## 🚀 快速开始

### 环境要求
- JDK 1.8+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+
- Node.js 16+ (前端开发)

### 数据库配置

1. **创建数据库**:
```bash
mysql -u root -p < doc/database_init.sql
```

2. **修改配置文件** `ruoyi-admin/src/main/resources/application-druid.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/lingua_learn?useUnicode=true&characterEncoding=utf8
    username: root
    password: your_password
```

### 编译项目

```bash
# 编译后端
cd ruoyi-admin
mvn clean package -DskipTests

# 运行项目
java -jar target/ruoyi-admin.jar
```

### 访问项目

- **管理后台**: http://localhost:80
- **默认账号**: admin / admin123

## 📖 核心功能模块

### 1️⃣ 用户认证与多语言支持
- ✅ 支持邮箱、手机号注册
- ✅ JWT Token认证
- ✅ 多语言界面支持
- ✅ 个性化学习语言设置

### 2️⃣ 分级课程体系
- ✅ 课程分类管理（英语、日语、汉语）
- ✅ 课程等级划分（Beginner → Advanced）
- ✅ 单元-课时结构
- ✅ 课程进度追踪

### 3️⃣ 互动式学习模块
- ✅ **单词记忆**: 闪卡记忆、间隔重复、联想记忆
- ✅ **语法练习**: 选择题、填空题、改错题
- ✅ **口语跟读**: AI语音识别、发音评分
- ✅ **听力训练**: 多倍速播放、听力理解测试
- ✅ **闯关天梯**: 100级闯关、3星评价体系

### 4️⃣ 学习进度追踪
- ✅ 实时学习时长统计
- ✅ 连续学习天数追踪
- ✅ 技能雷达图展示
- ✅ 学习数据报告

### 5️⃣ 社区交流
- ✅ 帖子发布与浏览
- ✅ 评论与回复
- ✅ 点赞与收藏
- ✅ 话题标签分类

### 6️⃣ 成就激励系统
- ✅ 多类型成就（学习、连续、社交、收集）
- ✅ 徽章收集系统
- ✅ 排行榜功能（总榜、周榜、日榜）
- ✅ 积分与经验奖励

## 🎨 数据库设计

平台采用 **23张核心数据表**，涵盖：

| 模块 | 表数量 | 主要表 |
|------|--------|--------|
| 用户系统 | 2 | edu_user_profile, edu_user_course |
| 课程系统 | 4 | edu_course, edu_course_unit, edu_course_lesson, edu_course_category |
| 学习资源 | 3 | edu_vocabulary, edu_grammar, edu_listening_resource |
| 学习记录 | 2 | edu_learning_record, edu_learning_progress |
| 闯关系统 | 2 | edu_ladder_challenge, edu_user_challenge |
| 成就系统 | 4 | edu_achievement, edu_badge, edu_user_achievement, edu_user_badge |
| 社区系统 | 3 | edu_post, edu_post_comment, edu_user_like |
| 学习路径 | 2 | edu_learning_path, edu_user_learning_path |
| 其他 | 1 | edu_speaking_practice |

详细设计请查看 [数据库设计文档](./doc/database_design.md)

## 📡 API接口

### 用户接口
```
POST /edu/user/register      - 用户注册
POST /edu/user/login         - 用户登录
GET  /edu/user/info          - 获取用户信息
PUT  /edu/user/profile       - 修改用户信息
```

### 课程接口
```
GET  /edu/course/list        - 获取课程列表
GET  /edu/course/{id}        - 获取课程详情
GET  /edu/course/featured    - 获取精选课程
POST /edu/course/enroll/{id} - 报名课程
```

### 学习接口
```
POST /edu/learning/start     - 开始学习
POST /edu/learning/submit    - 提交学习结果
GET  /edu/learning/stats     - 获取学习统计
GET  /edu/learning/today     - 获取今日学习数据
```

### 社区接口
```
POST /edu/community/post     - 发布帖子
GET  /edu/community/post/{id} - 获取帖子详情
POST /edu/community/comment - 添加评论
POST /edu/community/post/{id}/like - 点赞帖子
```

### 成就接口
```
GET  /edu/achievement/my     - 获取我的成就
GET  /edu/achievement/leaderboard - 获取排行榜
```

## 🛠️ 开发指南

详细的开发指南请查看 [开发文档](./doc/development_guide.md)

### 代码规范
- **命名规范**: 遵循Java命名规范
- **注释要求**: 关键业务逻辑必须添加注释
- **提交规范**: 使用Git提交，遵循Conventional Commits

### Git提交规范
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

## 📊 性能优化

### 后端优化
- Redis缓存热点数据
- 数据库索引优化
- 异步任务处理
- 接口响应时间 < 200ms

### 前端优化
- 路由懒加载
- 图片压缩优化
- 组件按需加载
- CDN加速

## 🔒 安全措施

- ✅ JWT Token认证
- ✅ RBAC权限控制
- ✅ SQL注入防护
- ✅ XSS攻击防护
- ✅ CSRF令牌验证
- ✅ 密码加密存储

## 📝 开发文档

1. [数据库设计文档](./doc/database_design.md) - 完整的数据库表结构设计
2. [开发指南](./doc/development_guide.md) - 详细的开发规范和接口文档
3. [数据库初始化脚本](./doc/database_init.sql) - 完整的建表SQL和数据初始化

## 🤝 贡献指南

欢迎提交Issue和Pull Request！

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件

## 👥 开发团队

- **项目负责人**: [待定]
- **技术架构**: Spring Boot + Vue 3
- **开发周期**: 12周

## 🙏 致谢

- 感谢 [RuoYi](https://gitee.com/y_project/RuoYi-Vue) 框架提供的基础架构
- 感谢所有开源项目的贡献者

---

<p align="center">
  <strong>LinguaLearn - 让语言学习更简单</strong>
  <br>
  <em>Built with ❤️ using RuoYi + Vue</em>
</p>
