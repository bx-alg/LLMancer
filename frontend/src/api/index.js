import axios from 'axios'

// 创建axios实例
const api = axios.create({
  baseURL: '/api',
  timeout: 60000, // 60秒超时
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
api.interceptors.request.use(
  (config) => {
    console.log('发送请求:', config.method?.toUpperCase(), config.url)
    return config
  },
  (error) => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  (response) => {
    console.log('收到响应:', response.status, response.config.url)
    return response.data
  },
  (error) => {
    console.error('响应错误:', error)
    
    let errorMessage = '网络错误'
    
    if (error.response) {
      // 服务器返回错误状态码
      const { status, data } = error.response
      
      switch (status) {
        case 400:
          errorMessage = data?.error || '请求参数错误'
          break
        case 401:
          errorMessage = '未授权访问'
          break
        case 403:
          errorMessage = '禁止访问'
          break
        case 404:
          errorMessage = '服务不存在'
          break
        case 500:
          errorMessage = data?.error || '服务器内部错误'
          break
        default:
          errorMessage = data?.error || `服务器错误 (${status})`
      }
    } else if (error.request) {
      // 请求发送但没有收到响应
      errorMessage = '网络连接失败，请检查网络设置'
    } else {
      // 其他错误
      errorMessage = error.message || '未知错误'
    }
    
    return Promise.reject(new Error(errorMessage))
  }
)

// API方法
const apiMethods = {
  // 健康检查
  async health() {
    return await api.get('/health')
  },
  
  // 获取文件信息
  async getFileInfo() {
    return await api.get('/file-info')
  },
  
  // 文本预测
  async predict(request) {
    return await api.post('/predict', request)
  },
  
  // 文件预测
  async predictWithFile(file, prompt, outputFormat, options = {}) {
    const formData = new FormData()
    formData.append('file', file)
    formData.append('prompt', prompt)
    formData.append('outputFormat', outputFormat)
    
    // 添加可选参数
    if (options.model) {
      formData.append('model', options.model)
    }
    if (options.temperature !== undefined) {
      formData.append('temperature', options.temperature)
    }
    if (options.maxTokens) {
      formData.append('maxTokens', options.maxTokens)
    }
    
    return await api.post('/predict-with-file', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  }
}

// 导出axios实例供其他模块使用
export { api }

export default apiMethods