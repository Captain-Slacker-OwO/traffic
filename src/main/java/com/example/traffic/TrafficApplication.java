package com.example.traffic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.traffic.model")
public class TrafficApplication {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")  // 修改为全局路径
                    .allowedOrigins("*")    // 开发环境可放宽限制
                    .allowedMethods("*");
            }
        };
    }
    public static void main(String[] args) {
        SpringApplication.run(TrafficApplication.class, args);
    }
}