package ui.components;

import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class G6Text extends Text {
	public G6Text(String txt) {
		super(txt);
	}
	
	public static StackPane withPaddings(Text txt, Insets insets) {
		StackPane pane = new StackPane();
		pane.setPadding(insets);
		
		pane.getChildren().add(txt);
		
		return pane;
	}
}
