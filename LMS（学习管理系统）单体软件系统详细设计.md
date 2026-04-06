# LMS（学习管理系统）单体软件系统详细设计

## 一、系统概述

### 1.1 系统定位

企业级学习管理平台，支持用户管理、课程管理、学习进度追踪、考试评估、证书发放、互动交流、数据分析等核心功能，采用模块化单体架构设计，兼顾开发效率与可扩展性，可平滑演进为微服务架构。

### 1.2 技术栈建议

|分类|技术选型|
|---|---|
|后端|Spring Boot 3.x + Spring Security + JPA/MyBatis|
|前端|Vue 3 + Element Plus / React + Ant Design|
|数据库|MySQL 8.0 + Redis（缓存/会话）|
|存储|OSS/S3（文件存储）、Elasticsearch（搜索）|
|部署|单体JAR包 + Nginx反向代理 + Docker|
|其他|JWT（鉴权）、MQ（异步处理）、CDN（视频分发）|
### 1.3 系统模块划分

```Plain Text

LMS系统
├── 用户中心（学员/讲师/管理员）
├── 课程中心（课程分类/课程管理/章节管理）
├── 内容中心（视频/文档/富文本/外链管理）
├── 学习中心（选课/学习记录/进度追踪）
├── 考试中心（题库/试卷/考试/成绩）
├── 证书中心（证书模板/发放/验证）
├── 互动中心（评论/讨论/问答）
├── 消息通知（站内信/邮件/短信）
├── 统计分析（学习行为/完成率/留存率）
├── 权限管理（RBAC角色权限）
└── 系统管理（日志/配置/操作审计）
```

## 二、数据库设计

### 2.1 ER关系概览

```Plain Text

用户(User) ──┬── 选课记录(Enrollment) ──┬── 课程(Course) ──┬── 章节(Chapter/Section) ──┬── 内容(Content)
            ├── 学习记录(LearningRecord)┘                 ├── 评价(Review)            └── 资源(Resource)
            ├── 考试成绩(ExamScore)    │                 ├── 试卷(Exam)
            ├── 证书(Certificate)      │                 └── 讨论(Discussion)
            ├── 评论(Comment)          └── 课程分类(CourseCategory)
            ├── 通知(Notification)
            ├── 角色关联(User_Role) ── 角色(Role) ── 权限关联(Role_Permission) ── 权限(Permission)
            └── 操作日志(OperationLog)

题库(QuestionBank) ── 题目(Question) ──┬── 试卷(Exam) ── 考试记录(ExamRecord)
                                       └── 试卷题目(ExamPaperQuestion)
```

### 2.2 详细表结构

#### 1. 用户表 (sys_user)

|字段名|类型|约束|说明|
|---|---|---|---|
|id|BIGINT|PK, AUTO_INCREMENT|用户ID|
|username|VARCHAR(50)|NOT NULL, UNIQUE|用户名|
|password|VARCHAR(255)|NOT NULL|加密密码（BCrypt）|
|real_name|VARCHAR(50)|NOT NULL|真实姓名|
|email|VARCHAR(100)|NOT NULL, UNIQUE|邮箱|
|mobile|VARCHAR(20)||手机号|
|avatar|VARCHAR(255)||头像URL|
|role|ENUM('admin','teacher','student')|NOT NULL, DEFAULT 'student'|角色|
|status|TINYINT|NOT NULL, DEFAULT 1|状态:0禁用,1启用|
|last_login_time|DATETIME||最后登录时间|
|create_time|DATETIME|NOT NULL, DEFAULT CURRENT_TIMESTAMP|创建时间|
|update_time|DATETIME|NOT NULL, DEFAULT CURRENT_TIMESTAMP ON UPDATE|更新时间|
#### 2. 用户信息扩展表 (user_profile)

|字段名|类型|约束|说明|
|---|---|---|---|
|id|BIGINT|PK, AUTO_INCREMENT|主键ID|
|user_id|BIGINT|NOT NULL, FK→sys_user(id), UNIQUE|用户ID|
|bio|TEXT||个人简介|
|company|VARCHAR(100)||所属企业|
|job_title|VARCHAR(50)||职位|
|address|VARCHAR(255)||地址|
|birth_date|DATE||出生日期|
|update_time|DATETIME|NOT NULL|更新时间|
#### 3. 角色表 (sys_role)

