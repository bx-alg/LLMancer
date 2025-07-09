import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import Prediction from '../views/Prediction.vue'
import ApiKeyManagement from '../views/ApiKeyManagement.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home,
    meta: {
      title: '首页 - LLMancer'
    }
  },
  {
    path: '/prediction',
    name: 'Prediction',
    component: Prediction,
    meta: {
      title: 'AI预测 - LLMancer'
    }
  },
  {
    path: '/api-keys',
    name: 'ApiKeyManagement',
    component: ApiKeyManagement,
    meta: {
      title: 'API密钥管理 - LLMancer'
    }
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    redirect: '/'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫 - 设置页面标题
router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = to.meta.title
  }
  next()
})

export default router