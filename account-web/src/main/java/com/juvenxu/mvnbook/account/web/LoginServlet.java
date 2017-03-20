package com.juvenxu.mvnbook.account.web;

import com.juvenxu.mvnbook.account.persist.Account;
import com.juvenxu.mvnbook.account.service.AccountService;
import com.juvenxu.mvnbook.account.service.AccountServiceException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by lh on 17-3-20.
 */
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1;

    private ApplicationContext context;

    @Override
    public void init() throws ServletException {
        super.init();
        context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String password = req.getParameter("password");

        if (id == null || id.length() == 0 || password == null || password.length() == 0) {
            resp.sendError(400,"incomplete parameter");
            return;
        }

        AccountService service = (AccountService) context.getBean("accountService");

        try {
            service.login(id,password);
            resp.getWriter().print("Login Successful");
        } catch (AccountServiceException e) {
            resp.sendError(400,e.getMessage());
        }
    }
}
