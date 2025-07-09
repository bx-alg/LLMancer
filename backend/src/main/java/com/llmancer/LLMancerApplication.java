package com.llmancer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.llmancer.mapper")
public class LLMancerApplication {
    public static void main(String[] args) {
        SpringApplication.run(LLMancerApplication.class, args);
    }
}