|字段名|类型|约束|说明|
|---|---|---|---|
|id|BIGINT|PK, AUTO_INCREMENT|角色ID|
|role_name|VARCHAR(50)|NOT NULL, UNIQUE|角色名称|
|role_code|VARCHAR(50)|NOT NULL, UNIQUE|角色编码|
|description|VARCHAR(200)||角色描述|
|status|TINYINT|DEFAULT 1|状态：0禁用，1启用|
|create_time|DATETIME|NOT NULL|创建时间|
#### 4. 权限表 (sys_permission)

|字段名|类型|约束|说明|
|---|---|---|---|
|id|BIGINT|PK, AUTO_INCREMENT|权限ID|
|perm_name|VARCHAR(50)|NOT NULL|权限名称|
|perm_code|VARCHAR(100)|NOT NULL, UNIQUE|权限编码（如course:create）|
|perm_type|ENUM('menu','button','api')|NOT NULL|权限类型|
|parent_id|BIGINT|DEFAULT 0|父权限ID|
|sort_order|INT|DEFAULT 0|排序|
|create_time|DATETIME|NOT NULL|创建时间|
#### 5. 角色-权限关联表 (sys_role_permission)

|字段名|类型|约束|说明|
|---|---|---|---|
|id|BIGINT|PK, AUTO_INCREMENT|关联ID|
|role_id|BIGINT|NOT NULL, FK→sys_role(id)|角色ID|
|perm_id|BIGINT|NOT NULL, FK→sys_permission(id)|权限ID|
|UNIQUE KEY|(role_id, perm_id)||联合唯一索引|
#### 6. 用户-角色关联表 (sys_user_role)

|字段名|类型|约束|说明|
|---|---|---|---|
|id|BIGINT|PK, AUTO_INCREMENT|关联ID|
|user_id|BIGINT|NOT NULL, FK→sys_user(id)|用户ID|
|role_id|BIGINT|NOT NULL, FK→sys_role(id)|角色ID|
|UNIQUE KEY|(user_id, role_id)||联合唯一索引|
#### 7. 课程分类表 (course_category)

|字段名|类型|约束|说明|
|---|---|---|---|
|id|BIGINT|PK, AUTO_INCREMENT|分类ID|
|parent_id|BIGINT|DEFAULT 0|父分类ID，0表示根分类|
|name|VARCHAR(50)|NOT NULL|分类名称|
|sort_order|INT|DEFAULT 0|排序序号|
|icon|VARCHAR(255)||图标URL|
|status|TINYINT|NOT NULL, DEFAULT 1|状态|
|create_time|DATETIME|NOT NULL|创建时间|
#### 8. 课程表 (course)

|字段名|类型|约束|说明|
|---|---|---|---|
|id|BIGINT|PK, AUTO_INCREMENT|课程ID|
|category_id|BIGINT|NOT NULL, FK→course_category(id)|分类ID|
|teacher_id|BIGINT|NOT NULL, FK→sys_user(id)|讲师ID|
|title|VARCHAR(200)|NOT NULL|课程标题|
|subtitle|VARCHAR(300)||副标题|
|cover_image|VARCHAR(255)||封面图URL|
|intro_video|VARCHAR(255)||介绍视频URL|
|description|TEXT||课程简介|
|level|ENUM('beginner','intermediate','advanced')|NOT NULL|难度等级|
|price|DECIMAL(10,2)|DEFAULT 0.00|价格(0免费)|
|total_duration|INT|DEFAULT 0|总时长(分钟)|
|total_students|INT|DEFAULT 0|学习人数|
|rating|DECIMAL(2,1)|DEFAULT 0.0|综合评分|
|status|ENUM('draft','published','closed')|NOT NULL, DEFAULT 'draft'|状态|
|publish_time|DATETIME||发布时间|
|create_time|DATETIME|NOT NULL|创建时间|
|update_time|DATETIME|NOT NULL|更新时间|
#### 9. 章节表 (chapter)

