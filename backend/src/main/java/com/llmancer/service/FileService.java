package com.llmancer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Service
public class FileService {
    
    private static final Logger logger = LoggerFactory.getLogger(FileService.class);
    
    // 支持的文件类型
    private static final List<String> SUPPORTED_CONTENT_TYPES = Arrays.asList(
            "text/plain",
            "text/csv",
            "application/json",
            "application/xml",
            "text/xml"
    );
    
    // 支持的文件扩展名
    private static final List<String> SUPPORTED_EXTENSIONS = Arrays.asList(
            ".txt", ".csv", ".json", ".xml", ".log", ".md"
    );
    
    public String processUploadedFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }
        
        logger.info("开始处理上传文件: {}, 大小: {} bytes, 类型: {}", 
                   file.getOriginalFilename(), file.getSize(), file.getContentType());
        
        // 验证文件类型
        validateFileType(file);
        
        // 验证文件大小 (最大10MB)
        validateFileSize(file);
        
        try {
            // 读取文件内容
            String content = new String(file.getBytes(), StandardCharsets.UTF_8);
            
            logger.info("文件处理成功，内容长度: {} 字符", content.length());
            
            return content;
            
        } catch (IOException e) {
            logger.error("读取文件内容时发生错误: {}", file.getOriginalFilename(), e);
            throw new IOException("无法读取文件内容: " + e.getMessage(), e);
        }
    }
    
    private void validateFileType(MultipartFile file) {
        String contentType = file.getContentType();
        String filename = file.getOriginalFilename();
        
        // 检查Content-Type
        boolean validContentType = contentType != null && 
                (SUPPORTED_CONTENT_TYPES.contains(contentType) || contentType.startsWith("text/"));
        
        // 检查文件扩展名
        boolean validExtension = false;
        if (filename != null) {
            String lowerFilename = filename.toLowerCase();
            validExtension = SUPPORTED_EXTENSIONS.stream()
                    .anyMatch(lowerFilename::endsWith);
        }
        
        if (!validContentType && !validExtension) {
            throw new IllegalArgumentException(
                    String.format("不支持的文件类型。文件: %s, Content-Type: %s。支持的类型: %s", 
                            filename, contentType, String.join(", ", SUPPORTED_EXTENSIONS)));
        }
        
        logger.debug("文件类型验证通过: {}, Content-Type: {}", filename, contentType);
    }
    
    private void validateFileSize(MultipartFile file) {
        long maxSize = 10 * 1024 * 1024; // 10MB
        
        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException(
                    String.format("文件大小超过限制。当前大小: %.2f MB, 最大允许: %.2f MB", 
                            file.getSize() / (1024.0 * 1024.0), 
                            maxSize / (1024.0 * 1024.0)));
        }
        
        logger.debug("文件大小验证通过: {} bytes", file.getSize());
    }
    
    public boolean isValidFileType(String filename) {
        if (filename == null) {
            return false;
        }
        
        String lowerFilename = filename.toLowerCase();
        return SUPPORTED_EXTENSIONS.stream().anyMatch(lowerFilename::endsWith);
    }
    
    public List<String> getSupportedExtensions() {
        return SUPPORTED_EXTENSIONS;
    }
}