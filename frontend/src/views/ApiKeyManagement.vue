<template>
  <div class="api-key-management">
    <div class="container">
      <!-- 页面标题 -->
      <div class="page-header">
        <h1 class="page-title">
          <el-icon class="title-icon"><Key /></el-icon>
          API密钥管理
        </h1>
        <p class="page-description">
          管理不同LLM提供商的API密钥，支持通义千问、DeepSeek和ChatGPT
        </p>
      </div>

      <!-- 操作栏 -->
      <div class="action-bar">
        <el-button 
          type="primary" 
          @click="showAddDialog = true"
          :icon="Plus"
        >
          添加API密钥
        </el-button>
        <el-button 
          @click="refreshApiKeys"
          :icon="Refresh"
        >
          刷新
        </el-button>
      </div>

      <!-- API密钥列表 -->
      <el-card class="api-key-list">
        <el-table 
          :data="apiKeys" 
          v-loading="loading"
          stripe
          style="width: 100%"
        >
          <el-table-column prop="provider" label="提供商" width="150">
            <template #default="scope">
              <el-tag 
                :type="getProviderTagType(scope.row.provider)"
                size="small"
              >
                {{ getProviderDisplayName(scope.row.provider) }}
              </el-tag>
            </template>
          </el-table-column>
          
          <el-table-column prop="displayName" label="显示名称" width="200" />
          
          <el-table-column prop="maskedApiKey" label="API密钥" width="200">
            <template #default="scope">
              <code class="masked-key">{{ scope.row.maskedApiKey }}</code>
            </template>
          </el-table-column>
          
          <el-table-column prop="isActive" label="状态" width="100">
            <template #default="scope">
              <el-tag 
                :type="scope.row.isActive ? 'success' : 'danger'"
                size="small"
              >
                {{ scope.row.isActive ? '激活' : '禁用' }}
              </el-tag>
            </template>
          </el-table-column>
          
          <el-table-column prop="createdAt" label="创建时间" width="180">
            <template #default="scope">
              {{ formatTime(scope.row.createdAt) }}
            </template>
          </el-table-column>
          
          <el-table-column label="操作" width="200">
            <template #default="scope">
              <el-button 
                size="small" 
                @click="toggleStatus(scope.row)"
                :type="scope.row.isActive ? 'warning' : 'success'"
                text
              >
                {{ scope.row.isActive ? '禁用' : '启用' }}
              </el-button>
              <el-button 
                size="small" 
                type="primary" 
                @click="editApiKey(scope.row)"
                text
              >
                编辑
              </el-button>
              <el-button 
                size="small" 
                type="danger" 
                @click="deleteApiKey(scope.row)"
                text
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <!-- 添加/编辑对话框 -->
      <el-dialog 
        :title="editingApiKey ? '编辑API密钥' : '添加API密钥'"
        v-model="showAddDialog"
        width="500px"
        @close="resetForm"
      >
        <el-form 
          ref="formRef" 
          :model="form" 
          :rules="rules" 
          label-width="100px"
        >
          <el-form-item label="提供商" prop="provider">
            <el-select v-model="form.provider" placeholder="选择LLM提供商" style="width: 100%">
              <el-option 
                v-for="provider in providers" 
                :key="provider" 
                :label="getProviderDisplayName(provider)" 
                :value="provider"
              />
            </el-select>
          </el-form-item>
          
          <el-form-item label="API密钥" prop="apiKey">
            <el-input 
              v-model="form.apiKey" 
              type="password" 
              placeholder="请输入API密钥"
              show-password
            />
          </el-form-item>
          
          <el-form-item label="显示名称" prop="displayName">
            <el-input 
              v-model="form.displayName" 
              placeholder="可选，用于标识此密钥"
            />
          </el-form-item>
        </el-form>
        
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="showAddDialog = false">取消</el-button>
            <el-button 
              type="primary" 
              @click="submitForm"
              :loading="submitting"
            >
              {{ editingApiKey ? '更新' : '添加' }}
            </el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useApiKeyStore } from '../stores/apiKey.js'
import {
  Key,
  Plus,
  Refresh
} from '@element-plus/icons-vue'

const apiKeyStore = useApiKeyStore()

// 响应式数据
const loading = ref(false)
const submitting = ref(false)
const showAddDialog = ref(false)
const editingApiKey = ref(null)
const formRef = ref()