|字段名|类型|约束|说明|
|---|---|---|---|
|id|BIGINT|PK, AUTO_INCREMENT|章节ID|
|course_id|BIGINT|NOT NULL, FK→course(id)|所属课程ID|
|parent_id|BIGINT|DEFAULT 0|父章节ID(支持章/节结构)|
|title|VARCHAR(200)|NOT NULL|章节标题|
|content_type|ENUM('video','document','quiz','assignment')|DEFAULT 'video'|内容类型|
|content_url|VARCHAR(500)||视频/文档URL|
|content_text|LONGTEXT||富文本内容|
|duration|INT|DEFAULT 0|预计学习时长(分钟)|
|sort_order|INT|DEFAULT 0|排序序号|
|is_free|TINYINT|DEFAULT 0|是否免费试看|
|create_time|DATETIME|NOT NULL|创建时间|
|update_time|DATETIME|NOT NULL|更新时间|
#### 10. 内容表 (content)

|字段名|类型|约束|说明|
|---|---|---|---|
|id|BIGINT|PK, AUTO_INCREMENT|内容ID|
|section_id|BIGINT|NOT NULL, FK→chapter(id)|所属章节ID|
|title|VARCHAR(200)|NOT NULL|内容标题|
|type|ENUM('video','doc','text','link')|NOT NULL|内容类型|
|url|VARCHAR(500)||内容URL|
|duration|INT|DEFAULT 0|时长(秒，仅视频)|
|file_size|BIGINT||文件大小(字节)|
|is_required|TINYINT|DEFAULT 1|是否必修内容|
|create_time|DATETIME|NOT NULL|创建时间|
#### 11. 资源表 (resource)

|字段名|类型|约束|说明|
|---|---|---|---|
|id|BIGINT|PK, AUTO_INCREMENT|资源ID|
|course_id|BIGINT|NOT NULL, FK→course(id)|所属课程ID|
|chapter_id|BIGINT|FK→chapter(id)|所属章节ID|
|resource_type|ENUM('file','link','video')|NOT NULL|资源类型|
|title|VARCHAR(200)|NOT NULL|资源标题|
|file_url|VARCHAR(500)||文件URL|
|file_size|BIGINT||文件大小(字节)|
|download_count|INT|DEFAULT 0|下载次数|
|create_time|DATETIME|NOT NULL|创建时间|
#### 12. 选课记录表 (enrollment)

|字段名|类型|约束|说明|
|---|---|---|---|
|id|BIGINT|PK, AUTO_INCREMENT|记录ID|
|user_id|BIGINT|NOT NULL, FK→sys_user(id)|学员ID|
|course_id|BIGINT|NOT NULL, FK→course(id)|课程ID|
|order_no|VARCHAR(32)|UNIQUE|订单号(付费课程)|
|pay_amount|DECIMAL(10,2)|DEFAULT 0.00|支付金额|
|progress|INT|DEFAULT 0|学习进度(%)|
|last_learn_time|DATETIME||最后学习时间|
|status|ENUM('active','completed','dropped')|NOT NULL, DEFAULT 'active'|状态|
|enroll_time|DATETIME|NOT NULL|选课时间|
|complete_time|DATETIME||完成时间|
|UNIQUE KEY|(user_id, course_id)||联合唯一索引|
#### 13. 学习记录表 (learning_record)

|字段名|类型|约束|说明|
|---|---|---|---|
|id|BIGINT|PK, AUTO_INCREMENT|记录ID|
|user_id|BIGINT|NOT NULL, FK→sys_user(id)|学员ID|
|course_id|BIGINT|NOT NULL, FK→course(id)|课程ID|
|chapter_id|BIGINT|NOT NULL, FK→chapter(id)|章节ID|
|content_id|BIGINT|FK→content(id)|内容ID|
|watch_duration|INT|DEFAULT 0|观看时长(秒)|
|is_completed|TINYINT|DEFAULT 0|是否完成|
|last_position|INT|DEFAULT 0|最后播放位置(秒)|
|create_time|DATETIME|NOT NULL|首次学习时间|
|update_time|DATETIME|NOT NULL|最后学习时间|
|INDEX|(user_id, course_id)||联合索引|
#### 14. 题库表 (question_bank)

|字段名|类型|约束|说明|
|---|---|---|---|
|id|BIGINT|PK, AUTO_INCREMENT|题库ID|
|teacher_id|BIGINT|NOT NULL, FK→sys_user(id)|创建讲师ID|
|name|VARCHAR(100)|NOT NULL|题库名称|
|description|TEXT||题库描述|
|question_count|INT|DEFAULT 0|题目数量|
|status|TINYINT|DEFAULT 1|状态|
|create_time|DATETIME|NOT NULL|创建时间|
#### 15. 题目表 (question)

