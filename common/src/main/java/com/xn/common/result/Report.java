package com.xn.common.result;/**
 * Created by xn056839 on 2016/9/7.
 */

import com.xn.common.response.Assert;
import com.xn.common.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Report {
    private static final Logger logger = LoggerFactory.getLogger(Report.class);
    private static Report report;

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }

    private Date startTime;
    private Date stopTime;
    private int error;
    private int failed;
    private int total;


    public ArrayList<Assert> getAssertList() {
        return assertList;
    }

    private ArrayList<Assert> assertList = new ArrayList();

    private Report() {
    }

    public static synchronized Report getReport() {
        if (report == null) {
            report = new Report();
        }
        return report;
    }

    public Date getStartTime() {
        return startTime;
    }


    public synchronized int getError() {
        return error;
    }

    public synchronized void setError(int error) {
        this.error = error;
    }

    public synchronized int getFailed() {
        return failed;
    }

    public synchronized void setFailed(int failed) {
        this.failed = failed;
    }

    public synchronized int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total += total;
    }

    public static void errorPlus() {
        int currentError = getReport().getError();
        getReport().setError(currentError + 1);
    }

    public static void failedPlus() {
        int currentFailed = getReport().getFailed();
        getReport().setFailed(currentFailed + 1);
    }

    public synchronized void assertAdd(Assert assertItem) {
        assertList.add(assertItem);
    }

    public String getTimeLong() {

        long between = 0;
        try {

            between = (stopTime.getTime() - startTime.getTime());// 得到两者的毫秒数
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            return String.valueOf(between);
        }
    }

    public void generateReport() {
        StringBuffer content = new StringBuffer();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        content.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head lang=\"en\">\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>dubbo接口测试报告</title>\n" +
                "</head>\n" +
                "<body style=\"height: 100%\"><div class=\"heading\"><h1 align=\"center\">dubbo接口测试报告</h1></div>");
        content.append("<table style=\"font-weight:bold;\" align=\"center\"><tr><td>开始时间:</td><td>" + format.format(startTime.getTime()) + "</td></tr>");
        content.append("<tr ><td>结束时间:</td><td>" + format.format(stopTime.getTime()) + "</td></tr>");
        content.append("<tr><td>执行时长:</td><td>" + getTimeLong() + "ms</td></tr>");
        content.append("<tr><td>用例总数:</td><td>" + total + "</td></tr>");
        content.append("<tr><td>失败个数:</td><td>" + failed + "</td></tr>");
        content.append("<tr><td>异常个数:</td><td>" + error + "</td></tr></table></br>");
        if (assertList.size() > 0) {
            content.append("<h2 align=\"center\">检验失败接口</h2>");
            content.append("<table align=\"center\" border=1 WIDTH=1000 HEIGHT=200><tr align=\"center\" style=\"width:120px; height: 21px; font-weight:bold; \" valign=\"middle\"><td>case名</td><td>校验字段</td><td>期望结果</td><td>实际结果</td></tr>");
            for (Assert asserts : assertList) {
                if (asserts.getDiff() != null) {
                    String caseName = asserts.getInterfaceName() + "/" + asserts.getMethodName() + "/" + asserts.getCaseName();
                    String key = asserts.getDiff().getAssertKey();
                    String expect = asserts.getDiff().getExpect();
                    String exact = asserts.getDiff().getExact();
                    content.append("<tr align=\"center\" style=\"width:120px; height: 21px;\" valign=\"middle\"><td>" + caseName + "</td><td>" + key + "</td><td>" + expect + "</td><td>" + exact + "</td></tr>");
                }
            }
            content.append("</table>");
            content.append("<h2 align=\"center\">抛出异常接口</h2>");
            content.append("<table align=\"center\" border=1 WIDTH=1000 HEIGHT=200><tr align=\"center\" style=\"width:120px; height: 21px; font-weight:bold; \" valign=\"middle\"><td>case名</td><td>抛出异常</td></tr>");
            for (Assert asserts : assertList) {
                if (asserts.getException() != null) {
                    String caseName = asserts.getInterfaceName() + "/" + asserts.getMethodName() + "/" + asserts.getCaseName();
                    String exception = asserts.getException().getCause().getMessage();
                    content.append("<tr align=\"center\" style=\"width:120px; height: 21px;\" valign=\"middle\"><td>" + caseName + "</td><td>" + exception + "</td></tr>");

                }

            }
            content.append("</table>");
        }
        content.append("</body>");


        FileUtil.fileWrite("suite/report.html", content.toString());

    }
}
