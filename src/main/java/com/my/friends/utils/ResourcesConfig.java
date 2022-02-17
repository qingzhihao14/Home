package com.my.friends.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class ResourcesConfig implements WebMvcConfigurer {

    @Value("${file.basepath}")
    private String baseAddress;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        //注意前面要加file,不然是访问不了的
        registry.addResourceHandler("/**").addResourceLocations("file:"+baseAddress+"/");


    }
}
