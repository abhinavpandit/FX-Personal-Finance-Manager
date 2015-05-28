/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.panels.register;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventType;
import javafx.geometry.Bounds;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Popup;
import javafx.util.Callback;
import javafx.util.StringConverter;
import models.AbstractAccount;
import models.Account;
import models.AccountGroup;
import models.DataModel;
import models.Payee;
import models.Transaction;

/**
 *
 * @author john
 */
public class TransactionDataEntryPanelSlim extends HBox
{
    DatePicker dateField;
    TextField numField;
    //ChoiceBox<Payee> payeeField;
    TextField payeeField;
    //ChoiceBox<Account> accountField;
    TextField accountField;
    TextField memoField;
    TextField debitField;
    TextField creditField;
    
    private Button enterButton;
    private Button clearButton;
    private Button deleteButton;
    private DataModel dataModel;
    //TableView<Transaction> register;
    
    private final ListView<Payee> payeeListView;
    private final ListView<Account> accountListView;
    
    FilteredList<Payee> payeeList;
    FilteredList<Account> accountList;
    
    Popup payeePopUp = new Popup();
    Popup accountPopUp = new Popup();
    
    private Payee selectedPayee = null;
    private Account selectedAccount = null;
    private Transaction selectedTransaction = null;
    private StringProperty status;
    private BooleanProperty editMode = new SimpleBooleanProperty(false);
    
    private final ObjectProperty<AbstractAccount> selectedAccountFromTree;
    public TransactionDataEntryPanelSlim(DataModel dataModel,StringProperty status,ObjectProperty<AbstractAccount> selectedAccountFromTree)
    {
        this.dataModel = dataModel;
        this.selectedAccountFromTree = selectedAccountFromTree;
        this.status = status;
        this.getStylesheets().add(this.getClass().getResource("register.css").toExternalForm());
        dateField = new DatePicker(LocalDate.now());
        dateField.setConverter(new StringConverter<LocalDate>() {
           
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            @Override
            public String toString(LocalDate object) {
                if(object!=null)
                    return object.format(dateFormat);
                else
                    return null;
            }

            @Override
            public LocalDate fromString(String string) {
                if(string!=null)
                    return LocalDate.parse(string);
                else
                    return null;
            }
        });
        numField = new TextField();
        numField.setPromptText("Transaction Num");
        payeeField = new TextField();
        clearButton = new Button("CLR");
        clearButton.setMinWidth(40);
        deleteButton = new Button("DEL");
        deleteButton.setMinWidth(40);
        payeeField.setPromptText("Payee");
       
        accountField = new TextField();
        accountField.setPromptText("Transfer");
        memoField = new TextField();
        memoField.setPromptText("Memo");
        
        debitField = new TextField();
        debitField.setPromptText("Debit");
        
        creditField = new TextField();
        creditField.setPromptText("Credit");
        
        enterButton = new Button("ENTER");
        enterButton.setMinWidth(60);
        this.setMaxHeight(30);
        this.setSpacing(5);
        
        dateField.setMaxWidth(100);
        numField.setMaxWidth(120);
        payeeField.setMaxWidth(150);
        accountField.setMaxWidth(150);
        memoField.setMaxWidth(150);
        debitField.setMaxWidth(100);
        creditField.setMaxWidth(100);
        enterButton.setMaxWidth(100);
        this.getChildren().addAll(dateField,numField,payeeField,accountField,memoField,debitField,creditField,enterButton);
        this.getChildren().add(clearButton);
        this.getChildren().add(deleteButton);
        HBox.setHgrow(enterButton,Priority.NEVER);
        HBox.setHgrow(clearButton,Priority.NEVER);
        HBox.setHgrow(deleteButton,Priority.NEVER);
    
        payeeList = new FilteredList<>(dataModel.getPayeeList());
        accountList = new FilteredList<>(dataModel.getAccountList());
        
        payeeListView = new ListView<>(payeeList);
        payeeListView.setMaxHeight(150);
        payeeListView.prefWidthProperty().bind(payeeField.prefWidthProperty());
        
        accountListView = new ListView<>(accountList);
        accountListView.setMaxHeight(150);
        accountListView.prefWidthProperty().bind(accountField.prefWidthProperty());
        accountListView.setCellFactory(new Callback<ListView<Account>, ListCell<Account>>()
        {

            @Override
            public ListCell<Account> call(ListView<Account> param) 
            {
                return new ListCell<Account>(){
                @Override
                protected void updateItem(Account item, boolean empty)
                {
                    super.updateItem(item, empty);
                    if(item!=null)
                         setText(item.getFullName());
                    else
                        setText("");
                }
                };
            }
        });
       
        payeePopUp.getContent().add(payeeListView);
        accountPopUp.getContent().add(accountListView);
        
        wireUpControls();
        setUpModes();
        
    }
    
