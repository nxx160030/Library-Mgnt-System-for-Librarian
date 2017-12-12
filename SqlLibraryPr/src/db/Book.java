package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import com.mysql.jdbc.Statement;

public class Book {
	
	private String BOOK_TABLE = "BOOK";	
	private List<BookRecord> book_records = new ArrayList<BookRecord>();

	public boolean insert(Connection connection, String isbn, String title) {	
		
		if(title.contains("\""))
			title = title.replaceAll("\"", Matcher.quoteReplacement("\\\""));
		String INSERT = "INSERT INTO " + BOOK_TABLE + " VALUES (\"" + isbn + "\",\"" + title + "\")";
		try {
			Statement statement = (Statement) connection.createStatement();
			statement.executeUpdate(INSERT);
			return true;
		} catch (SQLException e1) {
			e1.printStackTrace();
			System.out.println(INSERT);
			return false;
		}
	}
	
	// query table BOOK using regexp
	public List<BookRecord> query(Connection connection, String key)
	{
		try {
			Statement statement;
			ResultSet rs;
			if(key.contains("\""))
				key = key.replaceAll("\"", Matcher.quoteReplacement("\\\""));

			statement = (Statement) connection.createStatement();
			String QUERY = "SELECT * FROM "+ BOOK_TABLE +" WHERE Isbn REGEXP '" + key + "' OR Title REGEXP '" + key + "'";
			rs = statement.executeQuery(QUERY);
			
			while(rs.next())
			{
				String isbn = rs.getString("Isbn");
				String title = rs.getString("Title");
				book_records.add(new BookRecord(isbn,title));
			}		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return book_records;
	}
	
	public class BookRecord {
		
		private String Isbn;
		private String Title;

		private BookRecord(String isbn, String title)
		{
			Isbn = isbn;
			Title = title;
		}
		
		public String getIsbn()
		{
			return Isbn;
		}
		
		public String getTitle()
		{
			return Title;
		}
	}
}
