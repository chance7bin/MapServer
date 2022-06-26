package com.example.mapserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description web配置
 * @Author bin
 * @Date 2021/10/10
 */
@Configuration
public class WebConfig implements WebMvcConfigurer{

    //解决跨域问题
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**"); //允许远端访问的域名
            // .allowedOrigins("http://localhost:8099")
            //允许请求的方法("POST", "GET", "PUT", "OPTIONS", "DELETE")
            // .allowedMethods("*")
            //允许请求头
            // .allowedHeaders("*");
    }

}
