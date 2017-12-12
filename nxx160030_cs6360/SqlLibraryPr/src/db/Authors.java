package db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import com.mysql.jdbc.Statement;

import java.sql.Connection;
import java.sql.ResultSet;

public class Authors {
	
	private String AUTHOR_TABLE = "AUTHORS";
	
	public boolean insert(Connection connection, int author_id, String author) {

		if(author.contains("\'"))
		{
			author = author.replaceAll("\'", Matcher.quoteReplacement("\\\'"));
		}
				
		if(author.contains("\""))
		{
			author = author.replaceAll("\"", Matcher.quoteReplacement("\\\""));
		}
		String INSERT = "INSERT INTO " + AUTHOR_TABLE + " VALUES (\"" + author_id + "\",\"" + author + "\")";
		
		try {
			Statement statement = (Statement)connection.createStatement();
			statement.executeUpdate(INSERT);
			return true;
		} catch (SQLException e1) {
			e1.printStackTrace();
			System.out.println(INSERT);
			return false;
		}
	}

	public List<AuthorsRecord> query(Connection conn, String str) {
		
		if(str.contains("\'"))
			str = str.replaceAll("\"", Matcher.quoteReplacement("\\\'"));
		if(str.contains("\""))
			str = str.replaceAll("\"", Matcher.quoteReplacement("\\\""));
		Statement statement;
		ResultSet rs;
		List<AuthorsRecord> authors_record = new ArrayList<AuthorsRecord>();
		try {
			statement = (Statement) conn.createStatement();
			String QUERY = "SELECT * FROM "+ AUTHOR_TABLE +" WHERE Author_id REGEXP '" 
										+ str + "' OR Name REGEXP '" + str +"'";
			rs = statement.executeQuery(QUERY);
			while(rs.next())
			{
				int author_id = rs.getInt("Author_id");
				String name = rs.getString("Name");
				authors_record.add(new AuthorsRecord(author_id,name));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return authors_record;
	}
	
	public String queryNameByAuthorId(Connection conn, int author_id) {
		
		Statement statement;
		ResultSet rs;
		String name = "";
		
		try {
			statement = (Statement) conn.createStatement();
			String QUERY = "SELECT Name FROM "+ AUTHOR_TABLE +" WHERE Author_id = '" 
										+ author_id +"'";
			rs = statement.executeQuery(QUERY);
			while(rs.next())
			{
				name = rs.getString("Name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return name;
	}
	
	public int queryAuthorIdByName(Connection conn, String name) {
		
		Statement statement;
		ResultSet rs;
		int author_id=0;
		
		
		if(name.contains("\'"))
		{
			name = name.replaceAll("\'", Matcher.quoteReplacement("\\\'"));
		}
		
		if(name.contains("\""))
		{
			name = name.replaceAll("\"", Matcher.quoteReplacement("\\\""));
		}
		
		try {
			statement = (Statement) conn.createStatement();
			String QUERY = "SELECT Author_id FROM "+ AUTHOR_TABLE +" WHERE Name REGEXP '" 
										+ name +"'";
			rs = statement.executeQuery(QUERY);
			while(rs.next())
			{
				author_id = rs.getInt("Author_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return author_id;
	}
	
	public int queryAuthorIdByFullName(Connection conn, String name) {
		
		Statement statement;
		ResultSet rs;
		int author_id=0;
		
		
		if(name.contains("\'"))
		{
			name = name.replaceAll("\'", Matcher.quoteReplacement("\\\'"));
		}
		
		if(name.contains("\""))
		{
			name = name.replaceAll("\"", Matcher.quoteReplacement("\\\""));
		}
		
		try {
			statement = (Statement) conn.createStatement();
			String QUERY = "SELECT Author_id FROM "+ AUTHOR_TABLE +" WHERE Name = '" 
										+ name +"'";
			rs = statement.executeQuery(QUERY);
			while(rs.next())
			{
				author_id = rs.getInt("Author_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return author_id;
	}
	
	public class AuthorsRecord {
		
		private String Name;
		private int Author_id;

		private AuthorsRecord(int author_id, String name)
		{
			Author_id = author_id;
			Name = name;
		}
		
		public int getAuthor_id()
		{
			return Author_id;
		}
		
		public String getName()
		{
			return Name;
		}
	}

}
