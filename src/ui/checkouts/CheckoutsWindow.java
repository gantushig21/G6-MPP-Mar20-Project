package ui.checkouts;

import java.util.List;
import java.util.Optional;

import business.Address;
import business.ControllerInterface;
import business.CheckoutEntry;
import business.Checkout;
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
import ui.components.G6TextField;
import ui.components.G6VBox;

public class CheckoutsWindow extends Stage implements LibWindow {
	public static final CheckoutsWindow INSTANCE = new CheckoutsWindow();
	
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
	
	private TableView<Checkout> tableView;
	
	public void setData( List<Checkout> data ) {
		tableView.setItems(FXCollections.observableList(data));
	}

	
	/* This class is a singleton */
    private CheckoutsWindow () {}
    
    public void init() {
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
//        		Start.searchMembers(searchInput.getText().trim().toLowerCase());
        	}
		});
        
        G6Button addCheckout = new G6Button("Add checkout");
        
        addCheckout.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent e) {
        		Start.addCheckout();
        	}
        });
        
    	top2.setLeft(searchInput);
    	top2.setRight(addCheckout);

        
        topPane.getChildren().addAll(top1, top2);
                
        mainPane.setTop(topPane);
    	
    	// Rendering center
    	tableView = new TableView<>();

        TableColumn<Checkout, String> column0 = new TableColumn<>("ID");
        column0.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        TableColumn<Checkout, String> column1 = new TableColumn<>("Title");
        column1.setCellValueFactory(new PropertyValueFactory<>("title"));


        TableColumn<Checkout, String> column2 = new TableColumn<>("ISBN");
        column2.setCellValueFactory(new PropertyValueFactory<>("isbn"));

        TableColumn<Checkout, String> column3 = new TableColumn<>("Authors");
        column3.setCellValueFactory(new PropertyValueFactory<>("authors"));

        TableColumn<Checkout, String> column4 = new TableColumn<>("Copy ID");
        column4.setCellValueFactory(new PropertyValueFactory<>("copyID"));

        TableColumn<Checkout, String> column5 = new TableColumn<>("Checkout date");
        column5.setCellValueFactory(new PropertyValueFactory<>("checkoutDate"));

        TableColumn<Checkout, String> column6 = new TableColumn<>("Due date");
        column6.setCellValueFactory(new PropertyValueFactory<>("dueDate"));

        TableColumn<Checkout, String> column7 = new TableColumn<>("Return date");
        column7.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        

        TableColumn<Checkout, String> actionColumn = new TableColumn<>("Action");

        Callback<TableColumn<Checkout, String>, TableCell<Checkout, String>> cellFactory =
        		new Callback<TableColumn<Checkout,String>, TableCell<Checkout,String>>() {

					@Override
					public TableCell<Checkout, String> call(TableColumn<Checkout, String> arg0) {
						final TableCell<Checkout, String> cell = new TableCell<Checkout, String>() {

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
		                                Checkout checkout = getTableView().getItems().get(getIndex());
		                                
		                                Start.hideAllWindows();
		                    			if(!CheckoutInfoWindow.INSTANCE.isInitialized()) {
		                    				CheckoutInfoWindow.INSTANCE.init();
		                    			}
		                    			CheckoutInfoWindow.INSTANCE.clear();
		                    			CheckoutInfoWindow.INSTANCE.show();
		                    			CheckoutInfoWindow.INSTANCE.updateCheckout(checkout);

//		                                System.out.println(checkout.getFirstName()
//		                                        + "   " + checkout.getLastName());
		                                System.out.println("update");
		                            });
		                            
		                            btnDelete.setOnAction(event -> {
		                                Checkout checkout = getTableView().getItems().get(getIndex());
		                                
		                                Optional<ButtonType> result = new G6Alert(AlertType.CONFIRMATION, "Confirmation", "Are you sure to delete this record").showAndWait();
		                                
		                                if (result.get() == ButtonType.OK) {
//			                                ControllerInterface c = new SystemController();
//			                                c.deleteCheckout(checkout.getCheckoutId());
			                                
			                                tableView.getItems().remove(getIndex());
		                                }
		                                
		                                System.out.println("delete" + getIndex());
		                            });
		                            
		                            btnCheckout.setOnAction(event -> {
		                                Checkout checkout = getTableView().getItems().get(getIndex());
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
        tableView.getColumns().add(column3);
        tableView.getColumns().add(column4);
        tableView.getColumns().add(column5);
        tableView.getColumns().add(column6);
        tableView.getColumns().add(column7);
        tableView.getColumns().add(actionColumn);

        VBox vbox = new VBox(tableView);
        vbox.setPadding(new Insets(0));
        mainPane.setCenter(vbox);
    	
        Scene scene = new Scene(mainPane, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        // scene.getStylesheets().add(getClass().getResource("../library.css").toExternalForm());
        setScene(scene);
        
    }	    
}