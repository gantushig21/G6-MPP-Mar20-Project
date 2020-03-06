package ui.members;

import java.util.Optional;

import javax.swing.JOptionPane;

import business.Address;
import business.AlreadyExistException;
import business.ControllerInterface;
import business.LibraryMember;
import business.LoginException;
import business.SystemController;
import config.Constants;
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
import ui.components.G6SubTitle;
import ui.components.G6Text;
import ui.components.G6TextField;
import ui.components.G6VBox;

public class MemberInfoWindow extends Stage implements LibWindow {
	public static final MemberInfoWindow INSTANCE = new MemberInfoWindow();
	
	private boolean isInitialized = false;
	
	private G6Text scenetitle;
	private G6Button actionBtn;
	
	private G6TextField firstNameTxtf;
	private G6TextField lastNameTxtf;
	private G6TextField streetTxtf;
	private G6TextField cityTxtf;
	private G6TextField stateTxtf;
	private G6TextField zipTxtf;
	private G6TextField phoneNumberTxtf;
	
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
	private LibraryMember currentMember;
	
    private MemberInfoWindow () {
    }
    
	private void clearFields() {
		firstNameTxtf.setText(""); 
		lastNameTxtf.setText("");
		streetTxtf.setText("");
		cityTxtf.setText("");
		stateTxtf.setText("");
		zipTxtf.setText("");
		phoneNumberTxtf.setText("");
	}
	
	public void updateMember(LibraryMember member) {
		scenetitle.setText("Update member ID " + member.getMemberId());
		actionBtn.setText("Update");
		
		firstNameTxtf.setText(member.getFirstName()); 
		lastNameTxtf.setText(member.getLastName());
		
		Address address = member.getAddress();
		
		streetTxtf.setText(address.getStreet());
		cityTxtf.setText(address.getCity());
		stateTxtf.setText(address.getState());
		zipTxtf.setText(address.getZip());
		phoneNumberTxtf.setText(member.getTelephone());
		
		currentMember = member;
	}
    
