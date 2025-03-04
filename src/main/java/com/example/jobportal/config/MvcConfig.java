package com.example.jobportal.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    private static  String UPLOAD_DIR = "photos"; // This is the directory where the images will be stored.
//final
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

            exposeDirectory(UPLOAD_DIR, registry); // This method will expose the directory to the web.
    }

    private void exposeDirectory(String uploadDir, ResourceHandlerRegistry registry) {
        Path path = Paths.get(uploadDir);
        registry.addResourceHandler("/"+uploadDir+ "/**").addResourceLocations("file:/"+path.toAbsolutePath() +"/");
    }
}
