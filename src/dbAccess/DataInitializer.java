
package dbAccess;

import java.io.File;
import java.sql.*;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import models.ACCOUNT_TYPE;
import models.AbstractAccount;
import models.Account;
import models.AccountGroup;
import models.Payee;
import models.ScheduledTransaction;
import models.TRANSACTION_FREQUENCY;
import models.Transaction;


public class DataInitializer 
{
    private Connection connection = null;
    private ObservableList<Account> accountList;
    private ObservableList<Transaction> transactionList;
    private ObservableList<AccountGroup> accountGroupList;
    private ObservableList<Payee> payeeList;
    private ObservableList<ScheduledTransaction> scheduledTransactionList;
    private final Connection databaseConnection;
    
    private Statement stmt = null;
    
    private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
   
    public DataInitializer(ObservableList<Account> accountList, ObservableList<Transaction> transactionList, ObservableList<AccountGroup> accountGroupList, ObservableList<Payee> payeeList, Connection dbConnection) throws SQLException
    {
    	this.accountGroupList = accountGroupList;
    	this.accountList =accountList;
    	this.transactionList = transactionList;
    	this.payeeList = payeeList;
        this.databaseConnection = dbConnection;
        try
        {
             //String path = System.getProperty("user.dir");
            // String dbPath = "jdbc:h2:" +path +"\\" +"databases" +"\\" +"database";
             //System.out.println("db path is : "+dbPath);
             connection = databaseConnection;
             System.out.println("DataInitializer : Successfully connected to databse at : " +new java.util.Date());
        }
        catch(Exception e)
        {
            System.out.println("DataInitializer : exception encountered : "+e);
        }
      
       stmt = connection.createStatement();
    }
    public void initializeData() throws SQLException
    {
        setUpPayees();
        
        setUpGroups();
        System.out.println("DataInitalizer : Successfully retrieved account groups : "+ new Date());
        
        setUpAccounts();
        System.out.println("DataInitalizer : Successfully retrieved accounts from database : "+ new Date());
        
        setUpHierarchy();
        
        setupTransactions();
        System.out.println("DataInitalizer : Successfully retrieved Transactions from database : "+ new Date());
        
        setUpScheduledTransactions();
        System.out.println("DataInitializer : Successfully retrieved Scheduled Transactions from  database : "+new Date());
        
      //  connection.close();
    }
    private void setUpPayees() throws SQLException    
    {
        String getPayeesSQL = "SELECT * FROM PAYEES";
        ResultSet rs = stmt.executeQuery(getPayeesSQL);
        while(rs.next())
        {
            Payee payee = new Payee(rs.getLong(1),rs.getString(2));
            payeeList.add(payee);
        }
        System.out.println("DataInitializer : Read into Payee Map : "+payeeList.size() +" entries");
    }
    private void setUpGroups() throws SQLException
    {
    	String sql = "SELECT * FROM  ACCOUNTGROUP";
    	ResultSet rs = stmt.executeQuery(sql);
    	while(rs.next())
    	{
            AccountGroup group = new AccountGroup(rs.getLong("ID"), rs.getString("NAME"));
            group.setRemovable(rs.getBoolean("REMOVABLE"));
            accountGroupList.add(group);
    	}
    	System.out.println("DataInitializer : Read "+accountGroupList.size() +" Group entries from database");
    	
    }
    private void setUpAccounts() throws SQLException
    {
        String getAccountsSQL = "SELECT * FROM ACCOUNT";
        
        ResultSet rs = stmt.executeQuery(getAccountsSQL);
        while(rs.next())
        {
        	Account account = new Account(rs.getLong("ID"), rs.getString("NAME"));
        	account.setAccountType(ACCOUNT_TYPE.getType(rs.getInt("CATEGORY")));
        	account.setAccountCode(rs.getString("CODE"));
        	account.setAccountDescription(rs.getString("DESCRIPTION"));
        	account.setAccountNotes(rs.getString("NOTES"));
        	accountList.add(account);
 
        }
        System.out.println("Data Initializer : read into acounts : "+accountList.size() +" entries");
        
    }
    private void setUpHierarchy() throws SQLException
    {
    	String sql = "SELECT ID, PARENTGROUP FROM ACCOUNT";
    	ResultSet rs = stmt.executeQuery(sql);
    	while(rs.next())
    	{
            Account account = getAccountByID(rs.getLong(1));
            AccountGroup group = getAccountGroupByID(rs.getLong(2));

            account.setParentGroup(group);
            group.addChildAccount(account);
    		//accountMap.get(new Long(rs.getInt(1))).setAccountGroup(accountGroupMap.get(new Long(rs.getLong(2))));
    	}
    	
    	sql = "SELECT ID, PARENTGROUP FROM ACCOUNTGROUP";
    	rs = stmt.executeQuery(sql);
    	while(rs.next())
    	{
    		AccountGroup child = getAccountGroupByID(rs.getLong(1));
    		AccountGroup parent = getAccountGroupByID(rs.getLong(2));
    		//System.out.println("child is : "+child +" ; parent is : "+parent);
    		if(parent!= child)
    		{
    			child.setParentGroup(parent);
    			parent.addChildAccount(child);
    		}
    	//	accountGroupMap.get(new Long(rs.getInt(1))).setParentAccount(accountGroupMap.get(new Long(rs.getLong(2))));
    	}
    	
        for(Account a: accountList)
           a.updateFullName();
       
    }
    private void setupTransactions() throws SQLException
    {
       String getTransactionsSQL = "SELECT * FROM TRANSACTIONS";
       ResultSet rs = stmt.executeQuery(getTransactionsSQL);
        while(rs.next())
        {
            Transaction t = new Transaction(rs.getLong("ID"));
            Date date=null;
            LocalDate lDate;
            try{
             date = rs.getDate("TDATE");
                System.out.println("DataInitializer : read date : "+date);
                     
             lDate = LocalDate.of(date.getYear()+1900, date.getMonth()+1, date.getDate());
               // System.out.println("Date.getYear() returned : "+date.getYear());
               // System.out.println("date.getMonth() returned : "+date.getMonth());
               // System.out.println("date.getDay() returned : "+date.getDate());
               // System.out.println("DataInitializer : Local Date : "+lDate);
             t.setTransactionDate(lDate);}
            catch(DateTimeException ex)
            {
                System.out.println("DateTime Exception encountered");
                System.out.println("year : " +date.getYear());
                System.out.println("month : "+date.getMonth());
                System.out.println("day : "+date.getDay());
                System.exit(0);
            }
            t.setAmount(rs.getDouble("AMOUNT"));
            t.setFromAC(getAccountByID(rs.getLong("FROMAC")));
            t.setToAC(getAccountByID(rs.getLong("TOAC")));
            t.setMemo(rs.getString("MEMO"));
            t.setTransactionNum(rs.getString("NUM"));
            t.setPayee(getPayeeByID(rs.getLong("PAYEE")));           
            transactionList.add(t);
            t.getFromAC().addEntry(t);
            t.getToAC().addEntry(t); 
        }
        System.out.println("DataInitializer : Read into Transaction List : "+transactionList.size() +" entries");
    }
    
