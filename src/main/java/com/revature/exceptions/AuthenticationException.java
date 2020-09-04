package com.revature.exceptions;

public class AuthenticationException extends RuntimeException{

    public AuthenticationException(String message) {
        super(message);
    }
    public AuthenticationException() {
        super("User authentication failed!");
    }


}
