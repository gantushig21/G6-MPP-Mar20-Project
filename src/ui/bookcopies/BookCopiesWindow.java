package ui.bookcopies;

import java.util.List;
import java.util.Optional;

import business.Address;
import business.ControllerInterface;
import business.BookCopy;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
import ui.components.G6VBox;

public class BookCopiesWindow extends Stage implements LibWindow {
	public static final BookCopiesWindow INSTANCE = new BookCopiesWindow();

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

	public void setData(List<BookCopy> data) {
		tableView.setItems(FXCollections.observableList(data));
	}

	/* This class is a singleton */
	private BookCopiesWindow() {
	}

	public void init() {
		G6BorderPane mainPane = new G6BorderPane();
		mainPane.setPadding(new Insets(25));
		mainPane.setId("top-container");

		// Rendering top
		Text scenetitle = new Text("Manage bookCopies");
		StackPane sceneTitlePane = G6Text.withPaddings(scenetitle, new Insets(0));

		scenetitle.setFont(Font.font("Harlow Solid Italic", FontWeight.NORMAL, Constants.PANE_TITLE_FONT_SIZE));
		G6BorderPane topPane = new G6BorderPane();
		topPane.setCenter(sceneTitlePane);
		topPane.setPadding(new Insets(0, 10, 20, 0));

		G6Button backBtn = new G6Button("Back");

		backBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Start.homeWindow();;
			}
		});

		G6Button addBtn = new G6Button("Add a new bookCopy");

		addBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Start.hideAllWindows();
				if (!BookCopyInfoWindow.INSTANCE.isInitialized()) {
					BookCopyInfoWindow.INSTANCE.init();
				}
				BookCopyInfoWindow.INSTANCE.clear();
				BookCopyInfoWindow.INSTANCE.show();
			}
		});

		G6VBox vTopButtons = new G6VBox(10);
		vTopButtons.setAlignment(Pos.BOTTOM_LEFT);
		vTopButtons.getChildren().addAll(backBtn, addBtn);
		topPane.setLeft(vTopButtons);

		mainPane.setTop(topPane);

		// Rendering center
		tableView = new TableView<>();

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

							btnUpdate.setOnAction(event -> {
								BookCopy bookCopy = getTableView().getItems().get(getIndex());

								Start.hideAllWindows();
								if (!BookCopyInfoWindow.INSTANCE.isInitialized()) {
									BookCopyInfoWindow.INSTANCE.init();
								}
								BookCopyInfoWindow.INSTANCE.clear();
								BookCopyInfoWindow.INSTANCE.show();
								BookCopyInfoWindow.INSTANCE.updateBookCopy(bookCopy);

//		                                System.out.println(bookCopy.getFirstName()
//		                                        + "   " + bookCopy.getLastName());
								System.out.println("update");
							});

							btnDelete.setOnAction(event -> {
								BookCopy bookCopy = getTableView().getItems().get(getIndex());

								Optional<ButtonType> result = new G6Alert(AlertType.CONFIRMATION, "Confirmation",
										"Are you sure to delete this record").showAndWait();

								if (result.get() == ButtonType.OK) {
									ControllerInterface c = new SystemController();
									c.deleteBookCopy(bookCopy.getId());
									tableView.getItems().remove(getIndex());
								}

								System.out.println("delete" + getIndex());
							});

							btnCheckout.setOnAction(event -> {
								BookCopy bookCopy = getTableView().getItems().get(getIndex());
								System.out.println("checkout");
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

		tableView.getColumns().add(column0);
		tableView.getColumns().add(column1);
		tableView.getColumns().add(column2);

		tableView.getColumns().add(actionColumn);

		VBox vbox = new VBox(tableView);
		vbox.setPadding(new Insets(0));
		mainPane.setCenter(vbox);

		Scene scene = new Scene(mainPane, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
		// scene.getStylesheets().add(getClass().getResource("../library.css").toExternalForm());
		setScene(scene);

	}
}
