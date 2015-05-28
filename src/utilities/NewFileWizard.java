/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.DataModel;

/**
 *
 * @author HP
 */
public class NewFileWizard extends Application
{
    private Properties fileProperties;
    private OutputStream outStream;
    private GridPane mainGrid;
    private TextField fileNameField;
    private Label saveLocationLabel;
    private Button setLocationButton;
    private Button okButton;
    private Label statusLabel;
    private Stage mainStage;
    private PasswordField passwordField;
    
    private File saveDirectory = new File(System.getProperty("user.home"));
    private DataModel dataModel;
    
    public NewFileWizard()
    {
       // properties = new Properties();
        
    }

    @Override
    public void start(Stage primaryStage) throws Exception 
    {
        generateMainGrid();
        mainStage = new Stage();
        mainStage.initModality(Modality.WINDOW_MODAL);
        mainStage.initOwner(primaryStage);
        
        
        wireUpControls();
        Scene scene = new Scene(mainGrid);
        mainStage.setScene(scene);
        mainStage.setTitle("New File Wizard");
        mainStage.show();
        
    }

    private void generateMainGrid() 
    {
        mainGrid = new GridPane();
        mainGrid.setHgap(10);
        mainGrid.setVgap(5);
        mainGrid.setPadding(new Insets(5));
        
        ColumnConstraints col1 = new ColumnConstraints(200);
        ColumnConstraints col2 = new ColumnConstraints(200);
        mainGrid.getColumnConstraints().addAll(col1,col2);
        
        Label topLabel = new Label("New Account Hierarchy");
        topLabel.setFont(Font.font("Helvetica", FontWeight.EXTRA_BOLD, 20));
        ImageView topIcon = new ImageView(this.getClass().getResource("topIcon.png").toExternalForm());
        topIcon.setFitHeight(64);
        topIcon.setFitWidth(64);
        topLabel.setGraphic(topIcon);
        topLabel.setMaxWidth(Double.MAX_VALUE);
       // topLabel.setBackground(new Background(new BackgroundFill(Color.CHARTREUSE, CornerRadii.EMPTY, Insets.EMPTY)));
        
        Label fileNameLabel = new Label ("Choose a File Name");
        fileNameLabel.setFont(Font.font("Helvetica", FontWeight.BLACK, FontPosture.ITALIC, 12));
        fileNameField = new TextField();
        
        Label passwordLabel = new Label("Set Password");
        passwordField = new PasswordField();
        
        saveLocationLabel = new Label(saveDirectory.getAbsolutePath());
        saveLocationLabel.setFont(Font.font("Helvetica", FontWeight.BLACK, FontPosture.ITALIC, 12));
        setLocationButton = new Button("Set Location");
   
        okButton = new Button("CREATE");
        
        statusLabel = new Label("");
        statusLabel.setFont(Font.font("Helvetica", FontWeight.BLACK, FontPosture.ITALIC, 12));
        
        mainGrid.add(topLabel, 0, 0, 2, 1);
        
        Rectangle r1 = new Rectangle(400, 2);
        mainGrid.add(r1, 0, 1, 2,1);
        
        mainGrid.add(fileNameLabel , 0, 2);
        mainGrid.add(fileNameField,1,2);
        
        mainGrid.add(passwordLabel,0,3);
        mainGrid.add(passwordField,1,3);
        
        mainGrid.add(saveLocationLabel,0,4);
        mainGrid.add(setLocationButton, 1, 4);
        
        mainGrid.add(okButton,1,5);
        
        mainGrid.add(statusLabel, 0, 6, 2, 1);
    }
    
    private void wireUpControls()
    {
        setLocationButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            public void handle(ActionEvent event) 
            {
    
                    DirectoryChooser dirChooser = new DirectoryChooser();
                    dirChooser.setTitle("Select a Save Location");
                    
                    File defaultDir = new File(System.getProperty("user.home"));
                    dirChooser.setInitialDirectory(defaultDir);
                    File showDialog = dirChooser.showDialog(mainStage);
                    if(showDialog != null)
                    {
                        saveDirectory = showDialog;
                        saveLocationLabel.setText(saveDirectory.getAbsolutePath());
                    }
                    System.out.println("SELECTED > "+showDialog +" ; is Directory : "+showDialog.isDirectory());
                
            }
        });
        
        okButton.setOnAction(new EventHandler<ActionEvent>()  
        {
            @Override
            public void handle(ActionEvent event) 
            {
               boolean isFileNameValid = validateFileName();
               if(!isFileNameValid)
               {
                   statusLabel.setText("SELECT A VALID FILE NAME");
                   return;
               }
               File propertiesFile = new File(saveDirectory.getAbsolutePath() +"\\" +fileNameField.getText()+".fxm");
               statusLabel.setText(propertiesFile.getAbsolutePath());
                try 
                {
                    propertiesFile.createNewFile();
                    setUpPropertiesFile(propertiesFile);
                    setUpDatabaseFiles(propertiesFile);
                    mainStage.close();
                } 
                catch (IOException ex) 
                {
                    Logger.getLogger(NewFileWizard.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(NewFileWizard.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            private void setUpPropertiesFile(File propertiesFile) throws FileNotFoundException, IOException 
            {
                fileProperties = new Properties();
                fileProperties.setProperty("dbname", fileNameField.getText());
                OutputStream fileOutputStream = new FileOutputStream(propertiesFile);
                fileProperties.store(fileOutputStream, "Creating File on "+new java.util.Date());
           }

            private void setUpDatabaseFiles(File propertiesFile) throws SQLException 
            {
              
               BlankDatabaseGenerator dbG = new BlankDatabaseGenerator(propertiesFile, fileNameField.getText(),passwordField.getText().trim());
            }
        });
        
    }
    
    private boolean validateFileName()
    {
        String text = fileNameField.getText();
        if(text.length()<=0)
        {
            //statusLabel.setText(text);
            return false;
        }
        
        if(!Character.isLetter(text.charAt(0)))
            return false;
        
        for(int i=0;i<text.length();i++)
        {
            char ch = text.charAt(i);
            if(Character.isLetterOrDigit(ch))
                continue;
            else
                return false;
        }
        
        return true;
    }
    
    public static void main(String [] args)
    {
        launch(args);
    }
}
