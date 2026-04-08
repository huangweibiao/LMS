import request from './request'
import type { LoginRequest, RegisterRequest, LoginResponse, User, PageResponse, Course, CourseCategory, Enrollment } from '../types'

// 认证API
export const authApi = {
  // 登录
  login(data: LoginRequest): Promise<LoginResponse> {
    return request.post('/auth/login', data)
  },
  
  // 注册
  register(data: RegisterRequest): Promise<User> {
    return request.post('/auth/register', data)
  },
  
  // 获取当前用户信息
  getCurrentUser(): Promise<User> {
    return request.get('/auth/me')
  }
}

// 用户API
export const userApi = {
  // 获取用户列表
  getUsers(page: number = 1, size: number = 10, keyword?: string): Promise<PageResponse<User>> {
    return request.get('/users', { params: { page, size, keyword } })
  },
  
  // 获取用户详情
  getUserById(id: number): Promise<User> {
    return request.get(`/users/${id}`)
  },
  
  // 创建用户
  createUser(data: Partial<User>): Promise<User> {
    return request.post('/users', data)
  },
  
  // 更新用户
  updateUser(id: number, data: Partial<User>): Promise<User> {
    return request.put(`/users/${id}`, data)
  },
  
  // 删除用户
  deleteUser(id: number): Promise<void> {
    return request.delete(`/users/${id}`)
  }
}

// 课程API
export const courseApi = {
  // 获取课程列表
  getCourses(page: number = 1, size: number = 10, categoryId?: number, status?: string): Promise<PageResponse<Course>> {
    return request.get('/courses', { params: { page, size, categoryId, status } })
  },
  
  // 获取课程详情
  getCourseById(id: number): Promise<Course> {
    return request.get(`/courses/${id}`)
  },
  
  // 根据分类获取课程
  getCoursesByCategory(categoryId: number): Promise<Course[]> {
    return request.get(`/courses/category/${categoryId}`)
  },
  
  // 创建课程
  createCourse(data: Partial<Course>, teacherId: number = 1): Promise<Course> {
    return request.post('/courses', data, { params: { teacherId } })
  },
  
  // 更新课程
  updateCourse(id: number, data: Partial<Course>): Promise<Course> {
    return request.put(`/courses/${id}`, data)
  },
  
  // 发布课程
  publishCourse(id: number): Promise<Course> {
    return request.put(`/courses/${id}/publish`)
  },
  
  // 删除课程
  deleteCourse(id: number): Promise<void> {
    return request.delete(`/courses/${id}`)
  }
}

// 课程分类API
export const categoryApi = {
  // 获取所有分类
  getAllCategories(): Promise<CourseCategory[]> {
    return request.get('/categories')
  },
  
  // 获取根分类
  getRootCategories(): Promise<CourseCategory[]> {
    return request.get('/categories/root')
  },
  
  // 根据父ID获取子分类
  getCategoriesByParentId(parentId: number): Promise<CourseCategory[]> {
    return request.get(`/categories/parent/${parentId}`)
  },
  
  // 获取分类详情
  getCategoryById(id: number): Promise<CourseCategory> {
    return request.get(`/categories/${id}`)
  },
  
  // 创建分类
  createCategory(data: Partial<CourseCategory>): Promise<CourseCategory> {
    return request.post('/categories', data)
  },
  
  // 更新分类
  updateCategory(id: number, data: Partial<CourseCategory>): Promise<CourseCategory> {
    return request.put(`/categories/${id}`, data)
  },
  
  // 删除分类
  deleteCategory(id: number): Promise<void> {
    return request.delete(`/categories/${id}`)
  }
}

// 选课API
export const enrollmentApi = {
  // 选课
  enrollCourse(userId: number, courseId: number): Promise<Enrollment> {
    return request.post('/enrollments', { userId, courseId })
  },
  
  // 获取用户选课列表
  getUserEnrollments(userId: number): Promise<Enrollment[]> {
    return request.get(`/enrollments/user/${userId}`)
  },
  
  // 获取课程选课列表
  getCourseEnrollments(courseId: number): Promise<Enrollment[]> {
    return request.get(`/enrollments/course/${courseId}`)
  }
}