package com.llmancer.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.llmancer.dto.ApiKeyRequest;
import com.llmancer.dto.ApiKeyResponse;
import com.llmancer.entity.ApiKey;
import com.llmancer.entity.LLMProvider;
import com.llmancer.mapper.ApiKeyMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ApiKeyService extends ServiceImpl<ApiKeyMapper, ApiKey> {
    
    /**
     * 获取所有API密钥
     */
    public List<ApiKeyResponse> getAllApiKeys() {
        return list().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * 根据提供商获取API密钥
     */
    public List<ApiKeyResponse> getApiKeysByProvider(LLMProvider provider) {
        QueryWrapper<ApiKey> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("provider", provider);
        return list(queryWrapper).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取激活的API密钥
     */
    public List<ApiKeyResponse> getActiveApiKeys() {
        return baseMapper.selectAllActive().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * 根据ID获取API密钥
     */
    public Optional<ApiKeyResponse> getApiKeyById(Long id) {
        return Optional.ofNullable(getById(id))
                .map(this::convertToResponse);
    }
    
    /**
     * 创建新的API密钥
     */
    public ApiKeyResponse createApiKey(ApiKeyRequest request) {
        ApiKey apiKey = new ApiKey();
        apiKey.setProvider(request.getProvider());
        apiKey.setApiKey(request.getApiKey());
        apiKey.setDisplayName(request.getDisplayName() != null ? 
            request.getDisplayName() : request.getProvider().getDisplayName() + " API Key");
        apiKey.setBaseUrl(request.getProvider().getBaseUrl());
        apiKey.setModelName(request.getProvider().getDefaultModel());
        apiKey.setIsActive(true);
        apiKey.setCreatedAt(LocalDateTime.now());
        apiKey.setUpdatedAt(LocalDateTime.now());
        
        save(apiKey);
        return convertToResponse(apiKey);
    }
    
    /**
     * 更新API密钥
     */
    public Optional<ApiKeyResponse> updateApiKey(Long id, ApiKeyRequest request) {
        ApiKey apiKey = getById(id);
        if (apiKey != null) {
            apiKey.setProvider(request.getProvider());
            apiKey.setApiKey(request.getApiKey());
            if (request.getDisplayName() != null && !request.getDisplayName().trim().isEmpty()) {
                apiKey.setDisplayName(request.getDisplayName());
            }
            apiKey.setBaseUrl(request.getProvider().getBaseUrl());
            apiKey.setModelName(request.getProvider().getDefaultModel());
            apiKey.setUpdatedAt(LocalDateTime.now());
            updateById(apiKey);
            return Optional.of(convertToResponse(apiKey));
        }
        return Optional.empty();
    }
    
    /**
     * 切换API密钥的激活状态
     */
    public Optional<ApiKeyResponse> toggleApiKeyStatus(Long id) {
        ApiKey apiKey = getById(id);
        if (apiKey != null) {
            apiKey.setIsActive(!apiKey.getIsActive());
            apiKey.setUpdatedAt(LocalDateTime.now());
            updateById(apiKey);
            return Optional.of(convertToResponse(apiKey));
        }
        return Optional.empty();
    }
    
    /**
     * 删除API密钥
     */
    public boolean deleteApiKey(Long id) {
        return removeById(id);
    }
    
    /**
     * 获取指定提供商的激活API密钥
     */
    public Optional<String> getActiveApiKeyForProvider(LLMProvider provider) {
        List<ApiKey> activeKeys = baseMapper.selectActiveByProvider(provider);
        return activeKeys.isEmpty() ? Optional.empty() : 
            Optional.of(activeKeys.get(0).getApiKey());
    }
    
    /**
     * 检查指定提供商是否有可用的API密钥
     */
    public boolean hasActiveApiKeyForProvider(LLMProvider provider) {
        List<ApiKey> activeKeys = baseMapper.selectActiveByProvider(provider);
        return !activeKeys.isEmpty();
    }
    
    /**
     * 根据提供商获取激活的API密钥实体
     */
    public Optional<ApiKey> getActiveApiKeyByProvider(LLMProvider provider) {
        List<ApiKey> activeKeys = baseMapper.selectActiveByProvider(provider);
        return activeKeys.isEmpty() ? Optional.empty() : Optional.of(activeKeys.get(0));
    }
    
    /**
     * 转换实体为响应DTO
     */
    private ApiKeyResponse convertToResponse(ApiKey apiKey) {
        return new ApiKeyResponse(
                apiKey.getId(),
                apiKey.getProvider(),
                apiKey.getMaskedApiKey(),
                apiKey.getDisplayName(),
                apiKey.getIsActive(),
                apiKey.getCreatedAt(),
                apiKey.getUpdatedAt()
        );
    }
}