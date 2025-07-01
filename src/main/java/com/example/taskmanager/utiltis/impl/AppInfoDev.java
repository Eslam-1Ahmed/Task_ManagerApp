package com.example.taskmanager.utiltis.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
@Profile("dev")
public class AppInfoDev {

    @Value("${spring.application.version}")
    private String version;

    @PostConstruct
    public void printVersion() {
        System.out.println("This Devoloper Version");
        System.out.println("App Version: " + version);
    }
}
