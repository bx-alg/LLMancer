<template>
  <div class="prediction-page">
    <div class="container">
      <!-- 页面标题 -->
      <div class="page-header">
        <h1 class="page-title">
          <el-icon class="title-icon"><DataAnalysis /></el-icon>
          AI智能预测
        </h1>
        <p class="page-description">
          上传您的数据文件，输入分析需求，获得AI驱动的智能预测结果
        </p>
      </div>

      <div class="prediction-layout">
        <!-- 左侧：输入区域 -->
        <div class="input-section">
          <el-card class="input-card card-shadow">
            <template #header>
              <div class="card-header">
                <el-icon><Upload /></el-icon>
                <span>数据输入</span>
              </div>
            </template>

            <el-form 
              ref="formRef" 
              :model="form" 
              :rules="rules" 
              label-width="100px"
              class="prediction-form"
            >
              <!-- 数据输入方式选择 -->
              <el-form-item label="输入方式">
                <el-radio-group v-model="inputMode" @change="handleInputModeChange">
                  <el-radio-button label="file">文件上传</el-radio-button>
                  <el-radio-button label="text">文本输入</el-radio-button>
                </el-radio-group>
              </el-form-item>

              <!-- 文件上传 -->
              <el-form-item 
                v-if="inputMode === 'file'" 
                label="数据文件" 
                prop="file"
              >
                <el-upload
                  ref="uploadRef"
                  class="upload-demo"
                  drag
                  :auto-upload="false"
                  :on-change="handleFileChange"
                  :on-remove="handleFileRemove"
                  :accept="acceptedFileTypes"
                  :limit="1"
                >
                  <el-icon class="el-icon--upload"><upload-filled /></el-icon>
                  <div class="el-upload__text">
                    将文件拖到此处，或<em>点击上传</em>
                  </div>
                  <template #tip>
                    <div class="el-upload__tip">
                      支持 {{ supportedFormats }} 格式，文件大小不超过 10MB
                    </div>
                  </template>
                </el-upload>
              </el-form-item>

              <!-- 文本输入 -->
              <el-form-item 
                v-if="inputMode === 'text'" 
                label="源数据" 
                prop="sourceData"
              >
                <el-input
                  v-model="form.sourceData"
                  type="textarea"
                  :rows="8"
                  placeholder="请输入您的源数据..."
                  show-word-limit
                  maxlength="10000"
                />
              </el-form-item>

              <!-- Prompt输入 -->
              <el-form-item label="分析需求" prop="prompt">
                <el-input
                  v-model="form.prompt"
                  type="textarea"
                  :rows="4"
                  placeholder="请描述您希望AI如何分析这些数据，例如：分析销售趋势、预测未来走势、提取关键信息等..."
                  show-word-limit
                  maxlength="1000"
                />
              </el-form-item>

              <!-- 输出格式 -->
              <el-form-item label="输出格式" prop="outputFormat">
                <el-input
                  v-model="form.outputFormat"
                  type="textarea"
                  :rows="3"
                  placeholder="请指定期望的输出格式，例如：JSON格式、表格形式、总结报告等..."
                  show-word-limit
                  maxlength="500"
                />
              </el-form-item>

              <!-- 高级设置 -->
              <el-form-item>
                <el-collapse v-model="advancedSettingsOpen">
                  <el-collapse-item title="高级设置" name="advanced">
                    <div class="advanced-settings">
                      <el-form-item label="AI模型">
                        <el-select v-model="form.model" placeholder="选择AI模型">
                          <el-option label="DeepSeek V3" value="deepseek-chat" />
                          <el-option label="GPT-3.5 Turbo" value="gpt-3.5-turbo" />
                          <el-option label="GPT-4" value="gpt-4" />
                        </el-select>
                      </el-form-item>
                      
                      <el-form-item label="创造性">
                        <el-slider
                          v-model="form.temperature"
                          :min="0"
                          :max="1"
                          :step="0.1"
                          show-tooltip
                          :format-tooltip="formatTemperatureTooltip"
                        />
                      </el-form-item>
                      
                      <el-form-item label="最大Token">
                        <el-input-number
                          v-model="form.maxTokens"
                          :min="100"
                          :max="4000"
                          :step="100"
                          controls-position="right"
                        />
                      </el-form-item>
                    </div>
                  </el-collapse-item>
                </el-collapse>
              </el-form-item>

              <!-- 提交按钮 -->
              <el-form-item>
                <el-button 
                  type="primary" 
                  size="large" 
                  @click="handleSubmit"
                  :loading="predictionStore.isLoading"
                  :disabled="!canSubmit"
                  class="submit-button"
                >
                  <el-icon v-if="!predictionStore.isLoading"><MagicStick /></el-icon>
                  {{ predictionStore.isLoading ? '分析中...' : '开始预测' }}
                </el-button>
                
                <el-button 
                  v-if="predictionStore.hasResult"
                  size="large" 
                  @click="clearResult"
                  class="clear-button"
                >
                  <el-icon><RefreshLeft /></el-icon>
                  重新开始
                </el-button>
              </el-form-item>
            </el-form>
          </el-card>
        </div>

        <!-- 右侧：结果区域 -->
        <div class="result-section">
          <!-- 结果显示 -->
          <el-card v-if="predictionStore.hasResult || predictionStore.hasError" class="result-card card-shadow">
            <template #header>
              <div class="card-header">
                <el-icon><View /></el-icon>
                <span>预测结果</span>
                <div class="result-actions">
                  <el-button 
                    v-if="predictionStore.hasResult"
                    size="small" 
                    @click="copyResult"
                    :icon="DocumentCopy"
                  >
                    复制
                  </el-button>
                  <el-button 
                    v-if="predictionStore.hasResult"
                    size="small" 
                    @click="downloadResult"
                    :icon="Download"
                  >
                    下载
                  </el-button>
                </div>
              </div>
            </template>

            <!-- 成功结果 -->
            <div v-if="predictionStore.hasResult && !predictionStore.hasError" class="result-content">
              <div class="result-meta">
                <el-tag type="success" size="small">
                  <el-icon><SuccessFilled /></el-icon>
                  预测成功
                </el-tag>
                <span class="result-time">
                  耗时: {{ predictionStore.currentResponse?.processingTimeMs }}ms
                </span>
                <span v-if="predictionStore.currentResponse?.tokensUsed" class="result-tokens">
                  Token: {{ predictionStore.currentResponse.tokensUsed }}
                </span>
              </div>
              
              <div class="result-text">
                <pre>{{ predictionStore.currentResponse?.result }}</pre>
              </div>
            </div>

            <!-- 错误结果 -->
            <div v-if="predictionStore.hasError" class="error-content">
              <el-alert
                :title="predictionStore.error"
                type="error"
                :closable="false"
                show-icon
              />
            </div>
          </el-card>

          <!-- 加载状态 -->
          <el-card v-if="predictionStore.isLoading" class="loading-card card-shadow">
            <div class="loading-content">
              <el-icon class="loading-icon"><Loading /></el-icon>
              <h3>AI正在分析您的数据...</h3>
              <p>请稍候，这可能需要几秒钟时间</p>
            </div>
          </el-card>

          <!-- 历史记录 -->
          <el-card v-if="predictionStore.historyCount > 0" class="history-card card-shadow">
            <template #header>
              <div class="card-header">
                <el-icon><Clock /></el-icon>
                <span>历史记录 ({{ predictionStore.historyCount }})</span>
                <el-button 
                  size="small" 
                  type="danger" 
                  text 
                  @click="clearHistory"
                  :icon="Delete"
                >
                  清空
                </el-button>
              </div>
            </template>

            <div class="history-list">
              <div 
                v-for="item in predictionStore.history.slice(0, 5)" 
                :key="item.id"
                class="history-item"
                @click="loadHistoryItem(item)"
              >
                <div class="history-meta">
                  <span class="history-time">
                    {{ formatTime(item.timestamp) }}
                  </span>
                  <el-tag 
                    :type="item.response.success ? 'success' : 'danger'" 
                    size="small"
                  >
                    {{ item.response.success ? '成功' : '失败' }}
                  </el-tag>
                </div>
                <div class="history-prompt text-ellipsis">
                  {{ item.request.prompt || '文件预测' }}
                </div>
              </div>
            </div>
          </el-card>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { usePredictionStore } from '../stores/prediction.js'
