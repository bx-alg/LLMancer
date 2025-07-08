import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import api from '../api/index.js'

export const usePredictionStore = defineStore('prediction', () => {
  // 状态
  const isLoading = ref(false)
  const currentRequest = ref(null)
  const currentResponse = ref(null)
  const history = ref([])
  const error = ref(null)
  
  // 计算属性
  const hasResult = computed(() => currentResponse.value !== null)
  const hasError = computed(() => error.value !== null)
  const historyCount = computed(() => history.value.length)
  
  // 操作
  const predict = async (request) => {
    try {
      isLoading.value = true
      error.value = null
      currentRequest.value = request
      
      const response = await api.predict(request)
      
      currentResponse.value = response
      
      // 添加到历史记录
      addToHistory({
        id: Date.now(),
        request: { ...request },
        response: { ...response },
        timestamp: new Date().toISOString()
      })
      
      return response
    } catch (err) {
      error.value = err.message || '预测失败'
      currentResponse.value = null
      throw err
    } finally {
      isLoading.value = false
    }
  }
  
  const predictWithFile = async (file, prompt, outputFormat, options = {}) => {
    try {
      isLoading.value = true
      error.value = null
      
      const request = {
        file,
        prompt,
        outputFormat,
        ...options
      }
      
      currentRequest.value = request
      
      const response = await api.predictWithFile(file, prompt, outputFormat, options)
      
      currentResponse.value = response
      
      // 添加到历史记录
      addToHistory({
        id: Date.now(),
        request: {
          fileName: file.name,
          fileSize: file.size,
          prompt,
          outputFormat,
          ...options
        },
        response: { ...response },
        timestamp: new Date().toISOString()
      })
      
      return response
    } catch (err) {
      error.value = err.message || '预测失败'
      currentResponse.value = null
      throw err
    } finally {
      isLoading.value = false
    }
  }
  
  const addToHistory = (item) => {
    history.value.unshift(item)
    // 限制历史记录数量
    if (history.value.length > 50) {
      history.value = history.value.slice(0, 50)
    }
    // 保存到本地存储
    saveHistoryToLocal()
  }
  
  const clearHistory = () => {
    history.value = []
    localStorage.removeItem('llmancer_history')
  }
  
  const removeHistoryItem = (id) => {
    const index = history.value.findIndex(item => item.id === id)
    if (index > -1) {
      history.value.splice(index, 1)
      saveHistoryToLocal()
    }
  }
  
  const loadHistoryFromLocal = () => {
    try {
      const saved = localStorage.getItem('llmancer_history')
      if (saved) {
        history.value = JSON.parse(saved)
      }
    } catch (err) {
      console.warn('加载历史记录失败:', err)
    }
  }
  
  const saveHistoryToLocal = () => {
    try {
      localStorage.setItem('llmancer_history', JSON.stringify(history.value))
    } catch (err) {
      console.warn('保存历史记录失败:', err)
    }
  }
  
  const clearCurrentResult = () => {
    currentRequest.value = null
    currentResponse.value = null
    error.value = null
  }
  
  const clearError = () => {
    error.value = null
  }
  
  // 初始化时加载历史记录
  loadHistoryFromLocal()
  
  return {
    // 状态
    isLoading,
    currentRequest,
    currentResponse,
    history,
    error,
    
    // 计算属性
    hasResult,
    hasError,
    historyCount,
    
    // 操作
    predict,
    predictWithFile,
    clearHistory,
    removeHistoryItem,
    clearCurrentResult,
    clearError
  }
})