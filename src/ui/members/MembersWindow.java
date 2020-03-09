package ui.members;


import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import business.Address;
import business.ControllerInterface;
import business.LibraryMember;
import business.SystemController;
import config.Constants;
import dataaccess.Auth;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import ui.LibWindow;
import ui.Start;
import ui.components.G6Alert;
import ui.components.G6BorderPane;
import ui.components.G6Button;
import ui.components.G6FlowPane;
import ui.components.G6HBox;
import ui.components.G6Label;
import ui.components.G6TableView;
import ui.components.G6Text;
import ui.components.G6TextField;
import ui.components.G6VBox;

public class MembersWindow extends Stage implements LibWindow {
	public static final MembersWindow INSTANCE = new MembersWindow();
	
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
	
	private TableView<LibraryMember> tableView;
	private int page = 1, pages = 1;
	private G6Button btnPrev;
	private G6Label pageLbl;
	private G6Button btnNext;
	private List<LibraryMember> list;
	
	public void setData(List<LibraryMember> data) {
		list = data;
		
		page = 1;
		pages = list.size() / Constants.PAGE_LIMIT;
		if (list.size() % Constants.PAGE_LIMIT != 0)
			pages++;
		
		controlPageButtonDisable();
		setPage(page);
//		tableView.setItems(FXCollections.observableList(data));
	}
	
	private void prevPage() {
		if (page > 1) {
			page--;
			controlPageButtonDisable();
			setPage(page);
		}
	}
	
	private void setPage(int page) {
		tableView.getItems().clear();
		
		pageLbl.setText(page + "/" + pages);
		int start = (page - 1) * Constants.PAGE_LIMIT;
		int end = page * Constants.PAGE_LIMIT >= list.size() ? list.size() : page * Constants.PAGE_LIMIT;
		for (int i = start; i < end; i++) {
			tableView.getItems().add(list.get(i));
		}
	}
	
	private void controlPageButtonDisable() {
		if (page < pages) {
			btnNext.setDisable(false);
		} else {
			btnNext.setDisable(true);
		}
		
		if (page > 1) {
			btnPrev.setDisable(false);
		} else {
			btnPrev.setDisable(true);
		}
	}
	
	private void nextPage() {
		if (page < pages) {
			page++;
			controlPageButtonDisable();
			setPage(page);
		}
	}

	
	/* This class is a singleton */
    private MembersWindow () {}
    