import {
  DataAnalysis,
  Upload,
  UploadFilled,
  View,
  MagicStick,
  RefreshLeft,
  DocumentCopy,
  Download,
  SuccessFilled,
  Loading,
  Clock,
  Delete
} from '@element-plus/icons-vue'

const predictionStore = usePredictionStore()

// 表单引用
const formRef = ref()
const uploadRef = ref()

// 输入模式
const inputMode = ref('file')

// 高级设置
const advancedSettingsOpen = ref([])

// 表单数据
const form = reactive({
  file: null,
  sourceData: '',
  prompt: '',
  outputFormat: '',
  model: 'deepseek-chat',
  temperature: 0.7,
  maxTokens: 2000
})

// 表单验证规则
const rules = {
  file: [
    { required: true, message: '请上传数据文件', trigger: 'change' }
  ],
  sourceData: [
    { required: true, message: '请输入源数据', trigger: 'blur' },
    { min: 10, message: '源数据至少需要10个字符', trigger: 'blur' }
  ],
  prompt: [
    { required: true, message: '请输入分析需求', trigger: 'blur' },
    { min: 5, message: '分析需求至少需要5个字符', trigger: 'blur' }
  ],
  outputFormat: [
    { required: true, message: '请指定输出格式', trigger: 'blur' },
    { min: 3, message: '输出格式至少需要3个字符', trigger: 'blur' }
  ]
}

