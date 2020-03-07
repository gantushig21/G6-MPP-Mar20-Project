package business;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import dataaccess.Auth;
import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;
import dataaccess.User;

public class SystemController implements ControllerInterface {
	public static Auth currentAuth = null;
	
	public void login(String id, String password) throws LoginException {
		DataAccess da = new DataAccessFacade();
		HashMap<String, User> map = da.readUserMap();
		if(!map.containsKey(id)) {
			throw new LoginException("ID " + id + " not found");
		}
		String passwordFound = map.get(id).getPassword();
		if(!passwordFound.equals(password)) {
			throw new LoginException("Password incorrect");
		}
		currentAuth = map.get(id).getAuthorization();
		
	}
	@Override
	public List<String> allMemberIds() {
		DataAccess da = new DataAccessFacade();
		List<String> retval = new ArrayList<>();
		retval.addAll(da.readMemberMap().keySet());
		return retval;
	}
	
	@Override
	public List<String> allBookIds() {
		DataAccess da = new DataAccessFacade();
		List<String> retval = new ArrayList<>();
		retval.addAll(da.readBooksMap().keySet());
		return retval;
	}
	
	@Override
	public List<LibraryMember> allMembers() {
		DataAccess da = new DataAccessFacade();
		
		Collection<LibraryMember> members = da.readMemberMap().values();
		
		
		return new ArrayList<LibraryMember>(members);
	}
	
	@Override
	public List<Book> allBooks() {
		DataAccess da = new DataAccessFacade();
		
		return new ArrayList<Book>(da.readBooksMap().values());
	}
//	@Override
//	public List<Author> allAuthors() {
//		DataAccess da = new DataAccessFacade();
//		
//		return new ArrayList<Author>(da.readAuthorsMap().values());
//	}	
	
	@Override
	public void addMember(LibraryMember member) throws AlreadyExistException {
		DataAccess da = new DataAccessFacade();
		boolean result = da.saveNewMember(member);		
		if (!result)
			throw new AlreadyExistException("The member with this ID already exists!");
	}
	@Override
	public void deleteMember(String memberId) {
		DataAccess da = new DataAccessFacade();
		da.deleteMember(memberId);				
	}
	@Override
	public void updateMember(LibraryMember member) {
		DataAccess da = new DataAccessFacade();
		da.updateMember(member);				
	}
	@Override
	public void addBook(Book book) throws AlreadyExistException {
		DataAccess da = new DataAccessFacade();

		boolean result = da.saveNewBook(book);
		
		if (!result)
			throw new AlreadyExistException("The book with this isbn already exists");
		
	}
	@Override
	public void deleteBook(String isbn) {
		DataAccess da = new DataAccessFacade();
		
		da.deleteBook(isbn);
	}
	@Override
	public void updateBook(Book book) {
		DataAccess da = new DataAccessFacade();
		
		da.updateBook(book);
	}
//	@Override
//	public void addAuthor(Author author) throws AlreadyExistException {
//		DataAccess da = new DataAccessFacade();
//		
//		boolean result = da.saveNewAuthor(author);
//		
//		if (!result)
//			throw new AlreadyExistException("The author with this id already exists");
//	}
//	@Override
//	public void deleteAuthor(String authorId) {
//		DataAccess da = new DataAccessFacade();
//		
//		da.deleteAuthor(authorId);
//	}
//	@Override
//	public void updateBook(Author author) {
//		DataAccess da = new DataAccessFacade();
//		
//		da.updateAuthor(author);
//	}	
	
	
	// AUTHOR START
	@Override
	public List<Author> allAuthors() {
		DataAccess da = new DataAccessFacade();
		Collection<Author> authors = da.readAuthorsMap().values();
		return new ArrayList<Author>(authors);
	}
	
	@Override
	public void addAuthor(Author author) throws AlreadyExistException {
		DataAccess da = new DataAccessFacade();
		boolean result = da.saveNewAuthor(author);		
		if (!result)
			throw new AlreadyExistException("The author with this ID is already exists!");
	}
	@Override
	public void deleteAuthor(String id) {
		DataAccess da = new DataAccessFacade();
		da.deleteAuthor(id);				
	}
	@Override
	public void updateAuthor(Author author) {
		DataAccess da = new DataAccessFacade();
		da.updateAuthor(author);				
	}	
	// AUTHOR END
	

	// BOOKCOPY START
	@Override
	public List<BookCopy> allBookCopies() {
		DataAccess da = new DataAccessFacade();
		Collection<BookCopy> bookCopies = da.readBookCopiesMap().values();
		return new ArrayList<BookCopy>(bookCopies);
	}
	
	@Override
	public void addBookCopy(BookCopy bookCopy) throws AlreadyExistException {
		DataAccess da = new DataAccessFacade();
		boolean result = da.saveNewBookCopy(bookCopy);		
		if (!result)
			throw new AlreadyExistException("The bookCopy with this ID is already exists!");
	}
	@Override
	public void deleteBookCopy(String id) {
		DataAccess da = new DataAccessFacade();
		da.deleteBookCopy(id);				
	}
	@Override
	public void updateBookCopy(BookCopy bookCopy) {
		DataAccess da = new DataAccessFacade();
		da.updateBookCopy(bookCopy);				
	}	
	// BOOKCOPY END
	
	// CHECKOUT START
	@Override
	public List<Checkout> allCheckouts() {
		DataAccess da = new DataAccessFacade();
		Collection<Checkout> checkouts = da.readCheckoutsMap().values();
		return new ArrayList<Checkout>(checkouts);
	}
	
	@Override
	public void addCheckout(Checkout checkout)  throws AlreadyExistException {
		DataAccess da = new DataAccessFacade();
		boolean result = da.saveNewCheckout(checkout);		
		if (!result)
			throw new AlreadyExistException("The checkout with this ID is already exists!");
	}
	@Override
	public void updateCheckout(Checkout checkout) {
		DataAccess da = new DataAccessFacade();
		da.updateCheckout(checkout);		
	}
	@Override
	public void deleteCheckout(String id) {
		DataAccess da = new DataAccessFacade();
		da.deleteCheckout(id);						
	}	
	// CHECKOUT END
}
