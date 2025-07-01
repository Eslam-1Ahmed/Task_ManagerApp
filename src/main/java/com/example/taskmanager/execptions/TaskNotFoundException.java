package com.example.taskmanager.execptions;
public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(String message){
        super(message);
    }
}
