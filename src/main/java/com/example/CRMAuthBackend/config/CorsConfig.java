package com.example.CRMAuthBackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000", "http://87.236.22.199:3000", "https://parking-yanokk.vercel.app/",
                        "https://parking-ruddy.vercel.app", "https://smart-parking-front.vercel.app", "http://31.129.109.89:8080",
                        "http://185.14.40.86")
                .allowedMethods("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}

