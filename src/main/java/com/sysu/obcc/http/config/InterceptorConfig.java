package com.sysu.obcc.http.config;

import com.sysu.obcc.http.interceptor.CustomInterceptor;
import com.sysu.obcc.http.interceptor.TokenInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @Author: obc
 * @Date: 2019/2/25 2:13
 * @Version 1.0
 */

/**
 * 拦截器注册
 */
@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {

    @Bean
    public HandlerInterceptor getTokenInterceptor() {
        return new TokenInterceptor();
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CustomInterceptor());
        registry.addInterceptor(getTokenInterceptor())
                .addPathPatterns("/api/*")
                .excludePathPatterns("/api/signIn", "/api/signUp", "/api/resetPassword");

        super.addInterceptors(registry);
    }
}