// 表单数据
const form = reactive({
  provider: '',
  apiKey: '',
  displayName: ''
})

// 表单验证规则
const rules = {
  provider: [
    { required: true, message: '请选择LLM提供商', trigger: 'change' }
  ],
  apiKey: [
    { required: true, message: '请输入API密钥', trigger: 'blur' },
    { min: 10, message: 'API密钥长度至少10个字符', trigger: 'blur' }
  ]
}

// 计算属性
const apiKeys = computed(() => apiKeyStore.apiKeys)
const providers = computed(() => apiKeyStore.providers)

// 方法
const refreshApiKeys = async () => {
  loading.value = true
  try {
    await apiKeyStore.fetchApiKeys()
  } catch (error) {
    ElMessage.error('获取API密钥列表失败')
  } finally {
    loading.value = false
  }
}

const submitForm = async () => {
  try {
    const valid = await formRef.value.validate()
    if (!valid) return
    
    submitting.value = true
    
    if (editingApiKey.value) {
      await apiKeyStore.updateApiKey(editingApiKey.value.id, form)
      ElMessage.success('API密钥更新成功')
    } else {
      await apiKeyStore.createApiKey(form)
      ElMessage.success('API密钥添加成功')
    }
    
    showAddDialog.value = false
    await refreshApiKeys()
  } catch (error) {
    ElMessage.error(editingApiKey.value ? 'API密钥更新失败' : 'API密钥添加失败')
  } finally {
    submitting.value = false
  }
}

const editApiKey = (apiKey) => {
  editingApiKey.value = apiKey
  form.provider = apiKey.provider
  form.apiKey = ''
  form.displayName = apiKey.displayName
  showAddDialog.value = true
}

const toggleStatus = async (apiKey) => {
  try {
    await apiKeyStore.toggleApiKeyStatus(apiKey.id)
    ElMessage.success(`API密钥已${apiKey.isActive ? '禁用' : '启用'}`)
    await refreshApiKeys()
  } catch (error) {
    ElMessage.error('状态切换失败')
  }
}

const deleteApiKey = async (apiKey) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除 "${apiKey.displayName || getProviderDisplayName(apiKey.provider)}" 的API密钥吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await apiKeyStore.deleteApiKey(apiKey.id)
    ElMessage.success('API密钥删除成功')
    await refreshApiKeys()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('API密钥删除失败')
    }
  }
}

const resetForm = () => {
  editingApiKey.value = null
  form.provider = ''
  form.apiKey = ''
  form.displayName = ''
  if (formRef.value) {
    formRef.value.resetFields()
  }
}

const getProviderDisplayName = (provider) => {
  const names = {
    'QWEN': '通义千问',
    'DEEPSEEK': 'DeepSeek',
    'OPENAI': 'ChatGPT'
  }
  return names[provider] || provider
}

const getProviderTagType = (provider) => {
  const types = {
    'QWEN': 'primary',
    'DEEPSEEK': 'success',
    'OPENAI': 'warning'
  }
  return types[provider] || 'info'
}

const formatTime = (timeString) => {
  if (!timeString) return ''
  return new Date(timeString).toLocaleString('zh-CN')
}

// 生命周期
onMounted(async () => {
  await apiKeyStore.fetchProviders()
  await refreshApiKeys()
})
</script>

<style scoped>
.api-key-management {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px 0;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.page-header {
  text-align: center;
  margin-bottom: 30px;
  color: white;
}

.page-title {
  font-size: 2.5rem;
  font-weight: 600;
  margin-bottom: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 15px;
}

.title-icon {
  font-size: 2.5rem;
}

.page-description {
  font-size: 1.1rem;
  opacity: 0.9;
  margin: 0;
}

.action-bar {
  margin-bottom: 20px;
  display: flex;
  gap: 10px;
}

.api-key-list {
  background: white;
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
}

.masked-key {
  background: #f5f5f5;
  padding: 4px 8px;
  border-radius: 4px;
  font-family: 'Courier New', monospace;
  font-size: 12px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

:deep(.el-table) {
  border-radius: 8px;
}

:deep(.el-table th) {
  background-color: #f8f9fa;
  font-weight: 600;
}

:deep(.el-dialog) {
  border-radius: 12px;
}

:deep(.el-dialog__header) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-radius: 12px 12px 0 0;
  padding: 20px;
}

:deep(.el-dialog__title) {
  color: white;
  font-weight: 600;
}
</style>