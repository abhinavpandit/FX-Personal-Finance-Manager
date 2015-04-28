/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ui.panels.charts;

import java.time.LocalDate;
import java.time.Month;
import java.util.Locale;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.Chart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import models.DataModel;
import ui.panels.charts.graphs.AccountBalanceLineChart;
import ui.panels.charts.graphs.ExpenseByPayeeChart;
import ui.panels.charts.graphs.ExpensePieChart;
import ui.panels.charts.graphs.IncomeVsExpenseBarChartPane;
import ui.panels.charts.graphs.NetWorthLineChart;


/**
 *
 * @author HP
 */
public class ChartPane extends BorderPane
{
    //private DatePicker fromDate;
    //private DatePicker toDate;
    private ChoiceBox<String> chartChooser;
    //public Button generateButton;
    private ObservableList<String> choices;
    private HBox topPanel;
    private DataModel dataModel;
    //private Chart currentChart = null;
    private StringProperty status;
    private HBox controlPanel = new HBox();
    
    //charts
    //private ExpensePieChart expensePieChart = null;
     private IncomeVsExpenseBarChartPane incomeVsExpenseChart = null;
     private ExpensePieChart expensePieChart;
     private ExpenseByPayeeChart expenseByPayeeChart = null;
     private AccountBalanceLineChart accountBalanceLineChart = null;
     private NetWorthLineChart netWorthLineChart = null;
    public ChartPane(DataModel dataModel, StringProperty status)
    {
        super();
        this.dataModel = dataModel;
        this.status = status;
        this.setPadding(new Insets(1, 10, 1, 1));
        
        
        this.getStylesheets().add(this.getClass().getResource("res/ChartPane.css").toExternalForm());

        generateTopPanel();
        this.setTop(topPanel);
        
        BorderPane.setAlignment(topPanel, Pos.CENTER);


        wireUpControls();
        
                
    }
    
    private void generateTopPanel()
    {
        generateChartChooser();

        topPanel = new HBox(20);
        topPanel.setPadding(new Insets(5, 5, 10, 5));
        
        
        
        topPanel.getChildren().add(controlPanel);
        HBox.setHgrow(controlPanel, Priority.ALWAYS);
        controlPanel.setMaxWidth(Double.MAX_VALUE);
        topPanel.setMaxHeight(20);
       
        Label chooseChartTypeLabel = new Label("CHOOSE CHART TYPE : ");
        chooseChartTypeLabel.setTextFill(Color.WHITE);
        topPanel.getChildren().add(chooseChartTypeLabel);
        
        topPanel.getChildren().add(chartChooser);
        chartChooser.setMinWidth(200);
        //topPanel.setAlignment(Pos.C);
        //controlPanel.setBorder(new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        topPanel.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    
    private void wireUpControls()
    {
        chartChooser.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>(){ 
  
        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) 
        {
            //...... ON selecting Expense PIE Chart
            if(newValue.intValue() == 0)
           {
               System.out.println("CHOICE 0 selected");
               if(expensePieChart == null)
                   expensePieChart = new ExpensePieChart(dataModel, status);
                setCenter(expensePieChart); 
           }
            
            //...............On selecting Income Vs Expense Bar Chart
            else if(newValue.intValue() == 1)
           {
               System.out.println("CHOICE 1 selected");
               if(incomeVsExpenseChart == null)
                   incomeVsExpenseChart = new IncomeVsExpenseBarChartPane(dataModel, status);
                setCenter(incomeVsExpenseChart); 
           }
            //...............On selecting Payee Expense Bar Chart
            else if(newValue.intValue() == 2)
           {
               System.out.println("CHOICE 2 selected");
               if(expenseByPayeeChart == null)
                   expenseByPayeeChart = new ExpenseByPayeeChart(dataModel, status);
                setCenter(expenseByPayeeChart); 
           }
            //...............On selecting Account Balance Line Chart
            else if(newValue.intValue() == 3)
           {
               System.out.println("CHOICE 3 selected");
               if(accountBalanceLineChart == null)
                   accountBalanceLineChart = new AccountBalanceLineChart(dataModel, status);
                setCenter(accountBalanceLineChart); 
           }
            else if(newValue.intValue() == 4)
           {
               System.out.println("CHOICE 4 selected");
               if(netWorthLineChart == null)
                   netWorthLineChart = new NetWorthLineChart(dataModel, status);
                setCenter(netWorthLineChart); 
           }
            
            
        }
        
        });
        
        
    }
    private void generateChartChooser() 
    {
        choices = FXCollections.observableArrayList();
        chartChooser = new ChoiceBox<>();
        choices.add("Expenses Pie Chart");
        choices.add("Income vs Expense Bar Chart");
        choices.add("Expense By Payee Chart");
        choices.add("Account Balance Line Chart");
        choices.add("Net Worth Chart");
   
        
        this.chartChooser.setItems(choices);
    }
}
