package db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import com.mysql.jdbc.Statement;

import java.sql.Connection;
import java.sql.ResultSet;

public class Borrower {
	
	private String BORROWER_TABLE = "BORROWER";
	private List<BorrowerRecord> brrRcrdList = new ArrayList<BorrowerRecord>();

	public boolean insert(Connection connection, String card_id, String ssn, String bname, String address, String phone) {
		
		if(bname.contains("\""))
		{
			bname = bname.replaceAll("\"", Matcher.quoteReplacement("\\\""));
		}
		
		if(address.contains("\""))
		{
			address = address.replaceAll("\"", Matcher.quoteReplacement("\\\""));
		}

		String INSERT = "INSERT INTO " + BORROWER_TABLE + " VALUES (\"" + card_id + "\",\"" + ssn 
				+ "\",\"" + bname + "\",\"" + address + "\",\"" + phone + "\")";
		try{
		Statement statement = (Statement) connection.createStatement();		
		statement.executeUpdate(INSERT);
		return true;
		} catch (SQLException e1) {
			e1.printStackTrace();
			System.out.println(INSERT);
			return false;
		}
	}
	


	public List<BorrowerRecord> queryByIdName(Connection connection,String srchKey) {
		
		if(srchKey.contains("\""))
		{
			srchKey = srchKey.replaceAll("\"", Matcher.quoteReplacement("\\\""));
		}

		String QUERY = "SELECT Card_id,Bname FROM " + BORROWER_TABLE + " WHERE Card_id='" 
					+ srchKey + "' OR Bname REGEXP '" + srchKey + "'";
		try{
			Statement statement = (Statement) connection.createStatement();		
			ResultSet rs = statement.executeQuery(QUERY);
		
			while(rs.next())
			{
				String card_id = rs.getString("Card_id");
				String bname = rs.getString("Bname");
				BorrowerRecord record = new BorrowerRecord(card_id,bname);
				brrRcrdList.add(record);
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
			System.out.println(QUERY);
		}
		return brrRcrdList;
	}
	
	
	public class BorrowerRecord{
		String Card_id;
		String Bname;
		
		private BorrowerRecord(String card_id, String bname)
		{
			Card_id = card_id;
			Bname = bname;
		}
		
		public String getCard_id()
		{
			return Card_id;
		}
		
		public String getBname()
		{
			return Bname;
		}
	}


	public boolean insert(com.mysql.jdbc.Connection conn, String ssn, String bname, String address, String phone) {
		
		if(bname.contains("\""))
		{
			bname = bname.replaceAll("\"", Matcher.quoteReplacement("\\\""));
		}
		
		if(address.contains("\""))
		{
			address = address.replaceAll("\"", Matcher.quoteReplacement("\\\""));
		}

		String INSERT = "INSERT INTO " + BORROWER_TABLE + "(Ssn,Bname,Address,Phone) VALUES (\""+ ssn 
				+ "\",\"" + bname + "\",\"" + address + "\",\"" + phone + "\")";
		try{
		Statement statement = (Statement) conn.createStatement();		
		statement.executeUpdate(INSERT);
		return true;
		} catch (SQLException e1) {
			e1.printStackTrace();
			System.out.println(INSERT);
			return false;
		}
	}



	public String queryBySsn(com.mysql.jdbc.Connection conn, String ssn) {
		
		String QUERY = "SELECT Card_id FROM " + BORROWER_TABLE + " WHERE Ssn='" 
				+ ssn + "'";
		String card_id="";
	try{
		Statement statement = (Statement) conn.createStatement();		
		ResultSet rs = statement.executeQuery(QUERY);
	
		while(rs.next())
		{
			card_id = rs.getString("Card_id");
		}

	} catch (SQLException e1) {
		e1.printStackTrace();
		System.out.println(QUERY);
	}
	return card_id;
	}

}
