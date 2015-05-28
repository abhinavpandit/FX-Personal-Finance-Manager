/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.panels.register;

import javafx.print.PrinterJob;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.CurrencyStringConverter;
import javafx.util.converter.NumberStringConverter;
import models.ACCOUNT_TYPE;
import models.AbstractAccount;
import models.Account;
import models.AccountGroup;
import models.DataModel;
import models.Payee;
import models.RegisterEntry;
import models.RegisterEntryType;
import models.Transaction;
import ui.models.TransactionComparatorByDate;
import ui.panels.commons.AccountTreeView;

/**
 *
 * @author john
 */
public class RegisterPanel 
{
    private GridPane mainGrid;
    
    private final DataModel dataModel;
    
    private TableView<RegisterEntry> registerTableView;
    
    private final GridPane registerBottomPanel = new GridPane();
    private final Label currencyLabel = new Label("BALANCE : Rs. ");
    private final Label balanceLabel = new Label();
    private Label noOfEntriesLabel = new Label("No. of Entries");
    private Label noOfEntries = new Label();
    private Button filterTableButton = new Button("Filter");
    TransactionDataEntryPanelSlim slimDataEntryPanel;
    
    private AccountTreeView accountTree;

    private final ObjectProperty<AbstractAccount> selectedAccount = new SimpleObjectProperty<>();
    private final TransactionComparatorByDate trCompare = new TransactionComparatorByDate();
    
    private final StringConverter<Number> converter = new NumberStringConverter();
  
    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private StringProperty status;
    
    
    public RegisterPanel(DataModel dm, StringProperty status)
    {
        this.dataModel = dm;
        this.status = status;
        registerTableView = new TableView<>();
        accountTree = new AccountTreeView(dataModel);

        slimDataEntryPanel = new TransactionDataEntryPanelSlim(dm,status,selectedAccount);
        setUpMainGrid();

       
        setUpRegisterTable();
        setUpRegisterBottomPanel();

        
        registerTableView.setMaxHeight(Double.MAX_VALUE);
        registerTableView.setPrefHeight(500);
     

        mainGrid.add(accountTree.getTreeView(), 0, 0, 1, 3);  
        //mainGrid.add(accountTree.getTreeView(), 0, 1); 
        mainGrid.add(registerTableView,1,0,1,2);
       // mainGrid.add(registerBottomPanel, 1, 2);
        mainGrid.add(slimDataEntryPanel, 1, 2);
        
        GridPane.setValignment(slimDataEntryPanel, VPos.CENTER);
     // mainGrid.setBorder(new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
     
       
  
       initializeListeners();
                
      
    }
    private void setUpMainGrid()
    {
        mainGrid = new GridPane();
        
        ColumnConstraints column1 = new ColumnConstraints(200, 200, 300, Priority.NEVER, HPos.LEFT, true);
        ColumnConstraints column2 = new ColumnConstraints(700,700, Double.MAX_VALUE, Priority.ALWAYS, HPos.CENTER, true);
        mainGrid.getColumnConstraints().addAll(column1,column2);
        
        RowConstraints row1 = new RowConstraints(20, 20, 20, Priority.NEVER, VPos.TOP, true);
        RowConstraints row2 = new RowConstraints(500, 500, Double.MAX_VALUE, Priority.ALWAYS, VPos.TOP, true);
        RowConstraints row3 = new RowConstraints(20, 20, 20, Priority.NEVER, VPos.CENTER, true);
        mainGrid.getRowConstraints().addAll(row1,row2,row3);
        mainGrid.setHgap(5);
        mainGrid.setVgap(5);
        mainGrid.setPadding(new Insets(2, 5, 2, 5));
        mainGrid.getStylesheets().add(this.getClass().getResource("register.css").toExternalForm());
     //   mainGrid.setGridLinesVisible(true);
    }
    //********************** method to set up the Table View
    private void setUpRegisterTable()
    {
        TableColumn<RegisterEntry, Integer> serialColumn = new TableColumn<>("#");
        serialColumn.setSortable(false);
        serialColumn.setCellValueFactory(new PropertyValueFactory<>("serial"));
     
        //................ set up Transaction Date Column
        TableColumn<RegisterEntry,String> entryDateColumn = new TableColumn<>("Date");
        entryDateColumn.setMinWidth(100);
        entryDateColumn.setCellValueFactory(new Callback<CellDataFeatures<RegisterEntry, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(CellDataFeatures<RegisterEntry, String> param) {
               return new SimpleStringProperty(param.getValue().getTransaction().transactionDateProperty().getValue().format(dateFormat));
            }
        });
        
