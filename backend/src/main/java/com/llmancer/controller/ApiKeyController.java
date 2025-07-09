package com.llmancer.controller;

import com.llmancer.dto.ApiKeyRequest;
import com.llmancer.dto.ApiKeyResponse;
import com.llmancer.entity.LLMProvider;
import com.llmancer.service.ApiKeyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/keys")
@CrossOrigin(origins = "*")
public class ApiKeyController {
    
    @Autowired
    private ApiKeyService apiKeyService;
    
    /**
     * 获取所有API密钥
     */
    @GetMapping
    public ResponseEntity<List<ApiKeyResponse>> getAllApiKeys() {
        List<ApiKeyResponse> apiKeys = apiKeyService.getAllApiKeys();
        return ResponseEntity.ok(apiKeys);
    }
    
    /**
     * 获取激活的API密钥
     */
    @GetMapping("/active")
    public ResponseEntity<List<ApiKeyResponse>> getActiveApiKeys() {
        List<ApiKeyResponse> apiKeys = apiKeyService.getActiveApiKeys();
        return ResponseEntity.ok(apiKeys);
    }
    
    /**
     * 根据提供商获取API密钥
     */
    @GetMapping("/provider/{provider}")
    public ResponseEntity<List<ApiKeyResponse>> getApiKeysByProvider(@PathVariable LLMProvider provider) {
        List<ApiKeyResponse> apiKeys = apiKeyService.getApiKeysByProvider(provider);
        return ResponseEntity.ok(apiKeys);
    }
    
    /**
     * 根据ID获取API密钥
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiKeyResponse> getApiKeyById(@PathVariable Long id) {
        return apiKeyService.getApiKeyById(id)
                .map(apiKey -> ResponseEntity.ok(apiKey))
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * 创建新的API密钥
     */
    @PostMapping
    public ResponseEntity<ApiKeyResponse> createApiKey(@Valid @RequestBody ApiKeyRequest request) {
        try {
            ApiKeyResponse response = apiKeyService.createApiKey(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    /**
     * 更新API密钥
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiKeyResponse> updateApiKey(@PathVariable Long id, 
                                                      @Valid @RequestBody ApiKeyRequest request) {
        return apiKeyService.updateApiKey(id, request)
                .map(apiKey -> ResponseEntity.ok(apiKey))
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * 切换API密钥的激活状态
     */
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<ApiKeyResponse> toggleApiKeyStatus(@PathVariable Long id) {
        return apiKeyService.toggleApiKeyStatus(id)
                .map(apiKey -> ResponseEntity.ok(apiKey))
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * 删除API密钥
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApiKey(@PathVariable Long id) {
        if (apiKeyService.deleteApiKey(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * 获取支持的LLM提供商列表
     */
    @GetMapping("/providers")
    public ResponseEntity<LLMProvider[]> getProviders() {
        return ResponseEntity.ok(LLMProvider.values());
    }
    
    /**
     * 检查指定提供商是否有可用的API密钥
     */
    @GetMapping("/provider/{provider}/available")
    public ResponseEntity<Map<String, Boolean>> checkProviderAvailability(@PathVariable LLMProvider provider) {
        boolean available = apiKeyService.hasActiveApiKeyForProvider(provider);
        return ResponseEntity.ok(Map.of("available", available));
    }
}