|字段名|类型|约束|说明|
|---|---|---|---|
|id|BIGINT|PK, AUTO_INCREMENT|题目ID|
|bank_id|BIGINT|NOT NULL, FK→question_bank(id)|所属题库ID|
|question_type|ENUM('single','multiple','judge','fill','essay')|NOT NULL|题型|
|content|TEXT|NOT NULL|题干|
|options|JSON||选项(JSON数组)|
|answer|TEXT|NOT NULL|答案|
|analysis|TEXT||解析|
|score|INT|NOT NULL, DEFAULT 1|分值|
|difficulty|TINYINT|DEFAULT 1|难度1-5|
|status|TINYINT|DEFAULT 1|状态|
|create_time|DATETIME|NOT NULL|创建时间|
#### 16. 试卷表 (exam)

|字段名|类型|约束|说明|
|---|---|---|---|
|id|BIGINT|PK, AUTO_INCREMENT|试卷ID|
|course_id|BIGINT|NOT NULL, FK→course(id)|所属课程ID|
|title|VARCHAR(200)|NOT NULL|试卷标题|
|description|TEXT||考试说明|
|total_score|INT|NOT NULL|总分|
|pass_score|INT|NOT NULL|及格分|
|duration|INT|NOT NULL|考试时长(分钟)|
|question_count|INT|NOT NULL|题目数量|
|attempt_limit|INT|DEFAULT 1|允许重考次数(0不限)|
|start_time|DATETIME||开始时间|
|end_time|DATETIME||结束时间|
|status|ENUM('draft','published','closed')|DEFAULT 'draft'|状态|
|create_time|DATETIME|NOT NULL|创建时间|
#### 17. 试卷题目关联表 (exam_paper_question)

|字段名|类型|约束|说明|
|---|---|---|---|
|id|BIGINT|PK, AUTO_INCREMENT|关联ID|
|exam_id|BIGINT|NOT NULL, FK→exam(id)|试卷ID|
|question_id|BIGINT|NOT NULL, FK→question(id)|题目ID|
|sort_order|INT|DEFAULT 0|排序|
|custom_score|INT||自定义分值(覆盖题目默认分)|
|UNIQUE KEY|(exam_id, question_id)||联合唯一索引|
#### 18. 考试记录表 (exam_record)

|字段名|类型|约束|说明|
|---|---|---|---|
|id|BIGINT|PK, AUTO_INCREMENT|记录ID|
|exam_id|BIGINT|NOT NULL, FK→exam(id)|试卷ID|
|user_id|BIGINT|NOT NULL, FK→sys_user(id)|学员ID|
|attempt_number|INT|NOT NULL, DEFAULT 1|第几次考试|
|answers|JSON||考生答案(JSON)|
|score|INT||得分|
|is_passed|TINYINT|DEFAULT 0|是否及格|
|start_time|DATETIME|NOT NULL|开始时间|
|submit_time|DATETIME||提交时间|
|status|ENUM('in_progress','submitted','graded')|DEFAULT 'in_progress'|状态|
|create_time|DATETIME|NOT NULL|创建时间|
#### 19. 证书模板表 (certificate_template)

|字段名|类型|约束|说明|
|---|---|---|---|
|id|BIGINT|PK, AUTO_INCREMENT|模板ID|
|name|VARCHAR(100)|NOT NULL|模板名称|
|template_code|VARCHAR(100)|NOT NULL, UNIQUE|模板标识码|
|background|VARCHAR(255)||背景图URL|
|fields|JSON||动态字段配置|
|content_html|TEXT||HTML内容|
|status|TINYINT|DEFAULT 1|状态|
|create_time|DATETIME|NOT NULL|创建时间|
#### 20. 证书表 (certificate)

