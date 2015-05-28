/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ui.panels.reportPanel;

import java.util.Iterator;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.geometry.Insets;
import javafx.print.JobSettings;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author HP
 */
public class PrintDialog extends Application
{
    private Stage printStage;
    private Scene scene;
    private GridPane mainGrid;
    private ObservableList<Printer> printerList = FXCollections.observableArrayList();
    private Button printButton;
    private Button cancelButton;
    private Button pageSetUpButton;
    private final WebEngine reportWebEngine;
    private ChoiceBox<Printer> printersChoiceBox;
    PrinterJob job;
    
    public PrintDialog(WebEngine webEngine)
    {
        this.reportWebEngine = webEngine;
    }

    @Override
    public void start(Stage primaryStage) throws Exception 
    {
        printStage = new Stage();
        printStage.initModality(Modality.APPLICATION_MODAL);
        //printStage.initOwner(primaryStage);
        
        setUpMainGrid();
        wireUpControls();
        
        scene = new Scene(mainGrid, 300, 200);
        printStage.setScene(scene);
        printStage.show();
       
    }

    private void setUpMainGrid() 
    {
       mainGrid = new GridPane();
       mainGrid.setHgap(10);
       mainGrid.setVgap(10);
       mainGrid.setPadding(new Insets(5));
       
       Label selectPrinterLabel = new Label("Select Printer");
       
       ObservableSet<Printer> allPrintersSet = Printer.getAllPrinters();
        Iterator<Printer> iterator = allPrintersSet.iterator();
       while(iterator.hasNext())
       {
           printerList.add(iterator.next());
       }
       
        printersChoiceBox = new ChoiceBox<Printer>(printerList);
        
        printButton = new Button("PRINT");
        cancelButton = new Button("CANCEL");
        pageSetUpButton = new Button("Page Setup");
        
        mainGrid.add(selectPrinterLabel,0,0);
        mainGrid.add(printersChoiceBox, 1, 0);
        
        mainGrid.add(printButton,0,1);
        mainGrid.add(pageSetUpButton,1,1);
        mainGrid.add(cancelButton,2,1);
        
    }

    private void wireUpControls() 
    {
       printButton.setOnAction(event->
       {
           Printer selectedPrinter = printersChoiceBox.getSelectionModel().getSelectedItem();
           if(selectedPrinter == null)
               return;
            job = PrinterJob.createPrinterJob(selectedPrinter);
            PageLayout layout = job.getPrinter().createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.HARDWARE_MINIMUM);
            job.getJobSettings().setPageLayout(layout);
            JobSettings jobSettings = job.getJobSettings();
            
            if (job != null) 
            {
                if(job.showPrintDialog(printStage))
                    reportWebEngine.print(job);
                
                job.endJob();
            }
            printStage.close();
       });
       
       cancelButton.setOnAction(event -> printStage.close());
           
       
    }
    
}
