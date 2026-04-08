<template>
  <div class="exam-list-container">
    <div class="page-header">
      <h2>考试中心</h2>
    </div>
    
    <!-- 试卷列表 -->
    <el-card>
      <el-table :data="exams" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="title" label="试卷标题" min-width="200" />
        <el-table-column prop="courseId" label="课程ID" width="100" />
        <el-table-column prop="totalScore" label="总分" width="80" />
        <el-table-column prop="passScore" label="及格分" width="80" />
        <el-table-column prop="duration" label="时长(分钟)" width="100" />
        <el-table-column prop="questionCount" label="题目数" width="80" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.status === 'DRAFT'" type="info">草稿</el-tag>
            <el-tag v-else-if="row.status === 'PUBLISHED'" type="success">已发布</el-tag>
            <el-tag v-else type="danger">已关闭</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleStartExam(row)">开始考试</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../api/request'
import type { Exam } from '../types'

const router = useRouter()

const exams = ref<Exam[]>([])
const loading = ref(false)

// 加载试卷列表
const loadExams = async () => {
  loading.value = true
  try {
    const res = await request.get('/api/exams/published')
    exams.value = res.data
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

// 开始考试
const handleStartExam = (row: Exam) => {
  ElMessage.info(`开始考试: ${row.title}`)
  router.push(`/exam/${row.id}`)
}

onMounted(() => {
  loadExams()
})
</script>

<style scoped>
.exam-list-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
}
</style>