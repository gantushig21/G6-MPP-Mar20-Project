package business;

import java.io.Serializable;

final public class Author extends Person implements Serializable, Comparable<Author> {
	private String id;
	private String bio;
	private String credentials;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCredentials() {
		return credentials;
	}

	public void setCredentials(String credentials) {
		this.credentials = credentials;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getBio() {
		return bio;
	}
	
	public Author(String f, String l, String t, Address a, String bio) {
		super(f, l, t, a);
		this.bio = bio;
		this.id = "Author_" + (System.currentTimeMillis() / 1000); 
	}

	public String getAuthorId() {
		return id;
	}
	
	public int compareTo(Author author) {
		return id.compareTo(author.getAuthorId()) > 0 ? 1 : -1;
	}
	
	@Override
	public String toString() {
		return getFirstName() + " " + getLastName();
	}
	
	private static final long serialVersionUID = 7508481740058530471L;
}
