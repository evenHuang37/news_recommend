<%@ page contentType="text/html; charset=gb2312" %>
<%@ page language="java" pageEncoding="gb2312"%>
<%@ page language="java" %>
<%@ page import="java.sql.*" %>
<%@ page import="conn.ConnSql" %>
<%
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  
ConnSql cs=new ConnSql();


String name=request.getParameter("name") ;
String des=request.getParameter("des") ;
String id=request.getParameter("id") ;

String query="select * from hobby where name='"+name+"' and id !="+id;
ResultSet rs11=cs.executeQuery(query);
 // response.getWriter().write(name);
  // response.getWriter().write("<script>alert('�Բ�����û��Ѿ����ڣ���������ӣ�');</script>");
if(rs11.next())
{
 response.getWriter().write("<script>alert('�Բ��𰮺��Ѿ����ڣ���������ӣ�');history.go(-1);</script>");
 rs11.close();
 return;
 }
else
{
rs11.close();
}


String update="update hobby set Name='"+name+"',des='"+des+"' where id="+id;
cs.executeUpdata(update);
response.getWriter().write("<script>alert('�ð����޸ĳɹ���');location.href='updateClass.jsp';</script>");

%>
<html>
<head>
<title>�޸ĳɹ�</title>
<link rel="stylesheet" href="../style.css" type="text/css">
</head>
<body  background="../../pic/back.jpg"> 
<center><h5>�޸ĳɹ�</h5></center>
</body>
</html>

 