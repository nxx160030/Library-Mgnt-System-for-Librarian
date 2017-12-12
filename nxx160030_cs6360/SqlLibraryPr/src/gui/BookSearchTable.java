package gui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class BookSearchTable {
	
	private Table table;
	private HashSet<String> isbns = new HashSet<String>();
	private List<String[]> rows = new ArrayList<String[]>();
	private int countMatch[];

	
	public BookSearchTable(Table tbl)
	{
		table = tbl;
	}	
	
	public Table getTable()
	{
		return table;
	}


	public void add(String[] row) {
		
		String isbn = row[0];

		if(!isbns.contains(isbn))
		{
			TableItem newRow = new TableItem(table,SWT.NONE);
			newRow.setText(row);
			isbns.add(isbn);
			rows.add(row);
		}
	}
	

	public void clearRecords()
	{
		int count = table.getItemCount();
		for(int i=count; i>0; i--)
		{
			table.remove(i-1);
		}
		isbns = new HashSet<String>();
		rows = new ArrayList<String[]>();
	}
	
	private void removeTableItems()
	{
		int count = table.getItemCount();
		for(int i=count; i>0; i--)
		{
			table.remove(i-1);
		}
		isbns = new HashSet<String>();
	}
	
	public boolean containRow(String isbn)
	{
		return isbns.contains(isbn);
	}

	public void sort(String[] srchStr) {
		
		if(rows.size()<2) return;
		
		int rowCount = rows.size();
		countMatch = new int[rowCount];
		int currentRowIdx = 0;
			
		Iterator<String[]> iterator = rows.listIterator();
		while(iterator.hasNext())
		{
			countMatch[currentRowIdx] = 0;
			String[] currentRow = (String[])iterator.next();
								
			for(int j=0;j<srchStr.length;j++)
			{
				String currentKey = srchStr[j];
				for(int i=0; i<currentRow.length-1;i++)
				{
					if(currentRow[i].toLowerCase().matches("(.*)"+currentKey.toLowerCase()+"(.*)"))
						countMatch[currentRowIdx]++;
				}
			}			
			currentRowIdx++;			
		}
		
		// use quick sort to sort the rows
		quickSort(0,rowCount-1);
	}
	
	private void quickSort(int frstIndex,int lstIndex)
	{		
		int i = frstIndex;
		int j = lstIndex;
		
		if(i>=j) return;

		int mddl = (int)(i+(j-i)/2);
		
		while(i<=mddl)
		{		
			if(countMatch[i]<countMatch[j]){
				exchangeRows(i,j);
				i++;
				j=lstIndex;
			}else
			{
				if(j>mddl)
				{
					j--;
				}
				else
				{
					i++;
					j=lstIndex;
				}
			}
		}	
		quickSort(frstIndex,mddl);
		quickSort(mddl+1,lstIndex);				
	}
	
	private void exchangeRows(int i, int j)
	{
		String[] temp_row = rows.get(i);
		rows.set(i, rows.get(j));
		rows.set(j, temp_row);
		
		int temp_count = countMatch[i];
		countMatch[i] = countMatch[j];
		countMatch[j] = temp_count;
	}
	
	public void createSortedTable()
	{
		// remove table items first
		removeTableItems();
		// map the sorted rows to table
		ListIterator<String[]> iterator = rows.listIterator();
		while(iterator.hasNext())
		{
			String[] sorted_row = (String[])iterator.next();
			TableItem newRow = new TableItem(table,SWT.NONE);
			newRow.setText(sorted_row);
		}			
	}

}
