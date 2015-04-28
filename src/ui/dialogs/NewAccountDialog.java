package ui.dialogs;



import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.ACCOUNT_TYPE;
import models.Account;
import models.AccountGroup;
import models.DataModel;

public class NewAccountDialog extends Application
{
	private DataModel dataModel;
	private Scene scene;
	private GridPane mainGrid;
	private Label topLabel;	
	private Label accountNameLabel;	
	private Label accountCodeLabel;	
	private Label accountDescriptionLabel;	
	private Label accountNotesLabel;	
	private TextField accountNameField;
	private TextField accountCodeField;	
	private TextField accountDescriptionField;	
	private TextArea accountNotesField;	
	private Label accountOpeningBalance;
	private TextField accountOpeningBalanceField;	
	private Label accountOpeningDateLabel;
	private DatePicker accountOpeningDateField;
	private Label typeOfAccountLabel;
	private Label groupAccountLabel;	
	private ListView<ACCOUNT_TYPE> typeOfAccountField;
	private ListView<AccountGroup> accountGroupField;
	private Button okButton;
	private Button cancelButton;
	private Label messageLabel;
	
	private Stage primaryStage;
        private boolean editMode = false;
        private Account editingAccount = null;

	public NewAccountDialog(DataModel dataModel)
	{
		this.dataModel = dataModel;
	}
        
        public NewAccountDialog() throws SQLException
        {
            dataModel = new DataModel(new SimpleStringProperty());
        }

	@Override
	public void start(Stage mainStage) throws Exception 
	{
		primaryStage = new Stage();
		primaryStage.initModality(Modality.WINDOW_MODAL);
                primaryStage.initOwner(mainStage);
        
        
       // dataModel = new DataModel();
        setUpMainGrid();
        wireUpControls();
        scene = new Scene(mainGrid,380,450);
        
        primaryStage.setScene(scene);
        primaryStage.setTitle("Enter a New Account");
        primaryStage.setResizable(false);
        primaryStage.show();
        
		
	}
	
	private void setUpMainGrid()
	{
		ColumnConstraints col1 = new ColumnConstraints(150);
		ColumnConstraints col2 = new ColumnConstraints(200);
		col2.setHgrow(Priority.ALWAYS);
                
                RowConstraints row1 = new RowConstraints(50);
                
                
		mainGrid = new GridPane();
		mainGrid.getColumnConstraints().addAll(col1,col2);
		mainGrid.setPadding(new Insets(10,10,10,10));
		mainGrid.setVgap(5);
		mainGrid.setHgap(10);
                mainGrid.setStyle(".label{-fx-text-fill: white;}");
               
		
		topLabel = new Label("NEW ACCOUNT WIZARD");
                HBox topLabelBox = new HBox(topLabel);
		
                topLabel.setFont(Font.font("Helvetica", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 20));
                topLabel.setAlignment(Pos.CENTER);
                topLabel.setGraphicTextGap(60);
                ImageView moneyIcon = new ImageView(this.getClass().getResource("res/coins48.png").toExternalForm());
                
                topLabel.setGraphic(moneyIcon);
               // topLabelBox.setBorder(new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                mainGrid.setBackground(new Background(new BackgroundImage(new Image(this.getClass().getResource("res/back1.png").toExternalForm()), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
               // topLabelBox.setBackground(new Background(new BackgroundImage(new Image(this.getClass().getResource("res/back2.png").toExternalForm()), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
                mainGrid.setStyle("-fx-font-family:'Helvetica';-fx-font-weight: bold;");
		
		accountNameLabel = new Label("Account Name ");
		
		accountCodeLabel = new Label("Account Code ");
		
		accountDescriptionLabel = new Label("Account Description");
		
		accountNotesLabel = new Label("Notes ");
		
		accountNameField = new TextField();
		
		accountCodeField = new TextField();
		
		accountDescriptionField = new TextField();
		
		accountNotesField = new TextArea();
                accountNotesField.setMaxHeight(100);
		
		accountOpeningBalance = new Label("Opening Balance");
	
		accountOpeningBalanceField = new TextField();
		
		accountOpeningDateLabel = new Label("Opening Date");
		
		accountOpeningDateField = new DatePicker(LocalDate.now());
		
		typeOfAccountLabel = new Label("Type of Account \n");
		
		groupAccountLabel = new Label("Parent Group Account");
		
		typeOfAccountField = new ListView<>();
		typeOfAccountField.setItems(FXCollections.observableArrayList((ACCOUNT_TYPE.values())));
		typeOfAccountField.setPrefWidth(Double.MAX_VALUE);
                typeOfAccountField.setMaxHeight(100);
		
		accountGroupField = new ListView<>();
		accountGroupField.setPrefWidth(Double.MAX_VALUE);
		accountGroupField.setItems(dataModel.getAccountGroupList());
                accountGroupField.setMaxHeight(100);
		
		okButton = new Button("OK");
		cancelButton = new Button("CANCEL");
		messageLabel = new Label("");
                messageLabel.setTextFill(Color.RED);
		
		
                
                mainGrid.add(topLabelBox,0,0,2,1);
                
                Rectangle bottomBorder = new Rectangle(360, 4);
                mainGrid.add(bottomBorder, 0, 1, 2, 1);
                
		mainGrid.add(accountNameLabel,0,2);
                mainGrid.add(accountNameField,1,2);
                
                mainGrid.add(accountCodeLabel,0,3);
                mainGrid.add(accountCodeField,1,3);
                
                mainGrid.add(accountDescriptionLabel, 0, 4);
                mainGrid.add(accountDescriptionField,1,4);
                
                mainGrid.add(accountNotesLabel,0,5);
                mainGrid.add(accountNotesField,1,5);
               // mainGrid.add(accountOpeningBalance, 0, 5);
               // mainGrid.add(accountOpeningBalanceField, 1, 5);
              //  mainGrid.add(accountOpeningDateLabel,0,6);
               
                mainGrid.add(typeOfAccountLabel,0,6);
                mainGrid.add(groupAccountLabel, 1, 6);
                
                mainGrid.add(typeOfAccountField,0,7);
                mainGrid.add(accountGroupField,1,7);
                
                mainGrid.add(okButton,0,8);
                GridPane.setHalignment(okButton, HPos.CENTER);
                mainGrid.add(cancelButton,1,8);
                GridPane.setHalignment(cancelButton, HPos.CENTER);
                
                Rectangle r2 = new Rectangle(360, 2);
                mainGrid.add(r2, 0, 9, 2, 1);
                mainGrid.add(messageLabel,0,10,2,1);
	}
	
