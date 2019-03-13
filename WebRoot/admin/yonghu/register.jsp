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
      if (form.password.value == "")
   {
      alert("请输入密码");
      return false
   }
   else if((form.password.value.length<3)||(form.password.value.length>8)){
	alert("密码必须是3-8位字母或数字");
	return false;
}
      if (form.re_password.value == "")
   {
      alert("请输入密码确认");
      return false
   }
   else if(form.password.value!=form.re_password.value){
	
	alert("两次密码不同!");
	return false;
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

<body bgcolor="#F4F5FF" leftmargin="0" topmargin="0" background="../../pic/back.jpg" ><form name="login" action="register_confirm.jsp" method=post onSubmit="return ValidateLoginForm(this)"> <table width="101" height="101" align= "top" border="0" cellpadding="0" cellspacing="0"> 

                    
  <table width="38%" border="0" cellpadding="4" cellspacing="1" bgcolor="#999999">
    <tr align="center" bgcolor="#f6f6f6">
                        
      <td colspan="2"><b>用户注册</b></td>
                      </tr>
                      <tr align="center" bgcolor="#FFFFFF">
                        <td><div align="right">用户名</div></td>
                        <td><input type="text" name="name"></td>
                      </tr>
                      <tr align="center" bgcolor="#FFFFFF">
                        <td><div align="right">真实姓名</div></td>
                        <td><input type="text" name="realname"></td>
                      </tr>
                      <tr align="center" bgcolor="#FFFFFF">
                        
      <td width="36%"><div align="right">密码：</div></td>
                        <td width="64%"><input name="password" type="password" ></td>
                      </tr>
                      <tr align="center" bgcolor="#FFFFFF">
                        <td><div align="right">密码确认：</div></td>
                        <td><input name="re_password" type="password"  value=""></td>
                      </tr>
					  
    <tr bgcolor="#FFFFFF"> 
      <td height="25"> 
        <div align="right">电 
      话：</div></td>
                                
      <td height="25"> 
        <div align="center"> 
                                    <input type="text" name="telephone"  >
        </div></td>
    </tr>
                              
    <tr bgcolor="#FFFFFF"> 
      <td height="25">        <div align="right">E_mail ：</div></td>
                                
      <td height="25"> 
        <div align="center"> 
                                    <input type="text" name="e_mail"  >
        </div></td>
    </tr>
                              
    <tr bgcolor="#FFFFFF"> 
      <td height="25"> 
        <div align="right">爱好：</div></td>
                                
      <td height="25"> 
        <div align="center"> 
                                    
                                    <select name="address" >
                        <%ConnSql cs=new ConnSql();
                          String select="select id,name from hobby ";
                          ResultSet rs=cs.executeQuery(select);
                          while(rs.next())
                          {
                          String str=rs.getString(1);
                          String str1=rs.getString(2);
                        %> 
                          <option value="<%=str%>"><%=str1%></option>
                        <% 
                           }
                        %>               
                       </select>
        </div></td>
    </tr>
       <tr bgcolor="#FFFFFF" style="display:none"> 
      <td height="25"> 
        <div align="right">添加用户类型：</div></td>
                                
      <td height="25"> 
        <div align="center" > 
                          <select name="class">
                           <option value="0">用户</option>
                           <option value="3">管理员</option>
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
