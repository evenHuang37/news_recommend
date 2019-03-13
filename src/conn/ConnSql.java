package conn;

import java.sql.*;

public class ConnSql {

	private Connection conn;
	private Statement stmt;

	// 2000
	// private String url =
	// "jdbc:microsoft:sqlserver://127.0.0.1:1433;DatabaseName=wyw";
	// private String classforname =
	// "com.microsoft.jdbc.sqlserver.SQLServerDriver";
	// 2005
	// private String url =
	// "jdbc:sqlserver://WANGZFF-PC\\SQL2005:49159;DatabaseName=webycdySys";
	// private String classforname =
	// "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	// 2005
	// private String url =
	// "jdbc:sqlserver://WANGZFF-PC\\SQL2005:49159;DatabaseName=db_tagSchool";
	// private String classforname =
	// "com.microsoft.sqlserver.jdbc.SQLServerDriver";

	private String url = "jdbc:sqlserver://2013-20141118YV\\SQLEXPRESS:1433;DatabaseName=asys";
	private String classforname = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private String uid = "sa";
	private String pwd = "123456";
	private ResultSet rs;

	public ConnSql() {
	}

	/**
	 * @return Connection
	 * @exception ClassNotFoundException
	 *                , SQLException
	 */
	public Connection getConnection() {
		try {
			Class.forName(classforname);
			if (conn == null || conn.isClosed())
				conn = DriverManager.getConnection(url, uid, pwd);
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}

		catch (SQLException ex) {
			ex.printStackTrace();
		}

		return conn;
	}

	public Statement getStatement() {
		conn = getConnection();
		try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_READ_ONLY);

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Query Exception");
		}
		return stmt;
	}

	public ResultSet executeQuery(String sql) {
		conn = getConnection();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Query Exception");
		}
		return rs;
	}

	public boolean executeUpdata(String sql) {
		conn = getConnection();
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}