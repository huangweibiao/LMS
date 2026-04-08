<template>
  <div class="courses-container">
    <div class="page-header">
      <h2>课程中心</h2>
      <el-button type="primary" @click="handleCreateCourse">创建课程</el-button>
    </div>
    
    <!-- 搜索筛选 -->
    <el-card class="filter-card">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索课程"
            clearable
            @clear="loadCourses"
          >
            <template #append>
              <el-button :icon="Search" @click="loadCourses" />
            </template>
          </el-input>
        </el-col>
        <el-col :span="4">
          <el-select v-model="filterStatus" placeholder="课程状态" clearable @change="loadCourses">
            <el-option label="全部" value="" />
            <el-option label="草稿" value="DRAFT" />
            <el-option label="已发布" value="PUBLISHED" />
            <el-option label="已关闭" value="CLOSED" />
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-select v-model="filterCategory" placeholder="选择分类" clearable @change="loadCourses">
            <el-option
              v-for="cat in categories"
              :key="cat.id"
              :label="cat.name"
              :value="cat.id"
            />
          </el-select>
        </el-col>
      </el-row>
    </el-card>
    
    <!-- 课程列表 -->
    <el-card class="courses-card">
      <el-table :data="courses" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="coverImage" label="封面" width="100">
          <template #default="{ row }">
            <el-image
              :src="row.coverImage || '/placeholder.png'"
              fit="cover"
              style="width: 60px; height: 40px"
            />
          </template>
        </el-table-column>
        <el-table-column prop="title" label="课程标题" min-width="200" />
        <el-table-column prop="categoryName" label="分类" width="100" />
        <el-table-column prop="teacherName" label="讲师" width="100" />
        <el-table-column prop="level" label="难度" width="80">
          <template #default="{ row }">
            <el-tag v-if="row.level === 'BEGINNER'" type="success">初级</el-tag>
            <el-tag v-else-if="row.level === 'INTERMEDIATE'" type="warning">中级</el-tag>
            <el-tag v-else type="danger">高级</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="price" label="价格" width="80">
          <template #default="{ row }">
            {{ row.price === 0 ? '免费' : `¥${row.price}` }}
          </template>
        </el-table-column>
        <el-table-column prop="totalStudents" label="学习人数" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.status === 'DRAFT'" type="info">草稿</el-tag>
            <el-tag v-else-if="row.status === 'PUBLISHED'" type="success">已发布</el-tag>
            <el-tag v-else type="danger">已关闭</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleViewCourse(row)">查看</el-button>
            <el-button type="primary" link @click="handleEditCourse(row)">编辑</el-button>
            <el-button v-if="row.status === 'DRAFT'" type="success" link @click="handlePublishCourse(row)">发布</el-button>
            <el-button type="danger" link @click="handleDeleteCourse(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.size"
          :total="pagination.total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="loadCourses"
          @current-change="loadCourses"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import { courseApi, categoryApi } from '../api'
import type { Course, CourseCategory } from '../types'

const router = useRouter()

// 搜索和筛选
const searchKeyword = ref('')
const filterStatus = ref('')
const filterCategory = ref<number>()

// 课程数据
const courses = ref<Course[]>([])
const categories = ref<CourseCategory[]>([])
const loading = ref(false)

// 分页信息
const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

// 加载课程列表
const loadCourses = async () => {
  loading.value = true
  try {
    const res = await courseApi.getCourses(
      pagination.page,
      pagination.size,
      filterCategory.value,
      filterStatus.value || undefined
    )
    courses.value = res.data.list
    pagination.total = res.data.total
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

// 加载分类列表
const loadCategories = async () => {
  try {
    const res = await categoryApi.getAllCategories()
    categories.value = res.data
  } catch (error) {
    console.error(error)
  }
}

// 查看课程
const handleViewCourse = (row: Course) => {
  router.push(`/courses/${row.id}`)
}

// 编辑课程
const handleEditCourse = (row: Course) => {
  router.push(`/courses/${row.id}?edit=true`)
}

// 创建课程
const handleCreateCourse = () => {
  router.push('/courses/create')
}

// 发布课程
const handlePublishCourse = async (row: Course) => {
  try {
    await courseApi.publishCourse(row.id)
    ElMessage.success('发布成功')
    loadCourses()
  } catch (error) {
    console.error(error)
  }
}

// 删除课程
const handleDeleteCourse = (row: Course) => {
  ElMessageBox.confirm('确定要删除该课程吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await courseApi.deleteCourse(row.id)
      ElMessage.success('删除成功')
      loadCourses()
    } catch (error) {
      console.error(error)
    }
  })
}

onMounted(() => {
  loadCourses()
  loadCategories()
})
</script>

<style scoped>
.courses-container {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
}

.filter-card {
  margin-bottom: 20px;
}

.courses-card {
  min-height: 500px;
}

.pagination-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}
</style>