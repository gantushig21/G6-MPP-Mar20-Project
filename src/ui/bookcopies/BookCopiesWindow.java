package ui.bookcopies;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import business.Address;
import business.Author;
import business.Book;
import business.ControllerInterface;
import business.BookCopy;
import business.SystemController;
import config.Constants;
import dataaccess.Auth;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import jfxtras.styles.jmetro.JMetro;
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

public class BookCopiesWindow extends Stage implements LibWindow {
	public static final BookCopiesWindow INSTANCE = new BookCopiesWindow();
	private Book book;
	private boolean isInitialized = false;

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

	private TableView<BookCopy> tableView;

	public void setData(Book book) {
		this.book = book;
	}

	/* This class is a singleton */
	private BookCopiesWindow() {
	}

	public void init() {
		G6BorderPane mainPane = new G6BorderPane();
		mainPane.setPadding(new Insets(35));
		mainPane.setId("top-container");

		// Rendering top
		Text scenetitle = new Text("Manage Copies");
		StackPane sceneTitlePane = G6Text.withPaddings(scenetitle, new Insets(0));

		scenetitle.setFont(Font.font("Harlow Solid Italic", FontWeight.NORMAL, Constants.PANE_TITLE_FONT_SIZE));
		G6BorderPane topPane = new G6BorderPane();
		topPane.setCenter(sceneTitlePane);
//		topPane.setPadding(new Insets(0, 10, 20, 0));

		G6Button backBtn = new G6Button("Back");

		backBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Start.showBooks();
			}
		});

		G6VBox vTopButtons = new G6VBox(10);
		vTopButtons.setAlignment(Pos.BOTTOM_LEFT);
		topPane.setLeft(backBtn);
		mainPane.setTop(topPane);

		G6GridPane bookInfoGrid = new G6GridPane();
		bookInfoGrid.setAlignment(Pos.CENTER);
		bookInfoGrid.setHgap(10);
		bookInfoGrid.setVgap(5);

		G6Label titleLbl = new G6Label("Title: ");
		bookInfoGrid.add(titleLbl, 0, 2);
		G6Text titleTxt = new G6Text(book.getTitle());
		bookInfoGrid.add(titleTxt, 1, 2);

		G6Label isbnLbl = new G6Label("ISBN: ");
		bookInfoGrid.add(isbnLbl, 0, 3);
		G6Text isbnTxt = new G6Text(book.getIsbn());
		bookInfoGrid.add(isbnTxt, 1, 3);

		G6Label authorsLbl = new G6Label("Authors: ");
		bookInfoGrid.add(authorsLbl, 0, 4);
		List<String> authorsFullnames = book.getAuthors().stream().map(a -> (a.getFirstName() + " " + a.getLastName()))
				.collect(Collectors.toList());
		G6Text authorsTxt = new G6Text(String.join(", ", authorsFullnames));
		bookInfoGrid.add(authorsTxt, 1, 4);
		bookInfoGrid.setMaxWidth(Double.MAX_VALUE);

		G6Button addBtn = new G6Button("Add a new book copy");
		addBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Start.hideAllWindows();
				BookCopyInfoWindow.INSTANCE.setBook(book);
				if (!BookCopyInfoWindow.INSTANCE.isInitialized()) {
					BookCopyInfoWindow.INSTANCE.init();
				}
				BookCopyInfoWindow.INSTANCE.clear();
				BookCopyInfoWindow.INSTANCE.show();
			}
		});

		G6HBox centerHBox = new G6HBox(2);
		centerHBox.setPadding(new Insets(0, 0, 20, 0));
		centerHBox.setAlignment(Pos.BOTTOM_RIGHT);

		G6HBox.setHgrow(bookInfoGrid, Priority.ALWAYS);
		centerHBox.getChildren().addAll(bookInfoGrid);
		if (SystemController.currentAuth.equals(Auth.ADMIN) ||
				SystemController.currentAuth.equals(Auth.BOTH) 
				) {
			centerHBox.getChildren().addAll(addBtn);
		}
		mainPane.setCenter(centerHBox);

		// Rendering center
		tableView = new TableView<>();
		tableView.setItems(FXCollections.observableArrayList(book.getCopies()));

		TableColumn<BookCopy, String> column0 = new TableColumn<>("ID");
		column0.setCellValueFactory(new PropertyValueFactory<>("id"));

		TableColumn<BookCopy, String> column1 = new TableColumn<>("Copy Number");
		column1.setCellValueFactory(new PropertyValueFactory<>("copyNum"));

		TableColumn<BookCopy, Boolean> column2 = new TableColumn<>("Available");
		column2.setCellValueFactory(new PropertyValueFactory<>("isAvailable"));

		TableColumn<BookCopy, String> actionColumn = new TableColumn<>("Action");

		Callback<TableColumn<BookCopy, String>, TableCell<BookCopy, String>> cellFactory = new Callback<TableColumn<BookCopy, String>, TableCell<BookCopy, String>>() {

			@Override
			public TableCell<BookCopy, String> call(TableColumn<BookCopy, String> arg0) {
				final TableCell<BookCopy, String> cell = new TableCell<BookCopy, String>() {

					final G6Button btnUpdate = new G6Button("Update");
					final G6Button btnDelete = new G6Button("Delete");
					final G6Button btnCheckout = new G6Button("Checkout");

					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
							setText(null);
						} else {
							G6HBox hbox = new G6HBox(5);

//							btnUpdate.setOnAction(event -> {
//								BookCopy bookCopy = getTableView().getItems().get(getIndex());
//
//								Start.hideAllWindows();
//								BookCopyInfoWindow.INSTANCE.setBook(book);
//								if (!BookCopyInfoWindow.INSTANCE.isInitialized()) {
//									BookCopyInfoWindow.INSTANCE.init();
//								}
//								BookCopyInfoWindow.INSTANCE.clear();
//								BookCopyInfoWindow.INSTANCE.show();
//								
//								BookCopyInfoWindow.INSTANCE.updateBookCopy(bookCopy);
//
//								System.out.println("update");
//							});

//							btnDelete.setOnAction(event -> {
//								BookCopy bookCopy = getTableView().getItems().get(getIndex());
//
//								Optional<ButtonType> result = new G6Alert(AlertType.CONFIRMATION, "Confirmation",
//										"Are you sure to delete this record").showAndWait();
//
//								if (result.get() == ButtonType.OK) {
//									ControllerInterface c = new SystemController();
//									BookCopy[] copies = book.getCopies();
//									BookCopy[] newCopies = 
//									for (int i = )
//									c.deleteBookCopy(bookCopy.getId());
//									tableView.getItems().remove(getIndex());
//								}
//
//								System.out.println("delete" + getIndex());
//							});

//							btnCheckout.setOnAction(event -> {
//								BookCopy bookCopy = getTableView().getItems().get(getIndex());
//								System.out.println("checkout");
//							});

//							hbox.getChildren().addAll(btnUpdate, btnDelete, btnCheckout);

							setGraphic(hbox);
							setText(null);
						}
					}
				};
				return cell;
			}
		};

		actionColumn.setCellFactory(cellFactory);

		tableView.getColumns().add(column0);
		tableView.getColumns().add(column1);
		tableView.getColumns().add(column2);

//		tableView.getColumns().add(actionColumn);

		VBox vbox = new VBox(tableView);
		vbox.setPadding(new Insets(0));
		mainPane.setBottom(vbox);

		Scene scene = new Scene(mainPane, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
		
		JMetro jMetro = new JMetro();
		jMetro.setScene(scene);
		
		// scene.getStylesheets().add(getClass().getResource("../library.css").toExternalForm());
		setScene(scene);

	}
}
