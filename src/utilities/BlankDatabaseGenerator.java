/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package utilities;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author HP
 */
public class BlankDatabaseGenerator 
{
    private final File fxmFile;
    private Connection connection;
    private String accountSQL;
    private String payeeSQL;
    private String accountGroupSQL;
    private String transactionsSQL;
    private String scheduledTransactionSQL;

    public BlankDatabaseGenerator(File fxmFile, String dbName,String pass) throws SQLException 
    {
        this.fxmFile = fxmFile;
        String dbURL = "jdbc:h2:" +fxmFile.getParent() +"\\" +dbName;
        connection = DriverManager.getConnection(dbURL,"fxm", pass);
        generateSQLstrings();
        generateDatabase();
        
        
        connection.close();
        System.out.println("dbURL is being set to :  "+dbURL);
    }
    
    private void generateSQLstrings()
    {
        accountSQL = "CREATE TABLE ACCOUNT(" +
                                "ID BIGINT PRIMARY KEY," +
                                "NAME VARCHAR(255)," +
                                "CATEGORY INT NOT NULL," +
                                "DESCRIPTION VARCHAR(255)," +
                                "NOTES VARCHAR(255)," +
                                "PARENTGROUP BIGINT," +
                                "CODE VARCHAR(255)," +
                                "FOREIGN KEY(PARENTGROUP) REFERENCES ACCOUNTGROUP(ID)" +
                                ")";
        payeeSQL = "CREATE TABLE PAYEES(\n" +
                    "ID BIGINT PRIMARY KEY,\n" +
                    "NAME VARCHAR(255)\n" +
                    ")";
        accountGroupSQL = "CREATE  TABLE ACCOUNTGROUP(" +
                            "    ID BIGINT PRIMARY KEY," +
                            "    NAME VARCHAR(255)," +
                            "    PARENTGROUP BIGINT," +
                            "    REMOVABLE BOOLEAN" +
                            ")";
        transactionsSQL = "	CREATE TABLE TRANSACTIONS(" +
                            "    ID BIGINT PRIMARY KEY," +
                            "    TDATE DATE NOT NULL," +
                            "    AMOUNT DECIMAL," +
                            "    FROMAC BIGINT," +
                            "    TOAC BIGINT," +
                            "    MEMO VARCHAR(255)," +
                            "    NUM VARCHAR(255)," +
                            "    PAYEE BIGINT," +
                            " FOREIGN KEY(FROMAC) REFERENCES ACCOUNT(ID)," +
                            " FOREIGN KEY(TOAC) REFERENCES ACCOUNT(ID)" +
                            ")";
        scheduledTransactionSQL = "	CREATE  TABLE SCHEDULEDTRANSACTIONS(" +
                                    "    ID BIGINT PRIMARY KEY," +
                                    "    AMOUNT DECIMAL," +
                                    "    FROMAC BIGINT," +
                                    "    TOAC BIGINT," +
                                    "    MEMO VARCHAR(255)," +
                                    "    NUM VARCHAR(255)," +
                                    "    PAYEE BIGINT," +
                                    "    FROMDATE DATE," +
                                    "    TODATE DATE," +
                                    "    NEXTDUEDATE DATE," +
                                    "    LASTCOMMITDATE DATE," +
                                    "    FREQUENCY INT," +
                                    "    ONWEEKDAY INT," +
                                    "    ONDAYOFMONTH INT," +
                                    " FOREIGN KEY(FROMAC) REFERENCES ACCOUNT(ID)," +
                                    " FOREIGN KEY(TOAC) REFERENCES ACCOUNT(ID)" +
                                    ")";
    }

    private void generateDatabase() throws SQLException 
    {
        Statement statement = connection.createStatement();
        
        statement.executeUpdate(accountGroupSQL);
        statement.executeUpdate(accountSQL);
        statement.executeUpdate(payeeSQL);
        statement.executeUpdate(transactionsSQL);
        statement.executeUpdate(scheduledTransactionSQL);
        statement.executeUpdate("INSERT INTO ACCOUNTGROUP VALUES(0,'root',NULL,FALSE)");
        createBasicAccounts();
    }
    private void createBasicAccounts() throws SQLException
    {
        Statement statement = connection.createStatement();
        statement.executeUpdate("INSERT INTO ACCOUNTGROUP VALUES(11,'Assets',0,TRUE)");
        statement.executeUpdate("INSERT INTO ACCOUNTGROUP VALUES(21,'Liabilities',0,TRUE)");
        statement.executeUpdate("INSERT INTO ACCOUNTGROUP VALUES(31,'Expenses',0,TRUE)");
        statement.executeUpdate("INSERT INTO ACCOUNTGROUP VALUES(41,'Incomes',0,TRUE)");
        statement.executeUpdate("INSERT INTO ACCOUNTGROUP VALUES(51,'Equity',0,TRUE)");
         statement.executeUpdate("INSERT INTO ACCOUNTGROUP VALUES(61,'Shopping',31,TRUE)");
          statement.executeUpdate("INSERT INTO ACCOUNTGROUP VALUES(71,'Bills & Utilities',31,TRUE)");
        
        
        statement.executeUpdate("INSERT INTO ACCOUNT VALUES(101,'Savings Account',100,'Savings',NULL,11,NULL)");
        statement.executeUpdate("INSERT INTO ACCOUNT VALUES(102,'Cash In Hand',700,'Cash in Wallet',NULL,11,NULL)");
        statement.executeUpdate("INSERT INTO ACCOUNT VALUES(301,'Health & Medical',300,'Health & Medical Expenses',NULL,31,NULL)");
        statement.executeUpdate("INSERT INTO ACCOUNT VALUES(302,'Transportation',300,'Transportation(cabs, subway fares etc.)',NULL,31,NULL)");
        statement.executeUpdate("INSERT INTO ACCOUNT VALUES(303,'Electronics',300,'Electronics & Software Shopping',NULL,61,NULL)");
        statement.executeUpdate("INSERT INTO ACCOUNT VALUES(304,'Clothes',300,'Clothes Shopping',NULL,61,NULL)");
        statement.executeUpdate("INSERT INTO ACCOUNT VALUES(305,'Electricity & Water',300,'Electricity & Water Bills',NULL,71,NULL)");
        statement.executeUpdate("INSERT INTO ACCOUNT VALUES(306,'Rent',300,'House Rent',NULL,71,NULL)");
        statement.executeUpdate("INSERT INTO ACCOUNT VALUES(401,'Salary',200,'Income from Salary & Arrears',NULL,41,NULL)");
        statement.executeUpdate("INSERT INTO ACCOUNT VALUES(402,'Interest',200,'Interest Income',NULL,41,NULL)");
        statement.executeUpdate("INSERT INTO ACCOUNT VALUES(501,'Opening Balances',600,'Opening Balances',NULL,51,NULL)");
        
    }
    
}
