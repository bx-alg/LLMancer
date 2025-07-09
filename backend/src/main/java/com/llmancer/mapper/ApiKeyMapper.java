package com.llmancer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.llmancer.entity.ApiKey;
import com.llmancer.entity.LLMProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ApiKeyMapper extends BaseMapper<ApiKey> {
    
    /**
     * 根据提供商查询激活的API密钥
     */
    @Select("SELECT * FROM api_keys WHERE provider = #{provider} AND is_active = 1 ORDER BY created_at DESC")
    List<ApiKey> selectActiveByProvider(LLMProvider provider);
    
    /**
     * 查询所有激活的API密钥
     */
    @Select("SELECT * FROM api_keys WHERE is_active = 1 ORDER BY provider, created_at DESC")
    List<ApiKey> selectAllActive();
    
    /**
     * 根据提供商统计API密钥数量
     */
    @Select("SELECT COUNT(*) FROM api_keys WHERE provider = #{provider}")
    int countByProvider(LLMProvider provider);
}