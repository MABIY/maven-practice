package com.juvenxu.mvnbook.account.persist;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.List;

/**
 * Created by lh on 17-3-13.
 */
public class AccountPersistServiceImpl implements AccountPersistService {
    public static final String ELEMENT_ROOT = "account-persist";
    public static final String ELEMENT_ACCOUNTS = "accounts";
    public static final String ELEMENT_ACCOUNT = "account";
    public static final String ELEMENT_ACCOUNT_ID = "id";
    public static final String ELEMENT_ACCOUNT_NAME = "NAME";
    public static final String ELEMENT_ACCOUNT_EMAIL= "email";
    public static final String ELEMENT_ACCOUNT_PASSWORD = "password";
    public static final String ELEMENT_ACCOUNT_ACTIVATED = "activated";

    private String file;

    private SAXReader reader = new SAXReader();

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Override
    public Account createAccount(Account account) throws AccountPersistException {
        Document doc = readDocument();

        Element accountsEle = doc.getRootElement().element(ELEMENT_ACCOUNTS);

        accountsEle.add(buildAccountElement(account));

        writeDocument(doc);

        return account;
    }

    private Account buildAccount(Element accountEle) {
        Account account = new Account();

        account.setId(accountEle.elementText(ELEMENT_ACCOUNT_ID));
        account.setName(accountEle.elementText(ELEMENT_ACCOUNT_NAME));
        account.setEmail(accountEle.elementText(ELEMENT_ACCOUNT_EMAIL));
        account.setPassword(accountEle.elementText(ELEMENT_ACCOUNT_PASSWORD));
        account.setActivated("true".equals(accountEle.elementText(ELEMENT_ACCOUNT_ACTIVATED)) ? true:false);

        return account;
    }

    private Element buildAccountElement(Account account) {
        Element element = DocumentFactory.getInstance().createElement(ELEMENT_ACCOUNT);

        element.addElement(ELEMENT_ACCOUNT_ID).setText(account.getId());
        element.addElement(ELEMENT_ACCOUNT_NAME).setText(account.getName());
        element.addElement(ELEMENT_ACCOUNT_EMAIL).setText(account.getEmail());
        element.addElement(ELEMENT_ACCOUNT_PASSWORD).setText(account.getPassword());
        element.addElement(ELEMENT_ACCOUNT_ACTIVATED).setText(account.isActivated() ? "true": "false");

        return element;
    }

    private Document readDocument() throws AccountPersistException {
        File dataFile = new File(file);

        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            Document doc = DocumentFactory.getInstance().createDocument();
            Element rootEle = doc.addElement(ELEMENT_ROOT);
            rootEle.addElement(ELEMENT_ACCOUNTS);
            writeDocument(doc);
        }
        try {
            return reader.read(new File(file));
        } catch (DocumentException e) {
            throw new AccountPersistException("unable to read persist data xml", e);
        }
    }

    private void writeDocument(Document doc) throws AccountPersistException {
        Writer out = null;

        try {
            out = new OutputStreamWriter(new FileOutputStream(file), "utf-8");
            XMLWriter witter = new XMLWriter(out, OutputFormat.createPrettyPrint());

            witter.write(doc);
        } catch (IOException e) {
            throw new AccountPersistException("Unable to write persist data xml", e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                throw new AccountPersistException("Unable to close persist data xml wirter", e);
            }
        }

    }

    @SuppressWarnings("unchecked")
    @Override
    public Account readAccount(String id) throws AccountPersistException {
        Document doc = readDocument();

        Element accountEle = doc.getRootElement().element(ELEMENT_ACCOUNTS);

        for (Element accountElement : (List<Element>) accountEle.elements()) {
            if (accountElement.elementText(ELEMENT_ACCOUNT_ID).equals(id)) {
                return buildAccount(accountElement);
            }
        }
        return null;
    }

    @Override
    public Account updateAccount(Account account) throws AccountPersistException {
        if (readAccount(account.getId()) != null) {

            deleteAccount(account.getId());

            return createAccount(account);
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void deleteAccount(String id) throws AccountPersistException {
        Document doc = readDocument();

        Element accountsElement = doc.getRootElement().element(ELEMENT_ACCOUNTS);

        for (Element accountElement : (List<Element>) accountsElement.elements()) {
            accountElement.detach();
            writeDocument(doc);
        }
    }
}