// 支持的文件格式
const acceptedFileTypes = '.txt,.csv,.json,.xml,.log,.md'
const supportedFormats = 'TXT, CSV, JSON, XML, LOG, MD'

// 计算属性
const canSubmit = computed(() => {
  if (inputMode.value === 'file') {
    return form.file && form.prompt && form.outputFormat
  } else {
    return form.sourceData && form.prompt && form.outputFormat
  }
})

// 方法
const handleInputModeChange = () => {
  // 清空相关字段
  if (inputMode.value === 'file') {
    form.sourceData = ''
  } else {
    form.file = null
    if (uploadRef.value) {
      uploadRef.value.clearFiles()
    }
  }
  predictionStore.clearError()
}

const handleFileChange = (file) => {
  form.file = file.raw
  predictionStore.clearError()
}

const handleFileRemove = () => {
  form.file = null
}

const formatTemperatureTooltip = (value) => {
  if (value <= 0.3) return '保守 (更准确)'
  if (value <= 0.7) return '平衡'
  return '创新 (更有创意)'
}

const handleSubmit = async () => {
  try {
    // 表单验证
    const valid = await formRef.value.validate()
    if (!valid) return

    // 根据输入模式调用不同的预测方法
    if (inputMode.value === 'file') {
      await predictionStore.predictWithFile(
        form.file,
        form.prompt,
        form.outputFormat,
        {
          model: form.model,
          temperature: form.temperature,
          maxTokens: form.maxTokens
        }
      )
    } else {
      await predictionStore.predict({
        sourceData: form.sourceData,
        prompt: form.prompt,
        outputFormat: form.outputFormat,
        model: form.model,
        temperature: form.temperature,
        maxTokens: form.maxTokens
      })
    }

    ElMessage.success('预测完成！')
  } catch (error) {
    ElMessage.error(error.message || '预测失败')
  }
}

const clearResult = () => {
  predictionStore.clearCurrentResult()
}

const copyResult = async () => {
  try {
    await navigator.clipboard.writeText(predictionStore.currentResponse.result)
    ElMessage.success('结果已复制到剪贴板')
  } catch (error) {
    ElMessage.error('复制失败')
  }
}

const downloadResult = () => {
  const content = predictionStore.currentResponse.result
  const blob = new Blob([content], { type: 'text/plain;charset=utf-8' })
  const url = URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = url
  link.download = `prediction-result-${Date.now()}.txt`
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
  URL.revokeObjectURL(url)
  ElMessage.success('结果已下载')
}

