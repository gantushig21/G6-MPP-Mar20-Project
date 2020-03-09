package ui.checkouts;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javax.swing.JOptionPane;

import business.Address;
import business.AlreadyExistException;
import business.Book;
import business.BookCopy;
import business.ControllerInterface;
import business.LibraryMember;
import business.Checkout;
import business.CheckoutEntry;
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
import javafx.scene.control.DateCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
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
import ui.components.G6ComboBox;
import ui.components.G6DatePicker;;

public class CheckoutInfoWindow extends Stage implements LibWindow {
	public static final CheckoutInfoWindow INSTANCE = new CheckoutInfoWindow();
	
	private boolean isInitialized = false;
	
	private G6Text scenetitle;
	private G6Button actionBtn;
	
	private G6TextField bookIDTxtf;
	private G6Label titleLbl;
	private G6Label isbnLbl;
	private G6Label authorsLbl;
	
	private G6TextField usernameTxtf;
	private G6Label userIDLbl;
	private G6Label usernameLbl;
	
	private G6Label bookCopyLbl;
	private G6Label checkoutDateLbl;
	private G6Label dueDateLbl0;
	private G6DatePicker dueDateDTDtpckr;
	
	private Optional<ButtonType> result;
	
	private Book selectedBook;
	private Checkout checkout;
	private LocalDate checkoutDate;
	private LocalDate dueDate;
	private LocalDate limitDate;
	
	public boolean isInitialized() {
		return isInitialized;
	}
	public void isInitialized(boolean val) {
		isInitialized = val;
	}
	private Text messageBar = new Text();
	
	public void start() {
		checkoutDate = LocalDate.now();
		
		setVisible(false);
		
	}
	
	private void searchBook() {
		setVisible(selectedBook != null);
		
		if (selectedBook != null) {
			dueDate = checkoutDate.plusDays(selectedBook.getBorrowDayLimit());
			String formattedDate = checkoutDate.format(DateTimeFormatter.ofPattern("M/d/YYYY"));
			checkoutDateLbl.setText(formattedDate);
			
			limitDate = checkoutDate.plusDays(selectedBook.getBorrowDayLimit());
			
			dueDateDTDtpckr.setValue(dueDate);			
		}
	}
	
	private void setVisible(boolean value) {
		dueDateDTDtpckr.setVisible(value);
		dueDateLbl0.setVisible(value);
		actionBtn.setVisible(value);
		
	}
	
	/* This class is a singleton */
	private Checkout currentCheckout;
	
    private CheckoutInfoWindow () {
    }
    
	private void clearFields() {

	}
	
	public void updateCheckout(Checkout checkout) {
		scenetitle.setText("Update checkout ID ");
		actionBtn.setText("Update");		
		
		currentCheckout = checkout;
	}
	
	public void clearBookTextFields() {
		selectedBook = null;
		
		titleLbl.setText("");
		isbnLbl.setText("");
		authorsLbl.setText("");
		bookCopyLbl.setText("");
	}
	
	public void clearUserTextFields() {
		checkout = null;
		
		userIDLbl.setText("");
		usernameLbl.setText("");
	}
	
	public void handleAddButtonDisabled() {
		actionBtn.setDisable(selectedBook == null || checkout == null);
	}
    
