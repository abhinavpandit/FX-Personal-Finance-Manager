/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ui.panels.reportPanel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.print.JobSettings;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javax.print.PrintException;
import models.Account;
import models.DataModel;
import reports.AccountStatementHTMLReport;

import reports.IncomeVsExpenseDetailsHTMLReport;

import reports.IncomeVsExpenseSummaryHTMLReport;
import reports.MonthlyExpensesReportHTML;

/**
 *
 * @author HP
 */
public class ReportPanelWebTest extends GridPane
{
    private DataModel dataModel;
    private Button generateButton;
    private Button generateTextReportButton;
    private Button printReportButton;
    private WebView reportWebView;
    private WebEngine reportEngine;
    private VBox sidePanel;
    private ChoiceBox<String> reportTypes;
    private ObservableList<String> reportLabels;
    private DatePicker fromDate = new DatePicker();
    private DatePicker toDate = new DatePicker();
    private VBox reportOptionsPanel;
    private File visibleFile;
    
    
    //reports...........................................
    AccountStatementHTMLReport accountStatementReport;
    IncomeVsExpenseDetailsHTMLReport incomeVsExpenseDetailedReport;
    IncomeVsExpenseSummaryHTMLReport incomeVsExpenseSummaryReport;
    MonthlyExpensesReportHTML monthlyExpensesReport;
    //..................................................
    

    public ReportPanelWebTest(DataModel dataModel) throws IOException
    {
        super();
        this.dataModel = dataModel;
        reportWebView = new WebView();
        reportEngine = reportWebView.getEngine();
       
        setUpReportObjects();
        generateGrid();
        setUpControls();
    }

    private void generateGrid() 
    {
        
        ColumnConstraints col1 = new ColumnConstraints(600, 600, Double.MAX_VALUE, Priority.ALWAYS, HPos.LEFT, true);
        ColumnConstraints col2 = new ColumnConstraints(200, 300, 300, Priority.NEVER, HPos.CENTER, true);
        RowConstraints row1 = new RowConstraints(400, 600, Double.MAX_VALUE, Priority.ALWAYS, VPos.CENTER, true);
        this.getColumnConstraints().addAll(col1);
        this.getRowConstraints().add(row1);
        this.setPadding(new Insets(5));
        this.setHgap(5);
        setUpSidePanel();
        
        
        
        //reportArea = new TextArea();
        reportWebView.setPrefWidth(60);
        reportWebView.setPrefHeight(600);
       
        //reportArea.setStyle("-fx-font-family:monospace;-fx-font-weight: bold;");
        InputStream is = this.getClass().getResourceAsStream("unispacebd.ttf");
        Font loadFont = Font.loadFont(is, USE_PREF_SIZE);
      //  reportArea.setFont(loadFont);
       // System.out.println("ReportArea : FONT > " +reportArea.getFont());
        
        this.add(reportWebView, 0, 0);
        this.add(sidePanel,1,0);
    }
    
    private void setUpSidePanel()
    {
      sidePanel = new VBox(10);
      sidePanel.setPadding(new Insets(10));
      sidePanel.setMinWidth(150);
      sidePanel.setMaxWidth(300);
      setUpReportTypes();
      Label l1 = new Label("REPORT TYPE");
      l1.setPadding(new Insets(5));
      l1.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.REGULAR, 12));
      sidePanel.getChildren().add(l1);
      sidePanel.getChildren().add(reportTypes);
      sidePanel.setBackground(new Background(new BackgroundFill(Color.BISQUE, CornerRadii.EMPTY, Insets.EMPTY)));
      
      reportOptionsPanel = new VBox(10);
      reportOptionsPanel.setMinHeight(400);
      sidePanel.getChildren().add(reportOptionsPanel);
      VBox.setVgrow(reportOptionsPanel, Priority.ALWAYS);
      
      printReportButton = new Button("Print Report");
      sidePanel.getChildren().add(printReportButton);
      generateTextReportButton = new Button("Generate Text Report");
      sidePanel.getChildren().add(generateTextReportButton);
      
      
      
      
    }
    
    private void setUpControls()
    {        
        generateTextReportButton.setOnAction(event -> {    
            try 
            {
                if(reportTypes.getSelectionModel().getSelectedIndex() == 0)
                {
                    File generatedReport = accountStatementReport.generateReport();
                    visibleFile  = generatedReport;
                    loadReport(generatedReport);
                }
                else if(reportTypes.getSelectionModel().getSelectedIndex() == 1)
                {
                    File generatedReport = incomeVsExpenseSummaryReport.generateReport();
                    visibleFile = generatedReport;
                    FileReader fReader = new FileReader(generatedReport);
                    loadReport(generatedReport);
                }
                else if(reportTypes.getSelectionModel().getSelectedIndex() == 2)
                {
                    File generatedReport = incomeVsExpenseDetailedReport.generateReport();
                    visibleFile = generatedReport;
                    loadReport(generatedReport);
                }
                else if(reportTypes.getSelectionModel().getSelectedIndex() == 3)
                {
                    File generatedReport = monthlyExpensesReport.generateReport();
                    visibleFile = generatedReport;
                    loadReport(generatedReport);
                }
                //reportArea.setEditable(false);
            } catch (IOException ex) {
                Logger.getLogger(ReportPanelWebTest.class.getName()).log(Level.SEVERE, null, ex);
            }
           
        
        
        });
        
        reportTypes.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>(){ 
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) 
            {
                if(newValue.intValue() == 0)
                {
                    reportOptionsPanel.getChildren().clear();
                    reportOptionsPanel.getChildren().add(accountStatementReport.getOptionsPanel());
                }
                else if(newValue.intValue() == 1)
                {
                   reportOptionsPanel.getChildren().clear();
                   reportOptionsPanel.getChildren().add(incomeVsExpenseSummaryReport.getOptionsPanel());
                }
                else if(newValue.intValue() == 2)
                {
                   reportOptionsPanel.getChildren().clear();
                   reportOptionsPanel.getChildren().add(incomeVsExpenseDetailedReport.getOptionsPanel());
                }
                else if(newValue.intValue() == 3)
                {
                   reportOptionsPanel.getChildren().clear();
                   reportOptionsPanel.getChildren().add(monthlyExpensesReport.getOptionsPanel());
                }
            }
        
        
        
        });
        
       
        printReportButton.setOnAction(event -> { 
        PrintDialog dialog = new PrintDialog(reportEngine);
            try {
                System.out.println("Launching print dialog");
                dialog.start(null);
            } catch (Exception ex) {
                Logger.getLogger(ReportPanelWebTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        
        });
        
        
    }
    private void loadReport(File file)
    {
        if(file!=null)
            reportEngine.load("file:///" +file.getAbsolutePath().replace("\\\\", "/"));
    }
    private void setUpReportTypes() 
    {
        reportLabels = FXCollections.observableArrayList();
        reportLabels.add("Account Statement");
        reportLabels.add("Income VS Expense Report-Summary");
        reportLabels.add("Income VS Expense Report-Details");
        reportLabels.add("Monthly Expense Report");
        
        reportTypes = new ChoiceBox<>(reportLabels);
    }
    private void setUpReportObjects() throws IOException
    {
        accountStatementReport =  new AccountStatementHTMLReport(dataModel);
        incomeVsExpenseDetailedReport = new IncomeVsExpenseDetailsHTMLReport(dataModel);
        incomeVsExpenseSummaryReport = new IncomeVsExpenseSummaryHTMLReport(dataModel);
        monthlyExpensesReport = new MonthlyExpensesReportHTML(dataModel);
    }
    
    
    
}
