package rulesets;

import config.Validations;
import javafx.stage.Stage;
import ui.authors.AuthorInfoWindow;

public class AuthorRuleSet implements RuleSet {
	private AuthorInfoWindow authorInfoWindow;

	@Override
	public void applyRules(Stage ob) throws RuleException {
		authorInfoWindow = (AuthorInfoWindow) ob;
		nonemptyRule();
		zipCodeRule();
	}
	
	private void nonemptyRule() throws RuleException {
		if (authorInfoWindow.getFirstNameValue().trim().isEmpty() || 
				authorInfoWindow.getLastNameValue().trim().isEmpty() ||
				authorInfoWindow.getStreetValue().trim().isEmpty() ||
				authorInfoWindow.getCityValue().trim().isEmpty() ||
				authorInfoWindow.getStateValue().trim().isEmpty() ||
				authorInfoWindow.getZipValue().trim().isEmpty() ||
				authorInfoWindow.getPhoneNumberValue().trim().isEmpty()) {
			throw new RuleException("All fields must be non-empty!");			
		}
	}
	
	private void zipCodeRule() throws RuleException {
		String zipCode = authorInfoWindow.getZipValue();
		
		if (!zipCode.matches(Validations.ZIP_CODE))
			throw new RuleException("Invalid zip code!");
	}
}
