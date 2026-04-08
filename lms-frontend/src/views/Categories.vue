<template>
  <div class="categories-container">
    <div class="page-header">
      <h2>分类管理</h2>
      <el-button type="primary" @click="handleCreateCategory">新增分类</el-button>
    </div>
    
    <!-- 分类列表 -->
    <el-card>
      <el-table :data="categories" v-loading="loading" row-key="id" default-expand-all>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="name" label="分类名称" min-width="200" />
        <el-table-column prop="parentId" label="父分类ID" width="100" />
        <el-table-column prop="sortOrder" label="排序" width="80" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEditCategory(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDeleteCategory(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 分类编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form ref="categoryFormRef" :model="categoryForm" :rules="categoryRules" label-width="80px">
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="categoryForm.name" />
        </el-form-item>
        <el-form-item label="父分类">
          <el-select v-model="categoryForm.parentId" placeholder="选择父分类">
            <el-option label="无（根分类）" :value="0" />
            <el-option
              v-for="cat in categories"
              :key="cat.id"
              :label="cat.name"
              :value="cat.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="categoryForm.sortOrder" :min="0" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="categoryForm.status" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveCategory">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox, FormInstance, FormRules } from 'element-plus'
import { categoryApi } from '../api'
import type { CourseCategory } from '../types'

const categories = ref<CourseCategory[]>([])
const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增分类')
const categoryFormRef = ref<FormInstance>()

const categoryForm = reactive({
  id: undefined as number | undefined,
  name: '',
  parentId: 0,
  sortOrder: 0,
  status: 1
})

const categoryRules: FormRules = {
  name: [{ required: true, message: '请输入分类名称', trigger: 'blur' }]
}

const loadCategories = async () => {
  loading.value = true
  try {
    const res = await categoryApi.getAllCategories()
    categories.value = res.data
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const handleCreateCategory = () => {
  dialogTitle.value = '新增分类'
  categoryForm.id = undefined
  categoryForm.name = ''
  categoryForm.parentId = 0
  categoryForm.sortOrder = 0
  categoryForm.status = 1
  dialogVisible.value = true
}

const handleEditCategory = (row: CourseCategory) => {
  dialogTitle.value = '编辑分类'
  categoryForm.id = row.id
  categoryForm.name = row.name
  categoryForm.parentId = row.parentId
  categoryForm.sortOrder = row.sortOrder
  categoryForm.status = row.status
  dialogVisible.value = true
}

const handleSaveCategory = async () => {
  if (!categoryFormRef.value) return
  await categoryFormRef.value.validate(async (valid) => {
    if (!valid) return
    try {
      if (categoryForm.id) {
        await categoryApi.updateCategory(categoryForm.id, categoryForm)
        ElMessage.success('更新成功')
      } else {
        await categoryApi.createCategory(categoryForm)
        ElMessage.success('创建成功')
      }
      dialogVisible.value = false
      loadCategories()
    } catch (error) {
      console.error(error)
    }
  })
}

const handleDeleteCategory = (row: CourseCategory) => {
  ElMessageBox.confirm('确定要删除该分类吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await categoryApi.deleteCategory(row.id)
      ElMessage.success('删除成功')
      loadCategories()
    } catch (error) {
      console.error(error)
    }
  })
}

onMounted(() => {
  loadCategories()
})
</script>

<style scoped>
.categories-container {
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
</style>