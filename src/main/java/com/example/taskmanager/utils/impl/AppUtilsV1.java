package com.example.taskmanager.utils.impl;

import org.springframework.context.annotation.Scope;

import com.example.taskmanager.utils.AppUtils;
@Scope("prototype")
public class AppUtilsV1 implements AppUtils {
    
    private int x;

    @Override
    public String Do() {
        return "Hello this version " + x++;
    }

}
