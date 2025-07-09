-- LLMancer 数据库表结构设计 (MySQL)
-- 简化版本：只包含API密钥表和预测历史记录表

-- 创建数据库
CREATE DATABASE IF NOT EXISTS llmancer DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE llmancer;

-- =============================================
-- 1. API密钥管理表
-- =============================================
-- 更新API密钥管理表
DROP TABLE IF EXISTS api_keys;
CREATE TABLE api_keys (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    provider VARCHAR(50) NOT NULL COMMENT 'LLM提供商(QWEN/DEEPSEEK/OPENAI)',
    api_key VARCHAR(500) NOT NULL COMMENT 'API密钥',
    display_name VARCHAR(100) NOT NULL COMMENT '显示名称',
    is_active BOOLEAN NOT NULL DEFAULT TRUE COMMENT '是否激活',
    base_url VARCHAR(200) COMMENT 'API基础URL',
    model_name VARCHAR(100) COMMENT '默认模型名称',
    max_tokens INT DEFAULT 2000 COMMENT '最大token限制',
    temperature DECIMAL(3,2) DEFAULT 0.7 COMMENT '默认温度参数',
    rate_limit_per_minute INT DEFAULT 60 COMMENT '每分钟请求限制',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    -- 索引
    INDEX idx_provider (provider),
    INDEX idx_active (is_active),
    INDEX idx_created_at (created_at),
    
    -- 唯一约束：同一提供商的显示名称不能重复
    UNIQUE KEY uk_provider_display_name (provider, display_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='API密钥管理表';

-- 插入初始数据
INSERT INTO api_keys (provider, api_key, display_name, base_url, model_name) VALUES
('QWEN', 'your-qwen-api-key', '通义千问主密钥', 'https://dashscope.aliyuncs.com/compatible-mode', 'qwen-plus'),
('DEEPSEEK', 'your-deepseek-api-key', 'DeepSeek主密钥', 'https://api.deepseek.com', 'deepseek-chat'),
('OPENAI', 'your-openai-api-key', 'ChatGPT主密钥', 'https://api.openai.com', 'gpt-3.5-turbo');

-- =============================================
-- 2. 预测历史记录表
-- =============================================
DROP TABLE IF EXISTS prediction_history;
CREATE TABLE prediction_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    request_id VARCHAR(100) UNIQUE COMMENT '请求ID',
    prompt TEXT NOT NULL COMMENT '用户输入的提示词',
    output_format VARCHAR(100) NOT NULL COMMENT '输出格式要求',
    source_data LONGTEXT COMMENT '源数据',
    model VARCHAR(100) NOT NULL COMMENT '使用的模型',
    temperature DECIMAL(3,2) DEFAULT 0.7 COMMENT '温度参数',
    max_tokens INT DEFAULT 2000 COMMENT '最大token数',
    result LONGTEXT COMMENT '预测结果',
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态(PENDING/SUCCESS/FAILED)',
    error_message TEXT COMMENT '错误信息',
    tokens_used INT COMMENT '使用的token数量',
    processing_time_ms BIGINT COMMENT '处理时间(毫秒)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='预测历史记录表';

-- =============================================
-- 创建索引
-- =============================================

-- API密钥表索引
CREATE INDEX idx_api_keys_provider ON api_keys(provider);
CREATE INDEX idx_api_keys_active ON api_keys(is_active);
CREATE INDEX idx_api_keys_created_at ON api_keys(created_at);

-- 预测历史记录表索引
CREATE INDEX idx_prediction_history_request_id ON prediction_history(request_id);
CREATE INDEX idx_prediction_history_model ON prediction_history(model);
CREATE INDEX idx_prediction_history_status ON prediction_history(status);
CREATE INDEX idx_prediction_history_created_at ON prediction_history(created_at);

-- =============================================
-- 插入测试数据（可选）
-- =============================================

-- 插入示例API密钥数据（仅用于开发测试）
-- 注意：生产环境中请删除这些测试数据
INSERT INTO api_keys (provider, api_key, display_name, is_active) VALUES
('QWEN', 'sk-test-qwen-key-placeholder', '通义千问测试密钥', false),
('DEEPSEEK', 'sk-test-deepseek-key-placeholder', 'DeepSeek测试密钥', false),
('OPENAI', 'sk-test-openai-key-placeholder', 'OpenAI测试密钥', false);

-- =============================================
-- 数据库视图（用于统计和查询）
-- =============================================

-- 活跃API密钥统计视图
CREATE VIEW v_active_api_keys AS
SELECT 
    provider,
    COUNT(*) as total_keys,
    SUM(CASE WHEN is_active = 1 THEN 1 ELSE 0 END) as active_keys
FROM api_keys 
GROUP BY provider;

-- 预测统计视图
CREATE VIEW v_prediction_stats AS
SELECT 
    model,
    status,
    COUNT(*) as count,
    AVG(processing_time_ms) as avg_processing_time,
    AVG(tokens_used) as avg_tokens_used,
    DATE(created_at) as date
FROM prediction_history 
GROUP BY model, status, DATE(created_at)
ORDER BY date DESC;