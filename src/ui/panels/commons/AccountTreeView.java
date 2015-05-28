
package ui.panels.commons;

import java.sql.SQLException;

import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import models.AbstractAccount;
import models.AccountGroup;
import models.DataModel;
import ui.models.AccountTreeModel;


public class AccountTreeView 
{
    private TreeItem<AbstractAccount> rootTreeItem;
    private AccountTreeModel accountTreeModel;
    private TreeView<AbstractAccount> treeView;
    private DataModel dataModel;
    ImageView icon1,icon2;
    
    public AccountTreeView(DataModel dataModel)
    {
        accountTreeModel = new AccountTreeModel(dataModel);
        rootTreeItem = accountTreeModel.getTreeRootItem();
        
        treeView = new TreeView<>(rootTreeItem);
        treeView.showRootProperty().set(false);
        icon1 = new ImageView(this.getClass().getResource("m1.png").toExternalForm());
        icon1.setFitHeight(16);
        icon1.setFitWidth(16);
        icon2 = new ImageView(this.getClass().getResource("m3.png").toExternalForm());
        icon2.setFitHeight(16);
        icon2.setFitWidth(16);
        
        treeView.setCellFactory(new Callback<TreeView<AbstractAccount>, TreeCell<AbstractAccount>>() 
        {
        @Override
        public TreeCell<AbstractAccount> call(TreeView<AbstractAccount> param) {
        return new TreeCell<AbstractAccount>()
        {
            @Override
            public void updateItem(AbstractAccount item, boolean empty)
            {
                super.updateItem(item, empty);
                if(empty)
                        setText(null);
                if(item!=null)
                {
                    //System.out.println("update items called with : "+item);
                    setText(item.getAccountName());       
                    //setGraphic(icon1);
                    getTreeItem().setGraphic(icon1);
                    if(item instanceof AccountGroup )
                    {
                        //System.out.println("item is : "+item.getAccountName() +" > setting icon1");
                        this.setStyle("-fx-font-weight : bolder;-fx-background-color : #9d4024;-fx-font-size: 12;-fx-text-fill:white;");
                        getTreeItem().setGraphic(icon1);
                        //System.out.println("ICON 1 : " +icon1.getImage().toString());                    
                    }
                     else
                    {
                        
                        //System.out.println("item is : "+item.getAccountName() +" > setting icon2");
                        this.setStyle("-fx-font-weight: bold;-fx-font-size: 12;");
                        getTreeItem().setGraphic(icon2);
                        //System.out.println("ICON 2 : " +icon2.getImage().toString());

                    }
                }
            }
        };
        }
    });
    }
    public TreeView<AbstractAccount> getTreeView()
    {
        return treeView;
    }
    public TreeItem<AbstractAccount> getRootTreeItem()
    {
        return rootTreeItem;
    }
    public void refreshTree()
    {
        System.out.println("AccountTreeView : refreshTree called");
        accountTreeModel.refreshTree();
        treeView.setRoot(accountTreeModel.getTreeRootItem());
    }
    public AccountTreeModel getAccountTreeModel()
    {
        return accountTreeModel;
    }
    public static void main(String [] args) throws SQLException
    {
        
        
        
    }
    
}
