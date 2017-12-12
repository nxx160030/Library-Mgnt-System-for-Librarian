package eventHandler;

import java.sql.SQLException;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.mysql.jdbc.Connection;

import db.Book_Loans;
import sql.SqlConnect;

public class BtnChckInHandler {
	
	private Table table;
	private Book_Loans bk_loans = new Book_Loans();
	private SqlConnect sqlConn = new SqlConnect();
	private Connection conn;
	private String Isbn;

	public BtnChckInHandler(Table tbl_chckin)
	{
		table = tbl_chckin;
	}
	
	
	public int checkIn()
	{
		TableItem[] item = table.getSelection();
		String[] txt = new String[3];
		
		for(int i=0;i<item.length;i++)
		{
			txt = item[i].getText().split("\t");
			Isbn = txt[0];
		}		
		
		// check in the selected row
		try {
			conn = sqlConn.connect();
			if(bk_loans.updateRrcd(conn, Isbn))
			{
				// check in success
				// remove the row
				updateItem();
				conn.close();
				return 0;
			}
			else 
			{
				// fail, return -1
				conn.close();
				return -1;	
			}
		
			} catch (SQLException e) {
				e.printStackTrace();
				return -1;	
			}
	}
	
	private void updateItem()
	{
		TableItem[] currentRow = table.getSelection();
		
		for(int i=0;i<currentRow.length;i++)
		{
			table.remove(i);
		}
		
		table.setRedraw(true);		
	}
	
}
