
package ui.panels.charts.graphs;

import java.time.LocalDate;
import java.time.Month;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import models.ACCOUNT_TYPE;
import models.Account;
import models.DataModel;
import models.Payee;
import models.Transaction;
import ui.models.TransactionComparatorByDate;


public class AccountBalanceLineChart extends GridPane
{
    private DataModel dataModel;
    private DatePicker fromDate;
    private DatePicker toDate;
    private LineChart chart;
    private HBox topPanel;
    private Button generate;
    private ChoiceBox<Account> accountChoiceBox;
    private ChoiceBox<String> intervalChoiceBox;
    private StringProperty status;
    
    public AccountBalanceLineChart(DataModel dataModel,StringProperty status)
    {
        super();
        this.dataModel = dataModel;
        this.status = status;
        this.setPadding(new Insets(15, 5, 5, 5));
        setUpTopPanel();
        setUpGraph();
        setUpMainGrid();
        setUpControls();
        
    }
    private void setUpMainGrid()
    {
        this.setHgap(5);
        RowConstraints row1 = new RowConstraints(20, 25, 25, Priority.NEVER, VPos.CENTER, true);
        RowConstraints row2 = new RowConstraints(500, 600, Double.MAX_VALUE, Priority.ALWAYS, VPos.CENTER, true);
        this.getRowConstraints().addAll(row1,row2);
        ColumnConstraints col1 = new ColumnConstraints(500, 500, Double.MAX_VALUE, Priority.ALWAYS, HPos.LEFT, true);
        this.getColumnConstraints().add(col1);
        this.add(topPanel, 0, 0);
        this.add(chart,0,1);
    }
    private void setUpTopPanel()
    {
        topPanel = new HBox(10);
        topPanel.setPadding(new Insets(5, 5, 5, 5));
        
        Label fromDateLabel = new Label("From Date ");
        topPanel.getChildren().add(fromDateLabel);
        
        
        fromDate = new DatePicker();
        topPanel.getChildren().add(fromDate);
        if(LocalDate.now().getMonthValue()>=4)
        {
            fromDate.setValue(LocalDate.of(LocalDate.now().getYear(), Month.APRIL,1));
        }
        else
        {
            fromDate.setValue(LocalDate.of(LocalDate.now().getYear()-1, Month.APRIL,1));
        }
            
        Label toDateLabel = new Label("To Date ");
        topPanel.getChildren().add(toDateLabel);
        
        toDate = new DatePicker(LocalDate.now());
        topPanel.getChildren().add(toDate);
        
        Label accountChoiceLabel = new Label("Choose Account");
        topPanel.getChildren().add(accountChoiceLabel);
        
        accountChoiceBox = new ChoiceBox<>(dataModel.getAccountList());
        topPanel.getChildren().add(accountChoiceBox);
             
        Label intervalLabel = new Label("Interval");
        topPanel.getChildren().add(intervalLabel);
        
        intervalChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList("Day","Week","Month","Year"));
        intervalChoiceBox.getSelectionModel().select(3);
        topPanel.getChildren().add(intervalChoiceBox);
        
        
        
        
        generate = new Button("GENERATE");
        topPanel.getChildren().add(generate);
    }
   
    private void setUpGraph()
    {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        chart = new LineChart(xAxis, yAxis);
        
    }
   
    private void setUpControls()
    {
        generate.setOnAction(event -> {
        if(fromDate.getValue().compareTo(toDate.getValue()) >0)
        {
            status.setValue("FROM DATE SHOULD BE LESS THAN OR EQUAL TO TO DATE");
            return;
        }
        if(accountChoiceBox.getSelectionModel().getSelectedItem() == null)
        {
            status.setValue("Choose a valid Account from ChoiceBox");
            return;
        }
        chart.setData(generateChart(accountChoiceBox.getSelectionModel().getSelectedItem()));
        
        
        });          
    }
    private ObservableList<XYChart.Series<String,Double>> generateChart(Account account)
    {
        ObservableList<XYChart.Series<String,Double>> seriesList = FXCollections.observableArrayList();
        XYChart.Series<String,Double> balanceSeries = new XYChart.Series<>();
        balanceSeries.setName("Account Balances");
        
        
        LocalDate datePoint = fromDate.getValue();
        //String label = datePoint.getDayOfMonth()+"/"+datePoint.getMonthValue()+"/"+datePoint.getYear();
       
        //balanceSeries.getData().add(new XYChart.Data<>(label, getBalanceAtDate(account, datePoint)));
       // datePoint = datePoint.plusMonths(1);
        //datePoint = LocalDate.of(datePoint.getYear(), datePoint.getMonth(), 1);
        int intervalChoice = intervalChoiceBox.getSelectionModel().getSelectedIndex();
        int pointCount = 0;
        System.out.println("interval CHoice = "+intervalChoice);
        while(datePoint.compareTo(toDate.getValue()) <0)
        {
            String label = datePoint.getDayOfMonth()+"/"+datePoint.getMonthValue()+"/"+datePoint.getYear();
            balanceSeries.getData().add(new XYChart.Data<>(label, account.getBalanceAtDate(datePoint)));
            
            if(intervalChoice == 0)
            {
                datePoint = datePoint.plusDays(1);
                System.out.println("Adding 1 DAY to datePoint");
            }
            else if(intervalChoice == 1)
            {
                datePoint = datePoint.plusWeeks(1);
                System.out.println("ADDING 1 WEEK to datePoint");
            }
            else if(intervalChoice == 2)
            {
                datePoint = datePoint.plusMonths(1);
                System.out.println("ADDING 1 MONTH to datePoint");
            }
            else if(intervalChoice == 3)
            {
                datePoint = datePoint.plusYears(1);
                System.out.println("ADDING 1 YEAR to datePoint");
            }
                
            System.out.println("GENERATED POINT : " +(++pointCount));
        }
        
        
 
        seriesList.add(balanceSeries);
        
        return seriesList;
              
    }
   
    
}
