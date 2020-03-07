package rulesets;

import config.Validations;
import javafx.stage.Stage;
import ui.bookcopies.BookCopyInfoWindow;

public class BookCopyRuleSet implements RuleSet {
	private BookCopyInfoWindow bookCopyInfoWindow;

	@Override
	public void applyRules(Stage ob) throws RuleException {
		bookCopyInfoWindow = (BookCopyInfoWindow) ob;
		nonemptyRule();
	}
	
	private void nonemptyRule() throws RuleException {
		if (bookCopyInfoWindow.getCopyNumValue().isEmpty() ) {
			throw new RuleException("All fields must be non-empty!");			
		}
	}
	
}
