package com.llmancer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PredictionRequest {
    
    @NotBlank(message = "Prompt不能为空")
    private String prompt;
    
    @NotBlank(message = "输出格式不能为空")
    private String outputFormat;
    
    @NotNull(message = "源数据不能为空")
    private String sourceData;
    
    private String model = "qwen-plus";
    private Double temperature = 0.7;
    private Integer maxTokens = 2000;
    
    public PredictionRequest() {}
    
    public PredictionRequest(String prompt, String outputFormat, String sourceData) {
        this.prompt = prompt;
        this.outputFormat = outputFormat;
        this.sourceData = sourceData;
    }
    
    // Getters and Setters
    public String getPrompt() {
        return prompt;
    }
    
    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
    
    public String getOutputFormat() {
        return outputFormat;
    }
    
    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }
    
    public String getSourceData() {
        return sourceData;
    }
    
    public void setSourceData(String sourceData) {
        this.sourceData = sourceData;
    }
    
    public String getModel() {
        return model;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    
    public Double getTemperature() {
        return temperature;
    }
    
    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }
    
    public Integer getMaxTokens() {
        return maxTokens;
    }
    
    public void setMaxTokens(Integer maxTokens) {
        this.maxTokens = maxTokens;
    }
    
    @Override
    public String toString() {
        return "PredictionRequest{" +
                "prompt='" + prompt + '\'' +
                ", outputFormat='" + outputFormat + '\'' +
                ", sourceData='" + (sourceData != null ? sourceData.substring(0, Math.min(100, sourceData.length())) + "..." : "null") + '\'' +
                ", model='" + model + '\'' +
                ", temperature=" + temperature +
                ", maxTokens=" + maxTokens +
                '}';
    }
}