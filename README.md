# 多语种学习平台后台管理系统

## 📖 项目概述

本项目是基于 RuoYi 框架专门为多语种在线教育平台开发的后台管理系统，支持英语、日语、汉语三种语言的学习课程管理。

## ✨ 核心功能

### 📊 数据仪表盘
- **用户统计**：实时查看平台总用户数、活跃用户数
- **课程统计**：课程总数、语言分布、热门课程
- **社区统计**：帖子总数、互动数据
- **学习分析**：学习时长分布、完成率趋势

### 📚 课程管理系统
- ✅ 课程 CRUD 完整功能
- ✅ 多语言支持（英语/日语/汉语）
- ✅ 难度等级管理（初级/中级/高级）
- ✅ 课程封面图片上传
- ✅ 学习人数统计
- ✅ 课程状态管理

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

## 🏗️ 系统架构

```
┌─────────────────────────────────────────┐
│           前端管理界面 (Vue 3)            │
│         http://localhost:80             │
└────────────────┬────────────────────────┘
                 │ HTTP/REST
┌────────────────▼────────────────────────┐
│          Spring Boot 后端               │
│        http://localhost:6666            │
│                                          │
│  ┌─────────────────────────────────┐    │
│  │    教育平台业务控制器             │    │
│  │  - EduMenuController           │    │
│  │  - EduCourseController         │    │
│  │  - EduLearningController       │    │
│  │  - EduCommunityController      │    │
│  │  - EduAchievementController    │    │
│  └─────────────────────────────────┘    │
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│              MySQL 数据库                 │
│  ┌─────────────────────────────────┐    │
│  │    教育业务相关表                 │    │
│  │  - edu_user_profile            │    │
│  │  - edu_course                  │    │
│  │  - edu_post                    │    │
│  │  - edu_achievement            │    │
│  │  - edu_learning_progress       │    │
│  └─────────────────────────────────┘    │
└─────────────────────────────────────────┘
```

## 📁 项目文件结构

```
RuoYi-Vue-master/
│
├── ruoyi-admin/                              # 后端管理模块
│   └── src/main/java/com/ruoyi/web/controller/edu/
│       └── EduMenuController.java            # 教育平台菜单路由
│
├── ruoyi-ui/                                 # 前端管理后台
│   └── src/views/
│       ├── index.vue                        # 首页仪表盘
│       └── edu/                             # 教育管理模块
│           ├── course/index.vue             # 课程管理页面
│           ├── learning/index.vue            # 学习数据管理
│           ├── community/index.vue           # 社区内容管理
│           └── achievement/index.vue         # 成就系统管理
│
├── ruoyi-system/                             # 系统业务模块
│   └── src/main/java/com/ruoyi/edu/         # 教育业务逻辑
│
└── sql/
    └── edu_menu_init.sql                    # 菜单初始化脚本
```

## 🚀 快速部署

### 环境要求
- JDK 1.8+
- Node.js 14+
- MySQL 5.7+
- Redis 3.0+
- Maven 3.0+

### 步骤 1：初始化数据库

```sql
-- 登录 MySQL
mysql -u root -p

-- 选择数据库
USE your_database_name;

-- 执行菜单初始化脚本
SOURCE /path/to/edu_menu_init.sql;
```

### 步骤 2：配置后端

编辑配置文件：
```yaml
# application-druid.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/your_database
    username: root
    password: your_password
```

### 步骤 3：启动后端服务

```bash
# 进入后端目录
cd ruoyi-admin

# 编译打包
mvn clean package

# 启动应用
java -jar target/ruoyi-admin.jar
```

后端启动后访问：`http://localhost:6666`

### 步骤 4：启动前端

```bash
# 进入前端目录
cd ruoyi-ui

# 安装依赖
npm install

# 开发模式启动
npm run dev
```

前端启动后访问：`http://localhost:80`

### 步骤 5：登录系统

- **管理员账号**：admin
- **管理员密码**：admin123

⚠️ **重要提示**：首次登录后请立即修改默认密码！

## 📱 功能演示

### 首页仪表盘预览

登录后的首页展示了：
1. 四大统计数据卡片（用户、课程、帖子、成就）
2. 学习数据趋势折线图
3. 课程语言分布饼图
4. 最新注册用户列表
5. 热门课程排行榜
6. 学习排行榜 TOP 10
7. 最新社区帖子
8. 平台使用指南

### 课程管理功能

