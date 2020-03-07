package business;

import java.util.List;

import business.Book;
import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;

public interface ControllerInterface {
	public void login(String id, String password) throws LoginException;
	
	public List<String> allBookIds();

	
	public List<LibraryMember> allMembers();
	public List<Book> allBooks();

	
	public List<String> allMemberIds();
	public void addMember(LibraryMember member)  throws AlreadyExistException;
	public void deleteMember(String memberId);
	public void updateMember(LibraryMember member);

	
	public void addBook(Book book) throws AlreadyExistException;
	public void deleteBook(String isbn);
	public void updateBook(Book book);
	

	public void addAuthor(Author author)  throws AlreadyExistException;
	public void deleteAuthor(String id);
	public void updateAuthor(Author author);
	public List<Author> allAuthors();
	
	
	public void addBookCopy(BookCopy bookCopy)  throws AlreadyExistException;
	public void deleteBookCopy(String id);
	public void updateBookCopy(BookCopy bookCopy);
	public List<BookCopy> allBookCopies();

	public List<Checkout> allCheckouts();
	public void addCheckout(Checkout checkou)  throws AlreadyExistException;
	public void updateCheckout(Checkout checkout);
	public void deleteCheckout(String id);
}
