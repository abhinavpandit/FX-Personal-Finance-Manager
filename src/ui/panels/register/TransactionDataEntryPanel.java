/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.panels.register;

import java.sql.SQLException;
import java.time.LocalDate;

import ui.panels.commons.AccountTreeView;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.StringConverter;
import models.AbstractAccount;
import models.DataModel;
import models.Payee;
import models.Account;
import models.Transaction;

/**
 *
 * @author john
 */
public class TransactionDataEntryPanel 
{
    private DataModel dataModel;
    private GridPane mainGrid;
    private StringProperty status = new SimpleStringProperty();
    private ObjectProperty<AbstractAccount> selectedAccount;
    //true -> New Transaction Mode ; false -> Edit Transaction Mode
    private BooleanProperty editMode = new SimpleBooleanProperty(false); 

    private HBox radioBox;
    RadioButton creditRadioButton;
    RadioButton debitRadioButton;
    private final ToggleGroup radioToggle;
    
    private TreeView<AbstractAccount> accountTree;

    private Label payeeLabel; 
    private Label descriptionLabel; 
    private Label numLabel;

    //TextField payeeField = new TextField();
    private ComboBox<Payee> payeeField;
    private TextField descriptionField;
    private  TextField numField;
    private Label dateLabel;
    private Label amountLabel;
    private DatePicker dateField;
    private TextField amountField;
    private  Button enterButton;
    private Button cancelButton;
    private ListView<Account> accountList;
    