	private void wireUpControls()
	{
		cancelButton.setOnAction(event -> {try {
			this.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}});
		
		okButton.setOnAction(event -> {
                if(accountNameField.getText() == null || accountNameField.getText().length() <3)
                {
                        messageLabel.setText("Invalid value in Account Name Field");
                        return;
                }

                if(accountCodeField.getText() == null)
                        accountCodeField.setText("");
                if(accountDescriptionField.getText() == null)
                        accountDescriptionField.setText("");
                if(accountNotesField.getText() == null)
                        accountNotesField.setText("");
               
                
                if(typeOfAccountField.getSelectionModel().getSelectedItem() == null)
                {
                        messageLabel.setText("Select a Valid Account Type");
                        return;
                }
                
                
                if(accountGroupField.getSelectionModel().getSelectedItem() == null)
                {
                        messageLabel.setText("SELECT A VALID PARENT GROUP");
                        return;
                }
                if(editMode == true)
                {
                    editingAccount.setAccountName(accountNameField.getText());
                    editingAccount.setAccountCode(accountCodeField.getText());
                    editingAccount.setAccountDescription(accountDescriptionField.getText());
                    editingAccount.setAccountNotes(accountNotesField.getText());
                    editingAccount.setAccountType(typeOfAccountField.getSelectionModel().getSelectedItem());
                    editingAccount.getParentGroup().removeChildAccount(editingAccount);
                    editingAccount.setParentGroup(accountGroupField.getSelectionModel().getSelectedItem());
                    accountGroupField.getSelectionModel().getSelectedItem().addChildAccount(editingAccount);
                    
                    try {
                        dataModel.updateAccount(editingAccount);
                    } catch (SQLException ex) {
                        Logger.getLogger(NewAccountDialog.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
               // Account account = new Account(openingBalance,accountOpeningDateField.getValue());
                Account account = new Account();
                account.accountNameProperty().setValue(accountNameField.getText());
                account.accountCodeProperty().setValue(accountCodeField.getText());
                account.accountDescriptionProperty().setValue(accountDescriptionField.getText());
                account.setAccountNotes(accountNotesField.getText());
                account.setAccountType(typeOfAccountField.getSelectionModel().getSelectedItem());

                account.setParentGroup(accountGroupField.getSelectionModel().getSelectedItem());
                accountGroupField.getSelectionModel().getSelectedItem().addChildAccount(account);
                
                try
                {
                    dataModel.addAccount(account);
                } catch (Exception e) 
                {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
                primaryStage.close();
		});
	}
        public void populateAccount(Account a)
        {
            accountNameField.setText(a.getAccountName());
            accountCodeField.setText(a.getAccountCode());
            accountNotesField.setText(a.getAccountNotes());
            accountDescriptionField.setText(a.getAccountDescription());
            typeOfAccountField.getSelectionModel().select(a.getAccountType());
            typeOfAccountField.scrollTo(a.getAccountType());
            accountGroupField.getSelectionModel().select(a.getParentGroup());
            accountGroupField.scrollTo(a.getParentGroup());
        }
        public void updateAccount(Account a)
        {
            this.okButton.setText("UPDATE");
            editMode = true;
            editingAccount =a;
            populateAccount(a);
            messageLabel.setText("UPDATE MODE");
        }
	 private boolean isNumeric(String str)  
	    {  
	      try  
	      {  
	        double d = Double.parseDouble(str);  
	      }  
	      catch(NumberFormatException nfe)  
	      {  
	        return false;  
	      }  
	      return true;  
	    }
	public static void main(String [] args)
	{
		
		launch(args);
	}

}
