/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package utilities;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.DataModel;

/**
 *
 * @author HP
 */
public class NewFileWizard extends Application
{
    private Properties properties;
    private OutputStream outStream;
    private GridPane mainGrid;
    private TextField filePathField;
    private Button setLocationButton;
    private Button okButton;
    private Label statusLabel;
    private Stage mainStage;
    
    private DataModel dataModel;
    
    public NewFileWizard()
    {
        properties = new Properties();
        
    }

    @Override
    public void start(Stage primaryStage) throws Exception 
    {
        generateMainGrid();
        
        mainStage = new Stage();
        wireUpControls();
        Scene scene = new Scene(mainGrid, 300, 150, Color.CHOCOLATE);
        mainStage.setScene(scene);
        mainStage.setTitle("New File Wizard");
        mainStage.show();
        
    }

    private void generateMainGrid() 
    {
        mainGrid = new GridPane();
        mainGrid.setHgap(5);
        mainGrid.setVgap(5);
        
        Label topLabel = new Label("New Account Hierarchy");
        topLabel.setFont(Font.font("Helvetica", FontWeight.EXTRA_BOLD, 16));
        ImageView topIcon = new ImageView(this.getClass().getResource("topIcon.png").toExternalForm());
        topIcon.setFitHeight(64);
        topIcon.setFitWidth(64);
        topLabel.setGraphic(topIcon);
        
        mainGrid.add(topLabel, 0, 0, 2, 1);
        
        filePathField = new TextField();
        filePathField.setPrefWidth(200);
        filePathField.setPromptText("Choose file Path");
        setLocationButton = new Button("Set Location");
   
        okButton = new Button("CREATE");
        
        statusLabel = new Label("....");
        
        mainGrid.add(filePathField, 0, 1);
        mainGrid.add(setLocationButton,1,1);
        mainGrid.add(okButton,1,2);
        mainGrid.add(statusLabel, 0, 3, 2, 1);
    }
    
    private void wireUpControls()
    {
        setLocationButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            public void handle(ActionEvent event) 
            {
                try 
                {
                    FileChooser fileChooser = new FileChooser();
                    
                    File showSaveDialog = fileChooser.showSaveDialog(mainStage);
                    showSaveDialog.createNewFile();
                    System.out.println(showSaveDialog);
                }
                catch (IOException ex) 
                {
                    Logger.getLogger(NewFileWizard.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    public static void main(String [] args)
    {
        launch(args);
    }
}
