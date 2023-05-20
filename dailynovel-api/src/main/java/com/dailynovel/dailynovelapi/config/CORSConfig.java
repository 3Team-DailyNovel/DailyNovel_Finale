package com.dailynovel.dailynovelapi.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CORSConfig {
    

    @Bean
    WebMvcConfigurer webMvcConfigurer(){

            WebMvcConfigurer Configurer = new WebMvcConfigurer(){
                
                @Override
                public void addCorsMappings(CorsRegistry registry){

                    registry.addMapping("/**").allowedOrigins("*")
                    .allowedMethods(
    	            HttpMethod.GET.name(),
    	            HttpMethod.HEAD.name(),
    	            HttpMethod.POST.name(),
    	            HttpMethod.PUT.name(),
    	            HttpMethod.DELETE.name());
                }
            };

            return Configurer;
    }
}
