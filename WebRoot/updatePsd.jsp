<%@ page contentType="text/html;charset=gb2312" %>

<html>
<head>
<title>新闻推荐系统</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link href="./admin/style.css" rel="stylesheet" type="text/css">
<script language="JavaScript">
function ValidateLoginForm(form)
{
   

  
      if (form.password.value == "")
   {
      alert("请输入新密码");
      return false
   }
   if((form.password.value.length<3)||(form.password.value.length>8)){
	alert("密码必须是3-8位字母或数字");
	return false;
}
      if (form.re_password.value == "")
   {
      alert("请输入密码确认");
      return false
   }
  if(form.password.value!=form.re_password.value){
	
	alert("两次密码不同!");
	return false;
}

   return true
}
 </script>
</head>

<%@ page import="conn.ConnSql" %>
<%
if(session.getAttribute("name")!=null&&!session.getAttribute("name").toString().equals(""))                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 
{ConnSql cs=new ConnSql();
String name=session.getAttribute("name").toString();
//response.getWriter().write(SQLid+"dcfs");
String password= request.getParameter("password");
if(password!=null&&!password.equals(""))
{
	String update="update customer_info set Passwd='"+password+"' where Name='"+name+"'";
	cs.executeUpdata(update);
	//response.getWriter().write(update);
	response.getWriter().write("<script>alert('密码更新成功');</script>");
}
}
else
{
response.getWriter().write("<script>alert('密码更新失败');</script>");
}
%>




<body bgcolor="#F4F5FF" leftmargin="0" topmargin="0" background="../../pic/back.jpg" >
<form name="login" action="updatePsd.jsp" method=post onSubmit="return ValidateLoginForm(this)"> <table width="101" height="101" align= "top" border="0" cellpadding="0" cellspacing="0"> 
 
				  <table width="38%" border="0" cellpadding="4" cellspacing="1" bgcolor="#999999">
				    <tr align="center" bgcolor="#f6f6f6">
				      <td colspan="2"><b>修改密码</b></td>
				   </tr>
                      <tr align="center" bgcolor="#FFFFFF">
                        <td width="36%"><div align="right">新密码：</div></td>
                        <td width="64%"><input name="password" type="password" ></td>
                      </tr>
                      <tr align="center" bgcolor="#FFFFFF">
                        <td><div align="right">密码确认：</div></td>
                        <td><input name="re_password" type="password"  value=""></td>
                      </tr>
		             <tr align="center" bgcolor="#FFFFFF">
		                 <td><div align="right">确定修改：</div></td>
                        <td colspan="2"><input type="submit" value="提交" width="50" height="22" border="0" ></a></td>
                      </tr>
                    </table>
                    
</Form> 
</body>
</html>
