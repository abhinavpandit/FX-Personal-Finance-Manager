/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ui.panels.charts.graphs;

import java.time.LocalDate;
import java.time.Month;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
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
import models.RegisterEntry;
import models.RegisterEntryType;
import models.Transaction;

/**
 *
 * @author HP
 */
public class ExpensePieChart extends GridPane
{
    private DataModel dataModel;
    private ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
    private DoubleProperty totalExpenses = new SimpleDoubleProperty(0.0);
    private DatePicker fromDate;
    private DatePicker toDate;
    private StringProperty status;
    private PieChart pieChart;
    private HBox topPanel;
    private Button generate;

    public ExpensePieChart(DataModel dataModel,StringProperty status) 
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
        this.add(pieChart,0,1);
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
        pieChart = new PieChart();
        
    }
   
    private void setUpControls()
    {
        generate.setOnAction(event -> {
        if(fromDate.getValue().compareTo(toDate.getValue()) >0)
        {
            status.setValue("FROM DATE SHOULD BE LESS THAN OR EQUAL TO TO DATE");
            return;
        }
        this.initializePieChartData();
        
        });
        
    }
     private void initializePieChartData()
    {
       pieChartData.clear();
       for(Account account: dataModel.getAccountList())
       {
           if(account.getAccountType() == ACCOUNT_TYPE.EXPENSE)
           {
             double balance = 0.0;
               System.out.println("considering account : "+account.getAccountName());
             for(RegisterEntry entry : account.getRegisterEntries())
             {
                 if(entry.getEntryType() == RegisterEntryType.CREDIT && entry.getTransaction().getTransactionDate().compareTo(fromDate.getValue()) >=0 && entry.getTransaction().getTransactionDate().compareTo(toDate.getValue()) <=0 )
                 {
                    balance = balance + entry.getTransaction().getAmount();
                 }
             }
             if(balance>0)
             {
                 PieChart.Data data = new PieChart.Data(account.getAccountName(), balance);
                data.setName(data.getName() + "\nRs. "+data.getPieValue());
                pieChartData.add(data);
             }
                 
           
           }
       }
        pieChart.setData(pieChartData);
       
       
    }
     
    
    
    
}
