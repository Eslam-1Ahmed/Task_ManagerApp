package com.example.taskmanager.utiltis.impl;

import org.springframework.context.annotation.Scope;

import com.example.taskmanager.utiltis.AppUtils;
@Scope("prototype")
public class AppUtilsV1 implements AppUtils {
    
    private int x;

    @Override
    public String Do() {
        return "Hello this version " + x++;
    }

}
