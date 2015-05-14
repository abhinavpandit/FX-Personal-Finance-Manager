/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.AuthProvider;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.DataModel;

/**
 *
 * @author HP
 */
public class Preloader extends Application
{
    private GridPane mainGrid;
    private FileChooser fileChooser;
    private Main mainApplication;
    Label filePathLabel;
    private Button chooseButton;
    private Button openButton;
    private Button createNewButton;
    PasswordField passwordField;
    private Label statusLabel;
    private Stage primaryStage;
    private Properties applicationProperties;
    
    private File fileToBeOpened;

    @Override
    public void start(Stage primaryStage) throws Exception 
    {
        loadProperties();
        this.primaryStage = primaryStage;
        setUpMainGrid();
        setUpControls();
        
        Scene scene = new Scene(mainGrid,Color.ALICEBLUE);
        primaryStage.setScene(scene);
        primaryStage.show();
        
                
                
    }

    private void setUpMainGrid() 
    {
        mainGrid = new GridPane();
        mainGrid.setVgap(5);
        mainGrid.setHgap(5);
        mainGrid.setPadding(new Insets(5));
        
        ColumnConstraints col1 = new ColumnConstraints(200);
        ColumnConstraints col2 = new ColumnConstraints(100);
        mainGrid.getColumnConstraints().addAll(col1,col2);
        
        
        
        Text fxLabel = new Text("FX Personal Finance Manager");
        
        String lastFilePath = applicationProperties.getProperty("lastfile", null);
        if(lastFilePath!=null)
        {
            fileToBeOpened = new File(lastFilePath);
        }
        filePathLabel = new Label(lastFilePath);
        
        chooseButton = new Button("Choose");
        
        openButton = new Button("Open File");
        createNewButton = new Button("Create New File");
        
        
        Label passwordLabel = new Label("Password ");
        passwordField = new PasswordField();
        
        statusLabel = new Label("/");
        statusLabel.setFont(Font.font("Helvetica", FontWeight.BLACK, FontPosture.ITALIC, 12));
        
        mainGrid.add(fxLabel, 0, 0, 2, 1);
        
        mainGrid.add(filePathLabel,0,1,2,1);
        
        mainGrid.add(chooseButton,0,2);
        mainGrid.add(createNewButton,1,2);
        
        mainGrid.add(passwordLabel,0,3);
        mainGrid.add(passwordField, 1, 3);
        
        mainGrid.add(openButton, 0, 4, 2, 1);
        
        mainGrid.add(statusLabel,0,5,2,1);
        GridPane.setHalignment(openButton, HPos.CENTER);
        GridPane.setHgrow(openButton, Priority.ALWAYS);
        
       
        
        //mainGrid.add(textArea,0,3,2,1);
    }
    public static void main(String [] args)
    {
        launch(args);
    }

    private void setUpControls() 
    {
       
        chooseButton.setOnAction(event -> 
        { 
            System.out.println("choose button event detected");
            fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("FX Finance Files", "*.fxm"));
            fileToBeOpened = fileChooser.showOpenDialog(primaryStage);
            if(fileToBeOpened == null)
                return;
            filePathLabel.setText(fileToBeOpened.getAbsolutePath());
           
        });
        
        openButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent event) 
            {
               if(fileToBeOpened == null)
                   return;
               Properties propertyFile = new Properties();
                try 
                {
                    propertyFile.load(new FileInputStream(fileToBeOpened));
                    String dbName = propertyFile.getProperty("dbname");
                    System.out.println("dbName recovered as : "+dbName);
                    String dbURL = fileToBeOpened.getParent() +"\\" +dbName;
                    File dbFile = new File(dbURL);
                    System.out.println("DB FIle is : "+dbFile.getAbsolutePath());
                    String dbFileURL = "jdbc:h2:" +fileToBeOpened.getParent() +"\\" +dbName +";IFEXISTS=TRUE";
                    System.out.println("DB FIle URL is : "+dbFile.getAbsolutePath());
                    
                    statusLabel.setText("Initilizing Data Model");
                    DataModel dataModel = new DataModel(dbFile, null);
                    statusLabel.setText("Finished Initilizing Data Model");
                    Main mainApplication = new Main();
                    mainApplication.setDataModel(dataModel);
                    statusLabel.setText("Launching Main Application");
                    applicationProperties.setProperty("lastfile", fileToBeOpened.getAbsolutePath());
                    System.out.println("set APP: last file to : "+applicationProperties.getProperty("lastfile", "NULL"));
                    
                    
                    mainApplication.start(null);
                    statusLabel.setText("Finished Launching Main Applicaiton");
                    
                            
                    
                } 
                catch (Exception ex) 
                {
                    handleException(ex);
                }
            }
        });
    }

    private void loadProperties() throws FileNotFoundException, IOException 
    {
        applicationProperties = new Properties();
        InputStream in = this.getClass().getResourceAsStream("app.conf");
        applicationProperties.load(in);
        in.close();
    }
    
    private void handleException(Exception ex)
    {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Exception Dialog");
        alert.setHeaderText("Exception Encountered");
        //alert.setContentText("Could not find file blabla.txt!");

        // Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }
    
}
