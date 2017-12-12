package gui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import org.eclipse.swt.SWT;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;

import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.wb.swt.SWTResourceManager;

import eventHandler.BtnBookSearchHandler;
import eventHandler.BtnBrrMgntCreateHandler;
import eventHandler.BtnChckInHandler;
import eventHandler.BtnChckInSrchHandler;
import eventHandler.BtnChckOutHandler;
import eventHandler.BtnFineSearchHandler;
import eventHandler.BtnPayHandler;
import eventHandler.BtnUpdateFinesHandler;

import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.RowData;



public class MISWindow {

	protected Shell shlUtdLibraryMIS;
	private Text txtIsbnAuthorBook_1;
	private Text txtChckIn;
	private Text txtSsn;
	private Text txtFName;
	private Text txtLName;
	private Text txtEmail;
	private Text txtAddress;
	private Text txtCity;
	private Text txtPhone;
	private Text txtFine;
	private Table table;
	private Text txt_bkInfo;
	private Table tbl_chckout;
	private Text txtborrowerId;
	private Table tbl_chckin;
	private Text txtState;
	private Text txtPayFine;
	private Table tbl_fine;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MISWindow window = new MISWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlUtdLibraryMIS.open();
		shlUtdLibraryMIS.layout();
		while (!shlUtdLibraryMIS.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlUtdLibraryMIS = new Shell();
		shlUtdLibraryMIS.setModified(true);
		shlUtdLibraryMIS.setSize(776, 483);
		shlUtdLibraryMIS.setText("Welcome to UTD Library System");
		RowLayout rl_shlUtdLibraryMIS = new RowLayout(SWT.HORIZONTAL);
		rl_shlUtdLibraryMIS.center = true;
		shlUtdLibraryMIS.setLayout(rl_shlUtdLibraryMIS);
		
		CTabFolder tabFolder = new CTabFolder(shlUtdLibraryMIS, SWT.BORDER);
		tabFolder.setLayoutData(new RowData(774, 440));
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		CTabItem tbtmBookSearch = new CTabItem(tabFolder, SWT.NONE);
		tbtmBookSearch.setText("Book Search");
		
		Composite composite_search = new Composite(tabFolder, SWT.NONE);
		tbtmBookSearch.setControl(composite_search);
		composite_search.setLayout(null);
		
		Label lblBookInfo_1 = new Label(composite_search, SWT.NONE);
		lblBookInfo_1.setBounds(3, 3, 66, 14);
		lblBookInfo_1.setAlignment(SWT.CENTER);
		lblBookInfo_1.setText("Book Info.:");
		
		txtIsbnAuthorBook_1 = new Text(composite_search, SWT.BORDER);
		txtIsbnAuthorBook_1.setBounds(72, 3, 210, 19);
		txtIsbnAuthorBook_1.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {	
				if(txtIsbnAuthorBook_1.getText().equalsIgnoreCase("(required)Isbn, Author, Book Name,etc"))
					txtIsbnAuthorBook_1.setText("");
			}
			@Override
			public void focusLost(FocusEvent arg0) {	
				if(txtIsbnAuthorBook_1.getText().equalsIgnoreCase(""))
					txtIsbnAuthorBook_1.setText("(required)Isbn, Author, Book Name,etc");
			}
		});
		txtIsbnAuthorBook_1.setText("(required)Isbn, Author, Book Name,etc");
		txtIsbnAuthorBook_1.setFont(SWTResourceManager.getFont(".Helvetica Neue DeskInterface", 11, SWT.NORMAL));
		txtIsbnAuthorBook_1.setForeground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		
		Button btn_search = new Button(composite_search, SWT.BORDER | SWT.FLAT | SWT.CENTER);
		btn_search.setBounds(285, 3, 58, 22);
		btn_search.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				String info = txtIsbnAuthorBook_1.getText();
				if(info.equalsIgnoreCase("")||info.equalsIgnoreCase("(required)Isbn, Author, Book Name,etc"))
				{
					// create a dialog with ok and cancel buttons and a question icon
					MessageBox dialog =
					        new MessageBox(shlUtdLibraryMIS, SWT.ICON_QUESTION | SWT.OK);
					dialog.setText("Invalid Search");
					dialog.setMessage("Please Input Search Key!");
					dialog.open();
				}
				else
				{
					BtnBookSearchHandler bkSrch = new BtnBookSearchHandler(table,info);
					// see if no result
					try {
						if(!bkSrch.search())
						{
							// no result
							// create a dialog with ok button and a question icon
							MessageBox dialog =
							        new MessageBox(shlUtdLibraryMIS, SWT.ICON_QUESTION | SWT.OK);
							dialog.setText("No Data");
							dialog.setMessage("No Such Book in the Library!");
							dialog.open();
						}
						else
						{
							// refresh the table
							table.setRedraw(true);
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		});
		btn_search.setText("Search");
		
		
		table = new Table(composite_search, SWT.BORDER);
		table.setBounds(23, 49, 713, 362);
		table.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.LEFT);
		tblclmnNewColumn.setWidth(92);
		tblclmnNewColumn.setText("ISBN");
		
		TableColumn tblclmnNewColumn_1 = new TableColumn(table, SWT.LEFT);
		tblclmnNewColumn_1.setWidth(278);
		tblclmnNewColumn_1.setText("Book Title");
		
		TableColumn tblclmnAuthors = new TableColumn(table, SWT.LEFT);
		tblclmnAuthors.setWidth(231);
		tblclmnAuthors.setText("Authors");
		
		TableColumn tblclmnAvailability = new TableColumn(table, SWT.LEFT);
		tblclmnAvailability.setWidth(112);
		tblclmnAvailability.setText("Availability");
		
		
		CTabItem tabItem_bookLoan = new CTabItem(tabFolder, SWT.NONE);
		tabItem_bookLoan.setText("Book Loan");
		
		CTabFolder tabFolder_bookLoan = new CTabFolder(tabFolder, SWT.BORDER);
		tabItem_bookLoan.setControl(tabFolder_bookLoan);

		
		CTabItem tbtmCheckOut = new CTabItem(tabFolder_bookLoan, SWT.NONE);
		tbtmCheckOut.setText("Check Out");
		
		Composite composite_checkOut = new Composite(tabFolder_bookLoan, SWT.NONE);
		tbtmCheckOut.setControl(composite_checkOut);
	
		Label lblBookInfo = new Label(composite_checkOut, SWT.NONE);
		lblBookInfo.setAlignment(SWT.CENTER);
		lblBookInfo.setBounds(29, 32, 122, 21);
		lblBookInfo.setText("Book Info.: ");
		
		Button btnSearch = new Button(composite_checkOut, SWT.BORDER | SWT.FLAT | SWT.CENTER);
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				String info = txt_bkInfo.getText();
				if(info.equalsIgnoreCase("")||info.equalsIgnoreCase("(required)Isbn, Author, Book Name,etc"))
				{
					// create a dialog with ok and cancel buttons and a question icon
					MessageBox dialog =
					        new MessageBox(shlUtdLibraryMIS, SWT.ICON_QUESTION | SWT.OK);
					dialog.setText("Invalid Search");
					dialog.setMessage("Please Input Search Key!");
					dialog.open();
				}
				else
				{
					BtnBookSearchHandler bkSrch = new BtnBookSearchHandler(tbl_chckout,info);
					// see if no result
					try {
						if(!bkSrch.search())
						{
							// no result
							// create a dialog with ok button and a question icon
							MessageBox dialog =
							        new MessageBox(shlUtdLibraryMIS, SWT.ICON_QUESTION | SWT.OK);
							dialog.setText("No Data");
							dialog.setMessage("No Such Book in the Library!");
							dialog.open();
						}
						else
						{
							// refresh the table
							tbl_chckout.setRedraw(true);
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		});
		btnSearch.setBounds(401, 27, 95, 21);
		btnSearch.setText("Search");
		
		Label lblBorrowerInfo = new Label(composite_checkOut, SWT.NONE);
		lblBorrowerInfo.setBounds(41, 73, 83, 14);
		lblBorrowerInfo.setText("Borrower Info.:");
		
		txt_bkInfo = new Text(composite_checkOut, SWT.BORDER);
		txt_bkInfo.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				if(txt_bkInfo.getText().equalsIgnoreCase(("(required)Isbn, Author, Book Name,etc")))
					txt_bkInfo.setText("");
			}
			@Override
			public void focusLost(FocusEvent arg0) {
				if(txt_bkInfo.getText().equalsIgnoreCase(""))
					txt_bkInfo.setText("(required)Isbn, Author, Book Name,etc");
			}
		});
		txt_bkInfo.setText("(required)Isbn, Author, Book Name,etc");
		txt_bkInfo.setForeground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		txt_bkInfo.setFont(SWTResourceManager.getFont(".Helvetica Neue DeskInterface", 11, SWT.NORMAL));
		txt_bkInfo.setBounds(130, 27, 239, 19);
		
		tbl_chckout = new Table(composite_checkOut, SWT.BORDER);
		tbl_chckout.setLinesVisible(true);
		tbl_chckout.setHeaderVisible(true);
		tbl_chckout.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		tbl_chckout.setBounds(41, 118, 667, 289);
		
		TableColumn tableColumn = new TableColumn(tbl_chckout, SWT.LEFT);
		tableColumn.setWidth(92);
		tableColumn.setText("ISBN");
		
		TableColumn tableColumn_1 = new TableColumn(tbl_chckout, SWT.LEFT);
		tableColumn_1.setWidth(278);
		tableColumn_1.setText("Book Title");
		
		TableColumn tableColumn_2 = new TableColumn(tbl_chckout, SWT.LEFT);
		tableColumn_2.setWidth(207);
		tableColumn_2.setText("Authors");
		
		TableColumn tblclmnCheckOut = new TableColumn(tbl_chckout, SWT.LEFT);
		tblclmnCheckOut.setWidth(88);
		tblclmnCheckOut.setText("Availability");
		
		txtborrowerId = new Text(composite_checkOut, SWT.BORDER);
		txtborrowerId.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				if(txtborrowerId.getText().equalsIgnoreCase(("(required)Borrower ID")))
					txtborrowerId.setText("");
			}
			@Override
			public void focusLost(FocusEvent arg0) {
				if(txtborrowerId.getText().equalsIgnoreCase(""))
					txtborrowerId.setText("(required)Borrower ID");
			}
		});
		txtborrowerId.setText("(required)Borrower ID");
		txtborrowerId.setForeground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		txtborrowerId.setFont(SWTResourceManager.getFont(".Helvetica Neue DeskInterface", 11, SWT.NORMAL));
		txtborrowerId.setBounds(130, 68, 239, 19);
		
		Button btn_chckOut = new Button(composite_checkOut, SWT.BORDER | SWT.FLAT | SWT.CENTER);
		btn_chckOut.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				String brrId = txtborrowerId.getText();
				if(brrId.equals("")||brrId.equalsIgnoreCase("(required)Borrower ID"))
				{
					MessageBox dialog =
					        new MessageBox(shlUtdLibraryMIS, SWT.ICON_QUESTION | SWT.OK);
					dialog.setText("No Data");
					dialog.setMessage("Please Input Borrower's ID !");
					dialog.open();
				}
				else if(!brrId.matches("[0-9]+"))
				{
					// wrong borrower input id
					MessageBox dialog =
					        new MessageBox(shlUtdLibraryMIS, SWT.ICON_QUESTION | SWT.OK);
					dialog.setText("Wrong Data");
					dialog.setMessage("The borrower's ID should be digits only!");
					dialog.open();				
				}
				else if(tbl_chckout.getItemCount()==0)
				{
					// no search result
					MessageBox dialog =
					        new MessageBox(shlUtdLibraryMIS, SWT.ICON_QUESTION | SWT.OK);
					dialog.setText("No Result");
					dialog.setMessage("No Book Search Result !");
					dialog.open();	
				}
				else if(tbl_chckout.getSelectionCount()==0)
				{
					// no selected record
					MessageBox dialog =
					        new MessageBox(shlUtdLibraryMIS, SWT.ICON_QUESTION | SWT.OK);
					dialog.setText("No Selection");
					dialog.setMessage("Please Select the Book to Check Out !");
					dialog.open();					
				}
				else
				{
					BtnChckOutHandler btnChckOutHandler = new BtnChckOutHandler(tbl_chckout,brrId);
					int rtnValue = btnChckOutHandler.checkOut();
					if(rtnValue==0)
					{	
						DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
						LocalDate rtnDate = LocalDate.now();
						rtnDate.plusDays(14);
						
						// success
						MessageBox dialog =
						        new MessageBox(shlUtdLibraryMIS, SWT.ICON_QUESTION | SWT.OK);
						dialog.setText("Success");
						dialog.setMessage("Book Checked Out Successfully! The Return Date is: " + dtf.format(rtnDate));
						dialog.open();
					}
					else if(rtnValue==1)
					{
						// book checked out already, fail
						MessageBox dialog =
						        new MessageBox(shlUtdLibraryMIS, SWT.ICON_QUESTION | SWT.OK);
						dialog.setText("Fail");
						dialog.setMessage("Book Checked Out Already!");
						dialog.open();
					}
					else if(rtnValue==-1)
					{
						// already has 3 book loans
						MessageBox dialog =
						        new MessageBox(shlUtdLibraryMIS, SWT.ICON_QUESTION | SWT.OK);
						dialog.setText("Fail");
						dialog.setMessage("There are 3 books loaned to the borrower already !");
						dialog.open();
					}
				}				
			}
		});
		btn_chckOut.setText("Check Out");
		btn_chckOut.setBounds(401, 66, 95, 21);
		
		CTabItem tbtmCheckIn = new CTabItem(tabFolder_bookLoan, SWT.NONE);
		tbtmCheckIn.setText("Check In");
		
		Composite composite_checkIn = new Composite(tabFolder_bookLoan, SWT.NONE);
		composite_checkIn.setLayout(null);
		tbtmCheckIn.setControl(composite_checkIn);
		
		Label lblReturnInfo = new Label(composite_checkIn, SWT.NONE);
		lblReturnInfo.setText("Return Info.: ");
		lblReturnInfo.setAlignment(SWT.CENTER);
		lblReturnInfo.setBounds(29, 32, 122, 21);
		
		Button btnCheckInSrch = new Button(composite_checkIn, SWT.BORDER | SWT.FLAT | SWT.CENTER);
		btnCheckInSrch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				String srchStr = txtChckIn.getText();
				
				if(srchStr.equals("")||srchStr.equals("(required)book id,card no.,borrower name.,etc"))
				{
					MessageBox dialog =
					        new MessageBox(shlUtdLibraryMIS, SWT.ICON_QUESTION | SWT.OK);
					dialog.setText("No Data");
					dialog.setMessage("Please Input Search Info. !");
					dialog.open();
				}
				else
				{					
					BtnChckInSrchHandler btnChckInSrchHandler = new BtnChckInSrchHandler(tbl_chckin,srchStr);
					try {
						if(!btnChckInSrchHandler.search())
						{
							// no result
							// create a dialog with ok button and a question icon
							MessageBox dialog =
							        new MessageBox(shlUtdLibraryMIS, SWT.ICON_QUESTION | SWT.OK);
							dialog.setText("No Data");
							dialog.setMessage("No Checked Out Books Related !");
							dialog.open();
						}
						else
						{
							// refresh the table
							tbl_chckin.setRedraw(true);
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		});
		btnCheckInSrch.setText("Search");
		btnCheckInSrch.setBounds(397, 27, 95, 21);
		
		txtChckIn = new Text(composite_checkIn, SWT.BORDER);
		txtChckIn.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				if(txtChckIn.getText().equalsIgnoreCase(("(required)book id,card no.,borrower name.,etc")))
						txtChckIn.setText("");
			}
			@Override
			public void focusLost(FocusEvent arg0) {
				if(txtChckIn.getText().equalsIgnoreCase(""))
					txtChckIn.setText("(required)book id,card no.,borrower name.,etc");
			}
		});
		txtChckIn.setText("(required)book id,card no.,borrower name.,etc");
		txtChckIn.setForeground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		txtChckIn.setFont(SWTResourceManager.getFont(".Helvetica Neue DeskInterface", 11, SWT.NORMAL));
		txtChckIn.setBounds(138, 29, 221, 19);
		
		tbl_chckin = new Table(composite_checkIn, SWT.BORDER);
		tbl_chckin.setLinesVisible(true);
		tbl_chckin.setHeaderVisible(true);
		tbl_chckin.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		tbl_chckin.setBounds(57, 94, 335, 289);
		
		TableColumn tblclmnISBN = new TableColumn(tbl_chckin, SWT.LEFT);
		tblclmnISBN.setWidth(92);
		tblclmnISBN.setText("ISBN");
		
		TableColumn tblclmnBorrowerId = new TableColumn(tbl_chckin, SWT.LEFT);
		tblclmnBorrowerId.setWidth(130);
		tblclmnBorrowerId.setText("Borrower ID");
		
		TableColumn tblclmnBorrowerName = new TableColumn(tbl_chckin, SWT.LEFT);
		tblclmnBorrowerName.setWidth(111);
		tblclmnBorrowerName.setText("Borrower Name");
		
		Button btn_chckin = new Button(composite_checkIn, SWT.BORDER | SWT.FLAT | SWT.CENTER);
		btn_chckin.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				if(tbl_chckin.getSelectionCount()==0)
				{
					// no selected record
					MessageBox dialog =
					        new MessageBox(shlUtdLibraryMIS, SWT.ICON_QUESTION | SWT.OK);
					dialog.setText("No Selection");
					dialog.setMessage("Please Select the Record to Check In !");
					dialog.open();					
				}
				else
				{					
					BtnChckInHandler btnChckInHandler = new BtnChckInHandler(tbl_chckin);
					// check in success
					if(btnChckInHandler.checkIn()==0)
					{
						// no search result
						MessageBox dialog =
						        new MessageBox(shlUtdLibraryMIS, SWT.ICON_QUESTION | SWT.OK);
						dialog.setText("Success");
						dialog.setMessage("Book Check In Success !");
						dialog.open();
					}
				}
			}
		});
		btn_chckin.setText("Check In");
		btn_chckin.setBounds(550, 27, 95, 21);
		
		CTabItem tbtmBorrowerMgnt = new CTabItem(tabFolder, SWT.NONE);
		tbtmBorrowerMgnt.setText("Borrower Mgnt");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		composite.setLayout(null);
		tbtmBorrowerMgnt.setControl(composite);
		
		txtSsn = new Text(composite, SWT.BORDER);
		txtSsn.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				if(txtSsn.getText().equalsIgnoreCase("(required)000-00-0000"))
					txtSsn.setText("");
			}
			@Override
			public void focusLost(FocusEvent arg0) {
				if(txtSsn.getText().equalsIgnoreCase(""))
					txtSsn.setText("(required)000-00-0000");
			}
		});
		txtSsn.setText("(required)000-00-0000");
		txtSsn.setForeground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		txtSsn.setFont(SWTResourceManager.getFont(".Helvetica Neue DeskInterface", 11, SWT.NORMAL));
		txtSsn.setBounds(112, 35, 123, 19);
		
		Button btnCreate = new Button(composite, SWT.BORDER | SWT.FLAT | SWT.CENTER);
		btnCreate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				String ssn=txtSsn.getText();
				String fName=txtFName.getText();
				String lName=txtLName.getText();

				String phone=txtPhone.getText();
				String email = txtEmail.getText();
				String city = txtCity.getText();
				if(city.equalsIgnoreCase("city")) city = "";
				String state = txtState.getText();
				String address=txtAddress.getText();
				if(!city.equals("")) address = address +"," + city;
				if(!state.equals("")) address = address +"," + state;

				if(email.equalsIgnoreCase("xxx@xxx.xxx")) email="";

				if(phone.equalsIgnoreCase("(000) 000-0000")) phone="";
				
				// check for all texts
				if(txtSsn.getText().equalsIgnoreCase("")||txtSsn.getText().equalsIgnoreCase("(required)000-00-0000"))
				{
					// required SSN
					MessageBox dialog =
					        new MessageBox(shlUtdLibraryMIS, SWT.ICON_QUESTION | SWT.OK);
					dialog.setText("Require SSN");
					dialog.setMessage("SSN Cannot Be Empty!");
					dialog.open();	
				}
				
				else if(!txtSsn.getText().matches("^(\\d{3}-\\d{2}-\\d{4})$"))
				{
					// wrong SSN
					MessageBox dialog =
					        new MessageBox(shlUtdLibraryMIS, SWT.ICON_QUESTION | SWT.OK);
					dialog.setText("Wrong SSN");
					dialog.setMessage("The SSN should be 9 digits with format 000-00-0000!");
					dialog.open();				
				}
				
				// FName
				else if(txtFName.getText().equalsIgnoreCase("")||txtFName.getText().equalsIgnoreCase("(required)"))
				{
					// required FName
					MessageBox dialog =
					        new MessageBox(shlUtdLibraryMIS, SWT.ICON_QUESTION | SWT.OK);
					dialog.setText("Require First Name");
					dialog.setMessage("First Name Cannot Be Empty!");
					dialog.open();	
				}
				
				else if(!txtFName.getText().matches("^[a-zA-Z]*$"))
				{
					// only alphabet allowed
					MessageBox dialog =
					        new MessageBox(shlUtdLibraryMIS, SWT.ICON_QUESTION | SWT.OK);
					dialog.setText("Wrong First Name");
					dialog.setMessage("Only Alphabet Characters Allowed !");
					dialog.open();				
				}
				
				// LName
				else if(txtLName.getText().equalsIgnoreCase("")||txtLName.getText().equalsIgnoreCase("(required)"))
				{
					// required FName
					MessageBox dialog =
					        new MessageBox(shlUtdLibraryMIS, SWT.ICON_QUESTION | SWT.OK);
					dialog.setText("Require Last Name");
					dialog.setMessage("Last Name Cannot Be Empty!");
					dialog.open();	
				}
				
				else if(!txtLName.getText().matches("^[a-zA-Z]*$"))
				{
					// only alphabet allowed
					MessageBox dialog =
					        new MessageBox(shlUtdLibraryMIS, SWT.ICON_QUESTION | SWT.OK);
					dialog.setText("Wrong Last Name");
					dialog.setMessage("Only Alphabet Characters Allowed !");
					dialog.open();				
				}
				
				else if(!txtEmail.getText().matches("^[a-zA-Z]*@[a-zA-Z]*.{1}[a-zA-Z]*$"))
				{
					// email not qualified
					MessageBox dialog =
					        new MessageBox(shlUtdLibraryMIS, SWT.ICON_QUESTION | SWT.OK);
					dialog.setText("Wrong Email Format");
					dialog.setMessage("Please Input Email with Format: xxx@xxx.xxx !");
					dialog.open();				
				}
				
				else if(!txtAddress.getText().matches("^[a-zA-Z]*$"))
				{
					// only alphabet allowed
					MessageBox dialog =
					        new MessageBox(shlUtdLibraryMIS, SWT.ICON_QUESTION | SWT.OK);
					dialog.setText("Wrong Address");
					dialog.setMessage("Only Alphabet Characters Allowed !");
					dialog.open();				
				}
				
				else if(!txtCity.getText().matches("^[a-zA-Z]*$"))
				{
					// only alphabet allowed
					MessageBox dialog =
					        new MessageBox(shlUtdLibraryMIS, SWT.ICON_QUESTION | SWT.OK);
					dialog.setText("Wrong City");
					dialog.setMessage("Only Alphabet Characters Allowed !");
					dialog.open();				
				}				
				else if(!txtState.getText().matches("^[a-zA-Z]*$"))
				{
					// only alphabet allowed
					MessageBox dialog =
					        new MessageBox(shlUtdLibraryMIS, SWT.ICON_QUESTION | SWT.OK);
					dialog.setText("Wrong State");
					dialog.setMessage("Only Alphabet Characters Allowed !");
					dialog.open();				
				}			
				else if(!txtPhone.getText().matches("^(\\(\\d{3}\\)\\s\\d{3}-\\d{4})$"))
				{
					// wrong Phone
					MessageBox dialog =
					        new MessageBox(shlUtdLibraryMIS, SWT.ICON_QUESTION | SWT.OK);
					dialog.setText("Wrong Phone");
					dialog.setMessage("The Phone should be 10 digits with format (000) 000-0000!");
					dialog.open();				
				}
				else 
				{				
					// create new borrower record
					HashMap<String,String> map = new HashMap<String,String>();
					map.put("Ssn", ssn);
					map.put("FName", fName);
					map.put("LName", lName);
					map.put("Address", address);
					map.put("Phone", phone);
					map.put("Email", email);

					
					BtnBrrMgntCreateHandler btnBrrMgntCreateHandler = new BtnBrrMgntCreateHandler(map);
					String card_id = btnBrrMgntCreateHandler.create();
					
					if(card_id.equalsIgnoreCase("repeatedSsn"))
					{
						// repeated SSN
						MessageBox dialog =
						        new MessageBox(shlUtdLibraryMIS, SWT.ICON_QUESTION | SWT.OK);
						dialog.setText("Repeated SSN");
						dialog.setMessage("There is already borrower info. with this SSN !");
						dialog.open();	
					}
					else if(!card_id.equalsIgnoreCase(""))
					{
						// success
						MessageBox dialog =
						        new MessageBox(shlUtdLibraryMIS, SWT.ICON_QUESTION | SWT.OK);
						dialog.setText("Success");
						dialog.setMessage("The Card ID is: " + card_id + "!");
						dialog.open();	
					}
				}		
			}
		});
		btnCreate.setText("Create");
		btnCreate.setBounds(140, 346, 95, 19);
		
		Label lblSsn = new Label(composite, SWT.NONE);
		lblSsn.setText("SSN:");
		lblSsn.setAlignment(SWT.RIGHT);
		lblSsn.setBounds(30, 39, 60, 19);
		
		txtFName = new Text(composite, SWT.BORDER);
		txtFName.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				if(txtFName.getText().equalsIgnoreCase("(required)"))
					txtFName.setText("");
			}
			@Override
			public void focusLost(FocusEvent arg0) {
				if(txtFName.getText().equalsIgnoreCase(""))
					txtFName.setText("(required)");
			}
		});
		txtFName.setText("(required)");
		txtFName.setForeground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		txtFName.setFont(SWTResourceManager.getFont(".Helvetica Neue DeskInterface", 11, SWT.NORMAL));
		txtFName.setBounds(112, 69, 123, 19);
		
		Label lblFirstName = new Label(composite, SWT.NONE);
		lblFirstName.setText("First Name:");
		lblFirstName.setAlignment(SWT.RIGHT);
		lblFirstName.setBounds(10, 73, 80, 19);
		
		txtLName = new Text(composite, SWT.BORDER);
		txtLName.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				if(txtLName.getText().equalsIgnoreCase("(required)"))
					txtLName.setText("");
			}
			@Override
			public void focusLost(FocusEvent arg0) {
				if(txtLName.getText().equalsIgnoreCase(""))
					txtLName.setText("(required)");
			}
		});
		txtLName.setToolTipText("");
		txtLName.setText("(required)");
		txtLName.setForeground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		txtLName.setFont(SWTResourceManager.getFont(".Helvetica Neue DeskInterface", 11, SWT.NORMAL));
		txtLName.setBounds(112, 103, 123, 19);
		
		Label lblLastName = new Label(composite, SWT.NONE);
		lblLastName.setText("Last Name:");
		lblLastName.setAlignment(SWT.RIGHT);
		lblLastName.setBounds(10, 107, 80, 19);
		
		txtEmail = new Text(composite, SWT.BORDER);
		txtEmail.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				if(txtEmail.getText().equalsIgnoreCase("xxx@xxx.xxx"))
					txtEmail.setText("");
			}
			@Override
			public void focusLost(FocusEvent arg0) {
				if(txtEmail.getText().equalsIgnoreCase(""))
					txtEmail.setText("xxx@xxx.xxx");
			}
		});
		txtEmail.setText("xxx@xxx.xxx");
		txtEmail.setForeground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		txtEmail.setFont(SWTResourceManager.getFont(".Helvetica Neue DeskInterface", 11, SWT.NORMAL));
		txtEmail.setBounds(112, 141, 123, 19);
		
		Label lblEmail = new Label(composite, SWT.NONE);
		lblEmail.setText("Email:");
		lblEmail.setAlignment(SWT.RIGHT);
		lblEmail.setBounds(30, 145, 60, 19);
		
		txtAddress = new Text(composite, SWT.BORDER);
		txtAddress.setForeground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		txtAddress.setFont(SWTResourceManager.getFont(".Helvetica Neue DeskInterface", 11, SWT.NORMAL));
		txtAddress.setBounds(112, 180, 123, 19);
		
		Label lblAddress = new Label(composite, SWT.NONE);
		lblAddress.setText("Address:");
		lblAddress.setAlignment(SWT.RIGHT);
		lblAddress.setBounds(30, 184, 60, 19);
		
		txtCity = new Text(composite, SWT.BORDER);
		txtCity.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				if(txtCity.getText().equalsIgnoreCase("city"))
					txtCity.setText("");
			}
			@Override
			public void focusLost(FocusEvent arg0) {
				if(txtCity.getText().equalsIgnoreCase(""))
					txtCity.setText("city");
			}
		});
		txtCity.setText("city");
		txtCity.setForeground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		txtCity.setFont(SWTResourceManager.getFont(".Helvetica Neue DeskInterface", 11, SWT.NORMAL));
		txtCity.setBounds(112, 219, 123, 19);
		
		Label lblCity = new Label(composite, SWT.NONE);
		lblCity.setText("City:");
		lblCity.setAlignment(SWT.RIGHT);
		lblCity.setBounds(30, 223, 60, 19);
		
		txtPhone = new Text(composite, SWT.BORDER);
		txtPhone.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				if(txtPhone.getText().equalsIgnoreCase("(000) 000-0000"))
					txtPhone.setText("");
			}
			@Override
			public void focusLost(FocusEvent arg0) {
				if(txtPhone.getText().equalsIgnoreCase(""))
					txtPhone.setText("(000) 000-0000");
			}
		});
		txtPhone.setToolTipText("");
		txtPhone.setText("(000) 000-0000");
		txtPhone.setForeground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		txtPhone.setFont(SWTResourceManager.getFont(".Helvetica Neue DeskInterface", 11, SWT.NORMAL));
		txtPhone.setBounds(112, 292, 123, 19);
		
		Label lblPhone = new Label(composite, SWT.NONE);
		lblPhone.setText("Phone:");
		lblPhone.setAlignment(SWT.RIGHT);
		lblPhone.setBounds(30, 296, 60, 19);
		
		Label lblState = new Label(composite, SWT.NONE);
		lblState.setText("State:");
		lblState.setAlignment(SWT.RIGHT);
		lblState.setBounds(30, 263, 60, 19);
		
		txtState = new Text(composite, SWT.BORDER);
		txtState.setForeground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		txtState.setFont(SWTResourceManager.getFont(".Helvetica Neue DeskInterface", 11, SWT.NORMAL));
		txtState.setBounds(112, 260, 123, 19);
		
		CTabItem tbtmFines = new CTabItem(tabFolder, SWT.NONE);
		tbtmFines.setText("Fines");
		
		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		composite_1.setLayout(null);
		tbtmFines.setControl(composite_1);
		
		txtFine = new Text(composite_1, SWT.BORDER);
		txtFine.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				if(txtFine.getText().equalsIgnoreCase("Name, Card ID, etc"))
					txtFine.setText("");
			}
			@Override
			public void focusLost(FocusEvent arg0) {
				if(txtFine.getText().equalsIgnoreCase(""))
					txtFine.setText("Name, Card ID, etc");
			}
		});
		txtFine.setText("Name, Card ID, etc");
		txtFine.setToolTipText("");
		txtFine.setForeground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		txtFine.setFont(SWTResourceManager.getFont(".Helvetica Neue DeskInterface", 11, SWT.NORMAL));
		txtFine.setBounds(112, 60, 162, 19);
		
		Button btnFine = new Button(composite_1, SWT.BORDER | SWT.FLAT | SWT.CENTER);
		btnFine.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				String srchStr = txtFine.getText();
				
				if(srchStr.equalsIgnoreCase("")||srchStr.equalsIgnoreCase("Name, Card ID, etc"))
				{
					MessageBox dialog =
					        new MessageBox(shlUtdLibraryMIS, SWT.ICON_QUESTION | SWT.OK);
					dialog.setText("No Info.");
					dialog.setMessage("Please Input Search Info.!");
					dialog.open();	
				}
				else
				{					
					BtnFineSearchHandler btnFineSearchHandler = new BtnFineSearchHandler(tbl_fine,srchStr);
					btnFineSearchHandler.search();
					tbl_fine.setRedraw(true);
				}
			}
		});
		btnFine.setText("Search");
		btnFine.setBounds(300, 59, 95, 19);
		
		Label lblBorrowInfo = new Label(composite_1, SWT.NONE);
		lblBorrowInfo.setText("Borrow Info.:");
		lblBorrowInfo.setAlignment(SWT.CENTER);
		lblBorrowInfo.setBounds(18, 60, 88, 19);
		
		txtPayFine = new Text(composite_1, SWT.BORDER);
		txtPayFine.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				if(txtPayFine.getText().equalsIgnoreCase("USD"))
					txtPayFine.setText("");
			}
			@Override
			public void focusLost(FocusEvent arg0) {
				if(txtPayFine.getText().equalsIgnoreCase(""))
					txtPayFine.setText("USD");
			}
		});
		txtPayFine.setToolTipText("");
		txtPayFine.setText("USD");
		txtPayFine.setForeground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		txtPayFine.setFont(SWTResourceManager.getFont(".Helvetica Neue DeskInterface", 11, SWT.NORMAL));
		txtPayFine.setBounds(112, 99, 162, 19);
		
		Label lblPayFine = new Label(composite_1, SWT.NONE);
		lblPayFine.setText("Pay Fine:");
		lblPayFine.setAlignment(SWT.CENTER);
		lblPayFine.setBounds(18, 102, 88, 19);
		
		Button btnPay = new Button(composite_1, SWT.BORDER | SWT.FLAT | SWT.CENTER);
		btnPay.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				String amount = txtPayFine.getText();
				if(amount.equalsIgnoreCase("")||amount.equalsIgnoreCase("USD"))
				{
					MessageBox dialog =
					        new MessageBox(shlUtdLibraryMIS, SWT.ICON_QUESTION | SWT.OK);
					dialog.setText("No Info.");
					dialog.setMessage("Please Input Fines Paid!");
					dialog.open();
				}
				else if(!amount.matches("[0-9.]+"))
				{
					MessageBox dialog =
					        new MessageBox(shlUtdLibraryMIS, SWT.ICON_QUESTION | SWT.OK);
					dialog.setText("Wrong Input");
					dialog.setMessage("Please Input Digits Only!");
					dialog.open();
				}
				else if(tbl_fine.getSelectionCount()==0)
				{
					MessageBox dialog =
					        new MessageBox(shlUtdLibraryMIS, SWT.ICON_QUESTION | SWT.OK);
					dialog.setText("No Selection");
					dialog.setMessage("Please Select a Record!");
					dialog.open();
				}
				else
				{
					TableItem[] selected = tbl_fine.getSelection();
					// no fines
					if(Double.parseDouble(selected[0].getText(1))<=0.0)
					{
						MessageBox dialog =
						        new MessageBox(shlUtdLibraryMIS, SWT.ICON_QUESTION | SWT.OK);
						dialog.setText("No Fine");
						dialog.setMessage("There is No Fine with This Borrower!");
						dialog.open();
					}
					else
					{
						BtnPayHandler btnPayHandler = new BtnPayHandler(tbl_fine,amount);
						int success = btnPayHandler.update();
						if(success==0)
						{
							MessageBox dialog =
							        new MessageBox(shlUtdLibraryMIS, SWT.ICON_QUESTION | SWT.OK);
							dialog.setText("Success");
							dialog.setMessage("Fine Update Success!");
							dialog.open();
							tbl_fine.setRedraw(true);
						}
						else
						{
							MessageBox dialog =
							        new MessageBox(shlUtdLibraryMIS, SWT.ICON_QUESTION | SWT.OK);
							dialog.setText("Fail");
							dialog.setMessage("Fine Update Failed!");
							dialog.open();
						}
					}
				}			
			}
		});
		btnPay.setText("Pay");
		btnPay.setBounds(300, 98, 95, 19);
		
		tbl_fine = new Table(composite_1, SWT.BORDER);
		tbl_fine.setLinesVisible(true);
		tbl_fine.setHeaderVisible(true);
		tbl_fine.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		tbl_fine.setBounds(23, 141, 335, 289);
		
		TableColumn tblclmnBorrowerId_1 = new TableColumn(tbl_fine, SWT.LEFT);
		tblclmnBorrowerId_1.setWidth(92);
		tblclmnBorrowerId_1.setText("Borrower ID");
		
		TableColumn tblclmnFines = new TableColumn(tbl_fine, SWT.LEFT);
		tblclmnFines.setWidth(130);
		tblclmnFines.setText("Fine Amount");
		
		TableColumn tableColumn_5 = new TableColumn(tbl_fine, SWT.LEFT);
		tableColumn_5.setWidth(111);
		tableColumn_5.setText("Borrower Name");
		
		Button btnUpdateFine = new Button(composite_1, SWT.BORDER | SWT.FLAT | SWT.CENTER);
		btnUpdateFine.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				BtnUpdateFinesHandler btnUpdateFinesHandler = new BtnUpdateFinesHandler();
				boolean success = btnUpdateFinesHandler.update();
				
				if(success)
				{
					MessageBox dialog =
					        new MessageBox(shlUtdLibraryMIS, SWT.ICON_QUESTION | SWT.OK);
					dialog.setText("Success");
					dialog.setMessage("Fines Update Success!");
					dialog.open();	
				}
				else
				{
					MessageBox dialog =
					        new MessageBox(shlUtdLibraryMIS, SWT.ICON_QUESTION | SWT.OK);
					dialog.setText("Fail");
					dialog.setMessage("Fines Update Fail!");
					dialog.open();	
				}				
			}
		});
		btnUpdateFine.setText("Update Fines");
		btnUpdateFine.setBounds(300, 20, 95, 19);
	}
}
