/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import dbAccess.DataInitializer;
import dbAccess.DatabaseAccessObject;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.Calendar;
import static java.util.Collections.list;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

/**
 *
 * @author john
 */
public class DataModel 
{
    private final ObservableList<Account> accountList = FXCollections.observableArrayList();
    private final ObservableList<Transaction> transactionList = FXCollections.observableArrayList();
    private final ObservableList<AccountGroup> accountGroupList = FXCollections.observableArrayList();
    private final ObservableList<Payee> payeeList =  FXCollections.observableArrayList();
    private final ObservableList<ScheduledTransaction> scheduledTransactionList = FXCollections.observableArrayList();
    DatabaseAccessObject dao;
    private final StringProperty status;
    private AccountGroup rootGroup;
    private final File databaseFile;
    
    public DataModel(File dbFile, StringProperty status) throws SQLException
    {
    	this.status = status;
        this.databaseFile = dbFile;
         String dbFileURL = "jdbc:h2:" +dbFile.getAbsolutePath() +";IFEXISTS=TRUE";
        Connection dbConnection = DriverManager.getConnection(dbFileURL);
        
        DataInitializer di = new DataInitializer(accountList,transactionList,accountGroupList,payeeList,dbConnection);
        di.setScheduledTransactionList(scheduledTransactionList);
        di.initializeData();
        
        dao = new DatabaseAccessObject(dbConnection);
        rootGroup = getAccountGroupByID(0);
        
    }
    public ObservableList<Account> getAccountList() {return accountList; }
    public ObservableList<AccountGroup> getAccountGroupList() {return accountGroupList; }
    public ObservableList<Transaction> getTransactionList() {return transactionList;}
    public ObservableList<Payee> getPayeeList() {return payeeList; }
    public ObservableList<ScheduledTransaction> getScheduledTransactionList() {return scheduledTransactionList;}
   
    //........ Transaction functions
    public void addTransaction(Transaction t) throws SQLException
    {
    	status.setValue("DataModel : ADDING TRANSACTION TO MAPS");
        transactionList.add(t);
        t.getFromAC().addEntry(t);
        t.getToAC().addEntry(t);
     
        status.setValue("DataModel : ADDING TRANSACTION TO DATABASE");
        dao.addTransactionToDatabase(t);  
        status.setValue("DataModel : TRANSACTION SUCCESSFULLY ADDED TO DATABASE");
    }
    public void deleteTransaction(Transaction t) throws SQLException
    {
    	status.setValue("DataModel : REMOVING TRANSACTION FROM MAPS");
    	transactionList.remove(t);
    	t.getFromAC().removeEntry(t);
    	t.getToAC().removeEntry(t);
    	status.setValue("DataModel : REMOVING TRANSACTION FROM DATABASE");
    	dao.deleteTransaction(t);
    	status.setValue("DataModel : SUCCESSFULLY REMOVED TRANSACTION FROM DATABASE");
    }
    public void updateTransaction(Transaction tnew,Transaction told) throws SQLException
    {
    	transactionList.remove(told);
    	told.getFromAC().removeEntry(told);
    	told.getToAC().removeEntry(told);
    	dao.deleteTransaction(told);
    
        transactionList.add(tnew);
        tnew.getFromAC().addEntry(tnew);
        tnew.getToAC().addEntry(tnew);
  
        dao.addTransactionToDatabase(tnew);  
        status.setValue("DataModel : TRANSACTION SUCCESSFULLY ADDED TO DATABASE");  
        status.setValue("UPDATED TRANSACTION : "+tnew.getTransactionID());
        
    }
    
    // ..........Account functions
    public void addAccount(Account account) throws SQLException
    {
    	status.setValue("DataModel : ADDING ACCOUNT "+account.getAccountName() +" TO MAPS");
    	status.setValue("DataModel : ADDING ACCOUNT "+account.getAccountName() +" TO DATABASE");
    	accountList.add(account);
        dao.addAccountToDatabase(account);
    	status.setValue("DataModel : SUCCESSFULLY ADDED ACCOUNT "+account.getAccountName() +" TO DATABASE");
    	
    }
    public void deleteAccount(Account account) throws SQLException
    {
        if(account.getRegisterEntries().size()!=0)
        {
            status.setValue(account.getAccountName() +" HAS " +account.getRegisterEntries().size() +" TRANSACTIONS: PLEASE EMPTY ACCOUNT BEFORE DELETING");
            return;
        }
        else
        {
            dao.deleteAccountFromDatabase(account);
            account.getParentGroup().removeChildAccount(account);
            accountList.remove(account);
            status.setValue("SUCCESSFULLY DELETED "+account.getAccountName()+" FROM DATABASE");
        }
    }
    public void updateAccount(Account editingAccount) throws SQLException
    {
        dao.updateAccount(editingAccount);
    }
    //...........................................
     

    
    public boolean addPayee(Payee payee) throws SQLException
    {
        if(payee == null)
            return false;
      
      payeeList.add(payee);
      dao.addPayee(payee);
      return true;
    }
    public boolean updatePayee(Payee payee, String newName) throws SQLException
    {
    	if(payee!=null)
    	{
    		payee.setName(newName);
    		dao.updatePayee(payee);
    	}
    	return true;
    }
    public void deletePayee(Payee p) throws SQLException
    {
        System.out.println("DataModel : calling DAO delete payee : "+p.getName());
        dao.deletePayee(p);
        System.out.println("DataModel : call to DAO to delete payee : "+p.getName()+" finished");
        payeeList.remove(p);
        System.out.println("DataModel : removed Payee : "+p.getName() +"from payeeList");
    }
    
