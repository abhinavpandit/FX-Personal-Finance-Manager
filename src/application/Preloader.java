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
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Reflection;
import javafx.scene.image.ImageView;
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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.DataModel;
import utilities.NewFileWizard;

/**
 *
 * @author HP
 */
public class Preloader extends Application
{
    private GridPane mainGrid;
    private Stage stage;
    private FileChooser fileChooser;
    private Main mainApplication;
    
    private Label filePathLabel;
    private Button chooseButton;
    private Button launchButton;
    private Button exitButton;
    private Button createNewFileButton;
    PasswordField passwordField;
    private Label statusLabel;
    
    private Properties applicationProperties;
    private File fileToBeOpened;
    
    @Override
    public void init()
    {
        Parameters parameters = getParameters();
        List<String> raw = parameters.getRaw();
        if(raw.size() == 1)
        {
            String filePath = raw.get(0);
            fileToBeOpened = new File(filePath);
        }
        try 
        {
            loadProperties();
            if(applicationProperties.getProperty("preloader","on").equals("off"))
            {
               // launchApplication();
            }
            setUpMainGrid();
            setUpControls();
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(Preloader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception 
    {
        
        
        this.stage = primaryStage;
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        
        
        final Scene scene = new Scene(mainGrid,Color.rgb(0,0, 0, 0));
        //scene.setFill(Color.gray(0.2, 0.5));
        primaryStage.setScene(scene);
        primaryStage.show();
        
                
                
    }

    private void setUpMainGrid() 
    {
        mainGrid = new GridPane();
        mainGrid.setVgap(5);
        mainGrid.setHgap(5);
        mainGrid.setPadding(new Insets(5));
        mainGrid.getStylesheets().add(this.getClass().getResource("preloader.css").toExternalForm());
        ColumnConstraints col1 = new ColumnConstraints(150);
        ColumnConstraints col2 = new ColumnConstraints(150);
        ColumnConstraints col3 = new ColumnConstraints(150);
        mainGrid.getColumnConstraints().addAll(col1,col2,col3);
        
        RowConstraints row1 = new RowConstraints(30, 40, 50, Priority.SOMETIMES, VPos.CENTER, true);
        mainGrid.getRowConstraints().add(row1);
        
        
        Label fxLabel = new Label("FX Personal Finance Manager");
        //fxLabel.setFont(Font.font("Helvetica", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 14));
        //fxLabel.setFill(Color.WHITESMOKE);
        fxLabel.getStyleClass().add("heading_text");
        
        Button closeButton = new Button();
        ImageView closeButtonIcon = new ImageView(this.getClass().getResource("res/close.png").toExternalForm());
        closeButtonIcon.setFitHeight(16);
        closeButtonIcon.setFitWidth(16);
        closeButton.setGraphic(closeButtonIcon);
        closeButton.setOnAction(event -> System.exit(0));
       
        
        filePathLabel = new Label("");
        if(fileToBeOpened != null)
            filePathLabel.setText(fileToBeOpened.getAbsolutePath());
        filePathLabel.setFont(Font.font(10));
        Text openFileText = new Text("OPEN FILE : ");
        openFileText.setFill(Color.WHITE);
        filePathLabel.wrapTextProperty().setValue(Boolean.TRUE);
        filePathLabel.getStyleClass().add("file_path");
        openFileText.getStyleClass().add("file_path");
        
        
        chooseButton = new Button("Browse");
        
        launchButton = new Button("Launch Application");
        launchButton.setMaxWidth(Double.MAX_VALUE);
        createNewFileButton = new Button("Create New File");
        
        exitButton = new Button("EXIT");
        Label passwordLabel = new Label("Password ");
        passwordLabel.setTextFill(Color.WHITE);
        
        passwordField = new PasswordField();
        
        statusLabel = new Label("/");
        statusLabel.setFont(Font.font("Helvetica", FontWeight.BLACK, FontPosture.ITALIC, 12));
        
        mainGrid.add(fxLabel, 0, 0, 3, 1);
        
        Rectangle r = new Rectangle(500, 2);
        r.setStroke(Color.WHITESMOKE);
        mainGrid.add(r,0,1,3,1);
        

        mainGrid.add(openFileText,0,2);
        mainGrid.add(filePathLabel,1,2,2,1);
        
        mainGrid.add(passwordLabel, 0, 3);
        mainGrid.add(passwordField,1,3,2,1);
        
        mainGrid.add(launchButton, 0, 4);
        mainGrid.add(chooseButton,1,4);
        mainGrid.add(createNewFileButton,2,4);
        
       
        
        mainGrid.add(statusLabel,0,5,2,1);
        mainGrid.add(closeButton,2,5);
        GridPane.setHalignment(closeButton, HPos.RIGHT);
        
        
      // mainGrid.setBlendMode(BlendMode.COLOR_BURN);
       mainGrid.setBackground(new Background(new BackgroundFill(Color.rgb(40, 50, 200, 0.9), new CornerRadii(5), Insets.EMPTY)));
        
        //mainGrid.add(textArea,0,3,2,1);
    }
    

    private void setUpControls() 
    {
       
        chooseButton.setOnAction(event -> 
        { 
            System.out.println("choose button event detected");
            fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("FX Finance Files", "*.fxm"));
            fileToBeOpened = fileChooser.showOpenDialog(stage);
            if(fileToBeOpened == null)
                return;
            filePathLabel.setText(fileToBeOpened.getAbsolutePath());
           
        });
        
        launchButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent event) 
            {
                launchApplication();
            }
        });
        
        createNewFileButton.setOnAction((ActionEvent event) -> 
        {
            NewFileWizard wizard = new NewFileWizard();
            try {
                wizard.start(stage);
            } catch (Exception ex) {
                Logger.getLogger(Preloader.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        exitButton.setOnAction(event -> System.exit(0));
    }

    private void loadProperties() throws FileNotFoundException, IOException 
    {
        applicationProperties = new Properties();
        applicationProperties.load(new FileInputStream("conf/app.conf"));
        String lastFilePath = applicationProperties.getProperty("lastfile", null);
        if(lastFilePath!=null)
        {
            fileToBeOpened = new File(lastFilePath);
        }
        
    }
    private void launchApplication()
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
                    DataModel dataModel = new DataModel(dbFile, null,passwordField.getText());
                    statusLabel.setText("Finished Initilizing Data Model");
                    Main mainApplication = new Main();
                    mainApplication.setDataModel(dataModel);
                    statusLabel.setText("Launching Main Application");
                    applicationProperties.setProperty("lastfile", fileToBeOpened.getAbsolutePath());
                    System.out.println("set APP: last file to : "+applicationProperties.getProperty("lastfile", "NULL"));
                    applicationProperties.store(new FileOutputStream("conf/app.conf"), "Updated on "+new java.util.Date());
                    
                    mainApplication.start(null);
                    statusLabel.setText("Finished Launching Main Applicaiton");
                    stage.close();
                            
                    
                } 
                catch (Exception ex) 
                {
                    handleException(ex);
                }
            
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
    
    public static void main(String [] args)
    {
        launch(args);
    }
}
