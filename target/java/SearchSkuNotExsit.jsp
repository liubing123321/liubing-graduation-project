<%--
  Created by IntelliJ IDEA.
  User: hangtong.li
  Date: 2017/12/5
  Time: 下午7:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.wormpex.bach.affair.man.provider.doors.fix.TransferSkuDoubleCheck" %>
<%@ page import="org.springframework.context.ApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%

    String startTime = request.getParameter("startDate");
    String endTime = request.getParameter("endDate");

    ServletContext context = request.getSession().getServletContext();
    ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);

    TransferSkuDoubleCheck transferSkuDoubleCheck = ctx.getBean(TransferSkuDoubleCheck.class);

    transferSkuDoubleCheck.queryNotExistSku(startTime, endTime);

%>


</body>
</html>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    