package rulesets;

import javafx.stage.Stage;
import ui.books.BooksInfoWindow;

public class BookRuleSet implements RuleSet {
	private BooksInfoWindow booksInfoWindow;

	@Override
	public void applyRules(Stage ob) throws RuleException {
		booksInfoWindow = (BooksInfoWindow) ob;
		nonemptyRule();
		maxCheckoutLengthNumericRule();
		// zipCodeRule();
	}

	private void nonemptyRule() throws RuleException {
		if (booksInfoWindow.getTitleValue().trim().isEmpty() 
				|| booksInfoWindow.getIsbnValue().trim().isEmpty()
				|| booksInfoWindow.getAuthorsValue() == 0
				|| booksInfoWindow.getMaxCheckoutLengthValue().trim().isEmpty()) {
			throw new RuleException("All fields must be non-empty!");
		}
	}
	
	private void maxCheckoutLengthNumericRule() throws RuleException {
		String val = booksInfoWindow.getMaxCheckoutLengthValue().trim();
		try {
			Integer.parseInt(val);
			//val is numeric
		} catch(NumberFormatException e) {
			throw new RuleException("Maximum checkout length must be numeric");
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
