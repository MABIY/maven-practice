package com.juvenxu.mvnbook.account.email;

/**
 * Created by lh on 17-2-28.
 */
public interface AccountEmailService {
    void sendMail(String to, String subject, String htmlTest) throws AccountEmailException;
}
