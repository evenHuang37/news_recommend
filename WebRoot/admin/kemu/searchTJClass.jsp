<%@ page contentType="text/html; charset=gb2312"%>
<%@ page language="java"%>
<%@ page import="java.sql.*"%>
<%@ page import="news.*,java.util.*"%>
<head>
	<title>推荐新闻显示</title>
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
				<font size=4>推荐新闻显示</font>
			</td>
			<td width="76%">
				<font size=4><%="最多为您推荐" + StaticConstant.UserRNUM + "条记录"%></font>
			</td>
		</tr>
	</table>

	<br>
	<center>
		<table width="100%" border="0" class="NormalTableTwo">
			<tr>
				<td class="InternalHeader">
					记录编号
				</td>
				<td class="InternalHeader">
					新闻标题
				</td>
				<td class="InternalHeader">
					新闻内容
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
					<A href="lookdes.jsp?id=<%=key%>">查看</A>
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

