# 多语种学习平台后台管理系统

## 📖 项目概述

本项目是基于 RuoYi 框架专门为多语种在线教育平台开发的后台管理系统，支持英语、日语、汉语三种语言的学习课程管理。系统采用双前端架构：
- **LinguaLearn 管理前端**：基于 Vue 3 + Vite 的现代化管理界面 (`frontend`目录)
- **RuoYi Admin 系统前端**：基于 Vue 2 + Element UI 的传统若依管理界面 (`ruoyi-ui`目录)

## ✨ 核心功能

### 📊 数据仪表盘
- **用户统计**：实时查看平台总用户数、活跃用户数
- **课程统计**：课程总数、语言分布、热门课程、学习人数
- **社区统计**：帖子总数、互动数据、审核状态
- **学习分析**：学习时长分布、完成率趋势、难度分析

### 📚 课程内容管理系统
- ✅ **课程管理**：完整的CRUD功能，支持多语言、多难度等级、课程类型分类
- ✅ **章节管理**：课程章节结构化管理，支持排序和描述
- ✅ **单元管理**：学习单元配置，包含经验奖励、及格分数等设置
- ✅ **课时管理**：详细的课时内容管理，支持多种课时类型（词汇、语法、听力、口语、测验、视频）
- ✅ **封面与媒体**：课程封面图片上传、预告视频管理
- ✅ **定价策略**：免费/付费课程配置，价格管理

### 👥 学习数据管理
- ✅ 用户学习进度追踪
- ✅ 学习时长统计分析
- ✅ 课程完成率监控
- ✅ 学习历史记录查看
- ✅ 积分和成就统计
- ✅ 学习数据可视化图表

### 💬 社区内容管理
- ✅ 帖子审核管理
- ✅ 评论和回复管理
- ✅ 多语言内容筛选
- ✅ 内容质量控制
- ✅ 批量操作支持
- ✅ 点赞和互动统计

### 🏆 成就系统管理
- ✅ 成就的增删改查
- ✅ 成就类型分类（学习/社区/系统）
- ✅ 成就图标管理
- ✅ 积分规则配置
- ✅ 获得人数统计
- ✅ 用户成就排行榜

### 📥 数据导入导出
- ✅ 词汇批量导入
- ✅ 课程数据导出
- ✅ 学习数据备份
- ✅ 社区内容迁移

## 🏗️ 系统架构

```
┌─────────────────────────────────────────┐
│     LinguaLearn 管理前端 (Vue 3 + Vite)   │
│         http://localhost           │
│         (frontend 目录)                 │
├─────────────────────────────────────────┤
│      RuoYi Admin 系统前端 (Vue 2)        │
│         http://localhost:66             │
│         (ruoyi-ui 目录)                 │
└────────────────┬────────────────────────┘
                 │ HTTP/REST API
┌────────────────▼────────────────────────┐
│          Spring Boot 后端 (v2.5.15)      │
│        http://localhost:6666            │
│        (ruoyi-admin 目录)               │
│                                          │
│  ┌─────────────────────────────────┐    │
│  │    教育平台业务控制器             │    │
│  │  - EduMenuController           │    │
│  │  - EduImportExportController   │    │
│  │  - EduPostController           │    │
│  │  - EduVocabularyController     │    │
│  └─────────────────────────────────┘    │
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│              MySQL 数据库 (utf8mb4)       │
│  ┌─────────────────────────────────┐    │
│  │    教育业务相关表                 │    │
│  │  - edu_course                  │    │
│  │  - edu_course_chapter          │    │
│  │  - edu_course_unit             │    │
│  │  - edu_course_lesson           │    │
│  │  - edu_post                    │    │
│  │  - edu_vocabulary              │    │
│  └─────────────────────────────────┘    │
└─────────────────────────────────────────┘
```

## 📁 项目文件结构

