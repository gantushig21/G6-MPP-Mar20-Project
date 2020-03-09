package business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 *
 */
final public class Book implements Serializable {

	private static final long serialVersionUID = 6110690276665962829L;
	private int total;
	private BookCopy[] copies;
	private List<Author> authors;
	private String isbn;
	private String title;
	private int maxCheckoutLength;
	private int checkedOut;
	private int borrowDayLimit;

	public Book(String isbn, String title, int maxCheckoutLength, List<Author> authors) {
		this.borrowDayLimit = 0;
		this.checkedOut = 0;
		this.total = 0;
		this.isbn = isbn;
		this.title = title;
		this.maxCheckoutLength = maxCheckoutLength;

//		this.authors = authors;
		this.authors = Collections.unmodifiableList(authors);
		copies = new BookCopy[] {};
	}

	public void updateCopies(BookCopy copy) {
		for (int i = 0; i < copies.length; ++i) {
			BookCopy c = copies[i];
			if (c.equals(copy)) {
				copies[i] = copy;
			}
		}
	}

	public List<Integer> getCopyNums() {
		List<Integer> retVal = new ArrayList<>();
		for (BookCopy c : copies) {
			retVal.add(c.getCopyNum());
		}
		return retVal;
	}
	
	public int getTotal() {
		return total;
	}
	
	public int getBorrowDayLimit() {
		return borrowDayLimit;
	}

	public void addCopy(BookCopy bc) {
		total++;
		bc.setCopyNum(total);
		BookCopy[] newArr = new BookCopy[copies.length + 1];
		System.arraycopy(copies, 0, newArr, 0, copies.length);
		newArr[copies.length] = bc;
		copies = newArr;
	}
	
	public void addCopies(List<BookCopy> cps) {
		int size = cps.size();
		
		BookCopy[] newArr = new BookCopy[copies.length + size];
		System.arraycopy(copies, 0, newArr, 0, copies.length);
		for (int i = 0; i < size; i++) {
			cps.get(i).setCopyNum(total + i + 1);
			newArr[copies.length + i] = cps.get(i); 
		}
		copies = newArr;
		total += size;
	}

	public void addCopy() {
		BookCopy[] newArr = new BookCopy[copies.length + 1];
		System.arraycopy(copies, 0, newArr, 0, copies.length);
		newArr[copies.length] = new BookCopy(this, copies.length + 1, true);
		copies = newArr;
	}

	@Override
	public boolean equals(Object ob) {
		if (ob == null)
			return false;
		if (ob.getClass() != getClass())
			return false;
		Book b = (Book) ob;
		return b.isbn.equals(isbn);
	}

	public boolean isAvailable() {
		if (copies == null) {
			return false;
		}

		return Arrays.stream(copies).map(l -> l.getIsAvailable()).reduce(false, (x, y) -> x || y);
	}

	public BookCopy getAvailableBookCopy() {
		for (BookCopy copy: copies) {
			if (copy.getIsAvailable())
				return copy;
		}
		
		return null;
	}
	
	@Override
	public String toString() {
		return "isbn: " + isbn + 
				", maxLength: " + maxCheckoutLength + 
				", available: " + isAvailable() +
				Arrays.toString(copies) + 
				authors.toString();
	}

	public int getNumCopies() {
		return copies.length;
	}
	
	public int getCheckedOut() {
		return checkedOut;
	}

	public String getTitle() {
		return title;
	}

	public BookCopy[] getCopies() {
		return copies;
	}

	public List<Author> getAuthors() {
		return authors;
	}

	public String getIsbn() {
		return isbn;
	}

	public BookCopy getNextAvailableCopy() {
		Optional<BookCopy> optional = Arrays.stream(copies).filter(x -> x.getIsAvailable()).findFirst();
		return optional.isPresent() ? optional.get() : null;
	}

	public BookCopy getCopy(int copyNum) {
		for (BookCopy c : copies) {
			if (copyNum == c.getCopyNum()) {
				return c;
			}
		}
		return null;
	}

	public int getMaxCheckoutLength() {
		return maxCheckoutLength;
	}

	public void setCheckedOut(int checkedOut) {
		this.checkedOut = checkedOut;
	}
	
	public void setCopies(BookCopy[] copies) {
		this.copies = copies;
	}
	public void setBorrowDayLimit(int borrowDayLimit) {
		this.borrowDayLimit = borrowDayLimit;
	}
	
	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setMaxCheckoutLength(int maxCheckoutLength) {
		this.maxCheckoutLength = maxCheckoutLength;
	}

}
