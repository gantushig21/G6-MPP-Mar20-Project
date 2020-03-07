package rulesets;

import javafx.stage.Stage;
import ui.books.BooksInfoWindow;

public class BookRuleSet implements RuleSet {
	private BooksInfoWindow booksInfoWindow;

	@Override
	public void applyRules(Stage ob) throws RuleException {
		booksInfoWindow = (BooksInfoWindow) ob;
		nonemptyRule();
		// zipCodeRule();
	}

	private void nonemptyRule() throws RuleException {
		if (booksInfoWindow.getTitleValue().trim().isEmpty() || booksInfoWindow.getIsbnValue().trim().isEmpty()
				|| booksInfoWindow.getAuthorsValue() == 0) {
			throw new RuleException("All fields must be non-empty!");
		}
	}
	/*
	 * private void zipCodeRule() throws RuleException { String zipCode =
	 * booksInfoWindow.getZipValue();
	 * 
	 * if (!zipCode.matches(Validations.ZIP_CODE)) throw new
	 * RuleException("Invalid zip code!"); }
	 */
}