```
RuoYi-Vue-master/
│
├── frontend/                             # LinguaLearn 管理前端 (Vue 3 + Vite)
│   ├── src/views/
│   │   ├── course/                      # 课程管理页面
│   │   ├── learning/                    # 学习数据管理
│   │   ├── community/                   # 社区内容管理
│   │   ├── achievement/                 # 成就系统管理
│   │   ├── unit/                        # 章节单元管理
│   │   ├── lesson/                      # 课时内容管理
│   │   ├── vocabulary/                  # 词汇管理
│   │   └── import-export/               # 导入导出管理
│   └── package.json                     # 前端依赖配置 (Vue 3)
│
├── ruoyi-ui/                             # RuoYi Admin 系统前端 (Vue 2 + Vue CLI)
│   └── src/views/                       # 若依系统管理页面
│       ├── index.vue                    # 系统首页
│       └── system/                      # 系统管理模块
│   └── package.json                     # 前端依赖配置 (Vue 2)
│
├── ruoyi-admin/                          # 后端Spring Boot应用
│   └── src/main/java/com/ruoyi/web/controller/edu/
│       ├── EduMenuController.java        # 教育平台菜单路由
│       ├── EduImportExportController.java # 导入导出控制器
│       ├── EduPostController.java        # 社区帖子控制器
│       └── EduVocabularyController.java  # 词汇管理控制器
│
├── ruoyi-system/                         # 系统业务模块
│   └── src/main/java/com/ruoyi/edu/     # 教育业务逻辑和服务
│
└── sql/
    └── all_db_backup.sql               # 完整数据库
```

## 🚀 快速部署

### 环境要求
- **JDK 1.8+** (推荐JDK 8或11)
- **Node.js 16+** (package.json指定>=16.0.0)
- **MySQL 5.7+** (支持utf8mb4字符集)
- **Maven 3.0+**
- **Redis 3.0+** (用于缓存)

### 步骤 1：初始化数据库

```sql
-- 创建数据库（确保使用utf8mb4字符集）
CREATE DATABASE ryvue CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 登录 MySQL
mysql -u root -p

-- 选择数据库
USE ryvue;

-- 执行表结构脚本
SOURCE /path/to/edu_tables.sql;

-- 执行菜单初始化脚本
SOURCE /path/to/edu_menu_init.sql;

-- （可选）导入示例数据
SOURCE /path/to/edu_sample_data.sql;
```

### 步骤 2：配置后端

编辑配置文件：
```yaml
# ruoyi-admin/src/main/resources/application-druid.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ryvue?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
    username: root
    password: your_password
```

### 步骤 3：启动所有服务（推荐方式）

```bash
# 在项目根目录执行一键启动脚本
start.bat
```

### 步骤 4：手动启动各服务

#### 启动后端服务
```bash
# 在项目根目录执行
mvn clean package -DskipTests

# 启动应用
java -jar ruoyi-admin/target/ruoyi-admin.jar
```

后端启动后访问：`http://localhost:6666`

#### 启动 LinguaLearn 管理前端
```bash
# 进入前端目录
cd frontend

# 安装依赖
npm install

# 开发模式启动
npm run dev
```

LinguaLearn前端启动后访问：`http://localhost:3000`

#### 启动 RuoYi Admin 系统前端
```bash
# 进入系统前端目录
cd ruoyi-ui

# 安装依赖
npm install

# 开发模式启动
npm run dev
```

RuoYi Admin前端启动后访问：`http://localhost:66`

### 步骤 5：登录系统

- **管理员账号**：admin
- **管理员密码**：admin123

⚠️ **重要提示**：首次登录后请立即修改默认密码！

## 📱 功能演示

### 访问地址说明

| 系统 | 访问地址 | 目录 | 说明 |
|------|----------|------|------|
| **LinguaLearn 管理前端** | http://localhost | `frontend` | 专门的教育平台管理界面，包含所有教育相关功能 |
| **RuoYi Admin 系统前端** | http://localhost:66 | `ruoyi-ui` | 传统的若依系统管理界面，包含用户、角色、菜单等系统管理功能 |
| **后端API** | http://localhost:6666 | `ruoyi-admin` | Spring Boot后端服务 |

### LinguaLearn 管理前端功能

登录后的首页展示了：
1. 四大统计数据卡片（用户、课程、帖子、成就）
2. 学习数据趋势折线图
3. 课程语言分布饼图
4. 最新注册用户列表
5. 热门课程排行榜
6. 学习排行榜 TOP 10
7. 最新社区帖子
8. 平台使用指南