|字段名|类型|约束|说明|
|---|---|---|---|
|id|BIGINT|PK, AUTO_INCREMENT|证书ID|
|user_id|BIGINT|NOT NULL, FK→sys_user(id)|学员ID|
|course_id|BIGINT|NOT NULL, FK→course(id)|课程ID|
|template_id|BIGINT|NOT NULL, FK→certificate_template(id)|模板ID|
|certificate_no|VARCHAR(50)|NOT NULL, UNIQUE|证书编号|
|issue_date|DATETIME|NOT NULL|发证日期|
|expire_date|DATETIME||有效期至(空为永久)|
|verify_code|VARCHAR(32)|NOT NULL, UNIQUE|验证码|
|status|ENUM('valid','revoked','expired')|DEFAULT 'valid'|状态|
|create_time|DATETIME|NOT NULL|创建时间|
#### 21. 课程评价表 (review)

|字段名|类型|约束|说明|
|---|---|---|---|
|id|BIGINT|PK, AUTO_INCREMENT|评价ID|
|course_id|BIGINT|NOT NULL, FK→course(id)|课程ID|
|user_id|BIGINT|NOT NULL, FK→sys_user(id)|学员ID|
|rating|TINYINT|NOT NULL, 1-5|评分|
|content|TEXT||评价内容|
|reply_content|TEXT||讲师回复|
|reply_time|DATETIME||回复时间|
|status|TINYINT|DEFAULT 1|状态(0隐藏,1显示)|
|create_time|DATETIME|NOT NULL|创建时间|
|UNIQUE KEY|(course_id, user_id)||每人每课程一条评价|
#### 22. 评论表 (comment)

|字段名|类型|约束|说明|
|---|---|---|---|
|id|BIGINT|PK, AUTO_INCREMENT|评论ID|
|user_id|BIGINT|NOT NULL, FK→sys_user(id)|评论人ID|
|content_id|BIGINT|NOT NULL, FK→content(id)|关联内容ID|
|chapter_id|BIGINT|FK→chapter(id)|关联章节ID|
|text|TEXT|NOT NULL|评论内容|
|parent_id|BIGINT|DEFAULT 0|父评论ID（回复）|
|like_count|INT|DEFAULT 0|点赞数|
|status|TINYINT|DEFAULT 1|状态：0隐藏，1显示|
|create_time|DATETIME|NOT NULL|创建时间|
#### 23. 讨论表 (discussion)

|字段名|类型|约束|说明|
|---|---|---|---|
|id|BIGINT|PK, AUTO_INCREMENT|讨论ID|
|course_id|BIGINT|NOT NULL, FK→course(id)|关联课程ID|
|user_id|BIGINT|NOT NULL, FK→sys_user(id)|发起用户ID|
|title|VARCHAR(200)|NOT NULL|讨论标题|
|content|TEXT|NOT NULL|讨论内容|
|view_count|INT|DEFAULT 0|浏览数|
|reply_count|INT|DEFAULT 0|回复数|
|status|TINYINT|DEFAULT 1|状态|
|create_time|DATETIME|NOT NULL|创建时间|
|update_time|DATETIME|NOT NULL|更新时间|
#### 24. 讨论回复表 (discussion_reply)

|字段名|类型|约束|说明|
|---|---|---|---|
|id|BIGINT|PK, AUTO_INCREMENT|回复ID|
|discussion_id|BIGINT|NOT NULL, FK→discussion(id)|讨论ID|
|user_id|BIGINT|NOT NULL, FK→sys_user(id)|回复人ID|
|content|TEXT|NOT NULL|回复内容|
|parent_id|BIGINT|DEFAULT 0|父回复ID|
|like_count|INT|DEFAULT 0|点赞数|
|create_time|DATETIME|NOT NULL|创建时间|
#### 25. 消息通知表 (notification)

|字段名|类型|约束|说明|
|---|---|---|---|
|id|BIGINT|PK, AUTO_INCREMENT|通知ID|
|user_id|BIGINT|NOT NULL, FK→sys_user(id)|接收人ID|
|type|ENUM('system','course','exam','certificate','interaction')|NOT NULL|通知类型|
|title|VARCHAR(200)|NOT NULL|标题|
|content|TEXT|NOT NULL|内容|
|is_read|TINYINT|DEFAULT 0|是否已读|
|read_time|DATETIME||阅读时间|
|create_time|DATETIME|NOT NULL|创建时间|
#### 26. 学习统计量表 (learning_analytics)

