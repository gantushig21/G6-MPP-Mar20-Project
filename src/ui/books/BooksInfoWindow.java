package ui.books;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import business.AlreadyExistException;
import business.Author;
import business.Book;
import business.ControllerInterface;
import business.SystemController;
import config.Constants;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Callback;
import rulesets.RuleException;
import rulesets.RuleSet;
import rulesets.RuleSetFactory;
import ui.LibWindow;
import ui.Start;
import ui.components.G6Alert;
import ui.components.G6BorderPane;
import ui.components.G6Button;
import ui.components.G6GridPane;
import ui.components.G6HBox;
import ui.components.G6Label;
import ui.components.G6TableView;
import ui.components.G6Text;
import ui.components.G6TextField;
import ui.components.G6VBox;

public class BooksInfoWindow extends Stage implements LibWindow {
	public static final BooksInfoWindow INSTANCE = new BooksInfoWindow();
	private static final List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

	private boolean isInitialized = false;

	public boolean isInitialized() {
		return isInitialized;
	}

	public void isInitialized(boolean val) {
		isInitialized = val;
	}

	private TextArea ta;
	private G6Text scenetitle;
	private G6Button actionBtn;
	private TableView<Author> tblAuthors;
	private G6TableView<Author> table;
	private G6TextField txtTitle;
	private G6TextField txtIsbn;
	private ListView<Author> listView;
	private ComboBox<Author> cmbAuthors;
	private ComboBox<Integer> cmbLength;
	private Book currentBook;

	public void setData(List<Author> data) {
		 table.setItems(FXCollections.observableList(data));
	}

	private void clearFields() {
		txtTitle.setText("");
		txtIsbn.setText("");
		listView.getItems().clear();
		cmbAuthors.getSelectionModel().selectFirst();
		cmbLength.getSelectionModel().selectFirst();
	}

	public void updateBook(Book book) {
		actionBtn.setText("Update");

		txtTitle.setText(book.getTitle());
		txtIsbn.setText(book.getIsbn());

		listView.getItems().addAll(book.getAuthors());
		cmbLength.getSelectionModel().select(book.getMaxCheckoutLength());
	}
	
	private void selectAuthor(Author author) {
		boolean check = false;
		List<Author> list = listView.getItems();
		for (Author a : list) {
			if (a.equals(author)) {
				check = true;
				break;
			}
		}
		if (!check)
			listView.getItems().add(author);
		else {
			Optional<ButtonType> result;
			result = new G6Alert(AlertType.NONE, "Error", "The author already added", ButtonType.OK)
					.showAndWait();
		}
	}

