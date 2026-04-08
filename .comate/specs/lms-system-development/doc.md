# LMS学习管理系统开发规范

## 一、项目概述

- **项目名称**: LMS学习管理系统
- **技术栈**: Spring Boot 3.5.11 + MySQL 8 + Vue 3 + Element Plus+typescript
- **架构模式**: 模块化单体架构
- **核心功能**: 用户管理、课程管理、学习进度、考试评估、证书发放、互动交流

## 二、技术架构

### 2.1 后端技术栈
- Spring Boot 3.5.11
- Spring Security 6.x (JWT认证)
- Spring Data JPA
- MySQL 8.0
- Validation (参数校验)

### 2.2 前端技术栈
- Vue 3 (Composition API)
- Element Plus (UI组件库)
- Typescript
- Vue Router
- Axios (HTTP客户端)
- Pinia (状态管理)

### 2.3 项目结构

```
LMS/
├── lms-backend/                 # Spring Boot后端
│   ├── src/main/java/com/lms/
│   │   ├── config/             # 配置类
│   │   ├── controller/         # 控制器
│   │   ├── service/            # 服务层
│   │   ├── repository/         # 数据访问层
│   │   ├── entity/             # 实体类
│   │   ├── dto/                # 数据传输对象
│   │   ├── common/             # 通用组件
│   │   └── LmsApplication.java
│   └── src/main/resources/
│       ├── application.yml
│       └── schema.sql
│
└── lms-frontend/                # Vue前端
    ├── src/
    │   ├── api/                # API接口
    │   ├── views/              # 页面视图
    │   ├── components/         # 组件
    │   ├── router/             # 路由配置
    │   ├── stores/             # 状态管理
    │   └── App.vue
    └── package.json
```

## 三、开发模块规划

### 第一阶段：基础框架搭建
1. 项目初始化 (Spring Boot + Vue)
2. 数据库配置与初始化脚本
3. JWT认证模块
4. 统一响应与异常处理

### 第二阶段：核心功能开发
1. 用户管理 (CRUD、角色绑定、权限控制)
2. 课程管理 (课程、分类、章节管理)
3. 学习中心 (选课、学习记录、进度追踪)

### 第三阶段：扩展功能
1. 考试中心 (题库、试卷、考试)
2. 证书中心 (模板、发放、验证)
3. 互动中心 (评论、讨论)
4. 消息通知

## 四、数据库设计

按照设计文档中的28张表进行实现，主要表包括：
- sys_user, sys_role, sys_permission (权限体系)
- course, course_category, chapter, content (课程体系)
- enrollment, learning_record (学习记录)
- question_bank, question, exam, exam_record (考试体系)
- certificate, certificate_template (证书体系)
- comment, discussion, notification (互动通知)

## 五、开发规范

### 5.1 代码规范
- 所有代码添加中文注释
- 遵循RESTful API设计规范
- 使用驼峰命名法
- Controller层加 `@RequestMapping` 注解说明接口用途

### 5.2 接口规范
- 统一响应格式: `{ code: number, message: string, data: object }`
- 分页接口: `{ list: [], total: number, page: number, size: number }`
- 错误码: 200成功, 400参数错误, 401未认证, 403无权限, 500服务器错误

### 5.3 安全规范
- 密码BCrypt加密存储
- JWT token认证
- 接口参数校验
- SQL注入防护 (使用参数化查询)

## 六、优先级安排

考虑到项目规模，建议按以下优先级开发：

| 优先级 | 模块 | 说明 |
|--------|------|------|
| P0 | 用户中心 | 登录注册、角色权限 |
| P0 | 课程中心 | 课程管理、分类、章节 |
| P1 | 学习中心 | 选课、进度追踪 |
| P2 | 考试中心 | 题库、试卷、考试 |
| P3 | 证书中心 | 证书发放、验证 |
| P3 | 互动中心 | 评论、讨论 |

## 七、预期产出

1. 完整的后端API (Spring Boot项目)
2. 完整的前端页面 (Vue 3项目)
3. 数据库初始化脚本
4. 部署配置文件

## 八、其他要求

1. 不使用Lombok
2. 不使用Hutool
3. 完成开发后，要编译通过
5. 创建打包脚本，验证打包
6. 代码要有注释
7. 支持数据库及表不存在时，启动系统自动创建