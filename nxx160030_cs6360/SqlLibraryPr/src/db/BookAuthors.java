package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;

public class BookAuthors {

	private String BOOK_AUTHORS_TABLE = "BOOK_AUTHORS";

	public boolean insert(Connection connection, String isbn, int author_id) throws SQLException {

		// insert into table BOOK_AUTHORS
		String INSERT = "INSERT INTO "+ BOOK_AUTHORS_TABLE 
					+ " VALUES(\"" + author_id + "\",\"" + isbn + "\")";
		Statement statement = (Statement)connection.createStatement();
		int success = statement.executeUpdate(INSERT);
		
		return success>0;
		
	}

	public List<BookAuthorsRecord> query(Connection conn, String str) {

		Statement statement;
		ResultSet rs;
		List<BookAuthorsRecord> book_authors = new ArrayList<BookAuthorsRecord>();
		try {
			statement = (Statement) conn.createStatement();
			String QUERY = "SELECT * FROM "+ BOOK_AUTHORS_TABLE +" WHERE Isbn = '" + str + "' OR Author_id = '" + str +"'";
			rs = statement.executeQuery(QUERY);
			
			while(rs.next())
			{
				int author_id = rs.getInt("Author_id");
				String isbn = rs.getString("Isbn");
				BookAuthorsRecord record = new BookAuthorsRecord(isbn,author_id);
				book_authors.add(record);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return book_authors;
	}

	public List<BookAuthorsRecord> queryIsbnByAuthorId(Connection conn, int authorId) {

		Statement statement;
		ResultSet rs;
		List<BookAuthorsRecord> book_authors = new ArrayList<BookAuthorsRecord>();
		try {
			statement = (Statement) conn.createStatement();
			String QUERY = "SELECT * FROM "+ BOOK_AUTHORS_TABLE +" WHERE " + "Author_id = '" + authorId+"'";
			rs = statement.executeQuery(QUERY);
			
			while(rs.next())
			{
				int author_id = rs.getInt("Author_id");
				String isbn = rs.getString("Isbn");
				book_authors.add(new BookAuthorsRecord(isbn,author_id));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return book_authors;
	}
	
	
	public class BookAuthorsRecord {
		
		private String Isbn;
		private int Author_id;

		private BookAuthorsRecord(String isbn, int author_id)
		{
			Isbn = isbn;
			Author_id = author_id;
		}
		
		public String getIsbn()
		{
			return Isbn;
		}
		
		public int getAuthor_id()
		{
			return Author_id;
		}
	}
}
