package business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Checkout implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4846947610921351467L;
	
	private String id;
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
	
	public String getId() {
		return id;
	}
	
	public void setEntries(List<CheckoutEntry> entries) {
		this.entries = entries;
	}
	
	public String toString() {
		return member.getFirstName() + " " + member.getLastName() + " => " + entries.toString();
	}
}
