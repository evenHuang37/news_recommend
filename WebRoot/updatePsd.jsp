<%@ page contentType="text/html;charset=gb2312" %>

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
      alert("������������");
      return false
   }
   if((form.password.value.length<3)||(form.password.value.length>8)){
	alert("���������3-8λ��ĸ������");
	return false;
}
      if (form.re_password.value == "")
   {
      alert("����������ȷ��");
      return false
   }
  if(form.password.value!=form.re_password.value){
	
	alert("�������벻ͬ!");
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
	response.getWriter().write("<script>alert('������³ɹ�');</script>");
}
}
else
{
response.getWriter().write("<script>alert('�������ʧ��');</script>");
}
%>




<body bgcolor="#F4F5FF" leftmargin="0" topmargin="0" background="../../pic/back.jpg" >
<form name="login" action="updatePsd.jsp" method=post onSubmit="return ValidateLoginForm(this)"> <table width="101" height="101" align= "top" border="0" cellpadding="0" cellspacing="0"> 
 
				  <table width="38%" border="0" cellpadding="4" cellspacing="1" bgcolor="#999999">
				    <tr align="center" bgcolor="#f6f6f6">
				      <td colspan="2"><b>�޸�����</b></td>
				   </tr>
                      <tr align="center" bgcolor="#FFFFFF">
                        <td width="36%"><div align="right">�����룺</div></td>
                        <td width="64%"><input name="password" type="password" ></td>
                      </tr>
                      <tr align="center" bgcolor="#FFFFFF">
                        <td><div align="right">����ȷ�ϣ�</div></td>
                        <td><input name="re_password" type="password"  value=""></td>
                      </tr>
		             <tr align="center" bgcolor="#FFFFFF">
		                 <td><div align="right">ȷ���޸ģ�</div></td>
                        <td colspan="2"><input type="submit" value="�ύ" width="50" height="22" border="0" ></a></td>
                      </tr>
                    </table>
                    
</Form> 
</body>
</html>
