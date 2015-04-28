/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ui.dialogs;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.SortedList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.Account;
import models.DataModel;
import models.Payee;
import models.RegisterEntry;
import models.Transaction;
import ui.models.TransactionComparatorByDate;

/**
 *
 * @author HP
 */
public class FullTransactionListDialog extends Application
{
    private final DataModel dataModel;
    private Stage mainStage;
    private Scene scene;
    private GridPane rootPane;
    private VBox filterPanel;
    private TableView<Transaction> tableView;
    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public FullTransactionListDialog(DataModel dataModel)
    {
        this.dataModel = dataModel;
    }

    @Override
    public void start(Stage primaryStage) throws Exception 
    {
        mainStage = new Stage();
        mainStage.initOwner(primaryStage);
        mainStage.initModality(Modality.WINDOW_MODAL);
        
        setUpRootPane();
        scene = new Scene(rootPane,800,500);
        mainStage.setScene(scene);
        mainStage.setTitle("Transactions");
        mainStage.show();
    }

    private void setUpRootPane() 
    {
        rootPane = new GridPane();
        ColumnConstraints col1 = new ColumnConstraints(500, 600, Double.MAX_VALUE, Priority.ALWAYS, HPos.LEFT, true);
        ColumnConstraints col2 = new ColumnConstraints(200, 250, 250, Priority.NEVER, HPos.LEFT, true);
        rootPane.getColumnConstraints().addAll(col1,col2);
        
        rootPane.add(setUpTopPanel(), 0, 0, 2, 1);
        setUpTable();
        rootPane.add(tableView, 0, 1);
        setUpFilterPanel();
        rootPane.add(filterPanel, 1, 1);
        
    }

    private HBox setUpTopPanel() 
    {
        HBox topPanel = new HBox(10);
        topPanel.setBackground(new Background(new BackgroundFill(Color.BISQUE, CornerRadii.EMPTY, Insets.EMPTY)));
        Label topLabel = new Label("TRANSACTIONS");
        topLabel.setFont(Font.font("Helvetica", FontWeight.EXTRA_BOLD, FontPosture.ITALIC, 15));
        ImageView topIcon = new ImageView(this.getClass().getResource("res/transactions.png").toExternalForm());
        topIcon.setFitHeight(64);
        topIcon.setFitWidth(64);
        topLabel.setGraphic(topIcon);
        topLabel.setGraphicTextGap(10);
        
        topPanel.getChildren().add(topLabel);
        return topPanel;
    }
    
    private void setUpTable()
    {
        tableView = new TableView<>();
        
        TableColumn<Transaction,String> transactionIdColumn = new TableColumn<>("Transaction ID");
        transactionIdColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Transaction, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Transaction, String> param) 
            {
                return new SimpleStringProperty(""+param.getValue().getTransactionID());
            }
        });
        tableView.getColumns().add(transactionIdColumn);
        
        TableColumn<Transaction,String> transactionDateColumn = new TableColumn<>("Date");
        transactionDateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Transaction, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Transaction, String> param) {
               return new SimpleStringProperty(param.getValue().transactionDateProperty().getValue().format(dateFormat));
            }
        });
        tableView.getColumns().add(transactionDateColumn);
        
        
        TableColumn<Transaction,String> transferFromColumn = new TableColumn<>("Transfer From");
        transferFromColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Transaction, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Transaction, String> param) 
            {
                return param.getValue().getFromAC().accountNameProperty();
            }
        });
        tableView.getColumns().add(transferFromColumn);
        
        TableColumn<Transaction,String> transferToColumn = new TableColumn<>("Transfer To");
        transferToColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Transaction, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Transaction, String> param) 
            {
                return param.getValue().getToAC().accountNameProperty();
            }
        });
        tableView.getColumns().add(transferToColumn);
        
        TableColumn<Transaction,String> payeeColumn = new TableColumn<>("Payee");
        payeeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Transaction, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Transaction, String> param) 
            {
                if(param.getValue().getPayee() != null)
                    return param.getValue().getPayee().nameProperty();
                else
                    return null;
            }
        });
        tableView.getColumns().add(payeeColumn);
        
        TableColumn<Transaction,String> memoColumn = new TableColumn<>("Memo");
        memoColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Transaction, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Transaction, String> param) 
            {
                return param.getValue().memoProperty();
            }
        });
        tableView.getColumns().add(memoColumn);
        
        TableColumn<Transaction,String> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Transaction, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Transaction, String> param) 
            {
                String str = customAmountFormat(param.getValue().getAmount());
                return new SimpleStringProperty(str);
            }
        });
        tableView.getColumns().add(amountColumn);
        amountColumn.setStyle("-fx-alignment: BASELINE_RIGHT; -fx-font-style: italic ");
        
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setMinHeight(400);
        tableView.setMinWidth(600);
        tableView.setItems(new SortedList<>(dataModel.getTransactionList(),new TransactionComparatorByDate()));
        
    }
    private void setUpFilterPanel()
    {
        filterPanel = new VBox(5);
        Label fromDateLabel = new Label("FROM DATE");
        Label toDateLabel = new Label("TO DATE");
        
        DatePicker fromDate = new DatePicker(LocalDate.MIN);
        DatePicker toDate = new DatePicker(LocalDate.MAX);
        
        Label fromAcLabel = new Label("From Account");
        ChoiceBox<Account> fromAcChoiceBox = new ChoiceBox<>(dataModel.getAccountList());
        
        Label toAcLabel = new Label("To Label");
        ChoiceBox<Account> toAcChoiceBox = new ChoiceBox<>(dataModel.getAccountList());
        
        Label payeeLabel = new Label("PAYEE");
        ChoiceBox<Payee> payeeChoiceBox = new ChoiceBox<>(dataModel.getPayeeList());
        
        Label memoLabel = new Label("MEMO");
        TextField memoField = new TextField();
        
        Label greaterThanLabel = new Label("Greater Than : > ");
        TextField greaterThanTextField = new TextField();
        
        Label lessThanLabel = new Label("Less Than : < ");
        TextField lessThanTextField = new TextField();
        
        Button filterButton = new Button("FILTER");
        
    
        filterPanel.getChildren().add(fromDateLabel);
        filterPanel.getChildren().add(toDateLabel);
        
        filterPanel.getChildren().add(fromDate);
        filterPanel.getChildren().add(toDate);
        
        filterPanel.getChildren().add(fromAcLabel);
        filterPanel.getChildren().add(toAcLabel);
        
        filterPanel.getChildren().add(fromAcChoiceBox);
        filterPanel.getChildren().add(toAcChoiceBox);
        
        filterPanel.getChildren().add(payeeLabel);
        filterPanel.getChildren().add(payeeChoiceBox);
        
        filterPanel.getChildren().add(memoLabel);
        filterPanel.getChildren().add(memoField);
        
        filterPanel.getChildren().add(greaterThanLabel);
        filterPanel.getChildren().add(greaterThanTextField);
        
        filterPanel.getChildren().add(lessThanLabel);
        filterPanel.getChildren().add(lessThanTextField);
        
        filterPanel.getChildren().add(filterButton);
    }
    public String customAmountFormat(double value ) 
    {
      DecimalFormat myFormatter = new DecimalFormat("Rs #,##,###.00");
      return(myFormatter.format(value));
      
   }
}
