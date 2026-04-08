import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { User } from '../types'
import { authApi } from '../api'

export const useUserStore = defineStore('user', () => {
  // 状态
  const token = ref<string>(localStorage.getItem('lms_token') || '')
  const userInfo = ref<User | null>(null)
  
  // 计算属性
  const isLoggedIn = computed(() => !!token.value)
  const userRole = computed(() => userInfo.value?.role || '')
  
  // 设置Token
  const setToken = (newToken: string) => {
    token.value = newToken
    localStorage.setItem('lms_token', newToken)
  }
  
  // 清除Token
  const clearToken = () => {
    token.value = ''
    localStorage.removeItem('lms_token')
  }
  
  // 设置用户信息
  const setUserInfo = (info: User) => {
    userInfo.value = info
  }
  
  // 清除用户信息
  const clearUserInfo = () => {
    userInfo.value = null
  }
  
  // 登录
  const login = async (username: string, password: string) => {
    const response = await authApi.login({ username, password })
    setToken(response.token)
    setUserInfo(response.user)
    return response
  }
  
  // 登出
  const logout = () => {
    clearToken()
    clearUserInfo()
  }
  
  // 获取当前用户信息
  const fetchCurrentUser = async () => {
    if (token.value) {
      try {
        const user = await authApi.getCurrentUser()
        setUserInfo(user)
      } catch (error) {
        clearToken()
        clearUserInfo()
      }
    }
  }
  
  return {
    token,
    userInfo,
    isLoggedIn,
    userRole,
    setToken,
    clearToken,
    setUserInfo,
    clearUserInfo,
    login,
    logout,
    fetchCurrentUser
  }
})