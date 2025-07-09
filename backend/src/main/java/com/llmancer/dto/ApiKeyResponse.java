package com.llmancer.dto;

import com.llmancer.entity.LLMProvider;
import java.time.LocalDateTime;

public class ApiKeyResponse {
    
    private Long id;
    private LLMProvider provider;
    private String maskedApiKey;
    private String displayName;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // 构造函数
    public ApiKeyResponse() {}
    
    public ApiKeyResponse(Long id, LLMProvider provider, String maskedApiKey, 
                         String displayName, Boolean isActive, 
                         LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.provider = provider;
        this.maskedApiKey = maskedApiKey;
        this.displayName = displayName;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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
    
    public String getMaskedApiKey() {
        return maskedApiKey;
    }
    
    public void setMaskedApiKey(String maskedApiKey) {
        this.maskedApiKey = maskedApiKey;
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
}