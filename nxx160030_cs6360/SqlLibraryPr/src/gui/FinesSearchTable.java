package gui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class FinesSearchTable {
	
	private Table fines_tbl;
	private HashSet<String> cardids = new HashSet<String>();
	private List<String[]> rows = new ArrayList<String[]>();
	
	public FinesSearchTable(Table tbl)
	{
		fines_tbl = tbl;
	}
	public void clearRecords() {
		
		int count = fines_tbl.getItemCount();
		for(int i=count; i>0; i--)
		{
			fines_tbl.remove(i-1);
		}
		cardids = new HashSet<String>();
		rows = new ArrayList<String[]>();		
	}

	public void createTable() {
		
		ListIterator<String[]> iterator = rows.listIterator();
		while(iterator.hasNext())
		{
			String[] row = (String[])iterator.next();
			TableItem newRow = new TableItem(fines_tbl,SWT.NONE);
			newRow.setText(row);
		}	
	}

	public boolean contains(String card_id) {

		return cardids.contains(card_id);
	}

	public void add(String[] row) {

		String isbn = row[0];

		if(!cardids.contains(isbn))
		{
			TableItem newRow = new TableItem(fines_tbl,SWT.NONE);
			newRow.setText(row);
			cardids.add(isbn);
			rows.add(row);
		}	
	}

}
