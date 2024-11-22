package com.project.library.config;

import com.project.library.util.PermissionInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class PermissionInterceptorConfig implements WebMvcConfigurer {
    @Bean
    PermissionInterceptor getPermissionInterceptor() {
        return new PermissionInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        String[] whiteList = {"/upload/**","/api/v1/auth/login", "/api/v1/auth/register", "/api/v1/auth/refresh",
//                "/api/v1/companies", "/api/v1/jobs", "/api/v1/skills", };
//        registry.addInterceptor(getPermissionInterceptor())
//                .excludePathPatterns(whiteList);
        registry.addInterceptor(getPermissionInterceptor());
    }
}
