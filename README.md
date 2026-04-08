# LMS 学习管理系统

一个基于 Spring Boot 3.5.11 + Vue 3 的企业级学习管理平台。

## 技术栈

### 后端
- Spring Boot 3.5.11
- Spring Security + JWT 认证
- Spring Data JPA + MySQL 8
- 无 Lombok、无 Hutool（纯原生实现）

### 前端
- Vue 3 + TypeScript
- Element Plus UI 组件库
- Pinia 状态管理
- Vue Router 路由管理
- Vite 构建工具

## 项目结构

```
LMS/
├── lms-backend/          # Spring Boot 后端
│   ├── src/main/java/    # Java 源码
│   │   └── com/lms/      # 主包
│   │       ├── config/   # 配置类
│   │       ├── common/   # 公共工具
│   │       ├── controller/  # 控制器
│   │       ├── service/     # 业务逻辑
│   │       ├── repository/   # 数据访问
│   │       ├── entity/       # 实体类
│   │       └── dto/          # 数据传输对象
│   └── src/main/resources/
│       ├── application.yml    # 应用配置
│       ├── schema.sql         # 数据库初始化
│       └── static/            # 前端静态资源（打包后）
│
├── lms-frontend/         # Vue 3 前端
│   ├── src/
│   │   ├── api/          # API 接口
│   │   ├── views/        # 页面组件
│   │   ├── stores/       # Pinia 状态管理
│   │   ├── router/       # 路由配置
│   │   └── main.ts       # 入口文件
│   └── dist/             # 构建产物
│
└── build.sh              # 整合打包脚本
```

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.8+
- Node.js 16+
- MySQL 8.0+

### 方式一：整合打包（推荐）

```bash
# 运行打包脚本
./build.sh

# 打包完成后运行
java -jar lms-backend/target/lms-backend-1.0.0.jar
```

### 方式二：分别启动

#### 后端启动

```bash
cd lms-backend

# 编译运行
mvn spring-boot:run

# 或先打包再运行
mvn clean package -DskipTests
java -jar target/lms-backend-1.0.0.jar
```

#### 前端启动

```bash
cd lms-frontend

# 安装依赖
npm install

# 开发模式
npm run dev

# 生产构建
npm run build
```

### 默认账号

- 管理员: `admin` / `admin123`
- 普通用户: `user` / `user123`

## 访问地址

- 前端页面: http://localhost:8080 (整合打包后)
- 后端 API: http://localhost:8080/api

## 功能模块

1. **用户管理** - 用户注册、登录、角色权限
2. **课程管理** - 课程分类、课程发布、章节管理
3. **学习管理** - 课程订阅、学习记录、进度跟踪
4. **考试系统** - 题库管理、试卷生成、在线考试
5. **证书管理** - 证书模板、证书颁发
6. **互动社区** - 评论、讨论、问答
7. **系统通知** - 站内消息推送

## 数据库

项目启动时会自动创建数据库（需提前配置 MySQL 连接信息）。

修改 `lms-backend/src/main/resources/application.yml` 中的数据库配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/lms?useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
```

## 打包产物

- 整合包: `lms-backend/target/lms-backend-1.0.0.jar` (58MB，包含前端)
- 前端独立: `lms-frontend/dist/`

## License

MIT