### 课程内容管理

- **四级结构管理**：课程 → 章节 → 单元 → 课时
- **多维度筛选**：按课程名称、语言(en/ja/zh)、难度(beginner/elementary/intermediate/advanced)、类型筛选
- **媒体管理**：封面图片上传、预告视频URL配置
- **定价策略**：免费/付费课程切换，价格设置
- **发布控制**：草稿/发布状态管理，精选课程标记

### 学习数据追踪

- 查看所有用户的学习进度详情
- 柱状图展示课程完成率分布
- 饼图展示不同语言的学习时长占比
- 支持查看用户详细学习历史和经验获取
- 可重置用户的学习进度和成就

### 社区审核流程

- 待审核帖子会高亮显示
- 支持批量审核通过或拒绝
- 点击「查看」可审核帖子内容和评论
- 可删除违规评论和帖子
- 支持按语言和内容类型筛选

### 词汇管理

- 词汇批量导入功能
- 支持多语言词汇管理
- 词汇分类和标签管理
- 词汇难度等级设置
- 词汇使用统计

### 数据导入导出

- 课程数据Excel导出
- 词汇CSV批量导入
- 学习数据JSON导出
- 社区内容备份功能

## 🔌 API 接口说明

### 获取教育平台菜单

```
GET /edu/menu/getRouters
Response: 菜单路由列表
```

### 课程管理接口

```
GET    /edu/course/list          # 获取课程列表
POST   /edu/course               # 新增课程
PUT    /edu/course               # 修改课程
DELETE /edu/course/{id}          # 删除课程
GET    /edu/course/{id}          # 获取课程详情
```

### 章节/单元/课时管理

```
GET    /edu/chapter/list         # 获取章节列表
GET    /edu/unit/list            # 获取单元列表  
GET    /edu/lesson/list          # 获取课时列表
POST/PUT/DELETE 相应资源        # CRUD操作
```

### 学习数据接口

```
GET    /edu/learning/stats       # 获取学习统计数据
GET    /edu/learning/list        # 获取学习记录列表
GET    /edu/learning/user/{id}   # 获取用户学习详情
```

### 社区管理接口

```
GET    /edu/community/list       # 获取帖子列表
POST   /edu/community/audit      # 审核帖子
DELETE /edu/community/{id}       # 删除帖子
GET    /edu/community/{id}       # 获取帖子详情
```

### 词汇管理接口

```
GET    /edu/vocabulary/list      # 获取词汇列表
POST   /edu/vocabulary/import    # 批量导入词汇
GET    /edu/vocabulary/export    # 导出词汇数据
```

### 导入导出接口

```
POST   /edu/importexport/course  # 导入课程数据
GET    /edu/importexport/course  # 导出课程数据
POST   /edu/importexport/vocab   # 导入词汇数据
```

## 🗄️ 数据库设计

### 主要数据表

| 表名 | 说明 | 主要字段 |
|------|------|---------|
| edu_course | 课程表 | course_id, course_name, language, level, course_type, cover_image, is_free, price, is_published |
| edu_course_chapter | 课程章节表 | chapter_id, course_id, chapter_name, chapter_order, description |
| edu_course_unit | 课程单元表 | unit_id, course_id, chapter_id, unit_name, experience_reward, passing_score, is_locked |
| edu_course_lesson | 课程课时表 | lesson_id, unit_id, lesson_name, lesson_type(vocabulary/grammar/listening/speaking/quiz/video), content, duration |
| edu_post | 帖子表 | post_id, title, content, author_id, language, status |
| edu_vocabulary | 词汇表 | vocab_id, word, pronunciation, definition, language, difficulty_level, category |
| edu_learning_progress | 学习进度 | progress_id, user_id, course_id, chapter_id, unit_id, lesson_id, progress, score |

## 🔒 安全配置

### 权限控制

系统采用 RBAC（基于角色的访问控制）模型：

