package eventHandler;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.swt.widgets.Table;

import com.mysql.jdbc.Connection;

import db.Authors;
import db.Authors.AuthorsRecord;
import db.Book;
import db.Book.BookRecord;
import db.BookAuthors;
import db.BookAuthors.BookAuthorsRecord;
import db.Book_Loans;
import gui.BookSearchTable;
import sql.SqlConnect;

public class BtnBookSearchHandler {
	
	private String[] srchStr;
	private SqlConnect sqlConn = new SqlConnect();
	Book book = new Book();
	Book_Loans bkLoans = new Book_Loans();
	BookAuthors bkAuthors = new BookAuthors();
	Authors authors = new Authors();
	private BookSearchTable resultTbl;
	private Connection conn;
	

	public BtnBookSearchHandler(Table table, String info) {
			
		resultTbl = new BookSearchTable(table);
		resultTbl.clearRecords();
		srchStr = info.split(",");	
	}
	
	public boolean search() throws SQLException
	{		
		boolean hasResult = false;	
		conn = sqlConn.connect();
		try {			
			for(int i=0;i<srchStr.length;i++)
			{
				// first search table BOOK->BOOK_AUTHORS->AUTHORS->BOOK_LOANS to get ISBN,Title,Authors,Availability
				if(searchBookFirst(srchStr[i]))
					hasResult = true;
				// then search table AUTHORS->BOOK_AUTHROS->BOOK->BOOK_LOANS to get  ISBN,Title,Authors,Availability
				if(searchAuthorsFirst(srchStr[i]))
					hasResult = true;
			}
			
			// sort the results
			resultTbl.sort(srchStr);
			
			resultTbl.createSortedTable();
			
			conn.close();
		} catch (SQLException e) {	
			conn.close();
			e.printStackTrace();
		}
		return hasResult;			
	}
	
	private boolean searchBookFirst(String str)
	{	
			// get isbn and title			
			List<BookRecord> rs_Book = book.query(conn, str);		
			ListIterator<BookRecord> bk_itr = (ListIterator<BookRecord>) rs_Book.listIterator();
			
			if(rs_Book.isEmpty()) return false;

			while(bk_itr.hasNext())
			{
				int author_id;
				String name="";
				String availability;
				// get "Isbn"
				BookRecord bk_rcd = bk_itr.next();
				String isbn = bk_rcd.getIsbn();
				// row already included
				if(resultTbl.containRow(isbn)) continue;
				
				String title = bk_rcd.getTitle();
				HashSet<String> names = new HashSet<String>();
						
				boolean loaned = bkLoans.queryAvailability(conn, isbn);
				// find availability
				if(loaned)
				{
					availability = "No";
				}else
				{
					availability = "Yes";
				}

				List<BookAuthorsRecord> bookAuthors = bkAuthors.query(conn, isbn);
				ListIterator<BookAuthorsRecord> bk_authors_itr = (ListIterator<BookAuthorsRecord>) bookAuthors.listIterator();
				// get author names into a String
				while(bk_authors_itr.hasNext())
				{
					// get author_id
					author_id = bk_authors_itr.next().getAuthor_id();			
					
					// one author_id has one name
					String current = authors.queryNameByAuthorId(conn, author_id);	
					if(!names.contains(current))
					{// get name
						name += current + ",";	
						names.add(current);
					}
				}
				
				// delete the last "," in name
//				if(name.lastIndexOf(",")>0)
//					name = name.substring(0, name.lastIndexOf(",")-1);
				
				String[] row = {isbn,title,name,availability};
				//System.out.println(name);
				resultTbl.add(row);
			}
			return true;
	}
	
	private boolean searchAuthorsFirst(String str)
	{	
			// get author_id and name
			List<AuthorsRecord> rs_Authors = authors.query(conn, str);	
			ListIterator<AuthorsRecord> authors_itr = (ListIterator<AuthorsRecord>) rs_Authors.listIterator();
			
			if(rs_Authors.isEmpty()) return false;
			
			while(authors_itr.hasNext())
			{
				// get "author_id" and "name"
				AuthorsRecord author_record = authors_itr.next();
				int author_id = author_record.getAuthor_id();
				String name = author_record.getName();
				String isbn;
				String title = "";
				String availability;
				
				List<BookAuthorsRecord> bookAuthors = bkAuthors.queryIsbnByAuthorId(conn, author_id);
				ListIterator<BookAuthorsRecord> bk_authors_itr = (ListIterator<BookAuthorsRecord>) bookAuthors.listIterator();

				while(bk_authors_itr.hasNext())
				{
					// get isbn
					BookAuthorsRecord record = bk_authors_itr.next();
					isbn = record.getIsbn();
					// row already included
					if(resultTbl.containRow(isbn)) continue;
					
					// make a row based on unique isbn
					List<BookRecord> rs_Book = book.query(conn, isbn);
					ListIterator<BookRecord> bk_itr = (ListIterator<BookRecord>) rs_Book.listIterator();
					while(bk_itr.hasNext())
					{
						// get unique title
						BookRecord bk_record = bk_itr.next();
						title = bk_record.getTitle();
					}
					
					boolean loaned = bkLoans.queryAvailability(conn, isbn);
					if(loaned)
					{
						availability = "No";
					}
					else
					{
						availability = "Yes";
					}
					
					// get names
					HashSet<String> names = new HashSet<String>();
					String rowNameStr = name+",";
					names.add(name);
				
					List<BookAuthorsRecord> bkAuthors_rcd = bkAuthors.query(conn, isbn);
					ListIterator<BookAuthorsRecord> bk_authors_itr2 = (ListIterator<BookAuthorsRecord>) bkAuthors_rcd.listIterator();
					// get author names into a String
					while(bk_authors_itr2.hasNext())
					{
						// get author_id
						author_id = bk_authors_itr2.next().getAuthor_id();
						// get name
						String current = authors.queryNameByAuthorId(conn, author_id);
						
						if(!names.contains(current))
						{// get name
							rowNameStr += current+",";
							names.add(current);
						}	
					}
					// delete the last "," in name
//					if(name.lastIndexOf(",")>0)
//						name = name.substring(0, name.lastIndexOf(",")-1);
					
					String[] row = {isbn,title,rowNameStr,availability};
					//System.out.println(title);
					resultTbl.add(row);
				}
			}
			return true;
	}
}
