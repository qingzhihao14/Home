package com.my.friends.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域前后端配置类
 **/
@EnableWebMvc
@Configuration
public class CorsConfig implements WebMvcConfigurer {


    @Value("${file.basepath}")
    private String baseAddress;
    /**X
     * 发现如果继承了WebMvcConfigurationSupport，则在yml中配置的相关内容会失效。 需要重新指定静态资源
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //注意前面要加file,不然是访问不了的
        registry.addResourceHandler("/**").addResourceLocations(("file:"+baseAddress+"/").replaceAll("\\\\", "/"));
//        registry.addResourceHandler("/**").addResourceLocations(("file:"+baseAddress+"/"));
        registry.addResourceHandler("swagger-ui.html").addResourceLocations(
                "classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations(
                "classpath:/META-INF/resources/webjars/");
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        //每次调用registry.addMappin可以添加一个跨域配置，需要多个配置可以多次调用registry.addMapping
        registry.addMapping("/**")
                .allowedOrigins("*") //放行哪些原始域
//                .allowedMethods("PUT", "DELETE","POST", "GET") //放行哪些请求方式
//                .allowedHeaders("header1", "header2", "header3") //放行哪些原始请求头部信息
//                .exposedHeaders("header1", "header2") //暴露哪些头部信息
                .allowCredentials(false) //是否发送 Cookie
                // 是否允许证书（cookies）
//                .allowCredentials(true)
                // 设置允许的方法
                .allowedMethods("*")
                // 跨域允许时间
                .maxAge(3600);

        // Add more mappings...
    }
}