    private void setUpScheduledTransactions() throws SQLException
    {
        String sql = "SELECT * FROM SCHEDULEDTRANSACTIONS";
        ResultSet rs = stmt.executeQuery(sql);
        while(rs.next())
        {
            ScheduledTransaction st = new ScheduledTransaction(rs.getLong("ID"));
            st.setAmount(rs.getDouble(2));
            st.setFromAC(getAccountByID(rs.getLong(3)));
            st.setToAC(getAccountByID(rs.getLong(4)));
            st.setMemo(rs.getString(5));
            st.setTransactionNum(rs.getString(6));
            st.setPayee(getPayeeByID(rs.getLong(7)));
            
            Date date = rs.getDate(8);
            LocalDate startDate = LocalDate.of(date.getYear()+1900, date.getMonth()+1, date.getDate());
            st.setStartDate(startDate);
            
            date = rs.getDate(9);
            LocalDate endDate = null;
            if(date!=null)
                endDate = LocalDate.of(date.getYear()+1900, date.getMonth()+1, date.getDate());
            st.setEndDate(endDate);
            
            date = rs.getDate(10);
            LocalDate nextDueDate = null;
            if(date!=null)
                nextDueDate = LocalDate.of(date.getYear()+1900, date.getMonth()+1, date.getDate());
            st.setNextDueDate(nextDueDate);
            
            date = rs.getDate(11);
            LocalDate lastCommitDate = null;
            if(date!=null)
               lastCommitDate = LocalDate.of(date.getYear()+1900, date.getMonth()+1, date.getDate());
            st.setLastCommitDate(lastCommitDate);
            
            st.setTransactionFrequency(TRANSACTION_FREQUENCY.getFromIntValue(rs.getInt(12)));
            if(rs.getInt(13) == 0)
                st.setOnWeekDay(null);
            else
            st.setOnWeekDay(DayOfWeek.of(rs.getInt(13)));
            
            st.setOnDate(rs.getInt(14));
            scheduledTransactionList.add(st);
        }
        System.out.println("DataInitilizer : Retrived "+scheduledTransactionList.size() +" entries from database");
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
    private Transaction getTransactionByID(long id)
    {
        for(Transaction t : transactionList)
        {
            if(t.getTransactionID() == id)
                return t;
        }
        return null;
    }
    
   
 
    public static void main(String [] args) throws SQLException
    {
      //  DataInitializer di = new DataInitializer();
        //di.initializeMaps();
 
    }

    public void setScheduledTransactionList(ObservableList<ScheduledTransaction> scheduledTransactionList) 
    {
      this.scheduledTransactionList = scheduledTransactionList;
    }
  
}
