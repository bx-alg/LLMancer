<template>
  <div id="app">
    <!-- 顶部导航栏 -->
    <el-header class="app-header">
      <div class="container">
        <div class="header-content">
          <div class="logo-section">
            <h1 class="logo">
              <router-link to="/" class="logo-link">
                <el-icon class="logo-icon"><MagicStick /></el-icon>
                LLMancer
              </router-link>
            </h1>
            <span class="subtitle">AI智能预测平台</span>
          </div>
          
          <nav class="nav-menu">
            <el-menu
              :default-active="activeIndex"
              mode="horizontal"
              :ellipsis="false"
              @select="handleSelect"
              class="nav-menu-items"
            >
              <el-menu-item index="/">
                <el-icon><House /></el-icon>
                <span>首页</span>
              </el-menu-item>
              <el-menu-item index="/prediction">
                <el-icon><DataAnalysis /></el-icon>
                <span>AI预测</span>
              </el-menu-item>
            </el-menu>
          </nav>
          
          <div class="header-actions">
            <el-tooltip content="GitHub" placement="bottom">
              <el-button 
                circle 
                size="small" 
                @click="openGitHub"
                class="action-btn"
              >
                <el-icon><Link /></el-icon>
              </el-button>
            </el-tooltip>
          </div>
        </div>
      </div>
    </el-header>
    
    <!-- 主要内容区域 -->
    <el-main class="app-main">
      <div class="main-content">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </div>
    </el-main>
    
    <!-- 底部 -->
    <el-footer class="app-footer">
      <div class="container">
        <div class="footer-content">
          <div class="footer-info">
            <p>&copy; 2024 LLMancer. 基于 Spring AI 和 Vue 3 构建</p>
          </div>
          <div class="footer-links">
            <span class="tech-stack">
              <el-tag size="small" type="info">Spring Boot</el-tag>
              <el-tag size="small" type="success">Spring AI</el-tag>
              <el-tag size="small" type="primary">Vue 3</el-tag>
              <el-tag size="small" type="warning">Pinia</el-tag>
            </span>
          </div>
        </div>
      </div>
    </el-footer>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  MagicStick,
  House,
  DataAnalysis,
  Link
} from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()

// 当前激活的菜单项
const activeIndex = computed(() => route.path)

// 处理菜单选择
const handleSelect = (key) => {
  if (key !== route.path) {
    router.push(key)
  }
}

// 打开GitHub
const openGitHub = () => {
  window.open('https://github.com', '_blank')
}

// 组件挂载时的初始化
onMounted(() => {
  // 可以在这里添加一些初始化逻辑
  console.log('LLMancer应用已启动')
})
</script>

<style scoped>
.app-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 0;
  height: 70px;
  line-height: 70px;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
}

.logo-section {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo {
  margin: 0;
  font-size: 24px;
  font-weight: 700;
}

.logo-link {
  display: flex;
  align-items: center;
  gap: 8px;
  color: white;
  text-decoration: none;
  transition: all 0.3s ease;
}

.logo-link:hover {
  transform: translateY(-1px);
  text-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}

.logo-icon {
  font-size: 28px;
  color: #ffd700;
}

.subtitle {
  color: rgba(255, 255, 255, 0.8);
  font-size: 12px;
  font-weight: 400;
}

.nav-menu {
  flex: 1;
  display: flex;
  justify-content: center;
}

.nav-menu-items {
  background: transparent;
  border: none;
}

.nav-menu-items :deep(.el-menu-item) {
  color: rgba(255, 255, 255, 0.9);
  border-bottom: 2px solid transparent;
  transition: all 0.3s ease;
}

.nav-menu-items :deep(.el-menu-item:hover),
.nav-menu-items :deep(.el-menu-item.is-active) {
  color: white;
  background: rgba(255, 255, 255, 0.1);
  border-bottom-color: #ffd700;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.action-btn {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  color: white;
  transition: all 0.3s ease;
}

.action-btn:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateY(-1px);
}

.app-main {
  min-height: calc(100vh - 140px);
  padding: 0;
  background-color: #f5f7fa;
}

.main-content {
  width: 100%;
  min-height: 100%;
}

.app-footer {
  background: #2c3e50;
  color: white;
  padding: 20px 0;
  height: auto;
}

.footer-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.footer-info p {
  margin: 0;
  font-size: 14px;
  color: rgba(255, 255, 255, 0.8);
}

.tech-stack {
  display: flex;
  gap: 8px;
  align-items: center;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .header-content {
    flex-direction: column;
    height: auto;
    padding: 10px 0;
  }
  
  .logo-section {
    margin-bottom: 10px;
  }
  
  .nav-menu {
    width: 100%;
  }
  
  .header-actions {
    margin-top: 10px;
  }
  
  .footer-content {
    flex-direction: column;
    gap: 15px;
    text-align: center;
  }
  
  .tech-stack {
    justify-content: center;
  }
}

@media (max-width: 480px) {
  .logo {
    font-size: 20px;
  }
  
  .subtitle {
    display: none;
  }
  
  .nav-menu-items :deep(.el-menu-item span) {
    display: none;
  }
}
</style>