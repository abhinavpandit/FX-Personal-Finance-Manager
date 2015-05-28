
package ui.panels.summary;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.Pair;
import models.ACCOUNT_TYPE;
import models.Account;
import models.DataModel;
import models.Transaction;

/**
 *
 * @author HP
 */

public class QuickStatisticsPanel 
{
    private TableView<Pair<String,Double>> quickStatsTableView_A;
    private TableView<Pair<String,Double>> quickStatsTableView_B;
    private TableView<Pair<Account,Double>> quickStatsTableView_C;
    private final DataModel dataModel;
    private GridPane quickStatsPanel;
    
    private Button refreshTableA,refreshTableB,refreshTableC,refreshTablesButton;
    
    private double totalExpenses = 1.0;
    public QuickStatisticsPanel(DataModel dm)
    {
        this.dataModel = dm;
        setUpGridPane();
        setUpControls();
        
    }
     private void setUpQuickStatisticsPanel()
    {
        
        //...............set up TABLE 1..............................
        quickStatsTableView_A = new TableView<>();
        TableColumn<Pair<String,Double>,String> descriptionColumn = new TableColumn("Asset/ Liability Statistics");
        descriptionColumn.setMaxWidth(250);descriptionColumn.setMinWidth(150);
        descriptionColumn.setStyle("-fx-font-style: italic; -fx-font-weight: bold ");
        descriptionColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Pair<String, Double>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Pair<String, Double>, String> param) {
                return new SimpleStringProperty(param.getValue().getKey());
            }
        });
        TableColumn<Pair<String,Double>,String> valueColumn = new TableColumn("Value");
        valueColumn.setMinWidth(150); valueColumn.setMaxWidth(400);
        valueColumn.setStyle("-fx-alignment: BASELINE_RIGHT;-fx-font-weight: bold ");
        valueColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Pair<String, Double>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Pair<String, Double>, String> param) {
                Currency indianCurrency = Currency.getInstance(new Locale("en", "IN"));
                
                NumberFormat currencyInstance = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
                String format = currencyInstance.format(param.getValue().getValue());
               return new SimpleStringProperty(format);
            }
        });
        quickStatsTableView_A.getColumns().addAll(descriptionColumn,valueColumn);
        quickStatsTableView_A.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // set up Table 2............................................
        quickStatsTableView_B = new TableView();
        TableColumn<Pair<String,Double>,String> descriptionColumn2 = new TableColumn("Income/ Expense Statistics");
        descriptionColumn2.setMaxWidth(300);descriptionColumn2.setMinWidth(200);
        descriptionColumn2.setStyle("-fx-font-style: italic; -fx-font-weight: bold ");
        descriptionColumn2.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Pair<String, Double>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Pair<String, Double>, String> param) {
                return new SimpleStringProperty(param.getValue().getKey());
            }
        });
        TableColumn<Pair<String,Double>,String> valueColumn2 = new TableColumn("Value");
        valueColumn2.setMaxWidth(350);valueColumn2.setMinWidth(200);
        valueColumn2.setStyle("-fx-alignment: BASELINE_RIGHT;-fx-font-weight: bold ");
        valueColumn2.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Pair<String, Double>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Pair<String, Double>, String> param) {
               NumberFormat currencyInstance = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
                String format = currencyInstance.format(param.getValue().getValue());
               return new SimpleStringProperty(format);
            }
        });
        quickStatsTableView_B.getColumns().addAll(descriptionColumn2,valueColumn2);
        quickStatsTableView_B.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        //...............set UP Table 3...................................................
        quickStatsTableView_C = new TableView();
        TableColumn<Pair<Account,Double>,String> expenseCategoryColumn = new TableColumn("Expense Statistics");
        expenseCategoryColumn.setMaxWidth(250);expenseCategoryColumn.setMinWidth(150);
        expenseCategoryColumn.setStyle("-fx-font-style: italic; -fx-font-weight: bold ");
        expenseCategoryColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Pair<Account, Double>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Pair<Account, Double>, String> param) {
                return param.getValue().getKey().accountNameProperty();
            }
        });
        TableColumn<Pair<Account,Double>,String> expenseAmountColumn = new TableColumn("Amount");
        expenseAmountColumn.setMaxWidth(250);expenseAmountColumn.setMinWidth(200);
        expenseAmountColumn.setStyle("-fx-alignment: BASELINE_RIGHT;-fx-font-weight: bold ");
        expenseAmountColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Pair<Account, Double>, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Pair<Account, Double>, String> param) {
                NumberFormat currencyInstance = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
                String format = currencyInstance.format(param.getValue().getValue());
               return new SimpleStringProperty(format);
            }
        });
        TableColumn<Pair<Account,Double>,String> expensePercentageColumn = new TableColumn<>("%age");
        expensePercentageColumn.setMaxWidth(120);expenseAmountColumn.setMinWidth(100);
       
        expensePercentageColumn.setStyle("-fx-alignment: BASELINE_RIGHT;-fx-font-weight: bold ");
        expensePercentageColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Pair<Account, Double>, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Pair<Account, Double>, String> param) {
                double percent = param.getValue().getValue()*100/totalExpenses;
                String format = String.format("%5.2f %%", percent);
                return new SimpleStringProperty("" +format);
            }
        }
        );
        quickStatsTableView_C.getColumns().addAll(expenseCategoryColumn,expenseAmountColumn,expensePercentageColumn);
        quickStatsTableView_C.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
       /* quickStatsPanel.getChildren().addAll(quickStatsTableView_A,quickStatsTableView_B,quickStatsTableView_C);
        HBox.setHgrow(quickStatsTableView_A, Priority.SOMETIMES);
        HBox.setHgrow(quickStatsTableView_B, Priority.SOMETIMES);
        HBox.setHgrow(quickStatsTableView_C, Priority.SOMETIMES);
        */
        populateQuickStatsTableView_A();
        populateQuickStatsTableView_B();
        populateQuickStatsTableView_C();
               
    }
    private void populateQuickStatsTableView_A()
    {
            
        double netAssets = 0.0;
        double netLiabilities = 0.0;
        for(Account a : dataModel.getAccountList())
        {
            ACCOUNT_TYPE at = a.getAccountType();
            
            if(at == ACCOUNT_TYPE.ASSET || at ==  ACCOUNT_TYPE.BANK || at==ACCOUNT_TYPE.CASH)
                netAssets = netAssets + a.getAccountBalance();
            else if(at == ACCOUNT_TYPE.LIABILITY)
                netLiabilities = netLiabilities + a.getAccountBalance();     
        }
        Pair<String,Double> totalAssets = new Pair<>("Total Assets",netAssets);
        Pair<String,Double> totalLiabilities = new Pair<>("Total Liabilities",netLiabilities);
        double netW = netAssets + netLiabilities;
        Pair<String,Double> netWorth = new Pair<>("Net Worth",netW);
        double netProf = 0.0;
        for(Transaction t : dataModel.getTransactionList())
        {
            if(t.getFromAC().getAccountType() == ACCOUNT_TYPE.INCOME)
                netProf = netProf + t.getAmount();
            if(t.getToAC().getAccountType() == ACCOUNT_TYPE.EXPENSE)
                netProf = netProf - t.getAmount();
        }
       
        Pair<String,Double> netProfit = new Pair<>("Net Profit",netProf);
        
        ObservableList<Pair<String,Double>> data = FXCollections.observableArrayList(totalAssets,totalLiabilities,netWorth,netProfit);
        quickStatsTableView_A.setItems(data);
        
    }
    private void populateQuickStatsTableView_B()
    {
        double exp_total = 0.0,inc_total = 0.0,exp_30 = 0.0,inc_30=0.0;
        for(Transaction t: dataModel.getTransactionList())
        {
            if(t.getFromAC().getAccountType() == ACCOUNT_TYPE.INCOME)
            {
                inc_total = inc_total + t.getAmount();
                LocalDate lastMonth = t.getTransactionDate().minusMonths(1);
                if(t.getTransactionDate().compareTo(lastMonth)>=0)
                    inc_30 = inc_30 + t.getAmount();
            }
            if(t.getToAC().getAccountType() == ACCOUNT_TYPE.EXPENSE)
            {
                exp_total = exp_total + t.getAmount();
                LocalDate lastMonth = t.getTransactionDate().minusMonths(1);
                if(t.getTransactionDate().compareTo(lastMonth)>=0)
                    exp_30 = exp_30 + t.getAmount();
            }
        }
        Pair<String,Double> totalExpenses = new Pair<>("Total Expenses (Financial Year)", exp_total);
        Pair<String,Double> totalIncome = new Pair<>("Total Income (Financial Year)",inc_total);
        Pair<String,Double> totalExpenses2 = new Pair<>("Total Expenses (Last 30 days)",exp_30);
        Pair<String,Double> totalIncome2 = new Pair<>("Total income (Last 30 days)",inc_30);
        ObservableList<Pair<String,Double>> data = FXCollections.observableArrayList(totalExpenses,totalIncome,totalExpenses2,totalIncome2);
        quickStatsTableView_B.setItems(data);
    }
    private void populateQuickStatsTableView_C()
    {
        LocalDate intervalStartDate = LocalDate.MIN;
        int interval = Integer.parseInt(dataModel.getApplicationProperties().getProperty("quick_expense_stat_interval"));
        String columnHeading = "";
        switch(interval)
        {
            case 0 :
                intervalStartDate = LocalDate.now().minusMonths(1);
                columnHeading = "Expense Statistics - 1 Month";
                break;
            case 1 :
                intervalStartDate = LocalDate.now().minusMonths(2);
                columnHeading = "Expense Statistics - 2 Months";
                break;
            case 2 :
                intervalStartDate = LocalDate.now().minusMonths(3);
                columnHeading = "Expense Statistics - 3 Months";
                break;
            case 3 :
                intervalStartDate = LocalDate.now().minusMonths(6);
                columnHeading = "Expense Statistics - 6 Months";
                break;
            case 4 :
                intervalStartDate = LocalDate.now().minusMonths(12);
                columnHeading = "Expense Statistics - 1 Year";
                break;
            default :
                intervalStartDate = LocalDate.MIN;
                columnHeading = "Expense Statistics - All Time";
                
        }
        
        
        Map<Account,Double> dataMap = new HashMap<Account, Double>();
      ObservableList<Pair<Account,Double>> data = FXCollections.observableArrayList();
      for(Account a : dataModel.getAccountList())
      {
          if(a.getAccountType() ==  ACCOUNT_TYPE.EXPENSE)
              dataMap.put(a, 0.0);
      }
        for(Transaction t: dataModel.getTransactionList())
        {
            if(t.getToAC().getAccountType() == ACCOUNT_TYPE.EXPENSE)
            {
                if(t.getTransactionDate().compareTo(intervalStartDate)>=0)
                {
                Double amt = dataMap.get(t.getToAC()) + t.getAmount();
                dataMap.put(t.getToAC(),amt);
                }
            }
        }
        totalExpenses = 0.0;
        for(Map.Entry<Account,Double> entry : dataMap.entrySet())
        {
            Pair<Account,Double> dataPoint = new Pair<>(entry.getKey(),entry.getValue());
            data.add(dataPoint);
            totalExpenses = totalExpenses + entry.getValue();
        }
        data.sort(new Comparator<Pair<Account, Double>>(){ 
            @Override
            public int compare(Pair<Account, Double> o1, Pair<Account, Double> o2) 
            {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        quickStatsTableView_C.setItems(data);
        quickStatsTableView_C.getColumns().get(0).setText(columnHeading);
    }
    
    private void setUpGridPane()
    {
        quickStatsPanel = new GridPane();
        quickStatsPanel.getStylesheets().add(this.getClass().getResource("summary.css").toExternalForm());
       // quickStatsPanel.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        quickStatsPanel.setHgap(10);
        ColumnConstraints col1 = new ColumnConstraints(200, 400, 600, Priority.ALWAYS, HPos.LEFT, true);
        ColumnConstraints col2 = new ColumnConstraints(200, 400, 600, Priority.ALWAYS, HPos.LEFT, true);
        ColumnConstraints col3 = new ColumnConstraints(200, 400, 600, Priority.ALWAYS, HPos.LEFT, true);
        ColumnConstraints col4 = new ColumnConstraints(10, 15, 20, Priority.NEVER, HPos.RIGHT, true);
        RowConstraints row1 = new RowConstraints(30, 30, 40, Priority.NEVER, VPos.CENTER, true);
        RowConstraints row2 = new RowConstraints(100, 150, 200, Priority.ALWAYS, VPos.TOP, true);
        quickStatsPanel.getColumnConstraints().addAll(col1,col2,col3,col4);
        quickStatsPanel.getRowConstraints().addAll(row1,row2);
        //quickStatsPanel.setBorder(new Border(new BorderStroke(Color.BLACK, null, Color.BLACK, null, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, CornerRadii.EMPTY, new BorderWidths(2), Insets.EMPTY)));
        quickStatsPanel.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        setUpQuickStatisticsPanel();
        Label label1 = new Label("Assets/Liabilities Statistics");
        label1.getStyleClass().add("quick_stats_labels");
        Label label2 = new Label("Income/Expense Statistics");
        label2.getStyleClass().add("quick_stats_labels");
        Label label3 = new Label("Expense Statistics - Last 30 days");
        label3.getStyleClass().add("quick_stats_labels");
        
        refreshTablesButton = new Button("Refresh");
        ImageView refreshIcon = new ImageView(this.getClass().getResource("res/sync.png").toExternalForm());
        refreshIcon.setFitHeight(16);
        refreshIcon.setFitWidth(16);
        refreshTablesButton.setGraphic(refreshIcon);
        refreshTablesButton.setOnAction(event -> { 
        populateQuickStatsTableView_A();
        populateQuickStatsTableView_B();
        populateQuickStatsTableView_C();
        });
        
        Label quickStatisticsLabel = new Label("Quick Statistics");
        quickStatisticsLabel.getStyleClass().add("quick_statistics_label");
       
        quickStatsPanel.add(quickStatisticsLabel, 0, 0);
        GridPane.setHalignment(quickStatisticsLabel, HPos.LEFT);
        
        quickStatsPanel.add(refreshTablesButton,2,0);
        GridPane.setHalignment(refreshTablesButton, HPos.RIGHT);
        
        quickStatsPanel.add(quickStatsTableView_A,0,1);
        quickStatsPanel.add(quickStatsTableView_B,1,1);
        quickStatsPanel.add(quickStatsTableView_C,2,1,2,1);
        
        
    }
    public Pane getPanel()
    {
        return quickStatsPanel;
    }

    private void setUpControls() 
    {
        dataModel.getTransactionList().addListener(new ListChangeListener<Transaction>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Transaction> c) 
            {
                System.out.println("QuickStatisticsPanel : onChanged() called on TransactionList");
                while(c.next())
                {
                    System.out.println("QuickStatisticsPanel : onChanged() : next() called");
                    populateQuickStatsTableView_A();
                    populateQuickStatsTableView_B();
                    populateQuickStatsTableView_C();
                }
            }
        });
                
    }
}
