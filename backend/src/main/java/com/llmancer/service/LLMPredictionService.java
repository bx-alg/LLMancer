package com.llmancer.service;

import com.llmancer.dto.PredictionRequest;
import com.llmancer.dto.PredictionResponse;
import com.llmancer.entity.ApiKey;
import com.llmancer.entity.LLMProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.stereotype.Service;

@Service
public class LLMPredictionService {
    
    private static final Logger logger = LoggerFactory.getLogger(LLMPredictionService.class);
    
    private final ApiKeyService apiKeyService;
    
    public LLMPredictionService(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }
    
    public PredictionResponse predict(PredictionRequest request) {
        long startTime = System.currentTimeMillis();
        
        try {
            logger.info("开始处理预测请求: {}", request);
            
            // 根据模型名称确定提供商
            LLMProvider provider = determineProvider(request.getModel());
            
            // 获取对应提供商的激活API密钥
            ApiKey apiKey = apiKeyService.getActiveApiKeyByProvider(provider)
                    .orElseThrow(() -> new RuntimeException("未找到 " + provider.getDisplayName() + " 的可用API密钥"));
            
            // 创建ChatClient
            ChatClient chatClient = createChatClient(provider, apiKey.getApiKey());
            
            // 构建完整的prompt
            String fullPrompt = buildFullPrompt(request);
            logger.debug("构建的完整prompt: {}", fullPrompt);
            
            // 配置ChatOptions
            OpenAiChatOptions chatOptions = OpenAiChatOptions.builder()
                    .withModel(request.getModel())
                    .withTemperature(request.getTemperature().doubleValue())
                    .withMaxTokens(request.getMaxTokens())
                    .build();
            
            // 创建用户消息和prompt
            UserMessage userMessage = new UserMessage(fullPrompt);
            Prompt prompt = new Prompt(userMessage, chatOptions);
            
            // 调用LLM
            ChatResponse response = chatClient.prompt(prompt).call().chatResponse();
            
            // 处理响应
            String result = response.getResult().getOutput().getContent();
            long processingTime = System.currentTimeMillis() - startTime;
            
            logger.info("预测完成，耗时: {}ms", processingTime);
            
            // 构建响应
            PredictionResponse predictionResponse = PredictionResponse.success(result);
            predictionResponse.setModel(request.getModel());
            predictionResponse.setProcessingTimeMs(processingTime);
            
            // 尝试获取token使用信息
            if (response.getMetadata() != null && response.getMetadata().getUsage() != null) {
                predictionResponse.setTokensUsed(response.getMetadata().getUsage().getTotalTokens().intValue());
            }
            
            return predictionResponse;
            
        } catch (Exception e) {
            long processingTime = System.currentTimeMillis() - startTime;
            logger.error("预测过程中发生错误，耗时: {}ms", processingTime, e);
            
            PredictionResponse errorResponse = PredictionResponse.error("预测失败: " + e.getMessage());
            errorResponse.setProcessingTimeMs(processingTime);
            errorResponse.setModel(request.getModel());
            
            return errorResponse;
        }
    }
    
    private LLMProvider determineProvider(String model) {
        if (model.startsWith("qwen")) {
            return LLMProvider.QWEN;
        } else if (model.startsWith("deepseek")) {
            return LLMProvider.DEEPSEEK;
        } else if (model.startsWith("gpt")) {
            return LLMProvider.OPENAI;
        } else {
            throw new IllegalArgumentException("不支持的模型: " + model);
        }
    }
    
    private ChatClient createChatClient(LLMProvider provider, String apiKey) {
        OpenAiApi openAiApi = new OpenAiApi(provider.getBaseUrl(), apiKey);
        OpenAiChatModel chatModel = new OpenAiChatModel(openAiApi);
        return ChatClient.create(chatModel);
    }
    
    private String buildFullPrompt(PredictionRequest request) {
        StringBuilder promptBuilder = new StringBuilder();
        
        // 添加系统指令
        promptBuilder.append("你是一个专业的数据分析助手。请根据以下要求分析数据并生成预测结果。\n\n");
        
        // 添加用户prompt
        promptBuilder.append("任务描述: ").append(request.getPrompt()).append("\n\n");
        
        // 添加输出格式要求
        promptBuilder.append("输出格式要求: ").append(request.getOutputFormat()).append("\n\n");
        
        // 添加源数据
        promptBuilder.append("源数据:\n").append(request.getSourceData()).append("\n\n");
        
        // 添加额外指令
        promptBuilder.append("请严格按照指定的输出格式返回结果，确保结果准确、清晰且有用。");
        
        return promptBuilder.toString();
    }
}