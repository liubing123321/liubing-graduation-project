<%@ page import="com.google.common.collect.Lists" %>
<%@ page import="com.google.common.collect.Maps" %>
<%@ page import="com.wormpex.api.json.JsonUtil" %>
<%@ page import="com.wormpex.api.pojo.page.PageRequest" %>
<%@ page import="com.wormpex.api.pojo.page.Pager" %>
<%@ page import="com.wormpex.bach.affair.baseinfo.api.model.check.CheckSummary" %>
<%@ page import="com.wormpex.bach.affair.baseinfo.api.remote.CheckRecordRemote" %>
<%@ page import="com.wormpex.bach.affair.baseinfo.api.request.check.QueryCheckSummaryReq" %>
<%@ page import="com.wormpex.bach.affair.core.api.model.check.CheckOrder" %>
<%@ page import="com.wormpex.bach.affair.core.api.util.order.CheckOrderConverter" %>
<%@ page import="com.wormpex.bach.affair.man.common.constant.ManConstant" %>
<%@ page import="com.wormpex.bach.affair.man.common.constant.QueryPathConstant" %>
<%@ page import="com.wormpex.bach.affair.man.common.util.DateFormatUtil" %>
<%@ page import="com.wormpex.bach.affair.man.common.util.base.PageBuilder" %>
<%@ page import="com.wormpex.bach.affair.man.service.particular.OrderStoreRemoteWrapper" %>
<%@ page import="com.wormpex.inf.order.store.api.expression.Expression" %>
<%@ page import="com.wormpex.inf.order.store.api.expression.ExpressionBuilder" %>
<%@ page import="com.wormpex.inf.order.store.api.model.Order" %>
<%@ page import="com.wormpex.inf.order.store.api.model.VersionData" %>
<%@ page import="com.wormpex.inf.order.store.api.query.Query" %>
<%@ page import="com.wormpex.inf.order.store.api.type.Comparisons" %>
<%@ page import="com.wormpex.inf.order.store.api.type.Ordering" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page import="org.springframework.context.ApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>queryorder</title>
</head>
<body>

<%
    PrintWriter outPrint = response.getWriter();
    try {
        Logger logger = LoggerFactory.getLogger(FixTransferCanOutTime.jsp
                fixTransferShopInfo.jsp
                queryCheckFix.jsp
                queryOrder.jsp
                SearchSkuNotExsit.jsp
                toInProcess.jsp
                transferCancel.jsp.class);
        logger.info("query order jsp start");
        outPrint.println("query order jsp start");

        ServletContext context = request.getSession().getServletContext();
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);

        OrderStoreRemoteWrapper orderStoreRemoteWrapper = ctx.getBean(OrderStoreRemoteWrapper.class);
        CheckRecordRemote checkRecordRemote = ctx.getBean(CheckRecordRemote.class);

        String checkDateFrom = "2018-01-01 00:00:00";
        String checkDateTo = "2018-01-05 23:59:59";
        String status = "Finished";
        String shopType = "4";

        String finishDateFrom = "2018-01-01 00:00:00";
        String finishDateTo = "2018-01-02 00:00:00";


        List<Expression> expressions = Lists.newArrayList();

        expressions.add(ExpressionBuilder.eq(QueryPathConstant.CheckPath.checkStatus, status));
        expressions.add(ExpressionBuilder.cmp(QueryPathConstant.CheckPath.checkDateTime, checkDateFrom, Comparisons.ge));
        expressions.add(ExpressionBuilder.cmp(QueryPathConstant.CheckPath.checkDateTime, checkDateTo, Comparisons.lt));
        expressions.add(ExpressionBuilder.eq("check-main.shopInfo.shopType", shopType));
        expressions.add(ExpressionBuilder.cmp("check-main.finishedTime", finishDateFrom, Comparisons.ge));
        expressions.add(ExpressionBuilder.cmp("check-main.finishedTime", finishDateTo, Comparisons.lt));

        Query query = Query.newQuery(0, 200).where(ExpressionBuilder.and(expressions.toArray(new Expression[expressions.size()])))
                .orderBy("check-main.finishedTime", Ordering.ASC);

        logger.info("query order jsp , query:{}", JsonUtil.toJson(query));

        Iterable<List<VersionData<Order>>> storeResult = orderStoreRemoteWrapper.queryAllOrder(ManConstant.StoreAppId.CHECK, query);

        logger.info("query order jsp , vesiondata.:{}", JsonUtil.toJson(storeResult));


        Map<String, String> results = Maps.newHashMap();
        int temp = 0 ;
        outer:
        for (List<VersionData<Order>> versionDatas : storeResult) {
            logger.info("query order jsp , vesiondata.size:{}", versionDatas.size());
            for (VersionData<Order> versionData : versionDatas) {
                CheckOrder order = CheckOrderConverter.toBizData(versionData);

                PageRequest.Page page1 = PageBuilder.toPage(1, Integer.MAX_VALUE);

                QueryCheckSummaryReq summaryReq = new QueryCheckSummaryReq();
                summaryReq.setOrderId(order.getOrderId());
                summaryReq.setPage(page1);
                Pager<CheckSummary> result = checkRecordRemote.listCheckSummary(summaryReq);

                int qty_0 = 0;
                int qty = 0;
                double qtyCost = 0d;

                int inventoryAdjust = 0;
                double inventoryAdjustCost = 0d;

                for (CheckSummary summary : result.getData()) {

                    qty += summary.getActualCheckQty();
                    qtyCost += summary.getActualCheckQty() * summary.getCostUnitPrice().getAmount().doubleValue();

                    if (summary.getActualCheckQty() == 0) {
                        qty_0++;
                    }

                    inventoryAdjust += summary.getActualCheckQty() - summary.getSnapQty();
                    inventoryAdjustCost += inventoryAdjust * summary.getCostUnitPrice().getAmount().doubleValue();
                }

                DecimalFormat df = new DecimalFormat("0.00%");
                DecimalFormat df1 = new DecimalFormat("0.00");
                String cvs = order.getMain().getShopInfo().getShopCode() + "," + order.getMain().getShopInfo().getShopName() + "," + (qty_0 == result.getDataSize()) + "," + qty_0 + "," + result.getDataSize()
                        + "," + qty + "," + df1.format(qtyCost)
                        + "," + df.format(((double)qty_0 )/ result.getDataSize()) + "," + inventoryAdjust + "," + df1.format(inventoryAdjustCost)
                        + "," + order.getOrderId() + "," + DateFormatUtil.format4y2M2d2h2m2s(order.getScheme().getCheckDateTime()) + "," + DateFormatUtil.format4y2M2d2h2m2s(order.getMain().getStartDate()) + ","
                        + DateFormatUtil.format4y2M2d2h2m2s(order.getMain().getFinishedTime()) + "," + order.getMain().getFirstCheckOperators().get(0).getUserId() + "," + order.getMain().getFirstCheckOperators().get(0).getUserName();
                results.put(order.getMain().getShopInfo().getShopCode(), cvs);
                temp ++ ;
            }
        }

        for (String cvs : results.values()) {
            outPrint.println(cvs);
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
