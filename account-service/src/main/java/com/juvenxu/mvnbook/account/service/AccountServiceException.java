package com.juvenxu.mvnbook.account.service;

/**
 * Created by lh on 17-3-18.
 */
public class AccountServiceException extends Exception{
    public static final long  serialVersionUID =1;

    public AccountServiceException(String message) {
        super(message);
    }

    public AccountServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
