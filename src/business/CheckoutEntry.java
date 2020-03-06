package business;

import java.time.LocalDate;

public class CheckoutEntry {
	private Book book;
	private LocalDate checkoutDate;
	private LocalDate dueDate;
	private LocalDate returnDate;
	
	public CheckoutEntry(Book book, LocalDate checkoutData, LocalDate dueDate) {
		this.book = book;
		this.checkoutDate = checkoutData;
		this.dueDate = dueDate;
	}

	public Book getBook() {
		return book;
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
}