    private void wireUpControls()
    {
        payeeField.focusedProperty().addListener(new ChangeListener<Boolean>(){ 

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) 
            {
               if(newValue == true)
               {
                Bounds localToScreen = payeeField.localToScreen(payeeField.getBoundsInLocal());
                System.out.println("bounds are X : " +localToScreen.getMinX());
                System.out.println("bounds are Y : " +localToScreen.getMinY());
                payeePopUp.show(payeeField, localToScreen.getMinX(), localToScreen.getMinY()+50);
               
               // System.out.println("SHowing popup >> width : "+popup.getWidth()+"; height : "+popup.getHeight());
               }
               else
               {
                   payeePopUp.hide();         
               }
            }
        });
        
        accountField.focusedProperty().addListener(new ChangeListener<Boolean>()
        { 
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) 
            {
               if(newValue == true)
               {
                Bounds localToScreen = accountField.localToScreen(payeeField.getBoundsInLocal());
                System.out.println("bounds are X : " +localToScreen.getMinX());
                System.out.println("bounds are Y : " +localToScreen.getMinY());
                accountPopUp.show(accountField, localToScreen.getMinX(), localToScreen.getMinY()+50);
               
               // System.out.println("SHowing popup >> width : "+popup.getWidth()+"; height : "+popup.getHeight());
               }
               else
               {
                   accountPopUp.hide();         
               }
            }
        });
        
        payeeField.textProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                if(newValue != null)
                {
                payeeList.setPredicate(new Predicate() {

                    @Override
                    public boolean test(Object t) 
                    {
                      Payee p = (Payee)t;
                      if(p.getName().toLowerCase().contains(newValue.toLowerCase()))
                          return true;
                      else
                          return false;
                    }
                });
                }
                
            } 
        });
        
        accountField.textProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                if(newValue != null)
                {
                accountList.setPredicate(new Predicate() {

                    @Override
                    public boolean test(Object t) 
                    {
                      Account a = (Account)t;
                      if(a.getFullName().toLowerCase().contains(newValue.toLowerCase()))
                          return true;
                      else
                          return false;
                    }
                });
                }
                
            } 
        });
        
        payeeListView.setOnMouseClicked(event -> {
        if(event.getClickCount() == 2)
        {
            selectedPayee = payeeListView.getSelectionModel().selectedItemProperty().getValue();
            payeeField.setText(selectedPayee.getName());
            payeePopUp.hide();
        }
        
        });
        accountListView.setOnMouseClicked(event -> {
        if(event.getClickCount() == 2)
        {
            selectedAccount = accountListView.getSelectionModel().selectedItemProperty().getValue();
            accountField.setText(selectedAccount.getFullName());
            accountPopUp.hide();
        }
        
        });
        
        payeeListView.setOnKeyPressed(event -> {
        if(event.getCode() == KeyCode.ENTER)
        {
            if(payeeListView.getSelectionModel().selectedItemProperty().getValue() != null)
            {
                selectedPayee = payeeListView.getSelectionModel().selectedItemProperty().getValue();
                payeeField.setText(selectedPayee.getName());
                payeePopUp.hide();
            }
        }
        });
        
        accountListView.setOnKeyPressed(event -> {
        if(event.getCode() == KeyCode.ENTER)
        {
            if(accountListView.getSelectionModel().selectedItemProperty().getValue() != null)
            {
                selectedAccount = accountListView.getSelectionModel().selectedItemProperty().getValue();
                accountField.setText(selectedAccount.getFullName());
                accountPopUp.hide();
            }
        }
        });
        
        enterButton.setOnAction(event -> {
            try {
                enterTransaction();
             
            } catch (SQLException ex) {
                Logger.getLogger(TransactionDataEntryPanelSlim.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        clearButton.setOnAction(event -> this.editMode.setValue(false));
        clearButton.disableProperty().bind(this.editMode.not());
        deleteButton.setOnAction(event -> {
        Transaction t = selectedTransaction;
            if(t == null)
            {
                status.setValue("Select a Transaction from Table to be Deleted");
                return;
            }/*
            Alert confirmAlter = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlter.setTitle("Delete Transaction Dialong");
            confirmAlter.setHeaderText("Are you sure you want to delete Transaction");
            confirmAlter.setContentText("Transaction Date : "+t.getTransactionDate() +"\n"
                                       +"Amount Rs.       : "+t.getAmount()+"\n"
                                       +"Memo              : "+t.getMemo()
            );
            Optional<ButtonType> result = confirmAlter.showAndWait();
            if(result.get() == ButtonType.OK)
            {
                 try 
            {   dataModel.deleteTransaction(t); } 
            catch (SQLException e) 
            {   e.printStackTrace(); }
            }
            */
            try {
                dataModel.deleteTransaction(t);
            } catch (SQLException ex) {
                Logger.getLogger(TransactionDataEntryPanelSlim.class.getName()).log(Level.SEVERE, null, ex);
            }
           
        });
        
        selectedAccountFromTree.addListener(new ChangeListener<AbstractAccount>(){
            @Override
            public void changed(ObservableValue<? extends AbstractAccount> observable, AbstractAccount oldValue, AbstractAccount newValue) {
                editMode.setValue(false);
            }
        
        });
        
        creditField.textProperty().addListener(new ChangeListener<String>(){ 
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!isNumeric(newValue))
                {
                    creditField.setText(creditField.getText().substring(0, newValue.length()));
                }
            }  
        });
        debitField.textProperty().addListener(new ChangeListener<String>(){ 
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!isNumeric(newValue))
                {
                    debitField.setText(debitField.getText().substring(0, newValue.length()));
                }
            }  
        });
        
        
    }

    private void enterTransaction() throws SQLException 
    {
        Transaction t;
        double debitAmount;
        double creditAmount;
        if(!isNumeric(debitField.getText()))
        {
            if(debitField.getText() == null || debitField.getText().length() == 0)
            {
                debitAmount = 0.0;
                debitField.setText("0.0");
            }
            else
            {
            status.set("NON NUMERIC VALUE IN AMOUNT FIELD");
            return;
            }
        }
        else if(Double.parseDouble(debitField.getText()) <0)
        {
            status.set("NEGARIVE VALUE IN AMOUNT FIELD");
            return;
        }
        if(!isNumeric(creditField.getText()))
        {
            if(creditField.getText() == null || creditField.getText().length() ==0)
            {
                creditAmount = 0.0;
                creditField.setText("0.0");
            }
            else
            {
            status.set("NON NUMERIC VALUE IN AMOUNT FIELD");
            return;
            }
        }
        else if(Double.parseDouble(creditField.getText()) <0)
        {
            status.set("NEGATIVE VALUE IN AMOUNT FIELD");
            return;
        }
         debitAmount = Double.parseDouble(debitField.getText());
         creditAmount = Double.parseDouble(creditField.getText());
        
        if(creditAmount == debitAmount)
        {
            status.set("ZERO AMOUNT TRANSACTION. NOTHING TO ADD.....");
            return;
        }
        if(selectedAccountFromTree.getValue() instanceof AccountGroup)
        {
            status.set("CHOOSE VALID ACCOUNT FROM SIDE PANEL");
            return;
        }
        if(dateField.getValue() == null)
        {
            status.set("SELECT VALID DATE FOR TRANSACTION");
            return;
        }
        if(selectedAccount == null)
        {
            status.set("NO ACCOUNT SELECTED FOR TRANSACTION");
            return;
        }
        if(selectedAccountFromTree.getValue() == null)
        {
            status.set("No Account Selected in Register");
            return;
        }
        if(selectedAccountFromTree.getValue() == selectedAccount)
        {
            status.set("Error in Transaction Entry : Transaction Debits & Credits the Same Account");
            return;
        }
        if(creditAmount > debitAmount)
        {
            t = Transaction.createTransaction(selectedAccount,(Account)selectedAccountFromTree.getValue());
            t.setAmount(creditAmount - debitAmount);
        }
        else
        {
             t = Transaction.createTransaction((Account)selectedAccountFromTree.getValue(),selectedAccount);
             t.setAmount(debitAmount-creditAmount);
        }
        t.setTransactionDate(dateField.getValue());
        t.setTransactionNum(numField.getText());
        t.setPayee(selectedPayee);
        t.setMemo(memoField.getText());
        
        status.set("SAVING TRANSACTION");
        if(editMode.getValue() == false)
            dataModel.addTransaction(t);
        else
            dataModel.updateTransaction(t,selectedTransaction);
            
        numField.clear();
        payeeField.clear();
        accountField.clear();
        memoField.clear();
        debitField.clear();
        creditField.clear();
        selectedAccount = null;
        selectedPayee = null;
        selectedTransaction = null;
        
        
    }
    private boolean isNumeric(String str)  
    {  
      try  
      {  
        double d = Double.parseDouble(str);  
      }  
      catch(NumberFormatException nfe)  
      {  
          System.out.println("NFE : "+str);
          if(str == null)
              System.out.println("NULL");
        return false;  
      }  
      return true;  
    }

    private void setUpModes() 
    {
        editMode.addListener(new ChangeListener<Boolean>(){
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
            {
            if(newValue == true)
            {
                enterButton.setText("UPDATE");
                
            }
            else
            {
                enterButton.setText("ENTER");
                numField.clear();
                payeeField.clear();
                accountField.clear();
                memoField.clear();
                debitField.clear();
                creditField.clear();
                selectedAccount = null;
                selectedPayee = null;
                selectedTransaction = null;
            }
            }
        
        });
    }
    
    public void changeToEditMode(Transaction t)
    {
        this.editMode.setValue(true);
        populateTransaction(t);
    }

    private void populateTransaction(Transaction t) 
    {
        selectedTransaction = t;
        dateField.setValue(t.getTransactionDate());
        numField.setText(t.getTransactionNum());
        memoField.setText(t.getMemo());
        if(t.getPayee() !=null)
        {
            payeeField.setText(t.getPayee().toString());
            selectedPayee = t.getPayee();
        }
        if(t.getFromAC() == selectedAccountFromTree.getValue())
        {
            accountField.setText(t.getToAC().getFullName());
            debitField.setText(""+t.getAmount());
            selectedAccount = t.getToAC();
        }
        else if(t.getToAC() == selectedAccountFromTree.getValue())
        {
            accountField.setText(t.getFromAC().getFullName());
            creditField.setText(""+t.getAmount());
            selectedAccount =  t.getFromAC();
                    
        }
        
    
    }
}
