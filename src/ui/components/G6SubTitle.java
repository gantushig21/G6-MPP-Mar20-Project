package ui.components;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class G6SubTitle extends Text {
	public G6SubTitle(String txt) {
		super(txt);
		
		setStyle("-fx-color: #000099;");
		setFont(Font.font("Harlow Solid Italic", FontWeight.BOLD, 16));
		
	}
	
}
