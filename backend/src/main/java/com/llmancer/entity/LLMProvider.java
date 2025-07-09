package com.llmancer.entity;

public enum LLMProvider {
    QWEN("通义千问", "https://dashscope.aliyuncs.com/compatible-mode"),
    DEEPSEEK("DeepSeek", "https://api.deepseek.com"),
    OPENAI("OpenAI", "https://api.openai.com");
    
    private final String displayName;
    private final String baseUrl;
    
    LLMProvider(String displayName, String baseUrl) {
        this.displayName = displayName;
        this.baseUrl = baseUrl;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getBaseUrl() {
        return baseUrl;
    }
    
    public String getDefaultModel() {
        switch (this) {
            case QWEN:
                return "qwen-plus";
            case DEEPSEEK:
                return "deepseek-chat";
            case OPENAI:
                return "gpt-3.5-turbo";
            default:
                return "";
        }
    }
}