package dataaccess;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import dataaccess.DataAccessFacade.StorageType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import business.Address;
import business.Author;
import business.Book;
import business.BookCopy;
import business.LibraryMember;


import business.*;

public class DataAccessFacade implements DataAccess {

	enum StorageType {
		BOOKS, MEMBERS, USERS, AUTHORS, BOOKCOPIES, CHECKOUTS;
	}

	public static final String OUTPUT_DIR = System.getProperty("user.dir") + "/src/dataaccess/storage";
	public static final String DATE_PATTERN = "MM/dd/yyyy";

	public void printHashMap(HashMap hm) {
		System.out.println("Deserialized HashMap");
		// Display content using Iterator
		Set set = hm.entrySet();
		Iterator iterator = set.iterator();
		while (iterator.hasNext()) {
			Map.Entry mentry = (Map.Entry) iterator.next();
			System.out.print("key: " + mentry.getKey() + " & Value: ");
			System.out.println(mentry.getValue());
		}
	}

	// implement: other save operations
	public boolean saveNewMember(LibraryMember member) {
		HashMap<String, LibraryMember> mems = readMemberMap();
		String memberId = member.getMemberId();
		if (!mems.containsKey(memberId)) {
			mems.put(memberId, member);
			saveToStorage(StorageType.MEMBERS, mems);
			return true;
		}
		return false;
	}

	public void deleteMember(String memberId) {
		HashMap<String, LibraryMember> mems = readMemberMap();
		mems.toString();
		mems.remove(memberId);
		saveToStorage(StorageType.MEMBERS, mems);
	}

	public void updateMember(LibraryMember member) {
		HashMap<String, LibraryMember> mems = readMemberMap();
		mems.put(member.getMemberId(), member);
		saveToStorage(StorageType.MEMBERS, mems);
	}

	// START BOOK

