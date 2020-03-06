package ui.components;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class G6Alert extends Alert {
	public G6Alert(AlertType type) {
		super(type);
	}
	
	public G6Alert(AlertType type, String header, String content) {
		super(type);
		setHeaderText(header);
		setContentText(content);
	}

	public G6Alert(AlertType type, String header, String content, ButtonType buttonType) {
		super(type, content, buttonType);
		setHeaderText(header);
	}
}