```yaml
权限标识:
  edu:course:list        # 课程查看
  edu:course:add         # 课程新增
  edu:course:edit        # 课程修改
  edu:course:remove      # 课程删除
  edu:chapter:list       # 章节管理
  edu:unit:list          # 单元管理
  edu:lesson:list        # 课时管理
  edu:vocabulary:list    # 词汇管理
  edu:importexport:list  # 导入导出管理
  edu:learning:list      # 学习数据查看
  edu:community:list     # 社区管理
  edu:achievement:list   # 成就管理
```

### 安全建议

1. ✅ 修改默认管理员密码
2. ✅ 启用登录验证码（配置kaptcha）
3. ✅ 配置登录失败锁定机制
4. ✅ 启用 HTTPS 访问（生产环境）
5. ✅ 定期备份数据库
6. ✅ 配置操作日志审计
7. ✅ 启用 XSS 防护（框架已内置）
8. ✅ 配置 SQL 注入过滤（MyBatis已防护）

## 🛠️ 维护指南

### 日志查看

```bash
# 查看应用日志
tail -f ruoyi-admin/logs/ruoyi.log

# 查看错误日志
grep "ERROR" ruoyi-admin/logs/ruoyi.log
```

### 性能优化

1. **数据库优化**
   - 定期分析慢查询日志
   - 为常用查询字段添加适当索引
   - 使用 Redis 缓存热点数据（课程列表、词汇数据等）

2. **前端优化**
   - 启用路由懒加载（已配置）
   - 压缩静态资源（构建时自动处理）
   - 启用 Gzip 压缩（Nginx配置）

3. **后端优化**
   - 配置 Druid 连接池大小
   - 启用异步处理（@Async注解）
   - 使用多级缓存（Redis + Caffeine）

### 故障排查

| 问题现象 | 可能原因 | 解决方案 |
|---------|---------|---------|
| 登录失败 | 数据库连接失败 | 检查application-druid.yml配置 |
| 菜单不显示 | 菜单未初始化 | 执行edu_menu_init.sql脚本 |
| 页面加载慢 | 网络问题或资源过大 | 检查网络配置，优化图片资源 |
| API 调用失败 | 权限不足 | 检查用户角色权限分配 |
| 图表不显示 | ECharts 未正确加载 | 检查node_modules依赖 |
| 字符乱码 | 数据库字符集问题 | 确保使用utf8mb4字符集 |

## 📞 技术支持

如遇到问题请按以下顺序排查：

1. 检查数据库是否正常运行（MySQL服务状态）
2. 检查后端应用是否正常启动（查看logs/ruoyi.log）
3. 检查前端依赖是否完整安装（npm install）
4. 查看浏览器开发者工具中的网络请求错误
5. 检查配置文件是否正确（数据库连接、端口等）

## 📄 技术栈版本

### 后端技术栈
- **Spring Boot**: 2.5.15
- **MyBatis**: 3.5.9
- **Druid**: 1.2.16
- **FastJSON**: 2.0.34
- **JWT**: 0.9.1

### 前端技术栈

#### LinguaLearn 管理前端 (frontend)
- **Vue**: 3.3.4
- **Vite**: 4.4.9
- **Element Plus**: 2.3.14
- **Pinia**: 2.1.6
- **ECharts**: 5.4.3
- **Axios**: 1.5.0

#### RuoYi Admin 系统前端 (ruoyi-ui)
- **Vue**: 2.6.14
- **Vue CLI**: 4.x
- **Element UI**: 2.15.x
- **Vuex**: 3.x
- **Axios**: 0.21.x

## 📄 开源协议

本项目基于 [RuoYi](https://gitee.com/y_project/RuoYi-Vue) 开源框架开发，遵循 Apache-2.0 开源协议。

## 🙏 致谢

- 感谢 [RuoYi](https://gitee.com/y_project/RuoYi-Vue) 团队的开源贡献
- 感谢 [Vue.js](https://vuejs.org/) 优秀的渐进式框架
- 感谢 [Element Plus](https://element-plus.org/) 现代化的UI组件库
- 感谢 [Element UI](https://element.eleme.cn/) 经典的UI组件库
- 感谢 [ECharts](https://echarts.apache.org/) 强大的图表库
- 感谢 [Vite](https://vitejs.dev/) 极速的前端构建工具

---

**版本**：2.1.0
**更新时间**：2026年5月18日
**开发团队**：LinguaLearn Development Team