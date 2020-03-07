package dataaccess;

import java.util.HashMap;
import java.util.List;

import business.*;

public interface DataAccess { 
	public HashMap<String,Book> readBooksMap();
	
	public HashMap<String,User> readUserMap();
	
	public HashMap<String, LibraryMember> readMemberMap();
	
	public boolean saveNewMember(LibraryMember member); 
	public void deleteMember(String memberId);
	public void updateMember(LibraryMember member);
	

	public boolean saveNewBook(Book book);
	public void deleteBook(String isbn);
	public void updateBook(Book book);
	
	public boolean saveNewAuthor(Author author);
	public void deleteAuthor(String authorId);
	public void updateAuthor(Author author);
	public HashMap<String, Author> readAuthorsMap();
	
	
	public boolean saveNewBookCopy(BookCopy bookCopy);
	public void deleteBookCopy(String id);
	public void updateBookCopy(BookCopy bookCopy);
	public HashMap<String, BookCopy> readBookCopiesMap();
	
	public boolean saveNewCheckout(Checkout checkout);
	public void deleteCheckout(String id);
	public void updateCheckout(Checkout checkout);
	public HashMap<String, Checkout> readCheckoutsMap();
}
