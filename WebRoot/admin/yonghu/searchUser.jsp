<%@ page contentType="text/html; charset=gb2312" %>
<%@ page language="java" %>
<%@ page import="java.sql.*" %>
<%@ page import="conn.ConnSql" %>
<% 
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              
ConnSql cs=new ConnSql();
Statement statement = cs.getStatement();

//ÿҳ��ʾ��¼��
int PageSize = 8;
int StartRow = 0; //��ʼ��ʾ��¼�ı�� 
int PageNo=0;//��Ҫ��ʾ��ҳ��
int CounterStart=0;//ÿҳҳ��ĳ�ʼֵ
int CounterEnd=0;//��ʾҳ������ֵ
int RecordCount=0;//�ܼ�¼��;
int MaxPage=0;//��ҳ��
int PrevStart=0;//ǰһҳ
int NextPage=0;//��һҳ
int LastRec=0; 
int LastStartRecord=0;//���һҳ��ʼ��ʾ��¼�ı�� 

//��ȡ��Ҫ��ʾ��ҳ�������û��ύ
if(request.getParameter("PageNo")==null){ //���Ϊ�գ����ʾ��1ҳ
  if(StartRow == 0){
     PageNo = StartRow + 1; //�趨Ϊ1
  }
}else{
  PageNo = Integer.parseInt(request.getParameter("PageNo")); //����û��ύ��ҳ��
  StartRow = (PageNo - 1) * PageSize; //��ÿ�ʼ��ʾ�ļ�¼���
}

//��Ϊ��ʾҳ��������Ƕ�̬�仯�ģ������ܹ���һ��ҳ���򲻿���ͬʱ��ʾ100�����ӡ����Ǹ��ݵ�ǰ��ҳ����ʾ
//һ��������ҳ������

//������ʾҳ��ĳ�ʼֵ!!
  if(PageNo % PageSize == 0){
   CounterStart = PageNo - (PageSize - 1);
  }else{
   CounterStart = PageNo - (PageNo % PageSize) + 1;
  }

CounterEnd = CounterStart + (PageSize - 1);
%>

<html>
<head>
<title>�����û�</title>
<link rel="stylesheet" href="../style.css" type="text/css">
</head>
<%

String name="%";
if(request.getParameter("name")!=null){ //���Ϊ�գ����ʾ��1ҳ
 name=new String(request.getParameter("name").getBytes("ISO-8859-1"),"gb2312") ;
  }

//��ȡ�ܼ�¼��
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

//��ȡ��ҳ��
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
   <td width="24%"><font size=4>�����û����ƣ�</font></td>
    <td width="36%">
       <font size=4><input type="text" name="name"></font>
    </td>
     <td width="36%">
       <font size=4><input type="submit" value="�����û�" name="button1"></font>
    </td>
 </tr>
</table>
</form>
</center>
<table width="100%" border="0" class="InternalHeader">
 <tr>
   <td width="24%"><font size=4>������¼��ʾ</font></td>
    <td width="76%">
       <font size=4><%="�ܹ�"+RecordCount+"����¼ - ��ǰҳ��"+PageNo+"/"+MaxPage %></font>
    </td>
 </tr>
</table>

<br>
<center>
<table width="100%" border="0" class="NormalTableTwo">
  <tr> 
  <td class="InternalHeader">��¼���</td>
    <td class="InternalHeader">�û�ID</td>
    <td class="InternalHeader">����</td>
    <td class="InternalHeader" >����</td>
    <td class="InternalHeader" >�绰</td>
    <td class="InternalHeader" >����</td>
    <td class="InternalHeader" >����</td>
     <td class="InternalHeader" >�û�����</td>
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
    <td class="NormalFieldTwo" >����Ա</td>
    <%} %>
    <%if(leixing!=null&&leixing.contains("0")){%>
    <td class="NormalFieldTwo" >�û�</td>
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
  //��ʾ��һҳ����ǰһҳ������
  //�����ǰҳ���ǵ�1ҳ������ʾ��һҳ��ǰһҳ������
  if(PageNo != 1){
    PrevStart = PageNo - 1;
    //out.print("<a href=TestPage.jsp?PageNo=1>��һҳ </a>: ");
    //out.print("<a href=TestPage.jsp?PageNo="+PrevStart+">ǰһҳ</a>");
  }
  out.print("[");

   //��ӡ��Ҫ��ʾ��ҳ��
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

if(PageNo < MaxPage){ //�����ǰҳ�������һҳ������ʾ��һҳ����
    NextPage = PageNo + 1;
    //out.print("<a href=searchUser.jsp?PageNo="+NextPage+">��һҳ</a>");
}

//ͬʱ�����ǰҳ�������һҳ��Ҫ��ʾ���һҳ������
if(PageNo < MaxPage){
   LastRec = RecordCount % PageSize;
   if(LastRec == 0){
      LastStartRecord = RecordCount - PageSize;
   }
   else{
      LastStartRecord = RecordCount - LastRec;
   }

   //out.print(":");
    //out.print("<a href=searchUser.jsp?PageNo="+MaxPage+">���һҳ</a>");
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

 