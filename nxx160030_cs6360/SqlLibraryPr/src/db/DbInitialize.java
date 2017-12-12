package db;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;

import com.csvreader.CsvReader;

import sql.SqlConnect;

import java.sql.Connection;

public class DbInitialize {
	
	private static Connection connection;
	private static String BOOKS = "./books.csv";
	private static String BORROWERS = "./borrowers.csv";
	
	private static char bk_delimiter = '\t';
	private static char br_delimiter = ',';
	
	private static String ISBN10 = "ISBN10";
	private static String TITLE = "Title";
	private static String AUTHOR = "Author";
	
	private static int author_id = 1;
	
	private static Book book = new Book();
	private static Authors authors = new Authors();
	private static Borrower borrower = new Borrower();
	private static BookAuthors bookAuthors = new BookAuthors();
	
	private static HashSet<String> authors_hashset = new HashSet<String>();
	private static HashSet<String> isbn_hashset = new HashSet<String>();
	private static HashSet<String> card_id_hashset = new HashSet<String>();



	public static void main(String[] args) throws IOException, SQLException {
		
		// new a mysql connector and connect to the db
		SqlConnect con = new SqlConnect();
		connection = con.connect();	
		
		try {
			// open books.csv
			CsvReader bkReader = new CsvReader(BOOKS);
			
			bkReader.setDelimiter(bk_delimiter);
			
			bkReader.readHeaders();
			
			while(bkReader.readRecord())
			{
				// initialize table BOOK
				String isbn = bkReader.get(ISBN10);
				String title = bkReader.get(TITLE);
				if(!isbn_hashset.contains(isbn))
				{
					boolean success = book.insert(connection,isbn,title);
					if(success)
						isbn_hashset.add(isbn);
				}

				
				// initialize table AUTHORS
				String author = bkReader.get(AUTHOR);
				if(author.equalsIgnoreCase("")) author = "WRITER MISSING";
				String[] author_arr = author.split(",");
				HashSet<String> current_arr = new HashSet<String>();
				for(int i=0; i<author_arr.length; i++)
				{
					if(!authors_hashset.contains(author_arr[i].toLowerCase()))
					{
						boolean success = authors.insert(connection, author_id,author_arr[i]);
						if(success){
							authors_hashset.add(author_arr[i].toLowerCase());
							bookAuthors.insert(connection,isbn,author_id);
							author_id++;
						}
					}
					else{
						
						if(!current_arr.contains(author_arr[i])) 
						{
							// get author_id by name and insert into BOOK_AUTHORS
							int author_id = authors.queryAuthorIdByFullName(connection, author_arr[i]);
							bookAuthors.insert(connection,isbn,author_id);
						}
					}
					current_arr.add(author_arr[i]);
				}
	
			}
			
			// open borrowers.csv
			String CARD_ID = "borrower_id";
			String SSN = "ssn";
			String F_NAME = "first_name";
			String L_NAME = "last_name";
			String ADDRESS = "address";
			String CITY = "city";
			String STATE = "state";
			String PHONE = "phone";
			CsvReader borrowersReader = new CsvReader(BORROWERS);
			
			borrowersReader.setDelimiter(br_delimiter);
			
			borrowersReader.readHeaders();
			
			while(borrowersReader.readRecord())
			{
				// initialize table BORROWER
				String card_id = borrowersReader.get(CARD_ID);
				String ssn = borrowersReader.get(SSN);
				String bname = borrowersReader.get(F_NAME) + " " + borrowersReader.get(L_NAME);
				String address = borrowersReader.get(ADDRESS) + "," + borrowersReader.get(CITY) + "," 
											+ borrowersReader.get(STATE);
				String phone = borrowersReader.get(PHONE);
				if(!card_id_hashset.contains(card_id))
				{
					boolean success = borrower.insert(connection,card_id,ssn,bname,address,phone);		
					if(success)
						card_id_hashset.add(card_id);
				}
			}
		
			connection.close();
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			connection.close();
		}
	}
}
