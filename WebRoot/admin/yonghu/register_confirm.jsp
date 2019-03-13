<%@page contentType="text/html; charset=gb2312" language="java" import="java.sql.*,conn.ConnSql" errorPage="" %>
<html>
<head>
<title>注册确认</title>
<link rel="stylesheet" href="../style.css" type="text/css">
</head>
<body  background="../../pic/back.jpg"> 
<center>

<% 
  String name=request.getParameter("name");
  String password=request.getParameter("password");
  String e_mail=request.getParameter("e_mail");
  String telephone=request.getParameter("telephone");
  String address=request.getParameter("address");
  String class_type=request.getParameter("class");
  String realname=request.getParameter("realname");
  String query="select name from customer_info where name='"+name+"'";                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  
   ConnSql cs=new ConnSql();
		Statement statement = cs.getStatement(); 
ResultSet rs=statement.executeQuery(query);
 //response.getWriter().write(rs.getString("name"));
 // response.getWriter().write(name);
  // response.getWriter().write("<script>alert('对不起该用户已经存在，请重新添加！');</script>");
if(rs.next())
{
 response.getWriter().write("<script>alert('对不起该用户已经存在，请重新添加！');history.go(-1);</script>");
 rs.close();
 }
else
{
rs.close();
}
%>
<FORM METHOD=POST ACTION="do_register.jsp">
<INPUT TYPE="hidden" name="name" value="<%=name%>">
<INPUT TYPE="hidden" name="password" value="<%=password%>">
<INPUT TYPE="hidden" name="e_mail" value="<%=e_mail%>">
<INPUT TYPE="hidden" name="telephone" value="<%=telephone%>">
<INPUT TYPE="hidden" name="address" value="<%=address%>">
<INPUT TYPE="hidden" name="class_type" value="<%=class_type%>">
<INPUT TYPE="hidden" name="realname" value="<%=realname%>">
亲爱的<%=name%>,请检查您的注册信息<br><br>
<TABLE width="60%" border="1">
<TR>
   <TD>用户编号：</TD>
    <TD><%=name%></TD>
</TR>
<TR>
   <TD>用户姓名：</TD>
    <TD><%=realname%></TD>
</TR>
<TR>
   <TD>密&nbsp;&nbsp;&nbsp;码：</TD>
    <TD><%=password%></TD>
</TR>
<TR>
   <TD>E_mail：</TD>
    <TD><%=e_mail%></TD>
</TR>
<TR>
   <TD>电话号码：</TD>
    <TD><%=telephone%></TD>
</TR>


</TABLE>
<br>
<INPUT TYPE="submit" value="提交信息">&nbsp;&nbsp;
<INPUT TYPE="button" value="返回修改" onclick="history.back()">
</FORM>
</CENTER>
</BODY>
</HTML>




