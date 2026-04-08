<template>
  <div class="home-container">
    <!-- 顶部导航 -->
    <el-header class="header">
      <div class="header-left">
        <h2>LMS学习管理系统</h2>
      </div>
      <div class="header-right">
        <span class="username">{{ userStore.userInfo?.realName || userStore.userInfo?.username }}</span>
        <el-button type="danger" @click="handleLogout">退出</el-button>
      </div>
    </el-header>
    
    <!-- 主体内容 -->
    <el-main class="main">
      <el-row :gutter="20">
        <el-col :span="4">
          <el-menu
            :default-active="activeMenu"
            class="side-menu"
            @select="handleMenuSelect"
          >
            <el-menu-item index="home">
              <el-icon><House /></el-icon>
              <span>首页</span>
            </el-menu-item>
            <el-menu-item index="courses">
              <el-icon><Reading /></el-icon>
              <span>课程中心</span>
            </el-menu-item>
            <el-menu-item index="my-courses">
              <el-icon><Collection /></el-icon>
              <span>我的选课</span>
            </el-menu-item>
            <el-menu-item index="exams">
              <el-icon><Document /></el-icon>
              <span>考试中心</span>
            </el-menu-item>
            <el-menu-item index="categories">
              <el-icon><FolderOpened /></el-icon>
              <span>分类管理</span>
            </el-menu-item>
            <el-menu-item v-if="isAdmin" index="users">
              <el-icon><User /></el-icon>
              <span>用户管理</span>
            </el-menu-item>
          </el-menu>
        </el-col>
        
        <el-col :span="20">
          <router-view />
        </el-col>
      </el-row>
    </el-main>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '../stores/user'
import { House, Reading, Collection, Document, FolderOpened, User } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

// 当前激活菜单
const activeMenu = ref('home')

// 是否是管理员
const isAdmin = computed(() => userStore.userInfo?.role === 'ADMIN')

// 菜单选择处理
const handleMenuSelect = (index: string) => {
  activeMenu.value = index
  router.push(`/${index}`)
}

// 退出登录
const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}

onMounted(() => {
  // 初始化获取用户信息
  if (userStore.isLoggedIn && !userStore.userInfo) {
    userStore.fetchCurrentUser()
  }
  
  // 设置当前激活菜单
  const path = route.path.replace('/', '')
  if (path) {
    activeMenu.value = path
  }
})
</script>

<style scoped>
.home-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fff;
  border-bottom: 1px solid #e4e7ed;
  padding: 0 20px;
}

.header-left h2 {
  margin: 0;
  color: #409eff;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 15px;
}

.username {
  font-size: 14px;
  color: #606266;
}

.main {
  flex: 1;
  background: #f5f7fa;
  padding: 20px;
}

.side-menu {
  height: 100%;
  border-right: none;
}
</style>