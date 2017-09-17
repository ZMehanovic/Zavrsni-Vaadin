package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbManager {

	private static final String CONNECTION_URL = "jdbc:mysql://localhost/mydb?user=root&password=1591530123&verifyServerCertificate=false&useSSL=true";

	private DbManager() {
	}

	private static class SingletonHelper {
		private static final DbManager INSTANCE = new DbManager();
	}

	public static DbManager getInstance() {
		return SingletonHelper.INSTANCE;
	}

	private Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = DriverManager.getConnection(CONNECTION_URL);
		return connection;
	}

	public ResultSet executeQuery(String sql) {
		ResultSet result = null;
		try {
			result = getConnection().createStatement().executeQuery(sql);

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		return result;
	}
	public void executeUpdateQuery(String sql) {
		try {
			getConnection().createStatement().executeUpdate(sql);

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

	}
}
