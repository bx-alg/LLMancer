<template>
  <div class="home">
    <!-- 英雄区域 -->
    <section class="hero-section">
      <div class="container">
        <div class="hero-content">
          <div class="hero-text">
            <h1 class="hero-title">
              <span class="gradient-text">LLMancer</span>
              <br>
              AI智能预测平台
            </h1>
            <p class="hero-description">
              基于Spring AI构建的强大预测平台，支持文件上传、自定义Prompt和输出格式。
              让AI为您的数据分析提供智能洞察。
            </p>
            <div class="hero-actions">
              <el-button 
                type="primary" 
                size="large" 
                @click="goToPrediction"
                class="cta-button"
              >
                <el-icon><DataAnalysis /></el-icon>
                开始预测
              </el-button>
              <el-button 
                size="large" 
                @click="scrollToFeatures"
                class="secondary-button"
              >
                了解更多
              </el-button>
            </div>
          </div>
          <div class="hero-visual">
            <div class="visual-card">
              <el-icon class="visual-icon"><MagicStick /></el-icon>
              <div class="visual-content">
                <h3>智能分析</h3>
                <p>上传数据，获得AI洞察</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 特性介绍 -->
    <section class="features-section" ref="featuresSection">
      <div class="container">
        <div class="section-header">
          <h2 class="section-title">核心特性</h2>
          <p class="section-description">
            LLMancer提供完整的AI预测解决方案，从数据上传到结果展示，一站式服务
          </p>
        </div>
        
        <div class="features-grid">
          <div class="feature-card" v-for="feature in features" :key="feature.id">
            <div class="feature-icon">
              <el-icon><component :is="feature.icon" /></el-icon>
            </div>
            <h3 class="feature-title">{{ feature.title }}</h3>
            <p class="feature-description">{{ feature.description }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- 工作流程 -->
    <section class="workflow-section">
      <div class="container">
        <div class="section-header">
          <h2 class="section-title">使用流程</h2>
          <p class="section-description">
            简单三步，即可获得AI预测结果
          </p>
        </div>
        
        <div class="workflow-steps">
          <div class="step" v-for="(step, index) in workflowSteps" :key="step.id">
            <div class="step-number">{{ index + 1 }}</div>
            <div class="step-content">
              <h3 class="step-title">{{ step.title }}</h3>
              <p class="step-description">{{ step.description }}</p>
            </div>
            <div class="step-icon">
              <el-icon><component :is="step.icon" /></el-icon>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 技术栈 -->
    <section class="tech-section">
      <div class="container">
        <div class="section-header">
          <h2 class="section-title">技术架构</h2>
          <p class="section-description">
            采用现代化技术栈，确保高性能和可扩展性
          </p>
        </div>
        
        <div class="tech-grid">
          <div class="tech-category">
            <h3 class="tech-category-title">后端技术</h3>
            <div class="tech-items">
              <el-tag v-for="tech in backendTech" :key="tech" size="large" class="tech-tag">
                {{ tech }}
              </el-tag>
            </div>
          </div>
          
          <div class="tech-category">
            <h3 class="tech-category-title">前端技术</h3>
            <div class="tech-items">
              <el-tag v-for="tech in frontendTech" :key="tech" size="large" class="tech-tag" type="success">
                {{ tech }}
              </el-tag>
            </div>
          </div>
          
          <div class="tech-category">
            <h3 class="tech-category-title">AI能力</h3>
            <div class="tech-items">
              <el-tag v-for="tech in aiTech" :key="tech" size="large" class="tech-tag" type="warning">
                {{ tech }}
              </el-tag>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- CTA区域 -->
    <section class="cta-section">
      <div class="container">
        <div class="cta-content">
          <h2 class="cta-title">准备开始您的AI预测之旅？</h2>
          <p class="cta-description">
            立即体验LLMancer的强大功能，让AI为您的数据分析提供智能支持
          </p>
          <el-button 
            type="primary" 
            size="large" 
            @click="goToPrediction"
            class="cta-button-large"
          >
            <el-icon><Promotion /></el-icon>
            立即开始
          </el-button>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import {
  DataAnalysis,
  MagicStick,
  Upload,
  Edit,
  View,
  Document,
  Setting,
  Lightning,
  Promotion
} from '@element-plus/icons-vue'

const router = useRouter()
const featuresSection = ref(null)

// 特性数据
const features = [
  {
    id: 1,
    icon: 'Upload',
    title: '文件上传',
    description: '支持多种格式的数据文件上传，包括CSV、JSON、TXT等常见格式'
  },
  {
    id: 2,
    icon: 'Edit',
    title: '自定义Prompt',
    description: '灵活的Prompt编辑功能，让您精确描述分析需求和预期结果'
  },
  {
    id: 3,
    icon: 'Setting',
    title: '输出格式定制',
    description: '自定义输出格式，获得符合您需求的结构化预测结果'
  },
  {
    id: 4,
    icon: 'Lightning',
    title: '实时预测',
    description: '基于Spring AI的高性能预测引擎，快速获得准确的AI分析结果'
  },
  {
    id: 5,
    icon: 'View',
    title: '结果可视化',
    description: '清晰的结果展示界面，支持复制、下载等多种操作方式'
  },
  {
    id: 6,
    icon: 'Document',
    title: '历史记录',
    description: '完整的预测历史记录，方便回顾和比较不同的预测结果'
  }
]

// 工作流程
const workflowSteps = [
  {
    id: 1,
    icon: 'Upload',
    title: '上传数据',
    description: '选择并上传您的源数据文件，支持多种常见格式'
  },
  {
    id: 2,
    icon: 'Edit',
    title: '配置参数',
    description: '输入分析Prompt，指定输出格式，调整模型参数'
  },
  {
    id: 3,
    icon: 'DataAnalysis',
    title: '获得结果',
    description: 'AI模型分析数据并生成预测结果，支持实时查看'
  }
]

// 技术栈
const backendTech = ['Spring Boot', 'Spring AI', 'Maven', 'Java 17']
const frontendTech = ['Vue 3', 'Pinia', 'Element Plus', 'Vite']
const aiTech = ['OpenAI GPT', 'ChatGPT API', '自然语言处理', '智能分析']

// 方法
const goToPrediction = () => {
  router.push('/prediction')
}

const scrollToFeatures = () => {
  featuresSection.value?.scrollIntoView({ behavior: 'smooth' })
}
</script>

<style scoped>
/* 英雄区域 */
.hero-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 80px 0;
  min-height: 600px;
  display: flex;
  align-items: center;
}

