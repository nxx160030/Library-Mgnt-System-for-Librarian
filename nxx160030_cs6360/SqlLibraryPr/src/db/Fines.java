package db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ListIterator;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class Fines {
	
	private String FINES_TABLE = "FINES";	


	public boolean queryByLoanId(Connection connection, String loan_id) {
		
		String QUERY = "SELECT COUNT(*) AS COUNT FROM "+ FINES_TABLE + " WHERE Loan_id=" + loan_id;
		try {
			Statement statement = (Statement) connection.createStatement();
			ResultSet rs = statement.executeQuery(QUERY);
			int count=0;
			while(rs.next())
			{
				count = Integer.parseInt(rs.getString("COUNT"));			
			}
			return (count>0);	
			
		} catch (SQLException e1) {
			e1.printStackTrace();
			System.out.println(QUERY);
			return false;
		}
	}
	
	public double queryAmtByLoanId(Connection connection, String loan_id)
	{
		String QUERY = "SELECT Fine_amt FROM "+ FINES_TABLE + " WHERE Loan_id=" + loan_id;
		double fine_amt = 0;
		try {
			Statement statement = (Statement) connection.createStatement();
			ResultSet rs = statement.executeQuery(QUERY);
			while(rs.next())
			{
				fine_amt = Double.parseDouble(rs.getString("Fine_amt"));			
			}
			return fine_amt;	
			
		} catch (SQLException e1) {
			e1.printStackTrace();
			System.out.println(QUERY);
			return fine_amt;
		}
	}

	public boolean updateByLoanId(Connection conn, String loan_id, double amount) {
		
		String UPDATE = "UPDATE " + FINES_TABLE +" SET Fine_amt='"+ amount + "' WHERE Loan_id='" + loan_id + "' AND Paid=0";
		try {
			Statement statement = (Statement) conn.createStatement();
			int success = statement.executeUpdate(UPDATE);

			return (success>0);	
			
		} catch (SQLException e1) {
			e1.printStackTrace();
			System.out.println(UPDATE);
			return false;
		}		
	}

	public boolean insert(Connection conn, String loan_id, double amount, int paid) {
		
		String INSERT = "INSERT INTO "+ FINES_TABLE +" VALUES('" + loan_id + "','" + amount +  "','" + paid + "')";
		try {
			Statement statement = (Statement) conn.createStatement();
			boolean success = statement.execute(INSERT);
			return success;				
		} catch (SQLException e1) {
			e1.printStackTrace();
			System.out.println(INSERT);
			return false;
		}		
	}

	public boolean updatePaid(Connection conn, List<String> ln_ids) {

		ListIterator<String> itr = ln_ids.listIterator();	
		if(!itr.hasNext()) return false;
		
		while(itr.hasNext())
		{
			String ln_id = itr.next();
			String UPDATE = "UPDATE " + FINES_TABLE +" SET Paid='1' WHERE Loan_id='" + ln_id + "'";
			try {
				Statement statement = (Statement) conn.createStatement();
				statement.execute(UPDATE);
				
			} catch (SQLException e1) {
				e1.printStackTrace();
				System.out.println(UPDATE);
				return false;
			}	
		}
		return true;
	}	

}
