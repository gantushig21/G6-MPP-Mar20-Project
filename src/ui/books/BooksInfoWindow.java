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
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
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
	private G6TextField txtTitle;
	private G6TextField txtIsbn;
	private ListView<Author> listView;
	private ComboBox<Author> cmbAuthors;
	private ComboBox<Integer> cmbLength;
	private Book currentBook;

	public void setData(List<Author> data) {
		tblAuthors.setItems(FXCollections.observableList(data));
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

	@Override
	public void init() {
		G6VBox vbox = new G6VBox(5);
		vbox.setPadding(new Insets(25));
		vbox.setId("top-container");

		scenetitle = new G6Text("Add new book");
		StackPane sceneTitlePane = G6Text.withPaddings(scenetitle, new Insets(0));

		scenetitle.setFont(Font.font("Harlow Solid Bold", FontWeight.NORMAL, Constants.PANE_TITLE_FONT_SIZE));
		G6BorderPane topPane = new G6BorderPane();
		topPane.setCenter(sceneTitlePane);
		topPane.setPadding(new Insets(0, 10, 20, 0));

		G6Button btnBack = new G6Button("Back");

		G6HBox hBack = new G6HBox(10);
		hBack.setAlignment(Pos.BOTTOM_LEFT);
		hBack.getChildren().add(btnBack);
		topPane.setLeft(hBack);

		G6GridPane grid = new G6GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(5);
		grid.setPadding(new Insets(25, 25, 25, 25));

		vbox.getChildren().addAll(topPane, grid);

		grid.add(topPane, 0, 0, 2, 1);

		G6Label lblTitle = new G6Label("Title: ");
		grid.add(lblTitle, 0, 3);
		txtTitle = new G6TextField();
		txtTitle.setPromptText("Title");
		grid.add(txtTitle, 1, 3);

		G6Label lblIsbn = new G6Label("ISBN: ");
		grid.add(lblIsbn, 0, 4);
		txtIsbn = new G6TextField();
		txtIsbn.setPromptText("ISBN");
		grid.add(txtIsbn, 1, 4);

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
		/*
		 * List<Hyperlink> links = new ArrayList<>(); ControllerInterface ci = new
		 * SystemController(); List<Author> data = ci.allAuthors(); List<String> names =
		 * new ArrayList<>();
		 * 
		 * for (Author a : data) { names.add(a.getFirstName()); }
		 */
		/*
		 * TableColumn<Author, List<Hyperlink>> col1 = new TableColumn<>("Authors");
		 * PropertyValueFactory<Author, List<Hyperlink>> thirdColFactory = new
		 * PropertyValueFactory<>(new Hyperlink("auhtor"));
		 * col1.setCellValueFactory(thirdColFactory);
		 */
		/*
		 * col1.setCellFactory(col -> new TableCell<Author, List<Author>>() {
		 * 
		 * @Override public void updateItem(List<Author> authors, boolean empty) {
		 * super.updateItem(authors, empty); if (empty) { setText(null); } else {
		 * setText(authors.stream().map(Author::getFirstName).toString()); } } });
		 */
		// tblAuthors.getColumns().addAll(col1);

		// listView.getItems().addAll(links);
		grid.add(listView, 1, 5);

		ControllerInterface ci = new SystemController();
		List<Author> data = ci.allAuthors();
		cmbAuthors = new ComboBox<Author>();
		cmbAuthors.getItems().addAll(data);
		cmbAuthors.getSelectionModel().selectFirst();
		cmbAuthors.setButtonCell(new ListCell<Author>() {
			@Override
			protected void updateItem(Author t, boolean bln) {
				super.updateItem(t, bln);
				if (t != null) {
					setText(t.getFirstName());
				} else {
					setText(null);
				}
			}
		});

		cmbAuthors.setCellFactory(new Callback<ListView<Author>, ListCell<Author>>() {
			@Override
			public ListCell<Author> call(ListView<Author> p) {
				return new ListCell<Author>() {
					@Override
					protected void updateItem(Author t, boolean bln) {
						super.updateItem(t, bln);
						if (t != null) {
							setText(t.getFirstName());
							// System.out.println("SET PROPERTY " + t.nomeProperty().toString());
						} else {
							setText(null);
						}

					}
				};
			}
		});

		grid.add(cmbAuthors, 1, 6, 2, 1);

		G6Button btnSelect = new G6Button("Select");
		btnSelect.setAlignment(Pos.BASELINE_LEFT);
		btnSelect.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				boolean check = false;
				List<Author> list = listView.getItems();
				for (Author a : list) {
					if (a.equals(cmbAuthors.getValue())) {
						check = true;
						break;
					}
				}
				if (!check)
					listView.getItems().add(cmbAuthors.getValue());
			}
		});

		HBox boxCombo = new HBox();
		boxCombo.getChildren().addAll(cmbAuthors, btnSelect);
		// grid.add(cmbAuthors, 1, 6, 2, 1);
		grid.add(btnSelect, 1, 7, 2, 1);

		G6Label lblLength = new G6Label("Maximum checkout length: ");
		grid.add(lblLength, 0, 8);
		cmbLength = new ComboBox<Integer>();
		cmbLength.getItems().addAll(numbers);
		cmbLength.getSelectionModel().selectFirst();
		grid.add(cmbLength, 1, 8);

		G6Label lblAvailable = new G6Label("Available: ");
		grid.add(lblAvailable, 0, 9);
		CheckBox chkAvailable = new CheckBox("is available");
		grid.add(chkAvailable, 1, 9);

		actionBtn = new G6Button("Add");
		HBox hbBtn = new HBox(11);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(actionBtn);
		grid.add(hbBtn, 1, 15);

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
							ControllerInterface c = new SystemController();
							Book book = new Book(isbn, title, maxLength, authors);

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
					BooksWindow.INSTANCE.show();
				} else if (actionBtn.getText().equals("Update")) {
					BooksWindow.INSTANCE.show();
				}
			}
		});
		Scene scene = new Scene(vbox, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
		scene.getStylesheets().add(getClass().getResource("../library.css").toExternalForm());
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
