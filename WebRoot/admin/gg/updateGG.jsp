<%@ page contentType="text/html;charset=gb2312" import="java.sql.*,conn.ConnSql"%>

<html>
<head>
<title>�����Ƽ�ϵͳ</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link href="./admin/style.css" rel="stylesheet" type="text/css">
<script language="JavaScript">
function ValidateLoginForm(form)
{
   

  
      if (form.password.value == "")
   {
      alert("�����빫������");
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
	response.getWriter().write("<script>alert('������³ɹ�');</script>");
}


 rs=cs.executeQuery("select content from gong ");
rs.next();
%>




<body bgcolor="#F4F5FF" leftmargin="0" topmargin="0" background="../../pic/back.jpg" >
<form name="login" action="updateGG.jsp" method=post onSubmit="return ValidateLoginForm(this)"> <table width="101" height="101" align= "top" border="0" cellpadding="0" cellspacing="0"> 
 
				  <table width="68%" border="0" cellpadding="4" cellspacing="1" bgcolor="#999999">
				    <tr align="center" bgcolor="#f6f6f6">
				      <td colspan="2"><b>���¹���</b></td>
				   </tr>
				   <tr align="center" bgcolor="#FFFFFF">
                        <td width="30%"><div align="right">�������ݣ�</div></td>
                        <td width="70%"><textarea style="font-size:18px;" rows="20" cols="70" name="gonggao"><%=rs.getString(1) %></textarea></td>
                      </tr>
		             <tr align="center" bgcolor="#FFFFFF">
		                 <td><div align="right">ȷ�����£�</div></td>
                        <td colspan="2"><input type="submit" value="�ύ" width="50" height="22" border="0" ></a></td>
                      </tr>
                    </table>
                    
</Form> 
</body>
</html>
