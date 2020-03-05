package ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AddBookWindow extends Stage implements LibWindow {
	public static final AddBookWindow INSTANCE = new AddBookWindow();
	
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
	
    private AddBookWindow () {}
    
    public void init() { 
        GridPane grid = new GridPane();
        grid.setId("top-container");
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(30, 30, 30, 30));

        Text scenetitle = new Text("Add Book");
        scenetitle.setFont(Font.font("Harlow Solid Italic", FontWeight.NORMAL, 20));
        StackPane titlePane = new StackPane(scenetitle);
        titlePane.setPadding(new Insets(0, 0, 20, 0));
        grid.add(titlePane, 0, 0, 2, 1);

        Label isbn = new Label("ISBN:");
        grid.add(isbn, 0, 1);

        TextField isbnTf = new TextField();
        grid.add(isbnTf, 1, 1);

        Label title = new Label("Title:");
        grid.add(title, 0, 2);

        TextField titleTf = new TextField();
        grid.add(titleTf, 1, 2);

        Label authors = new Label("Authors:");
        grid.add(authors, 0, 3);

        TextField authorsTf = new TextField();
        grid.add(authorsTf, 1, 3);

        Label maximumCheckout = new Label("Maximum Checkout Length:");
        grid.add(maximumCheckout, 0, 4);

        TextField maximumCheckoutTf = new TextField();
        grid.add(maximumCheckoutTf, 1, 4);

        Label numberOfCopies = new Label("Number of Copies:");
        grid.add(numberOfCopies, 0, 5);

        TextField numberOfCopiesTf = new TextField();
        grid.add(numberOfCopiesTf, 1, 5);
        
        Button addBtn = new Button("Add");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(addBtn);
        grid.add(hbBtn, 1, 7);

        HBox messageBox = new HBox(10);
        messageBox.setAlignment(Pos.BOTTOM_RIGHT);
        messageBox.getChildren().add(messageBar);;
        grid.add(messageBox, 1, 9);
        
        addBtn.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent e) {
        		String isbnString = isbnTf.getText();
        		String titleString = titleTf.getText();
        		String authorsString = authorsTf.getText();
        		String maximumCheckoutString = maximumCheckoutTf.getText();
        		String numberOfCopiesString = numberOfCopiesTf.getText();
        		
        		System.out.println(isbnString + " " + titleString + " " + authorsString + " " + maximumCheckoutString + " " + numberOfCopiesString);
        	}
        });

        Button backBtn = new Button("Back to Main");
        backBtn.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent e) {
        		Start.hideAllWindows();
        		Start.primStage().show();
        	}
        });
        HBox hBack = new HBox(10);
        hBack.setAlignment(Pos.BOTTOM_LEFT);
        hBack.getChildren().add(backBtn);
        grid.add(hBack, 0, 10);
        Scene scene = new Scene(grid);
        scene.getStylesheets().add(getClass().getResource("library.css").toExternalForm());
        setScene(scene);
        
    }
	
	
}

