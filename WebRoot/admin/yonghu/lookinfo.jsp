<%@page contentType="text/html; charset=gb2312" language="java" import="java.sql.*,conn.ConnSql" errorPage="" %>
<%
  ConnSql cs=new ConnSql();
  String name=session.getAttribute("name").toString();
  String select1="select * from customer_info where Name='"+name+"'";
  
  ResultSet rs=cs.executeQuery(select1);
           rs.next();
   int num=rs.getInt(6);
   String select2="select * from hobby where id="+num;
       ResultSet rs1=cs.executeQuery(select2);
           rs1.next(); 
                          
%> 
<html>
<head>
<title>�����Ƽ�ϵͳ</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="../style.css" type="text/css">
<script language="JavaScript">
function ValidateLoginForm(form)
{
   

   if (form.name.value == "")
   {
      alert("�������û�ID");
      return false
   }
   if (form.telephone.value == "")
   {
      alert("������绰");
      return false
   }
   if (form.e_mail.value == "")
   {
      alert("������e_mail");
      return false
   }
      if (form.address.value == "")
   {
      alert("�����밮��");
      return false
   }
   return true
}
 </script>
</head>

<body bgcolor="#F4F5FF" leftmargin="0" topmargin="0" background="../../pic/back.jpg" ><form name="login" action="register_confirm.jsp" method=post onSubmit="return ValidateLoginForm(this)"> <table width="101" height="101" align= "top" border="0" cellpadding="0" cellspacing="0"> 

                    
  <table width="38%" border="0" cellpadding="4" cellspacing="1" bgcolor="#999999">
    <tr align="center" bgcolor="#f6f6f6">
                        
      <td colspan="2"><b>�鿴������Ϣ</b></td>
                      </tr>
                      <tr align="center" bgcolor="#FFFFFF">
                        <td width="21%"><div align="right">�û���:</div></td>
                        <td width="79%"><%=rs.getString(2) %></td>
                      </tr>
                      <tr align="center" bgcolor="#FFFFFF">
                        <td><div align="right">��ʵ����:</div></td>
                        <td><%=rs.getString(9) %></td>
                      </tr>
                      
					  
    <tr bgcolor="#FFFFFF"> 
      <td height="25"> 
        <div align="right">�� 
      ����</div></td>
                                
      <td height="25"> 
        <div align="center"> 
         <%=rs.getString(4) %>
        </div></td>
    </tr>
                              
    <tr bgcolor="#FFFFFF"> 
      <td height="25">        <div align="right">E_mail ��</div></td>
                                
      <td height="25"> 
        <div align="center"> 
         <%=rs.getString(5) %>
        </div></td>
    </tr>
                              
    <tr bgcolor="#FFFFFF"> 
      <td height="25"> 
        <div align="right">���ã�</div></td>
                          
      <td height="25"> 
        <div align="center"> 
                         <%=rs1.getString(2) %>               
        </div></td>
    
                    </table>
                 
          
 </Form> 
</body>
</html>
