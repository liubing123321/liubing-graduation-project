<%@ page import="com.wormpex.bach.affair.core.api.model.transfer.TransferOrder" %>
<%@ page import="com.wormpex.bach.affair.core.api.util.order.TransferOrderConverter" %>
<%@ page import="com.wormpex.bach.affair.man.common.constant.ManConstant" %>
<%@ page import="com.wormpex.bach.affair.man.common.constant.QueryPathConstant" %>
<%@ page import="com.wormpex.bach.affair.man.model.vo.pda.AffairUserContext" %>
<%@ page import="com.wormpex.bach.affair.man.model.vo.pda.AffairUserInfo" %>
<%@ page import="com.wormpex.bach.affair.man.service.particular.OrderStoreRemoteWrapper" %>
<%@ page import="com.wormpex.inf.order.store.api.expression.Expression" %>
<%@ page import="com.wormpex.inf.order.store.api.expression.ExpressionBuilder" %>
<%@ page import="com.wormpex.inf.order.store.api.model.Order" %>
<%@ page import="com.wormpex.inf.order.store.api.model.VersionData" %>
<%@ page import="com.wormpex.inf.order.store.api.query.Query" %>
<%@ page import="com.wormpex.inf.order.store.api.type.Ordering" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page import="org.springframework.context.ApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.List" %>
<%@ page import="com.wormpex.api.json.JsonUtil" %>
<%@ page import="org.springframework.context.ApplicationContext" %>
<%@ page import="com.google.common.collect.Lists" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>queryorder</title>
</head>
<body>

<%
    PrintWriter outPrint = response.getWriter();
    try {
        Logger logger = LoggerFactory.getLogger(OrderStoreRemoteWrapper.class);
        logger.info("query order jsp start");
        outPrint.println("query order jsp start");
        AffairUserInfo affairUserInfo = new AffairUserInfo();
        affairUserInfo.setUserName("cancel.jiaqiang.yan");
        affairUserInfo.setUserRole("");
        affairUserInfo.setMobile("");
        affairUserInfo.setTerminal("");
        affairUserInfo.setUserId("cancel.jiaqiang.yan");
        affairUserInfo.setUserIp("");
        affairUserInfo.setShopInfo(null);
        AffairUserContext.setUserInfo(affairUserInfo);

        ServletContext context = request.getSession().getServletContext();
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);

        OrderStoreRemoteWrapper orderStoreRemoteWrapper = ctx.getBean(OrderStoreRemoteWrapper.class);


        String orderIds = "49037601544204";
        List<String> orderIdList = Arrays.asList(orderIds.split(","));

        List<String> orderIdListNew = Lists.newArrayList();
        for (String orderId : orderIdList) {
            orderIdListNew.add(orderId.trim());
        }


        List<List<String>> orderIdBatch = Lists.partition(orderIdListNew, 200);

        for (List<String> idBatch : orderIdBatch) {

            logger.info("idBatch:{}", JsonUtil.toJson(idBatch));

            String[] orderIdArr = org.unidal.helper.Lists.toArray(String.class, idBatch);

            Expression inOrder = ExpressionBuilder.in(ManConstant.TransferQueryPathConstant.orderIdField, orderIdArr);
            Query query = Query.newQuery(0, 200).where(ExpressionBuilder.and(inOrder))
                    .orderBy(QueryPathConstant.TransferPath.createDate, Ordering.ASC)
                    .orderBy(QueryPathConstant.TransferPath.orderId, Ordering.ASC);

            logger.info("query order jsp , query:{}", JsonUtil.toJson(query));

            Iterable<List<VersionData<Order>>> storeResult = orderStoreRemoteWrapper.queryAllOrder(ManConstant.StoreAppId.TRANSFER, query);

            logger.info("query order jsp , vesiondata.:{}", JsonUtil.toJson(storeResult));

            for (List<VersionData<Order>> versionDatas : storeResult) {
                logger.info("query order jsp , vesiondata.size:{}", versionDatas.size());
                for (VersionData<Order> versionData : versionDatas) {
                    TransferOrder order = TransferOrderConverter.toBizData(versionData);
                    outPrint.println(order.getOrderId() + "," + order.getMain().getMonetOrderId() + "," + order.getMain().getTransferStatus().getName());
                    logger.info("######query order jsp , result:{}", order.getOrderId() + "," + order.getMain().getMonetOrderId() + "," + order.getMain().getTransferStatus().getName());

                }

            }
        }


    } catch (Exception e) {
        outPrint.println("An exception occurred: " + e.getMessage());
        outPrint.println();
        for (StackTraceElement k : e.getStackTrace())
            outPrint.println(k);
    } finally {
        outPrint.close();
    }
%>
</body>
</html>
