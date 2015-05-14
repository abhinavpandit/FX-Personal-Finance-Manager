package application;
	
import java.io.File;
import java.sql.SQLException;
import java.util.Date;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import models.DataModel;
import ui.panels.commons.MenuBarPanel;
import ui.panels.commons.StatusPanel;
import ui.panels.contentPanes.ContentPane;
import ui.panels.register.RegisterPanel;
import ui.panels.summary.SummaryPanel;


public class Main extends Application
{
    private VBox root;
    private Scene scene;
    private DataModel dataModel;
    private MenuBar menuBar;
    private ToolBar toolBar;
    private TabPane contentPane;
    private StatusPanel statusPanel;
    private StringProperty status = new SimpleStringProperty("STATUS");
    private Border border;
    private SummaryPanel summaryPanel;
    private RegisterPanel registerPanel;
    private File dataFile;
    
   // Tab summaryTab;
   // Tab registerTab;
	@Override
	public void start(Stage parentStage) throws SQLException
	{
            Stage primaryStage = new Stage();
             root = new VBox();
	     root.setMinHeight(500);
	     root.setMinWidth(1300);
             //root.setBackground(new Background(new BackgroundImage(new Image(this.getClass().getResource("res/binding_dark.png").toExternalForm()), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

	    // initializeDataModel();
	     
	     statusPanel = new StatusPanel(status);
	     menuBar = new MenuBarPanel(primaryStage, dataModel).getMenuBar();
	  
	   //  toolBar = new ButtonPanel(this).getToolBar();
	     contentPane = new ContentPane(dataModel, status).getTabPane();
	      VBox.setVgrow(contentPane, Priority.ALWAYS);
	     //contentPane.getStylesheets().add(this.getClass().getResource("tabPane.css").toExternalForm());
	     root.getChildren().addAll(menuBar,contentPane,statusPanel);
	     scene = new Scene(root,Screen.getPrimary().getVisualBounds().getWidth(),Screen.getPrimary().getVisualBounds().getHeight()-40,Color.BLACK);
             //scene.setFill(Color.BEIGE);
	        
	     
	     primaryStage.setScene(scene);
	     
	    // primaryStage.setMaximized(true);
	     primaryStage.setTitle("FX Finance Application");
            //setUserAgentStylesheet(STYLESHEET_CASPIAN);
	     primaryStage.show();
	     
             primaryStage.setOnCloseRequest(event -> {
            try
            {
                 dataModel.closeConnection();
                 primaryStage.close();
                 System.exit(0);
            }
            catch(Exception e)
            {
                
            }
             
             });
	     
		
	}
	 private void initializeDataModel() throws SQLException
	    {
	        //status.set("Initializing data model at : "+new Date());
	        dataModel = new DataModel(dataFile, status);
	               System.out.println("DataModel initialization completed at : "+new Date());
	       
	    }
        public void setDataModel(DataModel dm)
        {
            this.dataModel = dm;
        }
	public static void main(String[] args) 
	{

		launch(args);
           
	}
}
