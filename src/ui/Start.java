package ui;

import java.util.Collections;
import java.util.List;

import business.ControllerInterface;
import business.LibraryMember;
import business.SystemController;
import config.Constants;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import ui.components.G6Button;
import ui.components.G6Label;
import ui.components.G6VBox;
import ui.members.MemberInfoWindow;
import ui.members.MembersWindow;


public class Start extends Application {
	public static void main(String[] args) {
		launch(args);
	}
	private static Stage primStage = null;
	public static Stage primStage() {
		return primStage;
	}
	
	public static class Colors {
		public static Color green = Color.web("#034220");
		public static Color red = Color.FIREBRICK;
	}
	
	private static Stage[] allWindows = { 
		LoginWindow.INSTANCE,
		AllMembersWindow.INSTANCE,	
		AllBooksWindow.INSTANCE,
		MemberInfoWindow.INSTANCE,
		MembersWindow.INSTANCE,
		HomeWindow.INSTANCE
	};
	
	public static void hideAllWindows() {
		primStage.hide();
		for(Stage st: allWindows) {
			st.hide();
		}
	}
	
	public static void showMembers() {
    	hideAllWindows();
		if(!MembersWindow.INSTANCE.isInitialized()) {
			MembersWindow.INSTANCE.init();
		}
		ControllerInterface ci = new SystemController();
		List<LibraryMember> members = ci.allMembers();
		Collections.sort(members);
		
		MembersWindow.INSTANCE.setData(members);
		
		MembersWindow.INSTANCE.clear();
		MembersWindow.INSTANCE.show();
	}
	
	public static void homeWindow() {
    	hideAllWindows();
		if(!HomeWindow.INSTANCE.isInitialized()) {
			HomeWindow.INSTANCE.init();
		}
		HomeWindow.INSTANCE.show();
	}
	
	@Override
	public void start(Stage primaryStage) {
		primStage = primaryStage;
		primaryStage.setTitle("Main Page");

		BorderPane topContainer = new BorderPane();
		topContainer.setPadding(new Insets(30));
		topContainer.setId("top-container");
		
//		VBox topContainer = new VBox();
//		topContainer.setId("top-container");
//		MenuBar mainMenu = new MenuBar();
//		VBox imageHolder = new VBox();
//		Image image = new Image("ui/library.jpg", 400, 300, false, false);

        // simply displays in ImageView the image as is
//        ImageView iv = new ImageView();
//        iv.setImage(image);
//        imageHolder.getChildren().add(iv);
//        imageHolder.setAlignment(Pos.CENTER);
        HBox splashBox = new HBox();
        G6Label splashLabel = new G6Label("The Library System");
        splashLabel.setFont(Font.font("Kalam", FontWeight.BOLD, 30));
        splashBox.getChildren().add(splashLabel);
        splashBox.setAlignment(Pos.CENTER);
		
//		topContainer.getChildren().add(mainMenu);
//		topContainer.getChildren().add(splashBox);
//		topContainer.getChildren().add(imageHolder);
//		
//		Menu optionsMenu = new Menu("Options");
//		MenuItem login = new MenuItem("Login");
        
        topContainer.setTop(splashBox);
		
        G6Button login = G6Button.createButtonWithLength("Login", Constants.BUTTON_LONG_LENGTH);
		login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	hideAllWindows();
    			if(!LoginWindow.INSTANCE.isInitialized()) {
    				LoginWindow.INSTANCE.init();
    			}
    			LoginWindow.INSTANCE.clear();
    			LoginWindow.INSTANCE.show();
            }
        });			
							
//		MenuItem bookIds = new MenuItem("All Book Ids");
//		bookIds.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent e) {
//				hideAllWindows();
//				if(!AllBooksWindow.INSTANCE.isInitialized()) {
//					AllBooksWindow.INSTANCE.init();
//				}
//				ControllerInterface ci = new SystemController();
//				List<String> ids = ci.allBookIds();
//				Collections.sort(ids);
//				StringBuilder sb = new StringBuilder();
//				for(String s: ids) {
//					sb.append(s + "\n");
//				}
//				AllBooksWindow.INSTANCE.setData(sb.toString());
//				AllBooksWindow.INSTANCE.show();
//            }
//		});
//		
//		MenuItem memberIds = new MenuItem("All Member Ids");
//		memberIds.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent e) {
//				hideAllWindows();
//				if(!AllMembersWindow.INSTANCE.isInitialized()) {
//					AllMembersWindow.INSTANCE.init();
//				}
//				ControllerInterface ci = new SystemController();
//				List<String> ids = ci.allMemberIds();
//				Collections.sort(ids);
//				System.out.println(ids);
//				StringBuilder sb = new StringBuilder();
//				for(String s: ids) {
//					sb.append(s + "\n");
//				}
//				System.out.println(sb.toString());
//				AllMembersWindow.INSTANCE.setData(sb.toString());
//				AllMembersWindow.INSTANCE.show();
//            }
//		});	
//		
//		MenuItem addMember = new MenuItem("Add Member");
//		
//		addMember.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent e) {
//            	hideAllWindows();
//    			if(!MemberInfoWindow.INSTANCE.isInitialized()) {
//    				MemberInfoWindow.INSTANCE.init();
//    			}
//    			MemberInfoWindow.INSTANCE.clear();
//    			MemberInfoWindow.INSTANCE.show();
//            }
//        });			
//
//		MenuItem getMembers = new MenuItem("Get Members");
//		
//		getMembers.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent e) {
//            	getMembers();
//            }
//        });			

//		MenuItem home = new MenuItem("Home");
//		home.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent e) {
//            	hideAllWindows();
//    			if(!HomeWindow.INSTANCE.isInitialized()) {
//    				HomeWindow.INSTANCE.init();
//    			}
//    			HomeWindow.INSTANCE.show();
//            }
//        });		

		G6Button exit = G6Button.createButtonWithLength("Exit", Constants.BUTTON_LONG_LENGTH);
		
		G6VBox centerPane = new G6VBox(10);
		centerPane.setAlignment(Pos.CENTER);
		
		centerPane.getChildren().addAll(login, exit);
		
		topContainer.setCenter(centerPane);
		
//		MenuItem exit = new MenuItem("Exit");
		exit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				hideAllWindows();
				primaryStage.close();
			}
		});
		
		
//		optionsMenu.getItems().addAll(login, exit);

//		mainMenu.getMenus().addAll(optionsMenu);
		Scene scene = new Scene(topContainer, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
		primaryStage.setScene(scene);
		scene.getStylesheets().add(getClass().getResource("library.css").toExternalForm());
		primaryStage.show();
	}
	
}
