package business;

import java.io.Serializable;

/**
 * Immutable class
 */
final public class BookCopy implements Serializable {
		

	private static final long serialVersionUID = -63976228084869815L;
	private String id;
	private Book book;
	private int copyNum;
	private boolean isAvailable;
	public BookCopy(Book book, int copyNum, boolean isAvailable) {
		this.id =	"BookCopies_" + RandomIdGenerator.GetBase62(8); 
		this.book = book;
		this.copyNum = copyNum;
		this.isAvailable = isAvailable;
	}
	
//	public BookCopy(Book book, int copyNum) {
//		this.id =	"BookCopies_" + (System.currentTimeMillis() / 1000); 
//		this.book = book;
//		this.copyNum = copyNum;
//		this.isAvailable = false;
//	}
	
	
	public void changeAvailability() {
		isAvailable = !isAvailable;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public int getCopyNum() {
		return copyNum;
	}

	public void setCopyNum(int copyNum) {
		this.copyNum = copyNum;
	}

	public boolean getIsAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
	
	@Override
	public String toString() {
		return book.getIsbn() + " " + id;
	}

	@Override
	public boolean equals(Object ob) {
		if(ob == null) return false;
		if(!(ob instanceof BookCopy)) return false;
		BookCopy copy = (BookCopy)ob;
		return copy.book.getIsbn().equals(book.getIsbn()) && copy.copyNum == copyNum;
	}
	
}
