// 类型定义文件

// 用户相关类型
export interface User {
  id: number
  username: string
  realName: string
  email: string
  mobile?: string
  avatar?: string
  role: 'ADMIN' | 'TEACHER' | 'STUDENT'
  status: number
  lastLoginTime?: string
  createTime?: string
}

// 登录请求
export interface LoginRequest {
  username: string
  password: string
}

// 注册请求
export interface RegisterRequest {
  username: string
  password: string
  realName: string
  email: string
  mobile?: string
}

// 登录响应
export interface LoginResponse {
  token: string
  tokenType: string
  user: User
}

// 分页响应
export interface PageResponse<T> {
  list: T[]
  total: number
  page: number
  size: number
}

// 课程相关类型
export interface Course {
  id: number
  categoryId: number
  teacherId: number
  teacherName?: string
  categoryName?: string
  title: string
  subtitle?: string
  coverImage?: string
  introVideo?: string
  description?: string
  level: string
  price: number
  totalDuration: number
  totalStudents: number
  rating: number
  status: string
  publishTime?: string
  createTime?: string
  updateTime?: string
}

// 课程分类
export interface CourseCategory {
  id: number
  parentId: number
  name: string
  sortOrder: number
  icon?: string
  status: number
  createTime?: string
}

// 选课记录
export interface Enrollment {
  id: number
  userId: number
  courseId: number
  orderNo?: string
  payAmount: number
  progress: number
  lastLearnTime?: string
  status: string
  enrollTime: string
  completeTime?: string
}

// 章节
export interface Chapter {
  id: number
  courseId: number
  parentId: number
  title: string
  contentType: string
  contentUrl?: string
  contentText?: string
  duration: number
  sortOrder: number
  isFree: number
  createTime?: string
  updateTime?: string
}

// API响应格式
export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
}