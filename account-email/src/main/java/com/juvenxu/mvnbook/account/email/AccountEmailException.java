package com.juvenxu.mvnbook.account.email;

/**
 * Created by lh on 17-2-28.
 */
public class AccountEmailException extends Exception {
    public static final long serialVersionUID = -4817386460334501672L;

    public AccountEmailException(String message) {
        super(message);
    }

    public AccountEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
