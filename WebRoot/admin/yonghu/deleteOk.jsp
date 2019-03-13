<%@ page contentType="text/html; charset=gb2312" %>
<%@ page language="java" %>
<%@ page import="java.sql.*" %>
<%@ page import="conn.ConnSql" %>

<%

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              
ConnSql cs=new ConnSql();

String id=request.getParameter("id").trim();
String del="delete from customer_info where customer_id='"+id+"'";
cs.executeUpdata(del);

%>
<html>
<head>
<title>删除成功</title>
<link rel="stylesheet" href="../style.css" type="text/css">
</head>
<body  background="../../pic/back.jpg"> 
<center><h5>删除成功</h5></center>
</body>
</html>

 