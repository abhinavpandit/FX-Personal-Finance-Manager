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
import javax.print.PrintException;
import models.Account;
import models.DataModel;
import reports.AccountStatementTextReport;
import reports.IncomeVsExpenseReportDetails;
import reports.IncomeVsExpenseReportTotals;

/**
 *
 * @author HP
 */
public class ReportPanel extends GridPane
{
    private DataModel dataModel;
    private ChoiceBox<Account> accountChoiceBox; 
    private Button generateButton;
    private Button generateTextReportButton;
    private Button printReportButton;
    private TextArea reportArea;
    private VBox sidePanel;
    private ChoiceBox<String> reportTypes;
    private ObservableList<String> reportLabels;
    private DatePicker fromDate = new DatePicker();
    private DatePicker toDate = new DatePicker();
    private VBox reportOptions;
    private File visibleFile;
    
    

    public ReportPanel(DataModel dataModel)
    {
        super();
        this.dataModel = dataModel;
        accountChoiceBox = new ChoiceBox<>(dataModel.getAccountList());
        generateGrid();
        setUpControls();
    }

    private void generateGrid() 
    {
        
        ColumnConstraints col1 = new ColumnConstraints(600, 600, Double.MAX_VALUE, Priority.ALWAYS, HPos.LEFT, true);
        ColumnConstraints col2 = new ColumnConstraints(200, 300, 300, Priority.NEVER, HPos.CENTER, true);
        RowConstraints row1 = new RowConstraints(600, 800, Double.MAX_VALUE, Priority.ALWAYS, VPos.CENTER, true);
        this.getColumnConstraints().addAll(col1);
        this.getRowConstraints().add(row1);
        setUpSidePanel();
        
        
        
        reportArea = new TextArea();
        reportArea.setPrefWidth(60);
        reportArea.setPrefHeight(600);
       
        reportArea.setStyle("-fx-font-family:monospace;-fx-font-weight: bold;");
        InputStream is = this.getClass().getResourceAsStream("unispacebd.ttf");
        Font loadFont = Font.loadFont(is, USE_PREF_SIZE);
        reportArea.setFont(loadFont);
        System.out.println("ReportArea : FONT > " +reportArea.getFont());
        
        this.add(reportArea, 0, 0);
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
      l1.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.REGULAR, 12));
      sidePanel.getChildren().add(l1);
      sidePanel.getChildren().add(reportTypes);
      sidePanel.setBackground(new Background(new BackgroundFill(Color.BLANCHEDALMOND, CornerRadii.EMPTY, Insets.EMPTY)));
      
      reportOptions = new VBox(10);
      reportOptions.setMinHeight(500);
      sidePanel.getChildren().add(reportOptions);
      VBox.setVgrow(reportOptions, Priority.ALWAYS);
      
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
                    if(accountChoiceBox.getSelectionModel().getSelectedItem() == null)
                        return;
                    AccountStatementTextReport txtReport = new AccountStatementTextReport(dataModel);
                    File generatedReport = txtReport.generateReport(accountChoiceBox.getSelectionModel().getSelectedItem());
                    visibleFile  = generatedReport;
                    FileReader fReader = new FileReader(generatedReport);
                    BufferedReader buffReader = new BufferedReader(fReader);
                    reportArea.clear();
                    String line = buffReader.readLine();
                    while(line!=null)
                    {
                        reportArea.appendText(line);
                        reportArea.appendText("\n");
                        line = buffReader.readLine();
                    }
                }
                else if(reportTypes.getSelectionModel().getSelectedIndex() == 1)
                {
                    if(fromDate.getValue() == null || toDate.getValue() == null)
                        return;
                    if(fromDate.getValue().compareTo(toDate.getValue())>0)
                        return;
                    IncomeVsExpenseReportTotals report = new IncomeVsExpenseReportTotals(dataModel);
                    report.setDates(fromDate.getValue(), toDate.getValue());
                    File generatedReport = report.generateReport();
                    visibleFile = generatedReport;
                    FileReader fReader = new FileReader(generatedReport);
                    BufferedReader buffReader = new BufferedReader(fReader);

                    String line = buffReader.readLine();
                    reportArea.clear();
                    while(line!=null)
                    {
                        reportArea.appendText(line);
                        reportArea.appendText("\n");
                        line = buffReader.readLine();
                    }
                }
                else if(reportTypes.getSelectionModel().getSelectedIndex() == 2)
                {
                    if(fromDate.getValue() == null || toDate.getValue() == null)
                        return;
                    if(fromDate.getValue().compareTo(toDate.getValue())>0)
                        return;
                    IncomeVsExpenseReportDetails report = new IncomeVsExpenseReportDetails(dataModel);
                    report.setDates(fromDate.getValue(), toDate.getValue());
                    File generatedReport = report.generateReport();
                    visibleFile = generatedReport;
                    FileReader fReader = new FileReader(generatedReport);
                    BufferedReader buffReader = new BufferedReader(fReader);

                    String line = buffReader.readLine();
                    reportArea.clear();
                    while(line!=null)
                    {
                        reportArea.appendText(line);
                        reportArea.appendText("\n");
                        line = buffReader.readLine();
                    }
                }
                //reportArea.setEditable(false);
            } catch (IOException ex) {
                Logger.getLogger(ReportPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
           
        
        
        });
        
        reportTypes.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>(){ 
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) 
            {
                if(newValue.intValue() == 0)
                {
                    reportOptions.getChildren().clear();
                    Label l = new Label("Choose Account");
                    reportOptions.getChildren().add(l);
                    reportOptions.getChildren().add(accountChoiceBox);
                    Label l1 = new Label("From Date");
                    reportOptions.getChildren().add(l1);
                    reportOptions.getChildren().add(fromDate);
                    Label l2 = new Label("To Date");
                    reportOptions.getChildren().add(l2);
                    reportOptions.getChildren().add(toDate);
                    
                }
                else if(newValue.intValue() == 1 || newValue.intValue() == 2)
                {
                   reportOptions.getChildren().clear();
                    Label l1 = new Label("From Date");
                    reportOptions.getChildren().add(l1);
                    reportOptions.getChildren().add(fromDate);
                    Label l2 = new Label("To Date");
                    reportOptions.getChildren().add(l2);
                    reportOptions.getChildren().add(toDate);
                }
            }
        
        
        
        });
        
        printReportButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent event) 
            {
                
                try 
                {
                    if(visibleFile != null)
                        new utilities.TextFilePrinter(visibleFile);
                } catch (PrintException ex) {
                    Logger.getLogger(ReportPanel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(ReportPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
    }

    private void setUpReportTypes() 
    {
        reportLabels = FXCollections.observableArrayList();
        reportLabels.add("Account Statement");
        reportLabels.add("Income VS Expense Report-Summary");
        reportLabels.add("Income VS Expense Report-Details");
        
        reportTypes = new ChoiceBox<>(reportLabels);
    }
    
    
    
}