        // set up Transaction Num column
        TableColumn<RegisterEntry,String> numColumn = new TableColumn<>("Num");
        numColumn.setMinWidth(50);
        numColumn.setCellValueFactory(new Callback<CellDataFeatures<RegisterEntry, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(CellDataFeatures<RegisterEntry, String> param) {
                return param.getValue().getTransaction().transactionNumProperty();
            }
        });
        
        // set up Transaction Payee Column
        TableColumn<RegisterEntry,Payee> payeeColumn = new TableColumn<>("Payee");
        payeeColumn.setMinWidth(100);
        payeeColumn.setCellValueFactory(new Callback<CellDataFeatures<RegisterEntry, Payee>, ObservableValue<Payee>>() {

            @Override
            public ObservableValue<Payee> call(CellDataFeatures<RegisterEntry, Payee> param) {
                return param.getValue().getTransaction().payeeProperty();
            }
        });
        
        // set up account column 
        TableColumn<RegisterEntry,String> accountColumn = new TableColumn<>("Account ");
        accountColumn.setMinWidth(150);
        accountColumn.setCellValueFactory(new Callback<CellDataFeatures<RegisterEntry, String>, ObservableValue<String>>() 
        {
             public ObservableValue<String> call(CellDataFeatures<RegisterEntry, String> p) 
             {
              return p.getValue().getTransferAccount().accountNameProperty();
            }
         });
        
        // set up amount debited column
        TableColumn<RegisterEntry,String>  debitColumn = new TableColumn<>("Debit ");
        debitColumn.setMinWidth(75);
        debitColumn.setCellValueFactory(new Callback<CellDataFeatures<RegisterEntry, String>, ObservableValue<String>>() 
        {
             public ObservableValue<String> call(CellDataFeatures<RegisterEntry, String> p) 
             {
               if(p.getValue().getEntryType() == RegisterEntryType.DEBIT)
               {
                 return new SimpleStringProperty(String.format("%.2f",p.getValue().getTransaction().amountProperty().getValue()));
               }
               else
                   return null;
            }
         });
        debitColumn.setStyle("-fx-alignment: BASELINE_RIGHT ");
        
        // set up credit amount column
        TableColumn<RegisterEntry,String>  creditColumn = new TableColumn<>("CREDIT ");
        creditColumn.setMinWidth(75);
        creditColumn.setCellValueFactory(new Callback<CellDataFeatures<RegisterEntry, String>, ObservableValue<String>>() 
        {
             public ObservableValue<String> call(CellDataFeatures<RegisterEntry, String> p) 
             {
               if(p.getValue().getEntryType() == RegisterEntryType.CREDIT)
               {
                 return new SimpleStringProperty(String.format("%.2f",p.getValue().getTransaction().amountProperty().getValue()));
               }
               else
                   return null;
            }
         });
        creditColumn.setStyle("-fx-alignment: BASELINE_RIGHT ");
        
        // set up memo column
        TableColumn<RegisterEntry,String>  memoColumn = new TableColumn<>("Memo");
        memoColumn.setCellValueFactory(new Callback<CellDataFeatures<RegisterEntry, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(CellDataFeatures<RegisterEntry, String> param) {
                return param.getValue().getTransaction().memoProperty();
            }
        });
        memoColumn.setMinWidth(300);
        
       
        TableColumn<RegisterEntry,String> balanceColumn = new TableColumn<>("Balance");
        balanceColumn.setStyle("-fx-alignment: BASELINE_RIGHT;-fx-font-style: italic;");
        balanceColumn.setMinWidth(100);
        //balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));
        balanceColumn.setCellValueFactory(new Callback<CellDataFeatures<RegisterEntry, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(CellDataFeatures<RegisterEntry, String> param) 
            {
                DoubleProperty balanceProperty = param.getValue().balanceProperty();
                StringProperty balanceStringProperty = new SimpleStringProperty();
                 CurrencyStringConverter converter = new CurrencyStringConverter(NumberFormat.getCurrencyInstance(new Locale("en","IN"))); 
                Bindings.bindBidirectional(balanceStringProperty, balanceProperty, converter);
                return balanceStringProperty;
            }
        });

        
        registerTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        registerTableView.getColumns().add(serialColumn);
        registerTableView.getColumns().addAll(entryDateColumn,numColumn,payeeColumn,accountColumn,memoColumn,debitColumn,creditColumn);
       registerTableView.getColumns().add(balanceColumn);
        
    }
 
    private void setUpRegisterBottomPanel()
    {
        final Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
        registerBottomPanel.setHgap(10);
        ColumnConstraints col1 = new ColumnConstraints(120, 160, 160, Priority.NEVER, HPos.LEFT, true);
        ColumnConstraints col2 = new ColumnConstraints(200, 200, 200, Priority.NEVER, HPos.RIGHT, true);
        ColumnConstraints col3 = new ColumnConstraints(50,50, 50, Priority.NEVER, HPos.LEFT, true);
        ColumnConstraints col4 = new ColumnConstraints(100, 200, Double.MAX_VALUE, Priority.ALWAYS, HPos.RIGHT, true);
        ColumnConstraints col5 = new ColumnConstraints(100,150,150,Priority.NEVER,HPos.RIGHT,true);
        registerBottomPanel.getColumnConstraints().addAll(col1,col2,col3,col4,col5);
        
        balanceLabel.setFont(myFont);
        currencyLabel.setFont(myFont);
       
       
        noOfEntries.setAlignment(Pos.CENTER_LEFT);
        
        noOfEntries.setFont(myFont);

        registerBottomPanel.setMaxHeight(20);
       
        filterTableButton.setMinWidth(100);
        filterTableButton.setMaxHeight(16);
   
        
        noOfEntriesLabel.setText("No. of Entries : ");
        noOfEntriesLabel.setAlignment(Pos.CENTER_RIGHT);
        noOfEntriesLabel.setFont(myFont);
        
        noOfEntries.setAlignment(Pos.CENTER_RIGHT);
        noOfEntries.setFont(myFont);
        
        Button printButton = new Button("PRINT");
        printButton.setOnAction(event -> {
            PrinterJob pJob = PrinterJob.createPrinterJob();
            if(pJob!=null)
            {
                boolean printPage = pJob.printPage(registerTableView);
                if(printPage)
                {
                    System.out.println("successfully ended print Job");
                    pJob.endJob();
                }
            }
            
        });
    	
        registerBottomPanel.add(filterTableButton, 0, 0);
        registerBottomPanel.add(noOfEntriesLabel,1,0);
        registerBottomPanel.add(noOfEntries,2,0);
        registerBottomPanel.add(currencyLabel,3,0);
    	registerBottomPanel.add(balanceLabel,4,0);
        registerBottomPanel.add(printButton,5,0);
        
        balanceLabel.setBorder(Border.EMPTY);
           
    	registerBottomPanel.getStyleClass().add("register_bottom_panel");
        registerBottomPanel.setBackground(new Background(new BackgroundFill(Color.IVORY, CornerRadii.EMPTY, Insets.EMPTY)));
       // registerBottomPanel.setGridLinesVisible(true);
    }
    public GridPane getRegisterPanel()
    {
        return mainGrid;
    }
    
    public TreeView<AbstractAccount> getAccountTreeView()
    {
        return accountTree.getTreeView();
    }
   
    // initiliize all the change listeners
    private void initializeListeners()
    {
        //.................... on change to AccountList  >>>> refresh TreeView
        dataModel.getAccountList().addListener(new ListChangeListener<Account>()
        { 
            @Override
            public void onChanged(ListChangeListener.Change<? extends Account> c) 
            {
                while(c.next())
                {
                    accountTree.getAccountTreeModel().refreshTree();
                    accountTree.getTreeView().setRoot(accountTree.getAccountTreeModel().getTreeRootItem());
                } 
            }
        });
        //.................. on Change to Account Group List refresh TreeView
        dataModel.getAccountGroupList().addListener(new ListChangeListener<AccountGroup>(){ 
        @Override
        public void onChanged(ListChangeListener.Change<? extends AccountGroup> c) 
        {
            //System.out.println("onChanged() called on accountGroup");
             while(c.next());
            {
                accountTree.getAccountTreeModel().refreshTree();
                accountTree.getTreeView().setRoot(accountTree.getAccountTreeModel().getTreeRootItem());
            }      
        }
       });
        //.... on double clicking a table item change top panel to edit mode and populate this transaction
        registerTableView.setOnMouseClicked(event -> {
        if(event.getClickCount() == 2)
        {
            if(registerTableView.getSelectionModel().selectedItemProperty().getValue() !=null)
            {
             slimDataEntryPanel.changeToEditMode(registerTableView.getSelectionModel().selectedItemProperty().getValue().getTransaction());
            }
        }
        
        });
        
        // on selecting an item in side TreeView
       accountTree.getTreeView().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<? extends AbstractAccount>>(){
        @Override
        public void changed(ObservableValue<? extends TreeItem<? extends AbstractAccount>> observable, TreeItem<? extends AbstractAccount> oldValue, TreeItem<? extends AbstractAccount> newValue) 
        {   
            if(oldValue != null)
            {   Bindings.unbindBidirectional(balanceLabel.textProperty(),oldValue.getValue().accountBalanceProperty());}
            if(newValue == null)
                return;
            selectedAccount.setValue(newValue.getValue());
            // bind balance label to selected account's balance property
            Bindings.bindBidirectional(balanceLabel.textProperty(),selectedAccount.getValue().accountBalanceProperty(),converter);
            if(newValue.getValue() instanceof AccountGroup)
            {
                selectedAccount.setValue(null);
                registerTableView.setItems(null);
                return;
            }
            Account a = (Account)selectedAccount.getValue();
            registerTableView.setItems(a.getRegisterEntries());


            String creditText = ACCOUNT_TYPE.creditLabel(a.getAccountType());
            String debitText = ACCOUNT_TYPE.debitLabel(a.getAccountType());

            registerTableView.getColumns().get(6).setText(debitText);
            registerTableView.getColumns().get(7).setText(creditText);
            slimDataEntryPanel.creditField.setPromptText(creditText);
            slimDataEntryPanel.debitField.setPromptText(debitText);
            noOfEntries.setText(""+registerTableView.itemsProperty().get().size());
        }        
    });
    }
    
    // .................................set StatusPanel string property
    public void setStatusProperty(StringProperty staus)
    {
        this.status = status;
    }
    
    
}
