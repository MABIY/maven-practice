package com.juvenxu.mvnbook.account.captcha;

/**
 * Created by lh on 17-3-13.
 */
public class AccountCaptchaException extends Exception{
    public static final long serialVersionUID = 13394;

    public AccountCaptchaException(String message) {
        super(message);
    }

    public AccountCaptchaException(String message, Throwable cause) {
        super(message, cause);
    }
}
