package ui.checkouts;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import business.Address;
import business.Book;
import business.BookCopy;
import business.ControllerInterface;
import business.LibraryMember;
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
import jfxtras.styles.jmetro.JMetro;
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

public class MemberCheckoutsWindow extends Stage implements LibWindow {
	public static final MemberCheckoutsWindow INSTANCE = new MemberCheckoutsWindow();
	
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
	
	private G6Text scenetitle;
	
	private TableView<CheckoutEntry> table;
	
	public void setData( List<CheckoutEntry> data ) {
		table.setItems(FXCollections.observableList(data));
	}

	private Checkout checkout;
	private List<CheckoutEntry> entries;
	
	public void startMemberCheckout(Checkout co) {
		checkout = co;
		entries = co.getCheckoutEntries();
		Collections.sort(entries);
		
		scenetitle.setText(co.getMember().getFirstName() + " " + co.getMember().getLastName() + "'s checkouts");
		table.setItems(FXCollections.observableList(entries));
	}
	
	/* This class is a singleton */
    private MemberCheckoutsWindow () {}
    
    public void init() {
    	isInitialized(true);
    	
    	G6BorderPane mainPane = new G6BorderPane();
    	mainPane.setPadding(new Insets(25));
    	mainPane.setId("top-container");
    	
    	// Rendering top
    	G6VBox topPane = new G6VBox(15);
    	topPane.setPadding(new Insets(15, 0, 15, 0));
    	
    	G6BorderPane top1 = new G6BorderPane();
        scenetitle = new G6Text("Member checkouts");
        
        scenetitle.setFont(Font.font("Harlow Solid Italic", FontWeight.NORMAL, Constants.PANE_TITLE_FONT_SIZE)); 
        
        G6Button backBtn = new G6Button("Back");
        
        backBtn.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent e) {
        		Start.showMembers(false);
        	}
        });
        
        top1.setLeft(backBtn);
    	top1.setCenter(scenetitle);
    	
//    	G6BorderPane top2 = new G6BorderPane();
//        G6TextField searchInput = new G6TextField(Constants.TEXT_FIELD_WIDTH_MEDUIM);
//        searchInput.setPromptText("Search");
//        
//        searchInput.setOnAction(new EventHandler<ActionEvent>() {
//        	@Override
//        	public void handle(ActionEvent e) {
//        		Start.searchMembers(searchInput.getText().trim().toLowerCase());
//        	}
//		});
    	
//        top2.setLeft(searchInput);
        
        topPane.getChildren().addAll(top1);
//        topPane.getChildren().addAll(top1, top2);
                
        mainPane.setTop(topPane);
        
        table = new TableView<>();
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
        column4.setPrefWidth(Constants.TABLE_DEFAULT_LENGTH);
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

							final G6Button btnReturn = new G6Button("Return");
							
		                    @Override
		                    public void updateItem(String item, boolean empty) {
		                        super.updateItem(item, empty);
		                        if (empty) {
		                            setGraphic(null);
		                            setText(null);
		                        } else {
		                        	G6HBox hbox = new G6HBox(5);
		                        	
		                            
		                            btnReturn.setOnAction(event -> {
		                            	Optional<ButtonType> result = new G6Alert(AlertType.CONFIRMATION, "Confirmation", "Are you sure to return this book?").showAndWait();
		                            	if (result.get() == ButtonType.OK) {
			                                CheckoutEntry entry = getTableView().getItems().get(getIndex());
			                                entries.get(getIndex()).setReturnDate(LocalDate.now());
			                                
			                                checkout.setEntries(entries);
			                                
			                                ControllerInterface c = new SystemController();
			                                c.saveCheckout(checkout);
			                                table.setItems(FXCollections.observableList(entries));
			                                
			                                Book bk = entry.getBook().getBook();
			                                BookCopy[] copies = bk.getCopies();
			                                for (BookCopy copy: copies) {
			                                	if (copy.equals(entry.getBook())) {
			                                		copy.changeAvailability();
			                                		if (copy.getIsAvailable()) {
			                                			bk.setCheckedOut(bk.getCheckedOut() - 1);
			                                		}
			                                		break;
			                                	}
			                                }
			                                c.updateBook(bk);
		                            	}

		                                
//		                                c.deleteCheckoutEntry(entry);
////		                                c.deleteCheckout(checkout.getCheckoutId());
//		                                
//		                                tableView.getItems().remove(getIndex());
		                            });
		                            
		                            hbox.getChildren().addAll(btnReturn);
		                            
		                            setGraphic(hbox);
		                            setText(null);
		                        }
		                    }
		                };
		                return cell;
					}
				};
        
		actionColumn.setCellFactory(cellFactory);
        
        table.getColumns().add(column1);
        table.getColumns().add(column2);
        table.getColumns().add(column3);
        table.getColumns().add(column4);
        table.getColumns().add(column5);
        table.getColumns().add(column6);
        table.getColumns().add(column7);
        table.getColumns().add(actionColumn);
        
        VBox vbox = new VBox(table);
        vbox.setPadding(new Insets(0));
        mainPane.setCenter(vbox);
        
        G6HBox printPane = new G6HBox(10);
        printPane.setAlignment(Pos.TOP_RIGHT);
        G6Button printBtn = new G6Button("Print");
        printPane.getChildren().add(printBtn);
        mainPane.setBottom(printPane);
        printBtn.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent e) {
        		System.out.print(checkout);
        	}
		});
        
        Scene scene = new Scene(mainPane, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        JMetro jMetro = new JMetro();
		jMetro.setScene(scene);
        // scene.getStylesheets().add(getClass().getResource("../library.css").toExternalForm());
        setScene(scene);
        
    }	    
}