package com.example.taskmanager.execptions;

public class MembershipNotfoundException extends RuntimeException {

    public MembershipNotfoundException(String message){
        super(message);
    }
}
