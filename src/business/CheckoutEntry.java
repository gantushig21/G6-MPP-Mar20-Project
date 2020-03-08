package business;

import java.io.Serializable;
import java.time.LocalDate;

public class CheckoutEntry implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6725780516540590492L;
	private BookCopy bookCopy;
	private LibraryMember member;
	private LocalDate checkoutDate;
	private LocalDate dueDate;
	private LocalDate returnDate;
	
	public CheckoutEntry(BookCopy bookCopy, LibraryMember member, LocalDate checkoutData, LocalDate dueDate) {
		this.bookCopy = bookCopy;
		this.member = member;
		this.checkoutDate = checkoutData;
		this.dueDate = dueDate;
	}

	public BookCopy getBook() {
		return bookCopy;
	}
	
	public LibraryMember getMember() {
		return member;
	}

	public LocalDate getCheckoutDate() {
		return checkoutDate;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}
	
	public LocalDate getReturnDate() {
		return returnDate;
	}
	
	@Override
	public String toString() {
		return bookCopy.getBook().getIsbn() + " " + bookCopy.getCopyNum();
	}
}
