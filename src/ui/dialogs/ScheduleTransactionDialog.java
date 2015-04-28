/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.dialogs;

import com.sun.javafx.sg.prism.NGCanvas;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import javax.swing.RowFilter;

import models.AbstractAccount;
import models.Account;
import models.AccountGroup;
import models.DataModel;
import models.Payee;
import models.ScheduledTransaction;
import models.TRANSACTION_FREQUENCY;
import models.Transaction;
import utilities.WEEKDAYS;
import ui.panels.commons.AccountTreeView;

/**
 *
 * @author john
 */
public class ScheduleTransactionDialog extends Application
{
   
    private Transaction transaction;
    private Scene scene;
    private GridPane mainGrid;
    private VBox rootPane;
    private DataModel dataModel;
    private TreeView<AbstractAccount> accountTree;
    private Stage primaryStage;
 
    private TextField amountField;
    private TextField numField;
    private ChoiceBox<Payee> payeeField;
    private TextField memoField;
    private TreeView<AbstractAccount> transferFromTreeField;
    private TreeView<AbstractAccount> transferToTreeField;
    private Button enterButton;
    private Button cancelButton;
    private DatePicker startDateField;
    private DatePicker endDateField;
    private ChoiceBox<models.TRANSACTION_FREQUENCY> transactionFrequencyField;
    private Label statusLabel = new Label();
    private HBox dynamicHBox = new HBox(10);
    private ChoiceBox<Integer> dayOfMonthField;
  //  private Spinner<WEEKDAYS> onWeekDayField;
    private Spinner<DayOfWeek> onWeekDayField;
    private Spinner<Integer> onDayOfMonthField;
    
