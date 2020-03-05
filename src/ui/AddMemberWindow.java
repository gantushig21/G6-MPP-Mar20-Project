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

public class AddMemberWindow extends Stage implements LibWindow {
	public static final AddMemberWindow INSTANCE = new AddMemberWindow();
	
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
	
    private AddMemberWindow () {}
    
    public void init() { 
        GridPane grid = new GridPane();
        grid.setId("top-container");
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(30, 30, 30, 30));

        Text scenetitle = new Text("Add Member");
        scenetitle.setFont(Font.font("Harlow Solid Italic", FontWeight.NORMAL, 20));
        StackPane titlePane = new StackPane(scenetitle);
        titlePane.setPadding(new Insets(0, 0, 20, 0));
        grid.add(titlePane, 0, 0, 2, 1);

        Label firstNameLbl = new Label("First Name:");
        grid.add(firstNameLbl, 0, 1);
        TextField firstNameTf = new TextField();
        grid.add(firstNameTf, 1, 1);

        Label lastNameLbl = new Label("Last Name:");
        grid.add(lastNameLbl, 0, 2);
        TextField lastNameTf = new TextField();
        grid.add(lastNameTf, 1, 2);

        Label phoneNumberLbl = new Label("Phone Number:");
        grid.add(phoneNumberLbl, 0, 3);
        TextField phoneNumberTf = new TextField();
        grid.add(phoneNumberTf, 1, 3);
        
        Label streetLbl = new Label("Street:");
        grid.add(streetLbl, 0, 4);
        TextField streetTf = new TextField();
        grid.add(streetTf, 1, 4);
        
        Label cityLbl = new Label("City");
        grid.add(cityLbl, 0, 5);
        TextField cityTf = new TextField();
        grid.add(cityTf, 1, 5);

        Label stateLbl = new Label("State");
        grid.add(stateLbl, 0, 6);
        TextField stateTf = new TextField();
        grid.add(stateTf, 1, 6);

        Label zipLbl = new Label("Zip");
        grid.add(zipLbl, 0, 7);
        TextField zipTf = new TextField();
        grid.add(zipTf, 1, 7);
        
        Button addBtn = new Button("Add");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(addBtn);
        grid.add(hbBtn, 1, 8);

        HBox messageBox = new HBox(10);
        messageBox.setAlignment(Pos.BOTTOM_RIGHT);
        messageBox.getChildren().add(messageBar);;
        grid.add(messageBox, 1, 10);
        
        addBtn.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent e) {
        		String firstNameStr = firstNameTf.getText();
        		String lastNameStr = lastNameTf.getText();
        		String phoneNumberStr = phoneNumberTf.getText();
        		String streetStr = streetTf.getText();
        		String cityStr = cityTf.getText();
        		String stateStr = stateTf.getText();
        		String zipStr = zipTf.getText();
        		
        		System.out.println(firstNameStr + " " + lastNameStr + " " + " " + phoneNumberStr + " " + streetStr + " " + cityStr + " " + stateStr + " " + zipStr);
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
        grid.add(hBack, 0, 11);
        Scene scene = new Scene(grid);
        scene.getStylesheets().add(getClass().getResource("library.css").toExternalForm());
        setScene(scene);
        
    }
	
	
}

