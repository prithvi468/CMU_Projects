<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.List" %>
<%--
  Created by IntelliJ IDEA.
  User: Prithvi
  Date: 04-04-2020
  Time: 19:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Dashboard</title>
    <h1>Analysis board</h1>
    <p>Most frequent searched word is  <%=request.getAttribute("mostFreq") %>
    </p>
    <p>Average Latency is  <%= request.getAttribute("averageLatency") %>
    </p>
    <p>Total number of hits today is: <%= request.getAttribute("todayCount") %> </p>
    <p>Total number of Hits by app is:<%= request.getAttribute("totalHits") %>
    </p>
</head>
<body>
<h1>Operations Analytics</h1>


<table style="width:100%" border="1" summary="Logs">
    <%%>
    <tr>
        <th>Search Word</th>
        <th>Request Time taken</th>
        <th>Response Time taken</th>
        <th>Latency</th>
        <th>URL Request</th>
        <th>URL Response</th>
    </tr>
    <% HashMap<Integer, List> tableData = (HashMap<Integer, List>) request.getAttribute("tableData");%>
    <% for (int i = 0; i < tableData.size(); i++) {%>
    <% List<String> display = tableData.get(i);%>
    <tr>
        <td><%=display.get(0)%>
        </td>
        <td><%=display.get(1)%>
        </td>
        <td><%=display.get(2)%>
        </td>
        <td><%=display.get(3)%>
        </td>
        <td><%=display.get(4)%>
        </td>
        <td><%=display.get(5)%>
        </td>
    </tr>
    <% }%>
    <% %>

</table>
</body>
</html>
