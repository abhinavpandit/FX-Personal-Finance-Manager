/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ui.dialogs;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.Observable;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.DataModel;
import models.ScheduledTransaction;

/**
 *
 * @author HP
 */
public class TransactionCalendar extends Application
{
    private Stage mainStage;
    private Scene scene;
    private VBox rootPane;
    private TableView<ScheduledTransaction> table;
    private HBox topBar;
    private Button newScheduledTrnansactionButton;
    private Button deleteScheduledTransactionButton;
    private Button checkScheduledTransactionsButton;
    private DataModel dataModel;

    public TransactionCalendar(DataModel dataModel) 
    {
        this.dataModel = dataModel;
    }
    
    
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        mainStage = new Stage();
        mainStage.initModality(Modality.WINDOW_MODAL);
        mainStage.initOwner(primaryStage);
        
        setUpRootPane();
        scene = new Scene(rootPane);
        mainStage.setScene(scene);
        mainStage.show();
            
    }

    private void setUpRootPane()
    {
       rootPane = new VBox(5);
       setUpToolBar();
       setUpTable();
       setUpControls();
       
       
       
       rootPane.getChildren().add(table);
       VBox.setVgrow(table, Priority.ALWAYS);
    }
    private void setUpToolBar()
    {
        HBox topLabelBox = new HBox(10);
        Label topLabel = new Label("SCHEDULED TRANSACTIONS");
        topLabel.setFont(Font.font("Helvetica", FontWeight.EXTRA_BOLD, FontPosture.ITALIC, 15));
        ImageView topLabelIcon = new ImageView(this.getClass().getResource("res/topLabelIcon.png").toExternalForm());
        topLabelIcon.setFitHeight(64);
        topLabelIcon.setFitWidth(64);
        topLabel.setGraphic(topLabelIcon);
        topLabel.setGraphicTextGap(20);
        topLabelBox.getChildren().add(topLabel);
        rootPane.getChildren().add(topLabelBox);
        topLabelBox.setAlignment(Pos.CENTER);
        topBar = new HBox(10);
        newScheduledTrnansactionButton = new Button("New Scheduled Transaction");
        deleteScheduledTransactionButton = new Button("Delete Scheduled Transaction");
        checkScheduledTransactionsButton = new Button("Check Scheduled Transactions");
        topBar.getChildren().addAll(newScheduledTrnansactionButton,deleteScheduledTransactionButton,checkScheduledTransactionsButton);
        topBar.setAlignment(Pos.CENTER);
        topBar.setMaxHeight(40);
        topLabelBox.getChildren().add(topBar);
        
    }
    private void setUpTable()
    {
        table = new TableView<>();
        
        TableColumn<ScheduledTransaction,Double> amountColumn = new TableColumn<ScheduledTransaction, Double>("Amount");
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        table.getColumns().add(amountColumn);
        
        
        TableColumn<ScheduledTransaction,String> fromAcColumn = new TableColumn<>("From A/C");
        fromAcColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ScheduledTransaction, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ScheduledTransaction, String> param) {
                return param.getValue().getFromAC().accountNameProperty();
            }
        });
        table.getColumns().add(fromAcColumn);
        
        TableColumn<ScheduledTransaction,String> toAcColumn = new TableColumn<>("To A/C");
        toAcColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ScheduledTransaction, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ScheduledTransaction, String> param) {
                return param.getValue().getToAC().accountNameProperty();
            }
        });
        table.getColumns().add(toAcColumn);
        
        TableColumn<ScheduledTransaction,String> memoColumn = new TableColumn<>("Memo");
        memoColumn.setCellValueFactory(new PropertyValueFactory<>("memo"));
        table.getColumns().add(memoColumn);
       
        TableColumn<ScheduledTransaction,String> startDateColumn = new TableColumn<>("Start Date");
        startDateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ScheduledTransaction, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ScheduledTransaction, String> param) {
                return new SimpleStringProperty(param.getValue().getStartDate().toString());
            }
        });
        table.getColumns().add(startDateColumn);
        
        TableColumn<ScheduledTransaction,String> endDateColumn = new TableColumn<>("End Date");
        endDateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ScheduledTransaction, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ScheduledTransaction, String> param) {
                if(param.getValue().getEndDate() !=null)
                     return new SimpleStringProperty(param.getValue().getEndDate().toString());
                else 
                    return null;
            }
        });
        table.getColumns().add(endDateColumn);
        
        TableColumn<ScheduledTransaction,String> nextDueDateColumn = new TableColumn<>("Next Due Date");
        nextDueDateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ScheduledTransaction, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ScheduledTransaction, String> param) {
               if(param.getValue().getNextDueDate()!=null)
                return new SimpleStringProperty(param.getValue().getNextDueDate().toString());
               else 
                   return null;
            }
        });
        table.getColumns().add(nextDueDateColumn);
        
        TableColumn<ScheduledTransaction,String> lastCommitDateColumn = new TableColumn<>("Last Commit Date");
        lastCommitDateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ScheduledTransaction, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ScheduledTransaction, String> param) 
            {
                if(param.getValue().getLastCommitDate()!=null)
                return new SimpleStringProperty(param.getValue().getLastCommitDate().toString());
                else
                    return null;
            }
        });
        table.getColumns().add(lastCommitDateColumn);
        
        TableColumn<ScheduledTransaction,String> frequencyColumn = new TableColumn<>("Frequency");
        frequencyColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ScheduledTransaction, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ScheduledTransaction, String> param) {
                return new SimpleStringProperty(param.getValue().getTransactionFrequency().toString());
            }
        });
        table.getColumns().add(frequencyColumn);
        
        TableColumn<ScheduledTransaction,String> onWeekDayColumn = new  TableColumn<>("On Week Day");
        onWeekDayColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ScheduledTransaction, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ScheduledTransaction, String> param) {
               if(param.getValue().getOnWeekDay() == null)
                   return null;
               else
                   return new SimpleStringProperty(param.getValue().getOnWeekDay().toString());
            }
        });
        table.getColumns().add(onWeekDayColumn);
        
        TableColumn<ScheduledTransaction,String> onDayOfMonthColumn = new TableColumn<>("On Day");
        onDayOfMonthColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ScheduledTransaction, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ScheduledTransaction, String> param) {
                return new SimpleStringProperty("" +param.getValue().getOnDate());
                
            }
        });
        table.getColumns().add(onDayOfMonthColumn);
        
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setItems(dataModel.getScheduledTransactionList());
    }
    private void setUpControls()
    {
        newScheduledTrnansactionButton.setOnAction(event -> { 
        ui.dialogs.ScheduleTransactionDialog schTrDialog = new ScheduleTransactionDialog(dataModel);
            try {
                schTrDialog.start(mainStage);
            } catch (Exception ex) {
                Logger.getLogger(TransactionCalendar.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        });
        deleteScheduledTransactionButton.setOnAction(event -> { 
            try {
                dataModel.removeScheduledTransaction(table.getSelectionModel().getSelectedItem());
            } catch (SQLException ex) {
                Logger.getLogger(TransactionCalendar.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        });
        checkScheduledTransactionsButton.setOnAction(event -> { 
            try {
                dataModel.commitScheduledTransactions();
            } catch (SQLException ex) {
                Logger.getLogger(TransactionCalendar.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        });
    }
}
