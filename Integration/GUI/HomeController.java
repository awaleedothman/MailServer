package GUI;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import Classes.App;
import Classes.DoublyLinkedList;
import Classes.Filter;
import Classes.FilterAttribute;
import Classes.Folder;
import Classes.Mail;
import Classes.Priority;
import Classes.Sort;
import Classes.SortAttribute;
import Misc.Birthday;
import Misc.Utils;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HomeController implements Initializable {

	private App app;
	private final String sep = System.getProperty("file.separator");
	
	@FXML
	private AnchorPane rootPane;
	@FXML
	private Button contactsBtn;
	@FXML
	private Button foldersBtn;
	@FXML
	private Button applyBtn;
	@FXML
	private Button priorityBtn;
	@FXML
	private Button refreshBtn;
	@FXML
	private Button moveBtn;
	@FXML
	private Button delBtn;
	@FXML
	private CheckBox reverseChkBox;
	@FXML
	private ChoiceBox<String> sortChoiceBox;
	@FXML
	private ChoiceBox<String> searchChoiceBox;
	@FXML
	private DatePicker datePck;
	@FXML
	private Label bannerLbl;
	@FXML
	private Pagination pgr;
	@FXML
	private TableView<MailHeader> mailsTbl;
	@FXML
	private TextField searchTxt;
	@FXML
	private TitledPane inboxTPane;
	@FXML
	private Button composeBtn;
	
	
	public class MailHeader {
		
		private int ID;
		
		private String title;
		
		private String senderName;
		
		private String address;
		
		private String date;
		
		private Priority priority;
		
		public MailHeader(int ID, String title, String sendername, String address, String date, Priority priority) {
	        this.ID = ID;
			this.title = title;
	        this.senderName = sendername;
	        this.address = address;
	        this.date = date;
	        this.priority = priority;
	    }

		public String getTitle() {
			return title;
		}

		public String getSenderName() {
			return senderName;
		}

		public String getAddress() {
			return address;
		}

		public String getDate() {
			return date;
		}

		public int getID() {
			return ID;
		}

		public void setID(int iD) {
			ID = iD;
		}

		public Priority getPriority() {
			return priority;
		}

		public void setPriority(Priority priority) {
			this.priority = priority;
		}
		
	}
	
	private void showMails(int i) {
		mailsTbl.getItems().clear();
		Mail[] mails = (Mail[])app.listEmails(i);
		for (int j = 0; j < 10 && mails[j] != null; j++) {
			MailHeader MH = new MailHeader(mails[j].getID(), mails[j].getTitle(), mails[j].getSenderName(), 
					mails[j].getSenderAddress(), mails[j].getDate().toString(), mails[j].getPriority());
			mailsTbl.getItems().add(MH);
		}
	}
	
	public void initialize(App app) {
		this.app = app;
		app.setViewingOptions(new Folder(app.getLoggedinUser().getFilePath() + sep + "inbox"), null, null);
		bannerLbl.setText("Hello, " + app.getLoggedinUser().getName());
		Calendar cal = Calendar.getInstance();
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH)+1;
		Birthday bd = app.getLoggedinUser().getBirthday();
		if (dayOfMonth == bd.getDay() && month == bd.getMonth()) {
			int age = bd.getAge();
			bannerLbl.setText("Hello, " + app.getLoggedinUser().getName() + "\t Happy birthday !! What a wonderful " + age + "years!!");
		}
		sortChoiceBox.getItems().addAll(SortAttribute.Date.toString(), 
				SortAttribute.Title.toString(), SortAttribute.SenderName.toString());
		sortChoiceBox.getSelectionModel().select(SortAttribute.Date.toString());
		searchChoiceBox.getItems().addAll("No Filter", FilterAttribute.Attachments.toString(), FilterAttribute.Date.toString(),
				FilterAttribute.Recievers.toString(), FilterAttribute.Sender.toString(),
				FilterAttribute.Text.toString(), FilterAttribute.Title.toString());
		
		searchChoiceBox.getSelectionModel().select("No Filter");
		searchChoiceBox.setOnAction(e -> {
		    if (FilterAttribute.valueOf(searchChoiceBox.getSelectionModel().getSelectedItem()) == FilterAttribute.Date) {
		    	datePck.setDisable(false);
		    	searchTxt.setDisable(true);
		    }
		    else {
		    	datePck.setDisable(true);
		    	searchTxt.setDisable(false);
		    } 	
		});
		
		// Preparing table columns and setting attributes
		TableColumn<MailHeader, String> IDColumn=new TableColumn<>();
		IDColumn.setVisible(false);
	    IDColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
		TableColumn<MailHeader, String> titleColumn=new TableColumn<>("Title");
	    titleColumn.setMinWidth(150);
	    titleColumn.setResizable(false);
	    titleColumn.setSortable(false);
	    titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
	    TableColumn<MailHeader, String> senderNameColumn=new TableColumn<>("Sender name");
	    senderNameColumn.setMinWidth(145);
	    senderNameColumn.setResizable(false);
	    senderNameColumn.setSortable(false);
	    senderNameColumn.setCellValueFactory(new PropertyValueFactory<>("SenderName"));
	    TableColumn<MailHeader, String> addressColumn=new TableColumn<>("Address");
	    addressColumn.setMinWidth(150);
	    addressColumn.setResizable(false);
	    addressColumn.setSortable(false);
	    addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
	    TableColumn<MailHeader, String> dateColumn=new TableColumn<>("Date");
	    dateColumn.setMinWidth(200);
	    dateColumn.setResizable(false);
	    dateColumn.setSortable(false);
	    dateColumn.setCellValueFactory(new PropertyValueFactory<MailHeader, String>("Date"));
	    TableColumn<MailHeader, String> priorityColumn=new TableColumn<>("Priority");
	    priorityColumn.setMinWidth(150);
	    priorityColumn.setResizable(false);
	    priorityColumn.setSortable(false);
	    priorityColumn.setCellValueFactory(new PropertyValueFactory<MailHeader, String>("Priority"));
	    mailsTbl.setOnMouseClicked(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent mouseEvent) {
	            if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
	                if(mouseEvent.getClickCount() == 2){
	                    openMail();
	                }
	            }
	        }
	    }); 
	    mailsTbl.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	    // Add columns to the table view
	    mailsTbl.getColumns().addAll(titleColumn, senderNameColumn, addressColumn, dateColumn, priorityColumn);
		// Max page count depending on the number of loaded mails in the current folder and filter
	    pgr.setPageCount((int)(((float)app.availableMailsCount()/10) + 0.9));
	    // Load mails in the page 1
		showMails(1);
    }
	
	public void loadInbox() {
		moveBtn.setVisible(true);
		delBtn.setVisible(true);
		app.setViewingOptions(new Folder(app.getLoggedinUser().getFilePath() + sep + "inbox"), null, null);
		showMails(1);
		// VBox to carry the user folders
		// Replace 5 with the user number of folders
		VBox folders = new VBox(5);
		
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(this.app.getLoggedinUser().getFilePath()
					+ sep + "inbox" + sep + "folders.txt"));
			String row;
			// Generating titled pane for each folder and set its attributes
			// Generate user defined folders
			while ((row = reader.readLine()) != null) {
				TitledPane tPane = new TitledPane();
				tPane.setText(row);
				tPane.setOnMouseClicked(new FoldersEventHandler());
				tPane.setCollapsible(false);
				tPane.setExpanded(false);
				folders.getChildren().add(tPane);
			}
		} catch (Exception e) {
			Utils.fileNotFound();
		}
		
		/* We will iterate on the user folders and add them*/
		inboxTPane.setContent(folders);
	}
	
	/*
	 * Event for handling user accessing folders
	 * */
	private class FoldersEventHandler implements EventHandler<Event>{
        @Override
        public void handle(Event evt) {
        	String path = app.getLoggedinUser().getFilePath()
					+ sep + "inbox" + sep + ((TitledPane)evt.getSource()).getText();
            // Here We call setViewOptions at the current selected folder and then show mails
        	app.setViewingOptions(new Folder(path), null, null);
        	showMails(1);

        }
    }
	
	/*
	 * Load mails in the user sent folder
	 * */
	public void loadSent() {
		moveBtn.setVisible(false);
		delBtn.setVisible(false);
		// SetviewingOption
		app.setViewingOptions(new Folder(app.getLoggedinUser().getFilePath() + sep + "sent"), null, null);
		// Show mails
		showMails(1);
	}
	
	/*
	 * Load mails in the user trash folder
	 * */
	public void loadTrash() {
		moveBtn.setVisible(false);
		delBtn.setVisible(false);
		// SetviewingOption
		app.setViewingOptions(new Folder(app.getLoggedinUser().getFilePath() + sep + "trash"), null, null);
		// Show mails
		showMails(1);
	}
	
	public void contactsBtnClicked() {
		AnchorPane pane = null;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("contacts.fxml"));
		try {
			pane = loader.load();
		} catch (IOException e) {
			Utils.fileNotFound();
		}
        Contacts controller = loader.getController();
        controller.setApp(app);
        rootPane.getChildren().setAll(pane);
        rootPane.getScene().getWindow().setHeight(639);
        rootPane.getScene().getWindow().setWidth(570);
	}
	
	public void foldersBtnClicked() {
        AnchorPane pane = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("folders.fxml"));
            pane = loader.load();
            Folders controller = loader.getController();
            controller.setApp(app);
        } catch (IOException e) {
            Utils.fileNotFound();
        }
        rootPane.getChildren().setAll(pane);
        rootPane.getScene().getWindow().setWidth(637);
        rootPane.getScene().getWindow().setHeight(487);
	}
	
	/*
	 * Handle page changing event
	 * */
	public void pageSelection() {
		showMails(pgr.getCurrentPageIndex()+1);
	}
	
	private Mail getSearchMail() {
		
		if (searchChoiceBox.getSelectionModel().getSelectedItem() != "No Filter") {
			FilterAttribute filter = FilterAttribute.valueOf(searchChoiceBox.getSelectionModel().getSelectedItem());
			Mail mail = null;
			if(filter == FilterAttribute.Date) {
				Date date = java.sql.Date.valueOf(datePck.getValue());
				mail = new Mail("", "", "", date,Priority.Secondary);
			}
			else if(filter == FilterAttribute.Title) {
				mail = new Mail(searchTxt.getText(), "", "", null,Priority.Secondary);
			}
			else if(filter == FilterAttribute.Sender) {
				mail = new Mail("", "", searchTxt.getText(), null,Priority.Secondary);
			}
			return mail;
		}
		return null;
	}
	
	public void applyBtnClicked() {
		// Get filter type
		Filter filter = null;
		if (searchChoiceBox.getSelectionModel().getSelectedItem() != "No Filter") {
			FilterAttribute filterAttr = FilterAttribute.valueOf(searchChoiceBox.getSelectionModel().getSelectedItem());
			if (filterAttr == FilterAttribute.Attachments || filterAttr == FilterAttribute.Recievers || filterAttr == FilterAttribute.Text) {
				filter = new Filter(FilterAttribute.valueOf(searchChoiceBox.getSelectionModel().getSelectedItem()), searchTxt.getText());
			}
			else {
				filter = new Filter(FilterAttribute.valueOf(searchChoiceBox.getSelectionModel().getSelectedItem()), getSearchMail());
			}
		}
		// Set Sorting and Filtering
		app.setViewingOptions(null, filter, new Sort(SortAttribute.valueOf(sortChoiceBox.getSelectionModel().getSelectedItem())));
		// Reverse Sorting
		app.reverseSort(reverseChkBox.isSelected());
		// Load the mails of the first page after sorting
		showMails(1);
	}
	
	public void priorityBtnClicked() {
		// Call function in app class to sort loaded mails by priority
		if (searchChoiceBox.getSelectionModel().getSelectedItem() != "No Filter") {
			Filter filter = new Filter(FilterAttribute.valueOf(searchChoiceBox.getSelectionModel().getSelectedItem()), searchTxt.getText());
			app.setViewingOptions(null, filter, null);
		}
		app.SortByPriority();
		app.reverseSort(false);
		// Show mails in the first page
		showMails(1);
	}
	
	public void openMail() {
		MailHeader MH = mailsTbl.getSelectionModel().getSelectedItem();
		Mail mail = null;
		try {
			 mail = new Mail(MH.getID(), MH.getTitle(), MH.getAddress(), MH.getSenderName(),
					new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy").parse(MH.getDate()), MH.getPriority());
		} catch (ParseException e1) {
			e1.printStackTrace();
			Utils.fileNotFound();
		}
		// We have mail ID then we can load it in the viewMail scene
		Parent root;
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewMail.fxml"));
    	try {
			root = loader.load();
			ViewMailController viewMailController = loader.getController();
			Stage stage = new Stage();
			viewMailController.setParameters(app, mail);
            stage.setTitle("View mail");
            stage.setScene(new Scene(root, 1096, 627));
            stage.show();
		} catch (IOException e) {
			e.printStackTrace();
			Utils.fileNotFound();
		}
	}
	
	public void refresh() {
		app.loadMails();
		applyBtnClicked();
		showMails(1);
	}
	
	public void composeBtnClicked() {
		Parent root;
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("Compose.fxml"));
    	try {
			root = loader.load();
			ComposeController composeController = loader.getController();
			Stage stage = new Stage();
			composeController.setParameters(app, stage);
            stage.setTitle("Compose");
            stage.setScene(new Scene(root, 948, 500));
            stage.setOnCloseRequest(event -> {composeController.draft();} );
            stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}   
	}
	
	public void moveBtnClicked() {
		ObservableList<MailHeader> moveMails = mailsTbl.getSelectionModel().getSelectedItems();
		DoublyLinkedList mailList = new DoublyLinkedList(); 
		for (MailHeader MH : moveMails) {
			try {
				mailList.add(new Mail(MH.getTitle(), MH.getAddress(), MH.getSenderName(),
						new SimpleDateFormat("EEEE - MMM dd - yyyy HH:mm:ss a").parse(MH.getDate()), MH.getPriority()));
			} catch (ParseException e) {
				Utils.fileNotFound();
			}
		}
		//app.move
	}
	
	public void delBtnClicked() {
		ObservableList<MailHeader> moveMails = mailsTbl.getSelectionModel().getSelectedItems();
		DoublyLinkedList mailList = new DoublyLinkedList(); 
		for (MailHeader MH : moveMails) {
			try {
				mailList.add(new Mail(MH.getTitle(), MH.getAddress(), MH.getSenderName(),
						new SimpleDateFormat("EEEE - MMM dd - yyyy HH:mm:ss a").parse(MH.getDate()), MH.getPriority()));
			} catch (ParseException e) {
				Utils.fileNotFound();
			}
		}
		app.deleteEmails(mailList);
	}
	
	/*public static void main(String[] args) {
		
	}*/

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
	
	
	
	
}