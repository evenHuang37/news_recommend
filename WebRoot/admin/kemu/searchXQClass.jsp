<%@ page contentType="text/html; charset=gb2312"%>
<%@ page language="java"%>
<%@ page import="java.sql.*"%>
<%@ page import="conn.*,news.*,java.util.*"%>
<head>
	<title>�Ƽ�������ʾ</title>
	<link rel="stylesheet" href="../style.css" type="text/css">
</head>
<%
	
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
				<font size=4><%="�ܹ�" + StaticConstant.UserRNUM + "����¼"%></font>
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
				String hobby = session.getAttribute("type").toString();
				ConnSql cs = new ConnSql();
				String select = "select id,name from hobby where id=" + hobby;
				ResultSet rs = cs.executeQuery(select);
				String str1 = null;
				while (rs.next()) {
					str1 = rs.getString(2);
				}
				Map<String, String> remap = TasteContAlgorithm.getRecommendNews(
						userid, str1);

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