	public boolean initBooks() {
		try {
			HashMap<String, Book> books = new HashMap();
			Address a = new Address("test", "test", "test", "test");
			Author author = new Author("test", "test", "test", a, "test");
			author.setCredentials("test");
			author.setId("test");

			List<Author> as = new ArrayList<>();
			Book b = new Book("test", "test", 10, as);

			books.put(b.getId(), b);
			saveToStorage(StorageType.BOOKS, books);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		;
		return false;

	}

	public boolean saveNewBook(Book book) {
		HashMap<String, Book> books = readBooksMap();
		String isbn = book.getIsbn();
		if (!books.containsKey(isbn)) {
			books.put(isbn, book);
			saveToStorage(StorageType.BOOKS, books);

			return true;
		}
		return false;
	}

	public void deleteBook(String isbn) {
		HashMap<String, Book> books = readBooksMap();
		books.remove(isbn);
		saveToStorage(StorageType.BOOKS, books);
	}

	public void updateBook(Book book) {
		HashMap<String, Book> books = readBooksMap();
		books.put(book.getIsbn(), book);
		saveToStorage(StorageType.BOOKS, books);
	}

	// END BOOK

	@SuppressWarnings("unchecked")
	public HashMap<String, Author> readAuthorsMap() {
		// Returns a Map with name/value pairs being
		// isbn -> Book
		return (HashMap<String, Author>) readFromStorage(StorageType.AUTHORS);
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Book> readBooksMap() {
		// Returns a Map with name/value pairs being
		// isbn -> Book
		return (HashMap<String, Book>) readFromStorage(StorageType.BOOKS);
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, LibraryMember> readMemberMap() {
		// Returns a Map with name/value pairs being
		// memberId -> LibraryMember
		return (HashMap<String, LibraryMember>) readFromStorage(StorageType.MEMBERS);
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, User> readUserMap() {
		// Returns a Map with name/value pairs being
		// userId -> User
		return (HashMap<String, User>) readFromStorage(StorageType.USERS);
	}

	///// load methods - these place test data into the storage area
	///// - used just once at startup

	static void loadBookMap(List<Book> bookList) {
		HashMap<String, Book> books = new HashMap<String, Book>();
		bookList.forEach(book -> books.put(book.getIsbn(), book));
		saveToStorage(StorageType.BOOKS, books);
	}

	static void loadUserMap(List<User> userList) {
		HashMap<String, User> users = new HashMap<String, User>();
		userList.forEach(user -> users.put(user.getId(), user));
		saveToStorage(StorageType.USERS, users);
	}

	static void loadMemberMap(List<LibraryMember> memberList) {
		HashMap<String, LibraryMember> members = new HashMap<String, LibraryMember>();
		memberList.forEach(member -> members.put(member.getMemberId(), member));
		saveToStorage(StorageType.MEMBERS, members);
	}

	static void saveToStorage(StorageType type, Object ob) {
		ObjectOutputStream out = null;
		try {
			Path path = FileSystems.getDefault().getPath(OUTPUT_DIR, type.toString());
			out = new ObjectOutputStream(Files.newOutputStream(path));
			out.writeObject(ob);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
				}
			}
		}
	}

	static Object readFromStorage(StorageType type) {
		ObjectInputStream in = null;
		Object retVal = null;
		try {
			Path path = FileSystems.getDefault().getPath(OUTPUT_DIR, type.toString());
			in = new ObjectInputStream(Files.newInputStream(path));
			retVal = in.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
		}
		return retVal;
	}

	final static class Pair<S, T> implements Serializable {

		S first;
		T second;

		Pair(S s, T t) {
			first = s;
			second = t;
		}

		@Override
		public boolean equals(Object ob) {
			if (ob == null)
				return false;
			if (this == ob)
				return true;
			if (ob.getClass() != getClass())
				return false;
			@SuppressWarnings("unchecked")
			Pair<S, T> p = (Pair<S, T>) ob;
			return p.first.equals(first) && p.second.equals(second);
		}

		@Override
		public int hashCode() {
			return first.hashCode() + 5 * second.hashCode();
		}

		@Override
		public String toString() {
			return "(" + first.toString() + ", " + second.toString() + ")";
		}

		private static final long serialVersionUID = 5399827794066637059L;
	}

	// AUTHOR START
	public boolean initAuthors() {
		try {
			HashMap<String, Author> authors = new HashMap();
			Address a = new Address("test", "test", "test", "test");
			Author author = new Author("test", "test", "test", a, "test");
			author.setCredentials("test");
			author.setId("test");
			authors.put("test", author);
			saveToStorage(StorageType.AUTHORS, authors);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		;
		return false;
	}

	public boolean saveNewAuthor(Author author) {
		HashMap<String, Author> authors = readAuthorMap();
		String id = author.getId();
		if (!authors.containsKey(id)) {
			authors.put(id, author);
			saveToStorage(StorageType.AUTHORS, authors);

			return true;
		}
		return false;
	}

	public void deleteAuthor(String id) {
		System.out.print("her3");
		try {
			HashMap<String, Author> authors = readAuthorMap();
			System.out.print("here");
			printHashMap(authors);
			authors.remove(id);
			saveToStorage(StorageType.AUTHORS, authors);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateAuthor(Author author) {
		HashMap<String, Author> authors = readAuthorMap();
		authors.put(author.getId(), author);
		saveToStorage(StorageType.AUTHORS, authors);
	}

	public HashMap<String, Author> readAuthorMap() {
		return (HashMap<String, Author>) readFromStorage(StorageType.AUTHORS);
	}
	// AUTHOR END

	// BOOKCOPY START
	public boolean initBookCopies() {

		try {
			HashMap<String, BookCopy> bookCopies = new HashMap();
			Address a = new Address("test", "test", "test", "test");
			Author author = new Author("test", "test", "test", a, "test");
			List<Author> authors = new ArrayList<Author>();
			authors.add(author);
			Book book = new Book("text", "text", 1, authors);
			BookCopy bookCopy = new BookCopy(book, 10);

			bookCopies.put("test", bookCopy);
			printHashMap(bookCopies);
			saveToStorage(StorageType.BOOKCOPIES, bookCopies);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		;
		return false;

	}

	public boolean saveNewBookCopy(BookCopy bookCopy) {
		HashMap<String, BookCopy> bookCopies = readBookCopiesMap();
		String id = bookCopy.getId();
		if (!bookCopies.containsKey(id)) {
			bookCopies.put(id, bookCopy);
			saveToStorage(StorageType.BOOKCOPIES, bookCopies);

			return true;
		}
		return false;
	}

	public void deleteBookCopy(String id) {
		try {
			HashMap<String, BookCopy> bookCopies = readBookCopiesMap();
			bookCopies.remove(id);
			saveToStorage(StorageType.BOOKCOPIES, bookCopies);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateBookCopy(BookCopy bookCopy) {
		HashMap<String, BookCopy> bookCopies = readBookCopiesMap();
		bookCopies.put(bookCopy.getId(), bookCopy);
		saveToStorage(StorageType.BOOKCOPIES, bookCopies);
	}

	public HashMap<String, BookCopy> readBookCopiesMap() {
		return (HashMap<String, BookCopy>) readFromStorage(StorageType.BOOKCOPIES);
	}
	// BOOKCOPY END
	
		
		// CHECKOUT START
		public boolean initCheckouts() {
			
			try {
				HashMap<String, Checkout> Checkouts = new HashMap();
				Address a = new Address("test", "test", "test", "test");
				Author author = new Author("test", "test", "test", a, "test");
				List<Author> authors = new ArrayList<Author>();
				authors.add(author);
				Book book = new Book("text2", "text2" , 12 , authors);
				
				Address address = new Address("N 4th st", "Fairfiled", "IOW", "52557");
				LibraryMember member = new LibraryMember("0", "fnameTest", "lnameTest", "510-123-1234", address);
				
//				CheckoutEntry entry = new CheckoutEntry(book, LocalDate.now(), LocalDate.now());
				
				Checkout checkout = new Checkout(member);
//				checkout.addEntry(entry);
				
				Checkouts.put("test", checkout);
				printHashMap(Checkouts);
				saveToStorage(StorageType.CHECKOUTS, Checkouts);
				return true;}
				catch(Exception e) {
					e.printStackTrace();
				};
				return false;
			
		}

		public boolean saveNewCheckout(Checkout checkout) {
			HashMap<String, Checkout> Checkouts = readCheckoutsMap();
			String id = checkout.getId();
			if (!Checkouts.containsValue(checkout)) {
				Checkouts.put(id, checkout);
				saveToStorage(StorageType.CHECKOUTS, Checkouts);

				return true;
			}
			return false;
		}

		public void deleteCheckout(String id) {
			try {
			HashMap<String, Checkout> Checkouts = readCheckoutsMap();
			Checkouts.remove(id);
			saveToStorage(StorageType.CHECKOUTS, Checkouts);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}

		public void updateCheckout(Checkout checkout) {
			HashMap<String, Checkout> Checkouts = readCheckoutsMap();
			Checkouts.put(checkout.getId(), checkout);
			saveToStorage(StorageType.CHECKOUTS, Checkouts);
		}

		@Override
		public HashMap<String, Checkout> readCheckoutsMap() {
			// TODO Auto-generated method stub
			return (HashMap<String, Checkout>) readFromStorage(StorageType.CHECKOUTS);
		}
		// CHECKOUT END




}
