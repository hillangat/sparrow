package com.techmaster.sparrow.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class SparrowMVCConfig implements WebMvcConfigurer {

    @Autowired
    private ApplicationProperties applicationProperties;

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/hello").setViewName("hello");
        registry.addViewController("/login").setViewName("login");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String urls = applicationProperties.getCrossOriginsUrl();
        if (urls != null) {
            String[] origins = Arrays.stream(urls.split(","))
                    .map(a -> a.trim()).toArray(String[]::new);
            registry.addMapping("/api**").allowedOrigins(origins);
        }
    }

}