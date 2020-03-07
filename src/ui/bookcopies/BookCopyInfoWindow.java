package ui.bookcopies;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.swing.JOptionPane;

import business.Address;
import business.AlreadyExistException;
import business.Author;
import business.Book;
import business.ControllerInterface;
import business.BookCopy;
import business.LoginException;
import business.SystemController;
import config.Constants;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import rulesets.RuleException;
import rulesets.RuleSet;
import rulesets.RuleSetFactory;
import ui.LibWindow;
import ui.Start;
import ui.components.G6Alert;
import ui.components.G6BorderPane;
import ui.components.G6Button;
import ui.components.G6CheckBox;
import ui.components.G6GridPane;
import ui.components.G6HBox;
import ui.components.G6Label;
import ui.components.G6SubTitle;
import ui.components.G6Text;
import ui.components.G6TextField;
import ui.components.G6VBox;

public class BookCopyInfoWindow extends Stage implements LibWindow {
	public static final BookCopyInfoWindow INSTANCE = new BookCopyInfoWindow();

	private boolean isInitialized = false;

	private G6Text scenetitle;
	private G6Button actionBtn;

	private G6TextField copyNumTxtf;
	private G6CheckBox isAvailableCb;
	private Book book;

	public void setBook(Book book) {
		this.book = book;
	}
	public boolean isInitialized() {
		return isInitialized;
	}

	public void isInitialized(boolean val) {
		isInitialized = val;
	}

	private Text messageBar = new Text();

	public void clear() {
		messageBar.setText("");
	}

	/* This class is a singleton */
	private BookCopy currentBookCopy;

	private BookCopyInfoWindow() {
	}

	private void clearFields() {

		copyNumTxtf.setText("");
		isAvailableCb.setSelected(false);
	}

	public void updateBookCopy(BookCopy bookCopy) {
		scenetitle.setText("Update BookCopy ID " + bookCopy.getId());
		actionBtn.setText("Update");
		
		copyNumTxtf.setText(Integer.toString(bookCopy.getCopyNum()) );
		System.out.println(bookCopy.getCopyNum());
		isAvailableCb.setSelected(bookCopy.getIsAvailable());
		
		currentBookCopy = bookCopy;
	}

	public void init() {
		G6VBox vbox = new G6VBox(5);
		vbox.setPadding(new Insets(25));
		vbox.setId("top-container");

		scenetitle = new G6Text("Add new BookCopy");
		StackPane sceneTitlePane = G6Text.withPaddings(scenetitle, new Insets(0));

		scenetitle.setFont(Font.font("Harlow Solid Italic", FontWeight.NORMAL, Constants.PANE_TITLE_FONT_SIZE));
		G6BorderPane topPane = new G6BorderPane();
		topPane.setCenter(sceneTitlePane);
		topPane.setPadding(new Insets(0, 10, 20, 0));

		G6Button backBtn = new G6Button("Back");

		G6HBox hBack = new G6HBox(10);
		hBack.setAlignment(Pos.BOTTOM_LEFT);
		hBack.getChildren().add(backBtn);
		topPane.setLeft(hBack);

		G6GridPane grid = new G6GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(5);
		grid.setPadding(new Insets(25, 25, 25, 25));

		vbox.getChildren().addAll(topPane, grid);

		grid.add(topPane, 0, 0, 2, 1);


		// Contact Section
		G6Label copyNumberLbl = new G6Label("Copy Number: ");
		grid.add(copyNumberLbl, 0, 2);
		copyNumTxtf = new G6TextField();
		grid.add(copyNumTxtf, 1, 2);

		// ALLOW INPUT NUMBER ONLY
		copyNumTxtf.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
		        if (!newValue.matches("\\d*")) {
		        	copyNumTxtf.setText(newValue.replaceAll("[^\\d]", ""));
		        }
		    }
		});
		
		G6Label isAvailableLbl = new G6Label("Is available: ");
		grid.add(isAvailableLbl, 0, 3);
		isAvailableCb = new G6CheckBox();
		grid.add(isAvailableCb, 1, 3);
		

		
		actionBtn = new G6Button("Add");
		HBox hbBtn = new HBox(11);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(actionBtn);
		grid.add(hbBtn, 1, 18);

		HBox messageBox = new HBox(10);
		messageBox.setAlignment(Pos.BOTTOM_RIGHT);
		messageBox.getChildren().add(messageBar);
		grid.add(messageBox, 1, 16);

		actionBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				int copyNumStr = Integer.parseInt(copyNumTxtf.getText().trim()) ;
				boolean isAvailable = isAvailableCb.isSelected();

				try {
					RuleSet ruleSet = RuleSetFactory.getRuleSet(BookCopyInfoWindow.this);
					ruleSet.applyRules(BookCopyInfoWindow.this);

					Optional<ButtonType> result;

					if (actionBtn.getText().equals("Add")) {
						System.out.println(copyNumStr + " " + isAvailable);

						result = new G6Alert(AlertType.CONFIRMATION, "Confirmation",
								"Are you sure to create a new bookCopy?").showAndWait();

						if (result.get() == ButtonType.OK) {
							ControllerInterface c = new SystemController();
							// TODO get book
							Address a = new Address("test", "test", "test", "test");
							Author author = new Author("test", "test", "test", a, "test");
							List<Author> authors = new ArrayList<Author>();
							authors.add(author);
							Book book = new Book("text", "text", 1, authors);

							BookCopy bookCopy = new BookCopy(book, 10);

							c.addBookCopy(bookCopy);

							result = new G6Alert(AlertType.NONE, "Success", "The bookCopy is added successful",
									ButtonType.OK).showAndWait();
							if (result.get() == ButtonType.OK) {
								clearFields();
							}
						} else {
							System.out.println("Canceled");
						}

					} else if (actionBtn.getText().equals("Update")) {
					
						currentBookCopy.setCopyNum(copyNumStr);
						currentBookCopy.setAvailable(isAvailable);

						result = new G6Alert(AlertType.CONFIRMATION, "Confirmation",
								"Are you sure to update this bookCopy").showAndWait();
						if (result.get() == ButtonType.OK) {
							ControllerInterface c = new SystemController();
							c.updateBookCopy(currentBookCopy);

							Start.showBookCopies(currentBookCopy.getBook());
						}

					}
				} catch (RuleException ex) {
					showErrorDialog("Error", ex.getMessage());
				} catch (AlreadyExistException ex) {
					showErrorDialog("Error", ex.getMessage());
				}

			}
		});

		backBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Start.hideAllWindows();
				if (actionBtn.getText().equals("Add")) {
					Start.primStage().show();
				} else if (actionBtn.getText().equals("Update")) {
					BookCopiesWindow.INSTANCE.clear();
					BookCopiesWindow.INSTANCE.show();
				}
			}
		});
		Scene scene = new Scene(vbox, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
		// scene.getStylesheets().add(getClass().getResource("../library.css").toExternalForm());
		setScene(scene);

	}

	private void showErrorDialog(String header, String msg) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("");
		alert.setHeaderText(header);
		alert.setContentText(msg);

		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == ButtonType.OK) {
		}
	}


	public String getCopyNumValue() {
		return copyNumTxtf.getText();
	}


	public static BookCopyInfoWindow getInstance() {
		return INSTANCE;
	}

	public Text getMessageBar() {
		return messageBar;
	}

}
