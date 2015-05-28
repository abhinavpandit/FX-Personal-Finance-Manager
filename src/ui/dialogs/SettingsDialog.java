/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ui.dialogs;

import com.sun.javafx.scene.control.skin.DatePickerContent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.DataModel;

/**
 *
 * @author HP
 */
public class SettingsDialog extends Application
{
    private Stage primaryStage;
    private VBox rootPane;
    private HBox bottomPanel;
    private TabPane tabPane;
    private Properties applicationProperties;
    private DataModel dataModel;
    public SettingsDialog(DataModel dataModel)
    {
        super();
        this.dataModel = dataModel;
        applicationProperties = dataModel.getApplicationProperties();
        
    }
    @Override
    public void start(Stage parentStage) throws Exception 
    {
        init();
        setUpRootPane();
        primaryStage = new Stage();
        primaryStage.initModality(Modality.WINDOW_MODAL);
        primaryStage.initOwner(parentStage);
        if(rootPane == null)
            System.out.println("ROOT PANE IS NULL >>>>>>>>>>>>>>>>>>>");
        Scene scene = new Scene(rootPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Preferences");
        primaryStage.show();
        
    }
    private void setUpRootPane()
    {
        rootPane = new VBox(10);
        rootPane.setPadding(new Insets(5));
        setUpSettingsTabs();
        rootPane.getChildren().add(tabPane);
        
        bottomPanel = new HBox(10);
        bottomPanel.setAlignment(Pos.CENTER_RIGHT);
        Button saveButton = new Button("SAVE");
        Button cancelButton = new Button("CANCEL");
        bottomPanel.getChildren().addAll(saveButton,cancelButton);
        
        rootPane.getChildren().add(bottomPanel);
        
        saveButton.setOnAction(event -> { 
            System.out.println("SettingsDialog :Save Button press detected");
            try 
            {
                if(applicationProperties == null)
                      System.out.println("APPLICATION PROPERTIES IS NULL");  
                applicationProperties.store(new FileOutputStream("conf/app.conf"), "Updated on "+new java.util.Date());
                System.out.println(applicationProperties);
                primaryStage.close();
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(SettingsDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
                 
    }
    private void setUpSettingsTabs()
    {
        tabPane = new TabPane();
        tabPane.setPrefWidth(400);
        tabPane.setPrefHeight(400);
        
        Tab fileTab = new Tab("File");
        fileTab.setContent(setUpFileSettingsTab());
        
        Tab generalSettingsTab = new Tab("GENERAL");
        generalSettingsTab.setContent(setUpGeneralSettingsTab());
        
        tabPane.getTabs().addAll(fileTab,generalSettingsTab);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
    }
    
    private Pane setUpFileSettingsTab()
    {
      VBox rootPane = new VBox(5);
      rootPane.setPadding(new Insets(8));
      
        CheckBox preloaderCheckBox = new CheckBox("Automatically Launch Last Opened File");
        preloaderCheckBox.setOnAction(event -> {
            System.out.println("SettingsDialog : ActionEvent on checkbox detected");
        if(preloaderCheckBox.isSelected())
        {
            applicationProperties.setProperty("preloader", "on");
        }
        else if(!preloaderCheckBox.isSelected())
        {
            applicationProperties.setProperty("preloader", "off");
        }
            System.out.println(applicationProperties);
        });
        rootPane.getChildren().add(preloaderCheckBox);
        
        return rootPane;
    }
    private Pane setUpGeneralSettingsTab()
    {
        GridPane generalSettingsRootPane = new GridPane();
        generalSettingsRootPane.setVgap(10);
        generalSettingsRootPane.setHgap(10);
        generalSettingsRootPane.setPadding(new Insets(5));
        
        Text expenseStatisticsText = new Text("Show Quick Expense Statistics for ");
        ChoiceBox<String> expenseStatisticsChoiceBox = new ChoiceBox<>();
        ObservableList<String> expenseStatisticsChoices = FXCollections.observableArrayList("One Month","Two Months","Three Months","Six Months","One Year","All Time");
        expenseStatisticsChoiceBox.setItems(expenseStatisticsChoices);
        expenseStatisticsChoiceBox.getSelectionModel().select(Integer.parseInt(applicationProperties.getProperty("quick_expense_stat_interval")));
        generalSettingsRootPane.add(expenseStatisticsText, 0, 0);
        generalSettingsRootPane.add(expenseStatisticsChoiceBox,1,0);
        
        expenseStatisticsChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() 
        {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) 
            {
                applicationProperties.setProperty("quick_expense_stat_interval", newValue.toString());
            }
        });
        
        
        return generalSettingsRootPane;
    }
    public static void main(String [] args)
    {
        launch(args);
    }
}