    public void init() {
    	isInitialized(true);
    	
    	G6BorderPane mainPane = new G6BorderPane();
    	mainPane.setPadding(new Insets(25));
    	mainPane.setId("top-container");
    	
    	// Rendering top
    	G6VBox topPane = new G6VBox(15);
    	topPane.setPadding(new Insets(15, 0, 15, 0));
    	
    	G6BorderPane top1 = new G6BorderPane();
        Text scenetitle = new Text("Manage members");
        
        scenetitle.setFont(Font.font("Harlow Solid Italic", FontWeight.NORMAL, Constants.PANE_TITLE_FONT_SIZE)); 
        
        G6Button backBtn = new G6Button("Back");
        
        backBtn.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent e) {
        		Start.homeWindow();
        	}
        });

    	top1.setLeft(backBtn);
    	top1.setCenter(scenetitle);
    	
    	G6BorderPane top2 = new G6BorderPane();
        G6TextField searchInput = new G6TextField(Constants.TEXT_FIELD_WIDTH_MEDUIM);
        searchInput.setPromptText("Search");
        
        searchInput.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent e) {
        		Start.searchMembers(searchInput.getText().trim().toLowerCase());
        	}
		});
        
        G6Button addMemberBtn = new G6Button("Add member");
        
        addMemberBtn.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent e) {
        		Start.addMember();
        	}
        });
        
    	top2.setLeft(searchInput);
    	if (SystemController.currentAuth.equals(Auth.ADMIN) ||
				SystemController.currentAuth.equals(Auth.BOTH)) {
        	top2.setRight(addMemberBtn);    		
    	}

        
        topPane.getChildren().addAll(top1, top2);
                
        mainPane.setTop(topPane);
    	
    	// Rendering center
    	tableView = new TableView<>();

        TableColumn<LibraryMember, String> column0 = new TableColumn<>("ID");
        column0.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        
        TableColumn<LibraryMember, String> column1 = new TableColumn<>("First Name");
        column1.setCellValueFactory(new PropertyValueFactory<>("firstName"));


        TableColumn<LibraryMember, String> column2 = new TableColumn<>("Last Name");
        column2.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<LibraryMember, String> column3 = new TableColumn<>("Address");
        column3.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<LibraryMember, String> column4 = new TableColumn<>("Phone Number");
        column4.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        

        TableColumn<LibraryMember, String> actionColumn = new TableColumn<>("Action");
        actionColumn.setPrefWidth(300);

        Callback<TableColumn<LibraryMember, String>, TableCell<LibraryMember, String>> cellFactory =
        		new Callback<TableColumn<LibraryMember,String>, TableCell<LibraryMember,String>>() {

					@Override
					public TableCell<LibraryMember, String> call(TableColumn<LibraryMember, String> arg0) {
						final TableCell<LibraryMember, String> cell = new TableCell<LibraryMember, String>() {

							final G6Button btnUpdate = new G6Button("Update");
							final G6Button btnDelete = new G6Button("Delete");
							final G6Button btnCheckout = new G6Button("Checkouts");
							
		                    @Override
		                    public void updateItem(String item, boolean empty) {
		                        super.updateItem(item, empty);
		                        if (empty) {
		                            setGraphic(null);
		                            setText(null);
		                        } else {
		                        	G6HBox hbox = new G6HBox(5);
		                        	
		                        	btnUpdate.setOnAction(event -> {
		                                LibraryMember member = getTableView().getItems().get(getIndex());
		                                
		                                Start.hideAllWindows();
		                    			if(!MemberInfoWindow.INSTANCE.isInitialized()) {
		                    				MemberInfoWindow.INSTANCE.init();
		                    			}
		                    			MemberInfoWindow.INSTANCE.clear();
		                    			MemberInfoWindow.INSTANCE.show();
		                    			MemberInfoWindow.INSTANCE.updateMember(member);

//		                                System.out.println(member.getFirstName()
//		                                        + "   " + member.getLastName());
		                                System.out.println("update");
		                            });
		                            
		                            btnDelete.setOnAction(event -> {
		                                LibraryMember member = getTableView().getItems().get(getIndex());
		                                
		                                Optional<ButtonType> result = new G6Alert(AlertType.CONFIRMATION, "Confirmation", "Are you sure to delete this record").showAndWait();
		                                
		                                if (result.get() == ButtonType.OK) {
			                                ControllerInterface c = new SystemController();
			                                c.deleteMember(member.getMemberId());
			                                
			                                Start.showMembers(true);
//			                                tableView.getItems().remove(getIndex());
		                                }
		                                
		                                System.out.println("delete" + getIndex());
		                            });
		                            
		                            btnCheckout.setOnAction(event -> {
		                                LibraryMember member = getTableView().getItems().get(getIndex());
		                                Start.showMemberCheckouts(member);
		                            });
		                            
		                        	if (SystemController.currentAuth.equals(Auth.ADMIN) ||
		                    				SystemController.currentAuth.equals(Auth.BOTH)) {
		                        		hbox.getChildren().addAll(btnUpdate, btnDelete);
		                        	}

		                            hbox.getChildren().addAll(btnCheckout);
		                            
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
        tableView.getColumns().add(column3);
        tableView.getColumns().add(column4);
        tableView.getColumns().add(actionColumn);

        VBox vbox = new VBox(tableView);
        vbox.setPadding(new Insets(0));
        mainPane.setCenter(vbox);
        
        G6FlowPane bottomPane = new G6FlowPane(10, 10);
        bottomPane.setPadding(new Insets(10));
        bottomPane.setAlignment(Pos.CENTER);
        btnPrev = new G6Button("Prev");
        
        btnPrev.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent e) {
        		prevPage();
        	}
        });
        pageLbl = new G6Label("1/1");
        
        btnNext = new G6Button("Next");
        btnNext.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent e) {
        		nextPage();
        	}
        });
        
        bottomPane.getChildren().addAll(btnPrev, pageLbl, btnNext);
        
        mainPane.setBottom(bottomPane);
    	
        Scene scene = new Scene(mainPane, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        JMetro jMetro = new JMetro();
		jMetro.setScene(scene);
        // scene.getStylesheets().add(getClass().getResource("../library.css").toExternalForm());
        setScene(scene);
        
    }	    
}

