<%@ page contentType="text/html; charset=gb2312"%>
<%@ page language="java"%>
<%@ page import="java.sql.*"%>
<%@ page import="news.*,java.util.*"%>
<head>
	<title>�Ƽ�������ʾ</title>
	<link rel="stylesheet" href="../style.css" type="text/css">
</head>
<%
	if (session.getAttribute("frist") != null) {
		response.sendRedirect("searchXQClass.jsp");
	}
%>
<body background="../../pic/back.jpg">
	<center>
	</center>
	<table width="100%" border="0" class="InternalHeader">
		<tr>
			<td width="24%">
				<font size=4>�Ƽ�������ʾ</font>
			</td>
			<td width="76%">
				<font size=4><%="���Ϊ���Ƽ�" + StaticConstant.UserRNUM + "����¼"%></font>
			</td>
		</tr>
	</table>

	<br>
	<center>
		<table width="100%" border="0" class="NormalTableTwo">
			<tr>
				<td class="InternalHeader">
					��¼���
				</td>
				<td class="InternalHeader">
					���ű���
				</td>
				<td class="InternalHeader">
					��������
				</td>
			</tr>

			<%
				String userid = session.getAttribute("yhid").toString();
				Map<String, String> remap = TasteUserAlgorithm
						.getRecommendNews(userid);
				Iterator it = remap.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry entry = (Map.Entry) it.next();
					Object key = entry.getKey();
					Object value = entry.getValue();
			%>
			<tr>
				<td class="NormalFieldTwo"><%=key%></td>
				<td class="NormalFieldTwo"><%=value%></td>
				<td class="NormalFieldTwo">
					<A href="lookdes.jsp?id=<%=key%>">�鿴</A>
				</td>
			</tr>
			<%
				}
			%>
		</table>
	</center>
	<br>
</body>
</html>

