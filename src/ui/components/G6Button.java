package ui.components;

import javafx.scene.control.Button;

public class G6Button extends Button {
	public G6Button(String txt) {
		super(txt);
	}
	
	public static G6Button createButtonWithLength(String txt, int length) {
		G6Button btn = new G6Button(txt);
		
		btn.setPrefWidth(length);
		
		return btn;
	}
}
