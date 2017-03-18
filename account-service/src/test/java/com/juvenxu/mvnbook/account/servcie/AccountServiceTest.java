package com.juvenxu.mvnbook.account.servcie;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;
import com.juvenxu.mvnbook.account.captcha.AccountCaptchaService;
import com.juvenxu.mvnbook.account.persist.AccountPersistException;
import com.juvenxu.mvnbook.account.service.AccountService;
import com.juvenxu.mvnbook.account.service.AccountServiceException;
import com.juvenxu.mvnbook.account.service.SignUpRequest;
import lombok.Getter;
import lombok.Setter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.mail.Message;
import javax.mail.MessagingException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by lh on 17-3-18.
 */
@Getter
@Setter
public class AccountServiceTest {
    private GreenMail greenMail;

    private AccountService accountService;

    @Before
    public void prepare() {
        String[] springConfigFiles = {
                "account-email.xml",
                "account-persist.xml",
                "account-captcha.xml",
                "account-service.xml"
        };
        ApplicationContext ctx = new ClassPathXmlApplicationContext(springConfigFiles);

        AccountCaptchaService accountCaptchaService = (AccountCaptchaService) ctx.getBean("accountCaptchaService");

        List<String> preDefinedTexts = new ArrayList<>();

        preDefinedTexts.add("12345");
        preDefinedTexts.add("abcde");
        accountCaptchaService.setPreDefinedTexts(preDefinedTexts);

        accountService = (AccountService) ctx.getBean("accountService");

        greenMail = new GreenMail(new ServerSetup(8001, (String) null, "smtp"));
        greenMail.setUser("test@juvenxu.com", "123456");
        greenMail.start();

        File persistDataFile = new File("target/test-classes/persist-data.xml");
        if (persistDataFile.exists()) {
            persistDataFile.delete();
        }
    }

    @Test
    public void testAccountService() throws AccountServiceException, InterruptedException, MessagingException, AccountPersistException {
        //1. Get captcha
        String captchaKey = accountService.generateCaptchaKey();
        accountService.generateCaptchaImage(captchaKey);
        String captchaValue = "12345";

        //2. Submit sign up Requet
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setCaptchaKey(captchaKey);
        signUpRequest.setCaptchaValue(captchaValue);
        signUpRequest.setId("juven");
        signUpRequest.setEmail("test@juvenxu.com");
        signUpRequest.setName("juven Xu");
        signUpRequest.setPassword("admin123");
        signUpRequest.setConfirmPassword("admin123");
        signUpRequest.setActivateServiceUrl("http://localhost:8080/account/activate");
        accountService.signUp(signUpRequest);

        //3. Read activation link
        greenMail.waitForIncomingEmail(2000, 1);
        Message[] msgs = greenMail.getReceivedMessages();
        assertEquals(1, msgs.length);
        assertEquals("Please Activate Your Account", msgs[0].getSubject());
        String activationLink = GreenMailUtil.getBody(msgs[0]).trim();

        try {
            // 3a. try login but not activated
            accountService.login("juven", "admin123");
            fail("Disabled account shouldn't be able to log in.");
        } catch (AccountServiceException e) {
            e.printStackTrace();
        }

        //4.Activate account
        String activationCode = activationLink.substring(activationLink.lastIndexOf("=") + 1);
        accountService.activate(activationCode);

        //5. Login with correct id and password
        accountService.login("juven", "admin123");

        try {
            //5a.Login with incorrect passwod
            accountService.login("juven", "admin456");
            fail("Password is incorrect,shouldn't be able to login.");
        } catch (AccountServiceException e) {
        }

    }

    @After
    public void stopMailServer() {
        greenMail.stop();
    }


}
