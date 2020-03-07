package business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 *
 */
final public class BookTemp implements Serializable {

	private static final long serialVersionUID = 6110690276665962829L;
	private String id;
	private List<Author> authors;
	private String isbn;
	private String title;
	private int maxCheckoutLength;

	public BookTemp(String isbn, String title, int maxCheckoutLength, List<Author> authors) {
		this.id = UUID.randomUUID().toString();
		this.isbn = isbn;
		this.title = title;
		this.maxCheckoutLength = maxCheckoutLength;
		this.authors = Collections.unmodifiableList(authors);
	}

	

}
