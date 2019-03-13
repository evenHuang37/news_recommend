<%@ page contentType="text/html;charset=gb2312" %>
<%@ page language="java" pageEncoding="gb2312"%>
<%@ page import="java.sql.*" %>
<%@ page import="conn.ConnSql" %>
<%
  ConnSql cs=new ConnSql();
  String select="select * from hobby where id="+request.getParameter("id").trim();
  ResultSet rs=cs.executeQuery(select);
            rs.next();                      
%> 
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="../style.css" type="text/css">
<script language="JavaScript">
function ValidateLoginForm(form)
{
   if (form.name.value == "")
   {
      alert("请输入爱好");
      return false
   }
  
   return true
}
 </script>
</head>
  
<body bgcolor="#F4F5FF" leftmargin="0" topmargin="0" background="../../pic/back.jpg"><form name="login" action="updateOk.jsp" method=post onSubmit="return ValidateLoginForm(this)"> <table width="101" height="101" align= "top" border="0" cellpadding="0" cellspacing="0"> 
         
                    
  <table width="58%" border="0" cellpadding="4" cellspacing="1" bgcolor="#999999">
    <tr align="center" bgcolor="#f6f6f6">
                        
      <td colspan="2"><b>修改爱好</b></td>
                      </tr>
                      <tr align="center" bgcolor="#FFFFFF">
                        <td><div align="right">爱好名称</div></td>
                        <td><input type="text" name="name" size=50 value="<%=rs.getString(2) %>" ></td>
                      </tr>
                      <tr align="center" bgcolor="#FFFFFF">
                        <td><div align="right">爱好描述</div></td>
                        <td>
                            <textarea rows="20" cols="50" name="des" ><%=rs.getString(3) %></textarea>
                        </td>
                      </tr>
                       <tr align="center" bgcolor="#FFFFFF">
                         <td><div align="right">确认修改</div></td>
                        <td colspan="2"><input type="submit"  width="50" height="22" border="0" value="确认"></a></td>
                 
            
                      </tr>
                    </table>
   <input type="hidden" name="id" value="<%=rs.getString(1)%>"> 
</Form> 
</body>
</html>
