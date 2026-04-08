<template>
  <div class="my-courses-container">
    <div class="page-header">
      <h2>我的选课</h2>
    </div>
    
    <!-- 选课列表 -->
    <el-card>
      <el-table :data="enrollments" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="courseId" label="课程ID" width="80" />
        <el-table-column label="课程名称" min-width="200">
          <template #default="{ row }">
            {{ getCourseName(row.courseId) }}
          </template>
        </el-table-column>
        <el-table-column prop="progress" label="学习进度" width="150">
          <template #default="{ row }">
            <el-progress :percentage="row.progress" :status="row.progress === 100 ? 'success' : ''" />
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.status === 'ACTIVE'" type="primary">学习中</el-tag>
            <el-tag v-else-if="row.status === 'COMPLETED'" type="success">已完成</el-tag>
            <el-tag v-else type="info">已放弃</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="enrollTime" label="选课时间" width="180" />
        <el-table-column prop="lastLearnTime" label="最后学习" width="180" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleContinueLearn(row)">继续学习</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { enrollmentApi, courseApi } from '../api'
import type { Enrollment, Course } from '../types'

const router = useRouter()

const enrollments = ref<Enrollment[]>([])
const courses = ref<Course[]>([])
const loading = ref(false)

// 加载课程列表
const loadCourses = async () => {
  try {
    const res = await courseApi.getCourses(1, 100)
    courses.value = res.data.list
  } catch (error) {
    console.error(error)
  }
}

// 获取课程名称
const getCourseName = (courseId: number) => {
  const course = courses.value.find(c => c.id === courseId)
  return course ? course.title : `课程${courseId}`
}

// 加载选课列表
const loadEnrollments = async () => {
  loading.value = true
  try {
    // 假设当前用户ID为1
    const userId = 1
    const res = await enrollmentApi.getUserEnrollments(userId)
    enrollments.value = res.data
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

// 继续学习
const handleContinueLearn = (row: Enrollment) => {
  router.push(`/courses/${row.courseId}`)
}

onMounted(() => {
  loadCourses()
  loadEnrollments()
})
</script>

<style scoped>
.my-courses-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
}
</style>