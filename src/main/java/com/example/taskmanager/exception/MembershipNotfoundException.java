package com.example.taskmanager.exception;

public class MembershipNotfoundException extends RuntimeException {

    public MembershipNotfoundException(String message){
        super(message);
    }
}
