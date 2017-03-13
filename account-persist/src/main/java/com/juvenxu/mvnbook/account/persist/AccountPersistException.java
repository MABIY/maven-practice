package com.juvenxu.mvnbook.account.persist;

/**
 * Created by lh on 17-3-13.
 */
public class AccountPersistException extends Exception {
    public static final long serialVersionUID = -1;

    public AccountPersistException(String message) {
        super(message);
    }

    public AccountPersistException(String message, Throwable cause) {
        super(message, cause);
    }
}
