package eventHandler;

import java.sql.SQLException;
import java.util.HashMap;

import com.mysql.jdbc.Connection;

import db.Borrower;
import sql.SqlConnect;

public class BtnBrrMgntCreateHandler {
	
	private HashMap<String,String> brrInfo;
	private Borrower brr = new Borrower();
	private SqlConnect sqlConn = new SqlConnect();
	private Connection conn;
	private String ssn;
	private String bname;
	private String address;
	private String phone;
	
	
	public BtnBrrMgntCreateHandler(HashMap<String,String> info)
	{
		brrInfo = info;	
		ssn = brrInfo.get("Ssn");
		bname = brrInfo.get("FName") + " " + brrInfo.get("LName");
		address = brrInfo.get("Address");
		phone = brrInfo.get("Phone");
	}
	
	public String create()
	{
		boolean success = false;
		String card_id = "";
		
		try {
			conn = sqlConn.connect();
			
			if(!brr.queryBySsn(conn, ssn).equals(""))
				return "repeatedSsn";

			success = brr.insert(conn, ssn, bname, address, phone);
			
			if(success)
			{
				card_id = brr.queryBySsn(conn, ssn);
			}
			
			conn.close();
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return card_id;
	}

}
