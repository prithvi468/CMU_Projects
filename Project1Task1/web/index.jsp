<%--
  Created by IntelliJ IDEA.
  User: Prithvi
  Date: 31-01-2020
  To change this template use File | Settings | File Templates.
--%>
<%----%>
<%--/* prithvipoddar Prithvip */--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Compute Hashing</title>
</head>
<body>
<form action="ComputeHashes" method="GET">
    Enter Text Data:
    <input name="data" type="text"><br>
    Choose a hash function: <br><br>
    <input type="radio" name="hashChoice" value="MD5" checked> MD5<br> <br>
    <input type="radio" name="hashChoice" value="SHA-256"> SHA-256<br> <br>
    <input type="submit" value=" submit">
</form>
</body>
</html>
