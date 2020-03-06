package dataaccess;

import java.util.HashMap;

import business.Author;
import business.Book;
import business.LibraryMember;
import dataaccess.DataAccessFacade.StorageType;

public interface DataAccess { 
	public HashMap<String,Book> readBooksMap();
	
	public HashMap<String,User> readUserMap();
	
	public HashMap<String, LibraryMember> readMemberMap();
	public boolean saveNewMember(LibraryMember member); 
	public void deleteMember(String memberId);
	public void updateMember(LibraryMember member);
	
	public HashMap<String, Author> readAuthorMap();
	public boolean saveNewAuthor(Author uthor); 
	public void deleteAuthor(String id);
	public void updateAuthor(Author author);
}
