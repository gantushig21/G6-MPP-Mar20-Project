package rulesets;

import java.util.HashMap;

import javafx.stage.Stage;
import ui.authors.AuthorInfoWindow;
import ui.bookcopies.BookCopyInfoWindow;
import ui.books.BooksInfoWindow;
import ui.members.MemberInfoWindow;

public class RuleSetFactory {
	private RuleSetFactory() {

	}

	static HashMap<Class<? extends Stage>, RuleSet> map = new HashMap<>();

	static {
		map.put(MemberInfoWindow.class, new MemberInfoRuleSet());
		map.put(AuthorInfoWindow.class, new AuthorRuleSet());
		map.put(BookCopyInfoWindow.class, new BookCopyRuleSet());
		map.put(BooksInfoWindow.class, new BookRuleSet());
	}

	public static RuleSet getRuleSet(Stage stage) {
		Class<? extends Stage> windowClass = stage.getClass();

		if (!map.containsKey(windowClass)) {
			throw new IllegalArgumentException("No RuleSet found for this Component");
		}

		return map.get(windowClass);
	}
}