	@Override
	public void init() {
		G6BorderPane mainPane = new G6BorderPane();
		mainPane.setPadding(new Insets(30, 100, 30, 100));
		mainPane.setId("top-container");
		
		G6BorderPane tPane = new G6BorderPane();
		scenetitle = new G6Text("Add new book");
		scenetitle.setFont(Font.font("Harlow Solid Bold", FontWeight.NORMAL, Constants.PANE_TITLE_FONT_SIZE));
		tPane.setCenter(scenetitle);
		tPane.setPadding(new Insets(0, 10, 20, 0));
		
		G6Button btnBack = new G6Button("Back");

		G6HBox hBack = new G6HBox(10);
		hBack.setAlignment(Pos.BOTTOM_LEFT);
		hBack.getChildren().add(btnBack);
		tPane.setLeft(hBack);
				
		mainPane.setTop(tPane);
		G6HBox centerPane = new G6HBox(30);
		mainPane.setCenter(centerPane);

		G6GridPane grid = new G6GridPane();
		centerPane.setAlignment(Pos.CENTER);
		centerPane.getChildren().add(grid);
		
		G6VBox searchAuthors = new G6VBox(5);
		centerPane.getChildren().add(searchAuthors);
		G6TextField searchTxtf = new G6TextField();
		searchTxtf.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Start.searchAuthors(searchTxtf.getText().trim().toLowerCase(), "addBook");
			}
		});
		searchAuthors.getChildren().add(searchTxtf);
		
		table = new G6TableView<Author>();
		
		table.setRowFactory( tv -> {
			TableRow<Author> row = new TableRow<>();
		    row.setOnMouseClicked(event -> {
		        if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
		        	System.out.print(row.getIndex());
		        	Author rowData = row.getItem();
		            selectAuthor(rowData);
		        }
		    });
		    return row ;
		});
		
		TableColumn<Author, String> firstNameColumn = new TableColumn<>("First Name");
		firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

		TableColumn<Author, String> lastNameColumn = new TableColumn<>("Last Name");
		lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		
		table.getColumns().add(firstNameColumn);
		table.getColumns().add(lastNameColumn);
		
		VBox vbox = new VBox(table);
		vbox.setPadding(new Insets(0));
		searchAuthors.getChildren().add(vbox);
		
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(5);
		
		G6Label lblTitle = new G6Label("Title: ");
		grid.add(lblTitle, 0, 0);
		txtTitle = new G6TextField();
		txtTitle.setPromptText("Title");
		grid.add(txtTitle, 1, 0);

		G6Label lblIsbn = new G6Label("ISBN: ");
		grid.add(lblIsbn, 0, 1);
		txtIsbn = new G6TextField();
		txtIsbn.setPromptText("ISBN");
		grid.add(txtIsbn, 1, 1);

		HBox boxAuthor = new HBox(5);
		boxAuthor.setAlignment(Pos.TOP_LEFT);
		G6Label lblAuthors = new G6Label("Authors: ");
		lblAuthors.setAlignment(Pos.TOP_LEFT);
		boxAuthor.getChildren().add(lblAuthors);
		grid.add(boxAuthor, 0, 5);
		// TODO Authors List

		listView = new ListView<Author>();
		listView.setCellFactory(param -> new ListCell<Author>() {
			@Override
			protected void updateItem(Author item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null || item.getFirstName() == null) {
					setText(null);
				} else {
					setText(item.getFirstName());
				}
			}
		});

		grid.add(listView, 1, 5);

//		ControllerInterface ci = new SystemController();
//		List<Author> data = ci.allAuthors();
//		cmbAuthors = new ComboBox<Author>();
//		cmbAuthors.getItems().addAll(data);
//		cmbAuthors.setPrefWidth(130);
//		cmbAuthors.getSelectionModel().selectFirst();
//		cmbAuthors.setButtonCell(new ListCell<Author>() {
//
//			@Override
//			protected void updateItem(Author t, boolean bln) {
//				super.updateItem(t, bln);
//				if (t != null) {
//					setText(t.getFirstName());
//				} else {
//					setText(null);
//				}
//			}
//		});
//
//		cmbAuthors.setCellFactory(new Callback<ListView<Author>, ListCell<Author>>() {
//
//			@Override
//			public ListCell<Author> call(ListView<Author> p) {
//				return new ListCell<Author>() {
//
//					@Override
//					protected void updateItem(Author t, boolean bln) {
//						super.updateItem(t, bln);
//						if (t != null) {
//							setText(t.getFirstName()); //
//							// System.out.println("SET PROPERTY " + t.nomeProperty().toString());
//						} else {
//							setText(null);
//						}
//
//					}
//				};
//			}
//		});

		G6Button btnDelete = new G6Button("Delete");
		btnDelete.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				final int selectedIdx = listView.getSelectionModel().getSelectedIndex();
				if (selectedIdx != -1) {
					Author itemToRemove = listView.getSelectionModel().getSelectedItem();

					final int newSelectedIdx = (selectedIdx == listView.getItems().size() - 1) ? selectedIdx - 1
							: selectedIdx;

					listView.getItems().remove(selectedIdx);
					listView.getSelectionModel().select(newSelectedIdx);
				}
			}
		});

		HBox boxCombo = new HBox(5);
		boxCombo.getChildren().addAll(btnDelete);
		grid.add(boxCombo, 1, 6);

		G6Label lblLength = new G6Label("Maximum checkout length: ");
		lblLength.setPrefWidth(300);
