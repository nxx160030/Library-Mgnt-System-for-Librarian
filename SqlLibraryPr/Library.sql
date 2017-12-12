DROP DATABASE IF EXISTS Library;
CREATE DATABASE Library;

USE Library;

DROP TABLE IF EXISTS BOOK CASCADE;
CREATE TABLE BOOK (
  Isbn        char(10) not null,
  Title        varchar(400) not null,
  CONSTRAINT pk_book primary key (Isbn)
);

DROP TABLE IF EXISTS AUTHORS CASCADE;
CREATE TABLE AUTHORS (
  Author_id     int not null,
  Name           varchar(50) not null,
  CONSTRAINT pk_authors primary key (Author_id),
  CONSTRAINT Author_id unique (Name )
);

DROP TABLE IF EXISTS BOOK_AUTHORS CASCADE; 
CREATE TABLE BOOK_AUTHORS (
  Author_id     int not null,
  Isbn             char(10),
  CONSTRAINT pk_authors primary key (Author_id,Isbn),
  CONSTRAINT fk_book_authors_book foreign key (Isbn) references BOOK(Isbn),
  CONSTRAINT fk_book_authors_authors foreign key (Author_id) references AUTHORS(Author_id)
);

DROP TABLE IF EXISTS BORROWER CASCADE;
CREATE TABLE BORROWER (
  Card_id     int(6) not null auto_increment,
  Ssn           char(11),
  Bname      varchar(30),
  Address    varchar(50),
  Phone       char(14),
  CONSTRAINT pk_borrower primary key (Card_id)
);

DROP TABLE IF EXISTS BOOK_LOANS CASCADE;
CREATE TABLE BOOK_LOANS (
  Loan_id     int not null auto_increment,
  Isbn           char(10) not null unique,
  Card_id     int(6) not null,
  Date_out   char(10) not null,
  Due_date  char(10) not null,
  Date_in     char(10),  
  CONSTRAINT pk_book_loans primary key (Loan_id),
  CONSTRAINT fk_book_loans_book foreign key (Isbn) references BOOK(Isbn),
  CONSTRAINT fk_book_loans_borrower foreign key (Card_id) references BORROWER(Card_id)
);

DROP TABLE IF EXISTS FINES CASCADE;
CREATE TABLE FINES (
  Loan_id     int,
  Fine_amt   double (6,2),
  Paid           int,
  CONSTRAINT pk_fines primary key (Loan_id),
  CONSTRAINT fk_fines_book_loans foreign key (Loan_id) references BOOK_LOANS(Loan_id)
);

DROP TABLE IF EXISTS PAID CASCADE;
CREATE TABLE PAID (
	Card_id     int(6) not null,
	Paid_amt   double (6,2) default 0,
    CONSTRAINT pk_paid primary key (Card_id),
    CONSTRAINT fk_paid foreign key (Card_id) references BOOK_LOANS(Card_id)
);


















