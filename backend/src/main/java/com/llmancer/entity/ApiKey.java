package com.llmancer.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

@TableName("api_keys")
public class ApiKey {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    @TableField("provider")
    private LLMProvider provider;
    
    @TableField("api_key")
    private String apiKey;
    
    @TableField("display_name")
    private String displayName;
    
    @TableField("is_active")
    private Boolean isActive = true;
    
    @TableField("base_url")
    private String baseUrl;
    
    @TableField("model_name")
    private String modelName;
    
    @TableField("max_tokens")
    private Integer maxTokens;
    
    @TableField("temperature")
    private Double temperature;
    
    @TableField("rate_limit_per_minute")
    private Integer rateLimitPerMinute;
    
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    
    // 构造函数
    public ApiKey() {}
    
    public ApiKey(LLMProvider provider, String apiKey, String displayName) {
        this.provider = provider;
        this.apiKey = apiKey;
        this.displayName = displayName;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // 获取脱敏的API密钥用于显示
    public String getMaskedApiKey() {
        if (apiKey == null || apiKey.length() < 8) {
            return "****";
        }
        return apiKey.substring(0, 4) + "****" + apiKey.substring(apiKey.length() - 4);
    }
    
    // 新增的getter和setter方法
    public String getBaseUrl() {
        return baseUrl;
    }
    
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
    
    public String getModelName() {
        return modelName;
    }
    
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
    
    public Integer getMaxTokens() {
        return maxTokens;
    }
    
    public void setMaxTokens(Integer maxTokens) {
        this.maxTokens = maxTokens;
    }
    
    public Double getTemperature() {
        return temperature;
    }
    
    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }
    
    public Integer getRateLimitPerMinute() {
        return rateLimitPerMinute;
    }
    
    public void setRateLimitPerMinute(Integer rateLimitPerMinute) {
        this.rateLimitPerMinute = rateLimitPerMinute;
    }
}