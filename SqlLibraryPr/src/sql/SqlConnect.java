package sql;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;

public class SqlConnect {
	
	private Connection connection;

	public Connection connect() throws SQLException
	{
		String URL = "jdbc:mysql://127.0.0.1:3306/Library";
		String USER_ID = "root";
		String PWD = "";
		connection = null;
		
		// the sql server driver string
	    try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = (Connection) DriverManager.getConnection(URL, USER_ID, PWD);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			connection.close();
		} catch (SQLException e){
	        e.printStackTrace();
	        connection.close();
	    }
		return connection;
	}
}