//		lblLength.setWrapText(true);
		HBox boxLength = new HBox(5);
		boxLength.setAlignment(Pos.TOP_LEFT);
		boxLength.getChildren().add(lblLength);
		boxLength.setPrefHeight(250);
		boxLength.setPrefWidth(80);
		grid.add(boxLength, 0, 8);
		cmbLength = new ComboBox<Integer>();
		cmbLength.getItems().addAll(numbers);
		cmbLength.setPrefWidth(130);
		cmbLength.getSelectionModel().selectFirst();
		HBox boxComb = new HBox(5);
		boxComb.setAlignment(Pos.TOP_LEFT);
		boxComb.getChildren().add(cmbLength);
		grid.add(boxComb, 1, 8);

		actionBtn = new G6Button("Add");
		HBox hbBtn = new HBox(11);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(actionBtn);
		grid.add(hbBtn, 1, 9);

		HBox messageBox = new HBox(10);
		messageBox.setAlignment(Pos.BOTTOM_RIGHT);
		// messageBox.getChildren().add(messageBar);
		grid.add(messageBox, 1, 16);

		actionBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				String title = txtTitle.getText().trim();
				String isbn = txtIsbn.getText().trim();
				List<Author> authors = listView.getItems();
				int maxLength = cmbLength.getValue();

				try {
					RuleSet ruleSet = RuleSetFactory.getRuleSet(BooksInfoWindow.this);
					ruleSet.applyRules(BooksInfoWindow.this);

					Optional<ButtonType> result;

					if (actionBtn.getText().equals("Add")) {
						result = new G6Alert(AlertType.CONFIRMATION, "Confirmation",
								"Are you sure to create a new book?").showAndWait();

						if (result.get() == ButtonType.OK) {
							// System.out.println("add book" + " " + title + " " + isbn + " " + maxLength +
							// " "
							// + authors.get(0).getFirstName());
							ControllerInterface c = new SystemController();
							Book book = new Book(isbn, title, maxLength, authors);
							System.out.println(book);
							c.addBook(book);

							result = new G6Alert(AlertType.NONE, "Success", "The book is added successful",
									ButtonType.OK).showAndWait();
							if (result.get() == ButtonType.OK) {
								clearFields();
							}
						} else {
							System.out.println("Canceled");
						}

					} else if (actionBtn.getText().equals("Update")) {
						currentBook.setTitle(title);
						currentBook.setIsbn(isbn);
						currentBook.setAuthors(authors);
						currentBook.setMaxCheckoutLength(maxLength);

						result = new G6Alert(AlertType.CONFIRMATION, "Confirmation", "Are you sure to update this book")
								.showAndWait();
						if (result.get() == ButtonType.OK) {
							ControllerInterface c = new SystemController();
							c.updateBook(currentBook);

							Start.showBooks();
						}

					}
				} catch (RuleException ex) {
					showErrorDialog("Error", ex.getMessage());
				} catch (AlreadyExistException ex) {
					showErrorDialog("Error", ex.getMessage());
				}
			}
		});

		btnBack.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Start.hideAllWindows();
				if (actionBtn.getText().equals("Add")) {
					Start.showBooks();
				} else if (actionBtn.getText().equals("Update")) {
					Start.showBooks();
				}
			}
		});
		Scene scene = new Scene(mainPane, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
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

	public String getTitleValue() {
		return txtTitle.getText();
	}

	public String getIsbnValue() {
		return txtIsbn.getText();
	}

	public int getAuthorsValue() {
		return listView.getItems().size();
	}

}
