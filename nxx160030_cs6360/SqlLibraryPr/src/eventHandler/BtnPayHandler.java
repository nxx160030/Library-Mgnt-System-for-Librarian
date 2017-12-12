package eventHandler;

import java.sql.SQLException;
import java.util.List;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.mysql.jdbc.Connection;

import db.Book_Loans;
import db.Fines;
import db.Paid;
import sql.SqlConnect;

public class BtnPayHandler {
	
	private Table tbl_fine;
	private double fine_paid;
	private Fines fines = new Fines();
	private Paid paid = new Paid();
	private Book_Loans bk_lns = new Book_Loans();
	private Connection conn;
	private SqlConnect sqlConn = new SqlConnect();

	public BtnPayHandler(Table tbl, String amnt) {

		tbl_fine = tbl;
		fine_paid = Double.parseDouble(amnt);
	}

	public int update() {
		
		try {
			conn = sqlConn.connect();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		String card_id = "";
		TableItem[] selected =	tbl_fine.getSelection();
		card_id = selected[0].getText();

		// update FINES
		// get loan_id by card_id first
		List<String> ln_ids = bk_lns.queryRtnedByCardId(conn, card_id);
		// not returned books cannot be paid fines
		if(ln_ids.isEmpty()) return -1;
		
		// update Fines with Loan_ids
		boolean success_fines = fines.updatePaid(conn,ln_ids);
			
		// update PAID
		boolean success_paid = paid.update(conn,fine_paid,card_id);
		
		// refresh the table
		double amt = Double.parseDouble(selected[0].getText(1))-fine_paid;
		selected[0].setText(1, Double.toString(amt));
		
		// success return 0
		if(success_fines&&success_paid)
			return 0;
		else 
			return -1;
	}	

}
