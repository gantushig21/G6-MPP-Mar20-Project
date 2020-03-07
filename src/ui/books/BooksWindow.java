package ui.books;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import business.Author;
import business.Book;
import business.ControllerInterface;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import ui.LibWindow;
import ui.Start;
import ui.components.G6Alert;
import ui.components.G6BorderPane;
import ui.components.G6Button;
import ui.components.G6HBox;
import ui.components.G6TableView;
import ui.components.G6Text;
import ui.components.G6TextField;

public class BooksWindow extends Stage implements LibWindow {

	public static final BooksWindow INSTANCE = new BooksWindow();

	private boolean isInitialized = false;

	public boolean isInitialized() {
		return isInitialized;
	}

	public void isInitialized(boolean val) {
		isInitialized = val;
	}

	private TableView<Book> tblBooks;

	public void setData(List<Book> data) {
		tblBooks.setItems(FXCollections.observableList(data));
	}

	private Text messageBar = new Text();

	public void clear() {
		messageBar.setText("");
	}

	private BooksWindow() {
	}

	@Override
	public void init() {
		G6BorderPane mainPane = new G6BorderPane();
		mainPane.setPadding(new Insets(25));
		mainPane.setId("top-container");

		// Rendering top
		Text scenetitle = new Text("Manage book");
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
				;
			}
		});

		G6HBox hBack = new G6HBox(10);
		hBack.setAlignment(Pos.BOTTOM_LEFT);
		hBack.getChildren().add(backBtn);
		topPane.setLeft(hBack);

		G6Button btnAdd = new G6Button("Add Book");

		btnAdd.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Start.hideAllWindows();
				if (!BooksInfoWindow.INSTANCE.isInitialized()) {
					BooksInfoWindow.INSTANCE.init();
				}
				// BooksInfoWindow.INSTANCE.clear();
				ControllerInterface ci = new SystemController();
				List<Author> authors = ci.allAuthors();
				// BooksInfoWindow.INSTANCE.setData(authors);
				BooksInfoWindow.INSTANCE.show();
				// BooksInfoWindow.INSTANCE.updateMember(member);
			}
		});
		/*
		 * G6HBox hBox = new G6HBox(10); hBox.setAlignment(Pos.BOTTOM_LEFT);
		 * hBox.setPrefWidth(200); hBox.getChildren().addAll(new SearchBox(), btnAdd);
		 */
		G6BorderPane nextPane = new G6BorderPane();
		nextPane.setLeft(new SearchBox());
		nextPane.setPadding(new Insets(0, 10, 10, 0));
		nextPane.setRight(btnAdd);
		nextPane.setTop(topPane);

		mainPane.setTop(nextPane);

		tblBooks = new G6TableView<>();
		TableColumn<Book, String> colId = new TableColumn<>("ID");
		colId.setCellValueFactory(new PropertyValueFactory<>("id"));
		TableColumn<Book, String> colTitle = new TableColumn<>("Title");
		colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
		TableColumn<Book, String> colISBN = new TableColumn<>("ISBN");
		colISBN.setCellValueFactory(new PropertyValueFactory<>("isbn"));
		TableColumn<Book, List<Author>> colAuthor = new TableColumn<>("Author");
		PropertyValueFactory<Book, List<Author>> thirdColFactory = new PropertyValueFactory<>("authors");
		colAuthor.setCellValueFactory(thirdColFactory);

		colAuthor.setCellFactory(col -> new TableCell<Book, List<Author>>() {
			@Override
			public void updateItem(List<Author> authors, boolean empty) {
				super.updateItem(authors, empty);
				if (empty) {
					setText(null);
				} else {
					setText(authors.stream().map(Author::getFirstName).collect(Collectors.joining(", ")));
				}
			}
		});

		/*
		 * TableColumn<Book, String> colDate = new TableColumn<>("Create date");
		 * colAuthor.setCellValueFactory(new PropertyValueFactory<>("authors"));
		 */

		TableColumn<Book, String> actionColumn = new TableColumn<>("Action");

		Callback<TableColumn<Book, String>, TableCell<Book, String>> cellFactory = new Callback<TableColumn<Book, String>, TableCell<Book, String>>() {

			@Override
			public TableCell<Book, String> call(TableColumn<Book, String> arg0) {
				final TableCell<Book, String> cell = new TableCell<Book, String>() {

					final G6Button btnUpdate = new G6Button("Update");
					final G6Button btnDelete = new G6Button("Delete");
					final G6Button btnCheckout = new G6Button("Book Copies");

					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
							setText(null);
						} else {
							G6HBox hbox = new G6HBox(5);

							btnUpdate.setOnAction(event -> {

								Book book = getTableView().getItems().get(getIndex());

								Start.hideAllWindows();
								if (!BooksInfoWindow.INSTANCE.isInitialized()) {
									BooksInfoWindow.INSTANCE.init();
								}
								BooksInfoWindow.INSTANCE.show();
								BooksInfoWindow.INSTANCE.updateBook(book);

								System.out.println("update");

							});

							btnDelete.setOnAction(event -> {

								Book book = getTableView().getItems().get(getIndex());

								Optional<ButtonType> result = new G6Alert(AlertType.CONFIRMATION, "Confirmation",
										"Are you sure to delete this book").showAndWait();

								if (result.get() == ButtonType.OK) {
									ControllerInterface c = new SystemController();
									c.deleteBook(book.getIsbn());

									tblBooks.getItems().remove(getIndex());
								}

								System.out.println("delete" + getIndex());

							});

							btnCheckout.setOnAction(event -> {
								/*
								 * LibraryMember member = getTableView().getItems().get(getIndex());
								 * System.out.println("checkout");
								 */
							});

							hbox.getChildren().addAll(btnUpdate, btnDelete, btnCheckout);

							setGraphic(hbox);
							setText(null);
						}
					}
				};
				return cell;
			}
		};

		actionColumn.setCellFactory(cellFactory);

		tblBooks.getColumns().add(colId);
		tblBooks.getColumns().add(colISBN);
		tblBooks.getColumns().add(colTitle);
		tblBooks.getColumns().add(colAuthor);
		tblBooks.getColumns().add(actionColumn);

		VBox vbox = new VBox(tblBooks);
		vbox.setPadding(new Insets(0));
		mainPane.setCenter(vbox);

		Scene scene = new Scene(mainPane, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
		scene.getStylesheets().add(getClass().getResource("../library.css").toExternalForm());
		setScene(scene);

		/*
		 * VBox tblVBox = new VBox(tblBooks); tblVBox.setPadding(new Insets(0));
		 * 
		 * grid.add(tblVBox, 0, 2); grid.add(vbox, 0, 1); grid.add(hbox, 0, 0); Scene
		 * scene = new Scene(grid, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
		 * setScene(scene);
		 */
	}

	private static class SearchBox extends Region {

		private G6TextField textBox;
		private G6Button clearButton;

		public SearchBox() {
			setId("SearchBox");
			// getStyleClass().add("search-box");
			setMinHeight(24);
			// setPrefSize(200, 24);
			// setMaxSize(Control.USE_PREF_SIZE, Control.USE_PREF_SIZE);
			textBox = new G6TextField();
			textBox.setPromptText("Search");
			clearButton = new G6Button("");
			clearButton.setVisible(false);
			getChildren().addAll(textBox, clearButton);
			clearButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent actionEvent) {
					textBox.setText("");
					textBox.requestFocus();
				}
			});
			textBox.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					clearButton.setVisible(textBox.getText().length() != 0);
				}
			});
		}

	}

	/*
	 * @Override public void start(Stage stage) throws Exception { // TODO
	 * Auto-generated method stub
	 * 
	 * stage.setTitle("Welcome user!"); G6GridPane grid = new G6GridPane();
	 * grid.setAlignment(Pos.CENTER); grid.setHgap(10); grid.setVgap(10);
	 * grid.setPadding(new Insets(25, 25, 25, 25));
	 * 
	 * G6Text scenetitle = new G6Text("Manage Books");
	 * scenetitle.setFont(Font.font("Harlow Solid Bold", FontWeight.NORMAL, 20)); //
	 * Tahoma // grid.add(scenetitle, 0, 0, 2, 1);
	 * 
	 * G6Button btnBack = new G6Button("Back");
	 * 
	 * G6HBox hbox = new G6HBox(10); hbox.setAlignment(Pos.TOP_CENTER);
	 * hbox.getChildren().addAll(btnBack, scenetitle);
	 * 
	 * G6Button btnAdd = new G6Button("Add new Book");
	 * 
	 * G6HBox vbox = new G6HBox(10); // vbox.getStylesheets().add(searchBoxCss); //
	 * vbox.setPrefWidth(200); // vbox.setMaxWidth(Control.USE_PREF_SIZE);
	 * vbox.getChildren().addAll(new SearchBox(), btnAdd);
	 * 
	 * G6TableView<Book> tblBooks = new G6TableView<>(); TableColumn<Book, String>
	 * colId = new TableColumn<>("ID"); colId.setCellValueFactory(new
	 * PropertyValueFactory<>("bookId")); TableColumn<Book, String> colTitle = new
	 * TableColumn<>("Title"); colTitle.setCellValueFactory(new
	 * PropertyValueFactory<>("title")); TableColumn<Book, String> colISBN = new
	 * TableColumn<>("ISBN"); colISBN.setCellValueFactory(new
	 * PropertyValueFactory<>("isbn")); TableColumn<Book, String> colAuthor = new
	 * TableColumn<>("Author"); colAuthor.setCellValueFactory(new
	 * PropertyValueFactory<>("authors"));
	 * 
	 * TableColumn<Book, String> colDate = new TableColumn<>("Create date");
	 * colAuthor.setCellValueFactory(new PropertyValueFactory<>("authors"));
	 * 
	 * 
	 * TableColumn<Book, String> actionColumn = new TableColumn<>("Action");
	 * 
	 * Callback<TableColumn<Book, String>, TableCell<Book, String>> cellFactory =
	 * new Callback<TableColumn<Book, String>, TableCell<Book, String>>() {
	 * 
	 * @Override public TableCell<Book, String> call(TableColumn<Book, String> arg0)
	 * { final TableCell<Book, String> cell = new TableCell<Book, String>() {
	 * 
	 * final G6Button btnUpdate = new G6Button("Update"); final G6Button btnDelete =
	 * new G6Button("Delete"); final G6Button btnCheckout = new
	 * G6Button("Checkout");
	 * 
	 * @Override public void updateItem(String item, boolean empty) {
	 * super.updateItem(item, empty); if (empty) { setGraphic(null); setText(null);
	 * } else { G6HBox hbox = new G6HBox(5);
	 * 
	 * btnUpdate.setOnAction(event -> {
	 * 
	 * Book member = getTableView().getItems().get(getIndex());
	 * 
	 * Start.hideAllWindows(); if (!MemberInfoWindow.INSTANCE.isInitialized()) {
	 * MemberInfoWindow.INSTANCE.init(); } MemberInfoWindow.INSTANCE.clear();
	 * MemberInfoWindow.INSTANCE.show();
	 * MemberInfoWindow.INSTANCE.updateMember(member);
	 * 
	 * System.out.println("update");
	 * 
	 * });
	 * 
	 * btnDelete.setOnAction(event -> {
	 * 
	 * LibraryMember member = getTableView().getItems().get(getIndex());
	 *
	 * Optional<ButtonType> result = new G6Alert(AlertType.CONFIRMATION,
	 * "Confirmation", "Are you sure to delete this record").showAndWait();
	 * 
	 * if (result.get() == ButtonType.OK) { ControllerInterface c = new
	 * SystemController(); c.deleteMember(member.getMemberId());
	 * 
	 * tblBooks.getItems().remove(getIndex()); }
	 * 
	 * System.out.println("delete" + getIndex());
	 * 
	 * });
	 * 
	 * btnCheckout.setOnAction(event -> {
	 * 
	 * LibraryMember member = getTableView().getItems().get(getIndex());
	 * System.out.println("checkout");
	 * 
	 * });
	 * 
	 * hbox.getChildren().addAll(btnUpdate, btnDelete, btnCheckout);
	 * 
	 * setGraphic(hbox); setText(null); } } }; return cell; } };
	 * 
	 * actionColumn.setCellFactory(cellFactory);
	 * 
	 * tblBooks.getColumns().add(colId); tblBooks.getColumns().add(colISBN);
	 * tblBooks.getColumns().add(colAuthor); tblBooks.getColumns().add(colTitle);
	 * tblBooks.getColumns().add(actionColumn); grid.add(tblBooks, 0, 2);
	 * grid.add(vbox, 0, 1); grid.add(hbox, 0, 0); Scene scene = new Scene(grid);
	 * stage.setScene(scene); stage.show(); }
	 */

}
