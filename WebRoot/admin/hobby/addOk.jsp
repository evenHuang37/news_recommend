<%@ page contentType="text/html; charset=gb2312" %>
<%@ page language="java" %>
<%@ page import="java.sql.*" %>
<%@ page import="conn.ConnSql" %>
<%@ page import="conn.CDate" %>
 <%@ page contentType="text/html; charset=gb2312" language="java" 
import="java.util.*,com.jspsmart.upload.*" errorPage="" %>


<html>
<head>
<title>��ӳɹ�</title>
<link rel="stylesheet" href="../style.css" type="text/css">
</head>
<body  background="../../pic/back.jpg"> 
<center><h5>��ӳɹ�</h5></center>




<%
String name= request.getParameter("name") ;
String des= request.getParameter("des") ;
ConnSql cs=new ConnSql();
String query="select name from hobby where name='"+name+"'";
ResultSet rs11=cs.executeQuery(query);
if(rs11.next())
{
 response.getWriter().write("<script>alert('�Բ���ð����Ѿ����ڣ���������ӣ�');history.go(-1);</script>");
 rs11.close();
  return;
 }
else
{
rs11.close();
}



String add="insert into hobby(name,des)values('"+name+"','"+des+"')";
cs.executeUpdata(add);
 response.getWriter().write("<script>alert('�ð�����ӳɹ���');history.go(-1);</script>");
%>


</body> 
</html> 
