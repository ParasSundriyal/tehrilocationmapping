package com.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${spring.web.cors.allowed-origins}")
    private String[] allowedOrigins;

    @Value("${spring.web.cors.allowed-methods}")
    private String allowedMethods;

    @Value("${spring.web.cors.allowed-headers}")
    private String allowedHeaders;

    @Value("${spring.web.cors.exposed-headers:Authorization,Content-Disposition}")
    private String exposedHeaders;

    @Value("${spring.web.cors.allow-credentials:true}")
    private boolean allowCredentials;

    @Value("${spring.web.cors.max-age:3600}")
    private long maxAge;

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // Allowed origins
        for (String origin : allowedOrigins) {
            config.addAllowedOrigin(origin);
        }
        
        // Allowed methods (convert comma-separated string to list)
        for (String method : allowedMethods.split(",")) {
            config.addAllowedMethod(method.trim());
        }
        
        // Allowed headers (convert comma-separated string to list)
        for (String header : allowedHeaders.split(",")) {
            config.addAllowedHeader(header.trim());
        }
        
        // Exposed headers (convert comma-separated string to list)
        for (String header : exposedHeaders.split(",")) {
            config.addExposedHeader(header.trim());
        }
        
        config.setAllowCredentials(allowCredentials);
        config.setMaxAge(maxAge);
        
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
} 