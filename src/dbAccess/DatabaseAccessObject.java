
package dbAccess;

import java.sql.*;
import java.time.format.DateTimeFormatter;

import models.ACCOUNT_TYPE;
import models.AbstractAccount;
import models.Account;
import models.AccountGroup;
import models.Payee;
import models.ScheduledTransaction;
import models.TRANSACTION_FREQUENCY;
import models.Transaction;


public class DatabaseAccessObject 
{
    Connection connection = null;
    private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public DatabaseAccessObject() throws SQLException 
    {
             String path = System.getProperty("user.dir");
             String dbPath = "jdbc:h2:" +path +"\\" +"databases" +"\\" +"database";
             System.out.println("db path is : "+dbPath);
             connection = DriverManager.getConnection(dbPath);
    }
    // TRANSACTION FUNCTIONS.............................................
    public int addTransactionToDatabase(Transaction t) throws SQLException
    {
        String insertTransactionSQL = "INSERT INTO TRANSACTIONS VALUES(?,?,?,?,?,?,?,?)";
        PreparedStatement stmt = connection.prepareStatement(insertTransactionSQL);
        stmt.setLong(1, t.getTransactionID());
        stmt.setDate(2,new Date(t.getTransactionDate().getYear()-1900, t.getTransactionDate().getMonthValue()-1,t.getTransactionDate().getDayOfMonth()));
        stmt.setDouble(3, t.getAmount());
        stmt.setLong(4,t.getFromAC().getAccountID());
        stmt.setLong(5, t.getToAC().getAccountID());
        stmt.setString(6, t.getMemo());
        stmt.setString(7,"" +t.getTransactionNum());
        if(t.payeeProperty().getValue()!=null)
         stmt.setLong(8,t.payeeProperty().getValue().getPayeeID());
        else
            stmt.setNull(8, java.sql.Types.BIGINT);
        
        int executeUpdate = stmt.executeUpdate();
        
        System.out.println("DAO : Transaction : "+t.getTransactionID() +" > "+t.getMemo() +" > Rs. " +t.getAmount() +" addded to DB");
        return executeUpdate;
    }
    public void deleteTransaction(Transaction t) throws SQLException
    {
    	String sql = "DELETE FROM TRANSACTIONS WHERE ID ="+t.getTransactionID();
    	Statement stmt = connection.createStatement();
    	stmt.executeUpdate(sql);
         System.out.println("DAO : Transaction : "+t.getTransactionID() +" > "+t.getMemo() +" > Rs. " +t.getAmount() +" deleted from DB");
    }
    
    public void updateTransaction(Transaction t ) throws SQLException
    {
        String updateTransactionSQL = "UPDATE TRANSACTIONS SET TDATE = ?,AMOUNT = ?, FROMAC = ?,TOAC = ?,MEMO =?,NUM = ?,PAYEE = ? WHERE ID = ?";
        PreparedStatement stmt = connection.prepareStatement(updateTransactionSQL);
       // stmt.setLong(1, t.getTransactionID());
        stmt.setDate(1, new Date(t.getTransactionDate().getYear(), t.getTransactionDate().getMonthValue()-1,t.getTransactionDate().getDayOfMonth()));
        stmt.setDouble(2, t.getAmount());
        stmt.setLong(3,t.getFromAC().getAccountID());
        stmt.setLong(4, t.getToAC().getAccountID());
        stmt.setString(5, t.getMemo());
        stmt.setString(6,"" +t.getTransactionNum());
        if(t.payeeProperty().get()!=null)
         stmt.setLong(7,t.payeeProperty().get().getPayeeID());
        stmt.setLong(8, t.getTransactionID());
        int executeUpdate = stmt.executeUpdate();
       
    }
    
