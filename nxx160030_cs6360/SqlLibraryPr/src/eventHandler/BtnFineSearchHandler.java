package eventHandler;

import java.sql.SQLException;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.swt.widgets.Table;

import com.mysql.jdbc.Connection;

import db.Book_Loans;
import db.Borrower;
import db.Borrower.BorrowerRecord;
import db.Fines;
import db.Paid;
import gui.FinesSearchTable;
import sql.SqlConnect;

public class BtnFineSearchHandler {
	
	FinesSearchTable tbl_fine;
	String[] srchStr;
	private Borrower brr = new Borrower();
	private Book_Loans bk_lns = new Book_Loans();
	private Fines fines = new Fines();
	private Paid paid = new Paid();
	private Connection conn;
	private SqlConnect sqlConn = new SqlConnect();
	
	public BtnFineSearchHandler(Table tbl, String str)
	{
		tbl_fine = new FinesSearchTable(tbl);
		tbl_fine.clearRecords();
		srchStr = str.split(",");
	}

	public void search() {
		
		String card_id="";
		String bname="";
		try {
			conn = sqlConn.connect();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		for(int i=0;i<srchStr.length;i++)
		{
			List<BorrowerRecord> brrRcdList = brr.queryByIdName(conn, srchStr[i]);
			ListIterator<BorrowerRecord> brrRcdListItr = brrRcdList.listIterator();
			
			if(brrRcdList.isEmpty()) continue;
			
			while(brrRcdListItr.hasNext())
			{
				BorrowerRecord rcd = brrRcdListItr.next();
				card_id = rcd.getCard_id();
				bname = rcd.getBname();
				double amount = 0;
				
				// find fine_amt
				// find loan_id of the card_id first
				List<String> ln_idList = bk_lns.queryRtnedByCardId(conn, card_id);
				ListIterator<String> ln_idListItr = ln_idList.listIterator();
				
				// add all the fine_amt of loan_ids of the same card_id
				while(ln_idListItr.hasNext())
				{
					String ln_id = ln_idListItr.next();
					amount += fines.queryAmtByLoanId(conn, ln_id);
				}
				
				// minus the fines already paid
				amount -= paid.queryAmtByCardId(conn,card_id);
				
				// new a row and add to the table
				String[] row = {card_id,Double.toString(amount),bname};
				
				tbl_fine.add(row);
			}
		}		
	}
}
