package gui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class CheckInTable {
	
	private Table table;
	private HashSet<String> isbns = new HashSet<String>();
	private List<String[]> rows = new ArrayList<String[]>();
	
	public CheckInTable(Table tbl)
	{
		table = tbl;
	}

	public void clearRecords() {
		
		int count = table.getItemCount();
		for(int i=count; i>0; i--)
		{
			table.remove(i-1);
		}
		isbns = new HashSet<String>();
		rows = new ArrayList<String[]>();		
	}

	public void createTable() {
		
		ListIterator<String[]> iterator = rows.listIterator();
		while(iterator.hasNext())
		{
			String[] row = (String[])iterator.next();
			TableItem newRow = new TableItem(table,SWT.NONE);
			newRow.setText(row);
		}	
	}

	public boolean contains(String isbn) {

		return isbns.contains(isbn);
	}

	public void add(String[] row) {

		String isbn = row[0];

		if(!isbns.contains(isbn))
		{
			isbns.add(isbn);
			rows.add(row);
		}	
	}	

}
