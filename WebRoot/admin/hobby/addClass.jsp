<%@ page contentType="text/html;charset=gb2312" %>
<%@ page language="java" pageEncoding="gb2312"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link href="../456/common/css/sunhoo.css" rel="stylesheet" type="text/css">
<script language="JavaScript">
function ValidateLoginForm(form)
{
   

   if (form.name.value == "")
   {
      alert("请输入爱好名称");
      return false
   }
  if (form.des.value == "")
   {
      alert("请输入爱好描述");
      return false
   }
   return true
}
 </script>
</head>

<body bgcolor="#F4F5FF" leftmargin="0" topmargin="0" background="../../pic/back.jpg"><form name="login" action="addOk.jsp" method=post onSubmit="return ValidateLoginForm(this)"> <table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0"> 
<tr>
    <td width="12%" height="307" valign="top"></td>
    <td width="88%" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          
      <td valign="top">&nbsp;</td>
        </tr>
        <tr>
          <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                
          <td width="19%">&nbsp;</td>
                <td width="81%" background="image/index_right_bg.gif">
                    
  <table width="58%" border="0" cellpadding="4" cellspacing="1" bgcolor="#999999">
    <tr align="center" bgcolor="#f6f6f6">
                        
      <td colspan="2"><b>添加爱好</b></td>
                      </tr>
                      <tr align="center" bgcolor="#FFFFFF">
                        <td><div align="right">爱好名称</div></td>
                        <td><input type="text" name="name" size=50></td>
                      </tr>
                      <tr align="center" bgcolor="#FFFFFF">
                        <td><div align="right">爱好描述</div></td>
                        <td>
                            <textarea rows="20" cols="50" name="des"></textarea>
                        </td>
                      </tr>
                       <tr align="center" bgcolor="#FFFFFF">
                         <td><div align="right">添加爱好</div></td>
                        <td colspan="2"><input type="submit"  width="50" height="22" border="0" value="提交"></a></td>
                 
            
                      </tr>
                    </table>
                  
  <span class="top">
  </form>

                </td>
</tr> </td> </tr> 
<tr>
          
  <td><span class="top">                  </td>
         </tr></td>
</tr> </Form> 
</body>
</html>