.hero-content {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 60px;
  align-items: center;
}

.hero-title {
  font-size: 3.5rem;
  font-weight: 700;
  line-height: 1.2;
  margin-bottom: 24px;
}

.gradient-text {
  background: linear-gradient(45deg, #ffd700, #ffed4e);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.hero-description {
  font-size: 1.2rem;
  line-height: 1.6;
  margin-bottom: 32px;
  color: rgba(255, 255, 255, 0.9);
}

.hero-actions {
  display: flex;
  gap: 16px;
}

.cta-button {
  padding: 12px 32px;
  font-size: 16px;
  border-radius: 12px;
  background: linear-gradient(45deg, #ff6b6b, #ee5a24);
  border: none;
  box-shadow: 0 4px 15px rgba(255, 107, 107, 0.4);
  transition: all 0.3s ease;
}

.cta-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(255, 107, 107, 0.6);
}

.secondary-button {
  padding: 12px 32px;
  font-size: 16px;
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.1);
  border: 2px solid rgba(255, 255, 255, 0.3);
  color: white;
  backdrop-filter: blur(10px);
}

.visual-card {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  padding: 40px;
  text-align: center;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.visual-icon {
  font-size: 4rem;
  color: #ffd700;
  margin-bottom: 20px;
}

.visual-content h3 {
  font-size: 1.5rem;
  margin-bottom: 12px;
}

/* 通用区域样式 */
.features-section,
.workflow-section,
.tech-section {
  padding: 80px 0;
}

.section-header {
  text-align: center;
  margin-bottom: 60px;
}

.section-title {
  font-size: 2.5rem;
  font-weight: 700;
  color: #2c3e50;
  margin-bottom: 16px;
}

.section-description {
  font-size: 1.1rem;
  color: #7f8c8d;
  max-width: 600px;
  margin: 0 auto;
}

/* 特性网格 */
.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 30px;
}

.feature-card {
  background: white;
  padding: 40px 30px;
  border-radius: 16px;
  text-align: center;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  border: 1px solid #f0f0f0;
}

.feature-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
}

.feature-icon {
  width: 80px;
  height: 80px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 24px;
  font-size: 2rem;
  color: white;
}

.feature-title {
  font-size: 1.3rem;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 16px;
}

.feature-description {
  color: #7f8c8d;
  line-height: 1.6;
}

/* 工作流程 */
.workflow-steps {
  display: flex;
  flex-direction: column;
  gap: 40px;
  max-width: 800px;
  margin: 0 auto;
}

.step {
  display: grid;
  grid-template-columns: 80px 1fr 80px;
  gap: 30px;
  align-items: center;
  padding: 30px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.step-number {
  width: 60px;
  height: 60px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
  font-weight: 700;
}

.step-title {
  font-size: 1.3rem;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 8px;
}

.step-description {
  color: #7f8c8d;
  line-height: 1.6;
}

.step-icon {
  font-size: 2.5rem;
  color: #667eea;
}

/* 技术栈 */
.tech-section {
  background: #f8f9fa;
}

.tech-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 40px;
}

.tech-category {
  background: white;
  padding: 40px 30px;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.tech-category-title {
  font-size: 1.3rem;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 24px;
  text-align: center;
}

.tech-items {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  justify-content: center;
}

.tech-tag {
  padding: 8px 16px;
  font-size: 14px;
  border-radius: 20px;
}

/* CTA区域 */
.cta-section {
  background: linear-gradient(135deg, #2c3e50 0%, #34495e 100%);
  color: white;
  padding: 80px 0;
  text-align: center;
}

.cta-title {
  font-size: 2.5rem;
  font-weight: 700;
  margin-bottom: 20px;
}

.cta-description {
  font-size: 1.2rem;
  margin-bottom: 40px;
  color: rgba(255, 255, 255, 0.9);
  max-width: 600px;
  margin-left: auto;
  margin-right: auto;
}

.cta-button-large {
  padding: 16px 48px;
  font-size: 18px;
  border-radius: 12px;
  background: linear-gradient(45deg, #ff6b6b, #ee5a24);
  border: none;
  box-shadow: 0 4px 15px rgba(255, 107, 107, 0.4);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .hero-content {
    grid-template-columns: 1fr;
    gap: 40px;
    text-align: center;
  }
  
  .hero-title {
    font-size: 2.5rem;
  }
  
  .hero-actions {
    justify-content: center;
  }
  
  .step {
    grid-template-columns: 1fr;
    text-align: center;
    gap: 20px;
  }
  
  .section-title {
    font-size: 2rem;
  }
  
  .cta-title {
    font-size: 2rem;
  }
}
</style>