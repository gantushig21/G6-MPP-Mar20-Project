package business;

import java.io.Serializable;

final public class Author extends Person implements Serializable {
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
	
	private static final long serialVersionUID = 7508481740058530471L;
}
