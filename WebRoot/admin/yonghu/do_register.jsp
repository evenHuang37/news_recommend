<%@page contentType="text/html; charset=gb2312" language="java" import="java.sql.*,conn.ConnSql" errorPage="" %>

<html>
<head>
<title>ע��ɹ�</title>
<link rel="stylesheet" href="../style.css" type="text/css">
</head>
<body  background="../../pic/back.jpg"> 
<%
  String name=request.getParameter("name");
  
  session.setAttribute("name",name);
  session.setAttribute("frist","frist");
  String pwd=request.getParameter("password");
  String e_mail=request.getParameter("e_mail");
  String telephone=request.getParameter("telephone");
  String address=request.getParameter("address");
  
  session.setAttribute("hobby",address);
  
    String class_type=request.getParameter("class_type");
    String realname=request.getParameter("realname");
  java.util.Date  regTime=new java.util.Date();
  
  String register_Time=regTime.toString();

  String query="insert into customer_info(namereal,Name,Telephone,Email,Address,Login_Time,Passwd,type)values('"+realname+"','"+name+"','"+telephone+"','"+e_mail+"' ,'"+address+"','"+register_Time+"','"+pwd+"','"+class_type+"')";

ConnSql cs=new ConnSql();
		Statement stmt = cs.getStatement(); 
stmt.executeUpdate(query);

stmt.close();

out.println("��ϲ���û���Ϣ��ӳɹ����û�����Ϣ�Ѿ�д�����ݿ⣡");
//}
//catch (SQLException e)
//{
 // out.println(e.getMessage());
//}
%>
<a name=a href="../../user/index.jsp">����ϵͳ</a>
</body>
</html>