    public void init() {
    	G6VBox vbox = new G6VBox(5);
    	vbox.setPadding(new Insets(25));
    	vbox.setId("top-container");
    	    	
        scenetitle = new G6Text("Add new checkout");
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
        grid.setVgap(15);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        vbox.getChildren().addAll(topPane, grid);


        grid.add(topPane, 0, 0, 6, 1);

        // Name section
        G6Label bookIDLbl = new G6Label("Find:");
        bookIDTxtf = new G6TextField();
        bookIDTxtf.setPromptText("ISBN");
        G6Button searchBtn = new G6Button("Search");
        grid.add(bookIDLbl, 0, 2);
        grid.add(bookIDTxtf, 1, 2);
        grid.add(searchBtn, 2, 2);
        
        searchBtn.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				clearBookTextFields();
				
				ControllerInterface c = new SystemController();
				Book book = c.getBookByISBN(bookIDTxtf.getText().trim());
				
				if (book == null) {
					result = new G6Alert(AlertType.WARNING, "Sorry", "Book with this ISBN is not found!").showAndWait();
				} else if (!book.isAvailable()) {
					result = new G6Alert(AlertType.WARNING, "Sorry", "There is not available copy!").showAndWait();
				} else if (book.getMaxCheckoutLength() <= book.getCheckedOut()) {
					result = new G6Alert(AlertType.WARNING, "Sorry", "The maximum checkout length is " + book.getMaxCheckoutLength()).showAndWait();
				} else {
					selectedBook = book;
					titleLbl.setText(book.getTitle());
					isbnLbl.setText(book.getIsbn());
					authorsLbl.setText(book.getAuthors().toString());
					BookCopy bookCopy = book.getNextAvailableCopy();
					bookCopyLbl.setText(bookCopy.getCopyNum() + "");
					
				}
				handleAddButtonDisabled();
				searchBook();
			}
		});
        
        G6Label titleLbl0 = new G6Label("Title: ");         
        grid.add(titleLbl0, 0, 3);        
        titleLbl = new G6Label("");         
        grid.add(titleLbl, 1, 3);
        
        G6Label isbnLbl0 = new G6Label("ISBN:");
        grid.add(isbnLbl0, 0, 4);
        isbnLbl = new G6Label("");
        grid.add(isbnLbl, 1, 4);
        
        G6Label authorsLbl0 = new G6Label("Authors");
        grid.add(authorsLbl0, 0, 5);
        authorsLbl = new G6Label("");
        grid.add(authorsLbl, 1, 5);
        
        
        G6Label usernameLbl0 = new G6Label("Find:");
        usernameTxtf = new G6TextField();
        usernameTxtf.setPromptText("Member ID");
        G6Button searchUserBtn = new G6Button("Search");
        grid.add(usernameLbl0, 3, 2);
        grid.add(usernameTxtf, 4, 2);
        grid.add(searchUserBtn, 5, 2);
        
        searchUserBtn.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
			public void handle(ActionEvent arg0) {
        		clearUserTextFields();
        		
				ControllerInterface c = new SystemController();
				LibraryMember member = c.getMemberById(usernameTxtf.getText().trim());
				if (member == null) {
					result = new G6Alert(AlertType.WARNING, "Sorry", "Member with this ID is not found!").showAndWait();
					userIDLbl.setText("");
					usernameLbl.setText("");
				} else {
					checkout = c.getMemberCheckout(member);
					userIDLbl.setText(member.getFirstName());
					usernameLbl.setText(member.getLastName());
				}
				handleAddButtonDisabled();
			}
		});
        
        G6Label userIDLbl0 = new G6Label("First Name: ");         
        grid.add(userIDLbl0, 3, 3);        
        userIDLbl = new G6Label("");         
        grid.add(userIDLbl, 4, 3);
        
        G6Label usernameLbl1 = new G6Label("User Name: ");
        grid.add(usernameLbl1, 3, 4);
        usernameLbl = new G6Label("");
        grid.add(usernameLbl, 4, 4);
        

        G6Label bookCopyLbl0 = new G6Label("Book Copy ID: ");
        grid.add(bookCopyLbl0, 0, 6);
        bookCopyLbl = new G6Label("");
        grid.add(bookCopyLbl, 1, 6);

        
        G6Label checkoutLbl0 = new G6Label("Checkout Date: ");
        grid.add(checkoutLbl0, 0, 7);
        checkoutDateLbl = new G6Label(LocalDate.now().toString());
        grid.add(checkoutDateLbl, 1, 7);
        
        dueDateLbl0 = new G6Label("Due Date: ");
        grid.add(dueDateLbl0, 0, 8);
        dueDateDTDtpckr = new G6DatePicker();
        grid.add(dueDateDTDtpckr, 1, 8);
        dueDateDTDtpckr.getEditor().setDisable(true);
        dueDateDTDtpckr.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                if (date.compareTo(today) < 0) {
                	setDisable(true);
                }
                if (date.compareTo(limitDate) > 0) {
                	setDisable(true);                	
                }
            }
        });
        dueDateDTDtpckr.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
			public void handle(ActionEvent arg0) {
				if (dueDateDTDtpckr.getValue().compareTo(limitDate) > 0) {
					result = new G6Alert(AlertType.WARNING, "Sorry", "You can't take the book for more than " + Constants.CHECKOUT_DAY_LIMIT + " days").showAndWait();
					dueDateDTDtpckr.setValue(limitDate);
				} else if (dueDateDTDtpckr.getValue().compareTo(checkoutDate) < 0) {
					dueDateDTDtpckr.setValue(LocalDate.now().plusDays(1));
				} else {
					dueDate = dueDateDTDtpckr.getValue();
				}
			}
		});

        actionBtn = new G6Button("Add");
        actionBtn.setDisable(true);
        HBox hbBtn = new HBox(11);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(actionBtn);
        grid.add(hbBtn, 1, 15);
        
        actionBtn.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
			public void handle(ActionEvent arg0) {
        		result = new G6Alert(AlertType.CONFIRMATION, "Confirmation", "Are you sure to checkout this book?").showAndWait();
				if (result.get() == ButtonType.OK) {
					ControllerInterface c = new SystemController();
					
					BookCopy copy = selectedBook.getAvailableBookCopy();
					copy.changeAvailability();
					System.out.println(selectedBook.getCheckedOut());
					selectedBook.setCheckedOut(selectedBook.getCheckedOut() + 1);
					c.updateBook(selectedBook);
					System.out.println(selectedBook.getCheckedOut());
					
					CheckoutEntry checkoutEntry = new CheckoutEntry(
						copy, 
						checkout.getMember(),
						checkoutDate, 
						dueDate
					);
									
					checkout.getCheckoutEntries().add(checkoutEntry);
					
					c.saveCheckout(checkout);
					
					result = new G6Alert(AlertType.NONE, "Success", "The checkout is added successful", ButtonType.OK).showAndWait();	       					
	            	   if (result.get() == ButtonType.OK) {
        					clearFields();
        					Start.showCheckouts(true);
	            	   } 
				}
			}
		});

        HBox messageBox = new HBox(10);
        messageBox.setAlignment(Pos.BOTTOM_RIGHT);
        messageBox.getChildren().add(messageBar);;
        grid.add(messageBox, 1, 16);


        backBtn.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent e) {
        		Start.showCheckouts(false);
        	}
        });
        Scene scene = new Scene(vbox, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        JMetro jMetro = new JMetro();
		jMetro.setScene(scene);
        // scene.getStylesheets().add(getClass().getResource("../library.css").toExternalForm());
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
    
//	public String getFirstNameValue() {
//		return idTxtf.getText();
//	}
//	public String getLastNameValue() {
//		return bookIDTxtf.getText();
//	}
//	public String getStreetValue() {
//		return isbnTxtf.getText();
//	}
//	public String getCityValue() {
//		return authorsTxtf.getText();
//	}
//	public String getStateValue() {
//		return copyIDTxtf.getText();
//	}
//	public String getZipValue() {
//		return checkoutDateTxtf.getText();
//	}
//	public String getPhoneNumberValue() {
//		return dueDateTxtf.getText();
//	}
	
	public static CheckoutInfoWindow getInstance() {
		return INSTANCE;
	}
	public Text getMessageBar() {
		return messageBar;
	}	
    
}