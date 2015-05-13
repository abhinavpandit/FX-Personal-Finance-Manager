/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reports;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import models.Account;
import models.DataModel;
import models.RegisterEntry;
import models.RegisterEntryType;

/**
 *
 * @author HP
 */
public class AccountStatementHTMLReport
{
    private DataModel dataModel;
    private Account forAccount;
    private File file;
    private FileWriter fileWriter;
    private BufferedWriter bufferedWriter;
    private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public AccountStatementHTMLReport(DataModel dataModel) throws IOException 
    {
        this.dataModel = dataModel;
        file = new File("tempReport.txt");
        if(!file.exists())
            file.createNewFile();
        fileWriter = new FileWriter(file);
        bufferedWriter = new BufferedWriter(fileWriter);
       // generateReport(dataModel.getAccountList().get(0));
        
    }
    
    
    public File generateReport(Account forAccount) throws IOException
    {
        printHeader(forAccount);
        String format = "%-10s | %-25s | %-20s | %-30s | %10s | %10s | %10s";
        String formattedAccountHeader = String.format(format, "Date","Payee","Account","Memo","Debit","Credit","Balance");
       bufferedWriter.write(formattedAccountHeader);
       bufferedWriter.newLine();
       bufferedWriter.write("------------------------------------------------------------------------------------------------------------");
       bufferedWriter.newLine();
      // String dataFormat = "%-10s %-15s %-20s %-30s %-10.2f %-10.2f %-10.2f";
       for(RegisterEntry entry : forAccount.getRegisterEntries())
       {   
               String date = entry.getTransaction().getTransactionDate().format(dateFormat);
               String payee = "";
               if(entry.getTransaction().getPayee() != null)
                    payee = entry.getTransaction().getPayee().getName();
               String account = entry.getTransaction().getFromAC().getAccountName();
               if(entry.getEntryType() == RegisterEntryType.DEBIT)
                   account = entry.getTransaction().getToAC().getAccountName();
               String memo = entry.getTransaction().getMemo();
               String debit,credit;
               if(entry.getEntryType() == RegisterEntryType.CREDIT)
               {
                   debit = "";
                   //credit = String.format("%0.2f", entry.getTransaction().getAmount());
                   credit = ""+entry.getTransaction().getAmount();
               }
               else
               {
                   credit = "";
                  // debit = String.format("%0.2f", entry.getTransaction().getAmount());
                  debit = ""+entry.getTransaction().getAmount();
                  
               }
               
               //String balance = String.format("%0.2f", entry.getBalance());
                 String balance = "" +entry.getBalance();
               
              String entryText = String.format(format, date,payee,account,memo,debit,credit,balance);
              bufferedWriter.write(entryText);
              bufferedWriter.newLine();
     
       }
       
       bufferedWriter.write("--------------------------------------------------------------------------------------------------------------");
       
       
       bufferedWriter.close();
       return file;
    }
    
    private void printHeader(Account forAccount) throws IOException
    {
        System.out.println("writng header to file");
       String header = "Account Statement FOR \t\t"+forAccount.getFullName();
       bufferedWriter.write(header);
       bufferedWriter.newLine();
       
       String periodHeader = "\nFrom : \t"+new Date() +" TO \t" + new Date();
       bufferedWriter.write(periodHeader);
       bufferedWriter.newLine();
       
       
    }
    
    public static void main(String [] args)
    {
        
    }
    
    
}
