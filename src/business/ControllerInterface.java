package business;

import java.util.List;

import business.Book;
import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;

public interface ControllerInterface {
	public void login(String id, String password) throws LoginException;
	
	public List<String> allBookIds();

	
	public List<String> allMemberIds();
	public void addMember(LibraryMember member)  throws AlreadyExistException;
	public void deleteMember(String memberId);
	public void updateMember(LibraryMember member);
	public List<LibraryMember> allMembers();
	
	public void addAuthor(Author author)  throws AlreadyExistException;
	public void deleteAuthor(String id);
	public void updateAuthor(Author author);
	public List<Author> allAuthors();
}
