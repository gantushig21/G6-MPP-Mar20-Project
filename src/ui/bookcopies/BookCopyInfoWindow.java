package ui.bookcopies;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
import jfxtras.styles.jmetro.JMetro;
import rulesets.RuleException;
import rulesets.RuleSet;
import rulesets.RuleSetFactory;
import ui.LibWindow;
import ui.Start;
import ui.books.BooksInfoWindow;
import ui.components.G6Alert;
import ui.components.G6BorderPane;
import ui.components.G6Button;
import ui.components.G6CheckBox;
import ui.components.G6GridPane;
import ui.components.G6HBox;
import ui.components.G6Label;

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
		scenetitle.setText("Update book copy ID: #" + bookCopy.getId());
		actionBtn.setText("Update");
		
		copyNumTxtf.setText(Integer.toString(bookCopy.getCopyNum()) );
		isAvailableCb.setSelected(bookCopy.getIsAvailable());
		
		currentBookCopy = bookCopy;
	}

	public void init() {
		G6BorderPane mainPane = new G6BorderPane();
		mainPane.setPadding(new Insets(Constants.WINDOW_DEFAULT_PADDING));
		mainPane.setId("top-container");
		
		// Rendering top
		
		scenetitle = new G6Text("Add a new book copy for book");
		StackPane sceneTitlePane = G6Text.withPaddings(scenetitle, new Insets(0));

		scenetitle.setFont(Font.font("Harlow Solid Italic", FontWeight.NORMAL, Constants.PANE_TITLE_FONT_SIZE));
		G6BorderPane topPane = new G6BorderPane();
		topPane.setCenter(sceneTitlePane);
		topPane.setPadding(new Insets(0, 10, 20, 0));

		G6Button backBtn = new G6Button("Back");

		backBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Start.homeWindow();
			}
		});
		

		G6HBox hBack = new G6HBox(10);
		hBack.setAlignment(Pos.BOTTOM_LEFT);
		hBack.getChildren().add(backBtn);
		topPane.setLeft(hBack);

		G6GridPane grid = new G6GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		
//		G6VBox vbox = new  G6VBox();
//		vbox.getChildren().addAll(topPane, grid);

		mainPane.setTop(topPane);
		mainPane.setCenter(grid);
		
		// Book Info section
		G6Label titleLbl = new G6Label("Title: ");
		grid.add(titleLbl, 0, 2);
		G6Text titleTxt = new G6Text(book.getTitle());
		grid.add(titleTxt, 1, 2);

		G6Label isbnLbl = new G6Label("ISBN: ");
		grid.add(isbnLbl, 0, 3);
		G6Text isbnTxt = new G6Text(book.getIsbn());
		grid.add(isbnTxt, 1, 3);

		G6Label authorsLbl = new G6Label("Authors: ");
		grid.add(authorsLbl, 0, 4);
		List<String> authorsFullnames = book.getAuthors().stream().map(a -> (a.getFirstName() + " " + a.getLastName()))
				.collect(Collectors.toList());
		G6Text authorsTxt = new G6Text(String.join(", ", authorsFullnames));
		grid.add(authorsTxt, 1, 4);
		grid.setMaxWidth(Double.MAX_VALUE);
		
		// Input Section
		G6Label copyNumberLbl = new G6Label("Number of copies: ");
		grid.add(copyNumberLbl, 0, 6);
		copyNumTxtf = new G6TextField();
		grid.add(copyNumTxtf, 1, 6);

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
		
		G6Label isAvailableLbl = new G6Label("Is Available: ");
//		grid.add(isAvailableLbl, 0, 7);
		isAvailableCb = new G6CheckBox();
//		grid.add(isAvailableCb, 1, 7);
//		

		
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
				int copyNumInt = Integer.parseInt(copyNumTxtf.getText().trim()) ;
				boolean isAvailable = true;

				try {
					RuleSet ruleSet = RuleSetFactory.getRuleSet(BookCopyInfoWindow.this);
					ruleSet.applyRules(BookCopyInfoWindow.this);

					Optional<ButtonType> result;

					if (actionBtn.getText().equals("Add")) {

						result = new G6Alert(AlertType.CONFIRMATION, "Confirmation",
								"Are you sure to add new copies?").showAndWait();

						if (result.get() == ButtonType.OK) {
							ControllerInterface c = new SystemController();
							List<BookCopy> cps = new ArrayList<>(copyNumInt);
							for (int i = 0; i < copyNumInt; i++) 
								cps.add(new BookCopy(book, 0, isAvailable));
							
							book.addCopies(cps);
							c.updateBook(book);
							result = new G6Alert(AlertType.NONE, "Success", "The copies are added successful",
									ButtonType.OK).showAndWait();
							if (result.get() == ButtonType.OK) {
								clearFields();
								Start.showBookCopies(book);
							}
						} 

					} else if (actionBtn.getText().equals("Update")) {
					
						currentBookCopy.setCopyNum(copyNumInt);
						currentBookCopy.setAvailable(isAvailable);

						result = new G6Alert(AlertType.CONFIRMATION, "Confirmation",
								"Are you sure to update this bookCopy").showAndWait();
						if (result.get() == ButtonType.OK) {
							ControllerInterface c = new SystemController();
//							c.updateBookCopy(currentBookCopy);
							BookCopy[] copies =  book.getCopies();
							int index = 0;
							for(int i = 0; i < copies.length; i++) {
								if(copies[i].getId() == currentBookCopy.getId()) {
									index = i; break;
								}
							}
							copies[index] = currentBookCopy;
							book.setCopies(copies);
							c.updateBook(book);
							Start.showBookCopies(book);
//							Start.showBookCopies(currentBookCopy.getBook());
						}

					}
				} catch (RuleException ex) {
					showErrorDialog("Error", ex.getMessage());
				} 

			}
		});

		backBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Start.showBookCopies(book);
			}
		});
		Scene scene = new Scene(mainPane, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
		JMetro jMetro = new JMetro();
		jMetro.setScene(scene);
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
