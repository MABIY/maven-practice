package com.juvenxu.mvnbook.account.email;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static org.junit.Assert.assertEquals;

/**
 * Created by lh on 17-3-1.
 */
public class AccountEmailServiceTest {

    private GreenMail   greenMail;

    @Before
    public void startMailServer() {
        greenMail = new GreenMail(new ServerSetup(8001 ,(String)null,"smtp"));
        greenMail.setUser("test@juvenxu.com", "123456");
        greenMail.start();
    }

    @Test
    public void testSendMail() throws AccountEmailException, InterruptedException, MessagingException {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("account-email.xml");
        AccountEmailService accountEmailService = (AccountEmailService) ctx.getBean("accountEmailService");

        String subject = "Test Subject";
        String htmlTest = "<h3>Test</h3>";

        accountEmailService.sendMail("test32@juvenxu.com", subject, htmlTest);
        greenMail.waitForIncomingEmail(2000, 1);
        MimeMessage[] msgs = greenMail.getReceivedMessages();
        assertEquals(1, msgs.length);
        assertEquals(subject,msgs[0].getSubject());
        assertEquals(htmlTest, GreenMailUtil.getBody(msgs[0]).trim());

    }
    @After
    public void stopMailServer() {
       greenMail.stop();
    }

}
