/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.dialogs;

import com.sun.javafx.sg.prism.NGCanvas;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import javax.swing.RowFilter;

import models.AbstractAccount;
import models.AccountGroup;
import models.DataModel;
import models.Payee;
import models.Account;
import models.Transaction;
import ui.panels.commons.AccountTreeView;

/**
 *
 * @author john
 */
public class NewTransactionDialog extends Application
{
   
    private Transaction transaction;
    private Scene scene;
    private GridPane mainGrid;
    private DataModel dataModel;
    private TreeView<AbstractAccount> accountTree;
    private Stage primaryStage;
 
    
    public NewTransactionDialog(DataModel dataModel)
    {
       this.dataModel = dataModel;
       
     
    }

    @Override
    public void start(Stage mainStage) throws Exception 
    {
        primaryStage = new Stage();
        primaryStage.initModality(Modality.WINDOW_MODAL);
        primaryStage.initOwner(mainStage);
       
	GridPane mainGrid = getWindow();
	Scene scene = new Scene(mainGrid,450,400);

	
	mainGrid.prefWidthProperty().bind(scene.widthProperty());
	
	primaryStage.initModality(Modality.WINDOW_MODAL);
        primaryStage.initOwner(mainStage);
	primaryStage.setTitle("Enter a New Transaction");
	primaryStage.setScene(scene);
	primaryStage.show();
    }
    private GridPane getWindow()
    {
           
            Font myFont = Font.font("Helvetica", FontWeight.BOLD, 14);
            GridPane mainGrid = new GridPane();
            mainGrid.setPadding(new Insets(10,10,10,10));
            ColumnConstraints column1 = new ColumnConstraints(200);
           
            ColumnConstraints column2 = new ColumnConstraints(200);

            mainGrid.getColumnConstraints().addAll(column1,column2);
            Label amountLabel = new Label("Amount");
            amountLabel.setFont(myFont);
            Label dateLabel = new Label("Date");
            dateLabel.setFont(myFont);
            Label numLabel = new Label("Num");
            numLabel.setFont(myFont);
            Label payeeLabel = new Label("Payee");
            payeeLabel.setFont(myFont);
            Label descriptionLabel = new Label ("Description");
            descriptionLabel.setFont(myFont);
            Label transferFrom = new Label("Transfer From");
            transferFrom.setFont(myFont);
            Label transferTo = new Label("Transfer To");
            transferTo.setFont(myFont);

            mainGrid.add(amountLabel, 0, 0);
            mainGrid.add(dateLabel, 0, 1);
            mainGrid.add(numLabel, 0, 2);
            mainGrid.add(payeeLabel, 0, 3);
            mainGrid.add(descriptionLabel, 0, 4);
            mainGrid.add(transferFrom, 0, 5);

            TextField amountField = new TextField();
            DatePicker dateField = new DatePicker();
            dateField.setMinWidth(200);
            TextField numField = new TextField();
            ComboBox<Payee> payeeField = new ComboBox();
            payeeField.setMinWidth(200);
            payeeField.setItems(dataModel.getPayeeList());
            payeeField.setEditable(false);
           /* payeeField.setConverter(new StringConverter<Payee>() {

                @Override
                public String toString(Payee object) {
                  if(object!=null)
                      return object.toString();
                  else
                      return null;
                }

                @Override
                public Payee fromString(String string) {
                    return new Payee(string);
                }
            });*/
            TextField descriptionField = new TextField();

            mainGrid.add(amountField,1,0);
            GridPane.setHalignment(amountField, HPos.RIGHT);
            mainGrid.add(dateField, 1, 1);
            GridPane.setHalignment(dateField, HPos.RIGHT);
            mainGrid.add(numField, 1, 2);
            GridPane.setHalignment(numField, HPos.RIGHT);
            mainGrid.add(payeeField, 1, 3);
            GridPane.setHalignment(payeeField, HPos.RIGHT);
            mainGrid.add(descriptionField,1,4);
            GridPane.setHalignment(transferTo, HPos.RIGHT);
            mainGrid.add(transferTo, 1, 5);

            TreeView<AbstractAccount> transferFromList = accountTree = new AccountTreeView(dataModel).getTreeView();;
           // transferFromList.setItems(personList);
            transferFromList.setMaxHeight(100);
            TreeView<AbstractAccount> transferToList = accountTree = new AccountTreeView(dataModel).getTreeView();
          //  transferToList.setItems(personList);
            transferToList.setMaxHeight(100);


            mainGrid.add(transferFromList, 0, 6);
            mainGrid.add(transferToList,1,6);

            mainGrid.setVgap(15);
            mainGrid.setHgap(10);
            mainGrid.setMinWidth(400);
            mainGrid.setPadding(new Insets(10,10,10,10));
            
            Button enterButton = new Button("ENTER");
            
            
            Button cancelButton = new Button("CANCEL");
            
            mainGrid.add(enterButton, 0, 7);
            mainGrid.add(cancelButton, 1, 7);
            GridPane.setHalignment(enterButton, HPos.CENTER);
            GridPane.setHalignment(cancelButton, HPos.CENTER);
            
            Label messageLabel = new Label("Please enter a Transaction");
            mainGrid.add(messageLabel, 0, 8, 1, 2);
            
            
            enterButton.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) 
                {
                    messageLabel.setText("");
                    if(!isNumeric(amountField.getText()))
                    {
                        messageLabel.setText("Amount value Should be Numeric");
                        amountField.setText("0.0");
                        return;
                    }
                    LocalDate date  = dateField.getValue();
                    if(date == null)
                    {
                        messageLabel.setText("SELECT A VALID DATE");
                        return;
                    }
                    Payee payee = null;
                    if(payeeField.getValue() ==  null)
                    {
                       // payee = new Payee("New Payee :" +new Date());
                    }
                    else
                        payee = payeeField.getValue();
                    
                    AbstractAccount trfFrom = transferFromList.getSelectionModel().selectedItemProperty().get().getValue();
                    AbstractAccount trfTo = transferToList.getSelectionModel().selectedItemProperty().get().getValue();
                    if(trfFrom instanceof AccountGroup)
                    {
                        messageLabel.setText(trfFrom.getAccountName() +" is a placeholder Account");
                        return;
                    }
                    if(trfTo instanceof AccountGroup)
                    {
                        messageLabel.setText(trfTo.getAccountName() +" is a placeholder Account");
                        return;
                    }
                    if(trfFrom == trfTo)
                    {
                        messageLabel.setText("From and To Accounts are same");
                        return;
                    }
                    transaction = Transaction.createTransaction((Account) trfFrom, (Account)trfTo);
                    transaction.setAmount(Double.parseDouble(amountField.getText()));
                    transaction.setTransactionDate(date);
                    transaction.setTransactionNum(numField.getText());
                    transaction.setMemo(descriptionField.getText());
                    transaction.setPayee(payee);
                    messageLabel.setText("SAVING TRANSACTION");
                    try 
                    {
                        dataModel.addTransaction(transaction);
                    } catch (SQLException ex) 
                    {
                        System.out.println("exception encountered : "+ex);
                    }
                    
                  primaryStage.close();
                }
            });
            
            cancelButton.setOnAction(event -> {try {
                this.stop();
                } catch (Exception ex)
                {
                    System.out.println("Exception encountered : " +ex);
                }
                });
            //mainGrid.gridLinesVisibleProperty().set(true);
            return mainGrid;

    }
    public void launchWindow()
    {
        launch();
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
    
}
