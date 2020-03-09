package business;

import java.io.Serializable;
import java.time.LocalDate;

public class CheckoutEntry implements Serializable, Comparable<CheckoutEntry> {
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
	
	public void setReturnDate(LocalDate date) {
		returnDate = date;
	}
	
	@Override
	public String toString() {
		return "\nISBN: " + bookCopy.getBook().getIsbn() + 
				"\nCopy Number: " + bookCopy.getCopyNum() + 
				"\nChecked out data: " + checkoutDate + 
				"\nDue date: " + dueDate + 
				"\nReturn date: " + (returnDate != null ? returnDate : "not yet") + "\n";
	}
	
	@Override
	public int compareTo(CheckoutEntry entry) {
		return checkoutDate.compareTo(entry.getCheckoutDate());
	}
}
