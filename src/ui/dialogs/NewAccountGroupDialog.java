
package ui.dialogs;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.AccountGroup;
import models.DataModel;

/**
 *
 * @author john
 */
public class NewAccountGroupDialog extends Application
{
    private Stage stage;
    private Scene scene;
    private DataModel dataModel;
    private TextField nameField;
    private ChoiceBox<AccountGroup> parentField;
    private Button okButton;
    GridPane mainGrid;
    private BooleanProperty editMode = new SimpleBooleanProperty(false);
    private AccountGroup updatingAccountGroup = null;

    public NewAccountGroupDialog(DataModel dataModel)
    {
        this.dataModel = dataModel;
    }
    
    public NewAccountGroupDialog() throws SQLException 
    {
       // dataModel = new DataModel(new SimpleStringProperty());
    }

    @Override
    public void start(Stage primaryStage) throws Exception 
    {
        stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primaryStage);
        
        mainGrid = new GridPane();
        setUpMainGrid();
        
        scene = new Scene(mainGrid, 380, 180);
        stage.setScene(scene);
        stage.setTitle("New Account Group");
        stage.show();
       
      
        
    }
    public static void main(String [] args)
    {
        launch(args);
    }

    private void setUpMainGrid() 
    {
        ColumnConstraints col1 = new ColumnConstraints(150);
        ColumnConstraints col2 = new ColumnConstraints(200);
        mainGrid.getColumnConstraints().addAll(col1,col2);
        mainGrid.setPadding(new Insets(10, 10, 10, 10));
        mainGrid.setVgap(10);
        mainGrid.setHgap(10);
        
        //mainGrid.setGridLinesVisible(true);
        Label topLabel = new Label("NEW ACCOUNT GROUP");
        topLabel.setFont(Font.font("Helvetica", FontWeight.SEMI_BOLD,FontPosture.REGULAR, 16));
        
        ImageView icon = new ImageView(this.getClass().getResource("res/agroup.png").toExternalForm());
        icon.setFitHeight(48);
        icon.setFitWidth(48);
        topLabel.setGraphic(icon);
        
        Label nameLabel = new Label("ACCOUNT GROUP NAME ");
        
        
        Label parentLabel = new Label("PARENT GROUP");
       
        
        nameField = new TextField();
        nameField.setPromptText("at least 3 characters");
        
        
        parentField = new ChoiceBox<>();
        parentField.setItems(dataModel.getAccountGroupList());
        parentField.getSelectionModel().clearAndSelect(0);
        parentField.setPrefWidth(200);
       
        Rectangle rect = new Rectangle(360, 5);
        
        
        
        okButton = new Button("OK");
        okButton.setMinWidth(80);
        
        
        mainGrid.add(topLabel, 0, 0, 2, 1);
        mainGrid.add(rect, 0, 1, 2, 1);
        mainGrid.add(nameLabel, 0, 2);
        mainGrid.add(parentLabel, 0, 3);
        mainGrid.add(nameField, 1, 2);
        mainGrid.add(parentField, 1, 3);
        mainGrid.add(okButton,1,4);
        
        wireUpControls();
        
    }

    private void wireUpControls() 
    {
        okButton.setOnAction(event -> {  
        if(nameField.getText().length()<3)
            return;
        if(parentField.getSelectionModel().getSelectedItem() == null)
            return;
        if(editMode.getValue() == true)
        {
            try {
                dataModel.updateAccountGroup(updatingAccountGroup);
            } catch (SQLException ex) {
                Logger.getLogger(NewAccountGroupDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        AccountGroup grp = new AccountGroup();
        grp.setAccountName(nameField.getText().trim());
        grp.setParentGroup(parentField.getSelectionModel().getSelectedItem());
        grp.getParentGroup().addChildAccount(grp);
          //  System.out.println("new Group created : "+grp.getAccountName());
           // System.out.println("new Group parent : "+grp.getParentAccount().getAccountName());
          //  System.out.println("children of parent : ");
          //  System.out.println(grp.getParentAccount().getChildAccounts());
        
            try {
                dataModel.addAccountGroup(grp);
            } catch (SQLException ex) {
                Logger.getLogger(NewAccountGroupDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        stage.close();
        });
    }
    
    public void editAccountGroup(AccountGroup group)
    {
        this.okButton.setText("UPDATE");
        editMode.setValue(Boolean.TRUE);
        updatingAccountGroup = group;
        populateGroup(group);
    }
    private void populateGroup(AccountGroup group)
    {
        this.nameField.setText(group.getAccountName());
        this.parentField.getSelectionModel().select(group.getParentGroup());
    }
    
    
}
