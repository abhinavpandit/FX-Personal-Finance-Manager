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
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
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
import reports.AccountStatementTextReport;
import reports.BalanceSheetReportHTML;
import reports.IncomeVsExpenseReportDetails;
import reports.IncomeVsExpenseReportTotals;

/**
 *
 * @author HP
 */
public class ReportPanelWebView extends GridPane
{
    private final DataModel dataModel;
    private WebView webView;
    private WebEngine webEngine;
    private VBox sidePanel;
    
    public ReportPanelWebView(DataModel dataModel)
    {
        super();
        this.dataModel = dataModel;
       
        setUpWebView();
        setUpSidePanel();
        
        ColumnConstraints col1 = new ColumnConstraints(500, 500, Double.MAX_VALUE, Priority.ALWAYS, HPos.LEFT, true);
        ColumnConstraints col2 = new ColumnConstraints(200, 200, 300, Priority.NEVER, HPos.LEFT, true);
        RowConstraints row1 = new RowConstraints(600, 600, Double.MAX_VALUE, Priority.ALWAYS, VPos.TOP, true);
        this.getColumnConstraints().addAll(col1,col2);
        this.getRowConstraints().addAll(row1);
        this.setGridLinesVisible(true);
        
        this.add(webView, 0, 0);
        this.add(sidePanel,1,0);
    }

    private void setUpWebView() 
    {
       webView = new WebView();
       webEngine = webView.getEngine();
       webView.setMinWidth(500);
       webView.setMinHeight(500);
       webEngine.loadContent("<b> testing webview </b>");
       
    }

    private void setUpSidePanel() 
    {
        sidePanel = new VBox(10);
        sidePanel.setMinWidth(200);
        sidePanel.setStyle("-fx-background-color: gray");
        
        Button b = new Button("Google");
        sidePanel.getChildren().add(b);
                
        b.setOnAction(event -> webEngine.load("http://www.google.com"));
        
        Button check = new Button("Check");
        sidePanel.getChildren().add(check);
        check.setOnAction(event -> 
        {
            System.out.println("" +webEngine.getDocument().getDocumentURI());
        }
        );
         Button checkFile = new Button("Check File");
        sidePanel.getChildren().add(checkFile);
        checkFile.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                try
                {
                    
                    BalanceSheetReportHTML report = new BalanceSheetReportHTML(dataModel);
                    File reportFile = report.getReportFile();
                    System.out.println("loading : "+reportFile.getAbsolutePath());
                    //webEngine.load(reportFile.toURI().toURL().toString());
                    String path = System.getProperty("user.dir");
                    System.out.println("path : "+path);
                    path.replace("\\\\", "/");  
                    System.out.println("after replace : "+path);
                     path +=  "\\tempreport.html";  
                     System.out.println("after adding : "+path);
                   // webEngine.load("file:///" + path);
                    
                    webEngine.load("file:///" +reportFile.getAbsolutePath().replace("\\\\", "/"));
                }
                catch (IOException ex) 
                {
                    Logger.getLogger(ReportPanelWebView.class.getName()).log(Level.SEVERE, null, ex);
                }
                
             
            }
        });
        
        
    }
    
    
    
}