    public ScheduleTransactionDialog(DataModel dataModel)
    {
       this.dataModel = dataModel;
    }
    public ScheduleTransactionDialog()
    {
        try {
            dataModel = new DataModel(new SimpleStringProperty());
        } catch (SQLException ex) {
            Logger.getLogger(ScheduleTransactionDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void start(Stage mainStage) throws Exception 
    {
        primaryStage = new Stage();
        primaryStage.initModality(Modality.WINDOW_MODAL);
        primaryStage.initOwner(mainStage);
        setUpRootPane();
        wireUpControls();
        rootPane.getStylesheets().add(this.getClass().getResource("res/scheduleTransactionDialog.css").toExternalForm());
        
	Scene scene = new Scene(rootPane,500,500);

	primaryStage.setTitle("SCHEDULE TRANSACTION");
	primaryStage.setScene(scene);
	primaryStage.show();
    }
    
    private void setUpRootPane()
    {
        rootPane = new VBox(5);
        setUpTopLabel();
        setUpMainGrid();
        rootPane.getChildren().add(mainGrid);
        
        
    }
    private void setUpTopLabel()
    {
        Label topLabel = new Label("SCHEDULE TRANSACTION"); 
        topLabel.setFont(Font.font("Helvetica", FontWeight.BLACK, FontPosture.ITALIC, 17));
        ImageView icon = new ImageView(this.getClass().getResource("res/schedule.png").toExternalForm());
        icon.setFitHeight(64);
        icon.setFitWidth(64);
        topLabel.setGraphic(icon);
        topLabel.setGraphicTextGap(20);
        HBox topLabelBox = new HBox(topLabel);
        topLabelBox.setBackground(new Background(new BackgroundFill(Color.BISQUE, CornerRadii.EMPTY, Insets.EMPTY)));
        rootPane.getChildren().add(topLabelBox);
    }
  
    private void setUpMainGrid()
    {
        mainGrid = new GridPane();
        mainGrid.setVgap(5);
        mainGrid.setHgap(5);
        mainGrid.setPadding(new Insets(5));
        
        Label amountLabel = new Label("Amount");
        amountField = new TextField();
        
        Label numLabel = new Label("Num");
        numField = new TextField();
        
        Label payeeLabel = new Label("Payee");
        payeeField = new ChoiceBox<>(dataModel.getPayeeList());
        
        Label memoLabel = new Label("Memo");
        memoField = new TextField();
        
        Label transferFromLabel = new Label("Transfer From");
        Label transferToLabel = new Label("Transfer To");
        
        transferFromTreeField = new AccountTreeView(dataModel).getTreeView();
        transferToTreeField = new AccountTreeView(dataModel).getTreeView();
        transferFromTreeField.setMaxHeight(150);
        transferToTreeField.setMaxHeight(150);
        
        Label frequencyLabel = new Label("Frequency");
        transactionFrequencyField = new ChoiceBox<models.TRANSACTION_FREQUENCY>(FXCollections.observableArrayList(models.TRANSACTION_FREQUENCY.values()));
        
        dynamicHBox.setMinHeight(30);
        startDateField = new DatePicker();
        endDateField = new DatePicker();
        startDateField.setPromptText("FROM DATE");
        endDateField.setPromptText("TO DATE");
        
        
        
        Label onWeekDayLabel = new Label("WeekDay");
      
        onWeekDayField = new Spinner<>(FXCollections.observableArrayList(DayOfWeek.values()));
        onDayOfMonthField = new Spinner<>(1, 31, 1, 1);
        
        
        dayOfMonthField = new ChoiceBox<Integer>();
        for(int i=1;i<=31;i++)
        {
            dayOfMonthField.getItems().add(i);
        }
        
        
        enterButton = new Button("ENTER");
        cancelButton = new Button("CANCEL");
        
        mainGrid.add(amountLabel, 0, 0);
        mainGrid.add(amountField,1,0);
        mainGrid.add(numLabel,0,1);
        mainGrid.add(numField,1,1);
        mainGrid.add(payeeLabel,0,2);
        mainGrid.add(payeeField, 1, 2);
        mainGrid.add(memoLabel,0,3);
        mainGrid.add(memoField,1,3);
        mainGrid.add(transferFromLabel,0,4);
        mainGrid.add(transferToLabel,1,4);
        mainGrid.add(transferFromTreeField,0,5);
        mainGrid.add(transferToTreeField,1,5);
        Rectangle r1 = new Rectangle(490, 2);
        mainGrid.add(r1, 0, 6,2,1);
        mainGrid.add(frequencyLabel,0,7);
        mainGrid.add(transactionFrequencyField,1,7);
        mainGrid.add(startDateField,0,8);
        mainGrid.add(endDateField,1,8);
        mainGrid.add(new Label("On Day of the Month "),0,9);
        mainGrid.add(onDayOfMonthField,1,9);
        mainGrid.add(new Label("On Day of Week "),0,10);
        mainGrid.add(onWeekDayField,1,10);
       
        mainGrid.add(enterButton,0,11);
        mainGrid.add(cancelButton,1,11);
        mainGrid.add(statusLabel,0,12,2,1);
        
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
    
    private void wireUpControls()
    {
        //transactionFrequencyField.getSelectionModel().clearAndSelect(1);
        amountField.setText("0.0");
    
        transactionFrequencyField.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TRANSACTION_FREQUENCY>(){ 

        @Override
        public void changed(ObservableValue<? extends TRANSACTION_FREQUENCY> observable, TRANSACTION_FREQUENCY oldValue, TRANSACTION_FREQUENCY newValue) 
        {
         if(newValue == TRANSACTION_FREQUENCY.ONCE)
         {
            startDateField.setDisable(false);
            endDateField.setDisable(true);
            onDayOfMonthField.setDisable(true);
            onWeekDayField.setDisable(true);
         }
         else if(newValue == TRANSACTION_FREQUENCY.DAILY)
         {
            startDateField.setDisable(false);
            endDateField.setDisable(false);
            onDayOfMonthField.setDisable(true);
            onWeekDayField.setDisable(true);
 
         }
         if(newValue == TRANSACTION_FREQUENCY.WEEKLY)
         {
            startDateField.setDisable(false);
            endDateField.setDisable(false);
            onDayOfMonthField.setDisable(true);
            onWeekDayField.setDisable(false);
         }
         if(newValue == TRANSACTION_FREQUENCY.MONTHLY)
         {
             startDateField.setDisable(false);
            endDateField.setDisable(false);
            onDayOfMonthField.setDisable(false);
            onWeekDayField.setDisable(true);
         }
        }
        });
        
        transactionFrequencyField.getSelectionModel().clearAndSelect(1);
        enterButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) 
            {
              if(!isNumeric(amountField.getText()))
              {
                  statusLabel.setText("INVALID AMOUNT VALUE");
                  return;
              }
              double amount = Double.parseDouble(amountField.getText());
              String num = numField.getText();
              Payee payee = payeeField.getSelectionModel().getSelectedItem();
               
              String memo = memoField.getText();
              
                TreeItem<AbstractAccount> selectedItem = transferFromTreeField.getSelectionModel().getSelectedItem();
                if(selectedItem == null || selectedItem.getValue() instanceof AccountGroup)
                {
                    statusLabel.setText("INVALID TRANSFER FROM ACCOUNT");
                    return;
                }
                Account fromAc = (Account)selectedItem.getValue();
                
                TreeItem<AbstractAccount> selectedItem2 = transferToTreeField.getSelectionModel().getSelectedItem();
                if(selectedItem2 == null || selectedItem2.getValue() instanceof AccountGroup)
                {
                    statusLabel.setText("INVALID TRANSFER TO ACCOUNT");
                    return;
                }
                Account toAc = (Account)selectedItem2.getValue();
                if(fromAc == toAc)
                {
                    statusLabel.setText("FROM AND TO ACCCOUNT CANNOT BE SAME");
                    return;
                }
                ScheduledTransaction tr = new ScheduledTransaction(utilities.UniqueID.get());
                tr.setAmount(amount);
                tr.setMemo(memo);
                tr.setFromAC(fromAc);
                tr.setToAC(toAc);
                tr.setTransactionNum(num);
                
                    tr.setPayee(payee);
                
                TRANSACTION_FREQUENCY transactionFrequency = transactionFrequencyField.getSelectionModel().getSelectedItem();
                tr.setTransactionFrequency(transactionFrequency);
                if(transactionFrequency == TRANSACTION_FREQUENCY.ONCE)
                {
                    if(startDateField.getValue()== null || startDateField.getValue().compareTo(LocalDate.now()) <=0)
                    {
                        statusLabel.setText("Choose a Valid On Date");
                        return;
                    }
                    tr.setStartDate(startDateField.getValue());
                }
                else if(transactionFrequency == TRANSACTION_FREQUENCY.DAILY)
                {
                    LocalDate fromDate = startDateField.getValue();
                    LocalDate toDate = endDateField.getValue();
                    
                    if(fromDate == null || toDate == null || fromDate.compareTo(toDate) >=0)
                    {
                        statusLabel.setText("Choose Valid From & To Dates");
                        return;
                    }
                    tr.setStartDate(startDateField.getValue());
                    tr.setEndDate(endDateField.getValue());
                    
                }
                else if(transactionFrequency == TRANSACTION_FREQUENCY.WEEKLY)
                {
                    LocalDate fromDate = startDateField.getValue();
                    LocalDate toDate = endDateField.getValue();
                    
                    if(fromDate == null || toDate == null || fromDate.compareTo(toDate) >=0)
                    {
                        statusLabel.setText("Choose Valid from & To Dates");
                        return;
                    }
                    tr.setStartDate(startDateField.getValue());
                    tr.setEndDate(endDateField.getValue());
                    tr.setOnWeekDay(onWeekDayField.getValue());
                }
                else if(transactionFrequency == TRANSACTION_FREQUENCY.MONTHLY)
                {
                    LocalDate fromDate = startDateField.getValue();
                    LocalDate toDate = endDateField.getValue();
                    
                    if(fromDate == null || toDate == null || fromDate.compareTo(toDate) >=0)
                    {
                        statusLabel.setText("Choose Valid from & To Dates");
                        return;
                    }
                    tr.setStartDate(startDateField.getValue());
                    tr.setEndDate(endDateField.getValue());
                    tr.setOnDate(onDayOfMonthField.getValue());
                }
                try 
                {
                    dataModel.addScheduledTransaction(tr);
                } catch (SQLException ex) {
                    Logger.getLogger(ScheduleTransactionDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            statusLabel.setText("successfully Scheduled Transaction : "+tr.getTransactionID());
            }
        });
    }
   
    public static void main(String [] args)
    {
        launch(args);
    }
   
}