|字段名|类型|约束|说明|
|---|---|---|---|
|id|BIGINT|PK, AUTO_INCREMENT|统计ID|
|user_id|BIGINT|NOT NULL|学员ID|
|course_id|BIGINT|NOT NULL|课程ID|
|date|DATE|NOT NULL|统计日期|
|learn_duration|INT|DEFAULT 0|当日学习时长(分钟)|
|complete_chapters|INT|DEFAULT 0|当日完成章节数|
|login_count|INT|DEFAULT 0|当日登录次数|
|UNIQUE KEY|(user_id, course_id, date)||联合唯一索引|
#### 27. 操作日志表 (operation_log)

|字段名|类型|约束|说明|
|---|---|---|---|
|id|BIGINT|PK, AUTO_INCREMENT|日志ID|
|user_id|BIGINT|FK→sys_user(id)|操作用户ID|
|username|VARCHAR(50)||用户名(冗余)|
|operation|VARCHAR(100)|NOT NULL|操作类型|
|method|VARCHAR(200)||请求方法|
|params|TEXT||请求参数|
|ip|VARCHAR(50)||操作IP|
|duration|INT||耗时(毫秒)|
|create_time|DATETIME|NOT NULL|操作时间|
|INDEX|(user_id, create_time)||联合索引|
#### 28. 系统配置表 (sys_config)

|字段名|类型|约束|说明|
|---|---|---|---|
|id|BIGINT|PK, AUTO_INCREMENT|配置ID|
|config_key|VARCHAR(100)|NOT NULL, UNIQUE|配置键|
|config_value|TEXT||配置值|
|config_type|VARCHAR(50)||配置类型|
|description|VARCHAR(200)||描述|
|create_time|DATETIME|NOT NULL|创建时间|
|update_time|DATETIME|NOT NULL|更新时间|
## 三、核心业务流程

### 3.1 选课流程

```Plain Text

学员浏览课程 → 点击选课 → 判断是否免费/付费
    ├─ 免费 → 创建选课记录 → 开始学习
    └─ 付费 → 创建订单 → 支付回调 → 创建选课记录 → 开始学习
```

### 3.2 学习进度追踪流程

```Plain Text

学员学习章节/内容 → 前端定时上报学习时长/进度 → 更新学习记录
    → 判断章节/内容完成度 → 汇总更新课程总进度
    → 课程100%完成 → 触发考试/证书发放流程
```

### 3.3 教师发布课程流程

```Plain Text

创建课程基本信息 → 配置课程分类/难度/价格 → 添加章节结构 → 上传内容/资源 → 预览课程 → 发布课程
```

### 3.4 考试流程

```Plain Text

学员进入考试 → 生成考试记录(in_progress)
    → 答题 → 提交答卷 → 自动批改(客观题)
    → 主观题需讲师批改 → 计算总分 → 判定是否及格
    → 及格且课程完成 → 更新证书发放状态
```

### 3.5 证书发放流程

```Plain Text

课程完成 + 考试及格 → 校验证书模板 → 生成唯一证书编号/验证码 → 创建证书记录 → 发送证书通知 → 学员可查看/下载证书
```

## 四、接口设计示例（RESTful）

|模块|方法|路径|说明|
|---|---|---|---|
|课程|GET|/api/courses|课程列表（支持分页/筛选/排序）|
|课程|GET|/api/courses/{id}|课程详情|
|课程|POST|/api/courses|创建课程(讲师/管理员)|
|课程|PUT|/api/courses/{id}|更新课程信息|
|章节|POST|/api/courses/{courseId}/chapters|添加课程章节|
|内容|POST|/api/chapters/{chapterId}/contents|上传章节内容|
|学习|POST|/api/enrollments|选课|
|学习|GET|/api/enrollments/my|我的选课列表|
|学习|GET|/api/progress/{courseId}|查询课程学习进度|
|学习|POST|/api/learning/record|上报学习时长/进度|
|考试|GET|/api/exams/{courseId}|获取课程关联试卷|
|考试|POST|/api/exams/{id}/start|开始考试|
|考试|POST|/api/exams/{id}/submit|提交答卷|
|证书|GET|/api/certificates|我的证书列表|
|证书|GET|/api/certificates/verify/{code}|证书验证|
|互动|POST|/api/contents/{contentId}/comments|发布内容评论|
|互动|GET|/api/courses/{courseId}/discussions|课程讨论列表|
|统计|GET|/api/analytics/learning/{courseId}|课程学习统计|
## 五、关键代码结构

