package com.uni.GradeCenter.exception;

public class AccountPendingException extends RuntimeException {
    public AccountPendingException(String message) {
        super(message);
    }
}
