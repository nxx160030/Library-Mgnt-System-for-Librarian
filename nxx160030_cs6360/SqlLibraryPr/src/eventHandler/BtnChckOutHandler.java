package eventHandler;

import java.sql.SQLException;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.mysql.jdbc.Connection;

import db.Book_Loans;
import sql.SqlConnect;

public class BtnChckOutHandler {
	private Table table;
	private String borrowerId;
	private Book_Loans bk_loans = new Book_Loans();
	private SqlConnect sqlConn = new SqlConnect();
	private Connection conn;
	private String Isbn;
	private String avail;

	public BtnChckOutHandler(Table tbl_chckout, String brrId) {

		table = tbl_chckout;
		borrowerId = brrId;		
	}

	public int checkOut() {
		
		TableItem[] item = table.getSelection();
		
		// check if checked out
		for(int i=0;i<item.length;i++)
		{
			//get last column "Availability"
			Isbn = item[i].getText(0);
			avail = item[i].getText(3);
			
			if(avail.equalsIgnoreCase("No"))
			{
				// already checked out, return 1
				return 1;
			}
		}
		
		// check if borrower has 3 records already
		try {
			if(checkLoanRecords())
			{
				// has 3 loans already, return -1
				return -1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// insert into book loan table
		try {
			if(createLoanRecord())
			{
				// refresh the table, set selected item availability to "No"
				updateItem();
				return 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// exception
		return 2;
	}
	
	// if loaned 3 books already, return true if 3 book records
	private boolean checkLoanRecords() throws SQLException
	{	
		conn = sqlConn.connect();
		try {
			
			boolean loanBln = bk_loans.queryRcrdCntByBrrId(conn, borrowerId)>=3;
			conn.close();
			return loanBln;
		} catch (SQLException e) {
			e.printStackTrace();
			conn.close();
			return false;
		}
	}
	
	private boolean createLoanRecord() throws SQLException
	{
		conn = sqlConn.connect();
		boolean success = bk_loans.insertRrcd(conn, Isbn, borrowerId);
		
		return success;
	}
	
	private void updateItem()
	{
		TableItem[] currentRow = table.getSelection();
		
		for(int i=0;i<currentRow.length;i++)
		{
			currentRow[i].setText(3, "No");
		}
		
		table.setRedraw(true);		
	}	
}
