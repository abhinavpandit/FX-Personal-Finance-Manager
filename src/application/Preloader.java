/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
    private TextArea textArea;
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
        
        textArea = new TextArea();
        textArea.setMaxHeight(100);
        
        Label passwordLabel = new Label("Password ");
        passwordField = new PasswordField();
        mainGrid.add(fxLabel, 0, 0, 2, 1);
        
        mainGrid.add(filePathLabel,0,1,2,1);
        
        mainGrid.add(chooseButton,0,2);
        mainGrid.add(createNewButton,1,2);
        
        mainGrid.add(passwordLabel,0,3);
        mainGrid.add(passwordField, 1, 3);
        
        mainGrid.add(openButton, 0, 4, 2, 1);
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
            filePathLabel.setText(fileToBeOpened.getAbsolutePath());
           
        });
        
        openButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent event) 
            {
               if(fileToBeOpened == null)
                   return;
               Properties dataFile = new Properties();
                try 
                {
                    dataFile.load(new FileInputStream(fileToBeOpened));
                    String dbName = dataFile.getProperty("dbname")
                } 
                catch (IOException ex) 
                {
                    Logger.getLogger(Preloader.class.getName()).log(Level.SEVERE, null, ex);
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
    
}
