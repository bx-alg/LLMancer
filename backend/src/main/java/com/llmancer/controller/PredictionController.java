package com.llmancer.controller;

import com.llmancer.dto.PredictionRequest;
import com.llmancer.dto.PredictionResponse;
import com.llmancer.service.FileService;
import com.llmancer.service.LLMPredictionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PredictionController {
    
    private static final Logger logger = LoggerFactory.getLogger(PredictionController.class);
    
    private final LLMPredictionService predictionService;
    private final FileService fileService;
    
    public PredictionController(LLMPredictionService predictionService, FileService fileService) {
        this.predictionService = predictionService;
        this.fileService = fileService;
    }
    
    @PostMapping("/predict")
    public ResponseEntity<PredictionResponse> predict(@Valid @RequestBody PredictionRequest request) {
        try {
            logger.info("收到预测请求");
            PredictionResponse response = predictionService.predict(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("处理预测请求时发生错误", e);
            PredictionResponse errorResponse = PredictionResponse.error("服务器内部错误: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @PostMapping("/predict-with-file")
    public ResponseEntity<PredictionResponse> predictWithFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("prompt") String prompt,
            @RequestParam("outputFormat") String outputFormat,
            @RequestParam(value = "model", defaultValue = "qwen-plus") String model,
            @RequestParam(value = "temperature", defaultValue = "0.7") Double temperature,
            @RequestParam(value = "maxTokens", defaultValue = "2000") Integer maxTokens) {
        
        try {
            logger.info("收到带文件的预测请求: {}", file.getOriginalFilename());
            
            // 处理上传的文件
            String sourceData = fileService.processUploadedFile(file);
            
            // 创建预测请求
            PredictionRequest request = new PredictionRequest(prompt, outputFormat, sourceData);
            request.setModel(model);
            request.setTemperature(temperature);
            request.setMaxTokens(maxTokens);
            
            // 执行预测
            PredictionResponse response = predictionService.predict(request);
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            logger.warn("文件验证失败: {}", e.getMessage());
            PredictionResponse errorResponse = PredictionResponse.error(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            logger.error("处理带文件的预测请求时发生错误", e);
            PredictionResponse errorResponse = PredictionResponse.error("服务器内部错误: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "LLMancer Backend");
        health.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(health);
    }
    
    @GetMapping("/file-info")
    public ResponseEntity<Map<String, Object>> getFileInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("supportedExtensions", fileService.getSupportedExtensions());
        info.put("maxFileSize", "10MB");
        info.put("supportedFormats", "文本文件 (.txt), CSV (.csv), JSON (.json), XML (.xml), 日志文件 (.log), Markdown (.md)");
        return ResponseEntity.ok(info);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<PredictionResponse> handleException(Exception e) {
        logger.error("未处理的异常", e);
        PredictionResponse errorResponse = PredictionResponse.error("服务器错误: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}