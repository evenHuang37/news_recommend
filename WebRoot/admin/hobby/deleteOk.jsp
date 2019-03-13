<%@ page contentType="text/html; charset=gb2312" %>
<%@ page language="java" %>
<%@ page import="java.sql.*" %>
<%@ page import="conn.ConnSql" %>

<%

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              
ConnSql cs=new ConnSql();

String id=request.getParameter("id").trim();
String del="delete from hobby where id='"+id+"'";
cs.executeUpdata(del);
response.getWriter().write("<script>alert('该爱好删除成功！');location.href='deleteClass.jsp';;</script>");
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

 