<%@page contentType="text/html; charset=gb2312" language="java" import="java.sql.*,conn.ConnSql" errorPage="" %>
<html>
<head>
<title>ע��ȷ��</title>
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
  // response.getWriter().write("<script>alert('�Բ�����û��Ѿ����ڣ���������ӣ�');</script>");
if(rs.next())
{
 response.getWriter().write("<script>alert('�Բ�����û��Ѿ����ڣ���������ӣ�');history.go(-1);</script>");
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
�װ���<%=name%>,��������ע����Ϣ<br><br>
<TABLE width="60%" border="1">
<TR>
   <TD>�û���ţ�</TD>
    <TD><%=name%></TD>
</TR>
<TR>
   <TD>�û�������</TD>
    <TD><%=realname%></TD>
</TR>
<TR>
   <TD>��&nbsp;&nbsp;&nbsp;�룺</TD>
    <TD><%=password%></TD>
</TR>
<TR>
   <TD>E_mail��</TD>
    <TD><%=e_mail%></TD>
</TR>
<TR>
   <TD>�绰���룺</TD>
    <TD><%=telephone%></TD>
</TR>


</TABLE>
<br>
<INPUT TYPE="submit" value="�ύ��Ϣ">&nbsp;&nbsp;
<INPUT TYPE="button" value="�����޸�" onclick="history.back()">
</FORM>
</CENTER>
</BODY>
</HTML>




