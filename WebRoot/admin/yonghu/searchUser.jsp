<%@ page contentType="text/html; charset=gb2312" %>
<%@ page language="java" %>
<%@ page import="java.sql.*" %>
<%@ page import="conn.ConnSql" %>
<% 
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              
ConnSql cs=new ConnSql();
Statement statement = cs.getStatement();

//每页显示记录数
int PageSize = 8;
int StartRow = 0; //开始显示记录的编号 
int PageNo=0;//需要显示的页数
int CounterStart=0;//每页页码的初始值
int CounterEnd=0;//显示页码的最大值
int RecordCount=0;//总记录数;
int MaxPage=0;//总页数
int PrevStart=0;//前一页
int NextPage=0;//下一页
int LastRec=0; 
int LastStartRecord=0;//最后一页开始显示记录的编号 

//获取需要显示的页数，由用户提交
if(request.getParameter("PageNo")==null){ //如果为空，则表示第1页
  if(StartRow == 0){
     PageNo = StartRow + 1; //设定为1
  }
}else{
  PageNo = Integer.parseInt(request.getParameter("PageNo")); //获得用户提交的页数
  StartRow = (PageNo - 1) * PageSize; //获得开始显示的记录编号
}

//因为显示页码的数量是动态变化的，假如总共有一百页，则不可能同时显示100个链接。而是根据当前的页数显示
//一定数量的页面链接

//设置显示页码的初始值!!
  if(PageNo % PageSize == 0){
   CounterStart = PageNo - (PageSize - 1);
  }else{
   CounterStart = PageNo - (PageNo % PageSize) + 1;
  }

CounterEnd = CounterStart + (PageSize - 1);
%>

<html>
<head>
<title>搜索用户</title>
<link rel="stylesheet" href="../style.css" type="text/css">
</head>
<%

String name="%";
if(request.getParameter("name")!=null){ //如果为空，则表示第1页
 name=new String(request.getParameter("name").getBytes("ISO-8859-1"),"gb2312") ;
  }

//获取总记录数
ResultSet rs = statement.executeQuery("select count(*) from customer_info where Name like '%"+name+"%'" ); 
rs.next(); 
RecordCount = rs.getInt(1); 



rs = statement.executeQuery("select Customer_ID from customer_info order by  Customer_ID  desc " ); 
rs.next(); 
int aaa = StartRow;
int idNumber=0;
while (rs.next()&&aaa>0) {
  idNumber = rs.getInt(1); 
  aaa--;
}
if(idNumber==0) idNumber=100000000;
rs = statement.executeQuery("select top 10 Customer_ID,customer_info.Name,Passwd,Telephone,Email, hobby.name,type,namereal from customer_info,hobby where customer_info.address=hobby.id and customer_info.Name like '%"+name+"%' and Customer_ID <="+idNumber +" order by  Customer_ID  desc ");

//获取总页数
MaxPage = RecordCount % PageSize;
if(RecordCount % PageSize == 0){
  MaxPage = RecordCount / PageSize;
}else{
   MaxPage = RecordCount/PageSize+1;
}
%>
<body  background="../../pic/back.jpg"> 
<center>
<form method="get" action="searchUser.jsp" name="divPage" >
<table width="100%" border="0" class="InternalHeader">
 <tr>
   <td width="24%"><font size=4>搜索用户名称：</font></td>
    <td width="36%">
       <font size=4><input type="text" name="name"></font>
    </td>
     <td width="36%">
       <font size=4><input type="submit" value="搜索用户" name="button1"></font>
    </td>
 </tr>
</table>
</form>
</center>
<table width="100%" border="0" class="InternalHeader">
 <tr>
   <td width="24%"><font size=4>搜索记录显示</font></td>
    <td width="76%">
       <font size=4><%="总共"+RecordCount+"条记录 - 当前页："+PageNo+"/"+MaxPage %></font>
    </td>
 </tr>
</table>

<br>
<center>
<table width="100%" border="0" class="NormalTableTwo">
  <tr> 
  <td class="InternalHeader">记录编号</td>
    <td class="InternalHeader">用户ID</td>
    <td class="InternalHeader">姓名</td>
    <td class="InternalHeader" >密码</td>
    <td class="InternalHeader" >电话</td>
    <td class="InternalHeader" >邮箱</td>
    <td class="InternalHeader" >爱好</td>
     <td class="InternalHeader" >用户类型</td>
  </tr>


<%
int i = 1,row;
while (rs.next()) {
  int bil = i ;
  String leixing=rs.getString(7);
%>
 <tr> 
    <td class="NormalFieldTwo" ><%=bil %></td>
    <td class="NormalFieldTwo" ><%=rs.getString(2)%></td>
     <td class="NormalFieldTwo" ><%=rs.getString(8)%></td>
    <td class="NormalFieldTwo" ><%=rs.getString(3)%></td>
    <td class="NormalFieldTwo" ><%=rs.getString(4)%></td>
    <td class="NormalFieldTwo" ><%=rs.getString(5)%></td>
    <td class="NormalFieldTwo" ><%=rs.getString(6)%></td>
    <%if(leixing!=null&&leixing.contains("1")){%>
    <td class="NormalFieldTwo" >管理员</td>
    <%} %>
    <%if(leixing!=null&&leixing.contains("0")){%>
    <td class="NormalFieldTwo" >用户</td>
    <%} %>
 </tr>
<%
  i++;
}%>
</table>
</center>
<br>
<table width="100%" border="0" class="InternalHeader">
  <tr>
   <td><div align="center">
<%
   out.print("<font size=4>");
  //显示第一页或者前一页的链接
  //如果当前页不是第1页，则显示第一页和前一页的链接
  if(PageNo != 1){
    PrevStart = PageNo - 1;
    //out.print("<a href=TestPage.jsp?PageNo=1>第一页 </a>: ");
    //out.print("<a href=TestPage.jsp?PageNo="+PrevStart+">前一页</a>");
  }
  out.print("[");

   //打印需要显示的页码
   for(int c=CounterStart;c<=CounterEnd;c++){
   if(c <MaxPage){
     if(c == PageNo){
       if(c %PageSize == 0){
         out.print(c);
       }else{
          out.print(c+" ,");
       }
     }else if(c % PageSize == 0){
        out.print("<a href=TestPage.jsp?PageNo="+c+">"+c+"</a>");
     }else{
        out.print("<a href=searchUser.jsp?PageNo="+c+">"+c+"</a> ,");
     }
   }else{
     if(PageNo == MaxPage){
      out.print(c);
      break;
     }else{
        out.print("<a href=searchUser.jsp?PageNo="+c+">"+c+"</a>");
     break;
   }
  }
}

out.print("]");;

if(PageNo < MaxPage){ //如果当前页不是最后一页，则显示下一页链接
    NextPage = PageNo + 1;
    //out.print("<a href=searchUser.jsp?PageNo="+NextPage+">下一页</a>");
}

//同时如果当前页不是最后一页，要显示最后一页的链接
if(PageNo < MaxPage){
   LastRec = RecordCount % PageSize;
   if(LastRec == 0){
      LastStartRecord = RecordCount - PageSize;
   }
   else{
      LastStartRecord = RecordCount - LastRec;
   }

   //out.print(":");
    //out.print("<a href=searchUser.jsp?PageNo="+MaxPage+">最后一页</a>");
  }
  out.print("</font>");
%>
</div>
</td>
</tr>
</table>
<%
 
  statement.close();

%>
</body>
</html>

 