- 点击「新增」按钮可添加新课程
- 支持按课程名称、语言、难度、状态筛选
- 点击「修改」可编辑课程信息
- 点击「详情」可查看完整课程信息
- 支持导出课程数据

### 学习数据追踪

- 查看所有用户的学习进度
- 柱状图展示课程完成率分布
- 饼图展示不同语言的学习时长占比
- 支持查看用户详细学习历史
- 可重置用户的学习进度

### 社区审核流程

- 待审核帖子会高亮显示
- 支持批量审核通过或拒绝
- 点击「查看」可审核帖子内容和评论
- 可删除违规评论
- 支持按语言和类型筛选

### 成就系统配置

- 支持自定义成就名称和图标
- 配置不同类型成就（学习/社区/系统）
- 设置获得成就所需的积分
- 查看成就获得统计
- 展示最近获得成就的用户

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
GET    /edu/community/{id}      # 获取帖子详情
```

### 成就管理接口

```
GET    /edu/achievement/list     # 获取成就列表
POST   /edu/achievement          # 新增成就
PUT    /edu/achievement          # 修改成就
DELETE /edu/achievement/{id}     # 删除成就
GET    /edu/achievement/stats    # 获取成就统计
```

## 🗄️ 数据库设计

### 主要数据表

| 表名 | 说明 | 主要字段 |
|------|------|---------|
| edu_user_profile | 用户扩展信息 | user_id, language, points, achievements |
| edu_course | 课程表 | course_id, name, language, difficulty, duration |
| edu_course_lesson | 课程单元 | lesson_id, course_id, title, content |
| edu_post | 帖子表 | post_id, title, content, author_id, language |
| edu_achievement | 成就表 | achievement_id, name, type, points, icon |
| edu_learning_progress | 学习进度 | progress_id, user_id, course_id, progress |
| edu_user_achievement | 用户成就 | user_id, achievement_id, earned_time |

## 🔒 安全配置

### 权限控制

系统采用 RBAC（基于角色的访问控制）模型：

```yaml
权限标识:
  edu:course:list        # 课程查看
  edu:course:add         # 课程新增
  edu:course:edit        # 课程修改
  edu:course:remove      # 课程删除
  edu:learning:list      # 学习数据查看
  edu:community:list      # 社区管理
  edu:achievement:list    # 成就管理
```

### 安全建议

1. ✅ 修改默认管理员密码
2. ✅ 启用登录验证码
3. ✅ 配置登录失败锁定
4. ✅ 启用 HTTPS 访问
5. ✅ 定期备份数据库
6. ✅ 配置操作日志审计
7. ✅ 启用 XSS 防护
8. ✅ 配置 SQL 注入过滤

## 🛠️ 维护指南

### 日志查看

```bash
# 查看应用日志
tail -f logs/ruoyi.log

# 查看错误日志
grep "ERROR" logs/ruoyi.log
```

### 性能优化

1. **数据库优化**
   - 定期分析慢查询
   - 添加适当索引
   - 使用 Redis 缓存热点数据

2. **前端优化**
   - 启用路由懒加载
   - 压缩静态资源
   - 启用 Gzip 压缩

3. **后端优化**
   - 配置连接池大小
   - 启用异步处理
   - 使用多级缓存

### 故障排查

| 问题现象 | 可能原因 | 解决方案 |
|---------|---------|---------|
| 登录失败 | 数据库连接失败 | 检查数据库配置 |
| 菜单不显示 | 菜单未初始化 | 执行 SQL 脚本 |
| 页面加载慢 | 网络问题 | 检查代理配置 |
| API 调用失败 | 权限不足 | 检查用户角色权限 |
| 图表不显示 | ECharts 未加载 | 检查依赖安装 |

## 📞 技术支持

如遇到问题请按以下顺序排查：

1. 检查数据库是否正常运行
2. 检查 Redis 是否正常运行
3. 检查端口是否被占用
4. 查看日志文件定位问题
5. 检查配置文件是否正确

## 📄 开源协议

本项目基于 [RuoYi](https://gitee.com/y_project/RuoYi-Vue) 开源框架开发，遵循 Apache-2.0 开源协议。

## 🙏 致谢

- 感谢 [RuoYi](https://gitee.com/y_project/RuoYi-Vue) 团队的开源贡献
- 感谢 [Element UI](https://element.eleme.cn/) 优秀的 UI 组件库
- 感谢 [ECharts](https://echarts.apache.org/) 强大的图表库

---

**版本**：1.0.0
**更新时间**：2024年1月15日
**开发团队**：LinguaLearn Development Team
