/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.panels.commons;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import models.AbstractAccount;
import models.DataModel;
import ui.dialogs.AccountManager;
import ui.dialogs.FullTransactionListDialog;
import ui.dialogs.NewAccountDialog;
import ui.dialogs.NewAccountGroupDialog;
import ui.dialogs.NewTransactionDialog;
import ui.dialogs.TransactionCalendar;
import utilities.NewFileWizard;

/**
 *
 * @author john
 */
public class MenuBarPanel 
{
        private MenuBar menuBar;
        
        private Menu fileMenu;
        MenuItem newMenuItem;;
        MenuItem openMenuItem;
        MenuItem backupMenuItem;
        MenuItem exitMenuItem;
        
        Menu editMenu;
        MenuItem findMenuItem;
        
   
        
        
        Menu transactionMenu;
        MenuItem addTransactionMenuItem;
        MenuItem allTransactionsMenuItem;
        MenuItem modifyTransactionMenuItem;
        MenuItem scheduleTransactionMenuItem;
        
        Menu accountMenu;
        MenuItem newAccountMenuItem;
        MenuItem newAccountDialogItem;
        MenuItem newAccountGroupItem;
        MenuItem accountManagerItem;
        
        Menu actionMenu;
        Menu reportsMenu;
        MenuItem accountStatmentMenuItem;
        
        Menu chartsMenu;
        Menu toolsMenu;
        MenuItem payeeManagerMenuItem;
        MenuItem transactionCalendarMenuItem;
        
        Stage mainStage;
        DataModel dataModel;
        TreeView<AbstractAccount> accountTree;
        
    
    public MenuBarPanel(Stage mainStage,DataModel dataModel)
    {
        this.mainStage = mainStage;
      
        this.dataModel = dataModel;
        menuBar = new MenuBar();
        //menuBar.getStylesheets().add(this.getClass().getResource("menuBar.css").toExternalForm());
        System.out.println("FLAG MENU>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        fileMenu = new Menu("File");
        newMenuItem = new MenuItem("New File");
        openMenuItem = new MenuItem("Open File");
        backupMenuItem = new MenuItem("Back Up File");
        exitMenuItem = new MenuItem("Exit");
        fileMenu.getItems().addAll(newMenuItem,openMenuItem,backupMenuItem,exitMenuItem);
        menuBar.getMenus().add(fileMenu);
  
        
        accountMenu = new Menu("Account");
        newAccountMenuItem = new MenuItem("New Account");
        newAccountDialogItem = new MenuItem("New Account Dialog");
        accountMenu.getItems().addAll(newAccountMenuItem);
        menuBar.getMenus().add(accountMenu);
        newAccountGroupItem = new MenuItem("New Account Group");
        accountManagerItem = new MenuItem("Account Manager");
        
        accountMenu.getItems().add(newAccountGroupItem);
        accountMenu.getItems().add(accountManagerItem);
        
        transactionMenu = new Menu("Transaction");
        addTransactionMenuItem = new MenuItem("Add Transaction");
        allTransactionsMenuItem = new MenuItem("All Transactions");
        modifyTransactionMenuItem = new MenuItem("Modify Transaction");
        scheduleTransactionMenuItem = new MenuItem("Schedule Transaction");
        transactionMenu.getItems().addAll(addTransactionMenuItem,allTransactionsMenuItem,modifyTransactionMenuItem,scheduleTransactionMenuItem);
        menuBar.getMenus().add(transactionMenu);
        
        reportsMenu = new Menu("Reports");
        accountStatmentMenuItem = new MenuItem("Account Statement");
        
        toolsMenu = new Menu("Tools");
        payeeManagerMenuItem = new MenuItem("Payee Manager");
        transactionCalendarMenuItem = new MenuItem("Transaction Calander");
        toolsMenu.getItems().add(payeeManagerMenuItem);
        toolsMenu.getItems().add(transactionCalendarMenuItem);
        
        menuBar.getMenus().add(toolsMenu);
        
        
        wireUpControls();
        
        
    }
    private  void wireUpControls()
    {
        newMenuItem.setOnAction(event ->{ 
            NewFileWizard newFileWizard = new NewFileWizard();
            try {
                newFileWizard.start(mainStage);
            } catch (Exception ex) {
                Logger.getLogger(MenuBarPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        
        });
        exitMenuItem.setOnAction(e -> {
         
                System.exit(0);
            
        });
        
        addTransactionMenuItem.setOnAction(event -> {  
        ui.dialogs.NewTransactionDialog tDialog = new NewTransactionDialog(dataModel);
            try {
                tDialog.start(mainStage);
            } catch (Exception ex) {
                Logger.getLogger(MenuBarPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        newAccountMenuItem.setOnAction(event -> {
        	ui.dialogs.NewAccountDialog newAccountDialog  = new NewAccountDialog(dataModel);
        	try {
				newAccountDialog.start(mainStage);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	
        });
        
        payeeManagerMenuItem.setOnAction(event -> { 
        	
        try {
                        ui.dialogs.PayeeManager payeeManager = new ui.dialogs.PayeeManager(dataModel);
                        payeeManager.start(mainStage);
                } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                }
        
           // new ui.dialogs.PayeeManagerDialog(dataModel).show();
        	
        	
        });
        newAccountGroupItem.setOnAction(event -> {
        
            ui.dialogs.NewAccountGroupDialog dialog;
                dialog = new NewAccountGroupDialog(dataModel);
           
            try {
                dialog.start(mainStage);
            } catch (Exception ex) {
                Logger.getLogger(MenuBarPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        });
        accountManagerItem.setOnAction(event -> {
            
        ui.dialogs.AccountManager accountManagerDialog = new AccountManager(dataModel);
            try {
                accountManagerDialog.start(mainStage);
            } catch (Exception ex) {
                Logger.getLogger(MenuBarPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            
           // new ui.dialogs.AccountManagerX(dataModel).showAndWait();
        });
       
        transactionCalendarMenuItem.setOnAction(event -> { 
        ui.dialogs.TransactionCalendar transactionCalendarDialog = new TransactionCalendar(dataModel);
        
            try {
                transactionCalendarDialog.start(mainStage);
            } catch (Exception ex) {
                Logger.getLogger(MenuBarPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        });
        
        allTransactionsMenuItem.setOnAction(event ->{ 
        ui.dialogs.FullTransactionListDialog dialog = new FullTransactionListDialog(dataModel);
            try {
                dialog.start(mainStage);
            } catch (Exception ex) {
                Logger.getLogger(MenuBarPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        });
        
    }
    public MenuBar getMenuBar()
    {
        return menuBar;
    }
    
}
