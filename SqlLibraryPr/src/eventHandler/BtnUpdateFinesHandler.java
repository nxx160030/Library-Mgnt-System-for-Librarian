package eventHandler;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.ListIterator;

import com.mysql.jdbc.Connection;

import db.Book_Loans;
import db.Book_Loans.BookLoanRecord;
import db.Fines;
import sql.SqlConnect;

public class BtnUpdateFinesHandler {
	
	private SqlConnect sqlConn = new SqlConnect();
	private Connection conn;
	private Book_Loans bk_lns = new Book_Loans();
	private Fines fines = new Fines();

	public boolean update() {
			
		try {
			conn = sqlConn.connect();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		List<BookLoanRecord> finedRcds = bk_lns.queryFinedRcds(conn);
		ListIterator<BookLoanRecord> bk_ln_itr = (ListIterator<BookLoanRecord>) finedRcds.listIterator();
		
		if(finedRcds.isEmpty()) return true;
		
		// update table FINES
		while(bk_ln_itr.hasNext())
		{
			BookLoanRecord bklnRcd = bk_ln_itr.next();
			String loan_id = bklnRcd.getLoanId();
			String[] due_date = bklnRcd.getDueDate().split("/");
			LocalDate currentDate = LocalDate.now();
			LocalDate dueDate = LocalDate.of(Integer.parseInt(due_date[0]), Integer.parseInt(due_date[1]), Integer.parseInt(due_date[2]));
			Period p = Period.between(dueDate,currentDate);
			double amount = 0;
			

			amount = (p.getYears()*365 + p.getMonths()*30 + p.getDays())*0.25;
			
			if(fines.queryByLoanId(conn,loan_id))
			{
				// exists, just update the amount if paid is false
				fines.updateByLoanId(conn,loan_id,amount);
			}
			else
			{
				// no exist, create new records, 0 stands for not paid
				fines.insert(conn,loan_id,amount,0);
			}
		}
		return true;	
	}	
}