    public TransactionDataEntryPanel(DataModel dataModel,ObjectProperty<AbstractAccount> selectedAccount,StringProperty status)
    {
        this.dataModel = dataModel;
        this.selectedAccount = selectedAccount;
        this.status = status;
        mainGrid = createGrid();
        radioToggle = new ToggleGroup();
       

    
    }
    
  
    private GridPane createGrid()
    {
            mainGrid = new GridPane();
            ColumnConstraints column1 = new ColumnConstraints();
            column1.setHalignment(HPos.CENTER);
            ColumnConstraints column2 = new ColumnConstraints();
            column2.setHalignment(HPos.RIGHT);
            ColumnConstraints column3 = new ColumnConstraints();
            column3.setHalignment(HPos.LEFT);
            ColumnConstraints column4 = new ColumnConstraints();
            column4.setHalignment(HPos.RIGHT);
            ColumnConstraints column5 = new ColumnConstraints();
            column5.setHalignment(HPos.LEFT);
            mainGrid.getColumnConstraints().addAll(column1,column2,column3,column4,column5);

            mainGrid.setVgap(10);
            mainGrid.setHgap(10);
            mainGrid.setPadding(new Insets(10,10,10,10));
            
           // Label accountLabel = new Label("ACCOUNT");
           // mainGrid.add(accountLabel, 0, 0);
            radioBox = new HBox();
            creditRadioButton = new RadioButton("CREDIT");
            debitRadioButton = new RadioButton("DEBIT");
            
            creditRadioButton.setToggleGroup(radioToggle);
            debitRadioButton.setToggleGroup(radioToggle);
            radioBox.getChildren().addAll(creditRadioButton,debitRadioButton);
            radioBox.setSpacing(50);
            creditRadioButton.setSelected(true);
            mainGrid.add(radioBox, 0, 0);

            /*
            TreeView<AbstractAccount> accountTree = new AccountTreeView(dataModel).getTreeView();
            accountTree.setMaxHeight(120);
            accountTree.setPrefWidth(300);
            mainGrid.add(accountTree,0,1,1,2);
            */
            accountList = new ListView<>();
            accountList.setMaxHeight(120);
            accountList.setPrefWidth(300);
            accountList.setItems(dataModel.getAccountList());
            accountList.setCellFactory(new Callback<ListView<Account>, ListCell<Account>>()
            {

            @Override
            public ListCell<Account> call(ListView<Account> arg0)
            {
                return new ListCell<Account>(){

                    @Override
                    public void updateItem(Account item, boolean empty)
                    {
                            super.updateItem(item, empty);
                            if(item!=null)
                                    setText(item.getFullName());
                            else
                                    setText("NULL ITEM");
                    }
                };

            }
            });
            mainGrid.add(accountList, 0, 1,1,2);

            Label payeeLabel = new Label("Payee");
            mainGrid.add(payeeLabel,1,0);

            Label descriptionLabel = new Label("Description");
            mainGrid.add(descriptionLabel,1,1);

            Label numLabel = new Label("Num");
            mainGrid.add(numLabel,1,2);

            //TextField payeeField = new TextField();
            payeeField = new ComboBox();
            payeeField.setMinWidth(200);
            payeeField.setItems(dataModel.getPayeeList());
            payeeField.setEditable(false);
           
            mainGrid.add(payeeField,2,0);

            descriptionField = new TextField();
            mainGrid.add(descriptionField,2,1);

            numField = new TextField();
            mainGrid.add(numField,2,2);

            dateLabel = new Label("Date");
            mainGrid.add(dateLabel,3,0);

            amountLabel = new Label("Amount");
            mainGrid.add(amountLabel, 3, 1);

            dateField = new DatePicker(LocalDate.now());
            mainGrid.add(dateField, 4, 0);

            amountField = new TextField();
            mainGrid.add(amountField, 4, 1);

            enterButton = new Button("ENTER");
            mainGrid.add(enterButton,3,2);

            cancelButton = new Button("CANCEL");
            mainGrid.add(cancelButton, 4, 2);
            
            

    //...................................................
            enterButton.setOnAction(new EventHandler<ActionEvent>() 
            {
                @Override
                public void handle(ActionEvent event) 
                {
                    if(status == null)
                        System.out.println("Button PRESS : status is NULL");
                    if(!isNumeric(amountField.getText()))
                    {
                        status.set("Amount value Should be Numeric");
                        amountField.setText("0.0");
                        return;
                    }
                    LocalDate date  = dateField.getValue();
                    if(date == null)
                    {
                        status.set("SELECT A VALID DATE");
                        return;
                    }
                    Payee payee = null;
                    if(payeeField.getValue() ==  null)
                    {
                       // payee = new Payee("New Payee :" +new Date());
                    }
                    else
                        payee = payeeField.getValue();
                    
                    AbstractAccount trfFrom=null, trfTo=null;
                    if(creditRadioButton.isSelected())
                    {
                    	
                   // trfFrom = accountTree.getSelectionModel().selectedItemProperty().get().getValue();
                    trfFrom = accountList.getSelectionModel().getSelectedItem();
                    //Account trfTo = transferToList.getSelectionModel().selectedItemProperty().get().getValue();
                    trfTo = selectedAccount.getValue();
                    }
                    else if(debitRadioButton.isSelected())
                    {
                    //  trfTo = accountTree.getSelectionModel().selectedItemProperty().get().getValue();
                    	trfTo = accountList.getSelectionModel().getSelectedItem();
                    //Account trfTo = transferToList.getSelectionModel().selectedItemProperty().get().getValue();
                      trfFrom = selectedAccount.getValue();
                    }
                  
                    if(trfFrom == trfTo)
                    {
                        status.set("From and To Accounts are same");
                        return;
                    }
                    Transaction transaction = Transaction.createTransaction((Account)trfFrom,(Account) trfTo);
                    transaction.setAmount(Double.parseDouble(amountField.getText()));
                    transaction.setTransactionDate(date);
                    transaction.setTransactionNum(numField.getText());
                    transaction.setMemo(descriptionField.getText());
                    transaction.setPayee(payee);
                    status.set("SAVING TRANSACTION");
                    try {
                        dataModel.addTransaction(transaction);
                    } catch (SQLException ex) {
                        System.out.println("exception encountered : "+ex);
                    }
                    
                    
                }
            });
            
            cancelButton.setOnAction(event -> {  
            descriptionField.clear();
            numField.clear();
            amountField.clear();
            
            });
    //...................................................


            return mainGrid;
    }
    
    public GridPane getGridPane()
    {
        return mainGrid;
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
    public void setStatusProperty(StringProperty status)
    {
        this.status = status;
        System.out.println("set status called on TransactionDataEntryPanel argument : "+status);
        
    }
    public void populateTransactionDataEntryPanel(Transaction t)
    {
        System.out.println("populate Transaction called () ");
    	if(t.fromACproperty().getValue() == selectedAccount.getValue())
    	{
            debitRadioButton.setSelected(true);
            accountList.getSelectionModel().select(t.toACproperty().getValue());
    	}
    	else if(t.toACproperty().getValue() == selectedAccount.getValue())
        {
    		creditRadioButton.setSelected(true);
            accountList.getSelectionModel().select(t.fromACproperty().getValue());
        }
        
        if(t.getPayee()!=null)
          payeeField.getSelectionModel().select(t.getPayee());
        
        System.out.println("payee field populated () ");
        descriptionField.setText(t.getMemo());
        System.out.println("memo field populated () ");
        numField.setText(t.getTransactionNum());
        dateField.setValue(t.getTransactionDate());
        amountField.setText(""+t.getAmount());
        
    	
    }
    public void changeToEditMode(Transaction t)
    {
    	
    }
}
