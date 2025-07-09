import { defineStore } from 'pinia'
import { api } from '../api/index.js'

export const useApiKeyStore = defineStore('apiKey', {
  state: () => ({
    apiKeys: [],
    providers: [],
    loading: false,
    error: null
  }),

  getters: {
    activeApiKeys: (state) => state.apiKeys.filter(key => key.isActive),
    
    getApiKeyByProvider: (state) => (provider) => {
      return state.apiKeys.find(key => key.provider === provider && key.isActive)
    },
    
    hasActiveKey: (state) => (provider) => {
      return state.apiKeys.some(key => key.provider === provider && key.isActive)
    }
  },

  actions: {
    async fetchApiKeys() {
      this.loading = true
      this.error = null
      try {
        const response = await api.get('/keys')
        this.apiKeys = response
      } catch (error) {
        this.error = error.message || '获取API密钥失败'
        throw error
      } finally {
        this.loading = false
      }
    },

    async fetchProviders() {
      try {
        const response = await api.get('/keys/providers')
        this.providers = response
      } catch (error) {
        this.error = error.message || '获取提供商列表失败'
        throw error
      }
    },

    async createApiKey(apiKeyData) {
      this.loading = true
      this.error = null
      try {
        const response = await api.post('/keys', apiKeyData)
        this.apiKeys.push(response)
        return response
      } catch (error) {
        this.error = error.message || '创建API密钥失败'
        throw error
      } finally {
        this.loading = false
      }
    },

    async updateApiKey(id, apiKeyData) {
      this.loading = true
      this.error = null
      try {
        const response = await api.put(`/keys/${id}`, apiKeyData)
        const index = this.apiKeys.findIndex(key => key.id === id)
        if (index !== -1) {
          this.apiKeys[index] = response
        }
        return response
      } catch (error) {
        this.error = error.message || '更新API密钥失败'
        throw error
      } finally {
        this.loading = false
      }
    },

    async toggleApiKeyStatus(id) {
      this.loading = true
      this.error = null
      try {
        const response = await api.patch(`/keys/${id}/toggle`)
        const index = this.apiKeys.findIndex(key => key.id === id)
        if (index !== -1) {
          this.apiKeys[index] = response
        }
        return response
      } catch (error) {
        this.error = error.message || '切换API密钥状态失败'
        throw error
      } finally {
        this.loading = false
      }
    },

    async deleteApiKey(id) {
      this.loading = true
      this.error = null
      try {
        await api.delete(`/keys/${id}`)
        this.apiKeys = this.apiKeys.filter(key => key.id !== id)
      } catch (error) {
        this.error = error.message || '删除API密钥失败'
        throw error
      } finally {
        this.loading = false
      }
    },

    async checkProviderAvailability(provider) {
      try {
        const response = await api.get(`/keys/providers/${provider}/available`)
        return response
      } catch (error) {
        this.error = error.message || '检查提供商可用性失败'
        throw error
      }
    },

    clearError() {
      this.error = null
    }
  }
})