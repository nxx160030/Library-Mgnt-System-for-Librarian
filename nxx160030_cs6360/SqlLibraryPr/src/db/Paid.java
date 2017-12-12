package db;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class Paid {

	private String PAID_TABLE = "PAID";

	public double queryAmtByCardId(Connection conn, String card_id) {

		String QUERY = "SELECT Paid_amt FROM "+ PAID_TABLE + " WHERE Card_id =" + card_id;
		double paid_amt = 0;
		try {
			Statement statement = (Statement) conn.createStatement();
			ResultSet rs = statement.executeQuery(QUERY);
			while(rs.next())
			{
				paid_amt = Double.parseDouble(rs.getString("Paid_amt"));			
			}
			return paid_amt;	
			
		} catch (SQLException e1) {
			e1.printStackTrace();
			System.out.println(QUERY);
			return paid_amt;
		}
	}

	public boolean update(Connection conn, double fine_paid, String card_id) {

		// check if card_id exists
		String QUERY = "SELECT COUNT(*) FROM "+ PAID_TABLE + " WHERE Card_id =" + card_id;
		try {
			Statement statement = (Statement) conn.createStatement();
			ResultSet rs = statement.executeQuery(QUERY);
			while(rs.next())
			{
				int count = rs.getInt(1);
				if(count==0)
				{
					// doesnot exist, create a new record with the card_id and paid_amt
					String CREATE = "INSERT INTO " + PAID_TABLE + " VALUES('" + card_id + "','" + fine_paid +"')";
					Statement create = (Statement) conn.createStatement();
					create.execute(CREATE);
				}
				else
				{
					// exists, update the record
					// doesnot exist, create a new record with the card_id and paid_amt
					String UPDATE = "UPDATE " + PAID_TABLE + " SET Paid_amt=Paid_amt+'" + fine_paid +"' WHERE Card_id=" + card_id;
					Statement update = (Statement) conn.createStatement();
					update.execute(UPDATE);				
				}
			}		
		} catch (SQLException e1) {
			e1.printStackTrace();
			System.out.println(QUERY);
			return false;
		}
		return true;
	}	
}