```Plain Text

com.lms
├── controller          // 控制层
│   ├── UserController      // 用户管理
│   ├── CourseController    // 课程管理
│   ├── ContentController   // 内容管理
│   ├── LearningController  // 学习管理
│   ├── ExamController      // 考试管理
│   ├── CertificateController // 证书管理
│   ├── InteractionController // 互动管理
│   ├── AnalyticsController  // 统计分析
│   └── SystemController     // 系统管理
├── service             // 服务层
│   ├── UserService
│   ├── CourseService
│   ├── ContentService
│   ├── ProgressService (核心：进度计算)
│   ├── ExamService
│   ├── CertificateService
│   ├── InteractionService
│   ├── AnalyticsService
│   └── AuthService
├── repository          // 数据访问层
│   └── (JPA/MyBatis接口，对应各表)
├── entity              // 实体层
│   └── (对应数据库表实体)
├── dto                 // 数据传输对象
│   ├── request         // 请求DTO
│   └── response        // 响应DTO
├── domain              // 领域层
│   ├── model           // 领域模型
│   └── event           // 领域事件
├── config              // 配置层
│   ├── SecurityConfig     // 安全配置
│   ├── WebMvcConfig       // Web配置
│   ├── RedisConfig        // 缓存配置
│   └── AsyncConfig        // 异步配置
├── util                // 工具类
│   ├── JwtUtil            // JWT工具
│   ├── RedisUtil          // Redis工具
│   ├── FileUtil           // 文件处理工具
│   └── SecurityUtil       // 安全工具
└── task                // 定时任务
    ├── CertificateExpireTask  // 证书过期检查
    ├── LearningStatsTask      // 学习数据统计
    └── CourseStatsTask        // 课程数据统计
```

## 六、非功能性设计

### 6.1 缓存策略

- Redis缓存：课程详情(30min)、分类树(1h)、用户会话、热门内容列表(10min)

- 本地缓存：系统配置、字典数据、角色权限缓存

- 缓存更新策略：更新操作后主动失效缓存，定时全量刷新核心缓存

### 6.2 安全设计

- JWT token认证，有效期2小时，刷新token机制

- 密码BCrypt加密，支持密码强度校验

- 防SQL注入（参数化查询）、XSS过滤（前端/后端双重过滤）、CSRF防护（Token验证）

- 接口限流(单用户10次/秒)，防止恶意请求

- 视频/文件防盗链（Referer校验 + 临时URL）

- 敏感操作日志审计（登录、权限变更、支付等）

### 6.3 性能指标

- 支持5000+并发学员

- 接口响应P99 < 500ms

- 学习记录上报异步处理(MQ)，削峰填谷

- 视频内容CDN分发，降低源站压力

- 大文件分片上传，断点续传

### 6.4 扩展性设计

虽然是单体架构，预留以下扩展点：

- 课程资源可无缝切换至OSS对象存储

- 学习记录、统计数据可切到时序数据库（如ClickHouse）

- 后续可按领域拆分微服务：用户中心、课程中心、考试中心、内容中心

- 支持多租户扩展（企业隔离）

- 预留国际化接口（多语言、多币种）

### 6.5 部署架构

```Plain Text

Client (Web/APP)
  ↓
Nginx (反向代理/静态资源/CDN对接)
  ↓
LMS App（单体JAR包）
  ↓
MySQL (主从分离) + Redis (集群) + OSS (文件存储) + MQ (异步处理)
```

### 6.6 演进路径

- 阶段1：模块化单体部署，快速上线核心功能

- 阶段2：拆分独立服务（如文件服务、视频转码服务），单体应用调用外部服务

- 阶段3：按领域拆分为微服务（用户、课程、考试、学习等），引入API网关、服务注册发现

- 阶段4：微服务集群化部署，支持弹性扩缩容、灰度发布

## 七、总结

本设计文档整合了LMS系统的核心功能模块、完整数据库表结构、关键业务流程及非功能性设计，基于模块化单体架构实现，兼顾了开发效率与未来扩展性。数据库表结构补充了用户扩展信息、RBAC权限体系、互动评论/讨论、学习统计等缺失内容，覆盖了企业级LMS系统的全场景需求，可直接用于开发实施或架构设计参考。
> （注：文档部分内容可能由 AI 生成）