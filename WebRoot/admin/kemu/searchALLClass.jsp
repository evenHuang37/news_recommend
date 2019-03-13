<%@ page contentType="text/html; charset=gb2312" %>
<%@ page language="java" %>
<%@ page import="java.sql.*" %>
<%@ page import="news.*,java.util.*" %>
<head>
<title>推荐新闻显示</title>
<link rel="stylesheet" href="../style.css" type="text/css">
</head>
<%
List<NewsBean> remap =DomUtils.findALL();
%>
<body  background="../../pic/back.jpg"> 
<center>
</center>
<table width="100%" border="0" class="InternalHeader">
 <tr>
   <td width="24%"><font size=4>全部新闻显示</font></td>
    <td width="76%">
       <font size=4><%="总共"+remap.size()+"条记录"%></font>
    </td>
 </tr>
</table>

<br>
<center>
<table width="100%" border="0" class="NormalTableTwo">
  <tr> 
      <td class="InternalHeader">记录编号</td>   
      <td class="InternalHeader" >新闻标题</td>
      <td class="InternalHeader" >新闻内容</td>
  </tr>

<%

for (NewsBean newsbean: remap) { 
%>
 <tr> 
    <td class="NormalFieldTwo" ><%=newsbean.getInNewId()%></td>  
    <td class="NormalFieldTwo" ><%=newsbean.getInNewTitle()%></td>
    <td class="NormalFieldTwo" ><A href="lookdes2.jsp?id=<%=newsbean.getInNewId()%>">查看</A></td>
 </tr>
<%
}%>
</table>
</center>
<br>
</body>
</html>

 