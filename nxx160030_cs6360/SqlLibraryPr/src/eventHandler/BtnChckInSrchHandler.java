package eventHandler;

import java.sql.SQLException;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.swt.widgets.Table;

import com.mysql.jdbc.Connection;

import db.Book_Loans;
import db.Book_Loans.BookLoanRecord;
import db.Borrower;
import db.Borrower.BorrowerRecord;
import gui.CheckInTable;
import sql.SqlConnect;

public class BtnChckInSrchHandler {
	
	private String[] srchStr;
	private Borrower brr = new Borrower();
	private Book_Loans bk_loans = new Book_Loans();
	private CheckInTable chckInTbl;
	private SqlConnect sqlConn = new SqlConnect();
	private Connection conn;
	
	public BtnChckInSrchHandler(Table table, String info) {	
		
		chckInTbl = new CheckInTable(table);
		chckInTbl.clearRecords();
		srchStr = info.split(",");		
	}

	public boolean search() throws SQLException{
		
		boolean hasResult = false;	
		conn = sqlConn.connect();
		try {			
			for(int i=0;i<srchStr.length;i++)
			{
				// first search 
				if(searchBrrFirst(srchStr[i]))
					hasResult = true;
				// then search table 
				if(searchBkLoansFirst(srchStr[i]))
					hasResult = true;
			}			
			chckInTbl.createTable();
			
			conn.close();
		} catch (SQLException e) {	
			conn.close();
			e.printStackTrace();
		}
		return hasResult;			
	}

	private boolean searchBkLoansFirst(String key) {
		
		String srchKey = key;
		
		List<BookLoanRecord> bkLnRcdList = bk_loans.queryNotRtnById(conn,srchKey);
		ListIterator<BookLoanRecord> bkLnRcdItr = bkLnRcdList.listIterator();
		if(bkLnRcdList.isEmpty())
			return false;
		
		while(bkLnRcdItr.hasNext())
		{
			BookLoanRecord rcd = bkLnRcdItr.next();
			String isbn = rcd.getIsbn();
			if(chckInTbl.contains(isbn)) continue;
			
			String card_id = rcd.getCardId();
			
			List<BorrowerRecord> brrRcdList = brr.queryByIdName(conn, card_id);
			ListIterator<BorrowerRecord> brrRcdItr = brrRcdList.listIterator();
			
			while(brrRcdItr.hasNext())
			{
				BorrowerRecord brrRcd = brrRcdItr.next();
				String bname = brrRcd.getBname();
				
				// a new row
				String[] row = {isbn,card_id,bname};
				chckInTbl.add(row);
			}
		}
		
		return true;
	}

	private boolean searchBrrFirst(String key) {

		String srchKey = key;
		List<BorrowerRecord> result = brr.queryByIdName(conn,srchKey);
		ListIterator<BorrowerRecord> brrRcrdItr = result.listIterator();
		if(result.isEmpty())
			return false;
		
		while(brrRcrdItr.hasNext())
		{
			BorrowerRecord rcd = brrRcrdItr.next();
			
			String BName = rcd.getBname();
			String Card_id = rcd.getCard_id();
			
			List<BookLoanRecord> bkLnList = bk_loans.queryNotRtnById(conn, Card_id);
			ListIterator<BookLoanRecord> bkLnRcrdItr = bkLnList.listIterator();
			
			while(bkLnRcrdItr.hasNext())
			{
				BookLoanRecord bkLnRcrd = bkLnRcrdItr.next();
				String isbn = bkLnRcrd.getIsbn();
				
				// already included
				if(chckInTbl.contains(isbn)) continue;
				
				// a new row
				String[] row = {isbn,Card_id,BName};				
				chckInTbl.add(row);			
			}		
		}
		return true;		
	}

}
