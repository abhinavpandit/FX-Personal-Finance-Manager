/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Random;
import javafx.beans.property.SimpleStringProperty;
import models.Account;
import models.DataModel;
import models.Payee;
import models.Transaction;

/**
 *
 * @author john
 */
public class PerformanceTest
{
    private DataModel dataModel;
    public PerformanceTest() throws SQLException 
    {
        System.out.println("Loading Data model at : "+ new Date());
      //  dataModel = new DataModel(new SimpleStringProperty());
        System.out.println("Finished loading dataModel at : "+new Date());
        
        int no_of_accounts = dataModel.getAccountList().size();
        int no_of_payees = dataModel.getPayeeList().size();
        Random rand = new Random();
        for(int i=0;i<10000;i++)
        {
            Account fromAc;
            Account toAc;
            
            fromAc = dataModel.getAccountList().get(rand.nextInt(no_of_accounts));
            toAc = dataModel.getAccountList().get(rand.nextInt(no_of_accounts));
            if(toAc == fromAc)
                continue;
            
            Payee p = dataModel.getPayeeList().get(rand.nextInt(no_of_payees));
            int year = 1972 +rand.nextInt(100);
            int month = rand.nextInt(12)+1;
            int day = rand.nextInt(25)+1;
            LocalDate date = LocalDate.of(year,month,day);
            Transaction t = Transaction.createTransaction(fromAc, toAc);
            t.setAmount(rand.nextDouble()*100000);
            t.setMemo("Memo No : "+new java.util.Date().toString());
            t.setTransactionDate(date);
            dataModel.addTransaction(t);
                System.out.println("added "+i+" th transaction to database");
        }
        
    }
    
    public static void main(String [] args) throws SQLException
    {
        new PerformanceTest();
    }
    
    
}
