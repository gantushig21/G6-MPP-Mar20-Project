package ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import business.Address;
import business.Author;
import business.ControllerInterface;
import business.LibraryMember;
import business.SystemController;
import config.Constants;
import dataaccess.DataAccessFacade;
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

import ui.authors.AuthorInfoWindow;
import ui.authors.AuthorsWindow;

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
		HomeWindow.INSTANCE,
		 AuthorInfoWindow.INSTANCE, AuthorsWindow.INSTANCE 
	};
	

	public static void hideAllWindows() {
		primStage.hide();
		for (Stage st : allWindows) {
			st.hide();
		}
	}

	
	public static void showMembers(boolean refresh) {
    	hideAllWindows();
		if(!MembersWindow.INSTANCE.isInitialized()) {

			MembersWindow.INSTANCE.init();
		}
		
		if (refresh) {
			System.out.println("Show members called");
			ControllerInterface ci = new SystemController();
			List<LibraryMember> members = ci.allMembers();
			Collections.sort(members);
			MembersWindow.INSTANCE.setData(members);			
		}

		MembersWindow.INSTANCE.clear();
		MembersWindow.INSTANCE.show();
	}

	public static void searchMembers(String key) {
    	hideAllWindows();
		if(!MembersWindow.INSTANCE.isInitialized()) {

			MembersWindow.INSTANCE.init();
		}
		
		ControllerInterface ci = new SystemController();
		List<LibraryMember> members = ci.allMembers();
		Collections.sort(members);
		
		List<LibraryMember> filteredMembers = new ArrayList<LibraryMember>();
		for (LibraryMember member: members) {
			if (member.getFirstName().toLowerCase().contains(key) ||
				member.getMemberId().toLowerCase().contains(key) ||
				member.getLastName().toLowerCase().contains(key) ||
				member.getTelephone().toLowerCase().contains(key)
			) {
				filteredMembers.add(member);
			}
		}
		MembersWindow.INSTANCE.setData(filteredMembers);			

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

	public static void showAuthors() {
		hideAllWindows();
		if (!AuthorsWindow.INSTANCE.isInitialized()) {
			AuthorsWindow.INSTANCE.init();
		}
		ControllerInterface ci = new SystemController();
		List<Author> authors = ci.allAuthors();
//		Collections.sort(authors);

		AuthorsWindow.INSTANCE.setData(authors);

		AuthorsWindow.INSTANCE.clear();
		AuthorsWindow.INSTANCE.show();
	}

	
	public static void showCheckouts() {
		// implement this for checkouts like authors
	}
	
	public static void showBooks() {
		// implement this for books like authors
	}
	
	public static void showOverdueBookReports() {
		
	}
	
	public static void addMember() {
    	hideAllWindows();
		if(!MemberInfoWindow.INSTANCE.isInitialized()) {
			MemberInfoWindow.INSTANCE.init();
		}
		MemberInfoWindow.INSTANCE.clear();
		MemberInfoWindow.INSTANCE.show();
		MemberInfoWindow.INSTANCE.addMember();
	}
	
	@Override
	public void start(Stage primaryStage) {
		
		(new DataAccessFacade()).initAuthors();
		primStage = primaryStage;
		primaryStage.setTitle("Main Page");


		BorderPane topContainer = new BorderPane();
		topContainer.setPadding(new Insets(30));

		topContainer.setId("top-container");
		
        HBox splashBox = new HBox();
        G6Label splashLabel = new G6Label("The Library System");
        splashLabel.setFont(Font.font("Kalam", FontWeight.BOLD, 30));
        splashBox.getChildren().add(splashLabel);
        splashBox.setAlignment(Pos.CENTER);
		
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

		// simply displays in ImageView the image as is
		ImageView iv = new ImageView();
//		iv.setImage(image);
//		imageHolder.getChildren().add(iv);
//		imageHolder.setAlignment(Pos.CENTER);
//		HBox splashBox = new HBox();
//		Label splashLabel = new Label("The Library System");
//		splashLabel.setFont(Font.font("Trajan Pro", FontWeight.BOLD, 30));
//		splashBox.getChildren().add(splashLabel);
//		splashBox.setAlignment(Pos.CENTER);
//
//		topContainer.getChildren().add(mainMenu);
//		topContainer.getChildren().add(splashBox);
//		topContainer.getChildren().add(imageHolder);

		Menu optionsMenu = new Menu("Options");
//		MenuItem login = new MenuItem("Login");
//
//		login.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent e) {
//				hideAllWindows();
//				if (!LoginWindow.INSTANCE.isInitialized()) {
//					LoginWindow.INSTANCE.init();
//				}
//				LoginWindow.INSTANCE.clear();
//				LoginWindow.INSTANCE.show();
//			}
//		});

//		MenuItem bookIds = new MenuItem("All Book Ids");
//		bookIds.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent e) {
//				hideAllWindows();
//				if (!AllBooksWindow.INSTANCE.isInitialized()) {
//					AllBooksWindow.INSTANCE.init();
//				}
//				ControllerInterface ci = new SystemController();
//				List<String> ids = ci.allBookIds();
//				Collections.sort(ids);
//				StringBuilder sb = new StringBuilder();
//				for (String s : ids) {
//					sb.append(s + "\n");
//				}
//				AllBooksWindow.INSTANCE.setData(sb.toString());
//				AllBooksWindow.INSTANCE.show();
//			}
//		});
//
//		MenuItem memberIds = new MenuItem("All Member Ids");
//		memberIds.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent e) {
//				hideAllWindows();
//				if (!AllMembersWindow.INSTANCE.isInitialized()) {
//					AllMembersWindow.INSTANCE.init();
//				}
//				ControllerInterface ci = new SystemController();
//				List<String> ids = ci.allMemberIds();
//				Collections.sort(ids);
//				System.out.println(ids);
//				StringBuilder sb = new StringBuilder();
//				for (String s : ids) {
//					sb.append(s + "\n");
//				}
//				System.out.println(sb.toString());
//				AllMembersWindow.INSTANCE.setData(sb.toString());
//				AllMembersWindow.INSTANCE.show();
//			}
//		});
//
//		MenuItem addMember = new MenuItem("Add Member");
//
//		addMember.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent e) {
//				hideAllWindows();
//				if (!MemberInfoWindow.INSTANCE.isInitialized()) {
//					MemberInfoWindow.INSTANCE.init();
//				}
//				MemberInfoWindow.INSTANCE.clear();
//				MemberInfoWindow.INSTANCE.show();
//			}
//		});
//
//		MenuItem getMembers = new MenuItem("Get Members");
//
//		getMembers.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent e) {
//				showMembers();
//			}
//		});
//
//		// AUTHOR START
//		MenuItem addAuthor = new MenuItem("Add Author");
//		addAuthor.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent e) {
//				hideAllWindows();
//				if (!AuthorInfoWindow.INSTANCE.isInitialized()) {
//					AuthorInfoWindow.INSTANCE.init();
//				}
//				AuthorInfoWindow.INSTANCE.clear();
//				AuthorInfoWindow.INSTANCE.show();
//			}
//		});
//
//		MenuItem getAuthors = new MenuItem("GetAuthors");
//
//		getAuthors.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent e) {
//				showAuthors();
//			}
//		});
		// AUTHOR END

//		optionsMenu.getItems().addAll( bookIds, memberIds, addMember, getMembers, getAuthors);


//		mainMenu.getMenus().addAll(optionsMenu);
		Scene scene = new Scene(topContainer, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
		primaryStage.setScene(scene);
		scene.getStylesheets().add(getClass().getResource("library.css").toExternalForm());
		primaryStage.show();
	}

}