    //................ account group functions
    public void addAccountGroup(AccountGroup grp) throws SQLException
    {
        accountGroupList.add(grp);
        dao.addAccountGroupToDatabase(grp);
        status.setValue("NEW GROUP ("+grp.getAccountID()+") ADDED TO DATABASE");
    }
    public void deleteAccountGroup(AccountGroup toBeDeleted) throws SQLException
    {
        if(toBeDeleted.getChildAccounts().size() == 0)
        {
            dao.deleteAccountGroupFromDatabase(toBeDeleted);
            toBeDeleted.getParentGroup().removeChildAccount(toBeDeleted);
            accountGroupList.remove(toBeDeleted);
            status.setValue("SUCCESSFULLY REMOVED GROUP : "+toBeDeleted.getAccountName()+" FROM DATABASE");
        }
        else
        {
            status.setValue("ACCOUNT GROUP HAS NON-ZERO CHILDREN");
        }
    }
    public void updateAccountGroup(AccountGroup updatingAccountGroup) throws SQLException
    {
        dao.updateAccountGroup(updatingAccountGroup);
    }
    
    //............ SCHEDULED TRANSACTION FUNCTIONS
    public void addScheduledTransaction(ScheduledTransaction t) throws SQLException
    {
        
        if(t.getTransactionFrequency() == TRANSACTION_FREQUENCY.ONCE)
        {
            t.setNextDueDate(t.getStartDate());
            t.setEndDate(t.getStartDate());
            t.setLastCommitDate(null);
        }
        else if(t.getTransactionFrequency() == TRANSACTION_FREQUENCY.DAILY)
        {
            t.setNextDueDate(t.getStartDate());
            t.setLastCommitDate(null);
        }
        else if(t.getTransactionFrequency() == TRANSACTION_FREQUENCY.WEEKLY)
        {
            DayOfWeek onWeekDay = t.getOnWeekDay();
            t.setNextDueDate(t.getStartDate());
            while(t.getNextDueDate().getDayOfWeek() != onWeekDay)
            {
                t.setNextDueDate(t.getNextDueDate().plusDays(1));
            }
            t.setLastCommitDate(null);
        }
        else if(t.getTransactionFrequency() == TRANSACTION_FREQUENCY.MONTHLY)
        {
            int day = t.getOnDate();
            Month month = t.getStartDate().getMonth();
            int year = t.getStartDate().getYear();
            if(t.getStartDate().getDayOfMonth() > day)
            {
                // if the day has passed in the current month
                month = month.plus(1);
                if(month == Month.JANUARY)
                    year = year + 1;
            }
            if(day>month.maxLength())
                day = month.maxLength();
            if(month ==Month.FEBRUARY)
                day = 28;
            t.setNextDueDate(LocalDate.of(year, month, day));
            t.setLastCommitDate(null);
            
        }
        System.out.println(".............. Scheduled Transaction Status........");
        t.printToConsole();
        scheduledTransactionList.add(t);
        dao.addScheduledTransaction(t);
        System.out.println("DataModel : calling dao to add transaction to DataBase");
    }
    public void removeScheduledTransaction(ScheduledTransaction t) throws SQLException
    {
        scheduledTransactionList.remove(t);
        dao.removeScheduledTransaction(t);
        status.setValue("ScheduledTransaction : {"+t.getTransactionID()+"} removed");
    }
    public void commitScheduledTransactions() throws SQLException
    {
        int count = 0;
        for(int i=0;i<scheduledTransactionList.size();i++)
        {
            Transaction transaction = scheduledTransactionList.get(i).commit();
            if(transaction!= null)
            {
                addTransaction(transaction);
                dao.scheduledTransactionCommitted(scheduledTransactionList.get(i));
                i--;
                count++;
            }
        }
        status.setValue("" +count +" Scheduled Transactions Committed");
    }
    
    // utility functions..............................................
    public AccountGroup getRootGroup()
    {
    	return rootGroup;
    }
       
    private Account getAccountByID(long id)
    {
        for(Account a : accountList)
        {
            if(a.getAccountID() == id)
                return a;
        }
        return null;
    }
    private AccountGroup getAccountGroupByID(long id)
    {
        for(AccountGroup a : accountGroupList)
        {
            if(a.getAccountID() == id)
                return a;
        }
        return null;
    }
    private Payee getPayeeByID(long id)
    {
        for(Payee p : payeeList)
        {
            if(p.getPayeeID() == id)
                return p;
        }
        return null;
    }
    
    public void closeConnection() throws SQLException
    {
        dao.closeConnection();
    }
   
    public static void main(String [] args) throws SQLException
    {
     // DataModel dm = new DataModel(new SimpleStringProperty());
    }

   
}