    // PAYEE FUNCTIONS..........................................................
    public void addPayee(Payee payee) throws SQLException
    {
      String addPayeeSQL = "INSERT INTO PAYEES VALUES(?,?)";
      PreparedStatement stmt = connection.prepareStatement(addPayeeSQL);
      stmt.setLong(1, payee.getPayeeID());
      stmt.setString(2, payee.getName());
      stmt.executeUpdate();
        System.out.println("DatabaseAccessObject : Payee : "+payee.getPayeeID() +" > "+payee.getName() +"added to DB");
              
      
    }
    public void deletePayee(Payee payee) throws SQLException
    {
      String deletePayeeSQL = "DELETE FROM PAYEES WHERE ID = ?";
      PreparedStatement stmt = connection.prepareStatement(deletePayeeSQL);
      stmt.setLong(1, payee.getPayeeID());
      stmt.executeUpdate();
      System.out.println("DatabaseAccessObject : Payee : "+payee.getPayeeID() +" > "+payee.getName() +"deleted from DB");
      
      
    }
    public void updatePayee(Payee payee) throws SQLException
    {
    	String sql = "UPDATE PAYEES SET name=? WHERE id=?";
    	PreparedStatement stmt = connection.prepareStatement(sql);
    	stmt.setString(1, payee.getName());
    	stmt.setLong(2, payee.getPayeeID());
    	stmt.executeUpdate();
    			
    }
    // ACCOUNT FUNCTIONS ..................................
    public int addAccountToDatabase(Account account) throws SQLException
    {
        String addAccountSQL = "INSERT INTO ACCOUNT VALUES(?,?,?,?,?,?,?)";
        PreparedStatement stmt = connection.prepareStatement(addAccountSQL);
        stmt.setLong(1, account.getAccountID());
        stmt.setString(2, account.getAccountName());
        stmt.setInt(3, account.getAccountType().getCode());
        stmt.setString(4, account.getAccountDescription());
        stmt.setString(5, account.getAccountNotes());
        stmt.setLong(6, account.getParentGroup().getAccountID());
        stmt.setString(7, account.getAccountCode());
        
        int executeUpdate = stmt.executeUpdate();
        return executeUpdate;
    }
    public void deleteAccountFromDatabase(Account account) throws SQLException
    {
        if(account.getRegisterEntries().size()!=0)
            return;
        else
        {
            String sql = "DELETE FROM ACCOUNT WHERE ID = "+account.getAccountID();
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);
        }
                    
    }
    public void updateAccount(Account account) throws SQLException 
    {
        String sql = "UPDATE ACCOUNT SET NAME = ?, CATEGORY = ?,DESCRIPTION = ?,NOTES = ?, PARENTGROUP = ?, CODE = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, account.getAccountName());
        stmt.setInt(2, account.getAccountType().getCode());
        stmt.setString(3, account.getAccountDescription());
        stmt.setString(4, account.getAccountNotes());
        stmt.setLong(5, account.getParentGroup().getAccountID());
        stmt.setString(6, account.getAccountCode());
        
        int executeUpdate = stmt.executeUpdate();
    }
   // ....... ACCOUNT GROUP FUNCTIONS...........................................
    public void addAccountGroupToDatabase(AccountGroup grp) throws SQLException
    {
       String sql = "INSERT INTO ACCOUNTGROUP values(?,?,?,?)";
       PreparedStatement stmt = connection.prepareStatement(sql);
       stmt.setLong(1, grp.getAccountID());
       stmt.setString(2, grp.getAccountName());
       stmt.setLong(3, grp.getParentGroup().getAccountID());
       stmt.setBoolean(4,grp.isRemovable());
       
        int executeUpdate = stmt.executeUpdate();
    }
    public void deleteAccountGroupFromDatabase(AccountGroup group) throws SQLException
    {
        if(group.getChildAccounts().size() == 0)
        {
            String sql = "DELETE FROM ACCOUNTGROUP WHERE ID = "+group.getAccountID();
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);
        }
    }
                
    public void updateAccountGroup(AccountGroup updatingAccountGroup) throws SQLException
    {
        String sql =  "UPDATE ACCOUNTGROUP set NAME = ?,PARENTGROUP = ? WHERE transactionID = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, updatingAccountGroup.getAccountName());
        stmt.setLong(2, updatingAccountGroup.getParentGroup().getAccountID());
        stmt.setLong(3, updatingAccountGroup.getAccountID());
        stmt.executeUpdate();
    }
    
    public static void main(String [] args)
    {
        
    }

    public void closeConnection() throws SQLException
    {
      connection.close();
    }

    public void addScheduledTransaction(ScheduledTransaction t) throws SQLException 
    {
      String sql = "INSERT INTO SCHEDULEDTRANSACTIONS VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      PreparedStatement ps = connection.prepareStatement(sql);
      ps.setLong(1, t.getTransactionID());
      ps.setDouble(2, t.getAmount());
      ps.setLong(3, t.getFromAC().getAccountID());
      ps.setLong(4, t.getToAC().getAccountID());
      ps.setString(5, t.getMemo());
      ps.setString(6, t.getTransactionNum());
      if(t.getPayee()!=null)
      ps.setLong(7, t.getPayee().getPayeeID());
      else
          ps.setNull(7, java.sql.Types.BIGINT);
      ps.setDate(8,new Date(t.getStartDate().getYear()-1900, t.getStartDate().getMonthValue()-1,t.getStartDate().getDayOfMonth()));
      if(t.getEndDate() !=null)
          ps.setDate(9,new Date(t.getEndDate().getYear()-1900, t.getEndDate().getMonthValue()-1,t.getEndDate().getDayOfMonth()));
      else
          ps.setDate(9, null);
      ps.setDate(10,new Date(t.getNextDueDate().getYear()-1900, t.getNextDueDate().getMonthValue()-1,t.getNextDueDate().getDayOfMonth()));
      ps.setDate(11, null);
      ps.setInt(12, TRANSACTION_FREQUENCY.getIntValue(t.getTransactionFrequency()));
      if(t.getOnWeekDay() == null)
          ps.setInt(13, 0);
      else
          ps.setInt(13, t.getOnWeekDay().getValue());
      ps.setInt(14, t.getOnDate());
      ps.executeUpdate();
        System.out.println("successfully added ScheduledTransaction to Database");
    }

    public void scheduledTransactionCommitted(ScheduledTransaction st) throws SQLException
    {
        String sql1 = "UPDATE SCHEDULEDTRANSACTIONS SET NEXTDUEDATE = ? WHERE ID = ?";
        String sql2 = "UPDATE SCHEDULEDTRANSACTIONS SET LASTCOMMITDATE = ? WHERE ID = ?";
        
        PreparedStatement ps = connection.prepareStatement(sql1);
        if(st.getNextDueDate() !=null)
            ps.setDate(1,new Date(st.getNextDueDate().getYear()-1900, st.getNextDueDate().getMonthValue()-1,st.getNextDueDate().getDayOfMonth()));
        else
            ps.setDate(1, null);
        ps.setLong(2, st.getTransactionID());
        ps.executeUpdate();
        
        ps = connection.prepareStatement(sql2);
        if(st.getLastCommitDate()!=null)
            ps.setDate(1,new Date(st.getLastCommitDate().getYear()-1900, st.getLastCommitDate().getMonthValue()-1,st.getLastCommitDate().getDayOfMonth()));
        else
            ps.setDate(1, null);
        ps.setLong(2, st.getTransactionID());
        ps.executeUpdate();
    }

    public void removeScheduledTransaction(ScheduledTransaction t) throws SQLException 
    {
        String sql = "DELETE FROM SCHEDULEDTRANSACTIONS WHERE ID = "+t.getTransactionID();
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(sql);
    }

   
    
}
