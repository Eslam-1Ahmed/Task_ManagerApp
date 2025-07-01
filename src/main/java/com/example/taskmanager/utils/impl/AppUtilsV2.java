package com.example.taskmanager.utils.impl;

import org.springframework.beans.factory.annotation.Value;

import com.example.taskmanager.utils.AppUtils;

public class AppUtilsV2 implements AppUtils {

    @Value("${spring.application.name}")
    private String name;

    @Override
    public String Do() {
        return "Hello this my Application " + name;
    }
}
