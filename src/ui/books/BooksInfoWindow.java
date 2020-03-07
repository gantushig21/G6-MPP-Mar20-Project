package ui.books;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import ui.LibWindow;
import ui.Start;
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

	public void setData(List<Author> data) {
		tblAuthors.setItems(FXCollections.observableList(data));
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
		G6TextField txtTitle = new G6TextField();
		grid.add(txtTitle, 1, 3);

		G6Label lblIsbn = new G6Label("ISBN: ");
		grid.add(lblIsbn, 0, 4);
		G6TextField txtIsbn = new G6TextField();
		grid.add(txtIsbn, 1, 4);

		G6Label lblAuthors = new G6Label("Authors: ");
		lblAuthors.setAlignment(Pos.TOP_LEFT);
		grid.add(lblAuthors, 0, 5);
		// TODO Authors List

		ListView<Hyperlink> listView = new ListView<Hyperlink>();
		List<Hyperlink> links = new ArrayList<>();
		ControllerInterface ci = new SystemController();
		List<Author> data = ci.allAuthors();
		List<String> names = new ArrayList<>();

		for (Author a : data) {
			names.add(a.getFirstName());
		}
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

		ComboBox<Author> cmbAuthors = new ComboBox<Author>();
		cmbAuthors.setItems(FXCollections.observableArrayList(data));

		grid.add(cmbAuthors, 1, 6, 2, 1);

		G6Button btnSelect = new G6Button("Select");
		btnSelect.setOnAction(new EventHandler<ActionEvent>() {

			Hyperlink hp = new Hyperlink();

			@Override
			public void handle(ActionEvent event) {
				hp = new Hyperlink(cmbAuthors.getValue().toString());
				listView.getItems().add(hp);
			}
		});

		grid.add(btnSelect, 1, 7, 2, 1);

		G6Label lblLength = new G6Label("Maximum checkout length: ");
		grid.add(lblLength, 0, 8);
		ComboBox<Integer> cmbLength = new ComboBox<Integer>();
		cmbLength.getItems().addAll(numbers);
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
				ControllerInterface c = new SystemController();
				String title = txtTitle.getText().trim();
				String isbn = txtIsbn.getText().trim();
				List<Author> authors = new ArrayList();
				int maxLength = cmbLength.getValue();

				Book book = new Book(isbn, title, maxLength, authors);
				try {
					c.addBook(book);
				} catch (AlreadyExistException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
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

}
