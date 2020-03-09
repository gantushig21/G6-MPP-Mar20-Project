package ui;

import java.util.ArrayList;
import java.util.List;

import business.Address;
import business.Author;
import business.Book;
import business.SystemController;
import config.Constants;
import dataaccess.Auth;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import ui.components.G6Button;
import ui.components.G6Text;
import ui.components.G6VBox;

public class HomeWindow extends Stage implements LibWindow {
	public static final HomeWindow INSTANCE = new HomeWindow();
	
	private boolean isInitialized = false;
	private G6Text welcomeTxt;
	
	public boolean isInitialized() {
		return isInitialized;
	}
	public void isInitialized(boolean val) {
		isInitialized = val;
	}
	
	private HomeWindow() {}
	
	public void init() {
		BorderPane mainPane = new BorderPane();
		mainPane.setPadding(new Insets(40));
		mainPane.setId("top-container");
		
		welcomeTxt = new G6Text("Welcome, " + (SystemController.currentAuth == Auth.LIBRARIAN ? "Librarian" : "Administrator"));
		
		HBox hTop = new HBox();
		hTop.setAlignment(Pos.TOP_LEFT);
		hTop.getChildren().add(welcomeTxt);
		
		G6VBox centerPane = new G6VBox(10);
		centerPane.setAlignment(Pos.CENTER);
		
		G6Button checkoutBtn = G6Button.createButtonWithLength("Checkout management", Constants.BUTTON_LONG_LENGTH);
		G6Button membersBtn = G6Button.createButtonWithLength("Members management", Constants.BUTTON_LONG_LENGTH);
		G6Button booksBtn = G6Button.createButtonWithLength("Books management", Constants.BUTTON_LONG_LENGTH);
		G6Button authorsBtn = G6Button.createButtonWithLength("Authors management", Constants.BUTTON_LONG_LENGTH);
		G6Button overdueBookReportBtn = G6Button.createButtonWithLength("Overdue book report", Constants.BUTTON_LONG_LENGTH);
		G6Button bookCopiesBtn = G6Button.createButtonWithLength("BookCopies management", Constants.BUTTON_LONG_LENGTH);

		checkoutBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				Start.showCheckouts(true);
			}
		});
		
		membersBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				Start.showMembers(true);
			}
		});
		
		booksBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				Start.showBooks();
			}
		});
		
		authorsBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				Start.showAuthors(true);
			}
		});

		overdueBookReportBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				Start.showOverdueBookReports();
			}
		});
		
//		bookCopiesBtn.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent arg0) {
//				Address a = new Address("test", "test", "test", "test");
//				Author author = new Author("test", "test", "test", a, "test");
//				List<Author> authors = new ArrayList<Author>();
//				authors.add(author);
//				Book book = new Book("text", "text" , 1 , authors);
//				Start.showBookCopies(book);
//			}
//			
//		});

		if (SystemController.currentAuth.equals(Auth.LIBRARIAN) ||
			SystemController.currentAuth.equals(Auth.BOTH) 
			) {
			centerPane.getChildren().addAll(checkoutBtn);
		}
		
		centerPane.getChildren().add(membersBtn);
		
		if (SystemController.currentAuth.equals(Auth.ADMIN) ||
				SystemController.currentAuth.equals(Auth.BOTH) 
				) {
				centerPane.getChildren().addAll(booksBtn, authorsBtn);
		}
		
//		centerPane.getChildren().addAll(overdueBookReportBtn);
				
		G6Button logoutBtn = new G6Button("Logout");
		logoutBtn.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent e) {
        		Start.hideAllWindows();
        		Start.primStage().show();
        	}
        });
        HBox hBottom = new HBox(10);
        hBottom.setAlignment(Pos.BOTTOM_RIGHT);
        hBottom.getChildren().add(logoutBtn);
        
        mainPane.setTop(hTop);
        mainPane.setCenter(centerPane);
        mainPane.setBottom(hBottom);
                
		Scene scene = new Scene(mainPane, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
		JMetro jMetro = new JMetro();
		jMetro.setScene(scene);
//		scene.getStylesheets().add(getClass().getResource("library.css").toExternalForm());
        setScene(scene);
	}
}
