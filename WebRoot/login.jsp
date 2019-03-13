<%@ page session="true"%>
<%@page contentType="text/html; charset=gb2312" language="java"
	import="java.sql.*,conn.ConnSql" errorPage=""%>

<html>
	<head>
		<title>Book store</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	</head>
	<body bgcolor="#F4F5FF">
		<font face="Times New Roman,Time" size="+3"> </font>
		<hr>
		<p>
			<center>
				<%
					String name = request.getParameter("name").trim();
					String pwd = request.getParameter("password").trim();
					

					//response.getWriter().write("<script>alert('"+name+"////"+pwd+"');</script>");
					session.removeAttribute("frist");

					ConnSql cs = new ConnSql();
					Statement statement = cs.getStatement();
					String query = "select type ,nameReal,customer_id,address,type from customer_info where Name='"
							+ name + "' and Passwd='" + pwd + "'";

					ResultSet res = statement.executeQuery(query);
					String type = "";
					if (res.next()) {
						//session.putValue("loginSign","OK");
						//session.putValue("cID",customerid);
						session.setAttribute("name", name);
						session.setAttribute("nameReal", res.getString(2));
						session.setAttribute("yhid", res.getString(3));
						session.setAttribute("type", res.getString(4));
						Integer temp1 = new Integer(res.getInt(1));
						type = temp1.toString().trim();
						;
						res.close();

						if (name.equals("admin")) {
							response.sendRedirect("./admin/index.jsp");
						} else {
							response.sendRedirect("./user/index.jsp");
						}
					} else {
						res.close();
						response
								.getWriter()
								.write(
										"<script>alert('用户名或密码不正确，请重新输入！');window.location.href='index.html';</script>");

					}
				%>
			</center>
	</body>
</html>
