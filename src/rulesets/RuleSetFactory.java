package rulesets;

import java.util.HashMap;

import javafx.stage.Stage;
import ui.members.MemberInfoWindow;

public class RuleSetFactory {
	private RuleSetFactory() {

	}
	static HashMap<Class<? extends Stage>, RuleSet> map = new HashMap<>();
	
	static {
		map.put(MemberInfoWindow.class, new MemberInfoRuleSet());
		
	}
	
	public static RuleSet getRuleSet(Stage stage) {
		Class<? extends Stage> windowClass = stage.getClass();
		
		if (!map.containsKey(windowClass)) {
			throw new IllegalArgumentException("No RuleSet found for this Component");
		}
		
		return map.get(windowClass);
	}
}
