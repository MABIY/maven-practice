package com.juvenxu.mvnbook.account.captcha;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by lh on 17-3-15.
 */
public class AccountCaptchaServiceTest {

    private AccountCaptchService service;

    @Before
    public void perpare() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("account-captcha.xml");
        service = (AccountCaptchService) ctx.getBean("accountCaptchaService");
    }

    @Test
    public void testGenerateCaptcha() throws AccountCaptchaException, IOException {
        String captchKey = service.generateCaptchaKey();
        assertNotNull(captchKey);

        byte[] captchaImage = service.generateCaptchaImage(captchKey);

        File image = new File("target/" + captchKey + ".jpg");

        OutputStream output = null;
        try {
            output = new FileOutputStream(image);
            output.write(captchaImage);

        } catch (FileNotFoundException e) {
            System.out.println(e);
        } finally {
            if (output != null) {
                output.close();
            }
        }
        assertTrue(image.exists() && image.length() > 0);
    }

    @Test
    public void testValidateCaptchaCorrect() throws AccountCaptchaException {
        List<String> preDefinedTexts = new ArrayList<>();
        preDefinedTexts.add("12345");
        preDefinedTexts.add("abcde");
        service.setPreDefinedTexts(preDefinedTexts);

        String captchaKey = service.generateCaptchaKey();
        service.generateCaptchaImage(captchaKey);

        assertTrue(service.validataCaptcha(captchaKey,"12345"));

        captchaKey = service.generateCaptchaKey();
        service.generateCaptchaImage(captchaKey);
        assertTrue(service.validataCaptcha(captchaKey, "abcde"));
    }

    @Test
    public void testValidateCaptchaIncorrect() throws AccountCaptchaException {
        List<String> preDefinedTexts = new ArrayList<>();
        preDefinedTexts.add("12345");
        service.setPreDefinedTexts(preDefinedTexts);

        String captchaKey = service.generateCaptchaKey();
        service.generateCaptchaImage(captchaKey);
        assertFalse(service.validataCaptcha(captchaKey, "6780"));
    }



}
