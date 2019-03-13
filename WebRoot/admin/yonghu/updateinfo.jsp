<%@page contentType="text/html; charset=gb2312" language="java" import="java.sql.*,conn.ConnSql" errorPage="" %>
<html>
<head>

<title>新闻推荐系统</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="../style.css" type="text/css">
<script language="JavaScript">
function ValidateLoginForm(form)
{
   

   if (form.name.value == "")
   {
      alert("请输入用户ID");
      return false
   }
   if (form.telephone.value == "")
   {
      alert("请输入电话");
      return false
   }
   if (form.e_mail.value == "")
   {
      alert("请输入e_mail");
      return false
   }
      if (form.address.value == "")
   {
      alert("请输入爱好");
      return false
   }
   return true
}
 </script>
</head>

<%
ConnSql cs=new ConnSql();
if(session.getAttribute("name")!=null&&!session.getAttribute("name").toString().equals(""))                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 
{
String name=session.getAttribute("name").toString();
String realname= request.getParameter("realname");
String telephone= request.getParameter("telephone");
String e_mail= request.getParameter("e_mail");
String address= request.getParameter("address");
if(realname!=null&&!realname.equals(""))
{
	String update="update customer_info set nameReal='"+realname+"' where Name='"+name+"'";
	cs.executeUpdata(update);
}
if(telephone!=null&&!telephone.equals(""))
{
	String update="update customer_info set Telephone='"+telephone+"' where Name='"+name+"'";
	cs.executeUpdata(update);
}
if(e_mail!=null&&!e_mail.equals(""))
{
	String update="update customer_info set Email='"+e_mail+"' where Name='"+name+"'";
	cs.executeUpdata(update);
}
if(address!=null&&!address.equals(""))
{
	String update="update customer_info set Address='"+address+"' where Name='"+name+"'";
	cs.executeUpdata(update);
	response.getWriter().write("<script>alert('个人信息更新成功');</script>");
}

}
else
{
response.getWriter().write("<script>alert('密码更新失败');</script>");
}
%>
<body bgcolor="#F4F5FF" leftmargin="0" topmargin="0" background="../../pic/back.jpg" ><form name="login" action="updateinfo.jsp" method=post onSubmit="return ValidateLoginForm(this)"> <table width="101" height="101" align= "top" border="0" cellpadding="0" cellspacing="0"> 

                    
  <table width="38%" border="0" cellpadding="4" cellspacing="1" bgcolor="#999999">
    <tr align="center" bgcolor="#f6f6f6">
                        
      <td colspan="2"><b>修改个人信息</b></td>
                      </tr>
                      
                      <tr align="center" bgcolor="#FFFFFF">
                        <td><div align="right">真实姓名</div></td>
                        <td><input type="text" name="realname" ></td>
                      </tr>
                      
					  
    <tr bgcolor="#FFFFFF"> 
      <td height="25"> 
        <div align="right">电 
      话：</div></td>
                                
      <td height="25"> 
        <div align="center"> 
                                    <input type="text" name="telephone" >
        </div></td>
    </tr>
                              
    <tr bgcolor="#FFFFFF"> 
      <td height="25">        <div align="right">E_mail ：</div></td>
                                
      <td height="25"> 
        <div align="center"> 
                                    <input type="text" name="e_mail" "  >
        </div></td>
    </tr>
                              
    <tr bgcolor="#FFFFFF"> 
      <td height="25"> 
        <div align="right">爱好：</div></td>
                                
      <td height="25"> 
        <div align="center"> 
                                    
                                    <select name="address"  >
                        <%
                          String select2="select id,name from hobby ";
                          ResultSet rs2=cs.executeQuery(select2);
                          while(rs2.next())
                          {
                          String str=rs2.getString(1);
                          String str1=rs2.getString(2);
                        %> 
                          <option value="<%=str%>"><%=str1%></option>
                        <% 
                           }
                        %>               
                       </select>
        </div></td>
    </tr>
       
                      <tr align="center" bgcolor="#FFFFFF">
                        <td colspan="2"><input type="submit" value="确定提交" width="50" height="22" border="0" ></a></td>
                      </tr>
                    </table>
                 
          
 </Form> 
</body>
</html>
