package rulesets;

import config.Validations;
import javafx.stage.Stage;
import ui.members.MemberInfoWindow;

public class MemberInfoRuleSet implements RuleSet {
	private MemberInfoWindow memberInfo;

	@Override
	public void applyRules(Stage ob) throws RuleException {
		memberInfo = (MemberInfoWindow) ob;
		nonemptyRule();
		zipCodeRule();
	}
	
	private void nonemptyRule() throws RuleException {
		if (memberInfo.getFirstNameValue().trim().isEmpty() || 
				memberInfo.getLastNameValue().trim().isEmpty() ||
				memberInfo.getStreetValue().trim().isEmpty() ||
				memberInfo.getCityValue().trim().isEmpty() ||
				memberInfo.getStateValue().trim().isEmpty() ||
				memberInfo.getZipValue().trim().isEmpty() ||
				memberInfo.getPhoneNumberValue().trim().isEmpty()) {
			throw new RuleException("All fields must be non-empty!");			
		}
	}
	
	private void zipCodeRule() throws RuleException {
		String zipCode = memberInfo.getZipValue();
		
		if (!zipCode.matches(Validations.ZIP_CODE))
			throw new RuleException("Invalid zip code!");
	}
}
