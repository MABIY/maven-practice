package com.juvenxu.mvnbook.account.service;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by lh on 17-3-18.
 */
@Getter
@Setter
public class SignUpRequest {

    private String id;

    private String email;

    private String name;

    private String password;

    private String confirmPassword;

    private String captchaKey;

    private String captchaValue;

    private String activateServiceUrl;
}
