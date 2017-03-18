package com.juvenxu.mvnbook.account.captcha;

import java.util.List;

/**
 * Created by lh on 17-3-13.
 */
public interface AccountCaptchaService {
    String generateCaptchaKey();

    byte[] generateCaptchaImage(String captchaKey) throws AccountCaptchaException;

    boolean validataCaptcha(String captchaKey, String captchaValue) throws AccountCaptchaException;

    List<String> getPreDefinedTexts();

    void setPreDefinedTexts(List<String> preDefinedTexts);
}