const clearHistory = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要清空所有历史记录吗？此操作不可恢复。',
      '确认清空',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    predictionStore.clearHistory()
    ElMessage.success('历史记录已清空')
  } catch {
    // 用户取消
  }
}

const loadHistoryItem = (item) => {
  // 加载历史记录到当前表单
  if (item.request.fileName) {
    inputMode.value = 'file'
    // 注意：无法重新设置文件，只能显示文件名
    ElMessage.info(`历史记录：${item.request.fileName}`)
  } else {
    inputMode.value = 'text'
    form.sourceData = item.request.sourceData || ''
  }
  
  form.prompt = item.request.prompt || ''
  form.outputFormat = item.request.outputFormat || ''
  form.model = item.request.model || 'deepseek-chat'
  form.temperature = item.request.temperature || 0.7
  form.maxTokens = item.request.maxTokens || 2000
  
  // 设置当前响应
  predictionStore.currentResponse = item.response
}

const formatTime = (timestamp) => {
  return new Date(timestamp).toLocaleString('zh-CN')
}

// 组件挂载时的初始化
onMounted(() => {
  // 可以在这里添加一些初始化逻辑
})
</script>

<style scoped>
.prediction-page {
  padding: 40px 0;
  min-height: calc(100vh - 140px);
}

.page-header {
  text-align: center;
  margin-bottom: 40px;
}

.page-title {
  font-size: 2.5rem;
  font-weight: 700;
  color: #2c3e50;
  margin-bottom: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
}

.title-icon {
  font-size: 2.5rem;
  color: #667eea;
}

.page-description {
  font-size: 1.1rem;
  color: #7f8c8d;
  max-width: 600px;
  margin: 0 auto;
}

.prediction-layout {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 30px;
  align-items: start;
}

.input-card,
.result-card,
.loading-card,
.history-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 600;
}

.result-actions {
  margin-left: auto;
  display: flex;
  gap: 8px;
}

.prediction-form {
  padding: 20px 0;
}

.upload-demo {
  width: 100%;
}

.advanced-settings {
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
}

.submit-button {
  width: 200px;
  height: 50px;
  font-size: 16px;
  border-radius: 12px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  border: none;
}

.clear-button {
  margin-left: 16px;
  height: 50px;
}

.result-content {
  padding: 20px 0;
}

.result-meta {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid #eee;
}

.result-time,
.result-tokens {
  font-size: 14px;
  color: #909399;
}

.result-text {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 20px;
  max-height: 400px;
  overflow-y: auto;
}

.result-text pre {
  white-space: pre-wrap;
  word-wrap: break-word;
  margin: 0;
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
  font-size: 14px;
  line-height: 1.6;
}

.error-content {
  padding: 20px 0;
}

.loading-card {
  text-align: center;
  padding: 40px 20px;
}

.loading-content h3 {
  margin: 20px 0 10px;
  color: #409eff;
}

.loading-content p {
  color: #909399;
  margin: 0;
}

.loading-icon {
  font-size: 3rem;
  color: #409eff;
  animation: spin 2s linear infinite;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

.history-list {
  max-height: 300px;
  overflow-y: auto;
}

.history-item {
  padding: 12px;
  border-radius: 8px;
  margin-bottom: 8px;
  background: #f8f9fa;
  cursor: pointer;
  transition: all 0.3s ease;
}

.history-item:hover {
  background: #e9ecef;
  transform: translateX(4px);
}

.history-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.history-time {
  font-size: 12px;
  color: #909399;
}

.history-prompt {
  font-size: 14px;
  color: #606266;
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .prediction-layout {
    grid-template-columns: 1fr;
    gap: 20px;
  }
}

@media (max-width: 768px) {
  .prediction-page {
    padding: 20px 0;
  }
  
  .page-title {
    font-size: 2rem;
  }
  
  .submit-button {
    width: 100%;
  }
  
  .clear-button {
    margin-left: 0;
    margin-top: 10px;
    width: 100%;
  }
}
</style>