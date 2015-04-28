
package ui.panels.charts.graphs;

import java.time.LocalDate;
import java.time.Month;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import models.ACCOUNT_TYPE;
import models.DataModel;
import models.Payee;
import models.Transaction;


public class ExpenseByPayeeChart extends GridPane
{
    private DataModel dataModel;
    private DatePicker fromDate;
    private DatePicker toDate;
    private BarChart chart;
    private HBox topPanel;
    private Button generate;
    private StringProperty status;
    
    public ExpenseByPayeeChart(DataModel dataModel,StringProperty status)
    {
        super();
        this.dataModel = dataModel;
        this.status = status;
        this.setPadding(new Insets(15, 5, 5, 5));
        setUpTopPanel();
        setUpGraph();
        setUpMainGrid();
        setUpControls();
        chart.setData(generateChart());
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
        topPanel = new HBox(5);
        topPanel.setPadding(new Insets(5, 5, 5, 5));
        
        Label fromDateLabel = new Label("From Date ");
        topPanel.getChildren().add(fromDateLabel);
        
        fromDate = new DatePicker(LocalDate.now().minusMonths(1));
        topPanel.getChildren().add(fromDate);
        
        Label toDateLabel = new Label("To Date ");
        topPanel.getChildren().add(toDateLabel);
        
        toDate = new DatePicker(LocalDate.now());
        topPanel.getChildren().add(toDate);
        
        generate = new Button("GENERATE");
        topPanel.getChildren().add(generate);
        
        if(LocalDate.now().getMonthValue()>=4)
        {
            fromDate.setValue(LocalDate.of(LocalDate.now().getYear(), Month.APRIL,1));
        }
        else
        {
            fromDate.setValue(LocalDate.of(LocalDate.now().getYear()-1, Month.APRIL,1));
        }
    }
   
    private void setUpGraph()
    {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        chart = new BarChart<>(xAxis,yAxis);
        
    }
   
    private void setUpControls()
    {
        generate.setOnAction(event -> {
        if(fromDate.getValue().compareTo(toDate.getValue()) >0)
        {
            status.setValue("FROM DATE SHOULD BE LESS THAN OR EQUAL TO TO DATE");
            return;
        }
        chart.setData(generateChart());
        
        
        });          
    }
    private ObservableList<XYChart.Series<String,Double>> generateChart()
    {
        ObservableList<XYChart.Series<String,Double>> seriesList = FXCollections.observableArrayList();
        XYChart.Series<String,Double> expenseSeries = new XYChart.Series<>();
        expenseSeries.setName("Expense");
        for(Payee p : dataModel.getPayeeList())
        {
            double balance = getExpenseForPayee(p, fromDate.getValue(), toDate.getValue());
            if(balance >0)
            {
                expenseSeries.getData().add(new XYChart.Data<>(p.getName(), balance));
            }
        }   
 
        seriesList.add(expenseSeries);
        
        return seriesList;
              
    }
    
    private double getExpenseForPayee(Payee p,LocalDate fromDate, LocalDate toDate)
    {
        double balance = 0;
        for(Transaction t : dataModel.getTransactionList())
        {
            if(t.getPayee() == p && t.getToAC().getAccountType()==ACCOUNT_TYPE.EXPENSE)
            {
                if(t.getTransactionDate().compareTo(fromDate) >=0 && t.getTransactionDate().compareTo(toDate)<=0)
                    balance = balance + t.getAmount();
            }
        }
        return balance;
    }
}