    public void init() {
    	G6VBox vbox = new G6VBox(5);
    	vbox.setPadding(new Insets(25));
    	vbox.setId("top-container");
    	
        scenetitle = new G6Text("Add new member");
        StackPane sceneTitlePane = G6Text.withPaddings(scenetitle, new Insets(0));
        
        scenetitle.setFont(Font.font("Harlow Solid Italic", FontWeight.NORMAL, Constants.PANE_TITLE_FONT_SIZE)); 
        G6BorderPane topPane = new G6BorderPane();
        topPane.setCenter(sceneTitlePane);
        topPane.setPadding(new Insets(0, 10, 20, 0));
        
        G6Button backBtn = new G6Button("Back");
        
        G6HBox hBack = new G6HBox(10);
        hBack.setAlignment(Pos.BOTTOM_LEFT);
        hBack.getChildren().add(backBtn);
        topPane.setLeft(hBack);

    	G6GridPane grid = new G6GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(5);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        vbox.getChildren().addAll(topPane, grid);

        
        grid.add(topPane, 0, 0, 2, 1);

        // Name section
        G6SubTitle nameTxt = new G6SubTitle("Name");
        grid.add(nameTxt, 0, 2);
        
        G6Label firstNameLbl = new G6Label("First Name: ");         
        grid.add(firstNameLbl, 0, 3);
        firstNameTxtf = new G6TextField(Constants.TEXT_FIELD_WIDTH_LONG);
        grid.add(firstNameTxtf, 1, 3);

        G6Label lastNameLbl = new G6Label("Last Name: ");         
        grid.add(lastNameLbl, 0, 4);
        lastNameTxtf = new G6TextField(Constants.TEXT_FIELD_WIDTH_LONG);
        grid.add(lastNameTxtf, 1, 4);
        
        // Address Section
        G6SubTitle addressTxt = new G6SubTitle("Address");
        grid.add(addressTxt, 0, 6);
        
        G6Label StreetLbl = new G6Label("Street: ");         
        grid.add(StreetLbl, 0, 7);
        streetTxtf = new G6TextField(Constants.TEXT_FIELD_WIDTH_LONG);
        grid.add(streetTxtf, 1, 7);

        G6Label CityLbl = new G6Label("City: ");         
        grid.add(CityLbl, 0, 8);
        cityTxtf = new G6TextField(Constants.TEXT_FIELD_WIDTH_LONG);
        grid.add(cityTxtf, 1, 8);

        G6Label StateLbl = new G6Label("State: ");         
        grid.add(StateLbl, 0, 9);
        stateTxtf = new G6TextField(Constants.TEXT_FIELD_WIDTH_LONG);
        grid.add(stateTxtf, 1, 9);

        G6Label ZipLbl = new G6Label("Zip: ");         
        grid.add(ZipLbl, 0, 10);
        zipTxtf = new G6TextField(Constants.TEXT_FIELD_WIDTH_LONG);
        grid.add(zipTxtf, 1, 10);
        
        // Contact Section
        G6SubTitle contactTxt = new G6SubTitle("Contact");
        grid.add(contactTxt, 0, 12);
        
        G6Label PhoneNumberLbl = new G6Label("Phone Number: ");         
        grid.add(PhoneNumberLbl, 0, 13);
        phoneNumberTxtf = new G6TextField(Constants.TEXT_FIELD_WIDTH_LONG);
        grid.add(phoneNumberTxtf, 1, 13);

        actionBtn = new G6Button("Add");
        HBox hbBtn = new HBox(11);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(actionBtn);
        grid.add(hbBtn, 1, 15);

        HBox messageBox = new HBox(10);
        messageBox.setAlignment(Pos.BOTTOM_RIGHT);
        messageBox.getChildren().add(messageBar);;
        grid.add(messageBox, 1, 16);
        
        actionBtn.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent e) {
          	   String firstNameStr = firstNameTxtf.getText().trim();
         	   String lastNameStr = lastNameTxtf.getText().trim();
         	   String streetStr = streetTxtf.getText().trim();
         	   String cityStr = cityTxtf.getText().trim();
         	   String stateStr = stateTxtf.getText().trim();
         	   String zipStr = zipTxtf.getText().trim();
         	   String phoneNumberStr = phoneNumberTxtf.getText().trim();
         	   
         	   try {
					RuleSet ruleSet = RuleSetFactory.getRuleSet(MemberInfoWindow.this);
					ruleSet.applyRules(MemberInfoWindow.this);
					
					Optional<ButtonType> result;
					
	        		if (actionBtn.getText().equals("Add")) {
	            	   System.out.println(firstNameStr + " " +
	            			   lastNameStr + " " +
	            			   streetStr + " " + 
	            			   cityStr + " " +
	            			   stateStr + " " +
	            			   zipStr + " " + 
	            			   phoneNumberStr
	            	   );
            	   
       					
       					result = new G6Alert(AlertType.CONFIRMATION, "Confirmation", "Are you sure to create a new member?").showAndWait();
       					
       					if (result.get() == ButtonType.OK) {       						
       	            	   ControllerInterface c = new SystemController();
       	            	   c.addMember(new LibraryMember(
       	            			   "611611", 
       	            			   firstNameStr, 
       	            			   lastNameStr, 
       	            			   phoneNumberStr, 
       	            			   new Address(streetStr, cityStr, stateStr, zipStr)));
       	            	   
       	            	   result = new G6Alert(AlertType.NONE, "Success", "The member is added successful", ButtonType.OK).showAndWait();	       					
       	            	   if (result.get() == ButtonType.OK) {
	           					clearFields();
       	            	   }       	            	   
       					} else {
       						System.out.println("Canceled");
       					}
       					
	        		} else if (actionBtn.getText().equals("Update")) {
	        			currentMember.setFirstName(firstNameStr);
	        			currentMember.setLastName(lastNameStr);
	        			currentMember.setTelephone(phoneNumberStr);
	        			currentMember.getAddress().setStreet(streetStr);
	        			currentMember.getAddress().setState(stateStr);
	        			currentMember.getAddress().setCity(cityStr);
	        			currentMember.getAddress().setZip(zipStr);
	        			
	        			result = new G6Alert(AlertType.CONFIRMATION, "Confirmation", "Are you sure to update this memeber").showAndWait();
	        			if (result.get() == ButtonType.OK) {
	        				ControllerInterface c = new SystemController();
	        				c.updateMember(currentMember);
	        				
	        				Start.showMembers();
	        			}
	        			
	        		}
  				} catch (RuleException ex) {		
   					showErrorDialog("Error", ex.getMessage());
   				} catch (AlreadyExistException ex) {
   					showErrorDialog("Error", ex.getMessage());	   					
   				}
        	
        	}
        });

        backBtn.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent e) {
        		Start.hideAllWindows();
        		if (actionBtn.getText().equals("Add")) {
            		Start.primStage().show();        			
        		} else if (actionBtn.getText().equals("Update")) {
        			MembersWindow.INSTANCE.clear();
        			MembersWindow.INSTANCE.show();
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
    
	public String getFirstNameValue() {
		return firstNameTxtf.getText();
	}
	public String getLastNameValue() {
		return lastNameTxtf.getText();
	}
	public String getStreetValue() {
		return streetTxtf.getText();
	}
	public String getCityValue() {
		return cityTxtf.getText();
	}
	public String getStateValue() {
		return stateTxtf.getText();
	}
	public String getZipValue() {
		return zipTxtf.getText();
	}
	public String getPhoneNumberValue() {
		return phoneNumberTxtf.getText();
	}
	
	public static MemberInfoWindow getInstance() {
		return INSTANCE;
	}
	public Text getMessageBar() {
		return messageBar;
	}	
    
}

