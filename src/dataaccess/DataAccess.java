package dataaccess;

import java.util.HashMap;

import business.Author;
import business.Book;
import business.LibraryMember;

public interface DataAccess { 
	public HashMap<String,Book> readBooksMap();
	public HashMap<String,User> readUserMap();
	public HashMap<String, LibraryMember> readMemberMap();
	public HashMap<String, Author> readAuthorsMap();
	
	public boolean saveNewMember(LibraryMember member); 
	public void deleteMember(String memberId);
	public void updateMember(LibraryMember member);
	
	public boolean saveNewBook(Book book);
	public void deleteBook(String isbn);
	public void updateBook(Book book);
	
	public boolean saveNewAuthor(Author author);
	public void deleteAuthor(String authorId);
	public void updateAuthor(Author author);
}
