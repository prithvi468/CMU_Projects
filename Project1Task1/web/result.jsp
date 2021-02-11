<%--
  Created by IntelliJ IDEA.
  User: Prithvi
  Date: 01-02-2020
  Time: 20:54
  To change this template use File | Settings | File Templates.
--%>
<%-- prithvipoddar Prithvip --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
<head>
    <title>Output Page </title>
</head>
<body>
<% if (request.getAttribute("textString") != null) { %>
<h1>RESULT</h1><br>
<h3>Original Text : <%= request.getAttribute("textString")%>
</h3>
<h3>The Hash method used is : <%= request.getAttribute("hashMethod")%>
</h3>
<h3>hexadecimal text: <%= request.getAttribute("hexaBinary")%>
</h3>
<h3>Base 64 Notation: <%= request.getAttribute("bin64")%>
</h3><br>
<% } else {%>
<h1>The input was empty, the  <%= request.getParameter("searchWord")%> could not be found</h1><br>
<% } %>
<form action="ComputeHashes" method="GET">
    Enter Text Data:
    <input name="data" type="text"><br>
    Choose a hash function:
    <br><br>
    <input type="radio" name="hashChoice" value="MD5" checked> MD5<br><br>
    <input type="radio" name="hashChoice" value="SHA-256"> SHA-256<br><br>
    <input type="submit" value=" submit">
</form>
</body>
</html>
