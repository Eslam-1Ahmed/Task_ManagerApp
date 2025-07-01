package com.example.taskmanager.appconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.taskmanager.utils.impl.AppUtilsV1;
import com.example.taskmanager.utils.impl.AppUtilsV2;

@Configuration
public class AppConfig {
    
    @Bean(name = "appUtilsV1")
    public AppUtilsV1 getAppUtilsV1(){
        return new AppUtilsV1();
    }    
    @Bean(name = "appUtilsV2")
    public AppUtilsV2 getAppUtilsV2(){
        return new AppUtilsV2();
    }
}
