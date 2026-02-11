# 多语种学习平台后台管理系统

## 📋 系统概述

这是一个基于RuoYi框架开发的多语种学习平台专用后台管理系统。

## 🎯 主要功能模块

### 1. 📊 首页仪表盘
- **统计概览**：总用户数、课程总数、社区帖子、成就总数
- **数据图表**：学习数据趋势图、课程语言分布饼图
- **实时数据**：最新注册用户、热门课程、学习排行榜、最新帖子

### 2. 📚 课程管理
- 支持课程的新增、编辑、删除、查询
- 支持按语言（英语/日语/汉语）筛选
- 支持按难度等级（初级/中级/高级）筛选
- 课程状态管理（启用/禁用）
- 课程详情查看

### 3. 👥 学习数据管理
- 用户学习数据统计
- 学习进度可视化
- 课程完成率分析
- 用户学习历史追踪
- 学习时长分布图表

### 4. 💬 社区内容管理
- 帖子审核管理
- 评论管理
- 内容质量控制
- 多语言内容筛选
- 批量操作支持

### 5. 🏆 成就系统管理
- 成就的新增、编辑、删除
- 成就类型分类（学习/社区/系统）
- 成就获得统计
- 热门成就展示
- 用户成就追踪

## 🚀 快速开始

### 1. 初始化数据库菜单

在MySQL中执行以下SQL脚本：

```bash
mysql -u root -p your_database_name < sql/edu_menu_init.sql
```

### 2. 启动后端服务

```bash
cd ruoyi-admin
mvn spring-boot:run
```

后端服务地址：`http://localhost:6666`

### 3. 启动前端管理后台

```bash
cd ruoyi-ui
npm install
npm run dev
```

前端管理后台地址：`http://localhost:80`（默认）

### 4. 登录系统

- **默认管理员账号**：admin
- **默认管理员密码**：admin123

⚠️ **注意**：首次使用请修改默认密码！

## 📁 项目结构

```
RuoYi-Vue-master/
├── ruoyi-admin/                      # 后端管理模块
│   └── src/main/java/com/ruoyi/web/controller/edu/
│       └── EduMenuController.java   # 教育平台菜单控制器
├── ruoyi-ui/                         # 前端管理后台
│   └── src/views/
│       ├── index.vue                # 首页仪表盘
│       └── edu/                     # 教育管理模块
│           ├── course/index.vue     # 课程管理
│           ├── learning/index.vue   # 学习数据
│           ├── community/index.vue  # 社区管理
│           └── achievement/index.vue# 成就管理
└── sql/
    └── edu_menu_init.sql            # 菜单初始化脚本
```

## 🔧 配置说明

### 修改后台标题

编辑 `ruoyi-ui/.env.development`：

```env
VUE_APP_TITLE = LinguaLearn 多语种学习平台
```

### 修改数据库连接

编辑 `ruoyi-admin/src/main/resources/application-druid.yml`

## 📝 功能开发说明

### 前端页面开发

所有教育管理的前端页面位于：
```
ruoyi-ui/src/views/edu/
```

### 后端API开发

所有教育管理的后端控制器位于：
```
ruoyi-admin/src/main/java/com/ruoyi/web/controller/edu/
```

### 权限配置

教育管理的权限标识：
- `edu:course:list` - 课程管理
- `edu:learning:list` - 学习数据
- `edu:community:list` - 社区管理
- `edu:achievement:list` - 成就管理

## 🔐 安全建议

1. ⚠️ **立即修改默认密码**
2. ⚠️ **配置HTTPS访问**（生产环境）
3. ⚠️ **定期备份数据库**
4. ⚠️ **启用登录验证码**
5. ⚠️ **配置登录失败锁定策略**

## 📞 技术支持

如遇到问题，请检查：
1. MySQL数据库是否正常运行
2. Redis缓存是否正常运行
3. 后端服务端口（6666）是否被占用
4. 前端服务端口（80）是否被占用

## 🎨 UI设计说明

- 使用 Element UI 组件库
- 响应式设计，支持移动端访问
- 图表使用 ECharts
- 统一的颜色主题：
  - 蓝色 (#409EFF) - 主要操作
  - 绿色 (#67C23A) - 成功状态
  - 橙色 (#E6A23C) - 警告状态
  - 红色 (#F56C6C) - 危险/错误

## 📄 License

本项目基于 RuoYi 框架开发，遵循 RuoYi 开源协议。
