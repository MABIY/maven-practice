package com.juvenxu.mvnbook.account.persist;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;

import static junit.framework.Assert.*;

/**
 * Created by lh on 17-3-13.
 */
public class AccountPersistServiceTest {

    private AccountPersistService service;

    @Before
    public void prepare() throws AccountPersistException {
        File persistDataFile = new File("target/test-classes/persist-data.xml");
        if (persistDataFile.exists()) {
            persistDataFile.delete();
        }

        ApplicationContext ctx = new ClassPathXmlApplicationContext("account-persist.xml");
        service = (AccountPersistService) ctx.getBean("accountPersistService");

        Account account = new Account();
        account.setId("juven");
        account.setName("juven Xu");
        account.setEmail("juven@changeme.com");
        account.setPassword("this_should_be_encrypted");
        account.setActivated(true);

        service.createAccount(account);
    }

    @Test
    public void testDeleteAccount() throws AccountPersistException {
        assertNotNull(service.readAccount("juven"));
        service.deleteAccount("juven");
        assertNull(service.readAccount("juven"));
    }

    @Test
    public void testCreateAccount() throws AccountPersistException {
        assertNull(service.readAccount("mike"));

        Account account = new Account();
        account.setId("mike");
        account.setName("Mike");
        account.setEmail("mike@changeme.com");
        account.setPassword("this_shoud_be_encrypted");
        account.setActivated(true);

        service.createAccount(account);

        assertNotNull(service.readAccount("mike"));
    }

    @Test
    public void testUpdateAccount() throws AccountPersistException {
        Account account = service.readAccount("juven");

        account.setName("juven Xu1");
        account.setEmail("juven1@changeme.com");
        account.setPassword("this_still_should_be_encrypted");
        account.setActivated(true);

        service.updateAccount(account);

        account = service.readAccount("juven");

        assertEquals("juven Xu1", account.getName());
        assertEquals("juven1@changeme.com", account.getEmail());
        assertEquals("this_still_should_be_encrypted", account.getPassword());
        assertTrue(account.isActivated());
    }
}
