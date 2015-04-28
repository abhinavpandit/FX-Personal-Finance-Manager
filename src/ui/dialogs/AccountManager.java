/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.dialogs;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

import javafx.scene.control.Button;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;

import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.CurrencyStringConverter;
import models.ACCOUNT_TYPE;
import models.AbstractAccount;
import models.Account;
import models.AccountGroup;
import models.DataModel;

/**
 *
 * @author john
 */
public class AccountManager extends Application
{
    Stage primaryStage;
    Scene scene;
    DataModel dataModel;
    GridPane mainGrid;
    TableView<AccountGroup> accountGroupTable;
    TableView<AbstractAccount> accountTable;
    Button newGroupButton;
    Button deleteGroupButton;
    Button modifyGroupButton;
    Button newAccountButton;
    Button deleteAccountButton;
    Button modifyAccountButton;
    private Label statusLabel;
   
    public AccountManager(DataModel dataModel) 
    {
        this.dataModel = dataModel;       
    }
    public AccountManager() throws SQLException
    {
        dataModel = new DataModel(new SimpleStringProperty());
    }

    @Override
    public void start(Stage mainStage) throws Exception 
    {
       primaryStage = new Stage();
       primaryStage.initModality(Modality.WINDOW_MODAL);
       primaryStage.initOwner(mainStage);
       
       setUpMainGrid();
       
        //setUserAgentStylesheet(STYLESHEET_CASPIAN);
       scene = new Scene(mainGrid,600,420);
       primaryStage.setResizable(false);
       primaryStage.setScene(scene);
       primaryStage.setTitle("Account Manager");
       primaryStage.show();
               
    }
    
    private void setUpMainGrid()
    {
        RowConstraints row1 = new RowConstraints(40);
        RowConstraints row2 = new RowConstraints(3);
        RowConstraints row3 = new RowConstraints(300, 400, 700, Priority.ALWAYS, VPos.TOP, true);
        RowConstraints row4 = new RowConstraints(50);
        
        ColumnConstraints col1 = new ColumnConstraints(300);
        ColumnConstraints col2 = new ColumnConstraints(300);
        mainGrid = new GridPane();
        mainGrid.setVgap(10);
        mainGrid.setHgap(10);
      //  mainGrid.setBackground(new Background(new BackgroundImage(new Image(this.getClass().getResource("res/binding_dark.png").toExternalForm()), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        mainGrid.getRowConstraints().addAll(row1,row2,row3,row4);
        mainGrid.getColumnConstraints().addAll(col1,col2);
        mainGrid.getStylesheets().add(this.getClass().getResource("AccountManager.css").toExternalForm());
        Label accountGroupLabel = new Label("ACCOUNT GROUPS");
        accountGroupLabel.setFont(Font.font("Helvetica", FontWeight.BLACK, 12));
        ImageView accountGroupIcon = new ImageView(this.getClass().getResource("res/agroup.png").toExternalForm());
        accountGroupIcon.setFitHeight(32);
        accountGroupIcon.setFitWidth(32);
        accountGroupLabel.setGraphic(accountGroupIcon);
        
        Label accountLabel = new Label("ACCOUNTS");
        accountLabel.setFont(Font.font("Helvetica", FontWeight.BLACK, 12));
        ImageView accountIcon = new ImageView(this.getClass().getResource("res/money.png").toExternalForm());
        accountIcon.setFitHeight(48);
        accountIcon.setFitWidth(48);
        accountLabel.setGraphic(accountIcon);
        
        
        setUpAccountGroupTable();
        
        
        setUpAccountTable();
        
        statusLabel = new Label();
        statusLabel.setFont(Font.font("Helvetica", FontWeight.EXTRA_BOLD, 11));
        
        
        mainGrid.add(accountGroupLabel,0,0);
        mainGrid.add(accountLabel,1,0);
        Rectangle r1 = new Rectangle(610, 3);
        mainGrid.add(r1, 0, 1, 2, 1);
        
        mainGrid.add(accountGroupTable,0,2);
        mainGrid.add(accountTable, 1, 2);
        
        mainGrid.add(statusLabel, 0, 4, 2, 1);
        
        GridPane.setHalignment(accountGroupLabel, HPos.CENTER);
        GridPane.setHalignment(accountLabel, HPos.CENTER);
        setUpButtons();
        setUpControls();
    }

