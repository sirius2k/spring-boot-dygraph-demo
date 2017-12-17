<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="ko">
<head>
    <script type="text/javascript" src="js/dygraph.min.js"></script>
    <link rel="stylesheet" src="css/dygraph.css" />
</head>
<body>
<div style="text-align:left; width:100%; font-size: 20pt;">
    <form id="ccuForm">
        <span id="datetime"></span>
        <b>PC</b> : <fmt:formatNumber value="${betagoCCU.pc}"/>
        <b>Mobile</b> : <fmt:formatNumber value="${betagoCCU.mobile}"/>
        <select name="timeBefore" value="${param.timeBefore}" onChange="changeTimeBefore();">
            <option value="120">최근 2시간</option>
            <option value="240">최근 4시간</option>
            <option value="360">최근 6시간</option>
            <option value="720">최근 12시간</option>
            <option value="1440">최근 1일</option>
            <option value="4320">최근 3일</option>
            <option value="10080">최근 7일</option>
        </select>
    </form>
</div>
<div id="graphdiv2" style="width:100%; height:90%;"></div>
<script type="text/javascript">
    <c:if test="${!empty ccuHistory}">
    g2 = new Dygraph(
        document.getElementById("graphdiv2"),
        "Time,CCU\n" +
        "<c:forEach var='history' items='${ccuHistory}' varStatus='status'><fmt:formatDate pattern='yyyy/MM/dd HH:mm:ss' value='${history.collectedDate}'/>,<c:out value='${history.pc}'/>\n</c:forEach>"<c:if test="${!empty ccuHistory}">,</c:if>
        /*
        {
        rollPeriod: 1,
        showRoller: true
        }
        */
        /*
        // Data sample
        "2017-12-13 20:00,75\n" +
        "2017-12-13 20:01,70\n" +
        "2017-12-13 20:02,80\n"
        */

    );
    </c:if>

    window.onload = function() {
        printTime();
        setInterval(reloadPage, 3000);
    }

    function printTime() {
        var clock = document.getElementById("datetime"); // 출력할 장소 선택
        var now = new Date(); // 현재시간
        var nowTime = now.getFullYear() + "-" + (now.getMonth()+1) + "-" + now.getDate() + " " + now.getHours() + ":" + now.getMinutes() + ":" + now.getSeconds() + "";
        clock.innerHTML = nowTime; // 현재시간을 출력
        setTimeout("printTime()",1000); // setTimeout(“실행할함수”,시간) 시간은1초의 경우 1000
    }

    function reloadPage() {
        location.reload();
    }

    function changeTimeBefore() {
        document.getElementById("ccuForm").submit();
    }
</script>
</body>
</html>