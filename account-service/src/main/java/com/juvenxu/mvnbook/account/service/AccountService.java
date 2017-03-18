package com.juvenxu.mvnbook.account.service;

import com.juvenxu.mvnbook.account.persist.AccountPersistException;

/**
 * Created by lh on 17-3-18.
 */
public interface AccountService {
    String generateCaptchaKey();

    byte[] generateCaptchaImage(String captchaKey) throws AccountServiceException;

    void signUp(SignUpRequest signUpRequest) throws AccountServiceException;

    void activate(String activationNumber) throws AccountServiceException;

    void login(String id, String password) throws  AccountServiceException;
}
