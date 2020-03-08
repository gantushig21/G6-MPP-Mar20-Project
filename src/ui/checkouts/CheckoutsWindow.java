package ui.checkouts;

import java.util.List;
import java.util.Optional;

import business.Address;
import business.ControllerInterface;
import business.CheckoutEntry;
import business.Checkout;
import business.SystemController;
import config.Constants;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.TableColumn.CellDataFeatures;
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
	
	private TableView<CheckoutEntry> tableView;
	
	public void setData( List<CheckoutEntry> data ) {
		tableView.setItems(FXCollections.observableList(data));
	}

	
	/* This class is a singleton */
    private CheckoutsWindow () {}
    
    public void init() {
    	isInitialized(true);
    	
    	G6BorderPane mainPane = new G6BorderPane();
    	mainPane.setPadding(new Insets(25));
    	mainPane.setId("top-container");
    	
    	// Rendering top
    	G6VBox topPane = new G6VBox(15);
    	topPane.setPadding(new Insets(15, 0, 15, 0));
    	
    	G6BorderPane top1 = new G6BorderPane();
        Text scenetitle = new Text("Manage checkouts");
        
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

        TableColumn<CheckoutEntry, String> column1 = new TableColumn<>("Title");
        column1.setPrefWidth(Constants.TABLE_TITLE_LENGTH);
        column1.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CheckoutEntry,String>, ObservableValue<String>>() {
        	public ObservableValue<String> call(CellDataFeatures<CheckoutEntry, String> p) {
        		// p.getValue() returns the Person instance for a particular TableView row
        		return new SimpleStringProperty(p.getValue().getBook().getBook().getTitle());
            }
		});


        TableColumn<CheckoutEntry, String> column2 = new TableColumn<>("ISBN");
        column2.setPrefWidth(Constants.TABLE_BOOK_ISBN_LENGTH);
        column2.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CheckoutEntry,String>, ObservableValue<String>>() {
        	public ObservableValue<String> call(CellDataFeatures<CheckoutEntry, String> p) {
        		// p.getValue() returns the Person instance for a particular TableView row
        		return new SimpleStringProperty(p.getValue().getBook().getBook().getIsbn());
            }
		});

        TableColumn<CheckoutEntry, String> column3 = new TableColumn<>("Authors");
        column3.setPrefWidth(Constants.TABLE_BOOK_AUTHORS_LENGTH);
        column3.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CheckoutEntry,String>, ObservableValue<String>>() {
        	public ObservableValue<String> call(CellDataFeatures<CheckoutEntry, String> p) {
        		// p.getValue() returns the Person instance for a particular TableView row
        		return new SimpleStringProperty(p.getValue().getBook().getBook().getAuthors().toString());
            }
		});

        TableColumn<CheckoutEntry, String> column4 = new TableColumn<>("Copy ID");
        column4.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CheckoutEntry,String>, ObservableValue<String>>() {
        	public ObservableValue<String> call(CellDataFeatures<CheckoutEntry, String> p) {
        		// p.getValue() returns the Person instance for a particular TableView row
        		return new SimpleStringProperty(p.getValue().getBook().getCopyNum() + "");
            }
		});

        TableColumn<CheckoutEntry, String> column5 = new TableColumn<>("Checkout date");
        column5.setPrefWidth(Constants.TABLE_DATE_LENGTH);
        column5.setCellValueFactory(new PropertyValueFactory<>("checkoutDate"));

        TableColumn<CheckoutEntry, String> column6 = new TableColumn<>("Due date");
        column6.setPrefWidth(Constants.TABLE_DATE_LENGTH);
        column6.setCellValueFactory(new PropertyValueFactory<>("dueDate"));

        TableColumn<CheckoutEntry, String> column7 = new TableColumn<>("Return date");
        column7.setPrefWidth(Constants.TABLE_DATE_LENGTH);
        column7.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        

        TableColumn<CheckoutEntry, String> actionColumn = new TableColumn<>("Action");

        Callback<TableColumn<CheckoutEntry, String>, TableCell<CheckoutEntry, String>> cellFactory =
        		new Callback<TableColumn<CheckoutEntry,String>, TableCell<CheckoutEntry,String>>() {

					@Override
					public TableCell<CheckoutEntry, String> call(TableColumn<CheckoutEntry, String> arg0) {
						final TableCell<CheckoutEntry, String> cell = new TableCell<CheckoutEntry, String>() {

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
		                                System.out.println("update " + getIndex());
		                            });
		                            
		                            btnDelete.setOnAction(event -> {		                                
//		                                Optional<ButtonType> result = new G6Alert(AlertType.CONFIRMATION, "Confirmation", "Are you sure to delete this record").showAndWait();
//		                                
//		                                if (result.get() == ButtonType.OK) {
//			                                CheckoutEntry entry = getTableView().getItems().get(getIndex());
//			                                ControllerInterface c = new SystemController();
//			                                
//			                                c.deleteCheckoutEntry(entry);
////			                                c.deleteCheckout(checkout.getCheckoutId());
//			                                
//			                                tableView.getItems().remove(getIndex());
//		                                }
//		                                
		                                System.out.println("delete " + getIndex());
		                            });
		                            
		                            btnCheckout.setOnAction(event -> {
//		                                Checkout checkout = getTableView().getItems().get(getIndex());
		                                System.out.println("checkout");
		                            });
		                            
//		                            hbox.getChildren().addAll(btnUpdate, btnDelete, btnCheckout);
		                            
		                            setGraphic(hbox);
		                            setText(null);
		                        }
		                    }
		                };
		                return cell;
					}
				};
        
		actionColumn.setCellFactory(cellFactory);

        tableView.getColumns().add(column1);
        tableView.getColumns().add(column2);
        tableView.getColumns().add(column3);
        tableView.getColumns().add(column4);
        tableView.getColumns().add(column5);
        tableView.getColumns().add(column6);
        tableView.getColumns().add(column7);
//        tableView.getColumns().add(actionColumn);

        VBox vbox = new VBox(tableView);
        vbox.setPadding(new Insets(0));
        mainPane.setCenter(vbox);
    	
        Scene scene = new Scene(mainPane, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        // scene.getStylesheets().add(getClass().getResource("../library.css").toExternalForm());
        setScene(scene);
        
    }	    
}