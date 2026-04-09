-- LMS学习管理系统数据库初始化脚本
-- 创建数据库(如果不存在)
CREATE DATABASE IF NOT EXISTS lms_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE lms_db;

-- ========================================
-- 用户相关表
-- ========================================

-- 用户表 (sys_user)
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '加密密码(BCrypt)',
    real_name VARCHAR(50) NOT NULL COMMENT '真实姓名',
    email VARCHAR(100) NOT NULL UNIQUE COMMENT '邮箱',
    mobile VARCHAR(20) COMMENT '手机号',
    avatar VARCHAR(255) COMMENT '头像URL',
    role ENUM('admin', 'teacher', 'student') NOT NULL DEFAULT 'student' COMMENT '角色',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态:0禁用,1启用',
    last_login_time DATETIME COMMENT '最后登录时间',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 用户信息扩展表 (user_profile)
CREATE TABLE IF NOT EXISTS user_profile (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL UNIQUE COMMENT '用户ID',
    bio TEXT COMMENT '个人简介',
    company VARCHAR(100) COMMENT '所属企业',
    job_title VARCHAR(50) COMMENT '职位',
    address VARCHAR(255) COMMENT '地址',
    birth_date DATE COMMENT '出生日期',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    CONSTRAINT fk_user_profile_user FOREIGN KEY (user_id) REFERENCES sys_user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息扩展表';

-- 角色表 (sys_role)
CREATE TABLE IF NOT EXISTS sys_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID',
    role_name VARCHAR(50) NOT NULL UNIQUE COMMENT '角色名称',
    role_code VARCHAR(50) NOT NULL UNIQUE COMMENT '角色编码',
    description VARCHAR(200) COMMENT '角色描述',
    status TINYINT DEFAULT 1 COMMENT '状态:0禁用,1启用',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 权限表 (sys_permission)
CREATE TABLE IF NOT EXISTS sys_permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '权限ID',
    perm_name VARCHAR(50) NOT NULL COMMENT '权限名称',
    perm_code VARCHAR(100) NOT NULL UNIQUE COMMENT '权限编码',
    perm_type ENUM('menu', 'button', 'api') NOT NULL COMMENT '权限类型',
    parent_id BIGINT DEFAULT 0 COMMENT '父权限ID',
    sort_order INT DEFAULT 0 COMMENT '排序',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- 角色-权限关联表 (sys_role_permission)
CREATE TABLE IF NOT EXISTS sys_role_permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '关联ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    perm_id BIGINT NOT NULL COMMENT '权限ID',
    UNIQUE KEY uk_role_perm (role_id, perm_id),
    CONSTRAINT fk_rp_role FOREIGN KEY (role_id) REFERENCES sys_role(id) ON DELETE CASCADE,
    CONSTRAINT fk_rp_perm FOREIGN KEY (perm_id) REFERENCES sys_permission(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';

-- 用户-角色关联表 (sys_user_role)
CREATE TABLE IF NOT EXISTS sys_user_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '关联ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_user_role (user_id, role_id),
    CONSTRAINT fk_ur_user FOREIGN KEY (user_id) REFERENCES sys_user(id) ON DELETE CASCADE,
    CONSTRAINT fk_ur_role FOREIGN KEY (role_id) REFERENCES sys_role(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- ========================================
-- 课程相关表
-- ========================================

-- 课程分类表 (course_category)
CREATE TABLE IF NOT EXISTS course_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
    parent_id BIGINT DEFAULT 0 COMMENT '父分类ID,0表示根分类',
    name VARCHAR(50) NOT NULL COMMENT '分类名称',
    sort_order INT DEFAULT 0 COMMENT '排序序号',
    icon VARCHAR(255) COMMENT '图标URL',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程分类表';

-- 课程表 (course)
CREATE TABLE IF NOT EXISTS course (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '课程ID',
    category_id BIGINT NOT NULL COMMENT '分类ID',
    teacher_id BIGINT NOT NULL COMMENT '讲师ID',
    title VARCHAR(200) NOT NULL COMMENT '课程标题',
    subtitle VARCHAR(300) COMMENT '副标题',
    cover_image VARCHAR(255) COMMENT '封面图URL',
    intro_video VARCHAR(255) COMMENT '介绍视频URL',
    description TEXT COMMENT '课程简介',
    level ENUM('beginner', 'intermediate', 'advanced') NOT NULL COMMENT '难度等级',
    price DECIMAL(10,2) DEFAULT 0.00 COMMENT '价格(0免费)',
    total_duration INT DEFAULT 0 COMMENT '总时长(分钟)',
    total_students INT DEFAULT 0 COMMENT '学习人数',
    rating DECIMAL(2,1) DEFAULT 0.0 COMMENT '综合评分',
    status ENUM('draft', 'published', 'closed') NOT NULL DEFAULT 'draft' COMMENT '状态',
    publish_time DATETIME COMMENT '发布时间',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    CONSTRAINT fk_course_category FOREIGN KEY (category_id) REFERENCES course_category(id),
    CONSTRAINT fk_course_teacher FOREIGN KEY (teacher_id) REFERENCES sys_user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程表';

-- 章节表 (chapter)
CREATE TABLE IF NOT EXISTS chapter (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '章节ID',
    course_id BIGINT NOT NULL COMMENT '所属课程ID',
    parent_id BIGINT DEFAULT 0 COMMENT '父章节ID(支持章/节结构)',
    title VARCHAR(200) NOT NULL COMMENT '章节标题',
    content_type ENUM('video', 'document', 'quiz', 'assignment') DEFAULT 'video' COMMENT '内容类型',
    content_url VARCHAR(500) COMMENT '视频/文档URL',
    content_text LONGTEXT COMMENT '富文本内容',
    duration INT DEFAULT 0 COMMENT '预计学习时长(分钟)',
    sort_order INT DEFAULT 0 COMMENT '排序序号',
    is_free TINYINT DEFAULT 0 COMMENT '是否免费试看',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    CONSTRAINT fk_chapter_course FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='章节表';

-- 内容表 (content)
CREATE TABLE IF NOT EXISTS content (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '内容ID',
    section_id BIGINT NOT NULL COMMENT '所属章节ID',
    title VARCHAR(200) NOT NULL COMMENT '内容标题',
    type ENUM('video', 'doc', 'text', 'link') NOT NULL COMMENT '内容类型',
    url VARCHAR(500) COMMENT '内容URL',
    duration INT DEFAULT 0 COMMENT '时长(秒,仅视频)',
    file_size BIGINT COMMENT '文件大小(字节)',
    is_required TINYINT DEFAULT 1 COMMENT '是否必修内容',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    CONSTRAINT fk_content_section FOREIGN KEY (section_id) REFERENCES chapter(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='内容表';

-- 资源表 (resource)
CREATE TABLE IF NOT EXISTS resource (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '资源ID',
    course_id BIGINT NOT NULL COMMENT '所属课程ID',
    chapter_id BIGINT COMMENT '所属章节ID',
    resource_type ENUM('file', 'link', 'video') NOT NULL COMMENT '资源类型',
    title VARCHAR(200) NOT NULL COMMENT '资源标题',
    file_url VARCHAR(500) COMMENT '文件URL',
    file_size BIGINT COMMENT '文件大小(字节)',
    download_count INT DEFAULT 0 COMMENT '下载次数',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    CONSTRAINT fk_resource_course FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE,
    CONSTRAINT fk_resource_chapter FOREIGN KEY (chapter_id) REFERENCES chapter(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源表';

-- ========================================
-- 学习相关表
-- ========================================

-- 选课记录表 (enrollment)
CREATE TABLE IF NOT EXISTS enrollment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
    user_id BIGINT NOT NULL COMMENT '学员ID',
    course_id BIGINT NOT NULL COMMENT '课程ID',
    order_no VARCHAR(32) UNIQUE COMMENT '订单号(付费课程)',
    pay_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '支付金额',
    progress INT DEFAULT 0 COMMENT '学习进度(%)',
    last_learn_time DATETIME COMMENT '最后学习时间',
    status ENUM('active', 'completed', 'dropped') NOT NULL DEFAULT 'active' COMMENT '状态',
    enroll_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '选课时间',
    complete_time DATETIME COMMENT '完成时间',
    UNIQUE KEY uk_user_course (user_id, course_id),
    CONSTRAINT fk_enrollment_user FOREIGN KEY (user_id) REFERENCES sys_user(id) ON DELETE CASCADE,
    CONSTRAINT fk_enrollment_course FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='选课记录表';

-- 学习记录表 (learning_record)
CREATE TABLE IF NOT EXISTS learning_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
    user_id BIGINT NOT NULL COMMENT '学员ID',
    course_id BIGINT NOT NULL COMMENT '课程ID',
    chapter_id BIGINT NOT NULL COMMENT '章节ID',
    content_id BIGINT COMMENT '内容ID',
    watch_duration INT DEFAULT 0 COMMENT '观看时长(秒)',
    is_completed TINYINT DEFAULT 0 COMMENT '是否完成',
    last_position INT DEFAULT 0 COMMENT '最后播放位置(秒)',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '首次学习时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后学习时间',
    INDEX idx_user_course (user_id, course_id),
    CONSTRAINT fk_lr_user FOREIGN KEY (user_id) REFERENCES sys_user(id) ON DELETE CASCADE,
    CONSTRAINT fk_lr_course FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE,
    CONSTRAINT fk_lr_chapter FOREIGN KEY (chapter_id) REFERENCES chapter(id) ON DELETE CASCADE,
    CONSTRAINT fk_lr_content FOREIGN KEY (content_id) REFERENCES content(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学习记录表';

-- ========================================
-- 考试相关表
-- ========================================

-- 题库表 (question_bank)
CREATE TABLE IF NOT EXISTS question_bank (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '题库ID',
    teacher_id BIGINT NOT NULL COMMENT '创建讲师ID',
    name VARCHAR(100) NOT NULL COMMENT '题库名称',
    description TEXT COMMENT '题库描述',
    question_count INT DEFAULT 0 COMMENT '题目数量',
    status TINYINT DEFAULT 1 COMMENT '状态',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    CONSTRAINT fk_qb_teacher FOREIGN KEY (teacher_id) REFERENCES sys_user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='题库表';

-- 题目表 (question)
CREATE TABLE IF NOT EXISTS question (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '题目ID',
    bank_id BIGINT NOT NULL COMMENT '所属题库ID',
    question_type ENUM('single', 'multiple', 'judge', 'fill', 'essay') NOT NULL COMMENT '题型',
    content TEXT NOT NULL COMMENT '题干',
    options JSON COMMENT '选项(JSON数组)',
    answer TEXT NOT NULL COMMENT '答案',
    analysis TEXT COMMENT '解析',
    score INT NOT NULL DEFAULT 1 COMMENT '分值',
    difficulty TINYINT DEFAULT 1 COMMENT '难度1-5',
    status TINYINT DEFAULT 1 COMMENT '状态',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    CONSTRAINT fk_question_bank FOREIGN KEY (bank_id) REFERENCES question_bank(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='题目表';

-- 试卷表 (exam)
CREATE TABLE IF NOT EXISTS exam (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '试卷ID',
    course_id BIGINT NOT NULL COMMENT '所属课程ID',
    title VARCHAR(200) NOT NULL COMMENT '试卷标题',
    description TEXT COMMENT '考试说明',
    total_score INT NOT NULL COMMENT '总分',
    pass_score INT NOT NULL COMMENT '及格分',
    duration INT NOT NULL COMMENT '考试时长(分钟)',
    question_count INT NOT NULL COMMENT '题目数量',
    attempt_limit INT DEFAULT 1 COMMENT '允许重考次数(0不限)',
    start_time DATETIME COMMENT '开始时间',
    end_time DATETIME COMMENT '结束时间',
    status ENUM('draft', 'published', 'closed') DEFAULT 'draft' COMMENT '状态',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    CONSTRAINT fk_exam_course FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='试卷表';

-- 试卷题目关联表 (exam_paper_question)
CREATE TABLE IF NOT EXISTS exam_paper_question (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '关联ID',
    exam_id BIGINT NOT NULL COMMENT '试卷ID',
    question_id BIGINT NOT NULL COMMENT '题目ID',
    sort_order INT DEFAULT 0 COMMENT '排序',
    custom_score INT COMMENT '自定义分值(覆盖题目默认分)',
    UNIQUE KEY uk_exam_question (exam_id, question_id),
    CONSTRAINT fk_epq_exam FOREIGN KEY (exam_id) REFERENCES exam(id) ON DELETE CASCADE,
    CONSTRAINT fk_epq_question FOREIGN KEY (question_id) REFERENCES question(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='试卷题目关联表';

-- 考试记录表 (exam_record)
CREATE TABLE IF NOT EXISTS exam_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
    exam_id BIGINT NOT NULL COMMENT '试卷ID',
    user_id BIGINT NOT NULL COMMENT '学员ID',
    attempt_number INT NOT NULL DEFAULT 1 COMMENT '第几次考试',
    answers JSON COMMENT '考生答案(JSON)',
    score INT COMMENT '得分',
    is_passed TINYINT DEFAULT 0 COMMENT '是否及格',
    start_time DATETIME NOT NULL COMMENT '开始时间',
    submit_time DATETIME COMMENT '提交时间',
    status ENUM('in_progress', 'submitted', 'graded') DEFAULT 'in_progress' COMMENT '状态',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    CONSTRAINT fk_er_exam FOREIGN KEY (exam_id) REFERENCES exam(id) ON DELETE CASCADE,
    CONSTRAINT fk_er_user FOREIGN KEY (user_id) REFERENCES sys_user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='考试记录表';

-- ========================================
-- 证书相关表
-- ========================================

-- 证书模板表 (certificate_template)
CREATE TABLE IF NOT EXISTS certificate_template (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '模板ID',
    name VARCHAR(100) NOT NULL COMMENT '模板名称',
    template_code VARCHAR(100) NOT NULL UNIQUE COMMENT '模板标识码',
    background VARCHAR(255) COMMENT '背景图URL',
    fields JSON COMMENT '动态字段配置',
    content_html TEXT COMMENT 'HTML内容',
    status TINYINT DEFAULT 1 COMMENT '状态',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='证书模板表';

-- 证书表 (certificate)
CREATE TABLE IF NOT EXISTS certificate (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '证书ID',
    user_id BIGINT NOT NULL COMMENT '学员ID',
    course_id BIGINT NOT NULL COMMENT '课程ID',
    template_id BIGINT NOT NULL COMMENT '模板ID',
    certificate_no VARCHAR(50) NOT NULL UNIQUE COMMENT '证书编号',
    issue_date DATETIME NOT NULL COMMENT '发证日期',
    expire_date DATETIME COMMENT '有效期至(空为永久)',
    verify_code VARCHAR(32) NOT NULL UNIQUE COMMENT '验证码',
    status ENUM('valid', 'revoked', 'expired') DEFAULT 'valid' COMMENT '状态',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    CONSTRAINT fk_cert_user FOREIGN KEY (user_id) REFERENCES sys_user(id) ON DELETE CASCADE,
    CONSTRAINT fk_cert_course FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE,
    CONSTRAINT fk_cert_template FOREIGN KEY (template_id) REFERENCES certificate_template(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='证书表';

-- ========================================
-- 互动相关表
-- ========================================

-- 课程评价表 (review)
CREATE TABLE IF NOT EXISTS review (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '评价ID',
    course_id BIGINT NOT NULL COMMENT '课程ID',
    user_id BIGINT NOT NULL COMMENT '学员ID',
    rating TINYINT NOT NULL COMMENT '评分(1-5)',
    content TEXT COMMENT '评价内容',
    reply_content TEXT COMMENT '讲师回复',
    reply_time DATETIME COMMENT '回复时间',
    status TINYINT DEFAULT 1 COMMENT '状态(0隐藏,1显示)',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_course_user (course_id, user_id),
    CONSTRAINT fk_review_course FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE,
    CONSTRAINT fk_review_user FOREIGN KEY (user_id) REFERENCES sys_user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程评价表';

-- 评论表 (comment)
CREATE TABLE IF NOT EXISTS comment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '评论ID',
    user_id BIGINT NOT NULL COMMENT '评论人ID',
    content_id BIGINT NOT NULL COMMENT '关联内容ID',
    chapter_id BIGINT COMMENT '关联章节ID',
    text TEXT NOT NULL COMMENT '评论内容',
    parent_id BIGINT DEFAULT 0 COMMENT '父评论ID(回复)',
    like_count INT DEFAULT 0 COMMENT '点赞数',
    status TINYINT DEFAULT 1 COMMENT '状态:0隐藏,1显示',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    CONSTRAINT fk_comment_user FOREIGN KEY (user_id) REFERENCES sys_user(id) ON DELETE CASCADE,
    CONSTRAINT fk_comment_content FOREIGN KEY (content_id) REFERENCES content(id) ON DELETE CASCADE,
    CONSTRAINT fk_comment_chapter FOREIGN KEY (chapter_id) REFERENCES chapter(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- 讨论表 (discussion)
CREATE TABLE IF NOT EXISTS discussion (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '讨论ID',
    course_id BIGINT NOT NULL COMMENT '关联课程ID',
    user_id BIGINT NOT NULL COMMENT '发起用户ID',
    title VARCHAR(200) NOT NULL COMMENT '讨论标题',
    content TEXT NOT NULL COMMENT '讨论内容',
    view_count INT DEFAULT 0 COMMENT '浏览数',
    reply_count INT DEFAULT 0 COMMENT '回复数',
    status TINYINT DEFAULT 1 COMMENT '状态',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    CONSTRAINT fk_disc_course FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE,
    CONSTRAINT fk_disc_user FOREIGN KEY (user_id) REFERENCES sys_user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='讨论表';

-- 讨论回复表 (discussion_reply)
CREATE TABLE IF NOT EXISTS discussion_reply (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '回复ID',
    discussion_id BIGINT NOT NULL COMMENT '讨论ID',
    user_id BIGINT NOT NULL COMMENT '回复人ID',
    content TEXT NOT NULL COMMENT '回复内容',
    parent_id BIGINT DEFAULT 0 COMMENT '父回复ID',
    like_count INT DEFAULT 0 COMMENT '点赞数',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    CONSTRAINT fk_dr_discussion FOREIGN KEY (discussion_id) REFERENCES discussion(id) ON DELETE CASCADE,
    CONSTRAINT fk_dr_user FOREIGN KEY (user_id) REFERENCES sys_user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='讨论回复表';

-- 消息通知表 (notification)
CREATE TABLE IF NOT EXISTS notification (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '通知ID',
    user_id BIGINT NOT NULL COMMENT '接收人ID',
    type ENUM('system', 'course', 'exam', 'certificate', 'interaction') NOT NULL COMMENT '通知类型',
    title VARCHAR(200) NOT NULL COMMENT '标题',
    content TEXT NOT NULL COMMENT '内容',
    is_read TINYINT DEFAULT 0 COMMENT '是否已读',
    read_time DATETIME COMMENT '阅读时间',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    CONSTRAINT fk_notif_user FOREIGN KEY (user_id) REFERENCES sys_user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息通知表';

-- ========================================
-- 系统相关表
-- ========================================

-- 学习统计量表 (learning_analytics)
CREATE TABLE IF NOT EXISTS learning_analytics (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '统计ID',
    user_id BIGINT NOT NULL COMMENT '学员ID',
    course_id BIGINT NOT NULL COMMENT '课程ID',
    date DATE NOT NULL COMMENT '统计日期',
    learn_duration INT DEFAULT 0 COMMENT '当日学习时长(分钟)',
    complete_chapters INT DEFAULT 0 COMMENT '当日完成章节数',
    login_count INT DEFAULT 0 COMMENT '当日登录次数',
    UNIQUE KEY uk_user_course_date (user_id, course_id, date),
    CONSTRAINT fk_la_user FOREIGN KEY (user_id) REFERENCES sys_user(id) ON DELETE CASCADE,
    CONSTRAINT fk_la_course FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学习统计量表';

-- 操作日志表 (operation_log)
CREATE TABLE IF NOT EXISTS operation_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
    user_id BIGINT COMMENT '操作用户ID',
    username VARCHAR(50) COMMENT '用户名(冗余)',
    operation VARCHAR(100) NOT NULL COMMENT '操作类型',
    method VARCHAR(200) COMMENT '请求方法',
    params TEXT COMMENT '请求参数',
    ip VARCHAR(50) COMMENT '操作IP',
    duration INT COMMENT '耗时(毫秒)',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    INDEX idx_user_time (user_id, create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- 系统配置表 (sys_config)
CREATE TABLE IF NOT EXISTS sys_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '配置ID',
    config_key VARCHAR(100) NOT NULL UNIQUE COMMENT '配置键',
    config_value TEXT COMMENT '配置值',
    config_type VARCHAR(50) COMMENT '配置类型',
    description VARCHAR(200) COMMENT '描述',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

-- ========================================
-- 初始化数据
-- ========================================

-- 初始化角色数据
INSERT IGNORE INTO sys_role (role_name, role_code, description, status, create_time) VALUES
('管理员', 'admin', '系统管理员', 1, NOW()),
('讲师', 'teacher', '课程讲师', 1, NOW()),
('学员', 'student', '普通学员', 1, NOW());

-- 初始化管理员账号 (密码: admin123)
INSERT IGNORE INTO sys_user (username, password, real_name, email, role, status, create_time, update_time) VALUES
('admin', '$2a$10$vd02FvFkIddy9lxWuQw8jOaIc7f7XHOJSd3ElEYlUvC7.EAbtLd6i', '系统管理员', 'admin@lms.com', 'ADMIN', 1, NOW(), NOW()),
('teacher', '$2a$10$vd02FvFkIddy9lxWuQw8jOaIc7f7XHOJSd3ElEYlUvC7.EAbtLd6i', '张老师', 'teacher@lms.com', 'TEACHER', 1, NOW(), NOW()),
('student', '$2a$10$vd02FvFkIddy9lxWuQw8jOaIc7f7XHOJSd3ElEYlUvC7.EAbtLd6i', '李同学', 'student@lms.com', 'STUDENT', 1, NOW(), NOW());

-- 初始化用户角色关联
INSERT IGNORE INTO sys_user_role (user_id, role_id) VALUES
(1, 1),
(2, 2),
(3, 3);

-- 初始化课程分类
INSERT IGNORE INTO course_category (name, parent_id, sort_order, status) VALUES
('编程技术', 0, 1, 1),
('前端开发', 1, 1, 1),
('后端开发', 1, 2, 1),
('数据科学', 0, 2, 1);

-- 初始化系统配置
INSERT IGNORE INTO sys_config (config_key, config_value, config_type, description) VALUES
('system.name', 'LMS学习管理系统', 'system', '系统名称'),
('system.version', '1.0.0', 'system', '系统版本'),
('jwt.expiration', '7200000', 'security', 'JWT过期时间(毫秒)');

SELECT '数据库初始化完成!' AS message;