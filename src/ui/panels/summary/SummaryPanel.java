/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.panels.summary;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Callback;
import javafx.util.Pair;
import javafx.util.converter.CurrencyStringConverter;
import models.ACCOUNT_TYPE;
import models.AbstractAccount;
import models.Account;
import models.AccountGroup;
import models.DataModel;
import models.Transaction;
import ui.models.AccountTreeModel;

/**
 *
 * @author john
 */
public class SummaryPanel
{
    private final DataModel dataModel;
    private final TreeTableView<AbstractAccount> treeTableView; 
    private GridPane mainGrid;
    AccountTreeModel accountTreeModel;

    private HBox quickStatsPanel;
    
    // constructor..............................................
    public SummaryPanel(DataModel dataModel)
    {
        this.dataModel = dataModel;
        treeTableView = new TreeTableView<>();
        setUpMainGrid();
    }
    
    //set up main grid pane ............................................
    //it will further initialize treeTable and change listeners
    private void setUpMainGrid()
    {
        
        Font.loadFont(this.getClass().getResource("res/coneria.ttf").toExternalForm(), 12);
        mainGrid= new GridPane();
        ColumnConstraints col1 = new ColumnConstraints(500, 600, Double.MAX_VALUE, Priority.ALWAYS, HPos.LEFT, true);
        RowConstraints row1 = new RowConstraints(300, 300, Double.MAX_VALUE, Priority.ALWAYS, VPos.TOP, true);
        RowConstraints row2 = new RowConstraints(150, 150, 150, Priority.NEVER, VPos.CENTER, true);
        mainGrid.getColumnConstraints().add(col1);
        mainGrid.getRowConstraints().addAll(row1,row2);
        mainGrid.setPadding(new Insets(2, 5, 3, 5));
        mainGrid.getStylesheets().add(this.getClass().getResource("summary.css").toExternalForm());
       mainGrid.setVgap(2);
        accountTreeModel= new AccountTreeModel(dataModel);
        treeTableView.setRoot(accountTreeModel.getTreeRootItem());
        setUpTreeTable();
        QuickStatisticsPanel quickStatisticsPanel = new QuickStatisticsPanel(dataModel);
        treeTableView.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        mainGrid.add(treeTableView, 0, 0);
        mainGrid.add(quickStatisticsPanel.getPanel(),0,1);
        initializeListener();
                
            
    }
    
   private void initializeListener()
   {
       dataModel.getAccountList().addListener(new ListChangeListener<Account>(){ 
        @Override
        public void onChanged(ListChangeListener.Change<? extends Account> c) 
        {
            System.out.println("SummaryPanel : onChanged() called on accountList");
            while(c.next());
            {
                accountTreeModel.refreshTree();
                treeTableView.setRoot(accountTreeModel.getTreeRootItem());
            }
        }   
       });
        dataModel.getAccountGroupList().addListener(new ListChangeListener<AccountGroup>(){ 
        @Override
        public void onChanged(ListChangeListener.Change<? extends AccountGroup> c) 
        {
            System.out.println("Summary Panel: onChanged() called on accountGroupList");
            while(c.next());
            {
                
                accountTreeModel.refreshTree();
                treeTableView.setRoot(accountTreeModel.getTreeRootItem());
            }   
        }
       });
   }
    
    private void setUpTreeTable()
    {
        TreeTableColumn<AbstractAccount,String> accountNameColumn = new TreeTableColumn<>("ACCOUNT NAME");
        accountNameColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("accountName"));
        accountNameColumn.setMinWidth(200);
        accountNameColumn.setSortable(false);
        
        TreeTableColumn<AbstractAccount,Integer> noOfEntriesColumn = new TreeTableColumn<>("No. of Entries");
        noOfEntriesColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("noOfEntries"));
        noOfEntriesColumn.setMinWidth(100);
        noOfEntriesColumn.setStyle("-fx-alignment: BASELINE_RIGHT ");
        noOfEntriesColumn.setSortable(false);
        
        TreeTableColumn<AbstractAccount,String> balanceColumn = new TreeTableColumn<>("BALANCE");
        //thirdColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("accountBalance"));
        balanceColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<AbstractAccount,String>, ObservableValue<String>>() 
        {
            @Override
            public ObservableValue<String> call(CellDataFeatures<AbstractAccount, String> param) 
            {
                DoubleProperty balanceProperty = param.getValue().getValue().accountBalanceProperty();
                StringProperty balanceStringProperty = new SimpleStringProperty();
                 CurrencyStringConverter converter = new CurrencyStringConverter(NumberFormat.getCurrencyInstance(new Locale("en","IN"))); 
                Bindings.bindBidirectional(balanceStringProperty, balanceProperty, converter);
                return balanceStringProperty;
            }
        });
        balanceColumn.setMinWidth(150);
        balanceColumn.setStyle("-fx-alignment: BASELINE_RIGHT ");
        balanceColumn.setSortable(false);
        
        TreeTableColumn<AbstractAccount,ACCOUNT_TYPE> typeOfAccountColumn = new TreeTableColumn<>("TYPE");
        typeOfAccountColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("accountType"));
        typeOfAccountColumn.setMinWidth(150);
        typeOfAccountColumn.setStyle("-fx-alignment: BASELINE_RIGHT ");
        typeOfAccountColumn.setSortable(false);
       
        TreeTableColumn<AbstractAccount,String> descriptionColumn = new TreeTableColumn<>("DESCRIPTION");
        descriptionColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("accountDescription"));
        descriptionColumn.setMinWidth(100);
        descriptionColumn.setMaxWidth(Double.MAX_VALUE);
        descriptionColumn.setSortable(false);
        
        treeTableView.setRowFactory(new Callback<TreeTableView<AbstractAccount>, TreeTableRow<AbstractAccount>>() 
        {
            @Override
            public TreeTableRow<AbstractAccount> call(TreeTableView<AbstractAccount> param) 
            {
                return new TreeTableRow<AbstractAccount>()
                {
                    @Override
                    protected void updateItem(AbstractAccount item, boolean empty)
                    { 
                        super.updateItem(item,empty);
                        if(item!=null)
                        {
                            if(item instanceof AccountGroup)
                            this.setStyle("-fx-background-color: ghostwhite;");
                        }
                        else
                            this.setStyle("-fx-background-color: ivory;");
                    }
                };
            }
        });
        
        treeTableView.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
        treeTableView.getColumns().addAll(accountNameColumn,noOfEntriesColumn,balanceColumn,typeOfAccountColumn,descriptionColumn);
        treeTableView.showRootProperty().set(false);
    }
    
   
    public TreeTableView<AbstractAccount> getTreeTableView()
    {
        return treeTableView;
    }
    public GridPane getMainGrid()
    {
        return mainGrid;
    }
    
}
