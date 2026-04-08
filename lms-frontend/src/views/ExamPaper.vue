<template>
  <div class="exam-paper-container">
    <el-page-header @back="goBack" :content="examTitle">
      <template #extra>
        <el-button type="primary" @click="handleSubmitExam" :loading="submitting">
          提交试卷
        </el-button>
      </template>
    </el-page-header>
    
    <el-card class="exam-info-card">
      <el-row :gutter="20">
        <el-col :span="6">总分: {{ examInfo?.totalScore || 0 }}</el-col>
        <el-col :span="6">及格分: {{ examInfo?.passScore || 0 }}</el-col>
        <el-col :span="6">时长: {{ examInfo?.duration || 0 }}分钟</el-col>
        <el-col :span="6">剩余时间: {{ remainingTime }}</el-col>
      </el-row>
    </el-card>
    
    <el-card class="questions-card" v-loading="loading">
      <div v-if="questions.length === 0" class="empty-state">
        暂无题目
      </div>
      <div v-else class="questions-list">
        <div v-for="(q, index) in questions" :key="q.id" class="question-item">
          <div class="question-header">
            <span class="question-no">{{ index + 1 }}.</span>
            <span class="question-type">[{{ getQuestionTypeText(q.questionType) }}]</span>
            <span class="question-score">({{ q.score }}分)</span>
          </div>
          <div class="question-content" v-html="q.content"></div>
          
          <!-- 单选题 -->
          <div v-if="q.questionType === 'SINGLE'" class="answer-options">
            <el-radio-group v-model="answers[q.id]">
              <el-radio v-for="(opt, i) in parseOptions(q.options)" :key="i" :label="String.fromCharCode(65 + i)">
                {{ String.fromCharCode(65 + i) }}. {{ opt }}
              </el-radio>
            </el-radio-group>
          </div>
          
          <!-- 多选题 -->
          <div v-else-if="q.questionType === 'MULTIPLE'" class="answer-options">
            <el-checkbox-group v-model="multiAnswers[q.id]">
              <el-checkbox v-for="(opt, i) in parseOptions(q.options)" :key="i" :label="String.fromCharCode(65 + i)">
                {{ String.fromCharCode(65 + i) }}. {{ opt }}
              </el-checkbox>
            </el-checkbox-group>
          </div>
          
          <!-- 判断题 -->
          <div v-else-if="q.questionType === 'JUDGE'" class="answer-options">
            <el-radio-group v-model="answers[q.id]">
              <el-radio label="T">正确</el-radio>
              <el-radio label="F">错误</el-radio>
            </el-radio-group>
          </div>
          
          <!-- 简答题 -->
          <div v-else class="answer-options">
            <el-input
              v-model="answers[q.id]"
              type="textarea"
              :rows="4"
              placeholder="请输入答案"
            />
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../api/request'

const route = useRoute()
const router = useRouter()

const examId = Number(route.params.id)
const examTitle = ref('考试中')
const examInfo = ref<any>(null)
const questions = ref<any[]>([])
const answers = ref<Record<number, string>>({})
const multiAnswers = ref<Record<number, string[]>>({})
const loading = ref(false)
const submitting = ref(false)
const remainingTime = ref('00:00')
let timer: number | null = null

// 加载考试信息
const loadExamInfo = async () => {
  try {
    const res = await request.get(`/api/exams/${examId}`)
    examInfo.value = res.data
    examTitle.value = res.data.title
    
    // 计算剩余时间（这里简化处理，实际应从后端获取剩余时间）
    const duration = res.data.duration
    const minutes = Math.floor(duration)
    remainingTime.value = `${minutes.toString().padStart(2, '0')}:00`
  } catch (error) {
    console.error(error)
  }
}

// 加载题目列表（这里简化，实际应从后端API获取）
const loadQuestions = async () => {
  loading.value = true
  try {
    // 模拟题目数据
    questions.value = [
      {
        id: 1,
        questionType: 'SINGLE',
        content: '以下哪个是Java的关键词？',
        options: '["class", "interface", "enum", "以上都是"]',
        score: 5
      },
      {
        id: 2,
        questionType: 'MULTIPLE',
        content: '以下哪些是Spring Boot的优点？',
        options: '["快速开发", "自动配置", "微服务支持", "需要大量配置"]',
        score: 10
      },
      {
        id: 3,
        questionType: 'JUDGE',
        content: 'Vue3使用Composition API是必选的。',
        score: 5
      },
      {
        id: 4,
        questionType: 'ESSAY',
        content: '请简述RESTful API的设计原则。',
        score: 20
      }
    ]
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

// 解析选项JSON
const parseOptions = (optionsStr: string) => {
  try {
    return JSON.parse(optionsStr)
  } catch {
    return []
  }
}

// 获取题目类型文本
const getQuestionTypeText = (type: string) => {
  const typeMap: Record<string, string> = {
    SINGLE: '单选题',
    MULTIPLE: '多选题',
    JUDGE: '判断题',
    FILL: '填空题',
    ESSAY: '简答题'
  }
  return typeMap[type] || type
}

// 返回上一页
const goBack = () => {
  router.back()
}

// 提交试卷
const handleSubmitExam = () => {
  ElMessageBox.confirm('确定要提交试卷吗？提交后将无法修改。', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    submitting.value = true
    try {
      // 构造提交数据
      const submitData = {
        examId,
        userId: 1, // 假设当前用户ID为1
        answers: answers.value,
        multiAnswers: multiAnswers.value
      }
      
      // 实际应调用后端API
      // await request.post(`/api/exams/${examId}/submit`, submitData)
      
      ElMessage.success('提交成功')
      router.push('/exams')
    } catch (error) {
      console.error(error)
    } finally {
      submitting.value = false
    }
  }).catch(() => {})
}

// 启动计时器
const startTimer = () => {
  timer = window.setInterval(() => {
    const parts = remainingTime.value.split(':')
    let minutes = parseInt(parts[0])
    let seconds = parseInt(parts[1])
    
    if (seconds > 0) {
      seconds--
    } else if (minutes > 0) {
      minutes--
      seconds = 59
    } else {
      // 时间到，自动提交
      handleSubmitExam()
      return
    }
    
    remainingTime.value = `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`
  }, 1000)
}

onMounted(() => {
  loadExamInfo()
  loadQuestions()
  startTimer()
})

onUnmounted(() => {
  if (timer) {
    clearInterval(timer)
  }
})
</script>

<style scoped>
.exam-paper-container {
  padding: 20px;
}

.exam-info-card {
  margin: 20px 0;
  background: #f0f9eb;
}

.questions-card {
  min-height: 400px;
}

.empty-state {
  text-align: center;
  padding: 40px;
  color: #909399;
}

.questions-list {
  padding: 10px;
}

.question-item {
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #ebeef5;
}

.question-header {
  margin-bottom: 10px;
}

.question-no {
  font-weight: bold;
  font-size: 16px;
  margin-right: 10px;
}

.question-type {
  color: #909399;
  margin-right: 10px;
}

.question-score {
  color: #67c23a;
}

.question-content {
  font-size: 15px;
  margin-bottom: 15px;
  line-height: 1.8;
}

.answer-options {
  padding-left: 20px;
}

.answer-options .el-radio,
.answer-options .el-checkbox {
  display: block;
  margin: 10px 0;
}
</style>