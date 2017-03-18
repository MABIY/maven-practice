package com.juvenxu.mvnbook.account.service;

import com.juvenxu.mvnbook.account.captcha.AccountCaptchaService;
import com.juvenxu.mvnbook.account.captcha.AccountCaptchaException;
import com.juvenxu.mvnbook.account.captcha.RandomGenerator;
import com.juvenxu.mvnbook.account.email.AccountEmailException;
import com.juvenxu.mvnbook.account.email.AccountEmailService;
import com.juvenxu.mvnbook.account.persist.Account;
import com.juvenxu.mvnbook.account.persist.AccountPersistException;
import com.juvenxu.mvnbook.account.persist.AccountPersistService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lh on 17-3-18.
 */
@Setter
@Getter
public class AccountServiceImpl implements AccountService {

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private Map<String, String> activationMap = new HashMap<>();

    private AccountPersistService accountPersistService;

    private AccountEmailService accountEmailService;

    private AccountCaptchaService accountCaptchaService;


    @Override
    public String generateCaptchaKey() {
        return accountCaptchaService.generateCaptchaKey();
    }

    @Override
    public byte[] generateCaptchaImage(String captchaKey) throws AccountServiceException {
        try {
            return accountCaptchaService.generateCaptchaImage(captchaKey);
        } catch (AccountCaptchaException e) {
            throw new AccountServiceException("Unable to generate Captcha Image,", e);
        }
    }

    @Override
    public void signUp(SignUpRequest signUpRequest) throws AccountServiceException {
        try {
            if (!signUpRequest.getPassword().equals(signUpRequest.getConfirmPassword())) {
                throw new AccountServiceException("2 passwords do not match.");
            }

            if (!accountCaptchaService.validataCaptcha(signUpRequest.getCaptchaKey(), signUpRequest.getCaptchaValue())) {
                throw new AccountServiceException("Incorrect Captcha");
            }
            Account account = new Account();
            account.setId(signUpRequest.getId());
            account.setEmail(signUpRequest.getEmail());
            account.setName(signUpRequest.getName());
            account.setPassword(signUpRequest.getPassword());
            account.setActivated(false);

            accountPersistService.createAccount(account);

            String activationId = RandomGenerator.getRandomString();

            activationMap.put(activationId, account.getId());

            String link = signUpRequest.getActivateServiceUrl().endsWith("/") ? signUpRequest.getActivateServiceUrl() + activationId : signUpRequest.getActivateServiceUrl() + "?key=" + activationId;

            accountEmailService.sendMail(account.getEmail(), "Please Activate Your Account", link);
        } catch (AccountServiceException e) {
            e.printStackTrace();
        } catch (AccountCaptchaException e) {
            throw new AccountServiceException("Unable to validate captcha.", e);
        } catch (AccountPersistException e) {
            throw new AccountServiceException("Unable to create acctount", e);
        } catch (AccountEmailException e) {
            throw new AccountServiceException("Unable to send actiavtion Mail.", e);
        }
    }

    @Override
    public void activate(String activationNumber) throws AccountServiceException {
        String accountId = activationMap.get(activationNumber);

        if (accountId == null) {
            throw new AccountServiceException("Invalid account activation ID");
        }

        try {
            Account account = accountPersistService.readAccount(accountId);
            account.setActivated(true);
            accountPersistService.updateAccount(account);
        } catch (AccountPersistException e) {
            throw new AccountServiceException("Unable to activate account.");
        }

    }

    @Override
    public void login(String id, String password) throws  AccountServiceException {

        try {
            Account account = accountPersistService.readAccount(id);
            if (account == null) {
                throw new AccountServiceException("Account does not exist");
            }

            if (!account.isActivated()) {
                throw new AccountServiceException("Account is disabled.");
            }

            if (!account.getPassword().equals(password)) {
                throw new AccountServiceException("Incorrect password.");
            }
        } catch (AccountPersistException e) {
            throw new AccountServiceException("Unable to log in.", e);
        }
    }
}
