package com.llmancer.dto;

import com.llmancer.entity.LLMProvider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ApiKeyRequest {
    
    @NotNull(message = "LLM提供商不能为空")
    private LLMProvider provider;
    
    @NotBlank(message = "API密钥不能为空")
    private String apiKey;
    
    private String displayName;
    
    // 构造函数
    public ApiKeyRequest() {}
    
    public ApiKeyRequest(LLMProvider provider, String apiKey, String displayName) {
        this.provider = provider;
        this.apiKey = apiKey;
        this.displayName = displayName;
    }
    
    // Getters and Setters
    public LLMProvider getProvider() {
        return provider;
    }
    
    public void setProvider(LLMProvider provider) {
        this.provider = provider;
    }
    
    public String getApiKey() {
        return apiKey;
    }
    
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}