package business;

import java.util.ArrayList;
import java.util.List;

public class Checkout {
	private LibraryMember member;
	private List<CheckoutEntry> entries;
	
	public Checkout(LibraryMember member) {
		this.member = member;
		entries = new ArrayList<CheckoutEntry>();
	}
	
	public void addEntry(CheckoutEntry entry) {
		entries.add(entry);
	}
	
	public List<CheckoutEntry> getCheckoutEntries() {
		return entries;
	}
	
	public LibraryMember getMember() {
		return member;
	}
}
