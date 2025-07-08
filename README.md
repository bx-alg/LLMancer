# LLMancer - AI智能预测平台

基于Spring AI和Vue 3构建的现代化AI预测平台，支持文件上传、自定义Prompt和输出格式，为用户提供强大的AI数据分析能力。

## 🚀 项目特性

### 核心功能
- **文件上传支持** - 支持TXT、CSV、JSON、XML、LOG、MD等多种格式
- **自定义Prompt** - 灵活的分析需求描述
- **输出格式定制** - 自定义结构化输出格式
- **实时预测** - 基于Spring AI的高性能预测引擎
- **结果可视化** - 清晰的结果展示和操作界面
- **历史记录** - 完整的预测历史管理

### 技术特性
- **现代化架构** - 前后端分离设计
- **响应式UI** - 支持多设备访问
- **实时通信** - 高效的API交互
- **状态管理** - 基于Pinia的状态管理
- **类型安全** - 完整的数据验证

## 🛠 技术栈

### 后端技术
- **Spring Boot 3.2.0** - 现代化Java应用框架
- **Spring AI 0.8.1** - AI集成框架
- **Maven** - 项目构建工具
- **Java 17** - 最新LTS版本

### 前端技术
- **Vue 3** - 渐进式JavaScript框架
- **Pinia** - Vue 3状态管理库
- **Element Plus** - Vue 3组件库
- **Vite** - 现代化构建工具
- **Axios** - HTTP客户端

### AI能力
- **OpenAI GPT** - 强大的语言模型
- **自然语言处理** - 智能文本分析
- **多模型支持** - GPT-3.5/GPT-4可选

## 📦 项目结构

```
LLMancer/
├── backend/                 # Spring Boot后端
│   ├── src/main/java/
│   │   └── com/llmancer/
│   │       ├── LLMancerApplication.java
│   │       ├── config/      # 配置类
│   │       ├── controller/  # REST控制器
│   │       ├── dto/         # 数据传输对象
│   │       └── service/     # 业务服务
│   ├── src/main/resources/
│   │   └── application.yml  # 应用配置
│   └── pom.xml             # Maven配置
├── frontend/               # Vue 3前端
│   ├── src/
│   │   ├── api/           # API服务
│   │   ├── components/    # Vue组件
│   │   ├── router/        # 路由配置
│   │   ├── stores/        # Pinia状态管理
│   │   ├── views/         # 页面组件
│   │   ├── App.vue        # 根组件
│   │   └── main.js        # 入口文件
│   ├── index.html         # HTML模板
│   ├── package.json       # 依赖配置
│   └── vite.config.js     # Vite配置
└── README.md              # 项目说明
```

## 🚀 快速开始

### 环境要求
- **Java 17+**
- **Node.js 16+**
- **Maven 3.6+**
- **OpenAI API Key**

### 1. 克隆项目
```bash
git clone <repository-url>
cd LLMancer
```

### 2. 配置后端

#### 设置环境变量
```bash
export OPENAI_API_KEY="your-openai-api-key"
```

或在 `backend/src/main/resources/application.yml` 中配置：
```yaml
spring:
  ai:
    openai:
      api-key: your-openai-api-key
```

#### 启动后端服务
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

后端服务将在 `http://localhost:8080` 启动

### 3. 配置前端

#### 安装依赖
```bash
cd frontend
npm install
```

#### 启动开发服务器
```bash
npm run dev
```

前端应用将在 `http://localhost:5173` 启动

### 4. 访问应用

打开浏览器访问 `http://localhost:5173`，即可开始使用LLMancer！

## 📖 使用指南

### 基本使用流程

1. **选择输入方式**
   - 文件上传：支持多种格式的数据文件
   - 文本输入：直接输入源数据

2. **配置分析参数**
   - 输入分析需求（Prompt）
   - 指定期望的输出格式
   - 调整高级设置（可选）

3. **执行预测**
   - 点击"开始预测"按钮
   - 等待AI分析完成
   - 查看预测结果

4. **结果操作**
   - 复制结果到剪贴板
   - 下载结果文件
   - 查看历史记录

### 高级设置

- **AI模型选择**：GPT-3.5 Turbo 或 GPT-4
- **创造性调节**：控制输出的创新程度（0-1）
- **最大Token数**：限制输出长度（100-4000）

### 支持的文件格式

- **TXT** - 纯文本文件
- **CSV** - 逗号分隔值文件
- **JSON** - JSON格式数据
- **XML** - XML格式数据
- **LOG** - 日志文件
- **MD** - Markdown文件

## 🔧 开发指南

### 后端开发

#### 添加新的API端点
1. 在 `controller` 包中创建新的控制器
2. 在 `service` 包中实现业务逻辑
3. 在 `dto` 包中定义数据传输对象

#### 配置新的AI模型
1. 在 `application.yml` 中添加模型配置
2. 在 `LLMPredictionService` 中添加模型支持

### 前端开发

#### 添加新页面
1. 在 `views` 目录创建新的Vue组件
2. 在 `router/index.js` 中添加路由配置
3. 在导航菜单中添加链接

#### 添加新的API调用
1. 在 `api/index.js` 中添加API方法
2. 在相应的store中调用API
3. 在组件中使用store方法

### 构建部署

#### 后端构建
```bash
cd backend
mvn clean package
java -jar target/llmancer-backend-1.0.0.jar
```

#### 前端构建
```bash
cd frontend
npm run build
```

构建产物在 `frontend/dist` 目录中。

## 🔒 安全考虑

- **API密钥保护**：确保OpenAI API密钥安全存储
- **文件上传限制**：限制文件大小和类型
- **输入验证**：对所有用户输入进行验证
- **CORS配置**：正确配置跨域访问策略

## 🐛 故障排除

### 常见问题

1. **后端启动失败**
   - 检查Java版本是否为17+
   - 确认OpenAI API密钥配置正确
   - 检查端口8080是否被占用

2. **前端无法访问后端**
   - 确认后端服务正常运行
   - 检查CORS配置
   - 验证代理配置

3. **AI预测失败**
   - 检查OpenAI API密钥有效性
   - 确认网络连接正常
   - 检查输入数据格式

### 日志查看

- 后端日志：控制台输出或日志文件
- 前端日志：浏览器开发者工具控制台

## 🤝 贡献指南

1. Fork项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开Pull Request

## 📄 许可证

本项目采用MIT许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 🙏 致谢

- [Spring AI](https://spring.io/projects/spring-ai) - 强大的AI集成框架
- [Vue.js](https://vuejs.org/) - 渐进式JavaScript框架
- [Element Plus](https://element-plus.org/) - Vue 3组件库
- [OpenAI](https://openai.com/) - 提供强大的AI能力

## 📞 联系方式

如有问题或建议，请通过以下方式联系：

- 提交Issue
- 发送邮件
- 创建Discussion

---

**LLMancer** - 让AI为您的数据分析提供智能支持！ 🚀