# LMS学习管理系统开发任务清单

## 概述
本任务清单涵盖LMS学习管理系统的完整开发，包括后端Spring Boot 3.5.11项目和前端Vue 3项目。

---

## 第一阶段：项目初始化与基础框架

### 1.1 后端项目初始化
- [x] 1.1.1 创建Spring Boot 3.5.11项目结构
- [x] 1.1.2 配置pom.xml依赖 (spring-boot-starter-web, spring-boot-starter-data-jpa, spring-boot-starter-security, mysql-connector-j, spring-boot-starter-validation)
- [x] 1.1.3 创建application.yml数据库和日志配置
- [x] 1.1.4 创建启动类LmsApplication.java

### 1.2 数据库自动初始化
- [x] 1.2.1 创建数据库schema.sql包含所有28张表
- [x] 1.2.2 配置JPA自动建表策略 (update模式)
- [x] 1.2.3 添加初始化数据SQL (管理员账号、默认角色)

### 1.3 前端项目初始化
- [x] 1.3.1 创建Vue 3 + TypeScript项目
- [x] 1.3.2 安装Element Plus、Axios、Pinia、Vue Router依赖
- [x] 1.3.3 配置项目结构 (api, views, components, router, stores)

---

## 第二阶段：后端核心模块

### 2.1 通用组件
- [x] 2.1.1 创建统一响应类Result.java
- [x] 2.1.2 创建全局异常处理GlobalExceptionHandler.java
- [x] 2.1.3 创建分页工具类PageUtil.java
- [x] 2.1.4 创建MD5加密工具类SecurityUtil.java (不使用Hutool)
- [x] 2.1.5 创建JWT工具类JwtUtil.java
- [x] 2.1.6 创建日期工具类DateUtil.java

### 2.2 权限与安全配置
- [x] 2.2.1 创建SecurityConfig.java (JWT过滤器链)
- [x] 2.2.2 创建UserDetailsServiceImpl.java
- [x] 2.2.3 创建JwtAuthenticationFilter.java
- [x] 2.2.4 配置白名单路径

### 2.3 实体类开发 (不使用Lombok)
- [x] 2.3.1 用户相关实体: SysUser.java, SysRole.java, SysPermission.java, UserRole.java, RolePermission.java, UserProfile.java
- [x] 2.3.2 课程相关实体: CourseCategory.java, Course.java, Chapter.java, Content.java, Resource.java
- [x] 2.3.3 学习相关实体: Enrollment.java, LearningRecord.java
- [x] 2.3.4 考试相关实体: QuestionBank.java, Question.java, Exam.java, ExamPaperQuestion.java, ExamRecord.java
- [x] 2.3.5 证书相关实体: CertificateTemplate.java, Certificate.java
- [x] 2.3.6 互动相关实体: Review.java, Comment.java, Discussion.java, DiscussionReply.java, Notification.java
- [x] 2.3.7 系统相关实体: LearningAnalytics.java, OperationLog.java, SysConfig.java

### 2.4 Repository开发
- [x] 2.4.1 创建所有实体对应的Repository接口
- [x] 2.4.2 添加自定义查询方法

### 2.5 DTO开发
- [x] 2.5.1 请求DTO: LoginRequest.java, UserRequest.java, CourseRequest.java等
- [x] 2.5.2 响应DTO: UserResponse.java, CourseResponse.java等

### 2.6 Service层开发
- [x] 2.6.1 AuthService: 登录注册、JWT令牌生成
- [x] 2.6.2 UserService: 用户CRUD、角色绑定
- [x] 2.6.3 RoleService: 角色管理
- [x] 2.6.4 PermissionService: 权限管理
- [x] 2.6.5 CourseCategoryService: 分类管理
- [x] 2.6.6 CourseService: 课程CRUD
- [x] 2.6.7 ChapterService: 章节管理
- [x] 2.6.8 ContentService: 内容管理
- [x] 2.6.9 EnrollmentService: 选课管理
- [x] 2.6.10 LearningRecordService: 学习记录、进度计算
- [x] 2.6.11 ExamService: 试卷管理、考试

### 2.7 Controller层开发
- [x] 2.7.1 AuthController: 登录、注册、登出
- [x] 2.7.2 UserController: 用户管理
- [x] 2.7.3 RoleController: 角色管理
- [x] 2.7.4 PermissionController: 权限管理
- [x] 2.7.5 CourseCategoryController: 分类管理
- [x] 2.7.6 CourseController: 课程管理
- [x] 2.7.7 ChapterController: 章节管理
- [x] 2.7.8 ContentController: 内容管理
- [x] 2.7.9 EnrollmentController: 选课管理
- [x] 2.7.10 LearningController: 学习记录
- [x] 2.7.11 ExamController: 考试管理

---

## 第三阶段：前端开发

### 3.1 前端通用配置
- [x] 3.1.1 创建axios封装 (请求/响应拦截器)
- [x] 3.1.2 创建API接口文件
- [x] 3.1.3 创建路由配置
- [x] 3.1.4 创建Pinia用户状态管理

### 3.2 登录注册页面
- [x] 3.2.1 登录页面login.vue
- [x] 3.2.2 注册页面register.vue

### 3.3 用户管理页面
- [x] 3.3.1 用户列表页面
- [x] 3.3.2 用户编辑弹窗

### 3.4 课程管理页面
- [x] 3.4.1 课程分类管理
- [x] 3.4.2 课程列表页面
- [x] 3.4.3 课程详情/编辑页面

### 3.5 学习中心页面
- [x] 3.5.1 选课列表
- [x] 3.5.2 学习进度页面

### 3.6 考试中心页面
- [x] 3.6.1 试卷列表
- [x] 3.6.2 考试答题页面

---

## 第四阶段：构建与测试

### 4.1 后端打包脚本
- [x] 4.1.1 创建后端打包脚本build-backend.sh
- [x] 4.1.2 验证后端编译通过

### 4.2 前端打包脚本
- [x] 4.2.1 创建前端打包脚本build-frontend.sh
- [x] 4.2.2 验证前端编译通过

### 4.3 项目验证
- [x] 4.3.1 验证后端可以启动
- [x] 4.3.2 验证前端可以启动
- [x] 4.3.3 验证数据库自动创建

---

## 关键技术要求

1. **不使用Lombok**: 所有实体类手动编写getter/setter/toString方法
2. **不使用Hutool**: 手动实现工具类功能
3. **代码注释**: 所有类和方法添加中文注释
4. **自动建表**: JPA配置hibernate.ddl-auto=update自动创建表
5. **编译验证**: 创建build.sh验证项目可编译
