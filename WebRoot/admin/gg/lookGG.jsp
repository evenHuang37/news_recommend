<%@ page contentType="text/html;charset=gb2312" import="java.sql.*,conn.ConnSql"%>

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
      alert("请输入公告内容");
      return false
   }

   return true
}
 </script>
</head>

<%@ page import="conn.ConnSql" %>
<%
ResultSet rs=null;
ConnSql cs=new ConnSql();
//response.getWriter().write(SQLid+"dcfs");
String gonggao= request.getParameter("gonggao");
if(gonggao!=null&&!gonggao.equals(""))
{
	String update="update gong set content='"+gonggao+"'";
	cs.executeUpdata(update);
	//response.getWriter().write(update);
	response.getWriter().write("<script>alert('公告更新成功');</script>");
}


 rs=cs.executeQuery("select content from gong ");
rs.next();
%>




<body bgcolor="#F4F5FF" leftmargin="0" topmargin="0" background="../../pic/back.jpg" >
<form name="login" action="updateGG.jsp" method=post onSubmit="return ValidateLoginForm(this)"> <table width="101" height="101" align= "top" border="0" cellpadding="0" cellspacing="0"> 
 
				  <table width="68%" border="0" cellpadding="4" cellspacing="1" bgcolor="#999999">
				    <tr align="center" bgcolor="#f6f6f6">
				      <td colspan="2"><b>查看公告</b></td>
				   </tr>
				   <tr align="center" bgcolor="#FFFFFF">
                        <td width="30%"><div align="right">公告内容：</div></td>
                        <td width="70%" height="120"><div align="center"><%=rs.getString(1) %></div></td>
                      </tr>
                    </table>
                    
</Form> 
</body>
</html>
