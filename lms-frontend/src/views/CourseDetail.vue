<template>
  <div class="course-detail-container">
    <el-page-header @back="goBack" content="课程详情">
      <template #extra>
        <el-button type="primary" @click="handleEnroll" v-if="!isEnrolled">立即选课</el-button>
        <el-button type="success" v-else>已选课</el-button>
      </template>
    </el-page-header>
    
    <el-card class="course-info-card" v-if="course">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-image :src="course.coverImage || '/placeholder.png'" fit="cover" style="width: 100%; height: 200px" />
        </el-col>
        <el-col :span="16">
          <h2>{{ course.title }}</h2>
          <p class="subtitle">{{ course.subtitle }}</p>
          <div class="course-meta">
            <span>分类：{{ course.categoryName }}</span>
            <span>讲师：{{ course.teacherName }}</span>
            <span>难度：
              <el-tag v-if="course.level === 'BEGINNER'" type="success" size="small">初级</el-tag>
              <el-tag v-else-if="course.level === 'INTERMEDIATE'" type="warning" size="small">中级</el-tag>
              <el-tag v-else type="danger" size="small">高级</el-tag>
            </span>
            <span>价格：{{ course.price === 0 ? '免费' : `¥${course.price}` }}</span>
          </div>
          <div class="course-stats">
            <span>学习人数：{{ course.totalStudents }}</span>
            <span>评分：{{ course.rating }}</span>
            <span>时长：{{ course.totalDuration }}分钟</span>
          </div>
          <div class="course-status">
            <el-tag v-if="course.status === 'DRAFT'" type="info">草稿</el-tag>
            <el-tag v-else-if="course.status === 'PUBLISHED'" type="success">已发布</el-tag>
            <el-tag v-else type="danger">已关闭</el-tag>
          </div>
        </el-col>
      </el-row>
      
      <el-divider />
      
      <div class="course-description">
        <h3>课程简介</h3>
        <p>{{ course.description || '暂无简介' }}</p>
      </div>
    </el-card>
    
    <!-- 章节列表 -->
    <el-card class="chapters-card" v-if="chapters.length > 0">
      <template #header>
        <div class="card-header">
          <span>课程章节</span>
        </div>
      </template>
      
      <el-collapse v-model="activeChapters">
        <el-collapse-item
          v-for="chapter in chapters"
          :key="chapter.id"
          :name="chapter.id"
        >
          <template #title>
            <div class="chapter-title">
              <span>{{ chapter.title }}</span>
              <el-tag size="small" v-if="chapter.isFree === 1">免费试看</el-tag>
            </div>
          </template>
          
          <div class="chapter-content" v-if="chapter.contentText">
            <p>{{ chapter.contentText }}</p>
          </div>
          <div class="chapter-video" v-else-if="chapter.contentUrl">
            <video :src="chapter.contentUrl" controls style="width: 100%"></video>
          </div>
        </el-collapse-item>
      </el-collapse>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { courseApi } from '../api'
import type { Course, Chapter } from '../types'

const route = useRoute()
const router = useRouter()

// 课程详情
const course = ref<Course>()
const chapters = ref<Chapter[]>([])
const activeChapters = ref<number[]>([])
const isEnrolled = ref(false)

// 返回上一页
const goBack = () => {
  router.back()
}

// 加载课程详情
const loadCourseDetail = async () => {
  const courseId = Number(route.params.id)
  try {
    const res = await courseApi.getCourseById(courseId)
    course.value = res.data
  } catch (error) {
    console.error(error)
    ElMessage.error('加载课程详情失败')
  }
}

// 选课
const handleEnroll = async () => {
  const courseId = Number(route.params.id)
  try {
    // 这里应该调用选课API
    ElMessage.success('选课成功')
    isEnrolled.value = true
  } catch (error) {
    console.error(error)
  }
}

onMounted(() => {
  loadCourseDetail()
})
</script>

<style scoped>
.course-detail-container {
  padding: 20px;
}

.course-info-card {
  margin-top: 20px;
}

.course-info-card h2 {
  margin: 0 0 10px 0;
}

.subtitle {
  color: #909399;
  margin-bottom: 15px;
}

.course-meta {
  display: flex;
  gap: 20px;
  margin-bottom: 15px;
  flex-wrap: wrap;
}

.course-stats {
  display: flex;
  gap: 20px;
  color: #606266;
}

.course-status {
  margin-top: 15px;
}

.course-description {
  margin-top: 20px;
}

.course-description h3 {
  margin-bottom: 10px;
}

.chapters-card {
  margin-top: 20px;
}

.chapter-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.chapter-content {
  padding: 10px;
}

.chapter-video {
  padding: 10px;
}
</style>