package ui.components;

import business.LibraryMember;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class G6VBox extends VBox {
	public G6VBox() {
	}
	
	public G6VBox(int value) {
		super(value);
	}
	
	public G6VBox(TableView<LibraryMember> table) {
		super(table);
	}
}
