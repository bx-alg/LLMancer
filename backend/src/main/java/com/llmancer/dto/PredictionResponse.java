package com.llmancer.dto;

import java.time.LocalDateTime;

public class PredictionResponse {
    
    private boolean success;
    private String result;
    private String error;
    private LocalDateTime timestamp;
    private String model;
    private Integer tokensUsed;
    private Long processingTimeMs;
    
    public PredictionResponse() {
        this.timestamp = LocalDateTime.now();
    }
    
    public PredictionResponse(boolean success, String result) {
        this();
        this.success = success;
        this.result = result;
    }
    
    public PredictionResponse(boolean success, String result, String error) {
        this();
        this.success = success;
        this.result = result;
        this.error = error;
    }
    
    // Static factory methods
    public static PredictionResponse success(String result) {
        return new PredictionResponse(true, result);
    }
    
    public static PredictionResponse error(String error) {
        return new PredictionResponse(false, null, error);
    }
    
    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getResult() {
        return result;
    }
    
    public void setResult(String result) {
        this.result = result;
    }
    
    public String getError() {
        return error;
    }
    
    public void setError(String error) {
        this.error = error;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getModel() {
        return model;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    
    public Integer getTokensUsed() {
        return tokensUsed;
    }
    
    public void setTokensUsed(Integer tokensUsed) {
        this.tokensUsed = tokensUsed;
    }
    
    public Long getProcessingTimeMs() {
        return processingTimeMs;
    }
    
    public void setProcessingTimeMs(Long processingTimeMs) {
        this.processingTimeMs = processingTimeMs;
    }
    
    @Override
    public String toString() {
        return "PredictionResponse{" +
                "success=" + success +
                ", result='" + (result != null ? result.substring(0, Math.min(100, result.length())) + "..." : "null") + '\'' +
                ", error='" + error + '\'' +
                ", timestamp=" + timestamp +
                ", model='" + model + '\'' +
                ", tokensUsed=" + tokensUsed +
                ", processingTimeMs=" + processingTimeMs +
                '}';
    }
}