<%--
  Created by IntelliJ IDEA.
  User: lh
  Date: 17-3-20
  Time: 下午6:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="com.juvenxu.mvnbook.account.service.* ,
org.springframework.context.ApplicationContext,
org.springframework.web.context.support.WebApplicationContextUtils" %>
<html>
<head>
    <style type="text/css">
        .text-field {position: absolute; left: 40%; background-color:rgb(255,230,220);}
        label {display: inline-table; width: 90px; margin: 0px 0px 10px 20px; }
        input {display: inline-table; width: 150px; margin: 0px 20px 10px 0px;}
        img {width:150px; margin: 0px 20px 10px 110px;}
        h2 {margin: 20px 20px 20px 40px;}
        button {margin: 20px 20px 10px 110px
    </style>
</head>
<body>
   <%
       ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
       AccountService accountService = (AccountService) context.getBean("accountService");
       String captchaKey = accountService.generateCaptchaKey();
   %>

<div class="text-field">
    <h2>注册账户</h2>
    <form name="signup" action="signup" method="post">
        <label>账户ID: </label> <input type="text" name="id"> <br/>
        <label>Email: </label> <input type="text" name="email"> <br/>
        <label>显示名称: </label><input type="text" name="name" > <br/>
        <label>密码: </label><input type="password" name="password"><br/>
        <label>确认密码: </label><input type="password" name="confirm_password"><br/>
        <label>验证码: </label><input type="text" name="captcha_value"> <br/>
    </form>
</div>
</body>
</html>
