/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package application;

import java.io.File;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
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
    private Button chooseButton;
    private Button openButton;
    private Button createNewButton;
    private TextArea textArea;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception 
    {
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
        // Reflection effect
                Reflection r = new Reflection();
                r.setFraction(0.7f);
                fxLabel.setFill(Color.RED);
                fxLabel.setFont(Font.font(null, FontWeight.BOLD, 20));
                fxLabel.setEffect(r);
                
        Label filePathLabel = new Label("/");
        
        chooseButton = new Button("Choose");
        
        openButton = new Button("Open File");
        createNewButton = new Button("Create New File");
        
        textArea = new TextArea();
        textArea.setMaxHeight(100);
        
        mainGrid.add(fxLabel, 0, 0, 2, 1);
        
        mainGrid.add(filePathLabel,0,1);
        mainGrid.add(chooseButton,1,1);
        
        mainGrid.add(openButton, 0, 2);
        mainGrid.add(createNewButton,1,2);
        
        mainGrid.add(textArea,0,3,2,1);
    }
    public static void main(String [] args)
    {
        launch(args);
    }

    private void setUpControls() 
    {
       
        chooseButton.setOnAction(event -> { 
            System.out.println("choose button event detected");
        fileChooser = new FileChooser();
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Database Files", "db"));
            File file = fileChooser.showOpenDialog(primaryStage);
            textArea.appendText("File Opened : \n");
            textArea.appendText("" +file.getAbsolutePath());
        
        
        });
    }
    
}
