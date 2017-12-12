package db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;




public class Book_Loans {
	
	private String BOOK_LOANS_TABLE = "BOOK_LOANS";

	public boolean queryAvailability(Connection conn, String isbn) {

			Statement statement;		
			try {
			statement = (Statement) conn.createStatement();
			String QUERY = "SELECT Loan_id, Date_in FROM "+ BOOK_LOANS_TABLE +" WHERE Isbn REGEXP '" + isbn + "'";		
			ResultSet rs = statement.executeQuery(QUERY);
			// true is loaned and not returned, false is not loaned
			if(rs.next())
			{
				if(rs.getString(2)==null)
					return true;
				else return false;
			}
			return false;
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
	}
	
	public int queryRcrdCntByBrrId(Connection conn,String id)
	{
		Statement statement;	
		int count = 0;
		try {
		statement = (Statement) conn.createStatement();
		String QUERY = "SELECT COUNT(*) FROM "+ BOOK_LOANS_TABLE +" WHERE Card_id= '" + id + "'";		
		ResultSet rs = statement.executeQuery(QUERY);
		while(rs.next())
		{
			count = rs.getInt(1);
		}
		
		return count; 
		
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public boolean insertRrcd(Connection conn, String isbn, String brrId)
	{
		Statement statement;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDate sysDate = LocalDate.now();
		String dateOut = dtf.format(sysDate);
		String dueDate = dtf.format(sysDate.plusDays(14));
		
		try {
		statement = (Statement) conn.createStatement();
		String INSERT = "INSERT INTO "+ BOOK_LOANS_TABLE +"(Isbn,Card_id,Date_out,Due_date) VALUES('" 
				+ isbn + "','" + brrId + "','" + dateOut + "','" + dueDate + "')";		
		Boolean result = (statement.executeUpdate(INSERT)!=0);
		return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean updateRrcd(Connection conn, String isbn)
	{
		Statement statement;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDate sysDate = LocalDate.now();
		String dateIn = dtf.format(sysDate);
		
		try {
		statement = (Statement) conn.createStatement();
		String UPDATE = "UPDATE "+ BOOK_LOANS_TABLE +" SET Date_in= '" + dateIn+ "' WHERE Isbn = '" + isbn + "'";		
		Boolean result = (statement.executeUpdate(UPDATE)!=0);
		return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}


	public List<BookLoanRecord> queryIsbnById(Connection conn, String card_id) {

		Statement statement;	
		List<BookLoanRecord> list = new ArrayList<BookLoanRecord>();
		
		try {
			statement = (Statement) conn.createStatement();

			String QUERY = "SELECT Isbn FROM "+ BOOK_LOANS_TABLE +" WHERE Card_id= '" + card_id + "'";		
			ResultSet rs = statement.executeQuery(QUERY);
			while(rs.next())
			{
				String isbn = rs.getString("Isbn");
				BookLoanRecord record = new BookLoanRecord(isbn,card_id);
				list.add(record);
			}
			} catch (SQLException e) {
				e.printStackTrace();
			}	
		return list;
	}
	

	public List<BookLoanRecord> queryByIsbnId(Connection conn, String srchKey) {

		Statement statement;	
		List<BookLoanRecord> list = new ArrayList<BookLoanRecord>();
		
		try {
			statement = (Statement) conn.createStatement();

			String QUERY = "SELECT Isbn, Card_id FROM "+ BOOK_LOANS_TABLE 
					+" WHERE Card_id= '" + srchKey + "' OR Isbn='" + srchKey + "' ";		
			ResultSet rs = statement.executeQuery(QUERY);
			while(rs.next())
			{
				String isbn = rs.getString("Isbn");
				String card_id = rs.getString("Card_id");
				BookLoanRecord record = new BookLoanRecord(isbn,card_id);
				list.add(record);
			}
			} catch (SQLException e) {
				e.printStackTrace();
			}	
		return list;
	}
	
	public List<BookLoanRecord> queryNotRtnById(Connection conn, String srchKey) {
		Statement statement;	
		List<BookLoanRecord> list = new ArrayList<BookLoanRecord>();
		
		try {
			statement = (Statement) conn.createStatement();

			String QUERY = "SELECT Isbn, Card_id FROM "+ BOOK_LOANS_TABLE 
					+" WHERE Card_id= '" + srchKey + "' AND Date_in IS NULL";		
			ResultSet rs = statement.executeQuery(QUERY);
			while(rs.next())
			{
				String isbn = rs.getString("Isbn");
				String card_id = rs.getString("Card_id");
				BookLoanRecord record = new BookLoanRecord(isbn,card_id);
				list.add(record);
			}
			} catch (SQLException e) {
				e.printStackTrace();
			}	
		return list;
	}
	
	public List<BookLoanRecord> queryFinedRcds(Connection conn)
	{
		Statement statement;	
		List<BookLoanRecord> list = new ArrayList<BookLoanRecord>();
		
		LocalDate currentDate = LocalDate.now();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		
		
		try {
			statement = (Statement) conn.createStatement();

			String QUERY = "SELECT Loan_id,Card_id,Due_date,Date_in FROM "+ BOOK_LOANS_TABLE 
					+" WHERE Due_date< '" +dtf.format(currentDate)+ "' AND (Date_in>Due_date OR (Date_in IS NULL))";		
			ResultSet rs = statement.executeQuery(QUERY);
			while(rs.next())
			{
				String ln_id = rs.getString("Loan_id");
				String cd_id = rs.getString("Card_id");
				String due_dt = rs.getString("Due_date");
				String dt_in = rs.getString("Date_in");
				BookLoanRecord record = new BookLoanRecord(ln_id,cd_id,due_dt,dt_in);
				list.add(record);
			}
			} catch (SQLException e) {
				e.printStackTrace();
			}	
		return list;
	}
	

	public List<String> queryByCardId(Connection conn, String card_id) {
		
		List<String> ln_idList = new ArrayList<String>();
		Statement statement;
		try {
			statement = (Statement) conn.createStatement();

			String QUERY = "SELECT Loan_id FROM "+ BOOK_LOANS_TABLE 
					+" WHERE Card_id='" + card_id + "'";		
			ResultSet rs = statement.executeQuery(QUERY);
			while(rs.next())
			{
				ln_idList.add(rs.getString("Loan_id"));			
			}
			} catch (SQLException e) {
				e.printStackTrace();
			}			
		return ln_idList;			
	}
	
	
	
	public List<String> queryRtnedByCardId(Connection conn, String card_id) {
		
		List<String> ln_idList = new ArrayList<String>();
		Statement statement;
		try {
			statement = (Statement) conn.createStatement();

			String QUERY = "SELECT Loan_id FROM "+ BOOK_LOANS_TABLE 
					+" WHERE Card_id='" + card_id + "' AND Date_in IS NOT NULL";		
			ResultSet rs = statement.executeQuery(QUERY);
			while(rs.next())
			{
				ln_idList.add(rs.getString("Loan_id"));			
			}
			} catch (SQLException e) {
				e.printStackTrace();
			}			
		return ln_idList;			
	}
	
	public class BookLoanRecord{
		
		private String Isbn;
		private String Card_id;
		private String loan_id;
		private String due_date;
		private String date_in;
		
		public BookLoanRecord(String isbn, String card_id)
		{
			Isbn = isbn;
			Card_id = card_id;
		}
		
		public BookLoanRecord(String ln_id, String cd_id, String due_dt, String dt_in) {
			
			loan_id = ln_id;
			Card_id = cd_id;
			due_date = due_dt;
			date_in = dt_in;			
		}

		public String getIsbn()
		{
			return Isbn;
		}
		
		public String getCardId()
		{
			return Card_id;
		}
		
		public String getLoanId()
		{
			return loan_id;
		}
		
		public String getDueDate()
		{
			return due_date;
		}
		
		public String getDateIn()
		{
			return date_in;
		}
		
	}

}
