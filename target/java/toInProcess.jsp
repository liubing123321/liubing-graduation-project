<%@ page import="org.springframework.context.ApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="com.google.common.collect.Lists" %>
<%@ page import="com.wormpex.bach.affair.man.biz.transfer.TransferOrderBizImpl" %>
<%@ page import="java.util.List" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="com.wormpex.api.json.JsonUtil" %>
<%@ page import="com.wormpex.bach.affair.man.model.vo.pda.AffairUserContext" %>
<%@ page import="com.wormpex.bach.affair.man.model.vo.pda.AffairUserInfo" %>
<%@ page import="org.joda.time.DateTime" %>
<%@ page import="com.wormpex.bach.affair.man.common.util.DateFormatUtil" %>
<%@ page import="java.util.Date" %>
<%--
  Created by IntelliJ IDEA.
  User: kangqizheng
  Date: 2017/8/23
  Time: 上午11:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>OpenProduct</title>
</head>
<body>

</body>

<%
    try {
        AffairUserInfo affairUserInfo = new AffairUserInfo();
        affairUserInfo.setUserName("修数据");
        affairUserInfo.setUserRole("");
        affairUserInfo.setMobile("");
        affairUserInfo.setTerminal("");
        affairUserInfo.setUserId("huojia.wuren");
        affairUserInfo.setUserIp("");
        affairUserInfo.setShopInfo(null);
        AffairUserContext.setUserInfo(affairUserInfo);
        Logger logger= LoggerFactory.getLogger(TransferOrderBizImpl.class);
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(getServletConfig().getServletContext());
        TransferOrderBizImpl transferOrderBiz = applicationContext.getBean(TransferOrderBizImpl.class);
        
        String param = "";
        String params[] = param.split(",");
        String batchNo= DateFormatUtil.parseyyyyMMddHHmmss(new Date());
        for(String key : params){
            try{
                transferOrderBiz.updateStatusToInProcess(Long.valueOf(key), false);
                logger.info("{} updateStatusToInProcess order success param:{}",batchNo, key);
            }catch (Exception e){
                logger.error("{} updateStatusToInProcess order fail param:{}", batchNo, key);
            }
        }
    }catch (Exception e){
        e.printStackTrace(response.getWriter());
    }finally {
        response.getWriter().close();
    }
%>

</html>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              