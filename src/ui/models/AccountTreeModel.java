/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.models;


import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import models.AbstractAccount;
import models.AccountGroup;
import models.DataModel;
import models.Account;

/**
 *
 * @author john
 */
public class AccountTreeModel 
{
    private TreeItem<AbstractAccount> treeRootItem;
    private DataModel dataModel;
    private AccountGroup rootGroup;
    
    // constructor.............................................................
    public AccountTreeModel(DataModel dataModel)
    {
    	this.dataModel = dataModel;
    	rootGroup = dataModel.getRootGroup();
        treeRootItem = generateTree(rootGroup);      
    }
    //generate Tree()
    // takes the root node as argument
    private TreeItem<AbstractAccount> generateTree(AbstractAccount thisAccount)
    {
    	TreeItem<AbstractAccount> thisAccountItem = new TreeItem<>(thisAccount);
        
        if(thisAccount instanceof Account)
        {
         // this item will be a leaf
        //	System.out.println("generate Tree called for (Account) : " +rootAccount.getAccountName());
            ImageView icon = new ImageView(this.getClass().getResource("res/m3.png").toExternalForm());
            icon.setFitHeight(16);
            icon.setFitWidth(16);
            thisAccountItem.setGraphic(icon);	
        }
        else //if(rootAccount instanceof AccountGroup)
        {
            ImageView icon = new ImageView(this.getClass().getResource("res/m1.png").toExternalForm());
            icon.setFitHeight(16);
            icon.setFitWidth(16);
            thisAccountItem.setGraphic(icon); 
            AccountGroup thisAccountGroup = (AccountGroup)thisAccount;
           // System.out.println("generate Tree called for (AccountGroup) : " +thisAccountGroup.getAccountName());
            for(int i=0;i<thisAccountGroup.getChildAccounts().size();i++)
            {
                //System.out.println("no. of children for "+thisAccountGroup.getAccountName() +"returned : "+thisAccountGroup.getChildAccounts().size());
                AbstractAccount childac = thisAccountGroup.getChildAccounts().get(i);
               // System.out.println("at i= " +i+"generating childac for : "+childac.getAccountName());
                TreeItem<AbstractAccount> child = generateTree(childac);
               // System.out.println("adding "+childac.getAccountName() + " to " +thisAccountGroup.getAccountName());
                thisAccountItem.getChildren().add(child);
            }
        	
        }
        thisAccountItem.setExpanded(true);
        
        return thisAccountItem;
    }
    
    public void refreshTree()
    {
        System.out.println("AccountTreeModel : refreshing tree");
       treeRootItem = generateTree(rootGroup);
    }
    
    public TreeItem<AbstractAccount> getTreeRootItem()
    {
        return treeRootItem;
    }
}