    private void setUpAccountGroupTable() 
    {
       accountGroupTable = new TableView<>();
        TableColumn<AccountGroup,String> groupNameColumn = new TableColumn<>("Account Group Name");
        groupNameColumn.setMinWidth(100);
        groupNameColumn.setCellValueFactory(new PropertyValueFactory<>("accountName"));
        
        TableColumn<AccountGroup,String> groupBalanceColumn = new TableColumn<>("Balance");
        groupBalanceColumn.setMinWidth(60);
        //groupBalanceColumn.setCellValueFactory(new PropertyValueFactory<>("accountBalance"));
        groupBalanceColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<AccountGroup, String>, ObservableValue<String>>() {
           @Override
           public ObservableValue<String> call(TableColumn.CellDataFeatures<AccountGroup, String> param)
           {
              DoubleProperty balanceProperty = param.getValue().accountBalanceProperty();
              StringProperty balanceStringProperty = new SimpleStringProperty();
              CurrencyStringConverter converter = new CurrencyStringConverter(NumberFormat.getCurrencyInstance(new Locale("en","IN"))); 
              Bindings.bindBidirectional(balanceStringProperty, balanceProperty, converter);
           
            return balanceStringProperty;
           }
       });
       
        accountGroupTable.getColumns().addAll(groupNameColumn,groupBalanceColumn);
        groupBalanceColumn.setStyle("-fx-alignment: BASELINE_RIGHT ");
        accountGroupTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        accountGroupTable.setItems(dataModel.getAccountGroupList());
    }

    private void setUpAccountTable() 
    {
        accountTable = new TableView<>();
        TableColumn<AbstractAccount,String> accountNameColumn = new TableColumn<>("Account Name");
        accountNameColumn.setMinWidth(100);
        accountNameColumn.setCellValueFactory(new PropertyValueFactory<>("accountName"));
        
        TableColumn<AbstractAccount,String> accountBalanceColumn = new TableColumn<>("Balance");
        accountBalanceColumn.setMinWidth(60);
       // accountBalanceColumn.setCellValueFactory(new PropertyValueFactory<>("accountBalance"));
        accountBalanceColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<AbstractAccount, String>, ObservableValue<String>>() {
           @Override
           public ObservableValue<String> call(TableColumn.CellDataFeatures<AbstractAccount, String> param)
           {
              DoubleProperty balanceProperty = param.getValue().accountBalanceProperty();
              StringProperty balanceStringProperty = new SimpleStringProperty();
              CurrencyStringConverter converter = new CurrencyStringConverter(NumberFormat.getCurrencyInstance(new Locale("en","IN"))); 
              Bindings.bindBidirectional(balanceStringProperty, balanceProperty, converter);
           
            return balanceStringProperty;
           }
       });
       
        accountTable.getColumns().addAll(accountNameColumn,accountBalanceColumn);
        accountBalanceColumn.setStyle("-fx-alignment: BASELINE_RIGHT ");
        accountTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        //accountTable.setItems(FXCollections.observableArrayList(dataModel.getAccountMap().values()));
        accountTable.setRowFactory(new Callback<TableView<AbstractAccount>, TableRow<AbstractAccount>>() {

            @Override
            public TableRow<AbstractAccount> call(TableView<AbstractAccount> param) {
               return new TableRow<AbstractAccount> () {
               @Override
               protected void updateItem(AbstractAccount item, boolean empty)
               {
                   super.updateItem(item, empty);
                   if(item != null)
                   {
                       if(item instanceof AccountGroup)
                       {
                           this.setStyle("-fx-font-weight: bold;");
                       }
                       else if(item instanceof Account)
                       {
                           this.setStyle("-fx-font-weight : normal");
                       }
                   }
               }
               
               
               };
            }
        });
        
    }

    private void setUpButtons() 
    {
        HBox buttonPanel1 = new HBox(30);
        buttonPanel1.setPadding(new Insets(10));
        newGroupButton = new Button("NEW");
        deleteGroupButton = new Button("DELETE");
        modifyGroupButton = new Button("MODIFY");
        buttonPanel1.getChildren().addAll(newGroupButton,deleteGroupButton,modifyGroupButton);
        
        mainGrid.add(buttonPanel1,0,3);
        GridPane.setHalignment(buttonPanel1, HPos.CENTER);
        
        HBox buttonPanel2 = new HBox(30);
        buttonPanel2.setPadding(new Insets(10));
        newAccountButton = new Button("NEW");
        deleteAccountButton = new Button("DELETE");
        modifyAccountButton = new Button("MODIFY");
        buttonPanel2.getChildren().addAll(newAccountButton,deleteAccountButton,modifyAccountButton);
        
        mainGrid.add(buttonPanel2,1,3);
        GridPane.setHalignment(buttonPanel2, HPos.CENTER);
   
    }
    private void setUpControls()
    {
        // set up selection modes on tables
        accountGroupTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        accountTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        /*
            on selecting an item in accountGroupTable
            1.  Enable or Disable Delete & Modify Buttons
            2.  Set up content of account Table
        */
        accountGroupTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<AccountGroup>(){

            @Override
            public void changed(ObservableValue<? extends AccountGroup> observable, AccountGroup oldValue, AccountGroup newValue)
            {
                if(newValue == null)
                    return;
               accountTable.setItems(newValue.getChildAccounts());
               if(newValue.isRemovable())
               {
                   deleteGroupButton.setDisable(false);
                   modifyGroupButton.setDisable(false);
               }
               else
               {
                   deleteGroupButton.setDisable(true);
                   modifyGroupButton.setDisable(true);
               }
               
            }
        });
        //...........................................................................................
        /*
        change listener for accountTable
        */
        accountTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<AbstractAccount>(){ 

            @Override
            public void changed(ObservableValue<? extends AbstractAccount> observable, AbstractAccount oldValue, AbstractAccount newValue) 
            {
                if(newValue == null)
                    return;
                if(newValue.isRemovable())
                {
                    deleteAccountButton.setDisable(false);
                    //modifyAccountButton.setDisable(false);
                }
               else
                {
                   deleteAccountButton.setDisable(true);
                  // modifyAccountButton.setDisable(true);
                }
                if(newValue instanceof AccountGroup)
                {
                   deleteAccountButton.setDisable(true);
                  modifyAccountButton.setDisable(true); 
                }
                else
                {
                   deleteAccountButton.setDisable(false);
                  modifyAccountButton.setDisable(false);  
                }
            }
        });
        //............................................................................
        newGroupButton.setOnAction(event -> {
            try {
                new ui.dialogs.NewAccountDialog(dataModel).start(primaryStage);
            } catch (SQLException ex) {
                Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        //................................................................................
        newAccountButton.setOnAction(event -> {
            try {
                new ui.dialogs.NewAccountGroupDialog(dataModel).start(primaryStage);
            } catch (SQLException ex) {
                Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        });
        //..........................................................................
        deleteGroupButton.setOnAction(event -> { 
        if(accountGroupTable.getSelectionModel().getSelectedItem() == null)
        {
            statusLabel.setText("NO SELECTED GROUP");
            return;
        }
        else
        {
            if(!accountGroupTable.getSelectionModel().getSelectedItem().isRemovable())
            {
                
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Selected Account is Non Removable");
                alert.setTitle("Error");
                alert.setContentText("Cannot remove System Account");
                alert.showAndWait();
                statusLabel.setText("SELECTED ACCOUNT IS NON REMOVABLE");
            }
            else
            {
                statusLabel.setText("CALLING DELETE ON : "+accountGroupTable.getSelectionModel().getSelectedItem().getAccountName());
                try {
                    dataModel.deleteAccountGroup(accountGroupTable.getSelectionModel().getSelectedItem());
                } catch (SQLException ex) {
                    Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        });
        //...................................................................
          deleteAccountButton.setOnAction(event -> { 
        if(accountTable.getSelectionModel().getSelectedItem() == null)
        {
            statusLabel.setText("NO SELECTED ACCOUNT");
            return;
        }
        else
        {
            if(!accountTable.getSelectionModel().getSelectedItem().isRemovable())
            {
                /*
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Selected Account is Non Removable");
                alert.setTitle("Error");
                alert.setContentText("Cannot remove System Account");
                alert.showAndWait();*/
                statusLabel.setText("SELECTED ACCOUNT IS NON REMOVABLE");
            }
            else
            {
                statusLabel.setText("CALLING DELETE ON : "+accountTable.getSelectionModel().getSelectedItem().getAccountName());
                if(accountTable.getSelectionModel().getSelectedItem() instanceof AccountGroup)
                    return;
                try {
                    dataModel.deleteAccount((Account)accountTable.getSelectionModel().getSelectedItem());
                } catch (SQLException ex) {
                    Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        });
        //.......................................................................
        modifyGroupButton.setOnAction(event -> {
        ui.dialogs.NewAccountGroupDialog dialog = new NewAccountGroupDialog(dataModel);
            try 
            {
                if(accountGroupTable.getSelectionModel().getSelectedItem() != null)
                {
                    dialog.start(primaryStage);
                    dialog.editAccountGroup(accountGroupTable.getSelectionModel().getSelectedItem());
                }
            } 
            catch (Exception ex) 
            {
                Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);
            }     
        });
        //........................................................................................
      
        //..................................................................................
        modifyAccountButton.setOnAction(event -> { 
        ui.dialogs.NewAccountDialog dialog = new NewAccountDialog(dataModel);
            try 
            {
                if(accountTable.getSelectionModel().getSelectedItem() != null)
                {
                dialog.start(primaryStage);
                dialog.updateAccount((Account)accountTable.getSelectionModel().getSelectedItem());
                }
            } 
            catch (Exception ex) 
            {
                Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        });
              
    }
    
    public static void main(String [] args)
    {
        launch(args);
    }
    
  
}
